package com.godarmed.core.starters.global.utils.excelUtil.impl;

import com.godarmed.core.starters.global.utils.excelUtil.IFileWriter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class ExcelFileWriter implements IFileWriter {
	private static String DEFAULT_TIME_FORMAT = "yyyy-MM-dd";
	private Sheet sheet = null;
	private SXSSFWorkbook workbook = null;
	private String currentFilePath = null;
	private AtomicInteger count = new AtomicInteger(0);
	public ExcelFileWriter() {
	}

	/**
	 *	poi的SXSSFWorkbook的底层机制，新写入的行号>(已经写入的行数-rowAccessWindowSize)
	 *	故使用此方式初始化写出行超过5000行的文件时可能出现行缺失的情况（待解决）
	 */
	@Override
	public IFileWriter init(String name, List<String> heads, String filePath) {
		// TODO Auto-generated method stub
		this.currentFilePath = filePath;
		workbook = new SXSSFWorkbook(5000);
		sheet = workbook.createSheet(name);
		Row titleRow = (Row) sheet.createRow(0);
		for (int i = 0; i < heads.size(); i++) {
			Cell cell = titleRow.createCell(i);
			cell.setCellValue(heads.get(i));
		}
		count.set(0);
		return this;
	}

	//poi的SXSSFWorkbook的底层机制，新写入的行号>(已经写入的行数-rowAccessWindowSize)
	@Override
	public IFileWriter init(String name, List<String> heads, String filePath, Integer fileSize) {
		// TODO Auto-generated method stub
		this.currentFilePath = filePath;
		workbook = new SXSSFWorkbook(fileSize);
		sheet = workbook.createSheet(name);
		Row titleRow = (Row) sheet.createRow(0);
		for (int i = 0; i < heads.size(); i++) {
			Cell cell = titleRow.createCell(i);
			cell.setCellValue(heads.get(i));
		}
		count.set(0);
		return this;
	}

	@Override
	public void write(List<Object> values, int dataIndex) {
		// TODO Auto-generated method stub
		Row row = null;
		synchronized (sheet) {
			int rowNumber = count.incrementAndGet();
			row = sheet.createRow(dataIndex);
		}
		int i = 0;
		for (Object value: values) {
			if(value instanceof Date){
				SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
				String time= sdf.format((Date)value);
				row.createCell(i).setCellValue(time);
			}else{
				row.createCell(i).setCellValue(String.valueOf(value));
			}
			i++;
		}
	}

	@Override
	public String flush() throws IOException {
		// TODO Auto-generated method stub
		File file = new File(currentFilePath);
		File fileFolder = new File(currentFilePath.substring(0,currentFilePath.lastIndexOf(File.separator)));
		if (!fileFolder.exists()) {
			fileFolder.mkdirs();
		}
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return "SUCCESS";
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		try {
			if (workbook != null) {
				workbook.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean isFlushSuccess(String msg) {
		// TODO Auto-generated method stub
		return msg != null && msg.equals("SUCCESS");
	}

}
