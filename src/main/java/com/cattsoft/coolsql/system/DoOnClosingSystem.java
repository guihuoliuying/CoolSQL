/*
 * �������� 2006-11-9
 */
package com.cattsoft.coolsql.system;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.main.frame.MainFrame;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.loadlib.LoadJar;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.parse.xml.XMLBeanUtil;
import com.cattsoft.coolsql.pub.parse.xml.XMLConstant;
import com.cattsoft.coolsql.pub.parse.xml.XMLException;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.bookmarkview.RecentSQLManage;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin �ڹرճ���֮ǰ,����Ҫ�������Ϣ���д���,���屣�����Ϣ,�Լ�����˳�������е�ע��
 */
public class DoOnClosingSystem implements CloseSystem {
    private static DoOnClosingSystem system=null;
    
    private MainFrame main = null;

    private CloseProgressDialog progressDialog=null;
    
    public synchronized static DoOnClosingSystem getInstance()
    {
        if(system==null)
            system=new DoOnClosingSystem((MainFrame)GUIUtil.getMainFrame());
        return system;
    }
    private DoOnClosingSystem(MainFrame main) {
        this.main = main;
        
        /**
         * ��ʼ���رս�ȴ���
         */
        progressDialog=new CloseProgressDialog();
        progressDialog.setTasks(TaskFactory.getCloseTasks());         
        
    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.system.CloseSystem#close()
     */
    public void close() throws UnifyException {

        main.removeAll();
        main.dispose();
    }

    /*
     * 1���Ͽ�������ݿ����� 2��������ǩ��Ϣ 3����������Ϣ 4������ִ�е�sql��Ϣ 5������sql�༭������
     * 
     * @see com.coolsql.system.CloseSystem#save()
     */
    public void save() throws UnifyException {
//        disconnectAllDB();
//
//        saveBookmarkInfo();
//
//        /**
//         * ��������Ϣ
//         */
//        LoadJar.newInstance().writeClasspath();
//
//        try {
//            /**
//             * ����ִ�е�sql��Ϣ
//             */
//            saveRecentSQL();
//        } catch (XMLException e1) {
//            LogProxy.errorReport(e1);
//        } catch (Exception e) {
//            LogProxy.outputErrorLog(e);
//            LogProxy.errorReport(e);
//        }
//
//        /**
//         * ����sql�༭����
//         */
//        saveContentOfSQLEditor();
    }
    /**
     * �Ͽ�������ǩ������
     */
    public void disconnectAllDB()
    {
        BookmarkManage bm = BookmarkManage.getInstance();
        Iterator it = bm.getBookmarks().iterator();
        while (it.hasNext()) {
            Bookmark bookmark = (Bookmark) it.next();
            if (bookmark.isConnected()) {
                try {
                    bookmark.disconnect();
                } catch (SQLException e2) {
                    LogProxy.outputErrorLog(e2);
                }
            }
        }
    }
    /**
     * ������ǩ��Ϣ <bookmarks><bookmark></bookmark> <bookmark></bookmark>
     * </bookmarks>
     */
    public void saveBookmarkInfo()
    {
        XMLBeanUtil xml = new XMLBeanUtil();
        Document doc = new Document(xml.createRootElement("bookmarks"));
        Element element = doc.getRootElement();

        BookmarkManage bm = BookmarkManage.getInstance();
        Iterator it = bm.getBookmarks().iterator();
        while (it.hasNext()) {
            Bookmark bookmark = (Bookmark) it.next();
            try {
                element.addContent(xml.parseBean(bookmark, "bookmark"));
            } catch (XMLException e) {
                LogProxy.errorMessage("(" + bookmark.getAliasName() + ")"
                        + PublicResource.getString("save.bookmark.erroroccur")
                        + e.getMessage());
                LogProxy.outputErrorLog(e);
            }
        }
        File file = new File(SystemConstant.bookmarkInfo);
        try {
            xml.saveDocumentToFile(doc, file);
        } catch (XMLException e) {
            LogProxy.errorMessage(e.getMessage());
        } catch (Exception e) {
            LogProxy.errorReport(e);
        }
    }
    /**
     * �������ִ�е�sql��Ϣ�������ʽ���� <recentSQL><bookmark datatype> <sql datatype> </sql>
     * </bookmark> </recentSQL>
     * 
     * @throws XMLException
     */
    public void saveRecentSQL() throws XMLException {
        XMLBeanUtil xml = new XMLBeanUtil();
        Document doc = new Document(new Element("recentSQL"));
        Element element = doc.getRootElement();

        RecentSQLManage manage = RecentSQLManage.getInstance();
        BookmarkManage bm = BookmarkManage.getInstance();
        Iterator it = bm.getBookmarks().iterator();
        while (it.hasNext()) {
            Bookmark bookmark = (Bookmark) it.next();

            /**
             * ������ǩ��ǩ��������name����Ϊ��ǩ����
             */
            Element e = new Element("bookmark");
            e.setAttribute(XMLConstant.TAG_ARRTIBUTE_NAME, bookmark
                    .getAliasName());

            List list = manage.getRecentSQLList(bookmark);
            if (list == null)
                continue;

            Element sqlsElement = e.addContent(xml.parseBean(list, "sqls"));
            element.addContent(e);
        }

        xml.saveDocumentToFile(doc, new File(getRecentSQLInfoFile()));
    }
    public void deleteNoUseFile()
    {
    	int saveDays=Setting.getInstance().getIntProperty(PropertyConstant.PROPERTY_SYSTEM_HISTORYDAYS, 15);
    	String fileName=getPreviousRecentSQLInfoFile(saveDays);
    	new File(fileName).delete();
    }
    /**
     * ����sql�༭����
     */
    public void saveContentOfSQLEditor()
    {
        String sqlContent = ViewManage.getInstance().getSqlEditor()
                .getEditorContent();
        File file = new File(SystemConstant.sqlEditeInfo);
        FileOutputStream out = null;
        try {
            GUIUtil.createDir(file.getAbsolutePath(),false, false);
            out = new FileOutputStream(file);
            if(Setting.getInstance().getBoolProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_ISSAVEEDITORCONTENT, true))
            	out.write(sqlContent.getBytes());
        } catch (Exception e) {
            LogProxy.errorMessage("save content of  sqlEditor view error:"
                    + e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }
    /**
     * ��ȡ��ǰ���ڣ�Ȼ����ɵ�ǰ�������Ӧ�����ִ��sql�ı����ļ���
     * @return --��ɵ��ļ���
     */
    protected static String getRecentSQLInfoFile()
    {
        return getPreviousRecentSQLInfoFile(0);
    }
    /**
     * ��ȡ��ǰ����ǰdays������ڣ�Ȼ��������Ӧ�����ִ��sql�ı����ļ���
     * @param days  --��ǰ׷�ݵ�����
     * @return --��ɵ��ļ���
     */
    protected static String getPreviousRecentSQLInfoFile(int days)
    {
        long timeGap = 24 * 60 * 60 * 1000*days;
        long currentTime=System.currentTimeMillis();
        
        String deaultDateTimeFormat = "yyyy-MM-dd";      
        String date=StringUtil.transDate(new Date(currentTime-timeGap),deaultDateTimeFormat);
        
        return SystemConstant.recentSqlInfo+"("+date+").xml";
    }
    /**
     * ��ȡָ�����ڶ�Ӧ���ļ�
     * @param date  --��������ַ�
     * @return  --��ɵ��ļ���
     * @throws UnifyException  --����ʽ����ȷ���׳����쳣
     */
    protected static String getRecentSQLInfoFileByDate(String date) throws UnifyException
    {
        String msg=StringUtil.checkDateFormat(date);

        if(msg.equals(""))
            return SystemConstant.recentSqlInfo+"("+date+").xml";
        else
            throw new UnifyException(msg);
    }
    public CloseProgressDialog getProgressDialog() {
        return progressDialog;
    }
}
