/**
 * CoolSQL 2009 copyright.
 * You must confirm to the GPL license if want to reuse and redistribute the codes.
 * 2009-4-17
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.metal.MetalTheme;

import net.java.balloontip.BalloonTip;
import net.java.balloontip.positioners.BasicBalloonTipPositioner;
import net.java.balloontip.styles.BalloonTipStyle;
import net.java.balloontip.styles.ModernBalloonStyle;

import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.jidesoft.swing.DefaultOverlayable;
import com.jidesoft.swing.OverlayTextField;
import com.jidesoft.swing.OverlayableIconsFactory;
import com.jidesoft.swing.OverlayableUtils;

/**
 * @author Kenny Liu
 *
 * 2009-4-17 create
 */
@SuppressWarnings("serial")
public class ValidateTextEditor extends DefaultOverlayable implements IValidate{

	private static final StringManager stringMgr = StringManagerFactory.getStringManager(ValidateTextEditor.class);
	
	private static final String ERRORINFO_EMPTY = stringMgr.getString("ValidateTextEditor.error.forbidempty");
	public static final String PROPERTY_ISLEGAL = "isLegal";
	
	public static final Icon ICON_CORRECT = OverlayableUtils.getPredefinedOverlayIcon(OverlayableIconsFactory.CORRECT);
	public static final Icon ICON_ERROR = OverlayableUtils.getPredefinedOverlayIcon(OverlayableIconsFactory.ERROR);
	
	private boolean isLegal = true;
	/**
	 * Determine whether checking empty value should be performed.
	 */
	private boolean emptyCheck = true;
	
	private IValidator validator = null;
	/**
	 * The actual editor.
	 */
	private OverlayTextField textEditor;
	private JLabel icon;
	/**
	 * Error information about editor.
	 */
	private String errorInfo;
	/**
	 * The balloon tip used to display error information in a suitable style.
	 */
	private BalloonTip balloonTip;
	/**
	 * 
	 */
	public ValidateTextEditor() {
		super();
		init(20);
	}

	/**
	 * @param length
	 */
	public ValidateTextEditor(int length) {
		super();
		init(length);
	}
	protected void init(int length) {
		/**
		 * Initialize the text editor.
		 */
		textEditor = new OverlayTextField(length);
		GUIUtil.installDefaultTextPopMenu(textEditor);
		MetalTheme theme = SubTheme.getCurrentTheme();
		if (theme != null && theme.getClass() == MyMetalTheme.class) {
			textEditor.setSelectionColor(DisplayPanel.getSelectionColor());
		}
		addPropertyChangeListener(PROPERTY_ISLEGAL, new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				if (isLegal()) {
					icon.setIcon(ICON_CORRECT);
				} else {
					icon.setIcon(ICON_ERROR);
				}
			}
			
		}
		);
		textEditor.getDocument().addDocumentListener(new DocumentListener() {

			private Timer timer = new Timer(300, new ActionListener(){
				
	            public void actionPerformed(ActionEvent e) {
	            	
	            	errorInfo = null;
	            	if (isEmptyCheck()) {
						if (!checkEmpty()) { //Return directly if the content of editor is empty.
							return;
						}
						
					}
	            	if (validator != null) {
						setLegal(validator.validate(ValidateTextEditor.this));
					}
	            }
	        });
			
			public void changedUpdate(DocumentEvent arg0) {
			}

			public void insertUpdate(DocumentEvent arg0) {
				startTimer();
			}

			public void removeUpdate(DocumentEvent arg0) {
				startTimer();
			}
			void startTimer() {
	            if (timer.isRunning()) {
	                timer.restart();
	            }
	            else {
	                timer.setRepeats(false);
	                timer.start();
	            }
		    }
		});
		
		/**
		 * Set editor as actual component
		 */
		setActualComponent(textEditor);
		
		icon = new JLabel(ICON_CORRECT);
		icon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		icon.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseEntered(MouseEvent e) {
				if (!isLegal()) {
					getBalloonTip().setText(errorInfo == null ? "" : errorInfo);
					getBalloonTip().setVisible(true);
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				
				if (balloonTip != null) {
					balloonTip.setVisible(false);
				}
			}
		});
		addOverlayComponent(icon, DefaultOverlayable.EAST);
		setOverlayLocationInsets(new Insets(0, 0, 0, 15));
	}
	
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	@Override
	public void setToolTipText(String tooltip) {
		textEditor.setToolTipText(tooltip);
	}
	@Override
	public void setEnabled(boolean isEnabled) {
		super.setEnabled(isEnabled);
		textEditor.setEnabled(isEnabled);
	}
	public void addEditorPropertyListener(PropertyChangeListener l) {
		textEditor.addPropertyChangeListener(l);
	}
	public void removeEditorPropertyListener(PropertyChangeListener l) {
		textEditor.removePropertyChangeListener(l);
	}
	@Override
	public void requestFocus() {
		textEditor.requestFocus();
	}
	@Override
	public boolean requestFocusInWindow() {
		return textEditor.requestFocusInWindow();
	}
	public void setEditable(boolean isEditable) {
		textEditor.setEditable(isEditable);
	}
	/**
	 * Retrieve the text of editor.
	 */
	public String getText() {
		
		return textEditor.getText();
	}
	public void setText(String t) {
		textEditor.setText(t);
	}
	private boolean checkEmpty() {
		if (textEditor.getText().trim().equals("")) {
			setLegal(false);
			errorInfo = ERRORINFO_EMPTY;
		} else {
			setLegal(true);
		}
		return isLegal();
	}
	/**
	 * @return the isLegal
	 */
	public boolean isLegal() {
		return this.isLegal;
	}

	/**
	 * @param isLegal the isLegal to set
	 */
	public void setLegal(boolean isLegal) {
		boolean oldValue = this.isLegal;
		this.isLegal = isLegal;
		firePropertyChange(PROPERTY_ISLEGAL, oldValue, this.isLegal);
	}

	/**
	 * @return the emptyCheck
	 */
	public boolean isEmptyCheck() {
		return this.emptyCheck;
	}

	/**
	 * @param emptyCheck the emptyCheck to set
	 */
	public void setEmptyCheck(boolean emptyCheck) {
		this.emptyCheck = emptyCheck;
	}
	/* (non-Javadoc)
	 * @see com.coolsql.pub.component.IValidate#setValidator(com.coolsql.pub.component.IValidator)
	 */
	public void setValidator(IValidator validator) {
		this.validator = validator;
	}
	/**
	 * Validate the content of editor according to validator.
	 * @return the result of validating. Return true if the content of editor is legal, otherwise return false.
	 */
	public boolean toValidate() {
		errorInfo = null;
    	if (isEmptyCheck()) {
			if (!checkEmpty()) { //Return directly if the content of editor is empty.
				return false;
			}
			
		}
    	if (validator != null) {
			setLegal(validator.validate(ValidateTextEditor.this));
		}
    	return isLegal();
	}
	private BalloonTip getBalloonTip() {
		if (balloonTip == null) {
			BalloonTipStyle style = new ModernBalloonStyle(0, 5, new Color(246, 197, 192, 220), Color.WHITE, Color.BLUE);
			balloonTip = new BalloonTip(icon, "",
					style, BalloonTip.Orientation.RIGHT_ABOVE, BalloonTip.AttachLocation.NORTHWEST, 0, 25, false);
			((BasicBalloonTipPositioner)balloonTip.getPositioner()).enableOffsetCorrection(true);
			((BasicBalloonTipPositioner)balloonTip.getPositioner()).enableOrientationCorrection(true);
		}
		return balloonTip;
	}
}
