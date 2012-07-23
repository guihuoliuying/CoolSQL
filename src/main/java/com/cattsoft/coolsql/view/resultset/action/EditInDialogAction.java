/**
 * Create date:2008-5-18
 */
package com.cattsoft.coolsql.view.resultset.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.cattsoft.coolsql.action.framework.AutoCsAction;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.display.Inputer;
import com.cattsoft.coolsql.pub.display.TextEditorDialog;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.resultset.DataSetPanel;
import com.cattsoft.coolsql.view.resultset.DataSetTable;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-5-18 create
 */
public class EditInDialogAction extends AutoCsAction {

	private static final long serialVersionUID = 1L;
	
	
	public EditInDialogAction()
	{
	}
	@Override
	public void executeAction(ActionEvent e)
	{
		super.executeAction(e);
		Component com=ViewManage.getInstance().getResultView().getDisplayComponent();
		if(com instanceof DataSetPanel)
		{
			final DataSetPanel dsPanel=(DataSetPanel)com;
			final DataSetTable dsTable=(DataSetTable)dsPanel.getContent();
			final int[] rows=dsTable.getSelectedRows();
			final int[] columns=dsTable.getSelectedColumns();
			if(rows.length!=1||columns.length!=1)
				return;
			
			Object value=dsTable.getDisplayData(rows[0], columns[0]);
			String text=value==null?"":value.toString();
			
			Inputer inputer=new Inputer()
			{
				public void setData(Object value) {
					dsTable.setValue(value, rows[0], columns[0]);
				}
			}
			;
			
			
			final TextEditorDialog dialog=new TextEditorDialog(GUIUtil.getMainFrame(),inputer,text,!dsTable.isEditable())
			{
				private static final long serialVersionUID = 1L;
				
				private PropertyChangeListener pcl;
				@Override
				protected void initComponents(boolean isReadOnly)
				{
					super.initComponents(isReadOnly);
					pcl=new PropertyChangeListener()
					{
						public void propertyChange(PropertyChangeEvent evt) {
							if(evt.getPropertyName().equals(DataSetPanel.PROPERTY_STATE))
							{
								setEditable((Boolean)evt.getNewValue());
							}
						}
					};
					dsPanel.addPanelPropertyChangeListener(pcl);
				}
				@Override
				public void close()
				{
					super.close();
					dsPanel.removePanelPropertyChangeListener(pcl);
				}
			};
			
			dsPanel.addAncestorListener(new AncestorListener()
			{

				public void ancestorAdded(AncestorEvent event) {
					
				}

				public void ancestorMoved(AncestorEvent event) {
					
				}

				public void ancestorRemoved(AncestorEvent event) {
					if(event.getAncestor()==dsPanel)
					{
						dialog.close();
					}
				}
				
			});
			
			dialog.setVisible(true);
		}
		
	}
}
