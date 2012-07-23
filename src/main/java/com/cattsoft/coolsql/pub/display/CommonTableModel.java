/*
 * �������� 2006-9-18
 */
package com.cattsoft.coolsql.pub.display;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 * @author liu_xlin
 */
public class CommonTableModel extends DefaultTableModel {
    
   public CommonTableModel(Object[][] data,Object[] header)
   {
       super(data,header);
   }
   public CommonTableModel(Vector data,Vector header)
   {
       super(data,header);
   }
   /**
    *Add a row data to model without notifying table.
    * @param row
    */
   public void addRowNoFire(Object[] row)
   {
   	 dataVector.insertElementAt(convertToVector(row), getRowCount()); 
   }
}
