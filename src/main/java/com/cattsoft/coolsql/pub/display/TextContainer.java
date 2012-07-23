package com.cattsoft.coolsql.pub.display;

public interface TextContainer
{
	String getText();
	String getSelectedText();
	void setSelectedText(String aText);
	void setText(String aText);
	void setCaretPosition(int pos);
	int getCaretPosition();
	int getSelectionStart();
	int getSelectionEnd();
	void select(int start, int end);
	void setEditable(boolean flag);
}
