/*
 * �������� 2006-12-6
 *
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;

import com.cattsoft.coolsql.pub.parse.PublicResource;

/**
 * @author liu_xlin ��ʾ��������,ͬʱҲ�ܹ���ѡ��
 */
public class DateLabel extends JLabel {

	private Date date = null;

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private static SimpleDateFormat dayFormat = new SimpleDateFormat("d");
	
	private String describe = PublicResource.getUtilString("date.currentdescribe");

	/**
	 * �ñ�ǩ�Ƿ���ʾ��������
	 */
	private boolean isDayDisplay = false;

	public DateLabel(Date date) {
		this(date, true);
	}

	/**
	 * 
	 * @param date
	 * @param isTodayDay --true ��ʾ����Ϣ false:��ʾ��ǰ������Ϣ
	 */
	public DateLabel(Date date, boolean isDayDisplay) {
		super();
		this.date = date;
		this.isDayDisplay = isDayDisplay;
		this.setPreferredSize(new Dimension(40, 20));
	     
		updateLabel();
		if(isDayDisplay)
			setHorizontalAlignment(JLabel.CENTER);
		else
			setHorizontalAlignment(JLabel.LEFT);
	}

	/**
	 * @return ���� date��
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            Ҫ���õ� date��
	 */
	public void setDate(Date date) {
		this.date = date;

		updateLabel();

	}

	/**
	 * ���±�ǩ����Ϣ��ʾ
	 *
	 */
	private void updateLabel() {
		if (date == null)
			return;
		if (!isDayDisplay) {
			String dateStr = dateFormat.format(date);
			this.setText("<html><body>" + describe + "<font color=red>"
					+ dateStr + "</font></body></html>");
		}else
		{
			this.setText(dayFormat.format(date));
		}
		setToolTipText(dateFormat.format(date));
	}
}
