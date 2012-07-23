/**
 * 
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.cattsoft.coolsql.pub.parse.PublicResource;

/**
 * @author kenny liu
 * 
 * 2007-11-6 create
 */
public class CancelButton extends RenderButton {

	private Window window;// ��Ҫ���رյĴ��ڶ���
	private boolean isPrompt;// �Ƿ���йر���ʾ(true:��ʾ,false:����ʾ)
	public CancelButton(Window window, boolean isPrompt) {
		super();
		this.window = window;
		this.isPrompt = isPrompt;
		init();
	}
	/**
	 * Ĭ������£��رմ���ʱ��������Ϣ��ʾ
	 * 
	 * @param window
	 *            --���رյĴ���
	 */
	public CancelButton(Window window) {
		this(window, false);
	}
	private void init() {
		setText(PublicResource.getString("propertyframe.button.quit"));
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isPrompt) {
					int r = JOptionPane
							.showConfirmDialog(
									window,
									PublicResource
											.getString("system.closedialog.forcecloseconfirm"),
									"please confirm",
									JOptionPane.OK_CANCEL_OPTION);
					if (r == JOptionPane.OK_OPTION)
						window.dispose();
					else
						return;
				} else
					window.dispose();
			}
		});

	}
}
