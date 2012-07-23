package com.cattsoft.coolsql.system.start;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.apache.velocity.app.Velocity;

import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.parse.xml.XMLException;
import com.cattsoft.coolsql.system.PropertyManage;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.system.SystemConstant;
import com.cattsoft.coolsql.system.Task;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * ����ϵͳ������Ϣ
 * @author kenny liu
 *
 */
public class LoadSystemPropertiesTask implements Task {

	/* (non-Javadoc)
	 * @see com.coolsql.system.Task#execute()
	 */
	public void execute() {
		File logDir=new File(SystemConstant.userPath+SystemConstant.separator+"log");
		if(!logDir.exists())
			logDir.mkdirs();
		try {
			Properties velocityProperty=new Properties();
			velocityProperty.load(LoadSystemPropertiesTask.class.getResourceAsStream("/com/coolsql/resource/velocity.properties"));
			String velocityLog=velocityProperty.getProperty("runtime.log");
			if(velocityLog==null)
				velocityLog="velocity.log";
			velocityLog=logDir.getPath()+SystemConstant.separator+velocityLog;
			velocityProperty.put("runtime.log", velocityLog);
			
			Velocity.init(velocityProperty);
		} catch (Exception e1) {
			LogProxy.errorReport("initing velocity failed!",e1);
		}
		System.setProperty("wizard.sidebar.image", "resource/image/wizard.png");
		Properties log4jproperty=new Properties();
		try
		{
			log4jproperty.load(LoadSystemPropertiesTask.class.getResourceAsStream("/com/coolsql/resource/log4j.properties"));
			String file=log4jproperty.getProperty("log4j.appender.errorlog.File");
			if(file==null)
				file="error.log";
			file=logDir.getPath()+SystemConstant.separator+file;
			log4jproperty.put("log4j.appender.errorlog.File", file);
			
			file=log4jproperty.getProperty("log4j.appender.sqlerror.File");
			if(file==null)
				file="sqlerror.log";
			file=logDir.getPath()+SystemConstant.separator+file;
			log4jproperty.put("log4j.appender.sqlerror.File", file);
			
			PropertyConfigurator.configure(log4jproperty);
		}catch(Exception e)
		{
			LogProxy.outputErrorLog(e);
			LogProxy.errorMessage("log4j init error:"+e.getMessage());
		}
		Setting.getInstance();
		
		try {
			PropertyManage.loadSystemProperty();
		} catch (XMLException e) {
			LogProxy.errorMessage(e.getMessage());
		}
		
	}

	/* (non-Javadoc)
	 * @see com.coolsql.system.Task#getDescribe()
	 */
	public String getDescribe() {
		
		return PublicResource.getString("system.launch.loadsystemproperties");
	}

	/* (non-Javadoc)
	 * @see com.coolsql.system.Task#getTaskLength()
	 */
	public int getTaskLength() {
		return 1;
	}

}
