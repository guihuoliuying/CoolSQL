/*
 * �������� 2006-10-12
 */
package com.cattsoft.coolsql.view.resultset;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import com.cattsoft.coolsql.action.framework.CsAction;
import com.cattsoft.coolsql.pub.component.BaseMenuManage;
import com.cattsoft.coolsql.pub.component.BasePopupMenu;
import com.cattsoft.coolsql.pub.display.BaseTable;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.system.menubuild.IconResource;
import com.cattsoft.coolsql.view.ResultSetView;
import com.cattsoft.coolsql.view.resultset.action.AddNewDataAction;
import com.cattsoft.coolsql.view.resultset.action.CancelDeleteRowsAction;
import com.cattsoft.coolsql.view.resultset.action.ChangeStatusOfDataSetPanelAction;
import com.cattsoft.coolsql.view.resultset.action.CopyAsSqlInsertAction;
import com.cattsoft.coolsql.view.resultset.action.DataSetTableCopyAction;
import com.cattsoft.coolsql.view.resultset.action.DeleteRowsAction;
import com.cattsoft.coolsql.view.resultset.action.EditInDialogAction;
import com.cattsoft.coolsql.view.resultset.action.ExportExcelOfResultAction;
import com.cattsoft.coolsql.view.resultset.action.ExportHtmlOfResultAction;
import com.cattsoft.coolsql.view.resultset.action.ExportTxtOfResultAction;
import com.cattsoft.coolsql.view.resultset.action.RefreshQueryAction;
import com.cattsoft.coolsql.view.resultset.action.RestoreCellAction;
import com.cattsoft.coolsql.view.resultset.action.SaveChangeToDBAction;
import com.cattsoft.coolsql.view.resultset.action.ShowColumnSelectDialogAction;
import com.cattsoft.coolsql.view.resultset.action.UpdateRowActioin;

/**
 * @author liu_xlin �����ͼ�Ҽ�˵�������
 */
public class ResultDisplayPopMenuManage extends BaseMenuManage {
    /**
     * ����
     */
    private JMenuItem copy;

    /**
     * �������
     */
    private JMenu export;

    //�������ı�
    private JMenuItem exportTxt;

    //������excel�ļ�
    private JMenuItem exportExcel;

    //��������ҳ
    private JMenuItem exportHtml;

    //ˢ�����(����ִ��sql)
//    private JMenuItem refresh;

    /**
     * �������ѡ�е���
     */
//    private JMenuItem updateData = null;

    /**
     * �����������ص�����(ֻ��һ��ʵ����еĲ���),�ɶԸ�ʵ�������ݵĲ���
     */
//    private JMenuItem insertData=null;
    
    private JMenuItem preferWidth;  //��ѡ��������Ϊ��ѿ��
    /**
     * ��ؼ���չʾ����
     */
    private JMenu tableSetting = null;

    //�Ƿ������
    private JCheckBoxMenuItem isSortable = null;

    private JMenu tableLine = null;

    //������������ʾ
    private JCheckBoxMenuItem horizontalLine = null;

    //������������ʾ
    private JCheckBoxMenuItem verticalLine = null;

    /**
     * enable/disable displaying table line number
     */
    private JCheckBoxMenuItem lineNumber;
    /**
     * @param com
     */
    public ResultDisplayPopMenuManage(JComponent com) {
        super(com);
        createPopMenu();
    }

