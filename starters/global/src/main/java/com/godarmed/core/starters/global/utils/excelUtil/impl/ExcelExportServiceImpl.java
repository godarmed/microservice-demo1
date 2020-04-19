package com.godarmed.core.starters.global.utils.excelUtil.impl;

import com.godarmed.core.starters.global.utils.excelUtil.FileExportObserver;
import com.godarmed.core.starters.global.utils.excelUtil.FileExportService;
import com.godarmed.core.starters.global.utils.excelUtil.IFileWriter;
import com.godarmed.core.starters.global.utils.excelUtil.entity.ExcelExportProperties;
import com.godarmed.core.starters.global.utils.excelUtil.entity.FileStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
public class ExcelExportServiceImpl implements FileExportService {

	private static final Logger log = LogManager.getLogger(ExcelExportServiceImpl.class);

	@ApiModelProperty(name = "文件导出观察者列表", notes = "可选")
	private List<FileExportObserver> observers = new ArrayList<>();

	@ApiModelProperty(name = "文件导出配置")
	private ExcelExportProperties config = null;

	@ApiModelProperty(name = "导出文件使用的线程池对象")
	private ThreadPoolTaskExecutor taskExecutor = null;

	@ApiModelProperty(name = "导出文件使用的写出对象")
	private List<IFileWriter> fileWriters = new ArrayList<>();

	@ApiModelProperty(name = "导出的所有文件的状态")
	private List<FileStatus> fileStatusList = new ArrayList<>();

	@ApiModelProperty(name = "是否正在导出文件")
	private AtomicBoolean isExporting = new AtomicBoolean(false);


	public ExcelExportServiceImpl() {
	}
	
	public ExcelExportServiceImpl(FileExportObserver observer) {
		this();
		this.observers.add(observer);
	}

	public ExcelExportServiceImpl(Integer updateTime, FileExportObserver observer) {
		this(observer);
		this.config.setExportUpdateTime(updateTime);
	}

	public ExcelExportServiceImpl(List<FileExportObserver> observers) {
		this();
		this.observers.addAll(observers);
	}

	public ExcelExportServiceImpl(Integer updateTime, List<FileExportObserver> observers) {
		this(observers);
		this.config.setExportUpdateTime(updateTime);
	}

	/**
	 * 写出文件
	 * @param taskId		任务ID
	 * @param heads			表头
	 * @param values		写出的数据
	 * @param filePrefix	文件前缀
	 * @param path			文件写出路径
	 */
	@Override
	public void write(Long taskId, List<String> heads, List<List<Object>> values, String filePrefix, String path){
		//查看是否有文件正在导出
		if(isExporting.getAndSet(true)){
			throw new RuntimeException("有文件正在导出，请稍后再试");
		}
		//初始化导出配置
		initFileExportConfig(filePrefix, heads, path);
		//如果数据为空，则设置一行空数据
		if(values == null || values.size() == 0){
			List<Object> blankRow = new ArrayList<>();
			for (int j = 0; j < this.config.getHeads().size(); j++) {
				blankRow.add("");
			}
			values.add(blankRow);
		}
		//计算文件数量
		int fileNum = 1;
		if(values.size()>this.config.getExcelMaxLines()){
			fileNum = (int)Math.ceil((double)values.size()/this.config.getExcelMaxLines());
		}
		//创建文件状态对象List
		initExportFileStatus(taskId, fileNum,this.config.getExcelMaxLines(),values.size());
		//创建文件写出对象List
		initFileWriters(fileNum,config.getExcelMaxLines());
		//开始监控
		asyncUpdateDataBase(this.config.getExportUpdateTime());
		//开始导出
		Integer fileIndex = null;
		Integer rowNum = null;
		for (int i = 0; i < values.size(); i++) {
			if(values.size()<=this.config.getExcelMaxLines()){
				fileIndex = 0;
				rowNum = i + 1;
			}else{
				fileIndex = (int)Math.ceil((double)(i+1)/this.config.getExcelMaxLines()) - 1;
				rowNum = (i)%this.config.getExcelMaxLines() + 1;
			}
			write(values.get(i), fileIndex, rowNum);
		}
	}

