package com.cattsoft.coolsql.view.sqleditor;


public class Token
{

    public Token(char type, char value, int start)
    {
        this.type = type;
        this.value = (new Character(value)).toString();
        this.start = start;
        end = start + 1;
    }

    public Token(char type, String value, int start, int end)
    {
        this.type = type;
        this.value = value;
        this.start = start;
        this.end = end;
    }

    public int getEnd()
    {
        return end;
    }

    public int getStart()
    {
        return start;
    }

    public int getType()
    {
        return type;
    }

    public String getValue()
    {
        return value;
    }

    public void setEnd(int end)
    {
        this.end = end;
    }

    public void setStart(int start)
    {
        this.start = start;
    }

    public void setType(char type)
    {
        this.type = type;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return type + " ->" + value + "<- [" + start + ", " + end + "]";
    }

    public static final char SEPARATOR = 83;
    public static final char SYMBOL = 89;
    public static final char LITERAL = 76;
    public static final char IDENTIFIER = 73;
    public static final char COMMENT = 67;
    public static final char WHITESPACE = 87;
    public static final char NUMERIC = 78;
    public static final char END_OF_LINE = 69;
    private char type;
    private int start;
    private int end;
    private String value;
}
