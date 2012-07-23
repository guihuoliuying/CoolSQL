/*
 * Created on 2007-2-2
 */
package com.cattsoft.coolsql.modifydatabase.insert;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.component.BaseDialog;
import com.cattsoft.coolsql.pub.display.EditorSQLArea;
import com.cattsoft.coolsql.pub.parse.PublicResource;

/**
 * @author liu_xlin
 *Ԥ��sql�Ի���
 */
public class PreviewSQLDialog extends BaseDialog {

    private EditorSQLArea sqlArea;  //sql��ʾ����

    public PreviewSQLDialog(JDialog dialog,boolean isModel)
    {
        this(null,dialog,isModel);
    }
    public PreviewSQLDialog(Bookmark bookmark,JDialog dialog,boolean isModel)
    {
        super(dialog,isModel);
        createGUI(bookmark);
    }
    /**
     * ����ͼ�ν���
     *
     */
    protected void createGUI(Bookmark bookmark)
    {
        setTitle(PublicResource.getSQLString("previewsql.dialog.title"));
        
        JPanel pane=(JPanel)getContentPane();
        sqlArea=new EditorSQLArea(bookmark)
        {
            public boolean isExecuteSQL() {
                return false;
            }
        };
        pane.add(sqlArea,BorderLayout.CENTER);
        
        setHighlight(false);
        addWindowListener(new WindowAdapter()
                {
            		public void windowClosing(WindowEvent e)
            		{
            		    closeDialog();
            		}
                }
        );
        setSize(600,470);
        toCenter();
    }
    private void closeDialog()
    {
        removeAll();
        sqlArea.dispose();
        dispose();
    }
    /**
     * ������ʾ�����������Ϣ
     * @param text  --��Ҫ��ʾ���ı�����
     */
    public void setContent(String text)
    {
        sqlArea.setText(text);
    }
    /**
     * ����״̬������Ϣ
     * @param txt  --״̬����Ҫ��ʾ����Ϣ
     */
    public void setStatueInfo(String txt)
    {
        sqlArea.setStatueInfo(txt);
    }
    /**
     * ����ʾ����׷���ı���Ϣ
     * @param txt  --׷�ӵ��ı���Ϣ
     * @throws BadLocationException
     */
    public void append(String txt) throws BadLocationException
    {
        sqlArea.append(txt);
    }
    /**
     * �Ƿ���и�������
     * @return  --true���������� false��������
     */
    public boolean isHighlight()
    {
        return sqlArea.isHighlight();
    }
    /**
     * �����Ƿ���������ĵ������е�����
     * @param isHighlight true:�������� false:��������
     */
    public void setHighlight(boolean isHighlight)
    {
        sqlArea.setHighlight(isHighlight);
    }
    /**
     * @return Returns the bookmark.
     */
    public Bookmark getBookmark() {
        return sqlArea.getBookmark();
    }
    /**
     * @param bookmark The bookmark to set.
     */
    public void setBookmark(Bookmark bookmark) {
        sqlArea.setBookmark(bookmark);
    }
}
