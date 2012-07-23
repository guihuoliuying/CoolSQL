/**
 * CoolSQL 2009 copyright.
 * You must confirm to the GPL license if want to reuse and redistribute the codes.
 * 2009-5-1
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author Kenny Liu
 *
 * 2009-5-1 create
 */
@SuppressWarnings("serial")
public class TextExpandableDialog extends ExpandableDialog {

	public TextExpandableDialog() throws HeadlessException {
		super();
	}

	public TextExpandableDialog(Dialog owner, boolean modal)
			throws HeadlessException {
		super(owner, modal);
	}

	public TextExpandableDialog(Dialog owner, String title, boolean modal,
			GraphicsConfiguration gc) throws HeadlessException {
		super(owner, title, modal, gc);
	}

	public TextExpandableDialog(Dialog owner, String title, boolean modal)
			throws HeadlessException {
		super(owner, title, modal);
	}

	public TextExpandableDialog(Dialog owner, String title)
			throws HeadlessException {
		super(owner, title);
	}

	public TextExpandableDialog(Frame owner, boolean modal)
			throws HeadlessException {
		super(owner, modal);
	}

	public TextExpandableDialog(Frame owner, String title, boolean modal)
			throws HeadlessException {
		super(owner, title, modal);
	}

	public TextExpandableDialog(Frame owner, String title)
			throws HeadlessException {
		super(owner, title);
	}

	public TextExpandableDialog(Frame owner) throws HeadlessException {
		super(owner);
	}

	/* (non-Javadoc)
	 * @see com.coolsql.pub.component.ExpandableDialog#createDetailsPanel()
	 */
	@Override
	public JComponent createDetailsPanel() {
		textArea = new JTextArea(detailedText == null ? "" : detailedText);
        textArea.setRows(10);
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.add(new JScrollPane(textArea));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        return panel;
	}
	/**
	 * Set the message which will be displayed in the textarea.
	 */
	public void setDetailedText(String msg) {
		this.detailedText = msg;
		if (textArea != null) {
			textArea.setText(msg);
		}
	}

	private JTextArea textArea = null;
	private String detailedText;
}
