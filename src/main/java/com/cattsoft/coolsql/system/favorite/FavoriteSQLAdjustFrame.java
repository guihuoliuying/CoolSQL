package com.cattsoft.coolsql.system.favorite;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.cattsoft.coolsql.pub.component.BaseDialog;
import com.cattsoft.coolsql.pub.component.BasePopupMenu;
import com.cattsoft.coolsql.pub.component.CancelButton;
import com.cattsoft.coolsql.pub.component.JDragableList;
import com.cattsoft.coolsql.pub.component.RenderButton;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.display.PopMenuMouseListener;
import com.cattsoft.coolsql.pub.editable.CloseWindowListener;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.system.menubuild.IconResource;
import com.cattsoft.coolsql.view.ViewManage;
import com.cattsoft.coolsql.view.sqleditor.EditorPanel;

/**
 * @author kenny liu ����sql�ղؼ� 2007-11-5 create
 */
public class FavoriteSQLAdjustFrame extends BaseDialog {
	private static final long serialVersionUID = 1L;
	
	private static final StringManager stringMgr=StringManagerFactory.getStringManager(FavoriteSQLAdjustFrame.class);

	private RenderButton addSQL = null; // ���sql��ť

	private RenderButton delSQL = null; // ɾ��sql��ť

	private RenderButton upMoveBtn = null;// ����sql��ť

	private RenderButton downMoveBtn = null;// ����sql��ť

	private JList list = null; // չ���ղص�sql�ؼ�

