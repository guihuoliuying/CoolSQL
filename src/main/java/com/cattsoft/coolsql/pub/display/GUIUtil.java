/*
 * �������� 2006-10-18
 */
package com.cattsoft.coolsql.pub.display;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.MutableComboBoxModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.JTextComponent;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.main.frame.MainFrame;
import com.cattsoft.coolsql.pub.component.MainFrameStatusBar;
import com.cattsoft.coolsql.pub.component.TextComponentPopMenu;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.system.PropertyManage;
import com.cattsoft.coolsql.view.View;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 * 
 */
public class GUIUtil {

	public static final int DEFAULT_VIEWWIDTH=200;
	
	private static JFrame mainFrame = null; // cache main frame
	/**
	 * ��ǰ�����
	 */
	private static String currentLookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";

	public static File selectFolder(Container con,String initDir,boolean isOpen)
	{
		JFileChooser fc = new JFileChooser(initDir);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setMultiSelectionEnabled(false); // ֻ����ѡ��һ���ļ�
		if(con==null)
			con=getMainFrame();
		
		int select = isOpen?fc.showOpenDialog(con):fc.showSaveDialog(con);
		if (select == JFileChooser.APPROVE_OPTION) { // ���ѡ�����ļ�
			return fc.getSelectedFile();
		}else
			return null;
	}
	/**
	 * ѡ��һ�ļ�����ѡ��ΧΪ�����ļ�
	 * 
	 * @param con
	 *            ����������
	 * @return
	 */
	public static File selectFileNoFilter(Container con) {
		return selectFileNoFilter(con, PropertyManage.getSystemProperty()
				.getSelectFile_exportData(),false);
	}

