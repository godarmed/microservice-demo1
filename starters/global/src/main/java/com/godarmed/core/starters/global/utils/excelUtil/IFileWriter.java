package com.godarmed.core.starters.global.utils.excelUtil;

import java.io.IOException;
import java.util.List;

public interface IFileWriter {
	IFileWriter init(String name, List<String> heads, String filePath);

	IFileWriter init(String name, List<String> heads, String filePath, Integer fileSize);
	
	void write(List<Object> values, int dataIndex);
	
	String flush() throws IOException;
	
	boolean isFlushSuccess(String msg);

	void close();
}
