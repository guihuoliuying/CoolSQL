/**
 * 
 */
package com.cattsoft.coolsql.popprompt;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.cattsoft.coolsql.pub.display.JEditTextArea;
import com.cattsoft.coolsql.view.sqleditor.EditorPanel;
import com.jidesoft.hints.IntelliHints;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.DelegateAction;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-4-5 create
 */
public abstract class AbstractIntelliHints implements IntelliHints{

    /**
     * The key of a client property. If a component has intellihints registered, you can use this client
     * property to get the IntelliHints instance.
     */
    public static final String CLIENT_PROPERTY_INTELLI_HINTS = "INTELLI_HINTS"; //NOI18N

    private JidePopup _popup;
    private JEditTextArea _textComponent;

    private boolean _followCaret = false;

    // we use this flag to workaround the bug that setText() will trigger the hint popup.
    private boolean _keyTyped = false;

    // Specifies whether the hints popup should be displayed automatically.
    // Default is true for backward compatibility.
    private boolean _autoPopup = true;

    /**
     * Creates an IntelliHints object for a given JTextComponent.
     *
     * @param textComponent the text component.
     */
    public AbstractIntelliHints(JEditTextArea textComponent) {
        _textComponent = textComponent;
        getTextComponent().putClientProperty(CLIENT_PROPERTY_INTELLI_HINTS, this);

        _popup = createPopup();

        getTextComponent().getDocument().addDocumentListener(documentListener);
        getTextComponent().addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                if (KeyEvent.VK_ESCAPE != e.getKeyCode()) {
                    setKeyTyped(true);
                }
            }
        });
        getTextComponent().addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
            }

            public void focusLost(FocusEvent e) {
                Container topLevelAncestor = _popup.getTopLevelAncestor();
                if (topLevelAncestor == null) {
                    return;
                }
                Component oppositeComponent = e.getOppositeComponent();
                if (topLevelAncestor == oppositeComponent || topLevelAncestor.isAncestorOf(oppositeComponent)) {
                    return;
                }
                hideHintsPopup();
            }
        });

        DelegateAction.replaceAction(getTextComponent(), JComponent.WHEN_FOCUSED, getShowHintsKeyStroke(), showAction);

        KeyStroke[] keyStrokes = getDelegateKeyStrokes();
        for (int i = 0; i < keyStrokes.length; i++) {
            KeyStroke keyStroke = keyStrokes[i];
            DelegateAction.replaceAction(getTextComponent(), JComponent.WHEN_FOCUSED, keyStroke, new LazyDelegateAction(keyStroke));
        }
    }

    protected JidePopup createPopup() {
        JidePopup popup = new JidePopup();
        popup.setLayout(new BorderLayout());
        popup.setResizable(true);
        popup.setPopupBorder(BorderFactory.createLineBorder(UIDefaultsLookup.getColor("controlDkShadow"), 1));
        popup.setMovable(false);
        popup.add(createHintsComponent());
        popup.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                DelegateAction.restoreAction(getTextComponent(), JComponent.WHEN_FOCUSED, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), hideAction);
                DelegateAction.restoreAction(getTextComponent(), JComponent.WHEN_FOCUSED, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), acceptAction);
            }

            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
        popup.setTransient(true);
        return popup;
    }

    public JEditTextArea getTextComponent() {
        return _textComponent;
    }


    /**
     * After user has selected a item in the hints popup, this method will update JTextComponent accordingly
     * to accept the hint.
     * <p/>
     * For JTextArea, the default implementation will insert the hint into current caret position.
     * For JTextField, by default it will replace the whole content with the item user selected. Subclass can
     * always choose to override it to accept the hint in a different way. For example, {@link com.jidesoft.hints.FileIntelliHints}
     * will append the selected item at the end of the existing text in order to complete a full file path.
     */
    public void acceptHint(Object selected) {
        if (selected == null)
            return;

        if (getTextComponent() instanceof EditorPanel) {
            int pos = getTextComponent().getCaretPosition();
            String text = getTextComponent().getText();
            int start = text.lastIndexOf("\n", pos - 1);
            String remain = pos == -1 ? "" : text.substring(pos);
            text = text.substring(0, start + 1);
            text += selected;
            pos = text.length();
            text += remain;
            getTextComponent().setText(text);
            getTextComponent().setCaretPosition(pos);
        }
    }


    protected void showHintsPopup() {
    	showHintsPopup(true);
    }
    /**
     * Shows the hints popup which contains the hints.
     * It will call {@link #updateHints(Object)}. Only if it returns true,
     * the popup will be shown.
     * @param isRequestFocus --indicate whether checking the text component'focus. 
     */
    protected void showHintsPopup(boolean isRequestFocus) {
        if (getTextComponent().isEnabled() && (!isRequestFocus||getTextComponent().hasFocus()) && updateHints(getContext())) {
            DelegateAction.replaceAction(getTextComponent(), JComponent.WHEN_FOCUSED, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), hideAction);
            DelegateAction.replaceAction(getTextComponent(), JComponent.WHEN_FOCUSED, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), acceptAction, true);

            Point tmp = getTextComponent().getCursorLocation();
    		SwingUtilities.convertPointToScreen(tmp,getTextComponent());
            _popup.setOwner(getTextComponent());
            _popup.showPopup(tmp.x,tmp.y);
        }
        else {
            _popup.hidePopup();
        }
    }
    /**
     * Gets the context for hints. The context is the information that IntelliHints needs
     * in order to generate a list of  hints. For example, for code-completion, the context is
     * current word the cursor is on. for file completion, the context is the full string starting from
     * the file system root.
     * <p>We provide a default context in AbstractIntelliHints. If it's a JTextArea,
     * the context will be the string at the caret line from line beginning to the caret position. If it's a JTextField,
     * the context will be whatever string in the text field. Subclass can always
     * override it to return the context that is appropriate.
     *
     * @return the context.
     */
    protected Object getContext() {
        if (getTextComponent() instanceof EditorPanel) {
            int pos = getTextComponent().getCaretPosition();
            if (pos == 0) {
                return "";
            }
            else {
                EditorPanel pane=(EditorPanel)getTextComponent();
                String text=pane.getWordOnOffset(pane.getCaretPosition());
                return text;
            }
        }
        else {
            return getTextComponent().getText();
        }
    }

    /**
     * Hides the hints popup.
     */
    protected void hideHintsPopup() {
        if (_popup != null) {
            _popup.hidePopup();
        }
        setKeyTyped(false);
    }

    /**
     * Enables or disables the hints popup.
     *
     * @param enabled true to enable the hints popup. Otherwise false.
     */
    public void setHintsEnabled(boolean enabled) {
        if (!enabled) {
            // disable show hint temporarily
            getTextComponent().getDocument().removeDocumentListener(documentListener);
        }
        else {
            // enable show hint again
            getTextComponent().getDocument().addDocumentListener(documentListener);
        }

    }

    /**
     * Checks if the hints popup is visible.
     *
     * @return true if it's visible. Otherwise, false.
     */
    public boolean isHintsPopupVisible() {
        return _popup != null && _popup.isPopupVisible();
    }
    /**
     * Should the hints popup follows the caret.
     *
     * @return true if the popup shows up right below the caret. False if the popup always shows
     *         at the bottom-left corner (or top-left if there isn't enough on the bottom of the screen)
     *         of the JTextComponent.
     */
    public boolean isFollowCaret() {
        return _followCaret;
    }

    /**
     * Sets the position of the hints popup. If followCaret is true, the popup
     * shows up right below the caret. Otherwise, it will stay at the bottom-left corner
     * (or top-left if there isn't enough on the bottom of the screen) of JTextComponent.
     *
     * @param followCaret true or false.
     */
    public void setFollowCaret(boolean followCaret) {
        _followCaret = followCaret;
    }

    /**
     * Returns whether the hints popup is automatically displayed. Default is
     * true
     *
     * @return true if the popup should be automatically displayed. False will
     *         never show it automatically and then need the user to manually activate
     *         it via the getShowHintsKeyStroke() key binding.
     */
    public boolean isAutoPopup() {
        return _autoPopup;
    }

    /**
     * Sets whether the popup should be displayed automatically. If autoPopup
     * is true then is the popup automatically displayed whenever updateHints()
     * return true. If autoPopup is false it's not automatically displayed and
     * will need the user to activate the key binding defined by
     * getShowHintsKeyStroke().
     *
     * @param autoPopup true or false
     */
    public void setAutoPopup(boolean autoPopup) {
        this._autoPopup = autoPopup;
    }

    /**
     * Gets the delegate keystrokes.
     * <p/>
     * When hint popup is visible, the keyboard focus never leaves the text component.
     * However the hint popup usually contains a component that user will try to use navigation key to
     * select an item. For example, use UP and DOWN key to navigate the list.
     * Those keystrokes, if the popup is visible, will be delegated
     * to the the component that returns from {@link #getDelegateComponent()}.
     *
     * @return an array of keystrokes that will be delegate to {@link #getDelegateComponent()} when hint popup is shown.
     */
    abstract protected KeyStroke[] getDelegateKeyStrokes();

    /**
     * Gets the delegate component in the hint popup.
     *
     * @return the component that will receive the keystrokes that are delegated to hint popup.
     */
    abstract protected JComponent getDelegateComponent();

    /**
     * Gets the keystroke that will trigger the hint popup. Usually the hints popup
     * will be shown automatically when user types. Only when the hint popup is hidden
     * accidentally, this keystroke will show the popup again.
     * <p/>
     * By default, it's the DOWN key for JTextField and CTRL+SPACE for JTextArea.
     *
     * @return the keystroek that will trigger the hint popup.
     */
    protected KeyStroke getShowHintsKeyStroke() {
            
    	return KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, KeyEvent.ALT_MASK);
    }

    private DelegateAction acceptAction = new LazyAction() {
    	
    	private static final long serialVersionUID = 1L;
        @Override
        public boolean delegateActionPerformed(ActionEvent e) {
            JComponent tf = (JComponent) e.getSource();
            AbstractIntelliHints hints = (AbstractIntelliHints) tf.getClientProperty(CLIENT_PROPERTY_INTELLI_HINTS);
            if (hints != null) {
            	try
            	{
	                if (hints.getSelectedHint() != null) {
	                    hints.setHintsEnabled(false);
	                    hints.acceptHint(hints.getSelectedHint());
	                    hints.setHintsEnabled(true);
	                    return true;
	                }
	                else if (getTextComponent().getRootPane() != null) {
	                    JButton button = getTextComponent().getRootPane().getDefaultButton();
	                    if (button != null) {
	                        button.doClick();
	                        return true;
	                    }
	                }
            	}finally
            	{
            		hints.hideHintsPopup();
            	}
                
            }
            return false;
        }
    };
    public static DelegateAction getDefaultShowAction()
    {
    	return showAction;
    }
    private static DelegateAction showAction = new LazyAction() {

    	private static final long serialVersionUID = 1L;

		@Override
        public boolean delegateActionPerformed(ActionEvent e) {
            JComponent tf = (JComponent) e.getSource();
            AbstractIntelliHints hints = (AbstractIntelliHints) tf.getClientProperty(CLIENT_PROPERTY_INTELLI_HINTS);
            if (hints != null && tf.isEnabled() && !hints.isHintsPopupVisible()) {
                hints.showHintsPopup();
                return true;
            }
            return false;
        }
    };

    private DelegateAction hideAction = new LazyAction() {
    	
    	private static final long serialVersionUID = 1L;
        @Override
        public boolean isEnabled() {
            return _textComponent.isEnabled() && isHintsPopupVisible();
        }

        @Override
        public boolean delegateActionPerformed(ActionEvent e) {
            if (isEnabled()) {
                hideHintsPopup();
                return true;
            }
            return false;
        }
    };
    
    private DocumentListener documentListener = new DocumentListener() {
        private Timer timer = new Timer(200, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isKeyTyped()) {
                    if (isHintsPopupVisible() || isAutoPopup()) {
                        showHintsPopup();
                    }
                    setKeyTyped(false);
                }
            }
        });

        public void insertUpdate(DocumentEvent e) {
            startTimer();
        }

        public void removeUpdate(DocumentEvent e) {
            startTimer();
        }

        public void changedUpdate(DocumentEvent e) {
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
    };

    private boolean isKeyTyped() {
        return _keyTyped;
    }

    private void setKeyTyped(boolean keyTyped) {
        _keyTyped = keyTyped;
    }
    /**
     * This interface is symbol represents a Intelligent hinter action.
     * @author(kenny liu)
     *
     * 2008-4-8 create
     */
    @SuppressWarnings("serial")
	public abstract static class LazyAction extends DelegateAction
    {
    	
    }
    private static class LazyDelegateAction extends LazyAction {
    	
    	private static final long serialVersionUID = 1L;
    	
        private KeyStroke _keyStroke;

        public LazyDelegateAction(KeyStroke keyStroke) {
            _keyStroke = keyStroke;
        }

        @Override
        public boolean delegateActionPerformed(ActionEvent e) {
            JComponent tf = (JComponent) e.getSource();
            AbstractIntelliHints hints = (AbstractIntelliHints) tf.getClientProperty(CLIENT_PROPERTY_INTELLI_HINTS);
            if (hints != null && tf.isEnabled()) {
                if (hints.isHintsPopupVisible()) {
                    Object key = hints.getDelegateComponent().getInputMap().get(_keyStroke);
                    key = key == null ? hints.getTextComponent().getInputMap(JComponent.WHEN_FOCUSED).get(_keyStroke) : key;
                    if (key != null) {
                        Object action = hints.getDelegateComponent().getActionMap().get(key);
                        if (action instanceof Action) {
                            ((Action) action).actionPerformed(new ActionEvent(hints.getDelegateComponent(), 0, "" + key));
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }
}
