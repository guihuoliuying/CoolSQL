/*
 * Created on 2007-5-18
 */
package com.cattsoft.coolsql.system.menubuild;

/**
 * @author liu_xlin
 *check the availability of JMenuItem object
 */
public interface MenuItemEnableCheck {

    /**
     * 
     *check the availability of JMenuItem object
     *@return --true if enabled,false if disabled
     */
    public boolean check();
}
