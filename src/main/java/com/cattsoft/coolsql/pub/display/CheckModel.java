/*
 * �������� 2006-9-19
 */
package com.cattsoft.coolsql.pub.display;

import javax.swing.JMenuItem;

/**
 * @author liu_xlin
 * �Բ˵������У��
 */
public abstract class CheckModel implements MenuCheckable {

    private JMenuItem menu=null;
    public CheckModel(JMenuItem item)
    {
        this.menu=item;
    }
    /**
     * @return ���� menu��
     */
    public JMenuItem getMenu() {
        return menu;
    }
    /**
     * @param menu Ҫ���õ� menu��
     */
    public void setMenu(JMenuItem menu) {
        this.menu = menu;
    }
}