	/**
	 * ѡ��һ�ļ�����ѡ��ΧΪ�����ļ�
	 * 
	 * @param con
	 * @param filter
	 *            ������
	 * @return
	 */
	public static File selectFileByFilter(Container con, FileFilter filter) {
		File file[]= selectFileByFilter(con, filter,new FileFilter[]{filter}, PropertyManage
				.getSystemProperty().getSelectFile_exportData(),false,false,true);
		if(file==null||file.length==0)
			return null;
		return file[0];
	}
	public static File selectFileByFilter(Container con, FileFilter filter,boolean isPromptOnExist) {
		File file[]= selectFileByFilter(con, filter,new FileFilter[]{filter}, PropertyManage
				.getSystemProperty().getSelectFile_exportData(),false,false,isPromptOnExist);
		if(file==null||file.length==0)
			return null;
		return file[0];
	}
	public static File selectFileNoFilter(Container con, String currentDir)
	{
		return selectFileNoFilter(con, currentDir,false);
	}
	public static File FileSelectFileNoFilter(Container con,String currentDir,boolean isPromptOnExist)
	{
		//single select,save dialog
		File file[]= selectFileNoFilter(con,currentDir,false,false,isPromptOnExist);
		if(file==null||file.length==0)
			return null;
		return file[0];
	}
	public static File selectFileNoFilter(Container con, String currentDir,boolean isOpen) {
		File[] file=selectFileNoFilter(con,currentDir,false,isOpen,true);
		if(file==null||file.length==0)
			return null;
		return file[0];
	}
	public static File[] selectFileNoFilter(Container con, String currentDir,
			boolean isMultiSelectable, boolean isOpen, boolean isPromptOnExist) {
		JFileChooser fc = new JFileChooser(currentDir);
		fc.setMultiSelectionEnabled(isMultiSelectable); // ֻ����ѡ��һ���ļ�
		if(con==null)
			con=getMainFrame();
		
		int select = isOpen?fc.showOpenDialog(con):fc.showSaveDialog(con);
		if (select == JFileChooser.APPROVE_OPTION) { // ���ѡ�����ļ�
			File[] tmp;
			if(isMultiSelectable)
				tmp = fc.getSelectedFiles(); // ��ȡѡ�е��ļ�
			else
				tmp=new File[]{fc.getSelectedFile()};
			if (tmp != null&&tmp.length>0) {
				// currentPath = tmp.getParent(); //���µ�ǰ��ѡ�ļ���Ŀ¼
				if (!isOpen&&isPromptOnExist&&tmp[0].exists()) {
					int result = JOptionPane.showConfirmDialog(con,
							PublicResource.getString("fileselect.exist"),
							"confirm!", JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.NO_OPTION)
						return selectFileNoFilter(con, tmp[0].getParent(),isMultiSelectable,isOpen,isPromptOnExist);
				}
			}
			return tmp;
		} else
			return null;
	}
	public static File selectFileByFilter(Container con, FileFilter filter,
			String currentDir) {
		File file[]= selectFileByFilter(con,filter,filter!=null?new FileFilter[]{filter}:null,currentDir,false,true,false);
		if(file==null||file.length==0)
			return null;
		return file[0];
	}
	public static File selectFileByFilter(Container con, FileFilter filter,
			String currentDir,boolean isPromptOnExist) {
		File file[]= selectFileByFilter(con,filter,filter!=null?new FileFilter[]{filter}:null,currentDir,false,true,isPromptOnExist);
		if(file==null||file.length==0)
			return null;
		return file[0];
	}
	public static File[] selectFileByFilter(Container con, FileFilter initialFilter,FileFilter filters[],
			String currentDir,boolean isMutiSelectable,boolean isOpen,boolean isPromptOnExist) {
		JFileChooser fc = new JFileChooser(currentDir);

		if(filters!=null)
		{
			for(int i=0;i<filters.length;i++)
			{
				if(filters[i]==null)
					continue;
				fc.addChoosableFileFilter(filters[i]);
			}
		}
		if(initialFilter!=null)
			fc.setFileFilter(initialFilter);
		fc.setMultiSelectionEnabled(isMutiSelectable);
		int select = isOpen?fc.showOpenDialog(con):fc.showSaveDialog(con);
		if (select == JFileChooser.APPROVE_OPTION) { // ���ѡ�����ļ�
			File[] tmp =null;
			if(isMutiSelectable)
				tmp=fc.getSelectedFiles(); // ��ȡѡ�е��ļ�
			else
			{
				tmp=new File[]{fc.getSelectedFile()};
			}
			if (tmp != null&&tmp.length==1) {//do only  when one file is selected .
				// currentPath = tmp.getParent(); //���µ�ǰ��ѡ�ļ���Ŀ¼
				if (isPromptOnExist&&!isOpen&&tmp[0].exists()) {
					int result = JOptionPane.showConfirmDialog(con,
							PublicResource.getString("fileselect.exist"),
							"confirm!", JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.NO_OPTION)
						return selectFileByFilter(con,initialFilter ,filters,currentDir,isMutiSelectable,isOpen,isPromptOnExist);
				}
			}
			return tmp;
		} else
			return null;
	}
	/**
	 * ���ò���ϵͳ�����ļ�ѡ��Ի�����ѡ���ļ�
	 */
	public static File selectFileNoFilterWindow(JFrame con) {
		return selectFileNoFilterWindow(con, null);
	}

	/**
	 * ���ò���ϵͳ�����ļ�ѡ��Ի�����ѡ���ļ�
	 */
	public static File selectFileNoFilterWindow(JFrame con,
			FilenameFilter filter) {
		if(con==null)
			con=getMainFrame();
		FileDialog fd = new FileDialog(con, "select a file", FileDialog.LOAD);
		if (filter != null)
			fd.setFilenameFilter(filter);
		fd.setVisible(true);
		if (fd.getDirectory() == null || fd.getFile() == null)
			return null;
		else {
			File file = new File(fd.getDirectory(), fd.getFile());
			return file;
		}
	}

