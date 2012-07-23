/*
 * Created on 2007-1-17
 */
package com.cattsoft.coolsql.modifydatabase;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Map;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ToolTipManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.cattsoft.coolsql.exportdata.Actionable;
import com.cattsoft.coolsql.modifydatabase.model.BaseTableCell;
import com.cattsoft.coolsql.modifydatabase.model.CheckBean;
import com.cattsoft.coolsql.modifydatabase.model.ColumnNameTableCell;
import com.cattsoft.coolsql.modifydatabase.model.NewValueTableCell;
import com.cattsoft.coolsql.modifydatabase.model.OldValueTableCell;
import com.cattsoft.coolsql.pub.component.BaseDialog;
import com.cattsoft.coolsql.pub.component.BaseMenuManage;
import com.cattsoft.coolsql.pub.display.EditorSQLArea;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.SqlUtil;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.sql.model.Column;
import com.cattsoft.coolsql.sql.model.DataType;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.sql.util.TypesHelper;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * ����ʵ���еļ�¼ʱ��ѡ���޸���Ӧ���ֶ�
 */
public class UpdateRowDialog extends BaseDialog {
	
	private static final long serialVersionUID = 1L;
	
	private final static String totalColumn=PublicResource.getSQLString("rowupdate.status.totalcolumn");
    private final static String updateColumn=PublicResource.getSQLString("rowupdate.status.updatecolumn");
    private final static String qualifyColumn=PublicResource.getSQLString("rowupdate.status.qualifycolumn");
    /**
     * չʾʵ����Ϣ�����
     */
    private EntityDisplayPanel entityInfoPane = null;

    /**
     * �����м�¼�ı�ؼ�
     */
    private UpdateRowTable table = null;

    //sql��ʾ����
    private EditorSQLArea sqlArea = null;

    //ʵ�������Ӧ��ֵ�������ڸ�ӳ������С�key:���� value�����е�ֵ
    private Map<String,Object> dataMap = null;

    /**
     * ���½���е��ֶ�ʱ,��������ֶβ���ȫ,��ô��Ҫ�ж��Ƿ�δ��ʾ���ֶμ��뵽���µ�������. true:ȫ����ʾ
     * false:��ʾ��Ч��һЩ�ֶ�
     */
    private boolean isDisplayAll;

    /**
     * �Ҽ�˵�������
     */
    private BaseMenuManage popMenuManage = null;

    /**
     * ��ɸ���sql��������
     */
    private String needModifyPartStr = null; //��ֵ����

    private String qualifyPartStr = null; //��������

    public UpdateRowDialog() {
        this(null);
    }

    public UpdateRowDialog(Entity entity) {
        this(entity, (Map<String,Object>) null, false);
    }

    public UpdateRowDialog(Entity entity, Map<String,Object> dataMap, boolean isDisplayAll) {
        super(GUIUtil.getMainFrame());
        this.dataMap = dataMap;
        this.isDisplayAll = isDisplayAll;
        initDialog(entity);
    }

    public UpdateRowDialog(Dialog owner, Entity entity) {
        this(owner, entity, null, false);
    }

    public UpdateRowDialog(Frame owner, Entity entity) {
        this(owner, entity, null, false);
    }

    public UpdateRowDialog(Dialog owner, Entity entity, Map<String,Object> dataMap,
            boolean isDisplayAll) {
        super(owner);
        this.dataMap = dataMap;
        this.isDisplayAll = isDisplayAll;
        initDialog(entity);
    }

    public UpdateRowDialog(Frame owner, Entity entity, Map<String,Object> dataMap,
            boolean isDisplayAll) {
        super(owner);
        this.dataMap = dataMap;
        this.isDisplayAll = isDisplayAll;
        initDialog(entity);
    }

    /**
     * ��ʼ������
     *  
     */
    private void initDialog(Entity entity) {
        setTitle(PublicResource.getSQLString("rowupdate.dialog.title"));

        JPanel pane = (JPanel) getContentPane();
        pane.setLayout(new BorderLayout());

        entityInfoPane = new EntityDisplayPanel(entity) {
			private static final long serialVersionUID = 1L;

			/**
             * ����ʾ�����ʵ�塱��ť
             */
            public boolean isDisplayChangeBtn() {
                return false;
            }
        };
        pane.add(entityInfoPane, BorderLayout.NORTH);

        table = new UpdateRowTable();
        ToolTipManager.sharedInstance().registerComponent(table); //ע����Ϣ��ʾ

        sqlArea = new EditorSQLArea(entity.getBookmark());
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(table), sqlArea);
        splitPane.setDividerSize(4);
        splitPane.setDividerLocation(350);
        pane.add(splitPane, BorderLayout.CENTER);

