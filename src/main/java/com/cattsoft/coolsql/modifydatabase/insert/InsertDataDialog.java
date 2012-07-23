/*
 * Created on 2007-1-31
 */
package com.cattsoft.coolsql.modifydatabase.insert;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;

import com.cattsoft.coolsql.action.common.ActionCommand;
import com.cattsoft.coolsql.adapters.dialect.DialectFactory;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.exportdata.Actionable;
import com.cattsoft.coolsql.modifydatabase.EntityDisplayPanel;
import com.cattsoft.coolsql.modifydatabase.insert.action.BatchPasteCommand;
import com.cattsoft.coolsql.pub.component.BaseDialog;
import com.cattsoft.coolsql.pub.component.BaseMenuManage;
import com.cattsoft.coolsql.pub.component.DefinableToolBar;
import com.cattsoft.coolsql.pub.component.DisplayPanel;
import com.cattsoft.coolsql.pub.component.IconButton;
import com.cattsoft.coolsql.pub.component.SelectIconButton;
import com.cattsoft.coolsql.pub.component.WaitDialog;
import com.cattsoft.coolsql.pub.component.WaitDialogManage;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.display.StatusBar;
import com.cattsoft.coolsql.pub.display.TableScrollPane;
import com.cattsoft.coolsql.pub.display.exception.NotRegisterException;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.SqlUtil;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.sql.Database;
import com.cattsoft.coolsql.sql.commonoperator.ColumnPropertyOperator;
import com.cattsoft.coolsql.sql.commonoperator.Operatable;
import com.cattsoft.coolsql.sql.commonoperator.OperatorFactory;
import com.cattsoft.coolsql.sql.model.Column;
import com.cattsoft.coolsql.sql.model.DataType;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.view.log.LogProxy;
import com.cattsoft.coolsql.view.resultset.ColumnDisplayDefinition;

/**
 * @author liu_xlin ������ݶԻ��򣬸ô����У�ͨ��ѡ���Ӧ��ʵ�����Ȼ�������Ӧ�ֶε����ֵ�����������Ӧ�Ĳ�����䡣
 *         ���ڿɱ༭�ı����༭��ʽֻ�Ǹ���ֶεĳ��ȿ����˱��Ԫ�������������󳤶ȡ������Է���Ľ�����������ճ��
 */
public class InsertDataDialog extends BaseDialog {
	private static final long serialVersionUID = 1L;

	private final static String selectRowsLabel = PublicResource
            .getSQLString("rowinsert.dialog.status.selectrows.label");

    private final static String selectColumnsLabel = PublicResource
            .getSQLString("rowinsert.dialog.status.selectcolumns.label");

    /**
     * �������ж������ذ�ť
     */
    private IconButton addSingle = null; //��ӵ���

    private IconButton addMuti = null; //��Ӷ���

    private IconButton deleteSelect = null; //ɾ��ѡ��

    private SelectIconButton preview = null; //Ԥ��

    private SelectIconButton exportSQL = null; //����sql���

//    private IconButton execute = null; //ִ��
    /**
     * չʾʵ����Ϣ�����
     */
    private EntityDisplayPanel entityInfoPane = null;

    /**
     * �ɱ༭��ؼ�
     */
    private EditorTable editTable;

    private BaseMenuManage tablePopMenu = null;

    /**
     * ״̬��
     */
    private StatusBar statusBar1 = null; //��ʾѡ�еĵ�һ�����λ��

    private StatusBar statusBar2 = null; //��ʾѡ�е���������

    private StatusBar statusBar3 = null; //��ʾ��ؼ�����������������

    /**
     * ����sql����������ֶβ��ֵ��ַ�ֵ
     */
    private String constantOfEntity;

    /**
     * Indicate whether the mouse is pressed, A true value means mouse is pressed.
     */
    private boolean isMousePressed=false;
    public InsertDataDialog(JFrame frame) {
        this(frame, null);
    }

    public InsertDataDialog(JFrame frame, Entity entity) {
        super(frame);
        constantOfEntity = "";
        createGUI(entity);
    }

    public InsertDataDialog(JDialog frame) {
        this(frame, null);
    }

    public InsertDataDialog(JDialog frame, Entity entity) {
        super(frame);
        constantOfEntity = "";
        createGUI(entity);
    }

    public InsertDataDialog(JFrame frame, boolean isModel) {
        this(frame, isModel, null);
    }

    public InsertDataDialog(JFrame frame, boolean isModel, Entity entity) {
        super(frame, isModel);
        constantOfEntity = "";
        createGUI(entity);
    }

    public InsertDataDialog(JDialog frame, boolean isModel) {
        this(frame, isModel, null);
    }

    public InsertDataDialog(JDialog frame, boolean isModel, Entity entity) {
        super(frame, isModel);
        constantOfEntity = "";
        createGUI(entity);
    }

