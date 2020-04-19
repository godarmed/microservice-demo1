package com.godarmed.core.starters.global.utils.excelUtil.impl;


import com.godarmed.core.starters.global.utils.excelUtil.FileExportObserver;
import com.godarmed.core.starters.global.utils.excelUtil.entity.FileStatus;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultFileExportObserverImpl implements FileExportObserver {

    private AtomicBoolean isExport = new AtomicBoolean(false);

    @Override
    public void update(List<FileStatus> fileStatusList) {
        System.out.println("========================");
        boolean temp = true;
        for (FileStatus fileStatus : fileStatusList) {
            temp = temp && fileStatus.getIsExport().get();
        }
        if(temp == true){
            isExport.set(temp);
        }
        System.out.println(fileStatusList);
        System.out.println("========================");
    }
}
