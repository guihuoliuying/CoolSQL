/*
 * �������� 2006-5-31
 *
 */
package com.cattsoft.coolsql.view.mouseEventProcess;

import java.awt.event.MouseEvent;

import com.cattsoft.coolsql.pub.display.PopMenuMouseListener;
import com.cattsoft.coolsql.view.View;


/**
 * @author liu_xlin
 *��ͼ����Ҽ��¼�����
 */
public class PopupAction extends PopMenuMouseListener {
    private View view;
    
	public PopupAction(View view)
	{
	   	this.view=view;
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if(isPopupTrigger(e))
		{
			view.popupMenu(e.getX(),e.getY());			
		}
	}
	/**
	 * @return ���� view��
	 */
	public View getView() {
		return view;
	}
	/**
	 * @param view Ҫ���õ� view��
	 */
	public void setView(View view) {
		this.view = view;
	}
}