        //�˵��������ĳ�ʼ��
        popMenuManage = new UpdateRowTableMenuManage(table);
        table.addMouseListener(new MouseAdapter() //Ϊ��ؼ�����Ҽ�˵�
                {
        			boolean isPopupTriggerWhenPress;
        			@Override
        			public void mousePressed(MouseEvent e)
        			{
        				isPopupTriggerWhenPress=e.isPopupTrigger();
        				int row=table.rowAtPoint(e.getPoint());
        				table.getSelectionModel().setSelectionInterval(row, row);
        			}
        			@Override
                    public void mouseReleased(MouseEvent e) {
                        if (isPopupTriggerWhenPress||e.isPopupTrigger())
                            popMenuManage.getPopMenu().show(table, e.getX(),
                                    e.getY());
                    }
                });

        entityInfoPane.addEntityChangeListener(new EntityChangeAction());
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeDialog();
            }
        });
        setSize(560, 530);
        toCenter();

        try {
            loadEntity(entity, dataMap);
        } catch (UnifyException e) {
            LogProxy.errorReport(this, e);
        } catch (SQLException e) {
            LogProxy.SQLErrorReport(this, e);
        }

        table.getModel().addTableModelListener(new CellValueChangeListener()); //��ӱ�ģ�ͼ���
    }

    /**
     * ��ȡ��ǰ���ڴ����ʵ�����
     * 
     * @return --ʵ�����
     */
    public Entity getEntityObject() {
        return entityInfoPane.getEntity();
    }

    /**
     * �رնԻ���
     *  
     */
    public void closeDialog() {
        sqlArea.dispose(); //�رո����߳�
        if (dataMap != null) {
            dataMap.clear();
            dataMap = null;
        }
        dispose();
        removeAll();
    }

    /**
     * װ��ʵ����ݣ�����ؼ��Ľ�����и���.��ȡʵ�������Ϣ�󣬴�����ؼ������
     * warning:������mapΪnull,��ô���α�ؼ�����Ĳ��ֱ���Ϊ�Ǹ�ʵ��;���map��null����ô�Ὣ��map��key�������е��ֶ�����Ϊ���ɹ�ѡ
     * 
     * @param entity
     *            --��ʵ�����
     * @param map
     *            --ʵ��������ֵ��ӳ�����
     * @throws SQLException
     * @throws UnifyException
     */
    private void loadEntity(Entity entity, Map<String,Object> map) throws UnifyException,
            SQLException {

        Column[] columns = entity.getColumns();
        if (columns == null) {
            throw new UnifyException(PublicResource
                    .getSQLString("createsql.noentityinfo"));
        }

        Vector<Vector<Object>> data = new Vector<Vector<Object>>(); //��������
        for (int i = 0; i < columns.length; i++) {
            Vector<Object> rowData = new Vector<Object>();

            if (map == null)
                rowData.add(new CheckBean(new Boolean(false), true));//��һ�У���ѡ��
            else {
                if (map.containsKey(columns[i].getName().toUpperCase())) //�������У���ô�ɹ�ѡ
                    rowData.add(new CheckBean(new Boolean(false), true));
                else {
                    if (isDisplayAll) //����ɸĵ��ֶ�Ҳ��ʾ
                        rowData.add(new CheckBean(new Boolean(false), false)); //���ɹ�ѡ
                    else
                        //�����ʾ���ɸĵ��ֶΣ�ֱ�ӽ�����һ�δ���
                        continue;
                }
            }

            rowData.add(new ColumnNameTableCell(table, columns[i])); //�ڶ��У��ֶ���

            //�����У�ԭֵ
            if (map == null) //��������£�����ʹԭֵ�пɱ༭
            {
                rowData.add(new OldValueTableCell(table, null, true));
            } else //���Ϊ�գ���ȡ�����Ӧ�Ķ���ֵ
            {
                Object ob = map.get(columns[i].getName().toUpperCase());
                String value = ob == null ? "" : ob.toString();
                rowData.add(new OldValueTableCell(table, value));
            }

            //�����У���ֵ��
            rowData.add(new NewValueTableCell(table, "", EditorFactory
                    .getEditorTypeBySQLType(columns[i].getType())));

            data.add(rowData);
        }
        table.replaceData(data);
        
        qualifyPartStr=getQualifyPart();  //װ��ʵ���ͬʱ�������ֶθ���
    }

    /**
     * @return Returns the needModifyPartStr.
     */
    public String getNeedModifyPartStr() {
        return needModifyPartStr;
    }

    /**
     * @return Returns the qualifyPartStr.
     */
    public String getQualifyPartStr() {
        return qualifyPartStr;
    }

    /**
     * ���ظ���sql�У��ֶθ�ֵ�Ĳ���
     * 
     * @return --(String) ���ظ���sql�У��ֶθ�ֵ�Ĳ���
     * @throws UnifyException
     */
    public String getEvaluatePart() throws UnifyException {
        int count = 0; //������Ҫ�޸ĵ��ֶ�����
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < table.getRowCount(); i++) {
            CheckBean bean = (CheckBean) table.getValueAt(i, 0); //��ȡѡ��״̬�ı��Ԫ��ֵ
            if (!bean.getSelectValue().booleanValue()) //���û��ѡ��,ֱ�ӽ�����һ�ִ���
                continue;

            TableCell columnCell = (TableCell) table.getValueAt(i, 1); //��ȡ�ֶ��еı��Ԫ��ֵ
            TableCell newValueCell = (TableCell) table.getValueAt(i, 3); //��ȡ��ֵ�еı��Ԫ��ֵ

            Column col = (Column) columnCell.getValue();
            String newValue = (String) newValueCell.getValue();

            //��ȡ��Ӧ���������
            DataType dataType = col.getParentEntity().getBookmark()
                    .getDbInfoProvider().getDataType(col.getTypeName());
                
            if (TypesHelper.isLob(dataType.getJavaType())) {
                buffer.append(col.getName() + "=?,");
            } else if(newValueCell.isNullValue())
            {
                buffer.append(col.getName() + "=null,");
            } else {
            	if (dataType == null)
                {
                	buffer.append(col.getName()).append("=").append(newValue==null?"null":SqlUtil.qualifyColumnValue(newValue, col.getType())).append(",");
                }else
                {
	                String currentStr =(dataType.getLiteralPrefix()==null?"": dataType.getLiteralPrefix())
	                        + newValue
	                        + (dataType.getLiteralSuffix()==null?"":dataType.getLiteralSuffix());
	                buffer.append(col.getName()).append("=").append(currentStr + ",");
                }
            }
            count++;
        }
        if (count > 0)
            buffer.deleteCharAt(buffer.length() - 1); //��ȥ���һ���ַ�,��
        return buffer.toString();
    }

    /**
     * ��ȡ����sql�е���������
     * 
     * @return --����sql�е���������
     * @throws UnifyException
     */
    public String getQualifyPart() throws UnifyException {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < table.getRowCount(); i++) {
            TableCell columnCell = (TableCell) table.getValueAt(i, 1); //��ȡ�ֶ��еı��Ԫ��ֵ
            if (columnCell.isAsTerm()) {
				Column col = (Column) columnCell.getValue();
				// ��ȡ��Ӧ���������
				DataType dataType = col.getParentEntity().getBookmark()
						.getDbInfoProvider().getDataType(col.getTypeName());
				if (TypesHelper.isLob(dataType.getJavaType())) // ����Ϊ�����ƶ��󣬲�������
					continue;

				TableCell oldValueCell = (TableCell) table.getValueAt(i, 2); // ��ȡԭֵ�еı��Ԫ��ֵ
				String value = (String) oldValueCell.getValue();

				if (value != null) // ���ԭֵ������ֵ��Ϊnullʱ
					if (dataType == null) {
						buffer.append(" ").append(col.getName()).append(" = ")
								.append(SqlUtil.qualifyColumnValue(value.toString(), col.getType()));
					} else {
						buffer.append(" ")
								.append(col.getName())
								.append("=")
								.append(
										(dataType.getLiteralPrefix() == null
												? ""
												: dataType.getLiteralPrefix())
												+ value.trim()
												+ (dataType.getLiteralSuffix() == null
														? ""
														: dataType
																.getLiteralSuffix()))
								.append(" and");
					}
				else
					// Ϊnullʱ
					buffer.append(" ").append(col.getName()).append(
							" is null and");
			}
        }
        if (buffer.length() > 0)
            buffer.delete(buffer.length() - 3, buffer.length()); // ɾ�����Ĵ�"and"
        return buffer.toString();
    }

    /**
     * ����״̬��Ϣ������Ϣ��¼�˵�ǰ���ѡ���޸�״̬
     * @return
     */
    public String getStatueInfo()
    {
        int updateCols=0;
        int qualifyCols=0;
        for(int i=0;i<table.getRowCount();i++)
        {
            CheckBean bean = (CheckBean) table.getValueAt(i, 0); //��ȡѡ��״̬�ı��Ԫ��ֵ
            if(bean.getSelectValue().booleanValue())
                updateCols++;
            
            TableCell columnCell = (TableCell) table.getValueAt(i, 1); //��ȡ�ֶ��еı��Ԫ��ֵ
            if(columnCell.isAsTerm())
                qualifyCols++;
        }
        return totalColumn+table.getRowCount()+"\t"+updateColumn+updateCols+"\t\t"+qualifyColumn+qualifyCols;
    }
    /**
     * ����sql��ʾ�����sql
     *
     */
    public void updateSQLInArea()
    {
        
        StringBuffer buffer=new StringBuffer();
        String tmp=StringUtil.trim(getEntityObject().getSchema());
        if(!tmp.equals(""))
        	tmp=tmp+".";
        buffer.append("update "+tmp+getEntityObject().getQualifiedName()+" set ");
        buffer.append(getNeedModifyPartStr()==null?"":getNeedModifyPartStr());
        buffer.append(StringUtil.trim(getQualifyPartStr()).equals("")?"":(" where "+getQualifyPartStr()));
        
        sqlArea.setText(buffer.toString());
    }
    private class EntityChangeAction implements Actionable {

        /*
         * (non-Javadoc)
         * 
         * @see com.coolsql.exportdata.Actionable#action()
         */
        public void action() {
            sqlArea.setText("");
            try {
                loadEntity(entityInfoPane.getEntity(), dataMap);

                if (dataMap != null) {
                    //��ʵ���,����ݾ�ӳ�����ɾ��
                    dataMap.clear();
                    dataMap = null;
                }
            } catch (UnifyException e) {
                LogProxy.errorReport(UpdateRowDialog.this, e);
            } catch (SQLException e) {
                LogProxy.SQLErrorReport(UpdateRowDialog.this, e);
            }
        }

    }

    /**
     * 
     * @author liu_xlin ���Ԫ��ֵ�����º󣬽�����Ӧ���? 1����һ�У�ѡ��״̬�������ֵ�����б����ݶ���ĵ�ɫ���и���
     *         2��ͬʱ����ֵ�е��Ƿ�ɱ༭���Խ��е���
     */
    private class CellValueChangeListener implements TableModelListener {

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
         */
        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE) //������Ԫ��ֵ����������
            {
                if (e.getColumn() == 0) {

                    int row = e.getFirstRow();
                    CheckBean bean = (CheckBean) table.getValueAt(row, 0);
                    if (bean.getSelectValue().booleanValue()) //�����б�ѡ�У�����ɫ��Ϊָ��ɫ
                    {
                        table.setRowBackgroundColor(row,
                                UpdateRowTable.cellBackground);
                        BaseTableCell cell = (BaseTableCell) table.getValueAt(
                                row, 3);
                        cell.setEditable(true);

                    } else //������δ��ѡ��
                    {
                        //                        setRowBackgroundColor(row,getSelectionBackground());
                        table.setRowBackgroundColor(row, null);
                        BaseTableCell cell = (BaseTableCell) table.getValueAt(
                                row, 3);
                        cell.setEditable(false);
                    }
                    
                    repaint();
                    
                    try {
                        needModifyPartStr = getEvaluatePart();  //���¸�ֵ����
                    } catch (UnifyException e1) {
                       LogProxy.errorReport(UpdateRowDialog.this,e1);
                       return;
                    }
                    
                    updateSQLInArea();
                    
                    sqlArea.setStatueInfo(getStatueInfo());
                } else if (e.getColumn() == 1) //����һ�з���仯��һ�㷢�����Ԫ�ص��Ƿ���Ϊ����������ֵ�����˱仯
                {
                    try {
                        qualifyPartStr = getQualifyPart();  //������������
                    } catch (UnifyException e1) {
                       LogProxy.errorReport(UpdateRowDialog.this,e1);
                       return;
                    }
                    
                    updateSQLInArea();
                    sqlArea.setStatueInfo(getStatueInfo());
                } else if (e.getColumn() == 3) {
                    try {
                        needModifyPartStr = getEvaluatePart();  //���¸�ֵ����
                    } catch (UnifyException e1) {
                        LogProxy.errorReport(UpdateRowDialog.this,e1);
                        return;
                    }
                    
                    updateSQLInArea();
                    sqlArea.setStatueInfo(getStatueInfo());
                }
            }
        }

    }
}