    /**
     * ��ʼ���������
     *  
     */
    @SuppressWarnings("unchecked")
    protected void createGUI(Entity entity) {
        setTitle(PublicResource.getSQLString("rowinsert.dialog.title"));
        JPanel main = (JPanel) getContentPane();

        //��ӹ�����
        main.add(createToolBar(), BorderLayout.NORTH);

        JPanel pane = new JPanel();
        pane.setLayout(new BorderLayout());

        entityInfoPane = new EntityDisplayPanel(entity);
        entityInfoPane.addEntityChangeListener(new Actionable() //���ʵ�巢��仯
                {

                    public void action() {
                        try {
                            loadNewEntity(entityInfoPane.getEntity());
                        } catch (UnifyException e) {
                            LogProxy.errorReport(InsertDataDialog.this, e);
                        } catch (SQLException e) {
                            LogProxy.SQLErrorReport(InsertDataDialog.this, e);
                        }

                    }

                });
        pane.add(entityInfoPane, BorderLayout.NORTH);
        try {
            editTable = new EditorTable((List) null, parseEntity(entity))
            {
				private static final long serialVersionUID = 1L;

				@Override
				public void changeSelection(int rowIndex, int columnIndex,
						boolean toggle, boolean extend) {
					super.changeSelection(rowIndex, columnIndex, toggle,extend);
					if(!isMousePressed)
						updateStatus();
				}
			}
            ;
        } catch (SQLException e) {
            LogProxy.SQLErrorReport(e);
            editTable = new EditorTable(new EditorTableModel());
        } catch (Exception e) { //����׳��쳣������ؼ���ʼ��Ϊһ���ձ�
            LogProxy.errorReport(e);
            editTable = new EditorTable(new EditorTableModel());
        }
        editTable.setHeaderPopMenu(true);
        editTable.setHeaderPopMenu(new TableHeaderMenu());
        createShortcut();
        //        editeTable.setEnableToolTip(false);
        pane.add(new TableScrollPane(editTable), BorderLayout.CENTER);

        tablePopMenu = new EditorTableMenuManage(editTable);
        editTable.setMenuManage(tablePopMenu); //���ؼ�����Ҽ�˵�
        
        main.add(pane, BorderLayout.CENTER);

        /**
         * ״̬���ĳ�ʼ��
         */
        JPanel statusPane = new JPanel();
        statusPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.gridx = 0;

        statusBar3 = new StatusBar(12);
        statusBar3.setToolTipText(PublicResource
                .getSQLString("rowinsert.dialog.status.tablescale"));
        statusPane.add(statusBar3, gbc);

        gbc.gridx++;
        statusBar1 = new StatusBar(12);
        statusBar1.setToolTipText(PublicResource
                .getSQLString("rowinsert.dialog.status.selectpoint"));
        statusPane.add(statusBar1, gbc);

        gbc.weightx = 1;
        gbc.gridx++;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        statusBar2 = new StatusBar();
        statusBar2.setToolTipText(PublicResource
                .getSQLString("rowinsert.dialog.status.selectcells"));
        statusPane.add(statusBar2, gbc);
        main.add(statusPane, BorderLayout.SOUTH);

        setSize(850, 550);
        toCenter();
        loadStatusListener();
    }