	/**
	 * ��ȡ��ؼ����б�ʶ
	 * 
	 * @param table
	 *            --��ؼ�
	 * @return --�б�ʶ
	 */
	public static Vector getColumnIdenfiers(JTable table) {
		TableColumnModel columnModel = table.getColumnModel();
		if (columnModel == null)
			return null;
		int count = columnModel.getColumnCount();
		Vector ob = new Vector(count);
		for (int i = 0; i < count; i++)
			ob.add(columnModel.getColumn(i).getHeaderValue());
		return ob;
	}

	/**
	 * Get main frame object instance.
	 */
	public static JFrame getMainFrame() {
		if (mainFrame == null)
			mainFrame = new MainFrame();
		return mainFrame;
	}
	public static void updateSystemStatusBarForSQLEditor(String info)
	{
		if(info==null)
			return;
		BorderLayout layout=(BorderLayout)mainFrame.getContentPane().getLayout();
		Component com=layout.getLayoutComponent(BorderLayout.SOUTH);
		if(com instanceof MainFrameStatusBar)
		{
			((MainFrameStatusBar)com).setEditorInfo(info);
		}
	}
	/**
	 * Add a status bar to the main frame
	 */
	public static void showStatusBarOfMainFrame()
	{
		if(mainFrame==null)
			return;
		BorderLayout layout=(BorderLayout)mainFrame.getContentPane().getLayout();
		Component com=layout.getLayoutComponent(BorderLayout.SOUTH);
		if(com instanceof MainFrameStatusBar)
			return;
		
		JPanel pane=(JPanel)mainFrame.getContentPane();
		MainFrameStatusBar statusBar=new MainFrameStatusBar();
		pane.add(statusBar,BorderLayout.SOUTH);
		pane.validate();
	}
	/**
	 * Hide system status bar.
	 */
	public static void hideStatusBarOfMainFrame()
	{
		if(mainFrame==null)
			return;
//		JPanel pane=(JPanel)mainFrame.getContentPane();
//		Component[] components=pane.getComponents();
//		
//		for(Component com:components)
//		{
//			if(com instanceof MainFrameStatusBar)
//			{
//				((MainFrameStatusBar)com).dispose();
//				pane.remove(com);
//			}
//		}
//		pane.validate();
		BorderLayout layout=(BorderLayout)mainFrame.getContentPane().getLayout();
		Component com=layout.getLayoutComponent(BorderLayout.SOUTH);
		if(com instanceof MainFrameStatusBar)
		{
			((MainFrameStatusBar)com).dispose();
			mainFrame.getContentPane().remove(com);
		}
		mainFrame.getContentPane().validate();
	}
	/**
	 * ��ȡ��˵���Ķ��������JPopupMenu�������ò˵����ǵ����˵�������null
	 * 
	 * @param item
	 *            --ָ���Ĳ˵���
	 * @return
	 */
	public static JPopupMenu getTopMenu(JMenuItem item) {
		JPopupMenu popMenu = (JPopupMenu) getUpParent(item, JPopupMenu.class,
				false);
		while (popMenu.getInvoker() instanceof JMenu) {
			popMenu = (JPopupMenu) getUpParent((JComponent) popMenu
					.getInvoker(), JPopupMenu.class, false);
		}
		return popMenu;
	}

	/**
	 * ��ȡ������Ķ������(parent)���������������ָ�����͵ĸ������������null
	 * 
	 * @param com
	 *            --��������
	 * @param parent
	 *            --�����������
	 * @return --���ӵĸ�������
	 */
	public static Container getUpParent(Container com, Class parent) {
		return getUpParent(com, parent, true);
	}

	/**
	 * ��ȡ������Ķ������(parent)��������isMustVisibleΪtrue�����뷵�ؿ��ӵĸ��������ڣ��������������ָ�����͵ĸ������������null
	 * 
	 * @param com
	 *            --��������
	 * @param parent
	 *            --�����������
	 * @param isMustVisible
	 *            --�Ƿ�����ȡ���ӵĸ�����
	 * @return --���ӵĸ�������
	 */
	public static Container getUpParent(Container com, Class parent,
			boolean isMustVisible) {
		if (com == null || parent == null)
			return null;
		if (parent == com.getClass())
			return com;
		Container con = com.getParent();
		for (; con != null && !parent.isAssignableFrom(con.getClass()); con = con
				.getParent());
		if (con != null && !con.isVisible() && isMustVisible) // �������ȡ���ӵ�������������ݹ�������?��
		{
			con = getUpParent(con, parent, isMustVisible);
		}
		return con;
	}

