/*
 * �������� 2006-7-11
 *
 */
package com.cattsoft.coolsql.view;

import java.awt.Container;

import javax.swing.Icon;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import com.cattsoft.coolsql.pub.display.GUIUtil;

/**
 * @author liu_xlin
 * 
 */
public abstract class TabView extends View {
	private static JTabbedPane tabPane = null;

	public TabView() {
		super();
	}
	/**
	 * get tab pane used to display all tab view.
	 * 
	 * @return
	 */
	public static synchronized JTabbedPane getTabPane() {
		if (tabPane == null)
			tabPane = new JTabbedPane();
		return tabPane;
	}
	/**
	 * return whether current view is visible,this method is diffent from isVisible.
	 * @return
	 */
	@Override
	public boolean isViewVisible()
	{
		Container con = getParent();
		if (!(con instanceof JTabbedPane))
			return false;
		JTabbedPane tp = (JTabbedPane) con;
		return  tp.indexOfComponent(this) > -1;
	}
	@Override
	public void showPanel(final boolean isFire) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Container con = getParent();
				if (con == null)
					con = getTabPane();
				if (con instanceof JTabbedPane) {
					final JTabbedPane tp = (JTabbedPane) con;
					if (tp.getTabCount() == 0) // if tab component is
					// visible ,it will show
					// part of split pane
					// which tab component
					// is in
					{
						tp.setVisible(true);
						GUIUtil.controlSplit(tp, false); // show split pane
						// which tab
						// component is in

						// �ָ�ԭʼλ��
						JSplitPane split = GUIUtil.getSplitContainer(tp);
						Integer location = (Integer) split
								.getClientProperty(View.LASTLOCATION);
						if(location==null)
							location=GUIUtil.DEFAULT_VIEWWIDTH;
						GUIUtil.getSplitContainer(tp).setDividerLocation(
								location);
						if (!GUIUtil.isMaxSplitToSelf(split))
							split.putClientProperty(View.LASTLOCATION, null); // ����Ѿ��ָ������
					}

					int index = getTabViewIndex();
					if (index == -1)
						tp.add(TabView.this);
					else
						tp.add(TabView.this, index);
					index = tp.indexOfComponent(TabView.this);
					tp.setSelectedIndex(index);
					tp.setTitleAt(index, getTabViewTitle());
					tp.setToolTipTextAt(index, getTabViewTip());
					tp.setIconAt(index, getTabViewIcon());

					if (isFire)
						firePropertyChange(PROPERTY_HIDDEN, true, false);
				}
			}
		});

	}
	@Override
	public void hidePanel(final boolean isFire) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Container con = getParent();
				if (con instanceof JTabbedPane) {
					final JTabbedPane tp = (JTabbedPane) con;
					tp.remove(TabView.this);
					if (tp.getTabCount() == 0) // if tab component is
					// hidden ,it will
					// hidden part of split
					// pane which tab
					// component is in
					{
						tp.setVisible(false);
						GUIUtil.controlSplit(tp, true); // hidden split pane
						// which tab component
						// is in

						/**
						 * ����ԭʼλ��
						 */
						JSplitPane split = GUIUtil.getSplitContainer(tp);
						// System.out.println("sql:"+split.getClientProperty(View.LASTLOCATION));
						if (split.getClientProperty(View.LASTLOCATION) == null) {
							int location = split.getDividerLocation();
							split
									.putClientProperty(View.LASTLOCATION,
											location);
						}
					}
					if (isFire)
						firePropertyChange(PROPERTY_HIDDEN, true, false);
				}
			}
		});

	}
	/**
	 * current tab view index in JTabbedPane component
	 * 
	 * @return
	 */
	public int getTabViewIndex() {
		return -1;
	}
	/**
	 * current tab view tool tip
	 * 
	 * @return
	 */
	public String getTabViewTip() {
		return getName();
	}
	/**
	 * current tab view icon
	 * 
	 * @return
	 */
	public Icon getTabViewIcon() {
		return null;
	}
	/**
	 * return tab title of current tab view
	 * 
	 * @return
	 */
	public String getTabViewTitle() {
		return getName();
	}
}
