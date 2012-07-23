/*
 * �������� 2006-6-1
 *
 */
package com.cattsoft.coolsql.pub.loadlib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JOptionPane;

import com.cattsoft.coolsql.bookmarkBean.DriverInfo;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.FileUtil;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.system.SystemConstant;
import com.cattsoft.coolsql.view.log.LogProxy;


/**
 * Load resource from file or other libraries.
 * @author Kenny Liu
 */
public class LoadJar {
	/**
	 * Load properties from specified resource using the given ClassLoader. 
	 * @param path --path of resource file
	 * @param c --specify a ClassLoader to load resource.
	 */
	public static Properties loadResource(String path, Class<?> c)
	{
		ClassLoader cl=c!=null?c.getClassLoader():LoadJar.class.getClassLoader();
		InputStream input=cl.getResourceAsStream(path);
		Properties properties=new Properties();
		try {
			properties.load(input);
		} catch (Exception e) {
			LogProxy.errorLog(e.getMessage(), e);
		}
		return properties;
	}
	private static LoadJar loader = null;

	private LibLoader classLoader = null;

	private Properties libMap = null;

	private List<String> extraFiles;
	private LoadJar() {
		if (libMap == null) {
			readClasspath();
		}
		extraFiles=readExternalFiles();
	}

	public static LoadJar getInstance() {
		if (loader == null) {
			loader = new LoadJar();
		}
		return loader;
	}

