/*
 * �������� 2006-6-7
 *
 */
package com.cattsoft.coolsql.action.bookmarkmenu;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import com.cattsoft.coolsql.action.common.PublicAction;
import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.bookmarkBean.BookmarkManage;
import com.cattsoft.coolsql.pub.component.CommonFrame;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.view.BookmarkView;
import com.cattsoft.coolsql.view.View;


/**
 * @author liu_xlin ����ǩ�������������
 */
public class ReNameAction extends PublicAction {
	public ReNameAction(View view) {
		super(view);
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) (((BookmarkView) getComponent())
				.getConnectTree().getLastSelectedPathComponent());
		Object ob = node.getUserObject();
		if (ob instanceof Bookmark) {
			Bookmark bm = (Bookmark) ob;
			boolean isOk = false;
			while (!isOk) {
				String result = JOptionPane.showInputDialog(CommonFrame
						.getParentFrame(this.getComponent()), PublicResource
						.getString("aliasnamechangedialog.prompt"), bm
						.getAliasName());
				if (result != null && !result.trim().equals("")) {
					if (!bm.getAliasName().equals(result)) {
						if (!result.equals(result.trim())) //���ܰ�ո�
						{
							JOptionPane
									.showMessageDialog(
											CommonFrame.getParentFrame(this
													.getComponent()),
											PublicResource
													.getString("aliasnamechangedialog.havespaces"),
											"warning", 2);
							continue;
						}
						if (BookmarkManage.getInstance().isExist(result)) //����������ظ�
						{
							JOptionPane
									.showMessageDialog(
											CommonFrame.getParentFrame(this
													.getComponent()),
											PublicResource
													.getString("aliasnameinputdialog.aliasexist"),
											"warning", 2);
							continue;
						}
						bm.setAliasName(result);
						bm.setDisplayLabel(result);
						((BookmarkView) getComponent()).getConnectTree().updateUI();
						
						isOk=true;
					}else
					{
					    isOk=true;
					}
				} else {
                    if(result==null)  //����
                    	isOk=true;
                    else if(result.trim().equals("")) //Ϊ�մ���������
                    {
						JOptionPane
						.showMessageDialog(
								CommonFrame.getParentFrame(this
										.getComponent()),
								PublicResource
										.getString("aliasnameinputdialog.noinput"),
								"warning", 2);
				        continue;                   	
                    }
				}
			}
		}

	}

}
