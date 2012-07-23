
package com.cattsoft.coolsql.pub.editable;

import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

public class DefaultUndoManager extends UndoManager
{

    DefaultUndoManager()
    {
        setLimit(300);
    }
    protected UndoableEdit editToBeUndone()
    {
        UndoableEdit ue = super.editToBeUndone();
        if(ue == null)
            return null;
        for(int i = edits.indexOf(ue); i >= 0;)
        {
            UndoableEdit edit = (UndoableEdit)edits.elementAt(i--);
            if(edit.isSignificant())
                if(edit instanceof javax.swing.text.AbstractDocument.DefaultDocumentEvent)
                {
                    if(javax.swing.event.DocumentEvent.EventType.CHANGE != ((javax.swing.text.AbstractDocument.DefaultDocumentEvent)edit).getType())
                        return edit;
                } else
                {
                    return edit;
                }
        }

        return null;
    }

    protected UndoableEdit editToBeRedone()
    {
        int count = edits.size();
        UndoableEdit ue = super.editToBeRedone();
        if(null == ue)
            return null;
        for(int i = edits.indexOf(ue); i < count;)
        {
            UndoableEdit edit = (UndoableEdit)edits.elementAt(i++);
            if(edit.isSignificant())
                if(edit instanceof javax.swing.text.AbstractDocument.DefaultDocumentEvent)
                {
                    if(javax.swing.event.DocumentEvent.EventType.CHANGE != ((javax.swing.text.AbstractDocument.DefaultDocumentEvent)edit).getType())
                        return edit;
                } else
                {
                    return edit;
                }
        }

        return null;
    }
}
