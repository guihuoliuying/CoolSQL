/*
 * �������� 2006-10-9
 */
package com.cattsoft.coolsql.pub.component;

import javax.swing.JFrame;

import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.PublicResource;

/**
 * @author liu_xlin
 *���ⴰ��,����Ϣ����ʾ,��Ҫ����ض���������ĳ�ʼ��
 */
public class BaseFrame extends JFrame implements Individuable{
    public BaseFrame()
    {
        super();
        iInit();
    }
    public BaseFrame(String title)
    {
        super(title);
        iInit();
    }
    /**���ڶ��ƺ�����۵ķ���
     * @see com.coolsql.pub.component.Individuable#iInit()
     */
    public void iInit() {
        this.setIconImage(PublicResource.getIcon("frame.icon").getImage());
    }
    public void toCenter()
    {
        GUIUtil.centerFrameToFrame(GUIUtil.getMainFrame(),this);
    }
}