	/**
	 * @see getClasses(File[], Class);
	 */
	public Class<?>[] getClasses(String[] files, Class<?> type) throws UnifyException {
		if (files == null || files.length == 0) {
			return new Class[0];
		}
		
		List<File> fileList = new ArrayList<File>();
		for (String s : files) {
			File f = new File(s);
			if (f.exists() && f.isFile()) {
				fileList.add(f);
			}
		}
		
		return getClasses(fileList.toArray(new File[fileList.size()]),type);
	}
	/**
	 * Retrieve all suitable classes which extend/implement class/interface(type) from specified files 
	 * @param files The libraries. 
	 * @param type the super class or interface.
	 * @return All suitable classes.
	 * @throws UnifyException 
	 */
	public Class<?>[] getClasses(File[] files, Class<?> type) throws UnifyException {
		if (files != null && files.length > 0) {
			List<Class<?>> list = new ArrayList<Class<?>>();
			getClassLoader().addURL(getLibURL(files));
			for (int i = 0; i < files.length; i++) {
				if (!files[i].exists())
					continue;
				JarFile jar = null;
				try {
					jar = new JarFile(files[i]);
				} catch (IOException e) {
				    throw new UnifyException(PublicResource
							.getString("jarfile.IOError")
							+ "\nFile path:" + files[i].getAbsolutePath());
				}
				list.addAll(searchClasses(jar, type));
			}
			if (list.size() < 1) {
				return new Class<?>[0];
			} else {
				return list.toArray(new Class<?>[list.size()]);
			}
		} else {
			return new Class[0];
		}
	}
	/**
	 * Retrieve JDBC drivers from specified files.
	 */
	public String[] getDriverName(File[] files) throws UnifyException {

		if (files != null && files.length > 0) {
			List<String> list = new ArrayList<String>();
			getClassLoader().addURL(getLibURL(files));
			for (int i = 0; i < files.length; i++) {
				if (!files[i].exists())
					continue;
				JarFile jar = null;
				try {
					jar = new JarFile(files[i]);
				} catch (IOException e) {
				    throw new UnifyException(PublicResource
							.getString("jarfile.IOError")
							+ "\nFile path:" + files[i].getAbsolutePath());
				}
				list.addAll(searchDriver(jar));
			}
			if (list.size() < 1) {
				return new String[0];
			} else {
				String str[] = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					str[i] = (String) list.get(i);
				}
				return str;
			}
		} else {
			return new String[0];
		}
	}
	/**
	 * ��ȡָ���ļ�����е������
	 * 
	 * @param files
	 * @return  ���û�����࣬����null
	 * @throws UnifyException
	 */
	public Map<String,List<String>> getDriverNameToFile(File[] files) throws UnifyException {

		if (files != null && files.length > 0) {
			getClassLoader().addURL(getLibURL(files));
			
			Map<String,List<String>> map=new HashMap<String,List<String>>();
			for (int i = 0; i < files.length; i++) {
				if (!files[i].exists())
					continue;
				JarFile jar = null;
				try {
					jar = new JarFile(files[i]);
				} catch (IOException e) {
				    throw new UnifyException(PublicResource
							.getString("jarfile.IOError")
							+ "\nFile path:" + files[i].getAbsolutePath());
				}
				List<String> drivers=searchDriver(jar);
				if(drivers.size()>0)
					map.put(files[i].getAbsolutePath(), drivers);
			}
			return map;
		} else {
			return new HashMap<String,List<String>>();
		}
	}
	/**
	 * ��ָ��jar�ļ��е��������뼯��List
	 * 
	 * @param list
	 * @param loader
	 * @param jar
	 * @return List --jar�ļ��е�driver�б����
	 */
	public List<String> searchDriver(JarFile jar) {
		Enumeration<JarEntry> en = jar.entries();
		List<String> drivers=new ArrayList<String>();
		while (en.hasMoreElements()) {
			JarEntry entry = (JarEntry) en.nextElement();
			String className = getClassNameFromFileName(entry.getName());
			Class<?> tmp = null;
			try {
				if (className == null || className.trim().equals("")) {
					continue;
				}
				tmp = isDriverClass(className);
			} catch (Throwable e) {
//				JOptionPane.showMessageDialog(GUIUtil.getTopFrame(), e+ "\n"
//						+ className, "error", 0);
				if(e instanceof NoClassDefFoundError)
					LogProxy.getProxy().error(e);

				continue;
			}
			if (tmp != null) {
				drivers.add(tmp.getName());
			}
		}
		return drivers;
	}
	
	public List<Class<?>> searchClasses(JarFile jar, Class<?> type) {
		Enumeration<JarEntry> en = jar.entries();
		List<Class<?>> drivers = new ArrayList<Class<?>>();
		while (en.hasMoreElements()) {
			JarEntry entry = (JarEntry) en.nextElement();
			String className = getClassNameFromFileName(entry.getName());
			Class<?> tmp = null;
			try {
				if (className == null || className.trim().equals("")) {
					continue;
				}
				tmp = isAssignableClass(className, type);
			} catch (Throwable e) {
				if(e instanceof NoClassDefFoundError)
					LogProxy.getProxy().error(e);
				continue;
			}
			if (tmp != null) {
				drivers.add(tmp);
			}
		}
		return drivers;
	}
	
	/**
	 * Find all class that is assignable from "classtype",
	 * @param jar --library file
	 * @param classType  --class  which must be assignable from
	 * @return
	 */
	public List<Class<?>> searchAssignableClasses(JarFile jar,Class<?> classType) {
		Enumeration<JarEntry> en = jar.entries();
		List<Class<?>> classes=new ArrayList<Class<?>>();
		while (en.hasMoreElements()) {
			JarEntry entry = (JarEntry) en.nextElement();
			String className = getClassNameFromFileName(entry.getName());
			Class<?> tmp = null;
			try {
				if (className == null || className.trim().equals("")) {
					continue;
				}
				tmp = isAssignableClass(className, classType);
			} catch (Throwable e) {
//				JOptionPane.showMessageDialog(GUIUtil.getTopFrame(), e+ "\n"
//						+ className, "error", 0);
				if(e instanceof NoClassDefFoundError)
					LogProxy.getProxy().error(e);

				continue;
			}
			if (tmp != null) {
				classes.add(tmp);
			}
		}
		return classes;
	}
	/**
	 * �ж�ָ�����Ƿ��������
	 * 
	 * @param className
	 * @return
	 */
	public Class<?> isDriverClass(String className) {
		return isAssignableClass(className, java.sql.Driver.class);
	}
	public Class<?> isAssignableClass(String className,Class<?> classType) {
		Class<?> tmp = null;
		try {
			tmp = getClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			return null;
		}
		return classType.isAssignableFrom(tmp) ? tmp : null;
	}
	public Class<?>[] getAssignableClass(URL[] urls, Class<?> classType)
	{
		List<Class<?>> list=new ArrayList<Class<?>>();
		
		for (int i = 0; i < urls.length; ++i)
		{
			URL url = urls[i];
			File file = new File(url.getFile());
			if (!file.isDirectory() && file.exists() && file.canRead())
			{
				JarFile jar = null;
				try {
					jar = new JarFile(file);
				} catch (IOException e) {
					LogProxy.errorReport(PublicResource
							.getString("jarfile.IOError")
							+ "\nFile path:" + file.getAbsolutePath(),e);
				}
				list.addAll(searchAssignableClasses(jar, classType));
			}
		}
		return (Class[])list.toArray(new Class[list.size()]);
	}
	/**
	 * Retrive Class by drivername and resource url
	 * 
	 */
	public Class<?> getDriverClass(String className, URL[] url)
			throws UnifyException {
		getClassLoader().addURL(url);
		return getDriverClass(className);
	}

	/**
	 * Retrieve Class by drivername
	 */
	public Class<?> getDriverClass(String className) {
		try {
			return getClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(GUIUtil.getMainFrame(), PublicResource
					.getString("class.notFound")+className, "loadError", 0);
			return null;
		}
	}
	/**
	 * Retrieve class by classname
	 */
    public Class<?> getClassByName(String className) throws ClassNotFoundException
    {
        return getClassLoader().loadClass(className);
    }
	/**
	 * Get all library path information that have been saved in file named
	 * '.classpath'
	 * 
	 * @throws UnifyException
	 */
	public void readClasspath() {
	    if(libMap!=null)
	        libMap.clear();
	    else
	        libMap = new Properties();
		InputStream input = null;
		try {
		    File file=new File(SystemConstant.driversInfo);
		    if(!file.exists())
		        return;
		    input = new FileInputStream(file);
			if (input != null)
				libMap.load(input);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(GUIUtil.getMainFrame(), PublicResource
					.getString("readClasspath.error")
					+ e.getMessage(), "IOError", 0);
		} finally {
			try {
				input.close();
			} catch (Exception e) {
			}
		}

	}

	/**
	 * ��������������Ϣ
	 *  
	 */
	public void writeClasspath() {
		FileOutputStream out = null;
		try {
		    GUIUtil.createDir(SystemConstant.driversInfo,false, false);
			out = new FileOutputStream(new File(SystemConstant.driversInfo));
			libMap.store(out, "Libraries which are required by application!");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, PublicResource
					.getString("classpath.saveerror"), "IOException", 0);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}

	}
	public void saveExternalFiles()
	{
		File extraFile=new File(SystemConstant.extraFiles);
		StringBuilder sb=new StringBuilder();
		for(String file:extraFiles)
		{
			sb.append(file).append("\n");
		}
		if(sb.length()>0)
		{
			sb.deleteCharAt(sb.length()-1);//Remove the last char:'\n'
		}
		
		FileUtil.writeSimpleTextToFile(sb.toString(), extraFile);
	}
	public List<String> readExternalFiles()
	{
		List<String> tmp= FileUtil.readMultiLineFile(new File(SystemConstant.extraFiles));
		if(tmp==null)
			return new ArrayList<String>();
		else
		{
			for(String str:tmp)
			{
				File file=new File(str);
				if(file.exists())
				{
					try {
						getClassLoader().addURL(file.toURI().toURL());
					} catch (MalformedURLException e) {
						LogProxy.errorReport(e);
					}
				}
			}
			return tmp;
		}
	}
	/**
	 * Get the classname from jar file
	 * 
	 * @param name
	 * @return
	 */
	private String getClassNameFromFileName(String name) {
		String result = null;
		if (name.endsWith(".class"))
			result = name.substring(0, name.length() - 6).replace('/', '.')
					.replace('\\', '.');
		else
			result = "";
		return result;
	}

	/**
	 * Get ClassLoader that loads extra libs.
	 */
	public LibLoader getClassLoader() {
		if (classLoader == null) {
			Enumeration<?> en = libMap.propertyNames();
			HashSet<File> list = new HashSet<File>();
			while (en.hasMoreElements()) {
				String name = (String) en.nextElement();
				File e = new File((String) libMap.get(name));
				if (!e.exists()) {
					libMap.remove(name);
				} else {
					list.add(e);
				}
			}
			if (list.size() < 1) {
				classLoader = new LibLoader(new URL[0]);
			} else
			{
				classLoader = new LibLoader(getLibURL((File[]) list
						.toArray(new File[list.size()])));
			}
		}
		return classLoader;
	}

	/**
	 * ��ȡ��������е�������ݿ���
	 * 
	 * @return
	 * @throws UnifyException
	 */
	public String[] getDriversInLib() throws UnifyException {
		Set<File> set = new HashSet<File>();
		Collection<Object> c = libMap.values();
		Iterator<Object> it = c.iterator();
		while (it.hasNext()) {
			File f = new File((String) it.next());
			if (f.exists())
				set.add(f);
		}
		return getDriverName((File[]) set.toArray());
	}

	public URL[] getLibURL(File[] files) {
		URL[] url = new URL[files.length];
		for (int i = 0; i < files.length; i++) {
			try {
				url[i] = files[i].toURI().toURL();
			} catch (MalformedURLException e) {
				continue;
			}
		}
		return url;
	}

	public String getLibPath(String className) {
		return (String) libMap.get(className);
	}

	/**
	 * load a library to jvm
	 * 
	 * @param url
	 */
	public void addURL(URL url) {
		getClassLoader().addURL(url);
	}
	/**
	 * load multiply libraries to jvm
	 * 
	 * @param url
	 */
	public void addURL(URL[] urls) {
		getClassLoader().addURL(urls);
	}
	/**
	 * ��������б?libMap��������µ���Ϣ
	 * 
	 * @param key
	 * @param value
	 */
	public void addClassPath(String key, String value) {
		libMap.put(key, value);
	}
	/**
	 * Remove the specified driver class name.
	 */
	public void removeDriver(String driverClass)
	{
		libMap.remove(driverClass);
	}
	public String[] getExtraFiles()
	{
		return extraFiles.toArray(new String[extraFiles.size()]);
	}
	/**
	 * Add an extra file into vm. 
	 * @param file
	 */
	public void addExtraFile(String file)
	{
		String trimValue=StringUtil.trim(file);
		if(trimValue.equals(""))
			return ;
		
		if(extraFiles.contains(trimValue))
			return;
		
		extraFiles.add(trimValue);
	}
	/**
	 * Reomve the specified extra file from extra file list.
	 * @param file --file that should be removed.
	 */
	public void removeExtraFile(String file)
	{
		String tmpValue=StringUtil.trim(file);
		extraFiles.remove(tmpValue);
	}
	/**
	 * Add the specified external files into list.
	 * @param files--file that should be added.
	 */
	public void addExtraFiles(String[] files)
	{
		if(files==null)
			return;
		
		for(String s:files)
		{
			addExtraFile(s);
		}
	}
	/**
	 * Add the specified external files into list.
	 * @param files--file that should be added.
	 */
	public void addExtraFiles(List<String> files)
	{
		if(files==null)
			return;
		for(String s:files)
		{
			addExtraFile(s);
		}
	}
	/**
	 * Return a array that contains all driver information .
	 */
	public DriverInfo[] getDriverInfos() {
		ArrayList<DriverInfo> list = new ArrayList<DriverInfo>();
		Set<Object> set = libMap.keySet();
		Iterator<Object> it = set.iterator();
		while (it.hasNext()) {
			String className = (String) it.next();
			list.add(new DriverInfo(className));
		}
		if (list.size() == 0)
			return new DriverInfo[0];
		else
		{
			DriverInfo[] tmp=new DriverInfo[list.size()];
			return (DriverInfo[]) (list.toArray(tmp));
		}
	}
}
