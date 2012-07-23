/*
 * �������� 2006-11-9
 */
package com.cattsoft.coolsql.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.loadlib.LoadJar;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.parse.xml.XMLBeanUtil;
import com.cattsoft.coolsql.pub.parse.xml.XMLConstant;
import com.cattsoft.coolsql.pub.parse.xml.XMLException;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.bookmarkview.RecentSQL;
import com.cattsoft.coolsql.view.bookmarkview.RecentSQLManage;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin ��������ʱ��װ�������Ϣ
 */
public class LoadData {
    private static LoadData loader=null;
    
    
    public synchronized static LoadData getInstance()
    {
        if(loader==null)
            loader=new LoadData();
        return loader;
    }
    private LoadData() {

    }
    
    /**
     * װ�������Ϣ 1��װ����ǩ��Ϣ 2��װ�����ִ�е�sql��� 3��װ���ϴα����sql�༭��ͼ�е����� 4��װ��ͼ�ν������ʾ״̬
     *  
     */
    public void loadData() {
        /**
         * װ����ǩ��Ϣ
         */
        loadBookmarks();

        try {
            /**
             * װ�����ִ�е�sql���
             */
            loadRecentSQL();
        } catch (Exception e) {
            LogProxy.errorReport(e);
        }

        /**
         * װ���ϴ�sql�༭��ͼ�ı༭����
         */
        loadSqlEditor();

    }

