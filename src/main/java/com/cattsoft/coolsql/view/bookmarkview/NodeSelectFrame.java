/**
 * 
 */
package com.cattsoft.coolsql.view.bookmarkview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.tree.DefaultTreeModel;

import com.cattsoft.coolsql.pub.component.BaseDialog;
import com.cattsoft.coolsql.pub.component.RenderButton;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.display.IActionTransfer;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.view.bookmarkview.model.DefaultTreeNode;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-3-15 create
 */
public class NodeSelectFrame extends BaseDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private IActionTransfer actionTransfer;
	private DbObjectSelectPanel selectPanel;
	public NodeSelectFrame(IActionTransfer actionTransfer)
	{
		this((DefaultTreeNode)null,actionTransfer);
	}
	public NodeSelectFrame(DefaultTreeNode rootNode,IActionTransfer actionTransfer)
	{
		this(GUIUtil.getMainFrame(),rootNode,actionTransfer);
	}
	public NodeSelectFrame(JDialog dialog,IActionTransfer actionTransfer)
	{
		this(dialog,null,actionTransfer);
	}
	public NodeSelectFrame(JFrame dialog,IActionTransfer actionTransfer)
	{
		this(dialog,null,actionTransfer);
	}
	public NodeSelectFrame(JDialog dialog,DefaultTreeNode rootNode,IActionTransfer actionTransfer)
	{
		super(dialog,PublicResource.getString("nodeselect.frame.title"));
		
		if(actionTransfer==null)
			throw new IllegalArgumentException("Please specify a action transfer interface!");
		this.actionTransfer=actionTransfer;
		initFrame(rootNode);
	}
	public NodeSelectFrame(JFrame dialog,DefaultTreeNode rootNode,IActionTransfer actionTransfer)
	{
		super(dialog,PublicResource.getString("nodeselect.frame.title"));
		
		if(actionTransfer==null)
			throw new IllegalArgumentException("Please specify a action transfer interface!");
		this.actionTransfer=actionTransfer;
		initFrame(rootNode);
	}
	protected void initFrame(DefaultTreeNode rootNode)
	{
		JPanel main=(JPanel)getContentPane();
		selectPanel=new DbObjectSelectPanel(rootNode);
		
		main.add(selectPanel,BorderLayout.CENTER);
		
		JPanel buttonPanel=new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		RenderButton okButton=new RenderButton(PublicResource.getOkButtonLabel());
		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				actionTransfer.doAction(selectPanel.getTree());
			}
		}
		);
		buttonPanel.add(okButton);
		RenderButton quitButton=new RenderButton(PublicResource.getCancelButtonLabel());
		quitButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				closeFrame();
			}
			
		}
		);
		buttonPanel.add(quitButton);
		main.add(buttonPanel,BorderLayout.SOUTH);
		
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				closeFrame();
			}
		}
		);
		
		setMinimumSize(new Dimension(350,500));
		pack();
		GUIUtil.centerToOwnerWindow(this);
		
	}
	/**
	 * close this frame.
	 *
	 */
	protected void closeFrame()
	{
		removeAll();
		dispose();
	}
	public void setRoot(DefaultTreeNode root)
	{
		DefaultTreeModel model=(DefaultTreeModel)selectPanel.getTree().getModel();
		model.setRoot(root);
	}
}
