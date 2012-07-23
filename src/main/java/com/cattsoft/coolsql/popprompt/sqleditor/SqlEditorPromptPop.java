/**
 * 
 */
package com.cattsoft.coolsql.popprompt.sqleditor;

import java.awt.BorderLayout;
import java.util.List;

import com.jidesoft.popup.JidePopup;

/**
 * Prompt pop window for sql editor, prompting information matched with user input.
 * @author ��Т��(kenny liu)
 *
 * 2008-4-5 create
 */
public class SqlEditorPromptPop extends JidePopup {

	private static final long serialVersionUID = 1L;

	private PopList dataList;
	public SqlEditorPromptPop()
	{
		this(null);
	}
	public SqlEditorPromptPop(List<Object> data)
	{
		setMovable(false);
        setResizable(true);
        getContentPane().setLayout(new BorderLayout());
        
        dataList=new PopList(data);
        getContentPane().add(dataList,BorderLayout.CENTER);
	}
	
//	public void setAllMatchedItem(List<Object> data,Object userInput)
//	{
//		dataList.setListData(data);
//		dataList.selectMatchValue((String)userInput);
//	}
}