	/**
	 * 写入单行数据
	 * @param values
	 * @param fileIndex
	 * @param rowNum
	 * @return
	 */
	private int write(List<Object> values, int fileIndex, Integer rowNum){
		//开始写入
		if (values != null) {
			taskExecutor.submit(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					simpleWrite(values, fileIndex, rowNum);
				}
			});
		}
		return rowNum;
	}

	/**
	 * 异步轮询传递状态给观察者们
	 */
	private void asyncUpdateDataBase(Integer exportUpdateTime) {
		final long startTime = System.currentTimeMillis();
		if (observers != null && observers.size() > 0) {
			taskExecutor.submit(new Runnable() {
				@Override
				public void run() {
					do{
						// TODO Auto-generated method stub
						try {
							Thread.sleep(exportUpdateTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						for (FileExportObserver observer : observers) {
							observer.update(fileStatusList);
						}
						log.debug("推送一次导出状态");
					}while(!acquireAllFileStatus() && System.currentTimeMillis() - startTime < 5*60*1000);
					for (FileExportObserver observer : observers) {
						observer.update(fileStatusList);
					}
				}
			});
		}
	}

	/**
	 * 写入单行数据
	 * @param values		行数据
	 * @param fileIndex		文件列表下表
	 * @param rowSize		行号
	 */
	private void simpleWrite(List<Object> values, int fileIndex, int rowSize) {
		try {
			if (values != null) {
				fileWriters.get(fileIndex).write(values, rowSize);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//单个文件进度加一
			addFileStatusRow(fileIndex);
			//更新整体进度
			updateFileStatusProcess(fileIndex);
		}
	}

	/**
	 * 初始化文件写出配置
	 * @param filePrefix	文件名前缀
	 * @param heads			表头
	 */
	private void initFileExportConfig(String filePrefix, List<String> heads, String path) {
		//判断路径的有效性并创建路径
		File paths = new File(path);
		if (!paths.exists() && !paths.isDirectory()) {
			if(!paths.mkdirs()){
				throw new RuntimeException("创建临时文件夹失败");
			}
		}
		//初始化导出配置
		ExcelExportProperties config = new ExcelExportProperties();
		config.setType("excel");
		config.setFileExt(getFileExt("excel"));
		config.setHeads(heads);
		config.setFilePrefix(filePrefix);
		config.setStorePath(path);
		this.config = config;
		this.taskExecutor = taskExecutor(config.getCoreWriteNumber());
	}

	/**
	 * 初始化文件写出对象
	 * @param number	要初始化的文件写出对象的数量
	 */
	private void initFileWriters(int number,int excelMaxLines) {
		for (int i = 0; i < number; i++) {
			this.fileWriters.add(getWriter(this.config.getType()).init(this.config.getFilePrefix(), this.config.getHeads(), getExcelFilePath(i), excelMaxLines));
		}
	}

	/**
	 * 初始化导出文件状态列表
	 * @param taskId		导出任务ID
	 * @param fileNumber	导出文件数量
	 * @param fileSize		导出文件大小
	 * @param dataNum		导出的Excel的总行数
	 */
	private void initExportFileStatus(long taskId, int fileNumber, int fileSize, int dataNum) {
		for (int i = 0; i < fileNumber; i++) {
			int totalRow = 0;
			if(dataNum <= fileSize){
				totalRow = dataNum;
			} else {
				if(dataNum % fileSize == 0){
					totalRow = fileSize;
				}else if(i == fileNumber - 1){
					totalRow = dataNum % fileSize;
				}else {
					totalRow = fileSize;
				}
			}
			FileStatus fileStatus = new FileStatus(taskId, this.config.getFilePrefix()+"_"+ (i + 1),this.config.getStorePath() + File.separator + this.config.getFilePrefix() + "_" + (i + 1) + this.config.getFileExt(), totalRow);
			this.fileStatusList.add(fileStatus);
		}
	}

	/**
	 * 获取导出文件对应的绝对路径
	 * @param index	文件列表下标
	 * @return
	 */
	private String getExcelFilePath(int index) {
		return this.fileStatusList.get(index).getPath();
	}

	/**
	 * 获取单个文件导出状态
	 * @param index	文件列表下标
	 * @return
	 */
	private boolean acquireFileStatus(int index) {
		if(fileStatusList.size()>index) {
			return fileStatusList.get(index).getIsExport().get();
		}
		return false;
	}

	/**
	 * 获取文件总体导出状态
	 * @return
	 */
	private boolean acquireAllFileStatus() {
		if(fileStatusList.size()>0) {
			boolean filesStatus = fileStatusList.get(0).getIsExport().get();
			for (FileStatus fileStatus : fileStatusList) {
				filesStatus = filesStatus && fileStatus.getIsExport().get();
			}
			return filesStatus;
		}
		return false;
	}

	/**
	 * 文件写入行数添加
	 * @param index	文件列表下标
	 * @return
	 */
	private void addFileStatusRow(int index) {
		fileStatusList.get(index).getCurrentRowNum().addAndGet(1);
	}

	/**
	 * 更新单个文件导出状态
	 * @param index	文件列表下标
	 * @return
	 */
	private int updateFileStatusProcess(int index) {
		if(fileStatusList.size()>index){
			//更新行数
			int currentRowNum = fileStatusList.get(index).getCurrentRowNum().get();
			int totalRowNum = fileStatusList.get(index).getTotalRowNum();
			fileStatusList.get(index).getExportProcess().set((int)(((double)currentRowNum/totalRowNum)*100));
			//单个文件完成后写出
			if(fileStatusList.get(index).getCurrentRowNum().get() >= fileStatusList.get(index).getTotalRowNum()
					&& !fileStatusList.get(index).getIsExport().get()){
				try {
					fileWriters.get(index).flush();
					fileStatusList.get(index).getIsExport().set(true);
				} catch (IOException e) {
					log.error("任务[{}]的第[{}]文件写出失败，失败原因[{}]",fileStatusList.get(index).getTaskId(),index+1,e.getLocalizedMessage());
				} finally {
					fileWriters.get(index).close();
				}
			}
			//所有文件完成后清空线程池
			if(acquireAllFileStatus()){
				taskExecutor.destroy();
			}
			return fileStatusList.get(index).getExportProcess().get();
		}
		return 0;
	}

	/**
	 * 获取对应文件扩展名
	 * @param type
	 * @return
	 */
	private String getFileExt(String type) {
		switch (type) {
			case "excel":
				return ".xlsx";
			case "txt":
				return ".txt";
			case "csv":
				return ".csv";
			default:
				return ".xlsx";
		}
	}

	/**
	 * 获取对应文件写出对象
	 * @param type
	 * @return
	 */
	private IFileWriter getWriter(String type) {
		switch (type) {
			case "excel":
				return new ExcelFileWriter();
			case "txt":
				return new TxtWriter();
			case "csv":
				return new ExcelFileWriter();
			default:
				return new TxtWriter();
		}
	}

	/**
	 * 获取线程池对象
	 * @return
	 */
	public ThreadPoolTaskExecutor taskExecutor(int coreWriteNumber) {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.initialize();
		if(coreWriteNumber>50){
			executor.setCorePoolSize(50);
		}else{
			executor.setCorePoolSize(coreWriteNumber);
		}
		executor.setMaxPoolSize(50);
		executor.setQueueCapacity(10000);
		executor.setKeepAliveSeconds(60);
		executor.setThreadNamePrefix("taskExecutor-file-export-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.setWaitForTasksToCompleteOnShutdown(true);
		return executor;
	}

	/**
	 * 测试导出效果
	 * @param args
	 */
	public static void main(String[] args) {
		//线程池
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.initialize();
		executor.setCorePoolSize(20);
		executor.setMaxPoolSize(50);
		executor.setQueueCapacity(10000);
		executor.setKeepAliveSeconds(60);
		executor.setThreadNamePrefix("taskExecutor-file-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.setWaitForTasksToCompleteOnShutdown(true);

		//规定文件名中时间格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String currentTime= sdf.format(new Date());

		//导出行数
		int num = 753513;

		Long node1 = System.currentTimeMillis();
		//获取表头
		List<String> headers = new ArrayList<>();
		headers.add("id");
		headers.add("ApproveTime");
		headers.add("CreateTime");
		headers.add("FactoryType");
		headers.add("name");
		headers.add("OperateType");
		headers.add("OrgCode");

		//填充数据
		List<List<List<Object>>> datas = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			List<List<Object>> rows = new ArrayList<>();
			for (int j = 0; j < num; j++) {
				List<Object> row = new ArrayList<>();
				row.add(j);
				row.add(new Date());
				row.add(new Date(System.currentTimeMillis()*2));
				row.add(123);
				row.add("中国");
				row.add("success");
				row.add(i);
				rows.add(row);
			}
			datas.add(rows);
		}

		Long node2 = System.currentTimeMillis();
		/*for (AtomicInteger i = new AtomicInteger(0); i.get() < datas.size(); i.incrementAndGet()) {
			executor.submit(new Runnable() {
				@Override
				public void run() {
					FileExportService fileExportService = new FileExportServiceImpl("test" + currentTime, headers, "excel", "D:\\images\\temp" + File.separator + "test_" + i, new DefaultFileExportObserverImpl());
					fileExportService.write(1L, datas.get(i.get()));
				}
			});

		}*/
		FileExportService fileExportService = new ExcelExportServiceImpl(new DefaultFileExportObserverImpl());
		fileExportService.write(1L, headers, datas.get(0),"test" + currentTime, "D:\\images\\temp" + File.separator + "test_"+currentTime);
		Long node3 = System.currentTimeMillis();
		System.out.println("查询耗时"+(node2-node1)+",导出耗时"+(node3-node2));
		log.info("查询耗时[{}],导出耗时[{}]",node2-node1,node3-node2);
	}
}
