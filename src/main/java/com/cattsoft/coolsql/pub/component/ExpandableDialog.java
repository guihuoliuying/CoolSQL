/**
 * CoolSQL 2009 copyright.
 * You must confirm to the GPL license if want to reuse and redistribute the codes.
 * 2009-5-1
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;
import com.jidesoft.dialog.StandardDialogPane;
import com.jidesoft.swing.JideBoxLayout;

/**
 * The expandable dialog.
 * @author Kenny Liu
 *
 * 2009-5-1 create
 */
@SuppressWarnings("serial")
public abstract class ExpandableDialog extends StandardDialog {

	private static final StringManager stringMgr = StringManagerFactory.getStringManager(ExpandableDialog.class);
	
	public ExpandableDialog(Dialog owner, boolean modal)
			throws HeadlessException {
		super(owner, modal);
	}

	public ExpandableDialog(Dialog owner, String title, boolean modal,
			GraphicsConfiguration gc) throws HeadlessException {
		super(owner, title, modal, gc);
	}

	public ExpandableDialog(Dialog owner, String title, boolean modal)
			throws HeadlessException {
		super(owner, title, modal);
	}

	public ExpandableDialog(Dialog owner, String title)
			throws HeadlessException {
		super(owner, title);
	}

	public ExpandableDialog(Frame owner, boolean modal)
			throws HeadlessException {
		super(owner, modal);
	}

	public ExpandableDialog(Frame owner, String title, boolean modal)
			throws HeadlessException {
		super(owner, title, modal);
	}

	public ExpandableDialog(Frame owner, String title) throws HeadlessException {
		super(owner, title);
	}

	public ExpandableDialog(Frame owner) throws HeadlessException {
		super(owner);
	}

    public ExpandableDialog() throws HeadlessException {
        super((Frame) null, "Expandable Message Dialog");
    }

    @Override
    public JComponent createBannerPanel() {
        return null;
    }

    public abstract JComponent createDetailsPanel();

    @Override
    protected StandardDialogPane createStandardDialogPane() {
        DefaultStandardDialogPane dialogPane = new DefaultStandardDialogPane() {
            @Override
            protected void layoutComponents(Component bannerPanel, Component contentPanel, ButtonPanel buttonPanel) {
                setLayout(new JideBoxLayout(this, BoxLayout.Y_AXIS));
                if (bannerPanel != null) {
                    add(bannerPanel);
                }
                if (contentPanel != null) {
                    add(contentPanel);
                }
                add(buttonPanel, JideBoxLayout.FIX);
                _detailsPanel = createDetailsPanel();
                add(_detailsPanel, JideBoxLayout.VARY);
                _detailsPanel.setVisible(false);
            }
        };
        return dialogPane;
    }

    @Override
    public JComponent createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

        summary = new JTextArea(summaryMsg == null ? "" : summaryMsg);
        summary.setMaximumSize(new Dimension(500, 50));
//        summary.setRows(4);
        summary.setEditable(false);
        summary.setBackground(panel.getBackground());

        JScrollPane pane = new JScrollPane(summary);
        pane.setBorder(null);
        panel.add(pane, BorderLayout.CENTER);
        return panel;
    }

    @Override
    public ButtonPanel createButtonPanel() {
        ButtonPanel buttonPanel = new ButtonPanel();
        JButton closeButton = new JButton();
        JButton detailButton = new JButton();
        detailButton.setMnemonic('D');
        closeButton.setName(OK);
        buttonPanel.addButton(closeButton, ButtonPanel.AFFIRMATIVE_BUTTON);
        buttonPanel.addButton(detailButton, ButtonPanel.OTHER_BUTTON);

        closeButton.setAction(new AbstractAction(PublicResource.getCloseButtonLabel()) {
            public void actionPerformed(ActionEvent e) {
                setDialogResult(RESULT_AFFIRMED);
                setVisible(false);
                dispose();
            }
        });

        final String expandLabel = stringMgr.getString("ExpandableDialog.detailbutton.expand.label");
        final String collapseLabel = stringMgr.getString("ExpandableDialog.detailbutton.collapse.label");
        detailButton.setAction(new AbstractAction(expandLabel) {
            public void actionPerformed(ActionEvent e) {
                if (_detailsPanel.isVisible()) {
                    _detailsPanel.setVisible(false);
                    putValue(Action.NAME, expandLabel);
                    pack();
                }
                else {
                    _detailsPanel.setVisible(true);
                    putValue(Action.NAME, collapseLabel);
                    pack();
                }
            }
        });

        setDefaultCancelAction(closeButton.getAction());
        setDefaultAction(closeButton.getAction());
        getRootPane().setDefaultButton(closeButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setSizeConstraint(ButtonPanel.NO_LESS_THAN); // since the checkbox is quite wide, we don't want all of them have the same size.
        return buttonPanel;
    }
    public void setSummaryMessage(String msg) {
    	this.summaryMsg = msg;
    	if (summary != null) {
    		summary.setText(msg);
    	}
    }
    
    private JComponent _detailsPanel;
    private JTextArea summary;
    
    private String summaryMsg;
}
