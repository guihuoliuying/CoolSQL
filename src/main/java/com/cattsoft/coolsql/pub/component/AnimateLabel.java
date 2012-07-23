/*
 * �������� 2006-12-27
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

import com.cattsoft.coolsql.pub.parse.PublicResource;

/**
 * @author liu_xlin
 *��̬��ǩ���ñ�ǩ���ڳ�ʱ��Ĵ�����ʾ��
 */
public class AnimateLabel extends JLabel {
	
	private static final long serialVersionUID = 1L;
	private static final Image image1=PublicResource.getIcon("gui.pub.animatelabel.icon1").getImage();
    private static final Image image2=PublicResource.getIcon("gui.pub.animatelabel.icon2").getImage();
    private static final Image image3=PublicResource.getIcon("gui.pub.animatelabel.icon3").getImage();
    private static final Image image4=PublicResource.getIcon("gui.pub.animatelabel.icon4").getImage();
    /**
     * ��̬��ʾͼ��
     */
    private Timer timer=null;
    
    /**
     * �ñ�־Ϊ������ֽ���ˢ��,���true:��ʾ�ɼ�ʱ����ʱ��ˢ��ҳ��,false:�������ڽ���Ĵ�С�����½����ˢ��
     */
    private boolean isAnimation=false;
    private int delay=1000;  //�ػ�ͼ���ʱ����
    
    
    public AnimateLabel()
    {
        super();
        init();
    }
    public AnimateLabel(String txt)
    {
        super(txt);
        init();
    }
    public AnimateLabel(String txt,int horizontalAlignment)
    {
        super(txt,horizontalAlignment);
        init();
    }
    protected void init()
    {
        setIcon(new LabelIcon());
        ActionListener action=new ActionListener()
        {

            public void actionPerformed(ActionEvent e) {
                isAnimation=true;
                repaint();               
            }
            
        };
        setDelay(700);
        timer=new Timer(delay,action);
        setMinimumSize(new Dimension(40,40));
    }
    /**
     * ������ʱ��
     *
     */
    public void start()
    {
        timer.start();
    }
    /**
     * ֹͣ��ʱ��
     *
     */
    public void stop()
    {
        timer.stop();
    }
    public boolean isRunning()
    {
        return timer.isRunning();
    }
    public int getDelay() {
        return delay;
    }
    public void setDelay(int delay) {
        this.delay = delay;
    }
    protected class LabelIcon extends ImageIcon
    {
		private static final long serialVersionUID = 1L;
		private Image image[]=null;
        private int index=0;
        public LabelIcon()
        {
            super();
            image=new Image[4];
            image[0]=image1;
            image[1]=image2;
            image[2]=image3;
            image[3]=image4;
            setImage(image[0]);
        }
        /* ���� Javadoc��
         * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
         */
        public void paintIcon(Component c, Graphics g, int x, int y) {
            if(isAnimation)
            {
                isAnimation=false;
                setImage(image[index]);
                
                index++;
                if(index>=image.length)
                    index=0;
            }
            super.paintIcon(c,g,x,y);     

        }
        
    }
}

