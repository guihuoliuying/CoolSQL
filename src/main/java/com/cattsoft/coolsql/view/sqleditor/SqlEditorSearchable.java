/*
 * @(#)TextComponentSearchable.java 10/11/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */
package com.cattsoft.coolsql.view.sqleditor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.cattsoft.coolsql.pub.display.JEditTextArea;
import com.jidesoft.swing.Searchable;
import com.jidesoft.swing.event.SearchableEvent;

/**
 * <code>TextComponentSearchable</code> is an concrete implementation of {@link Searchable} that
 * enables the search function in JEditTextArea. <p>It's very simple to use it. Assuming you have a
 * JEditTextArea, all you need to do is to call
 * <code><pre>
 * JEditTextArea textComponent = ....;
 * TextComponentSearchable searchable = new TextComponentSearchable(textComponent);
 * </pre></code>
 * Now the JEditTextArea will have the search function.
 * <p/>
 * There is very little customization you need to do to ListSearchable. The only thing you might
 * need is when the element in the JEditTextArea needs a special conversion to convert to string.
 * If so, you can overide convertElementToString() to provide you own algorithm to do the
 * conversion.
 * <code><pre>
 * JEditTextArea textComponent = ....;
 * TextComponentSearchable searchable = new ListSearchable(textComponent) {
 *      protected String convertElementToString(Object object) {
 *          ...
 *      }
 * <p/>
 *      protected boolean isActivateKey(KeyEvent e) { // change to a different activation key
 *          return (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_F &&
 * (KeyEvent.CTRL_MASK & e.getModifiers()) != 0);
 *      }
 * };
 * </pre></code>
 * <p/>
 * Additional customization can be done on the base Searchable class such as background and
 * foreground color, keystrokes, case sensitivity. TextComponentSearchable also has a special
 * attribute called highlightColor. You can change it using {@link #setHighlightColor(java.awt.Color)}.
 * <p/>
 * Due to the special case of JEditTextArea, the searching doesn't support wild card '*' or '?' as
 * in other Searchables. The other difference is JEditTextArea will keep the highlights after
 * search popup hides. If you want to hide the highlights, just press ESC again (the first ESC will
 * hide popup; the second ESC will hide all highlights if any).
 */
public class SqlEditorSearchable extends Searchable implements DocumentListener, PropertyChangeListener {
    private final static Color DEFAULT_HIGHLIGHT_COLOR = new Color(204, 204, 255);
    private Color _highlightColor = null;
    private int _selectedIndex = -1;
    
//    private HighlighCache _highlighCache;

    public SqlEditorSearchable(JEditTextArea textComponent) {
        super(textComponent);
//        _highlighCache = new HighlighCache();
        installHighlightsRemover();
        setHighlightColor(DEFAULT_HIGHLIGHT_COLOR);
    }

    /**
     * Uninstalls the handler for ESC key to remove all highlights
     */
    public void uninstallHighlightsRemover() {
        _component.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
    }

