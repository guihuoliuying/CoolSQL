/*
 * �������� 2006-7-2
 *
 */
package com.cattsoft.coolsql.view.bookmarkview;

import java.io.Serializable;

import javax.swing.Icon;

import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.system.menubuild.IconResource;


/**
 * @author liu_xlin
 *��������ǩ�Լ���ǩ��ͼ��������Ϣ
 */
public class BookMarkPubInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * �ڵ����Ͷ���
	 */
	public static final int NODE_HEADER=0;
	public static final int NODE_BOOKMARK=1;
	public static final int NODE_DATABASE=2;
	public static final int NODE_RECENTSQLGROUP=3;
	public static final int NODE_RECENTSQL=4;
	public static final int NODE_CATALOG=5;
	public static final int NODE_SCHEMA=6;
	public static final int NODE_VIEWS=7;
	public static final int NODE_VIEW=8;
	public static final int NODE_TABLES=9;
	public static final int NODE_TABLE=10;
	public static final int NODE_COLUMN=11;
	public static final int NODE_KEYCOLUMN=12;
	public static final int NODE_SYNONYM=13;
	public static final int NODE_SYSTEM_TABLES = 15;
	
	public static final int NODE_UNDEFINED = 14;//Undefined Type
	
	/**
	 * the state of bookmark
	 */
	public static final int BOOKMARKSTATE_NORMAL=0; 
	public static final int BOOKMARKSTATE_CONNECTING=1;
	public static final int BOOKMARKSTATE_DISCONNECTING=2;
	/**
	 * �ڵ�ͼ�궨��
	 */
	private static Icon[] list=null;
	/**
	 * ��ȡ�ڵ�ͼ���б�
	 * @return ���� list��
	 */
	public static Icon[] getIconList() {
		if(list==null)
			init();
		return list;
	}
	/**
	 * ���������ͻ�ȡ��Ӧ����ʾͼ�꣬Ŀǰֻ�Ƕ�����TABLE,VIEW���͵�icon������������ʱδ����
	 * @param tableType  --�������
	 * @return 
	 */
	public static Icon getTableTypeIcon(String tableType)
	{
		tableType=StringUtil.trim(tableType);
		if(tableType.equals("VIEW"))
		{
			return list[NODE_VIEW];
		}else if(tableType.equals("TABLE"))
		{
			return list[NODE_TABLE];
		}else
			return null;
	}
	private static void init()
	{
		list=new Icon[16];
		/**
		 * ��ڵ�
		 */
		list[NODE_HEADER]=PublicResource.getIcon("bookmarkView.treenodeicon.header");
		/**
		 * ��ǩ�ڵ�
		 */
		list[NODE_DATABASE]=PublicResource.getIcon("bookmarkView.treenodeicon.database");
	    /**
	     * ���ִ�е�sql�ڵ��ϲ�ͼ��
	     */		
		list[NODE_RECENTSQLGROUP]=PublicResource.getIcon("bookmarkView.treenodeicon.recentsql");
		/**
		 * ���ִ�е�sql���ڵ�
		 */
		list[NODE_RECENTSQL]=PublicResource.getIcon("bookmarkView.treenodeicon.sql");
		/**
		 * ����ڵ�
		 */
		list[NODE_CATALOG]=PublicResource.getIcon("bookmarkView.treenodeicon.catalog");
		
		/**
		 * ģʽͼ��
		 */
		list[NODE_SCHEMA]=PublicResource.getIcon("bookmarkView.treenodeicon.schema");
		/**
		 * ��ͼ��
		 */
		list[NODE_VIEWS]=PublicResource.getIcon("bookmarkView.treenodeicon.entitygroup");
		/**
		 * ��ͼ�ڵ�
		 */
		list[NODE_VIEW]=PublicResource.getIcon("bookmarkView.treenodeicon.view");
		/**
		 * ����
		 */
		list[NODE_TABLES]=list[NODE_VIEWS];
		/**
		 * ��ڵ�
		 */
		list[NODE_TABLE]=PublicResource.getIcon("bookmarkView.treenodeicon.table");
		/**
		 * �нڵ�
		 */
		list[NODE_COLUMN]=PublicResource.getIcon("bookmarkView.treenodeicon.column");
		/**
		 * �����нڵ�
		 */
		list[NODE_KEYCOLUMN]=PublicResource.getIcon("bookmarkView.treenodeicon.keycolumn");
		/**
		 * ����δ����״̬�µ���ǩ
		 */
		list[NODE_BOOKMARK]=PublicResource.getIcon("bookmarkView.treenodeicon.bookmark");
		
		/**
		 * synonym node
		 */
		list[NODE_SYNONYM]=PublicResource.getIcon("bookmarkView.treenodeicon.table");
		
		/**
		 * Return a blank icon if node is not defined.
		 */
		list[NODE_UNDEFINED]=IconResource.getBlankIcon();
		
		list[NODE_SYSTEM_TABLES] = list[NODE_TABLES];
	}
	/**
	 * get bookmark icon according to isConnected. 
	 * @param isConnected -- state of bookmark indicates whether bookmark is connected or not
	 */
	public static Icon getBookmarkIcon(boolean isConnected)
	{
		int index=isConnected?BookMarkPubInfo.NODE_DATABASE:BookMarkPubInfo.NODE_BOOKMARK;
		return getIconList()[index];
	}
	/**
	 * validate whether typeId is bookmark type.
	 * return true if it's bookmark type,otherwise return false.
	 */
	public static boolean isBookmarkNode(int typeId)
	{
		return typeId==BookMarkPubInfo.NODE_DATABASE||typeId==BookMarkPubInfo.NODE_BOOKMARK;
	}
}
