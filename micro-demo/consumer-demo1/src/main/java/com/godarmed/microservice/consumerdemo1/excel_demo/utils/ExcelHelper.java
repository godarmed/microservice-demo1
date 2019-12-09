package com.godarmed.microservice.consumerdemo1.excel_demo.utils;


import com.alibaba.excel.annotation.ExcelProperty;
import com.godarmed.core.starters.redis.RedisUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.NamedThreadLocal;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ExcelHelper {
    private static final ThreadLocal<Integer> ROW_INDEX = new NamedThreadLocal<>("当前行数");
    private static final ThreadLocal<Integer> COUNT = new NamedThreadLocal<>("总行数");
    private static final ThreadLocal<String> FILE_NAME = new NamedThreadLocal<>("导出文件名");
    private static final ThreadLocal<String> SHEET_NAME = new NamedThreadLocal<>("工作簿名称");
    private static final ThreadLocal<List<List<String>>> DATA_LIST = new NamedThreadLocal<>("导出数据");
    private static final ThreadLocal<String> REDIS_KEY = new NamedThreadLocal<>("用户锁");


    public int getRowIndex() {
        return ROW_INDEX.get();
    }

    public int getCount() {
        return COUNT.get();
    }

    /**
     * 生成Excel文件
     *
     * @param fileName  要生成的Excel文件名（可用绝对或相对路径）
     * @param sheetName 生成的Excel文件中的sheet名
     * @param dataList  要放入Excel文件的内容
     * @param redisKey  用来记录进度的redis键
     * @throws IOException
     */
    public ExcelHelper(@NotNull String fileName, @NotNull String sheetName, @NotNull List<List<String>> dataList, @NotNull String redisKey) {
        FILE_NAME.set(fileName);
        SHEET_NAME.set(sheetName);
        DATA_LIST.set(dataList);
        COUNT.set(dataList.size());
        REDIS_KEY.set(redisKey);
    }

    public void exportExcel(HttpServletResponse response) {
        try {
            if (ROW_INDEX.get() == null || ROW_INDEX.get() == 0) {
                generateWorkbook(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateWorkbook(HttpServletResponse response) throws IOException {
        RedisUtils redisUtils = null;
        try {
            redisUtils = new RedisUtils();
            Workbook wb = null;
            //相应类型，文件名称设置
            if (FILE_NAME.get().endsWith(".xlsx")) {
                wb = new XSSFWorkbook();
            } else if (FILE_NAME.get().endsWith(".xls")) {
                wb = new HSSFWorkbook();
            } else {
                FILE_NAME.set(FILE_NAME.get().concat(".xlsx"));
                wb = new XSSFWorkbook();
            }

            //遍历设置excel内容
            CellStyle cellStyle = wb.createCellStyle();
            Font font = wb.createFont();
            font.setColor(HSSFColor.RED.index);
            cellStyle.setFont(font);

            Sheet sheet = wb.createSheet(SHEET_NAME.get());
            ROW_INDEX.set(0);
            for (List<String> rowData : DATA_LIST.get()) {
                Row row = sheet.createRow(ROW_INDEX.get());
                int cellIndex = 0;
                for (String cellData : rowData) {
                    Cell cell = row.createCell(cellIndex);
                    cell.setCellValue(cellData);
                    cell.setCellStyle(cellStyle);
                    cellIndex++;
                }
                ROW_INDEX.set(ROW_INDEX.get() + 1);
                double process = 100*(double)ROW_INDEX.get() / COUNT.get();
                redisUtils.set(REDIS_KEY.get(), (int)(process), 10 * 60);
                //log.info("导出第[{}]行",ROW_INDEX.get());
            }

            //浏览器下载excel
            buildExcelDocument(FILE_NAME.get(),wb,response);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (redisUtils != null) {
                redisUtils.close();
            }
        }

    }

    //浏览器下载excel
    public void buildExcelDocument(String filename,Workbook workbook,HttpServletResponse response) throws Exception{
        OutputStream outputStream = null;
        try{
            response.reset();
            if(workbook instanceof HSSFWorkbook){
                //excel-2003
                response.setContentType("application/vnd.ms-excel");
            }else{
                //excel-2007+
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            }
            response.setHeader("Content-Disposition", "attachment;filename="+ filename);
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
        }finally{
            if(outputStream!=null){
                outputStream.close();
            }
        }
    }

    //表头转换为List<String>
    public static List<String> readTitleValue(Object obj) {
        List<String> values = new ArrayList<>();
        //得到class
        Class cls = obj.getClass();
        //得到所有属性
        Field[] fields = cls.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {//遍历
            //得到属性
            Field field = fields[i];
            //打开私有访问
            field.setAccessible(true);
            //获取属性
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            //获取表头值
            if(excelProperty!=null) {
                String title = excelProperty.value()[0];
                //一个个赋值
                values.add(String.valueOf(title));
            }
        }
        return values;
    }

    //对象属性转换为List<String>
    public static List<String> readAttributeValue(Object obj) {
        List<String> values = new ArrayList<>();
        //得到class
        Class cls = obj.getClass();
        //得到所有属性
        Field[] fields = cls.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {//遍历
            try {
                //得到属性
                Field field = fields[i];
                //打开私有访问
                field.setAccessible(true);
                //获取属性
                String name = field.getName();
                //获取属性值
                Object value = field.get(obj);
                //一个个赋值
                values.add(String.valueOf(value));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return values;
    }
}
