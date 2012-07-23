/*
 * �������� 2006-9-15
 */
package com.cattsoft.coolsql.pub.display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.KeyStroke;

import com.cattsoft.coolsql.action.common.ExportExcelOfTableAction;
import com.cattsoft.coolsql.action.common.ExportHtmlOfTableAction;
import com.cattsoft.coolsql.action.common.ExportTextOfTableAction;
import com.cattsoft.coolsql.action.common.TableCopyAction;
import com.cattsoft.coolsql.pub.component.BaseMenuManage;
import com.cattsoft.coolsql.pub.component.BasePopupMenu;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.system.menubuild.IconResource;

/**
 * @author liu_xlin ��ؼ��Ҽ���?���ܰ�:���ƣ���ݵ�������ӡ
 */
public class TableMenu extends BaseMenuManage {
    public static ActionListener printAction = null;

    /**
     * ����
     */
    private JMenuItem copy;

    /**
     * ����
     */
    private JMenu export;

    //�������ı�
    private JMenuItem exportTxt;

    //������excel�ļ�
    private JMenuItem exportExcel;

    //��������ҳ
    private JMenuItem exportHtml;

    private JMenuItem findInfo; //���������Ϣ
    private JMenuItem adjustWidth; //�����п�
    private JCheckBoxMenuItem isToolTip;//�Ƿ���б����Ϣ����ʾ
    /**
     * �������
     */
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
     * ��ӡ
     */
    private JMenuItem print;

    //�Ƿ���ʾ��ӡ��
    private boolean isPrintable = false;

    //�Ƿ���ʵ��ݵ�����
    private boolean isExport = true;

    /**
     * @param com
     */
    public TableMenu(JTable table) {
        super(table);

    }