	private BasePopupMenu popMenu; //the popup menu of sql list component
	private JMenuItem copySQL;
	private JMenuItem addSelectedToEditor;
	public FavoriteSQLAdjustFrame() {
		this(GUIUtil.getMainFrame());
	}
	public FavoriteSQLAdjustFrame(JFrame frame) {
		super(frame);
		init();

	}
	public FavoriteSQLAdjustFrame(JDialog frame) {
		super(frame);
		init();
	}
	private void init() {
		setTitle(PublicResource
				.getSystemString("system.menu.adjustfavoriteframe.title"));
		setModal(true);
		JPanel mainPane = (JPanel) getContentPane();
		mainPane.setLayout(new BorderLayout());

		list = new JDragableList();

		/**
		 * ����sql��Ŀ������ť
		 */
		JPanel operatePane = new JPanel();
		operatePane.setLayout(new BoxLayout(operatePane, BoxLayout.Y_AXIS));
		addSQL = new RenderButton(PublicResource
				.getSystemString("system.adjustfavoriteframe.addbtn.label"));
		delSQL = new RenderButton(PublicResource
				.getSystemString("system.adjustfavoriteframe.delbtn.label"));
		upMoveBtn = new RenderButton(PublicResource
				.getSystemString("system.adjustfavoriteframe.upmovebtn.label"));
		downMoveBtn = new RenderButton(
				PublicResource
						.getSystemString("system.adjustfavoriteframe.downmovebtn.label"));
		operatePane.add(addSQL);
		operatePane.add(Box.createVerticalStrut(12));
		operatePane.add(delSQL);
		operatePane.add(Box.createVerticalStrut(12));
		operatePane.add(upMoveBtn);
		operatePane.add(Box.createVerticalStrut(12));
		operatePane.add(downMoveBtn);

		addSQL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean isOk = false;
				while (!isOk) {
					String r = JOptionPane
							.showInputDialog(
									FavoriteSQLAdjustFrame.this,
									PublicResource
											.getSystemString("system.menu.action.collectsql"),
									"");
					if (r != null && !r.trim().equals("")) {
						addFavoriteSQL(r);
						isOk = true;
					} else {
						if(r==null)
						{
							isOk=true;
							continue;
						}
						JOptionPane.showMessageDialog(
								FavoriteSQLAdjustFrame.this,
								"please input valid value");
					}
				}
			}
		});
		delSQL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteFavoriteSQL(list.getSelectedIndices());
			}
		});
		upMoveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveSQL(list.getSelectedIndex(), list.getSelectedIndex() - 1);
			}
		});
		downMoveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveSQL(list.getSelectedIndex(), list.getSelectedIndex() + 1);
			}
		});

		/**
		 * ����Ӧ�ò������(ȷ�ϰ�ť��ȡ��ť)
		 */
		JPanel applyPane = new JPanel();
		applyPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton okBtn = new RenderButton(PublicResource
				.getString("propertyframe.button.ok"));
		JButton cancelBtn = new CancelButton(this);
		applyPane.add(okBtn);
		applyPane.add(cancelBtn);

		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				finishAdjusting();
				FavoriteSQLAdjustFrame.this.dispose();
			}
		});
		createListPopMenu();//Create popup menu.

		mainPane.add(BorderLayout.CENTER, list);
		mainPane.add(BorderLayout.EAST, operatePane);
		mainPane.add(BorderLayout.SOUTH, applyPane);
		addWindowListener(new CloseWindowListener(this));
		setSize(500, 500);

		loadFavoriteSQLs();
	}
	private void createListPopMenu()
	{
		popMenu=new BasePopupMenu();
		copySQL=new JMenuItem(stringMgr.getString("favorite.frame.popupmenu.copy.label"),IconResource.getBlankIcon());
		copySQL.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e) {
				Object[] selectedData=list.getSelectedValues();
				if(selectedData==null||selectedData.length==0)
					return;
				StringBuilder sb=new StringBuilder();
				Clipboard clp = Toolkit.getDefaultToolkit().getSystemClipboard();
				for(Object data:selectedData)
				{
					sb.append(data.toString()).append("\n");
				}
				StringSelection sel = new StringSelection(sb.toString());
				clp.setContents(sel, sel);
			}
			
		}
		);
		popMenu.add(copySQL);
		addSelectedToEditor=new JMenuItem(stringMgr.getString("favorite.frame.popupmenu.addSelectedToEditor.label")
				,IconResource.getBlankIcon());
		addSelectedToEditor.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e) {
				Object[] selectedData=list.getSelectedValues();
				if(selectedData==null||selectedData.length==0)
					return;
				StringBuilder sb=new StringBuilder("\n");
				int i=0;
				for(; i<selectedData.length-1;i++)
				{
					sb.append(selectedData[i].toString()).append("\n");
				}
				sb.append(selectedData[i].toString());
				EditorPanel editor=ViewManage.getInstance().getSqlEditor().getEditorPane();
				editor.insertText(editor.getDocument().getLength(), sb.toString());
			}
			
		});
		popMenu.add(addSelectedToEditor);
		
		list.addMouseListener(new PopMenuMouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				if(isPopupTrigger(e))
				{
					popMenu.show(list, e.getX(), e.getY());
				}
			}
		}
		);
	}
	/**
	 * ����µ�sql
	 * 
	 * @param sql
	 */
	private void addFavoriteSQL(String sql) {
		DefaultListModel model = (DefaultListModel) list.getModel();
		model.addElement(sql);
	}
	/**
	 * �����Ѿ��ղص�sql
	 * 
	 */
	private void loadFavoriteSQLs() {
		if (list == null)
			return;
		DefaultListModel model = (DefaultListModel) list.getModel();
		List f = FavoriteManage.getInstance().getSQLList();
		for (Iterator it = f.iterator(); it.hasNext();) {
			model.addElement(it.next().toString());
		}
	}
	/**
	 * ������λ��sourceIndex�Ķ�����targetIndex�Ķ�����н���
	 * 
	 * @param sourceIndex
	 * @param targetIndex
	 */
	private void moveSQL(int sourceIndex, int targetIndex) {
		if (list == null)
			return;

		DefaultListModel model = (DefaultListModel) list.getModel();
		if (sourceIndex < 0 || sourceIndex >= model.getSize()
				|| targetIndex < 0 || targetIndex >= model.getSize())
			return;
		Object targetObj = model.set(targetIndex, model.get(sourceIndex));
		model.set(sourceIndex, targetObj);
		list.setSelectedIndex(targetIndex);
	}
	/**
	 * ɾ��ָ����sql
	 * 
	 * @param index
	 *            --list�ؼ�Ԫ�ص�����
	 */
	@SuppressWarnings("unused")
	private void deleteFavoriteSQL(int index) {
		if (list == null || index < 0 || index >= list.getModel().getSize()) {
			JOptionPane.showMessageDialog(this,
					"please select sql that will be deleted!");
			return;
		}

		DefaultListModel model = (DefaultListModel) list.getModel();
		model.remove(index);
	}
	private void deleteFavoriteSQL(int[] index) {
		if (index == null || index.length == 0)
			return;
		DefaultListModel model = (DefaultListModel) list.getModel();
		for (int i = index.length - 1; i > -1; i--) {
			model.remove(index[i]);
		}
		int firstIndex=index[0];
		if(firstIndex<0||firstIndex>model.getSize()-1)
			firstIndex=model.getSize()-1;
		list.setSelectedIndex(firstIndex);
	}
	/**
	 * ȷ���޸�
	 * 
	 */
	private void finishAdjusting() {
		FavoriteManage.getInstance().clearAll();
		DefaultListModel model = (DefaultListModel) list.getModel();
		List array = Arrays.asList(model.toArray());

		FavoriteManage.getInstance().addSQLs(array);
	}
}
