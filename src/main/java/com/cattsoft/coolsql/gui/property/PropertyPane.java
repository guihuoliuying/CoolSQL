/*
 * �������� 2006-9-12
 */
package com.cattsoft.coolsql.gui.property;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.system.ISetting;
import com.jidesoft.swing.MultilineLabel;

/**
 * Property panel.
 * @author liu_xlin
 */
@SuppressWarnings("serial")
public abstract class PropertyPane extends JPanel
		implements
			PropertyInterface,
			ActionListener,
			ItemListener,
			ContainerListener,
			DocumentListener,
			ChangeListener,
			ListSelectionListener,
			TreeSelectionListener,
			TableModelListener {

	private MultilineLabel prompt = null;

	private boolean isChanged=true;
	
	private boolean ignoreEvents;
	
	private Map<String,Object> propertyMap=new HashMap<String,Object>();
	
    /**
     * Set of components that we're listening to models of, so we can look
     * up the component from the model as needed
     */
    private Set<Component> listenedTo = new HashSet<Component>();
    
    private CustomComponentListener extListener;
	public PropertyPane() {
		super();
		
		double[][] model = new double[][]{{TableLayout.FILL},
				{
			40,
			TableLayout.FILL}};
		this.setLayout(new TableLayout(model));
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		JPanel top = new JPanel(new BorderLayout());
		top.setBorder(border);
		top.setBackground(Color.WHITE);
		
		prompt = new MultilineLabel("");
		prompt.setFont(new Font(PublicResource.getString("propertyPane.font"),
				Font.PLAIN, prompt.getFont().getSize() + 3));
		top.add(prompt, BorderLayout.CENTER);
		
		this.add(top, "0, 0");
		this.add(initContent(), "0, 1");

		if (isNeedApply()&&isNeedListenToChild()) {
			initContent().addContainerListener(new ChildComponentListener());
		}
		extListener=createCustomComponentListener();
	}
	protected boolean isNeedListenToChild() {
		return false;
	}
	protected void putClientProperty(String propertyValue,JComponent component)
	{
		if(component!=null)
		{
			component.putClientProperty(PropertyInterface.PROPERTY_NAME, propertyValue);
		}
	}
	/**
	 * Indicated whether the value of some of property panel' components has changed.
	 * this method will not make sense if the return value of {@link #isNeedListenToChild()} method is false.
	 */
	protected boolean isChanged()
	{
		return isChanged;
	}
	public void reset()
	{
		propertyMap.clear();
		setChanged(false);
	}
	/**
	 * ���ñ���
	 * 
	 * @param txt
	 */
	public void setLabel(String txt) {
		prompt.setText(txt);
	}
	protected void fireItemChanged(Object e) {
		boolean isReceived=false;
        if (!ignoreEvents) {
            setIgnoreEvents(true);
            try {
                if (e instanceof EventObject && ((EventObject) e).getSource() instanceof Component) {
                    userInputReceived((Component) ((EventObject) e).getSource(), e);
                    isReceived=true;
                } else if (e instanceof TreeSelectionEvent) {
                    TreeSelectionModel mdl = (TreeSelectionModel) ((TreeSelectionEvent) e).getSource();
                    for (Iterator<Component> i = listenedTo.iterator(); i.hasNext();) {
                        Object o = i.next();
                        if (o instanceof JTree && ((JTree) o).getSelectionModel() == mdl) {
                            userInputReceived((Component) o, e);
                            isReceived=true;
                            break;
                        }
                    }
                } else if (e instanceof DocumentEvent) {
                    Document document = ((DocumentEvent) e).getDocument();
                    for (Iterator<Component> i = listenedTo.iterator(); i.hasNext();) {
                        Object o = i.next();
                        if (o instanceof JTextComponent && ((JTextComponent) o).getDocument() == document) {
                            userInputReceived((Component) o, e);
                            isReceived=true;
                            break;
                        }
                    }
                } else if (e instanceof ListSelectionEvent) {
                    ListSelectionModel model = (ListSelectionModel) ((ListSelectionEvent) e).getSource();
                    for (Iterator<Component> i = listenedTo.iterator(); i.hasNext();) {
                        Object o = i.next();
                        if (o instanceof JList && ((JList) o).getSelectionModel() == model) {
                            userInputReceived((Component) o, e);
                            isReceived=true;
                            break;
                        } else if (o instanceof JTable && ((JTable) o).getSelectionModel() == model) {
                            userInputReceived((Component) o, e);
                            isReceived=true;
                            break;
                        }
                    }
                } else {
                    userInputReceived(null, e);
                }
            } finally {
                setIgnoreEvents(false);
            }
        }
        if(isReceived)
        	setChanged(true);
	}
	protected void userInputReceived(Component source, Object event) {
		if(!(source instanceof JComponent))
			return;
		JComponent s=(JComponent)source;
        String mapKey =(String)s.getClientProperty(PropertyInterface.PROPERTY_NAME);
        // debug: System.err.println("MaybeUpdateMap " + mapKey + " from " + comp);
        if (mapKey != null) {
            Object value = valueFrom(s);
            propertyMap.put(mapKey, value);
        }
	}
	protected void setChanged(boolean isChanged)
	{
		if(!this.isNeedApply())
		{
			return;
		}
		boolean oldValue=this.isChanged;
		this.isChanged=isChanged;
		if(oldValue!=isChanged)
		{
			PropertyFrame pf = (PropertyFrame) GUIUtil.getUpParent(this,
					PropertyFrame.class);
			if (pf != null && pf.getCurrentComponent() == this) {
				pf.showApplyButton(isChanged);
			}
		}
		
	}
	/**
	 * Initializing the content panel.
	 */
	public abstract JPanel initContent();

	/**
	 * Property panel can specify a setting object. The propertymanager will use the system setting object 
	 * if user don't specify a setting.
	 * @return
	 */
	protected ISetting getSetting() {
		return null;
	}
	
	protected CustomComponentListener createCustomComponentListener()
	{
		return null;
	}
	
	protected boolean accept(Component jc) {
		if (extListener != null && extListener.accept(jc)) {
            return true;
        }
		if (!(jc instanceof JComponent)) {
			return false;
		}
		if (jc instanceof TableCellEditor || jc instanceof TreeCellEditor
				|| SwingUtilities.getAncestorOfClass(JTable.class, jc) != null
				|| SwingUtilities.getAncestorOfClass(JTree.class, jc) != null
				|| SwingUtilities.getAncestorOfClass(JList.class, jc) != null) {
			// Don't listen to cell editors, we can end up listening to them
			// multiple times, and the tree/table model will give us the event
			// we need
			return false;
		}
		if(isProbablyAContainer(jc))
			return true;
		if(((JComponent)jc).getClientProperty(PropertyInterface.PROPERTY_NAME)==null)
			return false;
		return  jc instanceof JList
				|| jc instanceof JComboBox || jc instanceof JTree
				|| jc instanceof JToggleButton
				|| // covers toggle, radio, checkbox
				jc instanceof JTextComponent || jc instanceof JColorChooser
				|| jc instanceof JSpinner || jc instanceof JSlider;
	}
	/**
	 * Return true if the given component is likely to be a container such the
	 * each component within the container should be be considered as a user
	 * input.
	 * 
	 * @param c
	 * @return true if the component children should have this listener added.
	 */
	protected boolean isProbablyAContainer(Component c) {
		boolean isSwing = isSwingClass(c);
		boolean result = extListener != null ? extListener.isContainer(c) : false;
		if (isSwing) {
			result = c instanceof JPanel || c instanceof JSplitPane
					|| c instanceof JToolBar || c instanceof JViewport
					|| c instanceof JScrollPane || c instanceof JFrame
					|| c instanceof JRootPane || c instanceof Window
					|| c instanceof Frame || c instanceof Dialog
					|| c instanceof JTabbedPane || c instanceof JInternalFrame
					|| c instanceof JDesktopPane || c instanceof JLayeredPane;
		} else {
			
			result =!( c instanceof JList||c instanceof JComboBox||c instanceof JTree
					||c instanceof JToggleButton||c instanceof JTextComponent||c instanceof JColorChooser
					||c instanceof JSpinner||c instanceof JSlider);
		}
		return result;
	}
	/**
	 * Return true if the given component is likely to be a swing primitive or a
	 * subclass. The default implmentation here just checks for the package of
	 * the component to be "javax.swing" If you use subclasses of swing
	 * components, you will need to override this method to get proper behavior.
	 * 
	 * @param c
	 * @return true if the component should be examined more closely (see
	 *         isProbablyAContainer)
	 */
	protected boolean isSwingClass(Component c) {
		String packageName = c.getClass().getPackage().getName();
		boolean swing = packageName.equals("javax.swing"); // NOI18N
		return swing;
	}
	protected void attachTo(Component jc) {
        if (extListener != null && extListener.accept (jc)) {
            extListener.startListeningTo(jc);
            listenedTo.add (jc);
            return;
        }
		if (isProbablyAContainer(jc)) {
			attachToHierarchyOf((Container) jc);
		} else if (jc instanceof JList) {
			listenedTo.add(jc);
			((JList) jc).addListSelectionListener(this);
		} else if (jc instanceof JComboBox) {
			((JComboBox) jc).addActionListener(this);
		} else if (jc instanceof JTree) {
			listenedTo.add(jc);
			((JTree) jc).getSelectionModel().addTreeSelectionListener(this);
		} else if (jc instanceof JToggleButton) {
			((AbstractButton) jc).addItemListener(this);
		} else if (jc instanceof JTextComponent) {
			listenedTo.add(jc);
			((JTextComponent) jc).getDocument().addDocumentListener(this);
		} else if (jc instanceof JColorChooser) {
			listenedTo.add(jc);
			((JColorChooser) jc).getSelectionModel().addChangeListener(this);
		} else if (jc instanceof JSpinner) {
			((JSpinner) jc).addChangeListener(this);
		} else if (jc instanceof JSlider) {
			((JSlider) jc).addChangeListener(this);
		} else if (jc instanceof JTable) {
			listenedTo.add(jc);
			((JTable) jc).getSelectionModel().addListSelectionListener(this);
		} else
			throw new RuntimeException("Unknown component type:"
					+ jc.getClass().getName());

	}

	protected void detachFrom(Component jc) {
		listenedTo.remove(jc);
		if (isProbablyAContainer(jc)) {
			detachFromHierarchyOf((Container) jc);
		} else if (jc instanceof JList) {
			((JList) jc).removeListSelectionListener(this);
		} else if (jc instanceof JComboBox) {
			((JComboBox) jc).removeActionListener(this);
		} else if (jc instanceof JTree) {
			((JTree) jc).getSelectionModel().removeTreeSelectionListener(this);
		} else if (jc instanceof JToggleButton) {
			((AbstractButton) jc).removeActionListener(this);
		} else if (jc instanceof JTextComponent) {
			((JTextComponent) jc).getDocument().removeDocumentListener(this);
		} else if (jc instanceof JColorChooser) {
			((JColorChooser) jc).getSelectionModel().removeChangeListener(this);
		} else if (jc instanceof JSpinner) {
			((JSpinner) jc).removeChangeListener(this);
		} else if (jc instanceof JSlider) {
			((JSlider) jc).removeChangeListener(this);
		} else if (jc instanceof JTable) {
			((JTable) jc).getSelectionModel().removeListSelectionListener(this);
		} else
			throw new RuntimeException("Unknown component type:"
					+ jc.getClass().getName());
	}
	
	private void detachFromHierarchyOf(Container container) {
		container.removeContainerListener(this);
		Component[] components = container.getComponents();
		for (int i = 0; i < components.length; i++) {
			detachFrom(components[i]); // Will callback recursively any nested
										// JPanels
		}
	}

	private void attachToHierarchyOf(Container container) {
		container.addContainerListener(this);
		Component[] components = container.getComponents();
		for (int i = 0; i < components.length; i++) {
			if(accept(components[i]))
				attachTo(components[i]); // Will recursively add any child
										// components in
		}
	}
	protected class ChildComponentListener implements ContainerListener {

		public void componentAdded(ContainerEvent e) {
			if (accept(e.getChild())) {
				attachTo(e.getChild());
			}
		}

		public void componentRemoved(ContainerEvent e) {
			if (accept(e.getChild())) {
				detachFrom(e.getChild());
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		fireItemChanged(e);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent e) {
		fireItemChanged(e);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ContainerListener#componentAdded(java.awt.event.ContainerEvent)
	 */
	public void componentAdded(ContainerEvent e) {
		fireItemChanged(e);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ContainerListener#componentRemoved(java.awt.event.ContainerEvent)
	 */
	public void componentRemoved(ContainerEvent e) {
		fireItemChanged(e);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	public void changedUpdate(DocumentEvent e) {
		fireItemChanged(e);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	public void insertUpdate(DocumentEvent e) {
		fireItemChanged(e);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	public void removeUpdate(DocumentEvent e) {
		fireItemChanged(e);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		fireItemChanged(e);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		fireItemChanged(e);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent e) {
		fireItemChanged(e);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	public void tableChanged(TableModelEvent e) {
		fireItemChanged(e);
	}
	protected Object valueFrom(Component comp) {
        if (extListener != null && extListener.accept(comp)) {
            return extListener.valueFor(comp);
        }
		
		if (comp instanceof JRadioButton || comp instanceof JCheckBox
				|| comp instanceof JToggleButton) {
			return ((AbstractButton) comp).getModel().isSelected()
					? Boolean.TRUE
					: Boolean.FALSE;
		} else if (comp instanceof JTree) {
			TreePath path = ((JTree) comp).getSelectionPath();
			if (path != null) {
				return path.getLastPathComponent();
			}
		} else if (comp instanceof JList) {
			Object[] o = ((JList) comp).getSelectedValues();
			if (o != null) {
				if (o.length > 1) {
					return o;
				} else if (o.length == 1) {
					return o[0];
				}
			}
		} else if (comp instanceof JTextComponent) {
			return ((JTextComponent) comp).getText();
		} else if (comp instanceof JComboBox) {
			return ((JComboBox) comp).getSelectedItem();
		} else if (comp instanceof JColorChooser) {
			return ((JColorChooser) comp).getSelectionModel()
					.getSelectedColor();
		} else if (comp instanceof JSpinner) {
			return ((JSpinner) comp).getValue();
		} else if (comp instanceof JSlider) {
			return new Integer(((JSlider) comp).getValue());
		}

		return null;
	}
    protected void setIgnoreEvents(boolean val) {
        ignoreEvents = val;
    }
    protected boolean isIgnoreEvents()
    {
    	return ignoreEvents;
    }
	/**
	 * @return the propertyMap
	 */
	public Map<String, Object> getPropertyMap() {
		return this.propertyMap;
	}
	
	public void doOnClose()throws UnifyException{} 
}