    protected void createPopMenu() {
        popMenu = new BasePopupMenu();

        //���Ʋ˵��ĳ�ʼ��
        Action copyAction = new TableCopyAction((JTable)getComponent());
        copy = createMenuItem(PublicResource
                .getString("TextEditor.popmenu.copy"), IconResource.ICON_COPY, copyAction);
        copy.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
        popMenu.add(copy);

        
        
        boolean isAddSeparator=false; //�ж��Ƿ���ӷָ���
        
        if(getComponent()!=null&&getComponent() instanceof BaseTable)
        {
        	popMenu.addSeparator(); //��ӷָ���
        	isAddSeparator=true;
        	
        	BaseTable tmpTable=(BaseTable)getComponent();
        	findInfo=createMenuItem(PublicResource                      //���Ҳ˵�
                .getString("table.popup.lable.findInfo"), PublicResource
                .getIcon("table.popup.icon.findInfo"), tmpTable.getDefaultFindAction());
        	popMenu.add(findInfo);
        	findInfo.setAccelerator(KeyStroke.getKeyStroke("alt F"));
        	
        	//�����п�˵���
        	adjustWidth=createMenuItem(PublicResource
                    .getString("table.popup.lable.adjustWidth"), PublicResource
                    .getIcon("table.popup.icon.adjustWidth"), tmpTable.getDefaultAdjustWidthAction());
        	popMenu.add(adjustWidth);
        	adjustWidth.setAccelerator(KeyStroke.getKeyStroke("alt W"));
        	
        	if (getComponent() instanceof CommonDataTable) // ���ΪCommonDataTable���͵�table�ؼ���������Ƿ���ʾ��ʾ��Ϣ��ѡ��˵�
			{
        		if (((CommonDataTable)getComponent()).isDisplayToolTipSelectMenu()) {  //�����ʾ����ʾ�˵��
					isToolTip = new JCheckBoxMenuItem(PublicResource
							.getString("table.popup.lable.istooltip"));
					isToolTip.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {

							((CommonDataTable) getComponent())
									.setEnableToolTip(isToolTip.getState());
						}

					});
					popMenu.add(isToolTip);
				}
			}
        }
        if (isExportable()) {
        	if(!isAddSeparator)
        		popMenu.addSeparator(); //��ӷָ���
            //������ݲ˵�
            export = new JMenu(PublicResource.getString("table.popup.export"));
            export.setIcon(IconResource.getBlankIcon());
            popMenu.add(export);

            //�����ı��˵�
            exportTxt = createMenuItem(PublicResource
                    .getString("table.popup.exportTxt"), PublicResource
                    .getIcon("table.popup.export.txticon"),
                    new ExportTextOfTableAction((JTable)getComponent()));
            export.add(exportTxt);

            //����excel�˵�
            exportExcel = createMenuItem(PublicResource
                    .getString("table.popup.exportExcel"), PublicResource
                    .getIcon("table.popup.export.excelicon"),
                    new ExportExcelOfTableAction((JTable)getComponent()));
            export.add(exportExcel);

            //������ҳ�˵�
            exportHtml = createMenuItem(PublicResource
                    .getString("table.popup.exportHtml"), PublicResource
                    .getIcon("table.popup.export.htmlicon"),
                    new ExportHtmlOfTableAction((JTable)getComponent()));
            export.add(exportHtml);
        }

        if (isPrintable()) {
        	if(!isAddSeparator)
        		popMenu.addSeparator(); //��ӷָ���
            //��ӡ
            print = createMenuItem(PublicResource
                    .getString("table.popup.print"), PublicResource
                    .getIcon("table.popup.print.icon"), printAction);
            popMenu.add(print);
        }

        if (isTableLineSetting()) {
        	if(!isAddSeparator)
        		popMenu.addSeparator(); //��ӷָ���
            //���������
            tableLine = new JMenu(PublicResource
                    .getString("table.popup.tableline"));
            tableLine.setIcon(IconResource.getBlankIcon());
            popMenu.add(tableLine);

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
        }
        lineNumber=new JCheckBoxMenuItem(PublicResource
                .getString("table.popup.linenumber"));
        lineNumber.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	((BaseTable)getComponent()).setTableRowNumberVisible(lineNumber.getState());
            }

        });
        popMenu.add(lineNumber);
    }
    /**
     * ���Ҫ����µĲ˵����ȡ�����λ��
     * @return --����Ӳ˵����λ��,�����˵�����û�г�ʼ��������-1
     * @override 
     */
    public int getAddPostion() {
        int index = -1;
        if (popMenu == null)
            return -1;
        
        if (findInfo != null) {
            index = popMenu.getComponentIndex(findInfo);
            if (index != -1) //�����Ҳ˵����Ѿ�����ڲ˵���
                return index - 1;
        }
        //��Ϊ���Ҳ˵��͵����Ȳ˵��϶�ͬʱ��ʾ/����ʾ���ԣ�ֱ���ж�export�˵�
        if (export != null) {
            index = popMenu.getComponentIndex(export);
            if (index != -1) //�����˵����Ѿ�����ڲ˵���
                return index - 1;
        }
        if (print != null) {
            index = popMenu.getComponentIndex(print);
            if (index != -1) //����ӡ�˵����Ѿ�����ڲ˵���
                return index - 1;
        }
        if (tableLine != null) {
            index = popMenu.getComponentIndex(tableLine);
            if (index != -1) //��������ò˵����Ѿ�����ڲ˵���
                return index - 1;
        }
        //�����˵��ʹ�ӡ�˵���û�б���ӣ���ô�������
        return popMenu.getComponentCount();

    }

    private void setHorizontalLine(boolean b) {
        JTable tmpTable = (JTable) getComponent();
        if (tmpTable == null)
            return;
        tmpTable.setShowHorizontalLines(b);
    }

    private void setVerticalLine(boolean b) {
        JTable tmpTable = (JTable) getComponent();
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
        int[] selects = ((JTable) getComponent()).getSelectedRows();
        if (selects == null || selects.length == 0) {
            if (copy.isEnabled())
                copy.setEnabled(false);
        } else {
            if (!copy.isEnabled())
                copy.setEnabled(true);
        }
        
        if(isToolTip!=null)
        	isToolTip.setSelected(((CommonDataTable)getComponent()).isToolTip());
        //�������ò���
        horizontalLine.setSelected(((JTable) getComponent()).getShowHorizontalLines());
        verticalLine.setSelected(((JTable) getComponent()).getShowVerticalLines());

        lineNumber.setSelected(((CommonDataTable) getComponent()).isRowNumberVisible());
        
        menuCheck();
        return popMenu;
    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.pub.display.BaseMenuManage#getPopMenu()
     */
    public BasePopupMenu getPopMenu() {
        if (popMenu == null)
            createPopMenu();

        return itemCheck();
    }

    /**
     * �����Ҽ�˵��Ƿ����ݵ�����,������ֻ�ڲ˵���ʼ��ǰ��Ч
     */
    public void setExportable(boolean isexport) {
        isExport = isexport;
    }

    /**
     * �Ƿ���ʾ������ݲ˵���
     * 
     * @return
     */
    public boolean isExportable() {
        return isExport;
    }

    /**
     * ���øò˵��Ƿ���ӡ��ť
     * 
     * @return
     */
    public boolean isPrintable() {
        return isPrintable;
    }

    /**
     * �����Ƿ���ʾ��������ò˵���,�ɸ��Ǹ÷���
     * 
     * @return
     */
    public boolean isTableLineSetting() {
        return true;
    }

    /**
     * �����Ƿ���ʾ��ӡ��
     * 
     * @param isPrintable
     */
    public void setPrintable(boolean isPrintable) {
        this.isPrintable = isPrintable;
        if (!isPrintable) //����ʾ��ӡ��
        {
            if (popMenu != null) {
                popMenu.remove(print);
                print.removeActionListener(printAction);
                print.removeAll();
                print = null;
            }
        } else //��ʾ��ӡ��ť
        {
            if (popMenu != null) {
                if (print == null) {
                    //��ӡ
                    print = createMenuItem(PublicResource
                            .getString("table.popup.print"), PublicResource
                            .getIcon("table.popup.print.icon"), printAction);
                    int index = popMenu.getComponentIndex(export) + 1;
                    popMenu.insert(print, index);
                }
            }
        }
    }
}
