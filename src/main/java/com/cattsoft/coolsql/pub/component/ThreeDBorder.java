/*
 * �������� 2006-9-25
 *
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

/**
 * @author liu_xlin
 *�Զ���߿��࣬�ṩ͹�����°��������͵ı߿�
 */
public class ThreeDBorder extends AbstractBorder {
   public static final int RAISE=0; //͹��
   public static final int LOWER=1; //�°�
   
   private int type=0; //�߿�����
   private final int thickness = 1;
   public ThreeDBorder(int type)
   {
   	 if(type<0||type>1)
   	 	throw new IllegalArgumentException("border type is not exists,type:"+type);
   	 this.type=type;
   }
   /**
    * ���Ʊ߿�
    */
   public void paintBorder(Component c, Graphics g, int x, int y,
                           int width, int height) {
       g.setColor(type==RAISE?Color.white:Color.gray);
       g.drawLine(0, 0, width - 1, 0);
       g.drawLine(0, 0, 0, height - 1);
       g.setColor(type==RAISE?Color.gray:Color.white);
       g.drawLine(width - 1, 0, width - 1, height);
       g.drawLine(0, height - 1, width, height - 1);
   }
   /**
    * ��д�߿�������ı߾�
    */
   public Insets getBorderInsets(Component c) {
       return new Insets(thickness, thickness, thickness, thickness);
   }
}
