/*
 * IPropertyProvider.java
 *
 * This file is part of CoolSQL, http://coolsql.dev.java.net.
 *
 * Copyright 2008-2010, kenny liu
 *
 * To contact the author please send an email to: mailforlxl@gmail.com
 *
 */
package com.cattsoft.coolsql.view.bookmarkview.model;

import com.cattsoft.coolsql.gui.property.NodeKey;
import com.cattsoft.coolsql.gui.property.PropertyFrame;
import com.cattsoft.coolsql.gui.property.PropertyPane;

/**
 * Property frame creator interface
 * @author ��Т��(kenny liu)
 *
 * 2008-6-9 create
 */
public interface IPropertyProvider {

	/**
	 * Get default tree node.
	 * This method will be invoked when property frame is initialized.
	 */
	public NodeKey getDefaultNode();
	
	/**
	 * Set the specified node as default node.
	 */
	public void setDefaultNode(NodeKey key);
	/**
	 * Add new property panel to property collection.
	 * @param key --data used to determine node position of tree of property frame.
	 * @param propertyClass --sub class of Propertypane 
	 * @param paramProvider --interface used to create parameter required in initializing propertypane.
	 */
	public void addPropertySheet(NodeKey key,Class<? extends PropertyPane> propertyClass,IParameterProvider paramProvider);
	
	/**
	 * Build property frame which displays all property panels.
	 */
	public PropertyFrame createPropertyFrame();
}
