/*
 * �������� 2006-12-6
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.basic.BasicButtonUI;

import com.sun.java.swing.plaf.windows.WindowsButtonUI;

/**
 * 
 * @author liu_xlin ���ⰴť�࣬ͨ��ͼ��չʾ���
 */
public class IconButton extends JButton {

	private static final long serialVersionUID = 1L;

	private ThreeDBorder raiseBorder = null;

    private ThreeDBorder lowerBorder = null;

    public IconButton(Icon icon) {
        super(icon);
        raiseBorder = new ThreeDBorder(ThreeDBorder.RAISE);
        lowerBorder = new ThreeDBorder(ThreeDBorder.LOWER);
        setMargin(new Insets(0, 0, 0, 0));
        setContentAreaFilled(false);
        this.setBorder(null);
        this.setFocusable(false);
        this.setBackground(null);
        setOpaque(false);
        this.setPreferredSize(getIconButtonSize());
        this.addMouseListener(new MouseAdapter() {
            private boolean isPressed;
            public void mouseReleased(MouseEvent e) {
                isPressed=false;
                if (getBorder() != null)  
                    setRaiseBorder();
            }

            public void mousePressed(MouseEvent e) {
                if (e.getModifiers() == InputEvent.BUTTON3_MASK
                        || e.getModifiers() == InputEvent.BUTTON2_MASK)//������껬�ֺ��Ҽ�
                    return;
                isPressed=true;
                setLowerBorder(); //������꣬����߿�
            }

            public void mouseEntered(MouseEvent e) {
                if(!isPressed)
                    setRaiseBorder();
                else
                    setLowerBorder();
            }

            public void mouseExited(MouseEvent e) {
                setBorder(null);
            }
        });
        this.addFocusListener(new FocusListener()
                {

                    public void focusGained(FocusEvent e) {                            
                    }

                    public void focusLost(FocusEvent e) {
                        setBorder(null);                           
                    }
            
                }
        );
    }

    public void setRaiseBorder() {
        if(this.isEnabled())
           setBorder(raiseBorder);
    }

    public void setLowerBorder() {
        if(this.isEnabled())
           setBorder(lowerBorder); //������꣬����߿�
    }

    public void setUI(ButtonUI ui) {
        if (ui instanceof WindowsButtonUI) {
            ui = new BasicButtonUI();
        }
        super.setUI(ui);
    }
    public Dimension getIconButtonSize() {
        return new Dimension(21, 21);
    }
}
