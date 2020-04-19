package com.godarmed.core.starters.global.utils.excelUtil.impl;

import com.godarmed.core.starters.global.utils.excelUtil.IFileWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TxtWriter implements IFileWriter {
	private BufferedWriter writer = null;
	private FileWriter fileWriter = null;
	public TxtWriter() {
	}
	
	@Override
	public IFileWriter init(String name, List<String> heads, String filePath) {
		// TODO Auto-generated method stub
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		try {
			closeWriter();
			fileWriter = new FileWriter(file);
			writer = new BufferedWriter(fileWriter);
			writer.write(String.join(",", heads));
			writer.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeWriter();
		}
		return this;
	}

	@Override
	public IFileWriter init(String name, List<String> heads, String filePath, Integer fileSize) {
		// TODO Auto-generated method stub
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		try {
			closeWriter();
			fileWriter = new FileWriter(file);
			writer = new BufferedWriter(fileWriter);
			writer.write(String.join(",", heads));
			writer.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeWriter();
		}
		return this;
	}
	
	private void closeWriter() {
		try {
			if (writer != null) {
				writer.close();
			}
			if (fileWriter != null) {
				fileWriter.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void write(List<Object> values, int dataIndex) {
		// TODO Auto-generated method stub
		try {
			synchronized (writer) {
				writer.write(String.join(",", values.stream().map(item -> String.valueOf(item)).collect(Collectors.toList())));
				writer.newLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String flush() throws IOException {
		// TODO Auto-generated method stub
		writer.flush();
		return "SUCCESS";
	}

	@Override
	public boolean isFlushSuccess(String msg) {
		// TODO Auto-generated method stub
		return msg != null && msg.equals("SUCCESS");
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		closeWriter();
	}

}