    /**
     * 
     * Ϊeditetable������ݼ� crtl+v :����ճ�� DELETE ��ɾ��ѡ�����ֵ ctrl+A �����б��ѡ��
     */
    protected void createShortcut() {
        /**
         * ����ճ�������
         */
        Action pastAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
                ActionCommand command = new BatchPasteCommand(editTable);
                try {
					command.exectue();
				} catch (Exception e1) {
					LogProxy.errorReport(e1);
				}
            }

        };
        GUIUtil.bindShortKey(editTable, "control V", pastAction, false);

        /**
         * ɾ��Ԫ������
         */
        Action delAction = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
                int[] selectrow = editTable.getSelectedRows();
                int[] selectcolumn = editTable.getSelectedColumns();
                if (selectrow == null || selectrow.length < 1
                        || selectcolumn == null || selectcolumn.length < 1)
                    return;

                for (int i = 0; i < selectrow.length; i++) {
                    for (int j = 0; j < selectcolumn.length; j++) {
                        int row = selectrow[i];
                        int column = selectcolumn[j];
                        EditeTableCell cell = (EditeTableCell) editTable
                                .getValueAt(row, column);
                        cell.setEmpty();
                        editTable.setValueAt(cell, row, column);
                    }
                }
            }

        };
        GUIUtil.bindShortKey(editTable, "DELETE", delAction, false);

        /**
         * ȫѡ����
         */
        Action selectAllAction = new SelectAllAction();
        GUIUtil.bindShortKey(editTable, "ctrl A", selectAllAction, false);
    }

    /**
     * �����µ�ʵ����Ϣ
     * 
     * @param entity
     *            --��ʵ�����
     * @throws UnifyException
     * @throws SQLException
     */
    public void loadNewEntity(Entity entity) throws UnifyException,
            SQLException {
        ColumnDisplayDefinition[] def = parseEntity(entity);
        editTable.setDataList(null, def);

        addOneRowToTable();//���һ��,׼�������Ϣ���
        editTable.adjustPerfectWidth();
//        updateConstantOfSQL(entity);
    }

    /**
     * ���¶�������constantOfEntity��ֵ
     *  
     */
    private void updateConstantOfSQL(Entity entity, boolean isFullQualifiedName) {
        if (entity == null)
            return;
        StringBuilder sb=new StringBuilder("INSERT INTO " + (isFullQualifiedName ? entity.getQualifiedName() : entity.getName()) + " (");

        TableColumnModel columnModel = editTable.getColumnModel();
        int i = 0;
        for (; i < columnModel.getColumnCount(); i++) {
            TableHeaderCell cell = (TableHeaderCell) columnModel.getColumn(i)
                    .getHeaderValue();
            if (!cell.getState()) //û��ѡ�и���
                continue;

            Column col = (Column) cell.getHeaderValue();
            sb.append(col.getName() + ",");
        }
        sb.deleteCharAt(sb.length()-1).append(") VALUES ");
        constantOfEntity=sb.toString();

    }

    /**
     * //�ڱ�ؼ������һ��
     *  
     */
    private void addOneRowToTable() {
        if (!(editTable.getModel() instanceof EditorTableModel)
                || editTable.getColumnCount() < 1) //����ؼ���ģ�Ͷ�����EditorTableModel���ͣ�����������
            return;

        //�ڱ�ؼ������һ��
        EditorTableModel model = (EditorTableModel) editTable.getModel();
        model.addRow(EditorTableModel.getEmptyCell(editTable
                .getColumnCount()));
    }

    /**
     * ����״̬��Ϣ�ļ��� ��ʼ��״̬��Ϣ
     */
    private void loadStatusListener() {
        editTable.getModel().addTableModelListener(new TableStructListener());
        StatusChangeListener mouseListener = new StatusChangeListener();
        editTable.addMouseListener(mouseListener);
        editTable.addMouseMotionListener(mouseListener);
        addOneRowToTable();
        editTable.adjustPerfectWidth();
    }

    /**
     * ���ʵ��������Ϊ�༭��ؼ���EditorTable�����ж����������
     * 
     * @param entity
     *            --ʵ�����
     * @return --��ؼ����ж����������
     * @throws UnifyException
     *             --��ȡʵ����г���ʱ���׳����쳣
     * @throws SQLException
     *             --��ȡʵ����г���ʱ���׳����쳣
     */
    public ColumnDisplayDefinition[] parseEntity(Entity entity)
            throws UnifyException, SQLException {
        if (entity == null)
            return null;

        Column[] columns = entity.getColumns();
        if (columns == null || columns.length == 0)
            throw new UnifyException(PublicResource
                    .getSQLString("rowinsert.unknownentity"));

        ColumnDisplayDefinition[] def = new ColumnDisplayDefinition[columns.length];

        for (int i = 0; i < columns.length; i++) {
            def[columns[i].getPosition()-1] = new ColumnDisplayDefinition(
                    (int) columns[i].getSize() + 10, columns[i].getName(),
                    columns[i]);
        }
        return def;
    }

    /**
     * У���null�ֶ��Ƿ�ѡ��,����null���ֶ�û�б�ѡ��,��ʾ�û��Ƿ�������Ĳ���
     * 
     * @return --true:�������ִ��, false��ȡ��֮���ִ��
     */
    protected boolean checkColumnValidate() {
        TableColumnModel columnModel = editTable.getColumnModel();
        String message = PublicResource
                .getSQLString("rowinsert.table.columnid")
                + "\"";
        boolean isWarning = false;
        for (int i = 0; i < editTable.getColumnCount(); i++) {
            TableColumn tableColumn = columnModel.getColumn(i);
            TableHeaderCell headerCell = (TableHeaderCell) tableColumn
                    .getHeaderValue();
            Column col = (Column) headerCell.getHeaderValue();
            if (!col.isNullable() && !headerCell.getState()) {
                message += col.getName() + " ";
                isWarning = true;
            }
        }

        if (isWarning) {
            int result = JOptionPane.showConfirmDialog(this, message
                    + "\""
                    + PublicResource
                            .getSQLString("rowinsert.table.columnidnotnull"),
                    "confirm information", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) //����
                return true;
            else
                //����
                return false;
        } else
            //û�з�null�ֶ�ûѡ��
            return true;
    }

    /**
     * ���������� 1����ӵ��У����β���� 2����Ӷ��� �����β���� 3��ɾ��ѡ�� 4��Ԥ����sql��
     * 
     * @return
     */
    private JToolBar createToolBar() {
        DefinableToolBar toolBar = new DefinableToolBar("edite table", JToolBar.HORIZONTAL);

        /**
         * ��ӵ���ͼ�갴ť
         */
        ActionListener addSingleAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!checkComponentValidate())
                    return;

                EditorTableModel model = (EditorTableModel) editTable
                        .getModel();
                model.addRow(EditorTableModel.getEmptyCell(editTable
                        .getColumnCount()));

            }
        };
        addSingle = new IconButton(PublicResource
                .getSQLIcon("rowinsert.dialog.addsingle.icon"));
        addSingle.setToolTipText(PublicResource
                .getSQLString("rowinsert.dialog.addsingle.tip"));
        addSingle.addActionListener(addSingleAction);
        toolBar.add(addSingle);
        /**
         * ��Ӷ���ͼ�갴ť
         */
        addMuti = new IconButton(PublicResource
                .getSQLIcon("rowinsert.dialog.addmuti.icon"));
        addMuti.setToolTipText(PublicResource
                .getSQLString("rowinsert.dialog.addmuti.tip"));
        addMuti.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!checkComponentValidate())
                    return;

                int rows = 0;
                String promptTxt = PublicResource
                        .getSQLString("rowinsert.table.popmenu.insertmutirows");
                String value = "";
                while (true) {
                    value = JOptionPane.showInputDialog(InsertDataDialog.this,
                            promptTxt);
                    if (value == null) //����
                        return;
                    try {
                        rows = Integer.parseInt(value);
                        break;
                    } catch (NumberFormatException e1) {
                        promptTxt = PublicResource
                                .getSQLString("rowinsert.table.popmenu.insert.invalidatevalue");
                    }
                }
                EditorTableModel model = (EditorTableModel) editTable
                        .getModel();
                model.addRows(EditorTableModel.getEmptyCell(rows,
                        editTable.getColumnCount()));
            }

        });
        toolBar.add(addMuti);
        /**
         * ɾ��ѡ��ͼ�갴ť
         */
        deleteSelect = new IconButton(PublicResource
                .getSQLIcon("rowinsert.table.popmenu.deleteselected.icon"));
        deleteSelect.setToolTipText(PublicResource
                .getSQLString("rowinsert.dialog.deleteselect.tip"));
        deleteSelect.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!checkComponentValidate())
                    return;

                int[] selectRows = editTable.getSelectedRows();
                if (selectRows == null || selectRows.length < 1)
                    return;
                EditorTableModel model = (EditorTableModel) editTable
                        .getModel();

                for (int i = selectRows.length - 1; i > -1; i--) {
                    model.removeRow(selectRows[i]);
                }

            }

        });
        toolBar.add(deleteSelect);

        /**
         * Ԥ��sqlͼ�갴ť
         */
        preview = new SelectIconButton(PublicResource
                .getSQLIcon("rowinsert.dialog.preview.icon"));
        //Ԥ��ȫ�����
        preview.addSelectItem(PublicResource
                .getSQLString("rowinsert.dialog.preview.all"), null,
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        if (!checkComponentValidate())
                            return;

                        int[] previewIndex = new int[editTable.getRowCount()];
                        for (int i = 0; i < previewIndex.length; i++) {
                            previewIndex[i] = i;
                        }
                        previewSQL(previewIndex);
                    }

                });
        //Ԥ��ѡ����
        preview.addSelectItem(PublicResource
                .getSQLString("rowinsert.dialog.preview.select"), null,
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        if (!checkComponentValidate())
                            return;

                        if (editTable.getSelectedRowCount() < 1) {
                            LogProxy
                                    .message(
                                            InsertDataDialog.this,
                                            PublicResource
                                                    .getSQLString("rowinsert.dialog.table.selectnorow"),
                                            JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        int[] previewIndex = editTable.getSelectedRows();
                        previewSQL(previewIndex);
                    }

                });
        preview.setToolTipText(PublicResource
                .getSQLString("rowinsert.dialog.preview.tip"));
        toolBar.add(preview);

        /**
         * ����sql���
         */
        //����ȫ��
        Action exportAllSQLAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
                if (!checkComponentValidate())
                    return;

                int[] previewIndex = new int[editTable.getRowCount()];
                for (int i = 0; i < previewIndex.length; i++) {
                    previewIndex[i] = i;
                }
                exportSQL(previewIndex);
            }

        };
        Action exportSelectedSQLAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
                if (!checkComponentValidate())
                    return;

                if (editTable.getSelectedRowCount() < 1) {
                    LogProxy
                            .message(
                                    InsertDataDialog.this,
                                    PublicResource
                                            .getSQLString("rowinsert.dialog.table.selectnorow"),
                                    JOptionPane.WARNING_MESSAGE);
                    return;
                }
                exportSQL(editTable.getSelectedRows());
            }

        };
        exportSQL = new SelectIconButton(PublicResource
                .getSQLIcon("rowinsert.dialog.export.icon"));
        exportSQL.setToolTipText(PublicResource
                .getSQLString("rowinsert.dialog.export.tip"));
        exportSQL.addSelectItem(PublicResource
                .getSQLString("rowinsert.dialog.export.all"), null,
                exportAllSQLAction);
        exportSQL.addSelectItem(PublicResource
                .getSQLString("rowinsert.dialog.export.select"), null,
                exportSelectedSQLAction);
        toolBar.add(exportSQL);
        /**
         * ִ��sqlͼ�갴ť
         */
        Action executeAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
                if (!checkComponentValidate())
                    return;

                int result = JOptionPane
                        .showConfirmDialog(
                                InsertDataDialog.this,
                                PublicResource
                                        .getSQLString("rowinsert.dialog.confirmstartexecute"),
                                "confirm execute", JOptionPane.YES_NO_OPTION);
                
                Bookmark bookmark = entityInfoPane.getEntity().getBookmark();
                if(!bookmark.isAutoCommit()&&result == JOptionPane.YES_OPTION)
                {
                	boolean r=GUIUtil.getYesNo(PublicResource.getSQLString("rowinsert.dialog.beforeadd.enableautocommit"));
                	if(!r)
                	{
                		return;
                	}
                }
                
                if (result == JOptionPane.YES_OPTION) //ѡ��������
                {
                    if (!checkColumnValidate()) //ȡ�����ִ��
                        return;

                    int[] previewIndex = new int[editTable.getRowCount()];
                    for (int i = 0; i < previewIndex.length; i++) {
                        previewIndex[i] = i;
                    }
                    final ExecuteAction exe = new ExecuteAction(previewIndex);
                    final ProcessThread pt = new ProcessThread(exe);
                    final WaitDialog waitDialog = WaitDialogManage
                            .getInstance().register(pt, InsertDataDialog.this);
                    waitDialog.setTitle("add data to database");
                    waitDialog.setPrompt(PublicResource
                            .getSQLString("rowinsert.waitdialog.prompt"));
                    waitDialog.setTaskLength(previewIndex.length);
                    waitDialog.addQuitAction(new Actionable() {

                        public void action() {

                            WaitDialogManage.getInstance().disposeRegister(pt);
                            waitDialog.dispose();
                            exe.stopExecute();
                        }

                    });
                    pt.start(); //�����߳�
                    waitDialog.setVisible(true); //��ʾ�ȴ�Ի���
                }
            }

        };
        toolBar.addIconButton(PublicResource
                .getSQLIcon("rowinsert.dialog.execute.icon"), executeAction,
                PublicResource.getSQLString("rowinsert.dialog.execute.tip"));

        toolBar.setBackground(DisplayPanel.getThemeColor());
        return toolBar;
    }

    /**
     * ����ָ����sql
     * 
     * @param rows
     *            --ָ����Ҫ��������
     */
    private void exportSQL(int rows[]) {
        if (rows == null)
            return;
        /**
         * ��ȡ�ļ������Ҵ򿪱����ļ������
         */
        File selectFile = GUIUtil.selectFileNoFilter(InsertDataDialog.this);
        if (selectFile == null)
            return;
        FileOutputStream out = null;
        try {
            GUIUtil.createDir(selectFile.getAbsolutePath(), false, true);
            out = new FileOutputStream(selectFile);

            for (int i = 0; i < rows.length; i++) {
                out.write((StringUtil.trim(getSQLToRow(rows[i]))+";\r\n").getBytes());
            }
        } catch (FileNotFoundException e1) {
            LogProxy.errorReport(InsertDataDialog.this, e1);
        } catch (IOException e1) {
            LogProxy.errorReport(InsertDataDialog.this, e1);
        } catch (UnifyException e1) {
            LogProxy.errorReport(InsertDataDialog.this, e1);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * У�������Ч��
     * 
     * @return --true:��Ч false:��Ч
     */
    private boolean checkComponentValidate() {
        if (editTable.getColumnCount() < 1) {
            LogProxy.errorMessage(this, PublicResource
                    .getSQLString("rowinsert.dialog.novalidateentity"));
            return false;
        } else
            return true;
    }

    /**
     * ����״̬��Ϣ����ѡ���λ�ã�ѡ����������
     *  
     */
    protected void updateStatus() {
        int[] rows = editTable.getSelectedRows();
        int[] columns = editTable.getSelectedColumns();
        if (rows == null || rows.length < 1 || columns == null
                || columns.length < 1) {
            statusBar1.setText("");
            statusBar2.setText("");
        } else {
            statusBar1.setText((rows[0] + 1) + "," + (columns[0] + 1));
            statusBar2.setText(selectRowsLabel + rows.length + ","
                    + selectColumnsLabel + columns.length);
        }
    }

    /**
     * ���±�ṹ���������Ϣ
     *  
     */
    protected void updateTableScaleInfo() {
        updateStatus();
        if (editTable != null) {
            int rows = editTable.getRowCount();
            int columns = editTable.getColumnCount();
            statusBar3.setText(rows + ":" + columns);
        }
    }

    /**
     * Ԥ��ָ��������ɵ�sql
     * 
     * @param rowIndex
     *            --ָ��������������
     */
    protected void previewSQL(int[] rowIndex) {
        if (!checkColumnValidate()) //���ȡ�����
            return;

        //updateConstantOfSQL(entityInfoPane.getEntity());//�ȸ��²���sql�������
        PreviewSQLDialog previewDialog = new PreviewSQLDialog(null, this, true);
        ProcessThread pt = new ProcessThread(new PreviewAction(previewDialog,
                rowIndex));

        /**
         * �������߳�,Ȼ������ʾԤ���Ի���
         */
        pt.start();
        previewDialog.setVisible(true);
    }

    /**
     * ��ȡָ�����������ɵĲ���sql���
     * 
     * @param rowIndex
     *            --ָ����������
     * @return --sql���
     * @throws UnifyException
     */
    protected String getSQLToRow(int rowIndex) throws UnifyException {
        StringBuilder buffer = new StringBuilder(constantOfEntity + "(");

        Database db = entityInfoPane.getEntity().getBookmark().getDbInfoProvider();
        TableColumnModel columnModel = editTable.getColumnModel();
        TableModel model = editTable.getModel();
        int i = 0;
        boolean isPostgreSQL=DialectFactory.isPostgreSQL(db.getDatabaseMetaData());
        for (; i < editTable.getColumnCount(); i++) {
            TableColumn tableColumn = columnModel.getColumn(i);
            TableHeaderCell cell = (TableHeaderCell) tableColumn
                    .getHeaderValue();
            if (!cell.getState()) //������û��ѡ��,ֱ�ӽ�����һ��ѭ��
                continue;

            Column col = (Column) cell.getHeaderValue();

//            int realColumnIndex = editeTable.convertColumnIndexToModel(i);
            EditeTableCell value = (EditeTableCell) model.getValueAt(rowIndex,
            		editTable.convertColumnIndexToModel(i)); //��ȡ��ͼ��Ӧ�����������ֵ
            String tmpValue=value.getDisplayLabel();
            
            if(isPostgreSQL)
            {
            	buffer.append(value.isNull()?"null":SqlUtil.qualifyColumnValue(tmpValue, col.getType())).append(",");
            	continue;
            }
            
            DataType dataType = db.getDataType(col.getTypeName()); //��ȡ�ֶε����Ͷ���
            if(dataType==null)
            {
            	buffer.append(value.isNull()?"null":SqlUtil.qualifyColumnValue(tmpValue, col.getType())).append(",");
            	continue;
            }
            if(SqlUtil.isNumberType(col.getType())&&tmpValue.equals(""))
            {
                tmpValue="0";
            }
            
            String realValue = (value.isNull() ? "null,"
                    : (StringUtil.trim(dataType.getLiteralPrefix())
                            + tmpValue
                            + StringUtil.trim(dataType.getLiteralSuffix()) + ","));
            buffer.append(realValue);
        }
        buffer.deleteCharAt(buffer.length() - 1); //ɾ�����Ķ���"'"
        buffer.append(")");

        return buffer.toString();
    }

    /**
     * 
     * @author liu_xlin Ԥ��sql�����߼�������
     */
    private class PreviewAction implements Actionable {
        private PreviewSQLDialog previewDialog; //Ԥ����ʾsql�ĶԻ���

        private int[] rowIndex; //��ҪԤ������

        public PreviewAction(PreviewSQLDialog previewDialog, int[] rowIndex) {
            this.previewDialog = previewDialog;
            this.rowIndex = rowIndex;
        }

        /*
         * (non-Javadoc)
         * 
         * @see com..exportdata.Actionable#action()
         */
        public void action() {
            if (previewDialog == null)
                return;

            previewDialog.setStatueInfo("sql count:" + rowIndex.length);
            updateConstantOfSQL(entityInfoPane.getEntity(), false);
            for (int i = 0; i < rowIndex.length; i++) {
                try {
                    previewDialog.append(getSQLToRow(rowIndex[i]) + ";");
                } catch (BadLocationException e) {
                    LogProxy.errorReport(InsertDataDialog.this, e); //������쳣,�ж�ִ��
                    return;
                } catch (UnifyException e) {
                    LogProxy.errorReport(InsertDataDialog.this, e);//������쳣,�ж�ִ��
                    return;
                }
            }
        }

    }

    /**
     * 
     * @author liu_xlin ִ�б���������ɵ�sql
     */
    private class ExecuteAction implements Actionable {
        private int[] rowIndex; //��Ҫִ�е���

        private boolean isRun; //����ִ�д������ֹ

        public ExecuteAction(int[] rowIndex) {
            isRun = true;
            this.rowIndex = rowIndex;
        }

        private void stopExecute() {
            isRun = false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see com..exportdata.Actionable#action()
         */
        public void action() {
            Bookmark bookmark = entityInfoPane.getEntity().getBookmark();
            boolean oldIsAutoCommit = bookmark.isAutoCommit();
            
            WaitDialog dialog;
            try {
                dialog = WaitDialogManage.getInstance().getDialogOfCurrent();
                dialog.setTaskLength(rowIndex.length);
            } catch (NotRegisterException e2) {
                LogProxy.errorReport(e2);
                return;
            }
            try {
            	if(!oldIsAutoCommit)
            	{
            		bookmark.getConnection().commit();
            	}
            	bookmark.setAutoCommit(false);
            } catch (SQLException e3) {
            	if (bookmark.isAutoCommit() != oldIsAutoCommit) {
            		try {
						bookmark.setAutoCommit(oldIsAutoCommit);
					} catch (Exception e) {
						e.printStackTrace();
					} 
            	}
                LogProxy.SQLErrorReport(InsertDataDialog.this, e3);
                dialog.dispose();
                return;
            } catch (UnifyException e3) {
            	if (bookmark.isAutoCommit() != oldIsAutoCommit) {
            		try {
						bookmark.setAutoCommit(oldIsAutoCommit);
					} catch (Exception e) {
						e.printStackTrace();
					} 
            	}
                LogProxy.errorReport(InsertDataDialog.this, e3);
                dialog.dispose();
                return;
            }
            Connection con = null;
            Statement sm = null;
            try {
                con = bookmark.getConnection();
                sm = con.createStatement();
            } catch (UnifyException e) {
                LogProxy.errorReport(InsertDataDialog.this, e);
                return;
            } catch (SQLException e) {
                LogProxy.SQLErrorReport(InsertDataDialog.this, e);
                return;
            }
            try {
            	updateConstantOfSQL(entityInfoPane.getEntity(), true);
            	int successCount = 0;//The row count of row data has been saved into database successfully.
                for (int i = 0; i < rowIndex.length; i++) {
                    if (!isRun) { //�����ֹ����
                        int result = JOptionPane
                                .showConfirmDialog(
                                        InsertDataDialog.this,
                                        PublicResource
                                                .getSQLString("rowinsert.dialog.confirmstop"),
                                        "confirm stop",
                                        JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.YES_OPTION) //���ѡ����
                        {
                            if (successCount == 0) {//���û�в�������,ֱ�����ѭ��
                                break;
                            }

                            result = JOptionPane
                                    .showConfirmDialog(
                                            InsertDataDialog.this,
                                            PublicResource
                                                    .getSQLString("rowinsert.dialog.confirmrollback")
                                                    + successCount, "confirm rollback",
                                            JOptionPane.YES_NO_CANCEL_OPTION);

                            if (result == JOptionPane.NO_OPTION) {
                                break;
                            } else if (result == JOptionPane.YES_OPTION) {
                                try {
                                    con.rollback();
                                } catch (SQLException e1) {
                                    LogProxy.SQLErrorReport(
                                            InsertDataDialog.this, e1);
                                }
                                break;
                            } else {
                                isRun = true; //������ִ��
                            }

                        } else {
                            isRun = true; //������ִ��
                        }
                    } else //���sqlִ��
                    {
                        try {
                            processRowInsert(rowIndex[i], sm, bookmark);
                            successCount ++;
                        } catch (UnifyException e1) {
                            LogProxy.errorReport(InsertDataDialog.this,"current row number:"+rowIndex[i], e1);
                            isRun = false; //�����쳣��,���û�ȷ���Ƿ��˳�ִ��
                        } catch (SQLException e1) {
                            LogProxy.SQLErrorReport(InsertDataDialog.this, e1);
                            isRun = false;//�����쳣��,���û�ȷ���Ƿ��˳�ִ��
                        } finally {
                        	dialog.setProgressValue(i+1); //���ý��
                        }
                    }
                }
            } finally {
                /**
                 * �ύ����ͬʱ�ָ���ݿ����ӵ��ύ���ԣ��ر�Statement����
                 */
                try {
                    con.commit();
                    sm.close();
                    bookmark.setAutoCommit(oldIsAutoCommit);
                } catch (SQLException e1) {
                    LogProxy.SQLErrorReport(InsertDataDialog.this, e1);
                } catch (UnifyException e1) {
                    LogProxy.errorReport(InsertDataDialog.this, e1);
                }
                

                /**
                 * �رյȴ�Ի���
                 */
                WaitDialogManage.getInstance().disposeRegister(
                        Thread.currentThread());
                if (dialog != null)
                {
                	int value=dialog.getProgressValue();
                    dialog.dispose();
                    if(value>0)
                    {
                    	int taskLen=dialog.getTaskLength();
                    	if(taskLen==value)//Have finished the task.
                    	{
                    		JOptionPane.showMessageDialog(InsertDataDialog.this,
                    				"Executing has been finished successfully","finished!",JOptionPane.INFORMATION_MESSAGE);
                    	}
                    }
                    	
                }
            }
        }

        /**
         * ִ�в����sql���
         * 
         * @param index
         *            --ָ��������
         * @param sm
         *            --Statement����
         * @throws UnifyException
         * @throws SQLException
         */
        private void processRowInsert(int index, Statement sm, Bookmark bookmark)
                throws UnifyException, SQLException {
            String sql = getSQLToRow(index);
            LogProxy.getProxy().debug(
                    "The INSERT SQL (" + bookmark.getAliasName() + "): " + sql);
            sm.execute(sql);
        }
    }

    /**
     * 
     * @author liu_xlin ״̬���µļ����࣬������ѡ���λ�ã�ѡ��������������Ϣ
     */
    protected class StatusChangeListener extends MouseAdapter implements
            MouseMotionListener {
        public void mousePressed(MouseEvent e) {
        	isMousePressed=true;
//            updateStatus();

        }

        public void mouseReleased(MouseEvent e) {
        	isMousePressed=false;
//            updateStatus();
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
         */
        public void mouseDragged(MouseEvent e) {
            updateStatus();

        }

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
         */
        public void mouseMoved(MouseEvent e) {
        }
    }

    /**
     * 
     * @author liu_xlin ���Ǳ�ؼ���ȱʡ��ݼ�ctrl A��ȫѡ����
     */
    private class SelectAllAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		/*
         * (non-Javadoc)
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            editTable.setRowSelectionInterval(0, editTable.getRowCount() - 1);
            editTable.setColumnSelectionInterval(0, editTable
                    .getColumnCount() - 1);
//            updateStatus();
        }

    }
    /**
     * 
     * @author liu_xlin Ϊ��ṹģ����Ӽ�������
     */
    private class TableStructListener implements TableModelListener {

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
         */
        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE) {
                if (e.getFirstRow() > -1 || e.getColumn() > -1) //����Ǹ��±����ݣ�ֱ�ӷ���
                    return;

                updateTableScaleInfo();
            } else {
                clearInvalidateRow(e);
                updateTableScaleInfo();
            }
        }

        private void clearInvalidateRow(TableModelEvent e) {
            editTable.getSelectionModel().clearSelection();
        }
    }

    /**
     * 
     * @author liu_xlin
     *��ͷ�Ҽ�˵�
     */
    private class TableHeaderMenu extends JPopupMenu {
		private static final long serialVersionUID = 1L;
		JMenuItem queryColumn = null;
        private int x1=0,y1=0;
        public TableHeaderMenu() {
            super();
            /**
             * �鿴�����Բ˵���
             */
            queryColumn = new JMenuItem(
                    PublicResource
                            .getSQLString("rowinsert.dialog.table.header.popmenu.querycolumn"));
            Action queryAction = new AbstractAction() //��ѯ��������Ϣ����
            {
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e1) {
                    /**
                     * ��ȡ��Ӧ���ж�����Ϣ
                     */
                    int columnIndex = editTable.getTableHeader()
                            .columnAtPoint(
                                    new Point(x1,y1));
                    Object ob = editTable.getColumnModel().getColumn(
                            columnIndex).getHeaderValue();
                    
                    Column col = (Column)((EditeTableHeaderCell)ob).getHeaderValue();

                    /**
                     * ���������ԶԻ���
                     */
                    Operatable operator;
                    try {
                        operator = OperatorFactory
                                .getOperator(ColumnPropertyOperator.class);
                    } catch (ClassNotFoundException e) {
                        LogProxy.errorReport(e);
                        return;
                    } catch (InstantiationException e) {
                        LogProxy.internalError(e);
                        return;
                    } catch (IllegalAccessException e) {
                        LogProxy.internalError(e);
                        return;
                    }
                    try {
                        operator.operate(InsertDataDialog.this, col);
                    } catch (UnifyException e2) {
                        LogProxy.errorReport(InsertDataDialog.this, e2);
                    } catch (SQLException e2) {
                        LogProxy.SQLErrorReport(InsertDataDialog.this, e2);
                    }
                }

            };
            queryColumn.addActionListener(queryAction);

            add(queryColumn);
        }
        public void show(Component com,int x,int y)
        {
            this.x1=x;
            this.y1=y;
            super.show(com,x,y);
        }
    }
}
