/*
 * �������� 2006-10-20
 */
package com.cattsoft.coolsql.view.resultset;

import java.sql.SQLException;

import javax.swing.SwingUtilities;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.sql.SQLResultSetResults;
import com.cattsoft.coolsql.sql.SQLResults;
import com.cattsoft.coolsql.sql.SQLUpdateResults;
import com.cattsoft.coolsql.view.ResultSetView;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.bookmarkview.RecentSQL;
import com.cattsoft.coolsql.view.bookmarkview.RecentSQLManage;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin ���Ļ�ȡ���༭�����࣬������Ҫ������������Ļ�ȡ��ˢ�¡���һҳ����һҳ����ݴ���
 */
public class ResultSetDataProcess {
    public static int EXECUTE = 0; //��ĵ�һ��ִ��

    public static int PREVIOUS = 1; //ǰһҳ��ݴ���

    public static int NEXT = 2; //��һҳ����ݴ���

    public static int REFRESH = 3;//ˢ�´���

    private int processType;

    private DataSetPanel dataPane; //������

    private String sql = null;

    private Bookmark bookmark;

    //    private JTable tmpTable = null; //��ʱ��ؼ����ñ���������ݵ�ҳ��������
    public ResultSetDataProcess(DataSetPanel dataPane, String sql,
            Bookmark bookmark, int processType) {
        super();
        if (!checkProcessType(processType))
            throw new IllegalArgumentException(
                    "process type is not in [0,3],error type:" + processType);

        this.processType = processType;
        this.dataPane = dataPane;
        this.sql = sql;
        this.bookmark = bookmark;

        //        dataReuse();
        dataPane.setPromptContent();
        dataPane.setReady(false); //�������ѵ�״̬��Ϊδ����
//        dataPane.setSqlResult(dataPane.getSqlResult())
//        dataPane.firePanelPropertyUpdate("sqlResult", null, );
    }

    /**
     * У�鴦������
     * 
     * @param type
     * @return true if in part of the normal route,false if not in
     */
    private boolean checkProcessType(int type) {
        if (type < 0 || type > 3)
            return false;
        else
            return true;
    }
    /**
     * ִ�д���
     *  
     */
    public void process() {
        if (processType == EXECUTE) //�״�ִ��
        {
            firstExcecute();
        } else //�ٴδ���
        {
            pageProcess();
        }
    }

    /**
     * �״λ�ȡsqlִ�н��,ͬʱ��ִ�е�sql��������ǩ�ڵ���
     *  
     */
    protected void firstExcecute() {
        //��һ����ʾ���ڴ���
        dataPane.setPromptText(PublicResource
                .getSQLString("sql.execute.process.start"));
        SQLResults set = null;
        try {
            set = bookmark.getDbInfoProvider().execute(bookmark.getConnection(), sql);          
            
            /**
             * ����sql����
             */
            RecentSQL sqlData=new RecentSQL(sql,set.getCostTime(),set.getTime(),bookmark);
            RecentSQLManage.getInstance().addSQL(sqlData,bookmark);
            
            //��sqlִ��ʱ����ʾ����־��
            LogProxy log = LogProxy.getProxy();
            log.debug(PublicResource
                    .getSQLString("sql.execute.process.costtime")
                    + ((float) set.getTime()) / 1000);
        } catch (SQLException e) {
            removeProcessingTab();
            LogProxy.SQLErrorReport(e);
            return;
        } catch (UnifyException e) {
            removeProcessingTab();
            LogProxy.errorReport(e);
            return;
        }

        //�ڶ�����ʾ��ʼ���������
        dataPane.setPromptText(PublicResource
                .getSQLString("sql.execute.process.initdata"));

        
        if (set.isResultSet()) //����ǲ�ѯ���
        {
            SQLResultSetResults querySet = (SQLResultSetResults) set;
                       
            //������ؼ�,ͬʱ��ʼ�����
            DataSetTable dataTable = new DataSetTable(querySet
                    .getVectorDataOfRow(), DataSetPanel
                    .getHeaderDefinition(querySet.getArrayDataOfColumn()));
            ResultSetView view=ViewManage.getInstance().getResultView();
            view.installDataSetTableSelectionListener(dataTable);
            dataPane.addTableToContent(dataTable); //�������ݲ���,�����չʾ�ڽ�����
            
            /**
             * ����ѯ�Ľ�����֮��չʾ�ڽ�����
             */
            dataPane.setSqlResult(querySet);

        } else //����Ǹ��»���ɾ�����
        {
            /**
             * ������sql�ʹ���ʱ�䣬�Լ�����ļ�¼��չʾ�ڽ����У��������ࣩ
             */
            SQLUpdateResults updateResult = (SQLUpdateResults) set;
            
            UpdateResultPane updatePane = new UpdateResultPane(this.sql,
                    updateResult.getCostTime(), updateResult.getUpdateCount());
            dataPane.setReady(true); //����״̬Ϊ�Ѿ���
            dataPane.setContent(updatePane);
            dataPane.setSqlResult(updateResult);
        }
    }

    /**
     * ��ݵ��ٴ��?����ˢ�¡���һҳ����һҳ��ݴ���
     *  
     */
    protected void pageProcess() {

//        dataPane.setPromptContent();//�����ʾ��ǩ

        dataPane.setPromptText(PublicResource
                .getSQLString("sql.execute.process.start"));

        boolean isError = false;
        try {
            if (processType == PREVIOUS)
                dataPane.previousPage();

            else if (processType == NEXT)
                dataPane.nextPage();
            else if (processType == REFRESH)
                dataPane.refreshPage();
        } catch (SQLException e) {
            isError = true;
            LogProxy.SQLErrorReport(e);
        } catch (UnifyException e) {
            isError = true;
            LogProxy.errorReport(e);
        }

        dataPane.setPromptText(PublicResource
                .getSQLString("sql.execute.process.initdata"));
        SQLResults data = dataPane.getSqlResult();
        if (data.isResultSet()) {
            SQLResultSetResults querySet = (SQLResultSetResults) data;
            //������ؼ�,ͬʱ��ʼ�����
            DataSetTable dataTable = new DataSetTable(querySet
                    .getVectorDataOfRow(), DataSetPanel
                    .getHeaderDefinition(querySet.getArrayDataOfColumn()));
            ResultSetView view=ViewManage.getInstance().getResultView();
            view.installDataSetTableSelectionListener(dataTable);
            dataPane.addTableToContent(dataTable); //�������ݲ���,�����չʾ�ڽ�����
            //����������Ӽ����¼�
            //            ResultSetView view=ViewManage.getInstance().getResultView();
            //            dataPane.addDataChangeListener(view.getResultSetListener());

            dataPane.setReady(true); //����״̬Ϊ�Ѿ���
            if (!isError) { //���û�д�����ô���½����ϵ���Ϣ
                dataPane.firePanelPropertyUpdate("sqlResult", null, data);
                dataPane.updateResultInfo((SQLResultSetResults) data);
            }
        }
    }

    /**
     * ɾ������ͼ�ж�Ӧ�����tab
     *  
     */
    private void removeProcessingTab() {
       	SwingUtilities.invokeLater(new Runnable()
    	{
    		public void run()
    		{
    			ResultSetView view = ViewManage.getInstance().getResultView();
    			view.removeTab(dataPane);
    			view.updateUI();
    		}
    	});
    }
}
