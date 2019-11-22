package com.godarmed.core.framework.authservice.UserTools.impl;

import com.godarmed.core.framework.authservice.UserTools.UserToolsService;
import com.godarmed.core.framework.authservice.UserTools.entity.DatabaseEntity;
import com.godarmed.core.framework.authservice.UserTools.entity.ExecuteSQLEntity;
import com.zaxxer.hikari.pool.ProxyResultSet;
import lombok.extern.log4j.Log4j2;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class UserToolsServiceImpl implements UserToolsService {
	/*final static String KEY = "sd5=f$%^$13t@:a4|}^&agh#45s4{>q6/rt-*";

	@Override
	public List<List<Object>> executeSQL(ExecuteSQLEntity executeSQL) throws Exception {
		if (!checkKey(executeSQL.getSecretKey(), executeSQL.getTimeType())) {
			throw new Exception("无权限操作！");
		}

		JdbcConfig jdbcConfig = new JdbcConfig();
		jdbcConfig.setJdbcUrl(executeSQL.getJdbcUrl());
		jdbcConfig.setDbUser(executeSQL.getDbUser());
		jdbcConfig.setPassword(executeSQL.getPassword());
		jdbcConfig.setUsePool("N");
        SourceProxy sourceProxy = null;
		try {
			SourceManager manager = SourceManager.getSourceManager();
			sourceProxy = manager.getSourceProxy(jdbcConfig);
	        ProxyResultSet result = sourceProxy.execute(executeSQL.getSql());
	        return result == null ? null : result.getResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			if (sourceProxy != null) {
				sourceProxy.close();
			}
		}
	}

	private boolean checkKey(String secretKey, String timeType) {
		Date now = new Date();
		String pattern = "yyyy-MM-dd HH:mm";
		if (timeType != null && timeType.equals("hour")) {
			pattern = "yyyy-MM-dd HH";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		String md5Str = MA.getMD5Str(KEY+"||"+dateFormat.format(now));
		if (md5Str != null && md5Str.equals(secretKey)) {
			return true;
		}
		return false;
	}

	@Override
	public String backupDatabase(DatabaseEntity databaseEntity) throws Exception {
		if (!checkKey(databaseEntity.getSecretKey(), databaseEntity.getTimeType())) {
			throw new Exception("无权限操作！");
		}
		databaseEntity = backupCheck(databaseEntity);
		UserInfo userInfo = new UserInfo();
        userInfo.setIp(databaseEntity.getIp());
        userInfo.setUsername(databaseEntity.getUser());
        userInfo.setPassword(databaseEntity.getPassword());
        SessionProxy sessionProxy = SessionFactory.getInstance().getCommandOperation(userInfo).getSessionProxy();
        ShellExecReturn shellExecReturn = sessionProxy.execReturn("mkdir "+databaseEntity.getSavePath(), 20 * 1000L);
        StringBuilder sb = new StringBuilder();
        sb.append(databaseEntity.getCommand());
        sb.append(" --default-character-set=utf8 ");
        sb.append(databaseEntity.getDatabaseName());
        sb.append(" > ");
        sb.append(databaseEntity.getSavePath());
        sb.append(databaseEntity.getFileName());
        log.info("backupDatabase:[{}]", sb.toString());
//        UserInfo userInfo = new UserInfo();
//        userInfo.setIp(databaseEntity.getIp());
//        userInfo.setUsername(databaseEntity.getUser());
//        userInfo.setPassword(databaseEntity.getPassword());
//        SessionProxy sessionProxy = SessionFactory.getInstance().getCommandOperation(userInfo).getSessionProxy();
        shellExecReturn = sessionProxy.execReturn(sb.toString(), 20 * 1000L);
        log.info("ShellExecReturn为[{}]",shellExecReturn);
        if (shellExecReturn.getStatus() == 0) {// 0 表示线程正常终止。
            return "数据库备份成功";
        }else{
            return shellExecReturn.getError().toString();
        }
	}
	
	private DatabaseEntity backupCheck(DatabaseEntity databaseEntity) {
      if (!databaseEntity.getSavePath().endsWith(File.separator)) {
          databaseEntity.setSavePath(databaseEntity.getSavePath() + File.separator);
      }
//      File saveFile = new File(databaseEntity.getSavePath());
//      //创建备份sql文件
//      if ((!saveFile.exists()) || (!saveFile.isDirectory())) {
//          saveFile.mkdirs();
//      }
      //后缀统一换成.sql结尾
      String fileName = databaseEntity.getFileName();
      int i = fileName.lastIndexOf('.');
      if (i != -1){
          databaseEntity.setFileName(fileName.substring(0,i)+".sql");
      }else{
          databaseEntity.setFileName(fileName+".sql");
      }
	return databaseEntity;
  }
	
	@Override
	public String restoreDatabase(DatabaseEntity databaseEntity) throws Exception{
		if (!checkKey(databaseEntity.getSecretKey(), databaseEntity.getTimeType())) {
			throw new Exception("无权限操作！");
		}
		databaseEntity = restoreCheck(databaseEntity);
        StringBuilder sb = new StringBuilder();
        //"mysql -h 192.168.25.129 -u root -p123456 --default-character-set=utf8 qinmei"
        sb.append(databaseEntity.getCommand());
        sb.append(" --default-character-set=utf8 ");
        sb.append(databaseEntity.getDatabaseName());
        sb.append(" < ");
        sb.append(databaseEntity.getSavePath());
        sb.append(databaseEntity.getFileName());
        log.info("restoreDatabase:[{}]", sb.toString());
        UserInfo userInfo = new UserInfo();
        userInfo.setIp(databaseEntity.getIp());
        userInfo.setUsername(databaseEntity.getUser());
        userInfo.setPassword(databaseEntity.getPassword());
        SessionProxy sessionProxy = SessionFactory.getInstance().getCommandOperation(userInfo).getSessionProxy();
        ShellExecReturn shellExecReturn = sessionProxy.execReturn(sb.toString(), 20 * 1000L);
        log.info("ShellExecReturn为[{}]",shellExecReturn);
        if (shellExecReturn.getStatus() == 0) {// 0 表示线程正常终止。
            return "数据库还原成功";
        }else{
            return shellExecReturn.getError().toString();
        }
	}
	
    private DatabaseEntity restoreCheck(DatabaseEntity databaseEntity){
        if (!databaseEntity.getSavePath().endsWith(File.separator)) {
            databaseEntity.setSavePath(databaseEntity.getSavePath() + File.separator);
        }
//        File sqlFile = new File(databaseEntity.getSavePath()+databaseEntity.getFileName());
//        if (!sqlFile.isFile()){
//            throw new RuntimeException(sqlFile.getPath()+"不存在!");
//        }
		return databaseEntity;
    }*/
}