	/**
	 * ��������Ŀ�����
	 * 
	 * @param isEnable
	 * @param btn
	 */
	public static void setComponentEnabled(boolean isEnable, JComponent btn) {
		if (btn.isEnabled() != isEnable)
			btn.setEnabled(isEnable);
	}

	/**
	 * ����Ŀ¼�ṹ�����Ŀ¼�����ڣ�����֮��������ļ���Ŀ¼������������Ŀ¼�����Ϊ�ļ�����ݱ�־�ж��Ƿ񴴽��ļ�
	 * 
	 * @param fileName
	 *            --�ļ���
	 * @param isDirectory
	 *            --fileName���Ӧ���ļ��Ƿ�ΪĿ¼
	 * @param isCreate
	 *            --true��������ļ������� �� false��������ļ���ֻ�����ļ����ϲ�Ŀ¼
	 * @throws IOException
	 *             --����ļ����?�׳����쳣
	 */
	public static void createDir(String fileName, boolean isDirectory,
			boolean isCreate) throws IOException {
		File file = new File(fileName);
		if (file.exists())
			return;
		else {
			File parentFile = file.getParentFile();
			if (parentFile != null && !parentFile.exists())
				createDir(parentFile.getAbsolutePath(), true, true);
			else if (parentFile != null && !parentFile.isDirectory()) {
				throw new IOException("Ŀ¼����������ļ�����:"
						+ parentFile.getAbsolutePath());
			}

			if (isDirectory) // ����ϼ�Ŀ¼���ڣ����ҵ�ǰ�ļ�ΪĿ¼��������ǰĿ¼
				file.mkdir();
			else if (isCreate) {
				file.createNewFile();
			}
		}
	}

	/**
	 * ��һ��������Ϊ���գ�ʹ��һ�������������м�. �����Ϊnull������Ļ��Ϊ����
	 * 
	 * @param owner
	 *            ��������
	 * @param frame
	 *            ����������
	 */
	public static void centerFrameToFrame(Container owner, Container frame) {
		if (owner == null) {
			Dimension rect = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setBounds((int) ((rect.getWidth() - frame.getWidth()) / 2),
					(int) ((rect.getHeight() - frame.getHeight()) / 2), frame
							.getWidth(), frame.getHeight());

			return;
		}
		Rectangle rect = owner.getBounds();
		frame
				.setBounds((int) (rect.getX() + (rect.getWidth() - frame
						.getWidth()) / 2), (int) (rect.getY() + (rect
						.getHeight() - frame.getHeight()) / 2), frame
						.getWidth(), frame.getHeight());
	}
	public static void centerToOwnerWindow(Window frame)
	{
		if(frame==null)
			return;
		Container owner=frame.getOwner();
		if(owner==null)
			return;
		centerFrameToFrame(owner,frame);
	}
	/**
	 * ��Ⱦ�����������
	 * 
	 * @param theme
	 *            --�µ�������
	 * @param com
	 *            --����Ⱦ�����
	 */
	public static void renderTheme(MetalTheme theme, Component com) {

		UIManager.put("swing.boldMetal", Boolean.FALSE);
		if (MetalLookAndFeel.class.isAssignableFrom(UIManager.getLookAndFeel()
				.getClass())) // ���ǰ���ΪMetalLookAndFeel�������������⡣
			MetalLookAndFeel.setCurrentTheme(theme);
		try {
			UIManager.setLookAndFeel(currentLookAndFeel);

			if (com != null)
				SwingUtilities.updateComponentTreeUI(com);
		} catch (Exception e) {
			LogProxy.internalError(e);
			JOptionPane.showMessageDialog(com == null ? getMainFrame() : com,
					"����������?����ԭ��Ϊ��" + e.getMessage());
		}

	}

