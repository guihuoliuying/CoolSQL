package com.cattsoft.coolsql.view.sqleditor;


public class StringPointer
{

    public StringPointer(String s)
    {
        offset = 0;
        mark = 0;
        value = s.toCharArray();
    }

    public char getNext()
    {
        char retVal = offset >= value.length ? '\0' : value[offset];
        offset++;
        return retVal;
    }

    public char peek()
    {
        char retVal = offset >= value.length ? '\0' : value[offset];
        return retVal;
    }

    public void mark()
    {
        mark = offset;
    }

    public void reset()
    {
        offset = mark;
    }

    public void back()
    {
        if(offset > 0)
            offset--;
    }

    public int getOffset()
    {
        return offset;
    }

    public boolean isDone()
    {
        return offset >= value.length;
    }

    public int getLength()
    {
        return value.length;
    }

    char value[];
    int offset;
    int mark;
}
