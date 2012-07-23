/* 
 * $Log: TristateCheckBox.java,v $
 * Revision 1.1  2010/08/03 14:09:24  xiaolin
 * �ϲ�dbmanage��sqleditor
 *
 * Revision 1.1.2.1  2008/05/25 03:51:33  benq
 * �ع�����ṹ���������ƽ���srcĿ¼
 *
 * Revision 1.1.2.3  2008/04/27 16:04:54  benq
 * ��������frame�޸���ɣ��ȴ����
 *
 * Revision 1.1.2.2  2008/04/13 02:31:51  benq
 * ���˴�����ع����������ļ����߲���
 *
 * Revision 1.1.2.1  2008/04/12 16:44:44  kenny liu
 * CoolSQL�ع���ȥ����������Ŀ���õĹؼ��֣������˰���
 *
 * Revision 1.1.2.1  2008/03/22 16:56:55  kenny liu
 * 1�������˽ű��������ܣ�����ȱmysql�Ľű��������ܣ�һ��Ҫ���ϡ�
 * 2��������ui�ϵ���ʾ��
 *
 * Revision 1.1  2005/08/30 15:18:00  pan
 * Rename to TristateCheckBox
 *
 * Revision 1.3  2005/08/19 11:37:52  pan
 * write comments
 *
 * Revision 1.2  2005/08/15 09:17:06  pan
 * extending JCheckBox with three state SELECTED, NOT_SELECTED and HALF_SELECTED
 *
 * Revision 1.1  2005/08/09 14:51:45  pan
 * Three state check box
 */

package com.cattsoft.coolsql.pub.component.selectabletree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ActionMapUIResource;

/**
 * An implementation of a three state check box
 * 
 * 	SELECTED, NOT_SELECTED, HALF_SELECTED(gray)
 */
public class TristateCheckBox extends JCheckBox {
	private static final long serialVersionUID = 1L;
	/**
	 * State definition
	 */
	public static final int NOT_SELECTED = 0;
	public static final int SELECTED = 1;
	public static final int HALF_SELECTED = 2;
	
	private final TristateDecorator model;
	
