/*
 * �������� 2006-9-14
 */
package com.cattsoft.coolsql.sql.commonoperator;

import java.awt.Window;
import java.sql.SQLException;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;

import com.cattsoft.coolsql.gui.property.NodeKey;
import com.cattsoft.coolsql.gui.property.PropertyFrame;
import com.cattsoft.coolsql.gui.property.database.EntityBaseInfo;
import com.cattsoft.coolsql.gui.property.database.EntityColumnProperty;
import com.cattsoft.coolsql.gui.property.database.ExportedKeyPropertyPane;
import com.cattsoft.coolsql.gui.property.database.ImportedKeyPropertyPane;
import com.cattsoft.coolsql.gui.property.database.IndicesProperty;
import com.cattsoft.coolsql.gui.property.database.PrimaryKeyProperty;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;

/**
 * @author liu_xlin
 *ʵ���������Բ鿴
 */
public class EntityPropertyOperator implements Operatable {

	private static final StringManager stringMgr=StringManagerFactory.getStringManager(EntityPropertyOperator.class);
	
    /* 
     * 0.Identifier
     * 1.Container(Dialog or Frame) :���Դ��ڵĸ�����,���û�и����,������Ϊ������(MainFrame)
     * 2.Boolean :�Ƿ�Ϊģ̬�Ի���,���ָ���ò���Ĭ������Ϊfalse
     * @see com.coolsql.sql.commonoperator.Operatable#operate(java.lang.Object)
     */
    public void operate(Object arg) throws UnifyException, SQLException {
        if(arg==null)
             throw new UnifyException("There is no operate object!");
        
        if(!(arg instanceof List))
        {
            throw new UnifyException("Illegal argument! Error type: "+arg.getClass());
        }       
        List<?> list=(List<?>)arg;
        if(list.size()<1)  //���
            return;
        
        Object firstOb=list.get(0);
        if(!(firstOb instanceof Identifier))
        {
            throw new UnifyException("Illegal argument! Error type: " + arg.getClass());
        }
        
        PropertyFrame pf=null;
        /**
         * ���û�и����ڲ���Ĭ����Ϊ�������
         */
        Window secOb = null;
        if(list.size()>1)
        {
            secOb = (Window)list.get(1);
        }
        if (secOb == null) {
        	secOb = GUIUtil.findLikelyOwnerWindow();
        }
        if(secOb instanceof JDialog)
            pf=new PropertyFrame((JDialog)secOb);
        else if(secOb instanceof JFrame)
            pf=new PropertyFrame((JFrame)secOb);
        else
            pf=new PropertyFrame(GUIUtil.getMainFrame());
        
        Identifier node=(Identifier)firstOb;
        
        pf.setTitle(PublicResource.getString("propertyframe.entity.title")+"("+node.getDisplayLabel()+")");
        
        //ʵ�����Ϣ��Ƭ
        NodeKey key=new NodeKey(PublicResource.getString("propertyframe.entity.entitybaseinfo"),(Icon)null,PropertyFrame.getRootData());
        key.setDisplayName(key.getName());
        pf.addCard(key,EntityBaseInfo.class,node.getDataObject());
        pf.setDefaultCard(key);
        
        //ʵ������Ϣ��Ƭ
        key=new NodeKey(PublicResource.getString("propertyframe.entity.columninfo"),(Icon)null,PropertyFrame.getRootData());
        key.setDisplayName(key.getName());
        pf.addCard(key,EntityColumnProperty.class,node);
        
        /**
         * Primary keys
         */
        key=new NodeKey(stringMgr.getString("property.entity.table.primarykeys.nodename"),(Icon)null,PropertyFrame.getRootData());
        key.setDisplayName(stringMgr.getString("property.entity.table.primarykeys.desc"));
        pf.addCard(key,PrimaryKeyProperty.class,node.getDataObject());
        
        /**
         * exported keys
         */
        key=new NodeKey("Exported keys",(Icon)null,PropertyFrame.getRootData());
        key.setDisplayName("Exported key information of entity!");
        pf.addCard(key,ExportedKeyPropertyPane.class,node.getDataObject());
        
        /**
         * imported keys
         */
        key=new NodeKey("Imported keys",(Icon)null,PropertyFrame.getRootData());
        key.setDisplayName("Imported key information of entity!");
        pf.addCard(key,ImportedKeyPropertyPane.class,node.getDataObject());
        
        //ʵ��������Ϣ��Ƭ
        key=new NodeKey("Indices",(Icon)null,PropertyFrame.getRootData());
        key.setDisplayName("Indices");
        pf.addCard(key,IndicesProperty.class,node);
        
        if(list.size()>2)
        {
            Object thirdOb=list.get(2);
            if(thirdOb instanceof Boolean)
            {
                pf.setModal(((Boolean)thirdOb).booleanValue());
            }else
                pf.setModal(false);
        }else
            pf.setModal(false);
        pf.setVisible(true);
    }

    /* ���� Javadoc��
     * @see com.coolsql.sql.commonoperator.Operatable#operate(java.lang.Object, java.lang.Object)
     */
    public void operate(Object arg0, Object arg1) throws UnifyException,
            SQLException {
    	throw new UnsupportedOperationException(this.getClass().getName()+"��not implement the method:operate(Object arg0, Object arg1)");
    }
}