    /**
     * װ������,�����������Դ���
     *
     */
    public void loadLibResource()
    {
        //װ�����ļ�
        LoadJar.getInstance().getClassLoader();
    }
    /**
     * װ����ǩ��Ϣ <bookmarks><bookmark></bookmark> </bookmarks>
     * 
     * @throws XMLException
     */
    public void loadBookmarks() {
        /**
         * װ����ǩ��Ϣ
         */
        XMLBeanUtil xml = new XMLBeanUtil();
        File file = new File(SystemConstant.bookmarkInfo);
        if (!file.exists())
            return;

        Document doc = null;
        try {
            doc = xml.importDocumentFromXML(file);
        } catch (XMLException e) {
            LogProxy.errorReport(e);
            return;
        } catch (MalformedURLException e) {
            LogProxy.errorReport(e);
            return;
        } catch (IOException e) {
            LogProxy.errorReport(e);
            return;
        }

        BookmarkManage bm = BookmarkManage.getInstance();
        Element root = doc.getRootElement();
        List children = root.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Element child = (Element) children.get(i);
            Object ob;
            try {
                ob = xml.getBean(child);

                if (ob instanceof Bookmark) {
                    Bookmark bookmark = (Bookmark) xml.getBean(child);
                    bm.addBookmark(bookmark);
                }
            } catch (XMLException e1) {
                LogProxy.errorReport(e1);
                continue;
            }
        }
        bm.nextBookmarkAsDefault();
    }

    /**
     * װ���ϴα����sql�༭��ͼ�е�����
     */
    public void loadSqlEditor() {
        FileInputStream input = null;
        String content = "";
        File file = new File(SystemConstant.sqlEditeInfo);
        if (file.exists()) {
            try {

                input = new FileInputStream(file);
                byte[] b = new byte[1024];
                int count = -1;
                while ((count = input.read(b)) != -1) {
                    content += new String(b, 0, count);
                }
                CsAction undoAction=Setting.getInstance().getShortcutManager().
					getActionByClass(com.cattsoft.coolsql.system.menu.action.UndoMenuAction.class);
                CsAction redoAction=Setting.getInstance().getShortcutManager().
					getActionByClass(com.cattsoft.coolsql.system.menu.action.RedoMenuAction.class);
                if(content.length()!=0)
                {
                	ViewManage.getInstance().getSqlEditor().setEditorContent(
                            content);
					redoAction.setEnabled(false);
					undoAction.setEnabled(true);
                }else
                {
                	redoAction.setEnabled(false);
					undoAction.setEnabled(false);
                }
            } catch (Exception e) {
                LogProxy.errorMessage("load content of  sqlEditor view error:"
                        + e.getMessage());
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * ������ʽ�� <recentsqls><bookmark><recentsql></recentsql> </bookmark>
     * </recentsqls>
     * 
     * @throws UnifyException
     * @throws XMLException
     * @throws IOException
     * @throws MalformedURLException
     */
    public void loadRecentSQL() throws UnifyException, MalformedURLException, XMLException, IOException {
        File file = new File(DoOnClosingSystem.getRecentSQLInfoFile());
        if (!file.exists())
            return;

        /**
         * ��ȡ������Ϣ�������ĵ�����
         */
        XMLBeanUtil xml = new XMLBeanUtil();
        Document doc = xml.importDocumentFromXML(file);

        RecentSQLManage manage = RecentSQLManage.getInstance();
        Element root = doc.getRootElement();
        List children = root.getChildren();
        Iterator it = children.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String aliasName = e
                    .getAttributeValue(XMLConstant.TAG_ARRTIBUTE_NAME);//��ǩ����
            if (aliasName != null) {
                Bookmark bookmark = BookmarkManage.getInstance().get(aliasName);
                if (bookmark == null)
                    continue;

                //װ��sql������װ��Collection���͵Ķ���
                List linkListChild = e.getChildren();
                if (linkListChild.size() > 1)
                    throw new UnifyException(
                            PublicResource
                                    .getSQLString("system.loaddata.recentsql.linklisttoomuch"));
                else if (linkListChild.size() == 0) {
                    continue;
                }

                /**
                 * һ����ǩֻ�ܶ�Ӧһ��linkedlist���������һһ��Ӧ�������׳��쳣���߹��˵�
                 */
                Object ob = xml.getBean((Element) linkListChild.get(0));
                if (ob == null) {
                    continue;
                } else if (!(ob instanceof LinkedList)) {
                    throw new UnifyException(
                            "attribute(datatype)  of tag(sqls) is incorrect");
                } else {
                    //�Ի�ȡ��������б���ͬʱ��RecentSQL������б���
                    LinkedList sqls = (LinkedList) ob;
                    Iterator tmpIt = sqls.iterator();
                    while (tmpIt.hasNext()) {
                        ob = tmpIt.next();
                        if (ob instanceof RecentSQL) {
                            ((RecentSQL) ob).setBookmark(bookmark);
                            manage.addSQL((RecentSQL) ob, bookmark);
                        }
                    }
                }
            }
        }
        manage.adjustAll();
    }

    /**
     * װ��ָ����ǩ���ļ������ִ��sql����Ϣ
     * 
     * @param bookmark
     *            --�����ǩ����
     * @param date
     *            --ָ��ִ��sql������
     * @return
     * @throws UnifyException
     * @throws XMLException
     * @throws IOException
     * @throws MalformedURLException
     */
    public Vector loadRecentSQL(String bookmark, String date)
            throws UnifyException, MalformedURLException, XMLException, IOException {
        File file = new File(DoOnClosingSystem.getRecentSQLInfoFileByDate(date)); //���ָ�������ڻ�ȡ�ļ���
        if (file == null || !file.exists()) {
            return new Vector();
        }

        /**
         * ��ȡ������Ϣ�������ĵ�����
         */
        XMLBeanUtil xml = new XMLBeanUtil();
        Document doc = xml.importDocumentFromXML(file);

        RecentSQLManage manage = RecentSQLManage.getInstance();
        Element root = doc.getRootElement();
        List children = root.getChildren();
        Iterator it = children.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String aliasName = e
                    .getAttributeValue(XMLConstant.TAG_ARRTIBUTE_NAME);//��ǩ����
            if (bookmark.equals(aliasName)) {
                //װ��sql������װ��Collection���͵Ķ���
                List linkListChild = e.getChildren();
                if (linkListChild.size() > 1)
                    throw new UnifyException(
                            PublicResource
                                    .getSQLString("system.loaddata.recentsql.linklisttoomuch"));
                else if (linkListChild.size() == 0) {
                    continue;
                }

                /**
                 * һ����ǩֻ�ܶ�Ӧһ��linkedlist���������һһ��Ӧ�������׳��쳣���߹��˵�
                 */
                Object ob = xml.getBean((Element) linkListChild.get(0));
                if (ob == null) {
                    continue;
                } else if (!(ob instanceof LinkedList)) {
                    throw new UnifyException(
                            "attribute(datatype)  of tag(sqls) is incorrect");
                } else {
                    //�Ի�ȡ��������б���ͬʱ��RecentSQL������б���
                    Bookmark tmpBm=BookmarkManage.getInstance().get(bookmark);
                    if(tmpBm==null)
                        continue;
                    LinkedList sqls = (LinkedList) ob;
                    manage.sortRecentSQLs(sqls);
                    Iterator sqlIt = sqls.iterator();
                    Vector datas = new Vector();
                    int count = 0;
                    while (sqlIt.hasNext()) {
                        count++;
                        Object sqlObject = sqlIt.next();
                        
                        if (sqlObject instanceof RecentSQL) {
                            RecentSQL tmpSQL = (RecentSQL) sqlObject;
                            tmpSQL.setBookmark(tmpBm);
                            Vector tmpVector=tmpSQL.converToVector(count);
                            datas.add(tmpVector);
                        }
                    }
                    return datas;
                }
            } else {
                continue;
            }
        }
        return new Vector();
    }
}
