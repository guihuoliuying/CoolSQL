/*
 * �������� 2006-10-9
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;

import javax.swing.JDialog;

import com.cattsoft.coolsql.pub.display.GUIUtil;

/**
 *  ���ⴰ��,����Ϣ����ʾ,��Ҫ����ض���������ĳ�ʼ��
 * @author liu_xlin
 */
@SuppressWarnings("serial")
public class BaseDialog extends JDialog implements Individuable{
	
    public BaseDialog()
    {
        super();
        iInit();
    }
    public BaseDialog(Frame owner)
    {
        super(owner);
        iInit();
    }
    public BaseDialog(Frame owner, boolean modal) throws HeadlessException {
        super(owner,modal);
        iInit();
    }
    public BaseDialog(Frame owner, String title) throws HeadlessException {
        super(owner, title);   
        iInit();
    }
    public BaseDialog(Frame owner, String title, boolean modal)
    {
        super(owner,title,modal);
        iInit();
    }
    public BaseDialog(Dialog owner)
    {
        super(owner);
        iInit();
    }
    public BaseDialog(Dialog owner, boolean modal)
    {
        super(owner,modal);
        iInit();
    }
    public BaseDialog(Dialog owner, String title)
    {
        super(owner,title);
        iInit();
    }
    public BaseDialog(Dialog owner, String title, boolean modal)
    {
        super(owner,title,modal);
        iInit();
    }
    /**
     * ���ڶ��ƺ�����۵ķ���
     *
     */
    public void iInit()
    {
        
    }
    
    public void toCenter()
    {
    	Window owner = getOwner();
    	if (owner == null) {
    		owner = GUIUtil.findLikelyOwnerWindow();
    	}
        GUIUtil.centerFrameToFrame(owner, this);
    }
}
