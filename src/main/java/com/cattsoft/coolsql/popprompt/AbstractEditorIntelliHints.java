/**
 * 
 */
package com.cattsoft.coolsql.popprompt;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.cattsoft.coolsql.popprompt.sqleditor.PopList;
import com.cattsoft.coolsql.pub.display.JEditTextArea;
import com.jidesoft.swing.JideScrollPane;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-4-6 create
 */
public abstract class AbstractEditorIntelliHints extends AbstractIntelliHints {
    private PopList _PopList;
    protected KeyStroke[] _keyStrokes;
    private JideScrollPane _scroll;

    /**
     * Creates a Completion for JTextComponent
     *
     * @param textComponent
     */
    public AbstractEditorIntelliHints(JEditTextArea textComponent) {
        super(textComponent);
    }

    public JComponent createHintsComponent() {
        JPanel panel = new JPanel(new BorderLayout());

        _PopList = createList();
        _scroll = new JideScrollPane(getList());
        getList().setFocusable(false);

        _scroll.setHorizontalScrollBarPolicy(JideScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        _scroll.setBorder(BorderFactory.createEmptyBorder());
        _scroll.getVerticalScrollBar().setFocusable(false);
        _scroll.getHorizontalScrollBar().setFocusable(false);

        panel.add(_scroll, BorderLayout.CENTER);
        
        _PopList.setRequestFocusEnabled(false);
        _PopList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	if(e.getClickCount()!=2) //double click can be accepted only.
            		return;
                
            	try {
	                setHintsEnabled(false);
	                acceptHint(getSelectedHint());
	                setHintsEnabled(true);
            	} finally {
            		hideHintsPopup();
            	}
            }
        });
        return panel;
    }

    /**
     * Creates the list to display the hints. By default, we create a JList using the code below.
     * <code><pre>
     * return new JList() {
     *     public int getVisibleRowCount() {
     *         int size = getModel().getSize();
     *         return size < super.getVisibleRowCount() ? size : super.getVisibleRowCount();
     *     }
     * <p/>
     *     public Dimension getPreferredScrollableViewportSize() {
     *         if (getModel().getSize() == 0) {
     *             return new Dimension(0, 0);
     *         }
     *         else {
     *             return super.getPreferredScrollableViewportSize();
     *         }
     *     }
     * };
     * </pre></code>
     *
     * @return the list.
     */
    protected PopList createList() {
    	
    	return new PopList((List<Object>)null);
    }
    @Override
    protected void hideHintsPopup() {
    	super.hideHintsPopup();
    	if(_PopList!=null)
    	{
    		_PopList.clearList();
    	}
    }
    /**
     * Gets the list.
     *
     * @return the list.
     */
    protected PopList getList() {
        return _PopList;
    }

    /**
     * Sets the list data.
     *
     * @param objects
     */
    protected void setListData(Object[] objects) {
        resetSelection();
        getList().setListData(objects);

        // update the view so that isViewSizeSet flag in JViewport is reset to false
        if (_scroll != null) {
            _scroll.setViewportView(getList());
        }

    }

    /**
     * Sets the list data.
     *
     * @param objects
     */
    protected void setListData(List<?> objects) {
        resetSelection();
        getList().setListData(objects);
        // update the view so that isViewSizeSet flag in JViewport is reset to false
        if (_scroll != null) {
            _scroll.setViewportView(getList());
        }
    }

    private void resetSelection() {
        getList().resetSelection(); 
    }

    public Object getSelectedHint() {
        return getList().getSelectedValue();
    }

    @Override
    public JComponent getDelegateComponent() {
        return getList().getCurrentList();
    }

    /**
     * Gets the delegate keystrokes. Since we know the hints popup is a JList, we return eight
     * keystrokes so that they can be delegate to the JList. Those keystrokes are
     * DOWN, UP, PAGE_DOWN, PAGE_UP, HOME and END.
     *
     * @return the keystokes that will be delegated to the JList when hints popup is visible.
     */
    @Override
    public KeyStroke[] getDelegateKeyStrokes() {
        if (_keyStrokes == null) {
            _keyStrokes = new KeyStroke[8];
            _keyStrokes[0] = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
            _keyStrokes[1] = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
            _keyStrokes[2] = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0);
            _keyStrokes[3] = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0);
            _keyStrokes[4] = KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0);
            _keyStrokes[5] = KeyStroke.getKeyStroke(KeyEvent.VK_END, 0);
            _keyStrokes[6] = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
            _keyStrokes[7] = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
        }
        return _keyStrokes;
    }
}