	/**
	 * ���嵱ǰ�����
	 * 
	 * @param currentLookAndFeel
	 *            --������
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws UnsupportedLookAndFeelException
	 */
	public static void setCurrentLookAndFeel(String currentLookAndFeel)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		if (currentLookAndFeel == null)
			return;
		GUIUtil.currentLookAndFeel = currentLookAndFeel;
		UIManager.setLookAndFeel(currentLookAndFeel);
		JFrame frame = getMainFrame();
		if (frame != null)
			SwingUtilities.updateComponentTreeUI(frame);
	}

	/**
	 * ��ȡ���ؼ���ָ���е���ѿ��
	 * 
	 * @param table
	 *            --��ı�ؼ�
	 * @param columnIndex
	 *            --��ָ������
	 * @return --��ѿ��
	 */
	public static int[] getPreferredColumnWidth(JTable table, int[] columnIndex) {
		if (columnIndex == null || columnIndex.length < 0 || table == null)
			return null;

		/**
		 * ��ʼ�����е���Ⱦ��
		 */
		TableCellRenderer[] render = new TableCellRenderer[columnIndex.length];
		for (int i = 0; i < columnIndex.length; i++) {
			render[i] = table.getColumnModel().getColumn(columnIndex[i])
					.getCellRenderer();
			if (render[i] == null) {
				render[i] = table.getDefaultRenderer(table
						.getColumnClass(columnIndex[i]));
			}
		}

		TableCellRenderer headerRender = table.getTableHeader()
				.getDefaultRenderer();
		int[] width = new int[columnIndex.length];
		for (int i = 0; i < width.length; i++) {
			width[i] = (int) headerRender.getTableCellRendererComponent(
					table,
					table.getColumnModel().getColumn(columnIndex[i])
							.getHeaderValue(), false, false, 0, columnIndex[i])
					.getPreferredSize().getWidth();
			// ���Ȼ�ȡ������ĳ���
			width[i] += 6; // ���Ӹ�ѡ��Ŀ��
		}

		FontMetrics fm = null;
		int additionWidth = 0;
		/**
		 * ѭ��������е�������ݣ���ȡ�����ʾ����
		 */
		for (int i = 0; i < table.getRowCount(); i++) {
			for (int j = 0; j < columnIndex.length; j++) {
				Object tmpValue = table.getValueAt(i, columnIndex[j]);
				if (tmpValue == null)
					continue;

				Component com = render[j].getTableCellRendererComponent(table,
						tmpValue, false, false, i, columnIndex[j]);

				if (fm == null) {
					fm = com.getFontMetrics(com.getFont());
					additionWidth = fm.stringWidth("xx");
				}

				int tmpWidth = (int) com.getPreferredSize().getWidth()
						+ additionWidth;
				if (tmpWidth > width[j])
					width[j] = tmpWidth;
			}
		}
		return width;
	}

	/**
	 * �����е���ǩ��Ϣ���ڵ����������ѡ��ؼ���
	 * 
	 * @param box
	 *            --װ����ǩ��Ϣ��������ѡ��ؼ�
	 */
	public static void loadBookmarksToComboBox(JComboBox box) {
		MutableComboBoxModel model = (MutableComboBoxModel) box.getModel();
		Set set = BookmarkManage.getInstance().getAliases();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			model.addElement(it.next());
		}

		// ����ǩѡ�����ó�ȱʡ��ǩ
		Bookmark bookmark = BookmarkManage.getInstance().getDefaultBookmark();
		model
				.setSelectedItem(bookmark == null ? null : bookmark
						.getAliasName());
	}

	/**
	 * ����Ŀ�ݼ���ص�ָ��������У���������ȫ�ֺ;ֲ����ֿ�ݷ�ʽ
	 * 
	 * @param componnet
	 *            --��Ҫ��ӿ�ݼ�����
	 * @param key
	 *            --��ݼ�
	 * @param action
	 *            --��ݼ�ִ�еĴ���
	 * @param isGlobal
	 *            --�Ƿ���ȫ�ַ�ʽ
	 */
	public static void bindShortKey(JComponent componnet, String key,
			Action action, boolean isGlobal) {
		KeyStroke stroke = KeyStroke.getKeyStroke(key);
		if (isGlobal)
			componnet.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
					stroke, key);
		else
			componnet.getInputMap().put(stroke, key);
		componnet.getActionMap().put(key, action);
	}
	/**
	 * ���丸���ӵ�н���ʱ��������Ӧ�Ĵ����¼�
	 * 
	 * @param componnet
	 *            --���ܿ�ݼ�����
	 * @param key
	 *            --��ݼ�
	 * @param action
	 *            --��ݼ�ִ�еĴ���
	 */
	public static void bindShortKeyInAncestor(JComponent componnet, String key,
			Action action) {
		KeyStroke stroke = KeyStroke.getKeyStroke(key);
		componnet.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(stroke, key);
		componnet.getActionMap().put(key, action);
	}
	/**
	 * ������Ļ���ڵĳߴ磬���ø��ڶ���Ĵ�С
	 * 
	 * @param con
	 *            --��Ҫ���ô�С�Ĵ��ڶ���
	 * @param widthRate
	 *            --��Ҫ���õĿ������Ļ��ȵı�ֵ
	 * @param heightRate
	 *            --��Ҫ���õĸ߶�����Ļ�߶ȵı�ֵ
	 */
	public static void setFrameSizeToScreen(Container con, float widthRate,
			float heightRate) {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); // ��Ļ���ؿ��
		con.setSize((int) (d.getWidth() * widthRate),
				(int) (d.getHeight() * heightRate));
	}
	/**
	 * ��ȡȱʡ���������ƶ���
	 * 
	 * @return
	 */
	public static GridBagConstraints getDefaultBagConstraints() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(2, 2, 2, 2);
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridwidth = 1;

		return gbc;
	}
	/**
	 * ������������con�����غ���ʾʱ�����ϲ��SplitPane�����غ���ʾ��
	 * 
	 * @param con
	 *            --���������������
	 * @param isHidden
	 *            ��������con�Ƿ�����
	 */
	public static void controlSplit(Container con, boolean isHidden) {
		JSplitPane split = getSplitContainer(con);
		if (split == null)
			return;
		Component left = split.getLeftComponent();
		Component right = split.getRightComponent();
		if (isHidden) {
			if ((left == null || !left.isVisible())
					&& (right == null || !right.isVisible())) // ���split����������ڻ������أ������Ҳ����
			{
				split.setVisible(false);
				controlSplit(split, true);
				
			}
		} else {
			if (!split.isVisible()) // ���split���أ�Ϊ�˱�֤����con����ʾ��������ʾ����
			{
				split.setVisible(true);
				controlSplit(split, false); // ensure that parent container is
											// visible.
			}
//			int currentLocation=split.getDividerLocation();
			if(isMaxSplitToSelf(split))
			{
				if(left!=null&&left.isVisible()&&right!=null&&right.isVisible())
				{
						Integer location=(Integer)split.getClientProperty(View.LASTLOCATION);
						if(location==null)
							location=DEFAULT_VIEWWIDTH;
						if (location >split.getMinimumDividerLocation()
								&&location <split.getMaximumDividerLocation())
							split.setDividerLocation(location);
				}
			}
		}
	}

	/**
	 * ��ȡ��������ڵ�Split����
	 * 
	 * @return --JSplitPane����
	 */
	public static JSplitPane getSplitContainer(Container con) {
		Container p;
		for (p = con.getParent(); p != null && !(p instanceof JSplitPane); p = p
				.getParent());
		JSplitPane split = (JSplitPane) p;
		return split;
	}
	/**
	 * �ж�Jsplitpane�����Ƿ������״̬(�����������ڶ���)
	 */
	public static boolean isMaxSplitToParent(JSplitPane split) {
		if (split == null)
			return false;
		if (isMaxSplitToSelf(split)) {
			JSplitPane tmp = GUIUtil.getSplitContainer(split);
			if (tmp == null)
				return true;
			if (GUIUtil.isMaxSplitToSelf(tmp)) {
				tmp = GUIUtil.getSplitContainer(tmp);
				if (tmp == null)
					return true;
				else
					return GUIUtil.isMaxSplitToSelf(tmp); // �Եݹ����ʽ�ж�
			} else
				return false;
		} else
			return false;
	}
	/**
	 * �ж�Jsplitpane�����Ƿ������״̬(������������)
	 * 
	 * @param split
	 * @return
	 */
	public static boolean isMaxSplitToSelf(JSplitPane split) {
		if (split == null)
			return false;
		Component left = split.getLeftComponent();
		Component right = split.getRightComponent();
		if (!left.isVisible() || !right.isVisible())
			return true;

		int currentLocation = split.getDividerLocation();
		if (currentLocation < split.getMinimumDividerLocation()
				|| currentLocation > split.getMaximumDividerLocation())
			return true;
		else
			return false;
	}

	/**
	 * �ж�ָ����JSplitPane�Ƿ������size
	 * 
	 * @param split
	 * @param �������󷵻ؿռ�ķֲ��������ԣ����򷵻�-1
	 */
	public static int isMaxState(JSplitPane split) {
		int type = split.getOrientation();
		if (isMaxSplitToSelf(split))
			return -1;
		else
			return type;
	}
	/**
	 * get the scale size of screen
	 * @param scale  
	 * @return
	 */
	public static Dimension getScaleScreenDimension(double scale)
	{
		if(scale>1)
			scale=1;
		if(scale<0)  //value of minimum scale is 0.1
			scale=.1;
		Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
		return new Dimension((int)(screenSize.width*scale),(int)(screenSize.height*scale));
	}
	public static void processOnSwingEventThread(Runnable todo)
	{
		processOnSwingEventThread(todo, false);
	}

	public static void processOnSwingEventThread(Runnable todo, boolean wait)
	{
		if (todo == null)
		{
			throw new IllegalArgumentException("Runnable == null");
		}

		if (wait)
		{
			if (SwingUtilities.isEventDispatchThread())
			{
				todo.run();
			}
			else
			{
				try
				{
					SwingUtilities.invokeAndWait(todo);
				}
				catch (InvocationTargetException ex)
				{
					throw new RuntimeException(ex);
				}
				catch (InterruptedException ex)
				{
					throw new RuntimeException(ex);
				}
			}
		}
		else
		{
            if (SwingUtilities.isEventDispatchThread()) {
                todo.run();
            } else {
                SwingUtilities.invokeLater(todo);
            }
		}
	}
    /**
     * Returns the focused Window, if the focused Window is in the same context
     * as the calling thread. The focused Window is the Window that is or
     * contains the focus owner.
     *
     * @return the focused Window
     */         
	public static Window findLikelyOwnerWindow() {
		Window result = KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.getFocusedWindow();
		if (result == null) {
			result = getMainFrame();
		}
		
		return result;
	}
	/**
	 * install a popup menu to specified textcomponent
	 * @param textComponent
	 */
	public static void installDefaultTextPopMenu(JTextComponent textComponent)
	{
		if(textComponent!=null)
			new TextComponentPopMenu(textComponent);
	}
	public static boolean getYesNo(String aMessage)
	{
		return getYesNo(findLikelyOwnerWindow(),aMessage);
	}
	public static boolean getYesNo(Component aCaller, String aMessage)
	{
		int result = JOptionPane.showConfirmDialog(aCaller == null ? getMainFrame() : SwingUtilities.getWindowAncestor(aCaller), aMessage, "CoolSQL", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		return (result == JOptionPane.YES_OPTION);
	}
}
