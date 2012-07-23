package com.cattsoft.coolsql.pub.display;

import java.util.EventListener;
public interface TextChangeListener extends EventListener
{
	void textStatusChanged(boolean modified);
}
