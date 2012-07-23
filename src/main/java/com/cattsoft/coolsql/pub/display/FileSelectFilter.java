/*
 * �������� 2006-6-2
 *
 */
package com.cattsoft.coolsql.pub.display;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * @author liu_xlin
 *�ļ����͹���
 */
public class FileSelectFilter extends FileFilter {

	private String description=null;
	private String[] fileType=null;
	public FileSelectFilter()
	{
        this((String)null,null);
	}
	public FileSelectFilter(String type)
	{
		this(type,null);
	}
	public FileSelectFilter(String type,String des)
	{
		if(des==null||des.trim().equals(""))
		{
			description="";
		}else
		{
			description=des;
		}
		fileType=new String[1];
		if(type==null||type.trim().equals(""))
		{
			fileType[0]="";
		}else
		{
			fileType[0]=type;
		}	
	}
	public FileSelectFilter(String[] type,String des)
	{
		if(des==null||des.trim().equals(""))
		{
			description="";
		}else
		{
			description=des;
		}
		fileType=type;
	}
	/**
	 * eg:".class"  ".exe"
	 */
	public boolean accept(File f) {
		if(fileType==null||fileType.length<1)
		{
			return true;
		}
		if(f==null)
			return false;
		if(isContain(getFileType(f))||getFileType(f).equals("/@\\"))
			return true;
		else
			return false;    
	}
	/**
	 * ����ļ���׺���Ƿ���������ʾ���嵥֮��
	 * @param suffix  --�ļ���׺���磺.exe    .class
	 * @return  --true:������ʾ   false:��������ʾ
	 */
	private boolean isContain(String suffix)
	{
	    for(int i=0;i<fileType.length;i++)
	    {
	        if(fileType[i].equals(suffix))
	            return true;
	    }
	    return false;
	}
	/* ���� Javadoc��
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * ��ȡ�ļ��ĺ�׺(���ΪĿ¼������/@\)
	 * @param f
	 * @return
	 */
    public static String getFileType(File f)
    {
    	if(f.isDirectory())
    		return "/@\\";
    	String name=f.getName();
    	name=name.toLowerCase();
    	if(name.lastIndexOf(".")==-1)
    	  return "";
    	else
    	  return name.substring(name.lastIndexOf("."),name.length()).toLowerCase();
    }
	/**
	 * @param description Ҫ���õ� description��
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @param fileType Ҫ���õ� fileType��
	 */
	public void setFileType(String[] fileType) {
		this.fileType = fileType;
	}
}