    /**
     * Installs the handler for ESC key to remove all highlights
     */
    public void installHighlightsRemover() {
    	
        AbstractAction highlightRemover = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
                removeAllHighlights();
            }
        };
        _component.registerKeyboardAction(highlightRemover, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_FOCUSED);
    }

    @Override
    public void installListeners() {
        super.installListeners();
        if (_component instanceof JEditTextArea) {
            ((JEditTextArea) _component).getDocument().addDocumentListener(this);
            _component.addPropertyChangeListener("document", this);
        }
    }

    @Override
    public void uninstallListeners() {
        super.uninstallListeners();
        if (_component instanceof JEditTextArea) {
            ((JEditTextArea) _component).getDocument().removeDocumentListener(this);
            _component.removePropertyChangeListener("document", this);
        }
    }

    @Override
    protected void setSelectedIndex(int index, boolean incremental) {
        if (_component instanceof JEditTextArea) {
            if (index == -1) {
                removeAllHighlights();
                _selectedIndex = -1;
                return;
            }

            if (!incremental) {
                removeAllHighlights();
            }

            String text = getSearchingText();
            try {
                addHighlight(index, text, incremental);
            }
            catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds highlight to text component at specified index and text.
     *
     * @param index       the index of the text to be highlighted
     * @param text        the text to be highlighted
     * @param incremental if this is an incremental adding highlight
     *
     * @throws BadLocationException
     */
    protected void addHighlight(int index, String text, boolean incremental) throws BadLocationException {
        if (_component instanceof JEditTextArea) {
            JEditTextArea textComponent = ((JEditTextArea) _component);
           
            textComponent.select(index,index+text.length());
            _selectedIndex = index;
            if (!incremental) {
                scrollTextVisible(textComponent, index, text.length());
            }
        }
    }

    private void scrollTextVisible(JEditTextArea textComponent, int index, int length) {
    	int line = textComponent.getLineOfOffset(index);
		int lineStart =textComponent.getLineStartOffset(line);
		int offset = Math.max(0, Math.min(textComponent.getLineLength(line) - 1,
				index - lineStart));
		textComponent.scrollTo(line, offset);
        // scroll highlight visible
//        if (index != -1) {
//            // Scroll the component if needed so that the composed text
//            // becomes visible.
//            try {
//                Rectangle begin = textComponent.modelToView(index);
//                if (begin == null) {
//                    return;
//                }
//                Rectangle end = textComponent.modelToView(index + length);
//                if (end == null) {
//                    return;
//                }
//                Rectangle bounds = _component.getVisibleRect();
//                if (begin.x <= bounds.width) { // make sure if scroll back to the beginning as long as selected rect is visible
//                    begin.width = end.x;
//                    begin.x = 0;
//                }
//                else {
//                    begin.width = end.x - begin.x;
//                }
//                textComponent.scrollRectToVisible(begin);
//            }
//            catch (BadLocationException ble) {
//            }
//        }
    }

    /**
     * Removes all highlights from the text component.
     */
    protected void removeAllHighlights() {

    }

    @Override
    protected int getSelectedIndex() {
        if (_component instanceof JEditTextArea) {
            return _selectedIndex;
        }
        return 0;
    }

    @Override
    protected Object getElementAt(int index) {
        String text = getSearchingText();
        if (text != null) {
            if (_component instanceof JEditTextArea) {
                int endIndex = index + text.length();
                int elementCount = getElementCount();
                if (endIndex > elementCount) {
                    endIndex = getElementCount();
                }
                try {
                    return ((JEditTextArea) _component).getDocument().getText(index, endIndex - index + 1);
                }
                catch (BadLocationException e) {
                    return null;
                }
            }
        }
        return "";
    }

    @Override
    protected int getElementCount() {
        if (_component instanceof JEditTextArea) {
            return ((JEditTextArea) _component).getDocument().getLength();
        }
        return 0;
    }

    /**
     * Converts the element in JEditTextArea to string. The returned value will be the
     * <code>toString()</code> of whatever element that returned from <code>list.getModel().getElementAt(i)</code>.
     *
     * @param object
     *
     * @return the string representing the element in the JEditTextArea.
     */
    @Override
    protected String convertElementToString(Object object) {
        if (object != null) {
            return object.toString();
        }
        else {
            return "";
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        hidePopup();
        _text = null;
        if (evt.getOldValue() instanceof Document) {
            ((Document) evt.getNewValue()).removeDocumentListener(this);
        }
        if (evt.getNewValue() instanceof Document) {
            ((Document) evt.getNewValue()).addDocumentListener(this);
        }
        fireSearchableEvent(new SearchableEvent(this, SearchableEvent.SEARCHABLE_MODEL_CHANGE));
    }

    public void insertUpdate(DocumentEvent e) {
        hidePopup();
        _text = null;
//        fireSearchableEvent(new SearchableEvent(this, SearchableEvent.SEARCHABLE_MODEL_CHANGE));
    }

    public void removeUpdate(DocumentEvent e) {
        hidePopup();
        _text = null;
//        fireSearchableEvent(new SearchableEvent(this, SearchableEvent.SEARCHABLE_MODEL_CHANGE));
    }

    public void changedUpdate(DocumentEvent e) {
        hidePopup();
        _text = null;
//        fireSearchableEvent(new SearchableEvent(this, SearchableEvent.SEARCHABLE_MODEL_CHANGE));
    }

    @Override
    protected boolean isActivateKey(KeyEvent e) {
        if (_component instanceof JEditTextArea && ((JEditTextArea) _component).isEditable()) {
            return (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_F && (KeyEvent.CTRL_MASK & e.getModifiers()) != 0);
        }
        else {
            return super.isActivateKey(e);
        }
    }

    /**
     * Gets the highlight color.
     *
     * @return the highlight color.
     */
    public Color getHighlightColor() {
        if (_highlightColor != null) {
            return _highlightColor;
        }
        else {
            return DEFAULT_HIGHLIGHT_COLOR;
        }
    }

    /**
     * Changes the highlight color.
     *
     * @param highlightColor
     */
    public void setHighlightColor(Color highlightColor) {
        _highlightColor = highlightColor;
//        _highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(_highlightColor);
    }

    @Override
    public int findLast(String s) {
        if (_component instanceof JEditTextArea) {
            String text = getDocumentText();
            if (isCaseSensitive()) {
                return text.lastIndexOf(s);
            }
            else {
                return text.toLowerCase().lastIndexOf(s.toLowerCase());
            }
        }
        else {
            return super.findLast(s);
        }
    }

    private String _text = null;

    /**
     * Gets the text from Document.
     *
     * @return the text of this JEditTextArea. It used Document to get the text.
     */
    private String getDocumentText() {
        if (_text == null) {
            Document document = ((JEditTextArea) _component).getDocument();
            try {
                String text = document.getText(0, document.getLength());
                _text = text;
            }
            catch (BadLocationException e) {
                return "";
            }
        }
        return _text;
    }

    @Override
    public int findFirst(String s) {
        if (_component instanceof JEditTextArea) {
            String text = getDocumentText();
            if (isCaseSensitive()) {
                return text.indexOf(s);
            }
            else {
                return text.toLowerCase().indexOf(s.toLowerCase());
            }
        }
        else {
            return super.findFirst(s);
        }
    }

    @Override
    public int findFromCursor(String s) {
        if (isReverseOrder()) {
            return reverseFindFromCursor(s);
        }

        if (_component instanceof JEditTextArea) {
            String text = getDocumentText();
            if (!isCaseSensitive()) {
                text = text.toLowerCase();
            }
            String str = isCaseSensitive() ? s : s.toLowerCase();
            int selectedIndex = (getCursor() != -1 ? getCursor() : getSelectedIndex());
            if (selectedIndex < 0)
                selectedIndex = 0;
            int count = getElementCount();
            if (count == 0)
                return s.length() > 0 ? -1 : 0;

            // find from cursor
            int found = text.indexOf(str, selectedIndex);

            // if not found, start over from the beginning
            if (found == -1) {
                found = text.indexOf(str, 0);
                if (found >= selectedIndex) {
                    found = -1;
                }
            }

            return found;
        }
        else {
            return super.findFromCursor(s);
        }
    }

    @Override
    public int reverseFindFromCursor(String s) {
        if (!isReverseOrder()) {
            return findFromCursor(s);
        }

        if (_component instanceof JEditTextArea) {
            String text = getDocumentText();
            if (!isCaseSensitive()) {
                text = text.toLowerCase();
            }
            String str = isCaseSensitive() ? s : s.toLowerCase();
            int selectedIndex = (getCursor() != -1 ? getCursor() : getSelectedIndex());
            if (selectedIndex < 0)
                selectedIndex = 0;
            int count = getElementCount();
            if (count == 0)
                return s.length() > 0 ? -1 : 0;

            // find from cursor
            int found = text.lastIndexOf(str, selectedIndex);

            // if not found, start over from the end
            if (found == -1) {
                found = text.lastIndexOf(str, text.length() - 1);
                if (found <= selectedIndex) {
                    found = -1;
                }
            }

            return found;
        }
        else {
            return super.findFromCursor(s);
        }
    }

    @Override
    public int findNext(String s) {
        if (_component instanceof JEditTextArea) {
            String text = getDocumentText();
            if (!isCaseSensitive()) {
                text = text.toLowerCase();
            }
            String str = isCaseSensitive() ? s : s.toLowerCase();
            int selectedIndex = (getCursor() != -1 ? getCursor() : getSelectedIndex());
            if (selectedIndex < 0)
                selectedIndex = 0;
            int count = getElementCount();
            if (count == 0)
                return s.length() > 0 ? -1 : 0;

            // find from cursor
            int found = text.indexOf(str, selectedIndex + 1);

            // if not found, start over from the beginning
            if (found == -1 && isRepeats()) {
                found = text.indexOf(str, 0);
                if (found >= selectedIndex) {
                    found = -1;
                }
            }

            return found;
        }
        else {
            return super.findNext(s);
        }
    }

    @Override
    public int findPrevious(String s) {
        if (_component instanceof JEditTextArea) {
            String text = getDocumentText();
            if (!isCaseSensitive()) {
                text = text.toLowerCase();
            }
            String str = isCaseSensitive() ? s : s.toLowerCase();
            int selectedIndex = (getCursor() != -1 ? getCursor() : getSelectedIndex());
            if (selectedIndex < 0)
                selectedIndex = 0;
            int count = getElementCount();
            if (count == 0)
                return s.length() > 0 ? -1 : 0;

            // find from cursor
            int found = text.lastIndexOf(str, selectedIndex - 1);

            // if not found, start over from the beginning
            if (found == -1 && isRepeats()) {
                found = text.lastIndexOf(str, count - 1);
                if (found <= selectedIndex) {
                    found = -1;
                }
            }

            return found;
        }
        else {
            return super.findPrevious(s);
        }
    }
    public void hidePopup() {
        super.hidePopup();
        _selectedIndex = -1;
    }
}
