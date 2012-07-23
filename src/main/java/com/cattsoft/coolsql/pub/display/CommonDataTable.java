/*
 * �������� 2006-9-14
 */
package com.cattsoft.coolsql.pub.display;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import com.cattsoft.coolsql.pub.component.BaseMenuManage;

/**
 * @author liu_xlin ��ʾ����Ϣʱ���õı�ؼ�
 */
public class CommonDataTable extends BaseTable {

	private static final long serialVersionUID = 1L;

	/**
	 * �Ƿ������Ϣ��ʾ��Ĭ��Ϊ��ʾ
	 */
	private boolean isToolTip = true;

	/**
	 * �Ƿ����Ҽ�˵���Ĭ��Ϊ����
	 */
	private boolean isPopMenu = true;

	/**
	 * ��ؼ��Ҽ�˵�
	 */
	private BaseMenuManage menu = null;
	public CommonDataTable() {
		super();
	}
	public CommonDataTable(TableModel model) {
		this(model, null);
	}
	public CommonDataTable(TableModel model, int[] renderCols) {
		this(model, renderCols, null);
		menu = new TableMenu(this);
	}
	public CommonDataTable(Vector<?> data, Vector<?> header) {
		this(data, header, null);
	}

	public CommonDataTable(Object[][] data, Object[] header) {
		this(data, header, null);
	}
	/**
	 * ��ʼ����ؼ��������ݣ�ͬʱ���б��Ԫ����Ⱦ
	 * 
	 * @param data
	 *            ���
	 * @param header
	 *            ��ͷ����
	 * @param renderCols
	 *            ���屻��Ⱦ����
	 */
	public CommonDataTable(Vector<?> data, Vector<?> header, int[] renderCols) {
		this(data, header, renderCols, null);
		menu = new TableMenu(this);
	}

	public CommonDataTable(Vector<?> data, Vector<?> header, int[] renderCols,
			TableMenu menu) {
		this(new CommonTableModel(data, header), renderCols, menu);

	}
	public CommonDataTable(TableModel model, int[] renderCols, TableMenu menu) {
		super(model);
		// getTableHeader().setUI(new IBasicTableHeaderUI());
		getTableHeader().setReorderingAllowed(false);
		setCellSelectionEnabled(true);
		this.menu = menu;
		setEnableToolTip(true);
		TableMouse tm = new TableMouse();
		addMouseListener(tm);
		addMouseMotionListener(tm);

		if (renderCols != null && renderCols.length > 0) {
			IconTableCellRender render = new IconTableCellRender();
			for (int i = 0; i < renderCols.length; i++) {
				getColumnModel().getColumn(renderCols[i]).setCellRenderer(
						render);
			}
		}
	}
	/**
	 * ��ʼ����ؼ��������ݣ�ͬʱ���б��Ԫ����Ⱦ
	 * 
	 * @param data
	 *            ���
	 * @param header
	 *            ��ͷ����
	 * @param renderCols
	 *            ���屻��Ⱦ����
	 */
	public CommonDataTable(Object[][] data, Object[] header, int[] renderCols) {
		this(data, header, renderCols, null);
		menu = new TableMenu(this);
	}
	public CommonDataTable(Object[][] data, Object[] header, int[] renderCols,
			TableMenu menu) {
		this(new CommonTableModel(data, header), renderCols, menu);
	}
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	/**
	 * ���ñ�ؼ���Ϣ��ʾ�Ŀ�����
	 * 
	 * @param isTip
	 */
	public void setEnableToolTip(boolean isTip) {
		this.isToolTip = isTip;
		if (!isTip) {
			this.setToolTipText(null);
		}
	}
	/**
	 * ���ص�ǰtable�ؼ��Ƿ���б����Ϣ����ʾ
	 * 
	 * @return true if prompt,false if not
	 */
	public boolean isToolTip() {
		return this.isToolTip;
	}
	/**
	 * �����Ҽ�˵��Ƿ񵯳�
	 * 
	 * @param isDisplay
	 */
	public void setPopMenuDisplay(boolean isDisplay) {
		isPopMenu = isDisplay;
	}
	/**
	 * �÷������ڷ��ص�ǰtable�ؼ����Ҽ�˵��Ƿ���ʾ����Ϣ��ʾ���Ĳ˵���
	 * 
	 * @return Ĭ���������ʾ������д�÷���
	 */
	public boolean isDisplayToolTipSelectMenu() {
		return true;
	}
	/**
	 * 
	 * @author liu_xlin ���������������ʾ��Ϣ�Լ��Ҽ�˵��Ĵ��?��װ�ڴ�
	 */
	protected class TableMouse extends MouseAdapter
			implements
				MouseMotionListener {
		boolean isPopupTriggerWhenPress;
		/**
		 * ��д���෽������������ͣ�У�������Ϣ��ʾ
		 * 
		 * @param e
		 */
		public void mouseMoved(MouseEvent e) {
			if (!isToolTip) // ��ʹ����Ϣ��ʾ
				return;
			if (e.getSource() instanceof CommonDataTable) {
				CommonDataTable source = (CommonDataTable) e.getSource();
				int row = source.rowAtPoint(e.getPoint()); // ���������
				int cols = source.getColumnCount();

				String displayText = "<html><table border=0><tr>";
				for (int i = 0; i < cols; i++) {
					displayText += "<th align=left color=blue>"
							+ source.getColumnName(i) + "</th>";
				}
				displayText += "</tr><tr>";
				for (int i = 0; i < cols; i++) {
					Object tmpOb = source.getValueAt(row, i);
					displayText += "<td align=left>"
							+ (tmpOb != null ? tmpOb.toString() : "") + "</th>";
				}
				displayText += "</tr></table></html>";

				setToolTipText(displayText); // ������Ϣ��ʾ����
			}
		}
		/**
		 * �����Ҽ�˵��ĵ���
		 */
		public void mouseReleased(MouseEvent e) {
			if (isPopupTriggerWhenPress||e.isPopupTrigger()) {
				if (isPopMenu && menu != null) // ������?���Ҽ�˵�
					menu.getPopMenu().show((JTable) e.getSource(), e.getX(),
							e.getY());
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e)
		{
			isPopupTriggerWhenPress=e.isPopupTrigger();
		}
		/*
		 * ���� Javadoc��
		 * 
		 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
		 */
		public void mouseDragged(MouseEvent e) {

		}
	}
	public void setMenuManage(BaseMenuManage bmm) {
		menu = bmm;
	}
	/**
	 * @return ���� menu��
	 */
	public BaseMenuManage getPopMenuManage() {
		return menu;
	}
}