    public void createPopMenu() {
        popMenu = new BasePopupMenu();

        //���Ʋ˵��ĳ�ʼ��
        Action copyAction = new DataSetTableCopyAction();
        copy = createMenuItem(PublicResource
                .getString("TextEditor.popmenu.copy"), PublicResource
                .getIcon("TextMenu.icon.copy"), copyAction);
        popMenu.add(copy);
        copy.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
        
        popMenu.addSeparator();
        
        //ˢ�²˵���
//        refresh = createMenuItem(PublicResource
//                .getString("resultView.popmenu.refresh.label"), PublicResource
//                .getIcon("resultView.iconbutton.refresh.icon"),
//                new EditDataSetTableAction(ResultSetDataProcess.REFRESH));
        CsAction refreshAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(RefreshQueryAction.class);
        popMenu.add(refreshAction.getMenuItem());

        //���²˵���
        
        CsAction updateAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(UpdateRowActioin.class);
        popMenu.add(updateAction.getMenuItem());
        
        CsAction addAction=Setting.getInstance().getShortcutManager()
			.getActionByClass(AddNewDataAction.class);
        //��ʵ�������ݲ˵���
        popMenu.add(addAction.getMenuItem());
        
        //Copy data as insert sql.
        CsAction copyAsInsertSQLAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(CopyAsSqlInsertAction.class);
        popMenu.add(copyAsInsertSQLAction.getMenuItem());
        
        popMenu.addSeparator();
        
        //To change the status of data panel
        CsAction changeStatusAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(ChangeStatusOfDataSetPanelAction.class);
        popMenu.add(changeStatusAction.getMenuItem());
        
        //To restore the value of selected cell
        CsAction restoreCellAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(RestoreCellAction.class);
        popMenu.add(restoreCellAction.getMenuItem());
        
        //To delete the selected rows
        CsAction deleteRowAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(DeleteRowsAction.class);
        popMenu.add(deleteRowAction.getMenuItem());
        
      //To cancel the selected rows have been marked as deleted.
        CsAction cancelDeleteRowAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(CancelDeleteRowsAction.class);
        popMenu.add(cancelDeleteRowAction.getMenuItem());
        
        //To edit cell value in dialog
        CsAction editInDialogAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(EditInDialogAction.class);
        popMenu.add(editInDialogAction.getMenuItem());
        
        //Select columns as key for instant updating.
        CsAction showColumnSelectDialogAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(ShowColumnSelectDialogAction.class);
        popMenu.add(showColumnSelectDialogAction.getMenuItem());
        
        //Save cell value into database
        CsAction saveChangeToDBAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(SaveChangeToDBAction.class);
        popMenu.add(saveChangeToDBAction.getMenuItem());
        
        popMenu.addSeparator();

        //������ݲ˵�
        export = new JMenu(PublicResource.getString("table.popup.export"));
        export.setIcon(IconResource.getBlankIcon());
        popMenu.add(export);

        //�����ı��˵�
        exportTxt = createMenuItem(PublicResource
                .getString("table.popup.exportTxt"), PublicResource
                .getIcon("table.popup.export.txticon"),
                new ExportTxtOfResultAction());
        export.add(exportTxt);

        //����excel�˵�
        exportExcel = createMenuItem(PublicResource
                .getString("table.popup.exportExcel"), PublicResource
                .getIcon("table.popup.export.excelicon"),
                new ExportExcelOfResultAction());
        export.add(exportExcel);

        //������ҳ�˵�
        exportHtml = createMenuItem(PublicResource
                .getString("table.popup.exportHtml"), PublicResource
                .getIcon("table.popup.export.htmlicon"),
                new ExportHtmlOfResultAction());
        export.add(exportHtml);

        popMenu.addSeparator();
        
        //�����Ȳ˵���
        Action preferWidthAction=new AbstractAction()
        {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
                BaseTable table=(BaseTable)popMenu.getClientProperty(ResultSetView.DataTable);
                table.getDefaultAdjustWidthAction().actionPerformed(e);
            }
            
        };
        preferWidth=addMenuItem(
                PublicResource
                .getSQLString("rowinsert.table.popmenu.preferwidth"),
                preferWidthAction,
        PublicResource
                .getSQLIcon("rowinsert.table.popmenu.preferwidth.icon"),
        false);
        preferWidth.setAccelerator(KeyStroke.getKeyStroke("alt W"));
        
        /**
         * ��ؼ��������ò˵���
         */
        tableSetting = new JMenu(PublicResource
                .getString("table.popup.tablesetting"));
        tableSetting.setIcon(IconResource.getBlankIcon());
        popMenu.add(tableSetting);

