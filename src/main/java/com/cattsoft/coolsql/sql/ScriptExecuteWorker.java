package com.cattsoft.coolsql.sql;

import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JOptionPane;

import org.jdesktop.swingworker.SwingWorker;

import com.cattsoft.coolsql.pub.component.WaitDialog;
import com.cattsoft.coolsql.pub.component.WaitDialogManage;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.sql.execute.BaseMultiStatementExecute;
import com.cattsoft.coolsql.sql.execute.IMultiStatementExecute;

/**
 * 
 * @author ��Т��(kenny liu)
 *
 * 2008-3-31 create
 */
public class ScriptExecuteWorker extends SwingWorker<Object, String> {
	private static final StringManager stringMgr=StringManagerFactory.getStringManager(BaseMultiStatementExecute.class);
	private IMultiStatementExecute statementExecuter;
	private WaitDialog wd;
	public ScriptExecuteWorker(IMultiStatementExecute statementExecute,WaitDialog waitDialog) {
		this.statementExecuter = statementExecute;
		this.wd=waitDialog;
		if(wd==null)
		{
			wd=WaitDialogManage.getInstance().register(GUIUtil.findLikelyOwnerWindow());
		}
		if (statementExecuter != null&&statementExecuter instanceof BaseMultiStatementExecute) {
			((BaseMultiStatementExecute)statementExecuter).addPropertyListener(new PropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getPropertyName().equals(
							BaseMultiStatementExecute.PROPERTY_MESSAGE)) {
						publish((String) evt.getNewValue());
					} else if (evt.getPropertyName().equals(
							BaseMultiStatementExecute.PROPERTY_PROGRESS)) {
						setProgress((Integer) evt.getNewValue());
					} else if (evt.getPropertyName().equals(
							BaseMultiStatementExecute.PROPERTY_STATE_STARTED)) {
						wd.setTaskLength((Integer) evt.getNewValue());
					}
					if(statementExecuter.isFinished())
					{
						Container con=wd.getOwner();
						wd.dispose();
						if(statementExecuter.isSuccess()&&!statementExecuter.isCancelExecution())
						{
							JOptionPane.showMessageDialog(con, stringMgr.getString("batchprocess.execute.successful"));
						}
					}
				}

			});
		}
		this.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("progress")) {
					wd.setProgressValue((Integer) evt.getNewValue());
				}

			}

		});
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdesktop.swingworker.SwingWorker#doInBackground()
	 */
	@Override
	protected Object doInBackground() throws Exception {
		statementExecuter.execute();
		return null;
	}
	@Override
	protected void process(List<String> chunks) {
		for (String str : chunks)
			wd.setPrompt(str);

	}
}