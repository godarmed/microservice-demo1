package com.godarmed.core.starters.global.utils;

import com.google.common.base.Strings;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class FileUtils {
    public FileUtils() {
    }

    public static BufferedWriter getFileHanlder(OutputStream stream, String charset) throws IOException {
        return new BufferedWriter(new OutputStreamWriter(stream, charset));
    }

    public static BufferedWriter getFileHanlder(String path, String charset) throws IOException {
        if (!Strings.isNullOrEmpty(path)) {
            String folder = path.substring(0, path.lastIndexOf(File.separator));
            File folderFile = new File(folder);
            if (!folderFile.exists()) {
                folderFile.mkdirs();
            }

            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }

            return getFileHanlder((OutputStream)(new FileOutputStream(file)), charset);
        } else {
            return null;
        }
    }

    public static BufferedWriter getFileHanlder(String path) throws IOException {
        return getFileHanlder(path, "utf-8");
    }

    public static BufferedReader getFileReader(String path, String charset) throws IOException {
        if (!Strings.isNullOrEmpty(path)) {
            File file = new File(path);
            if (!file.exists()) {
                throw new IOException("文件不存在");
            } else {
                return getFileReader((InputStream)(new FileInputStream(file)), charset);
            }
        } else {
            return null;
        }
    }

    public static BufferedReader getFileReader(String path) throws IOException {
        return getFileReader(path, "utf-8");
    }

    public static BufferedReader getFileReader(InputStream input, String charset) throws IOException {
        return new BufferedReader(new InputStreamReader(input, charset));
    }

    public static BufferedReader getFileReader(InputStream input) throws IOException {
        return new BufferedReader(new InputStreamReader(input, "utf-8"));
    }

    public static void main(String[] args) {
        try {
            BufferedReader reader = getFileReader("D:\\svg\\data\\graph\\1.txt");
            Throwable var2 = null;

            try {
                String line = null;

                while(!Strings.isNullOrEmpty(line = reader.readLine())) {
                    System.out.println(line);
                }
            } catch (Throwable var12) {
                var2 = var12;
                throw var12;
            } finally {
                if (reader != null) {
                    if (var2 != null) {
                        try {
                            reader.close();
                        } catch (Throwable var11) {
                            var2.addSuppressed(var11);
                        }
                    } else {
                        reader.close();
                    }
                }

            }
        } catch (IOException var14) {
            var14.printStackTrace();
        }

    }
}