        //�Ƿ������
        isSortable = new JCheckBoxMenuItem(PublicResource
                .getString("table.popup.issortable"));
        isSortable.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setTableSortable(isSortable.getState());
            }

        });
        tableSetting.add(isSortable);

        //���������
        tableLine = new JMenu(PublicResource.getString("table.popup.tableline"));
        tableSetting.add(tableLine);

        //����������
        horizontalLine = new JCheckBoxMenuItem(PublicResource
                .getString("table.popup.horizontal"));
        horizontalLine.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                setHorizontalLine(horizontalLine.getState());
            }

        });
        tableLine.add(horizontalLine);

        //����������
        verticalLine = new JCheckBoxMenuItem(PublicResource
                .getString("table.popup.vertical"));
        verticalLine.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                setVerticalLine(verticalLine.getState());
            }

        });
        tableLine.add(verticalLine);
        
        lineNumber=new JCheckBoxMenuItem(PublicResource
                .getString("table.popup.linenumber"));
        lineNumber.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	((BaseTable)getComponent()).setTableRowNumberVisible(lineNumber.getState());
            }

        });
        popMenu.add(lineNumber);
        
//        popMenu.addPopupMenuListener(new PopupMenuListener()
//        {
//
//			public void popupMenuCanceled(PopupMenuEvent e) {
//				
//			}
//
//			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
//				popMenu.putClientProperty(ResultSetView.DataTable,null);
//				
//			}
//
//			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
//				
//			}
//        	
//        }
//        );
    }

    /**
     * ���ñ�ؼ��Ƿ������
     * 
     * @param b
     */
    private void setTableSortable(boolean b) {
        DataSetTable tmpTable = (DataSetTable) this.getComponent();
        if (tmpTable == null)
            return;
        tmpTable.setSortable(b);
    }

    private void setHorizontalLine(boolean b) {
        DataSetTable tmpTable = (DataSetTable) this.getComponent();
        if (tmpTable == null)
            return;
        tmpTable.setShowHorizontalLines(b);
    }

    private void setVerticalLine(boolean b) {
        DataSetTable tmpTable = (DataSetTable) this.getComponent();
        if (tmpTable == null)
            return;
        tmpTable.setShowVerticalLines(b);
    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.pub.display.BaseMenuManage#itemSet()
     */
    public BasePopupMenu itemCheck() {
        if (popMenu == null)
            createPopMenu();

        DataSetTable tmpTable = (DataSetTable) this.getComponent();

        int[] selectRows = tmpTable.getSelectedRows();
        if (selectRows == null || selectRows.length == 0) //���û��ѡ����ݣ�������
        {
            if (copy.isEnabled())
                copy.setEnabled(false);
            GUIUtil.setComponentEnabled(false, preferWidth);
        } else //ѡ����ݣ�����
        {
            if (!copy.isEnabled())
                copy.setEnabled(true);

            GUIUtil.setComponentEnabled(true, preferWidth);
        }

        if (tmpTable.getRowCount() > 0) //��ؼ������
        {
            if (export.isEnabled())
                export.setEnabled(true);
        } else //��ؼ�û�����
        {
            if (export.isEnabled())
                export.setEnabled(false);
        }

        //�������ò���
        this.isSortable.setSelected(tmpTable.isSortable());
        this.horizontalLine.setSelected(tmpTable.getShowHorizontalLines());
        this.verticalLine.setSelected(tmpTable.getShowVerticalLines());
        this.lineNumber.setSelected(tmpTable.isRowNumberVisible());
        
        super.menuCheck();
        return popMenu;
    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.pub.display.BaseMenuManage#getPopMenu()
     */
    public BasePopupMenu getPopMenu() {
        return itemCheck();
    }

    /**
     * ֱ�ӻ�ȡδ���˵�������Դ���Ĳ˵�����
     * 
     * @return
     */
    public JPopupMenu getNoRenderMenu() {
        return popMenu;
    }
}