	/**
	 * Creates a check box with text and icon, specifies the initial state
	 * 
	 * @param text
	 *            the text of check box
	 * @param icon
	 *            the icon image to display
	 * @param initial
	 *            <code>NOT_SELECTED</code>, <code>SELECTED</code> or
	 *            <code>HALF_SELECTED</code>.
	 */
	public TristateCheckBox(String text, Icon icon, int initial){
		super(text, icon);
		
		super.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				grabFocus();
				model.nextState();
			}
		});
		
	    ActionMap map = new ActionMapUIResource();
		map.put("pressed", new AbstractAction() { 

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				grabFocus();
				model.nextState();
			}
		});
		
	    map.put("released", null); //$NON-NLS-1$
	    SwingUtilities.replaceUIActionMap(this, map);
	    
	    model = new TristateDecorator(getModel());
	    setModel(model);
	    setState(initial);
	}
	
    /**
	 * Creates a check box with text and specifies the initial state
	 * 
	 * @param text
	 *            the text of the check box.
	 * @param initial
	 *            <code>NOT_SELECTED</code>, <code>SELECTED</code> or
	 *            <code>HALF_SELECTED</code>.
	 */
	public TristateCheckBox(String text, int initial) {
	    this(text, null, initial);
	}
	
	/**
     * Creates an initially unselected check box with text.
     *
     * @param text the text of the check box.
     */
	public TristateCheckBox(String text) {
	    this(text, NOT_SELECTED);
	}
	
	/**
	 * Creates an initially unselected check box button with no text, no icon.
	 */
	public TristateCheckBox() {
	    this(""); //$NON-NLS-1$
	}
	  
	/**
	 * Set the new state to either SELECTED, NOT_SELECTED or HALF_SELECTED.
	 * 
	 * @param state 
	 * 	if state is undefined, don't change. 
	 */
	public void setState(int state) { 
		model.setState(state); 
	}
	
	/**
	 * 
	 * @return the current state.
	 *  which is determined by the selection status of the model.
	 */
	public int getState() {
		return model.getState();
	}
	
	public void setChecked(boolean checked) {
		if (checked)
			setState(SELECTED);
		else
			setState(NOT_SELECTED);
	}

	/**
	 * HALF_SELECTED is treated as not checked.
	 * 
	 * @return 
	 */
	public boolean getChecked() {
		return model.getState() == SELECTED;
	}
	
	/*
	 * @see java.awt.Component#addMouseListener(java.awt.event.MouseListener)
	 */
	public void addMouseListener(MouseListener l) {		
	}
	
	/**
	 * The Decorator Pattern
	 * 
	 * @author pan
	 */
	private class TristateDecorator implements ButtonModel {
	    
		private final ButtonModel other;
		
	    private TristateDecorator(ButtonModel other) {
			this.other = other;
		}
	    
	    private void setState(int state) {
			if (state == NOT_SELECTED) {
				other.setArmed(false);
				setPressed(false);
				setSelected(false);
			} else if (state == SELECTED) {
				other.setArmed(false);
				setPressed(false);
				setSelected(true);
			} else if (state == HALF_SELECTED) {
				other.setArmed(true);
				setPressed(true);
				setSelected(true);
			}
		}
	    
	    /**
		 * The current state is embedded in the selection / armed state of the
		 * model.
		 * 
		 * @return the SELECTED state when the checkbox is selected but not
		 * armed, NOT_SELECTED state when the checkbox is selected and armed (gray)
		 * and NOT_SELECTED when the checkbox is deselected.
		 */
	    private int getState() {
			if (isSelected() && !isArmed()) {
				return SELECTED;
			} else if (isSelected() && isArmed()) {
				return HALF_SELECTED;
			} else {
				return NOT_SELECTED;
			}
		}
	    
	    /** 
	     * This method rotates among NOT_SELECTED, SELECTED and HALF_SELECTED. 
	     */
	    private void nextState() {
			int current = getState();
			
			if (current == NOT_SELECTED) {
				setState(SELECTED);
			} else if (current == SELECTED) {
				setState(HALF_SELECTED);
			} else if (current == HALF_SELECTED) {
				setState(NOT_SELECTED);
			}
		}
	    
	    /*
	     * @see javax.swing.ButtonModel#setArmed(boolean)
	     */
	    public void setArmed(boolean b) {
		}
	    
	    /*
	     * @see javax.swing.ButtonModel#setEnabled(boolean)
	     */
	    public void setEnabled(boolean b) {
			setFocusable(b);
			other.setEnabled(b);
		}

	    /*
	     * @see javax.swing.ButtonModel#isArmed()
	     */
	    public boolean isArmed() {
			return other.isArmed();
		}
	    
	    /*
	     * @see javax.swing.ButtonModel#isSelected()
	     */
	    public boolean isSelected() {
			return other.isSelected();
		}
	    
	    /*
	     * @see javax.swing.ButtonModel#isEnabled()
	     */
	    public boolean isEnabled() {
			return other.isEnabled();
		}
	    
	    /*
	     * @see javax.swing.ButtonModel#isPressed()
	     */
	    public boolean isPressed() {
			return other.isPressed();
		}
	    
	    /*
	     * @see javax.swing.ButtonModel#isRollover()
	     */
	    public boolean isRollover() {
			return other.isRollover();
		}
	    
	    /*
	     * @see javax.swing.ButtonModel#setSelected(boolean)
	     */
	    public void setSelected(boolean b) {
			other.setSelected(b);
		}
	    
	    /*
	     * @see javax.swing.ButtonModel#setPressed(boolean)
	     */
	    public void setPressed(boolean b) {
			other.setPressed(b);
		}
	    
	    /*
	     * @see javax.swing.ButtonModel#setRollover(boolean)
	     */
	    public void setRollover(boolean b) {
			other.setRollover(b);
		}
	    
	    /*
	     * @see javax.swing.ButtonModel#setMnemonic(int)
	     */
	    public void setMnemonic(int key) {
			other.setMnemonic(key);
		}

	    /*
	     * @see javax.swing.ButtonModel#getMnemonic()
	     */
	    public int getMnemonic() {
			return other.getMnemonic();
		}
	    
	    /*
	     * @see javax.swing.ButtonModel#setActionCommand(java.lang.String)
	     */
	    public void setActionCommand(String s) {
			other.setActionCommand(s);
		}

	    /*
	     * @see javax.swing.ButtonModel#getActionCommand()
	     */
	    public String getActionCommand() {
			return other.getActionCommand();
		}
	    
	    /*
	     * @see javax.swing.ButtonModel#setGroup(javax.swing.ButtonGroup)
	     */
	    public void setGroup(ButtonGroup group) {
			other.setGroup(group);
		}

	    /*
	     * @see javax.swing.ButtonModel#addActionListener(java.awt.event.ActionListener)
	     */
	    public void addActionListener(ActionListener l) {
			other.addActionListener(l);
		}
	    
	    /*
	     * @see javax.swing.ButtonModel#removeActionListener(java.awt.event.ActionListener)
	     */
	    public void removeActionListener(ActionListener l) {
			other.removeActionListener(l);
		}
	    
	    /*
	     * @see java.awt.ItemSelectable#addItemListener(java.awt.event.ItemListener)
	     */
	    public void addItemListener(ItemListener l) {
			other.addItemListener(l);
		}
	    
	    /*
	     * @see java.awt.ItemSelectable#removeItemListener(java.awt.event.ItemListener)
	     */
	    public void removeItemListener(ItemListener l) {
			other.removeItemListener(l);
		}
	    
	    /*
	     * @see javax.swing.ButtonModel#addChangeListener(javax.swing.event.ChangeListener)
	     */
	    public void addChangeListener(ChangeListener l) {
			other.addChangeListener(l);
		}

	    /*
	     * @see javax.swing.ButtonModel#removeChangeListener(javax.swing.event.ChangeListener)
	     */
	    public void removeChangeListener(ChangeListener l) {
			other.removeChangeListener(l);
		}
	    
	    /*
	     * @see java.awt.ItemSelectable#getSelectedObjects()
	     */
	    public Object[] getSelectedObjects() {
			return other.getSelectedObjects();
		}
	}
}

