/*
 * Created on 2007-1-24
 */
package com.cattsoft.coolsql.modifydatabase;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;

import com.cattsoft.coolsql.gui.property.database.ColumnProperty;
import com.cattsoft.coolsql.modifydatabase.model.BaseTableCell;
import com.cattsoft.coolsql.pub.component.BaseMenuManage;
import com.cattsoft.coolsql.pub.component.BasePopupMenu;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.sql.commonoperator.EntityPropertyOperator;
import com.cattsoft.coolsql.sql.commonoperator.Operatable;
import com.cattsoft.coolsql.sql.commonoperator.OperatorFactory;
import com.cattsoft.coolsql.sql.model.Column;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;
import com.cattsoft.coolsql.view.bookmarkview.model.TableNode;
import com.cattsoft.coolsql.view.bookmarkview.model.ViewNode;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin ��������ݴ����б�ؼ����Ҽ�˵�������
 */
public class UpdateRowTableMenuManage extends BaseMenuManage {

    private JCheckBoxMenuItem asTermItem = null; //�����ֶ��Ƿ�Ϊ��ѯ�����Ĳ˵�

    private JCheckBoxMenuItem setAsNull = null; //�Ƿ�����Ϊnullֵ

    private JMenuItem detailColumnItem = null; //�鿴�е���ϸ��Ϣ

    private JMenuItem detailEntityItem = null; //�鿴ʵ����ϸ��Ϣ

    private UpdateRowDialog dialog = null;

    public UpdateRowTableMenuManage(UpdateRowTable table) {
        super(table);
        dialog = (UpdateRowDialog) GUIUtil.getUpParent(table,
                UpdateRowDialog.class);
    }

    protected void createPopMenu() {
        if (popMenu == null) {
            popMenu = new BasePopupMenu();

            //��Ϊ��������
            asTermItem = new JCheckBoxMenuItem(PublicResource
                    .getSQLString("rowupdate.popmenu.asterm.label"));
            asTermItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    UpdateRowTable table = (UpdateRowTable) getComponent();
                    BaseTableCell tableCell = (BaseTableCell) table.getValueAt(
                            table.getSelectedRow(), 1);//��ȡ������Ԫ��ֵ
                    tableCell.setAsTerm(asTermItem.getState());
                    table.setValueAt(tableCell, table.getSelectedRow(), 1);
                }

            });
            popMenu.add(asTermItem);

            //�Ƿ�����Ϊnullֵ�˵���
            setAsNull = new JCheckBoxMenuItem(PublicResource
                    .getSQLString("rowupdate.popmenu.setasnull.label"));
            setAsNull.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    UpdateRowTable table = (UpdateRowTable) getComponent();
                    BaseTableCell tableCell = (BaseTableCell) table.getValueAt(
                            table.getSelectedRow(), 3);//��ȡ��ֵ��Ԫ��ֵ
                    tableCell.setIsNullValue(setAsNull.getState());
                    table.setValueAt(tableCell, table.getSelectedRow(), 3);

                }

            });
            popMenu.add(setAsNull);

            //�鿴����Ϣ�˵���
            ActionListener columnPropertyListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    UpdateRowTable table = (UpdateRowTable) getComponent();

                    Column data = (Column) ((TableCell) table.getValueAt(table
                            .getSelectedRow(), 1)).getValue();//��ȡѡ�������Ӧ���ж���

                    ColumnProperty cp = null;
                    cp = new ColumnProperty(dialog, data, data
                            .getParentEntity().getBookmark());

                    cp.initData(data);
                    cp.setVisible(true);

                }

            };
            detailColumnItem = this.createMenuItem(PublicResource
                    .getSQLString("rowupdate.popmenu.detailcolumn.label"),
                    BookMarkPubInfo.getIconList()[BookMarkPubInfo.NODE_COLUMN],
                    columnPropertyListener);
            popMenu.add(detailColumnItem);

            //�鿴ʵ����Ϣ�˵���
            ActionListener entityPropertyListener = new ActionListener() {
                public void actionPerformed(ActionEvent e1) {
                    Entity entity = dialog.getEntityObject();
                    String type = entity.getType();
                    Identifier id = null;
                    if (type == "VIEW") {
                        id = new ViewNode(entity.getName(), entity
                                .getBookmark(), entity);
                    } else {
                        id = new TableNode(entity.getName(), entity
                                .getBookmark(), entity);
                    }

                    try {
                        Operatable operator = OperatorFactory
                                .getOperator(EntityPropertyOperator.class);
                        List list = new ArrayList();
                        list.add(id);
                        list.add(dialog);
                        operator.operate(list);
                    } catch (ClassNotFoundException e) {
                        LogProxy.errorReport(dialog, e);
                    } catch (InstantiationException e) {
                        LogProxy.internalError(e);
                    } catch (IllegalAccessException e) {
                        LogProxy.internalError(e);
                    } catch (UnifyException e) {
                        LogProxy.errorReport(dialog, e);
                    } catch (SQLException e) {
                        LogProxy.SQLErrorReport(dialog, e);
                    }
                }
            };
            detailEntityItem = this.createMenuItem(PublicResource
                    .getSQLString("rowupdate.popmenu.detailentity.label"),
                    BookMarkPubInfo.getIconList()[BookMarkPubInfo.NODE_TABLE],
                    entityPropertyListener);
            popMenu.add(detailEntityItem);
        }
    }

    /*
     * ��ҪУ��˵���asTermItem��detailColumnItem
     * 
     * @see com.coolsql.pub.display.BaseMenuManage#itemSet()
     */
    public BasePopupMenu itemCheck() {
        if (popMenu == null)
            createPopMenu();

        UpdateRowTable table = (UpdateRowTable) getComponent();
        int selectRow = table.getSelectedRow();
        if (selectRow < 0) {
            GUIUtil.setComponentEnabled(false, asTermItem);
            GUIUtil.setComponentEnabled(false, detailColumnItem);//
            GUIUtil.setComponentEnabled(false, setAsNull);
        } else {
            //�Ƿ���Ϊ��������
            GUIUtil.setComponentEnabled(true, asTermItem);
            TableCell cellValue = (TableCell) table.getValueAt(selectRow, 1);//��ȡѡ�����е�һ�ж�Ӧ��Ԫ�ض���ֵ
            asTermItem.setSelected(cellValue.isAsTerm()); //���Ԫ�ص�ǰ��״ֵ̬�����趨�˵����ѡ��״̬

            //�Ƿ�Ϊnull
            cellValue = (TableCell) table.getValueAt(selectRow, 3);//��ȡѡ�����е����ж�Ӧ��Ԫ�ض���ֵ
            if (cellValue.isEditable())
                GUIUtil.setComponentEnabled(true, setAsNull);
            else
                GUIUtil.setComponentEnabled(false, setAsNull);           
            setAsNull.setSelected(cellValue.isNullValue()); //���Ԫ�ص�ǰ��״ֵ̬�����趨�˵����ѡ��״̬

            //��������Ϣ
            GUIUtil.setComponentEnabled(true, detailColumnItem);
        }

        return popMenu;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coolsql.pub.display.BaseMenuManage#getPopMenu()
     */
    public BasePopupMenu getPopMenu() {
        return itemCheck();
    }
}
