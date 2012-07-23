/**
 * CoolSQL 2009 copyright.
 * You must confirm to the GPL license if want to reuse and redistribute the codes.
 * 2009-4-17
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.java.balloontip.BalloonTip;
import net.java.balloontip.positioners.BasicBalloonTipPositioner;
import net.java.balloontip.styles.BalloonTipStyle;
import net.java.balloontip.styles.ModernBalloonStyle;

import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.jidesoft.swing.DefaultOverlayable;
import com.jidesoft.swing.OverlayComboBox;

/**
 * @author Kenny Liu
 *
 * 2009-4-17 create
 */
@SuppressWarnings("serial")
public class ValidateComboBox extends DefaultOverlayable {
	
	public static final String PROPERTY_ISLEGAL = "isLegal";
	
	private boolean isLegal = true;
	
	private IValidator validator = null;
	
	/**
	 * The actual editor.
	 */
	private OverlayComboBox comboBox;
	private JLabel icon;
	
	/**
	 * The balloon tip used to display error information in a suitable style.
	 */
	private BalloonTip balloonTip;
	/**
	 * Error information about editor.
	 */
	private String errorInfo;
	/**
	 * 
	 */
	public ValidateComboBox() {
		super();
		comboBox = new OverlayComboBox();
		init();
	}

	/**
	 * @param items
	 */
	public ValidateComboBox(Object[] items) {
		super();
		comboBox = new OverlayComboBox(items);
		init();
	}

	/**
	 * @param arg0
	 */
	public ValidateComboBox(Vector<?> arg0) {
		super();
		comboBox = new OverlayComboBox(arg0);
		init();
	}
	protected void init() {
		
		setActualComponent(comboBox);
		
		Component editorComp = comboBox.getEditor().getEditorComponent();
		if(editorComp instanceof JTextField) {
			JTextField tf = (JTextField) editorComp;
			GUIUtil.installDefaultTextPopMenu(tf);
			tf.getDocument().addDocumentListener(new DocumentListener() {

				private Timer timer = new Timer(300, new ActionListener(){
					
		            public void actionPerformed(ActionEvent e) {
		            	
		            	toValidate();
		            }
		        });
				
				public void changedUpdate(DocumentEvent e) {
				}

				public void insertUpdate(DocumentEvent e) {
//					System.out.println("insert:" + box.isPopupVisible());
					if (!comboBox.isPopupVisible()) {
						startTimer();
					}
				}

				public void removeUpdate(DocumentEvent e) {
//					System.out.println("remove"+ box.isPopupVisible());
					if (!comboBox.isPopupVisible()) {
						startTimer();
					}
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
		}
		
		comboBox.addItemListener(new ItemListener(){

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					toValidate();
				}
			}
			
		});
		
		icon = new JLabel(ValidateTextEditor.ICON_CORRECT);
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
		
		this.addPropertyChangeListener(PROPERTY_ISLEGAL, new PropertyChangeListener()
		{

			public void propertyChange(PropertyChangeEvent evt) {
				if (isLegal()) {
					icon.setIcon(ValidateTextEditor.ICON_CORRECT);
				} else {
					icon.setIcon(ValidateTextEditor.ICON_ERROR);
				}
			}
			
		}
		);
	}
	public boolean toValidate() {
		
		errorInfo = null;
    	if (validator != null) {
			setLegal(validator.validate(comboBox));
		} else {
			icon.setIcon(null);
		}
		
		return isLegal();
	}
	
	public String getTextValue() {
		if (comboBox.isEditable()) {
			return comboBox.getEditor().getItem().toString();
		} else {
			Object v = comboBox.getSelectedItem();
			if (v == null) {
				return null;
			} else {
				return v.toString();
			}
		}
	}
	
	/**
	 * Proxy method for comboBox
	 */
	public void addItem(Object anObject) {
		comboBox.addItem(anObject);
	}
	public void removeItem(Object anObject) {
		comboBox.removeItem(anObject);
	}
	public Object getSelectedItem() {
		return comboBox.getSelectedItem();
	}
	public void setSelectedItem(Object anObject) {
		comboBox.setSelectedItem(anObject);
	}
	public void setSelectedIndex(int anIndex) {
		comboBox.setSelectedIndex(anIndex);
	}
	public void setEditable(boolean b) {
		comboBox.setEditable(b);
	}
	public boolean isEditable() {
		return comboBox.isEditable();
	}
	
	public void addComboBoxPropertyListener(PropertyChangeListener l) {
		comboBox.addPropertyChangeListener(l);
	}
	public void removeComboBoxPropertyListener(PropertyChangeListener l) {
		comboBox.removePropertyChangeListener(l);
	}
	
	@Override
	public void setToolTipText(String tooltip) {
		comboBox.setToolTipText(tooltip);
	}
	@Override
	public void setEnabled(boolean e) {
		super.setEnabled(e);
		comboBox.setEnabled(e);
	}
	@Override
	public boolean isEnabled() {
		return comboBox.isEnabled();
	}
	@Override
	public void requestFocus() {
		comboBox.requestFocus();
	}
	@Override
	public boolean requestFocusInWindow() {
		return comboBox.requestFocusInWindow();
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

	/**
	 * @return the validator
	 */
	public IValidator getValidator() {
		return this.validator;
	}

	/**
	 * @param validator the validator to set
	 */
	public void setValidator(IValidator validator) {
		this.validator = validator;
	}

	/**
	 * @return the errorInfo
	 */
	public String getErrorInfo() {
		return this.errorInfo;
	}

	/**
	 * @param errorInfo the errorInfo to set
	 */
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
}
