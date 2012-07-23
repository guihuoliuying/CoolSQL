/**
 * 
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.DefaultSingleSelectionModel;
import javax.swing.JTabbedPane;

import com.cattsoft.coolsql.action.common.PublicAction;
import com.cattsoft.coolsql.pub.display.GUIUtil;

/**
 * @author ��Т��(kenny liu)
 * 
 * 2008-3-12 create
 */
public class BaseTabbedPane extends JTabbedPane {

	// max size of
	// the index
	// history that
	// system saved
	// by default.
	public static final int DEFAULT_MAXINDEXHISTORYCOUNT = 20; 

	private static final long serialVersionUID = 1L;

	public BaseTabbedPane() {
		super();
		initSelectionModel();
	}
	public BaseTabbedPane(int tabPlacement) {
		super(tabPlacement);
		initSelectionModel();
	}
	public BaseTabbedPane(int tabPlacement, int tabLayoutPolicy) {
		super(tabPlacement, tabLayoutPolicy);
		initSelectionModel();
	}
	// recording
	// the
	// history
	// of
	// selected
	// index
	// value.
	private ArrayList<Component> indexHistory = new ArrayList<Component>(); 

	private boolean isRemoving = false; // indicate whether processing remove
	private boolean isShortMoving=false; //indicate whether processing shortcut moving
	// operation or not.
	private int maxHistoryCount = DEFAULT_MAXINDEXHISTORYCOUNT;
	private int indexOfViewing = -1;// the index value of historylist being
	// viewed
	protected void initSelectionModel() {
		DefaultSingleSelectionModel dssm = new DefaultSingleSelectionModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void setSelectedIndex(int index) {
				if (!isRemoving&&!isShortMoving) {
					if (indexHistory.size() >= maxHistoryCount) {
						indexHistory.remove(0);// remove oldest index.
					}
					if (getSelectedIndex() != index) {
						Component selectedComponent = getSelectedComponent();
						if (selectedComponent != null) {
							indexHistory.remove(selectedComponent);
							indexHistory.add(selectedComponent);
							indexOfViewing = indexHistory.size() - 1;
						}
					}
				}
				super.setSelectedIndex(index);
			}
		};
		setModel(dssm);
		Action processHistoryOfBack=new PublicAction()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e)
			{
				if(isBackable())
					nextBackComponent();
				else
					Toolkit.getDefaultToolkit().beep();
			}
		}
		;
		Action processHistoryOfForward=new PublicAction()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e)
			{
				if(isForwardable())
					nextForwardComponent();
				else
					Toolkit.getDefaultToolkit().beep();
			}
		}
		;
		GUIUtil.bindShortKey(this, "ctrl LEFT", processHistoryOfBack, false);
		GUIUtil.bindShortKey(this, "ctrl RIGHT", processHistoryOfForward, false);
	}
	@Override
	public void removeTabAt(int index) {
		isRemoving = true; // because removeTabAt method perform an operation
		// of setSelectedIndex, so it need be notified by
		// flag:isRemoving.
		Component tmpComponent = getComponentAt(index);
		super.removeTabAt(index);
		isRemoving = false;
		removeComponentRemoved(tmpComponent);
		if (indexHistory.size() != 0) {// select last selected component after
			// removing.
			Component lastSelectedComponent = indexHistory.get(indexHistory
					.size() - 1);
			setSelectedComponent(lastSelectedComponent);
		}
	}
	private void removeComponentRemoved(Component com)
	{
		indexHistory.remove(com);
	}
	/**
	 * determine whether go back to history index.
	 * 
	 * @return
	 */
	public boolean isBackable() {
		return indexOfViewing > -1;
	}
	/**
	 * determine whether go forward to history index.
	 * 
	 * @return
	 */
	public boolean isForwardable() {
		return indexOfViewing < indexHistory.size();
	}
	public Component nextBackComponent() {
		if(indexOfViewing>=indexHistory.size())
			indexOfViewing=indexHistory.size()-2;
		Component com = indexHistory.get(indexOfViewing);
		isShortMoving=true;
		setSelectedComponent(com);
		isShortMoving=false;
		indexOfViewing--;
		return com;
	}
	public Component nextForwardComponent() {
		if(indexOfViewing<0)
			indexOfViewing=1;
		Component com = indexHistory.get(indexOfViewing);
		indexOfViewing++;
		isShortMoving=true;
		setSelectedComponent(com);
		isShortMoving=false;
		return com;
	}
}
