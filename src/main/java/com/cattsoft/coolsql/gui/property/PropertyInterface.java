/*
 * �������� 2006-9-11
 */
package com.cattsoft.coolsql.gui.property;

import com.cattsoft.coolsql.pub.exception.UnifyException;

/**
 * @author liu_xlin �����޸�
 */
public interface PropertyInterface {

	public static final String PROPERTY_NAME="propertyName";
    /**
     * Save the changes took place in property panel
     * @return false if error occured ,otherwise return true.
     */
    public abstract boolean set();
    
    /**
     * Do something when user clicked the cancel button.
     *
     */
    public abstract void cancel();
    
    /**
     * Initialize the property pane with passed data object.
     * @param ob --the data object required in the initializing pane.
     */
    public abstract void setData(Object ob);
    
    /**
     * Do something when user clicked the apply button.
     *
     */
    public abstract void apply();
    
    /**
     * Indicate whether property panel need to display Apply button.
     * If the value return by this method is true, apply button will be displayed; otherwise will be hidden.
     */
    public boolean isNeedApply();
    
    /**
     * Reset the state of property panel.
     */
    public void reset();
    
    /**
     * Do when close the property frame
     */
    public void doOnClose() throws UnifyException;
}
