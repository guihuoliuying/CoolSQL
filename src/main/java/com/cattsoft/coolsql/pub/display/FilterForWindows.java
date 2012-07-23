/*
 * �������� 2006-10-20
 */
package com.cattsoft.coolsql.pub.display;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author liu_xlin
 *�ù��������ڵ��ñ����ļ�ѡ��Ի���,ֻ����ѡ��jar��zip��׺���ļ�
 */
public class FilterForWindows implements FilenameFilter {
    private FileSelectFilter filter=null;
    public FilterForWindows()
    {
        this(null,null);
    }
    public FilterForWindows(String type)
    {
        this(type,null);
    }
    public FilterForWindows(String type,String des)
    {
        filter=new FileSelectFilter(type,des);
    }
    /* ���� Javadoc��
     * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
     */
    public boolean accept(File dir, String name) {
        return filter.accept(new File(dir,name));
    }

}
