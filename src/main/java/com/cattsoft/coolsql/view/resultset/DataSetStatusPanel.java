/*
 * �������� 2006-7-20
 *
 */
package com.cattsoft.coolsql.view.resultset;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.RoundedBalloonStyle;

import com.cattsoft.coolsql.pub.component.TextEditor;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.system.menubuild.IconResource;


/**
 * @author liu_xlin ���״̬չʾ���: ҳ��չʾ
 */
public class DataSetStatusPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private static final StringManager stringMgr=StringManagerFactory.getStringManager(DataSetStatusPanel.class);
	
	public static final Icon STATUS_ICON_DISPLAY=IconResource.getIconResource(
			stringMgr.getString("resultset.datasetstatus.statusicon.displayicon"));
	public static final Icon STATUS_ICON_EDITABLE=IconResource.getIconResource(
			stringMgr.getString("resultset.datasetstatus.statusicon.editableicon"));
	public static final Icon STATUS_ICON_DISABLE_EDITED=IconResource.getIconResource(
			stringMgr.getString("resultset.datasetstatus.statusicon.disableediticon"));
	public static final Map<Icon,String> stateDesc=new HashMap<Icon,String>();
	static{
		stateDesc.put(STATUS_ICON_DISPLAY, stringMgr.getString("resultset.datasetstatus.statusicon.displayicon.desc"));
		stateDesc.put(STATUS_ICON_EDITABLE, stringMgr.getString("resultset.datasetstatus.statusicon.modifyicon.desc"));
		stateDesc.put(STATUS_ICON_DISABLE_EDITED, stringMgr.getString("resultset.datasetstatus.statusicon.disableediticon.desc"));
	}
	
	private final static String costInfo=PublicResource.getString("resultView.tablestatus.costtime.info");
    private final static String costInfoUnit=PublicResource.getString("resultView.tablestatus.costtime.unit");
    
    private BalloonTip balloonTip ;
    
    private JLabel statusIcon; //Used to display current data set status by icon
    
	private TextEditor rowCount = null;  //��ʾ���������

	private TextEditor rowRange = null;  //��ʾ��ʾҳ�м�¼���ܵĽ���е�λ��
 
	private TextEditor costTime=null;  //��ʾִ��sql����Ҫ��ʱ��
	
	private TextEditor sql = null;  //��ʾsql���

	private int start = 0;  //��¼��ʾ����ʼλ��

	private int end = 0;  //��¼��ʾ�Ľ���λ��

	public DataSetStatusPanel() {
		this(null);
	}
	public DataSetStatusPanel(Icon initIcon) {
		super(new GridBagLayout());
		this.setPreferredSize(new Dimension(100, 24));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0D;
		gbc.gridx=0;
		
		if(initIcon!=null)
		{
			statusIcon=new JLabel(initIcon)
			{
				private static final long serialVersionUID = 1L;
				
			};
		}else
		{
			statusIcon=new JLabel(IconResource.getBlankIcon())
			{
				private static final long serialVersionUID = 1L;
				
			};
		}
		
		add(statusIcon, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.1D;
		rowCount = new TextEditor(9);
		rowCount.setToolTipText(PublicResource.getString("resultView.tablestatus.rowcount"));
		rowCount.setEditable(false);
		rowCount.setBorder(createComponentBorder());
		add(rowCount, gbc);
				
		gbc.gridx++;
		rowRange = new TextEditor(11);
		rowRange.setToolTipText(PublicResource.getString("resultView.tablestatus.range"));
		rowRange.setEditable(false);
		rowRange.setBorder(createComponentBorder());
		add(rowRange, gbc);
		
		gbc.gridx++;
		costTime = new TextEditor(12);
		costTime.setToolTipText(PublicResource.getString("resultView.tablestatus.costtime.tip"));
		costTime.setEditable(false);
		costTime.setBorder(createComponentBorder());
		add(costTime, gbc);
		
		gbc.gridx++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0D;
		sql = new TextEditor();
		sql.setToolTipText(PublicResource.getString("resultView.tablestatus.sql"));
		sql.setEditable(false);
		sql.setBorder(createComponentBorder());
		add(sql, gbc);
		
		statusIcon.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent e)
			{
				String msg=stateDesc.get(statusIcon.getIcon());
				if(msg==null)
					return;
				
				RoundedBalloonStyle style = new RoundedBalloonStyle(7, 7, new Color(255, 255, 225), Color.BLACK);
				balloonTip = new BalloonTip(statusIcon, "", style, BalloonTip.Orientation.LEFT_ABOVE, BalloonTip.AttachLocation.NORTH, 
						10, 25, false);
				balloonTip.setText(msg);
				balloonTip.setVisible(true);
			}
			@Override
			public void mouseExited(MouseEvent e)
			{
				if(balloonTip!=null)
					balloonTip.setVisible(false);
			}
		}
		);
	}

	public void setStatusIcon(Icon icon)
	{
		statusIcon.setIcon(icon);
	}
	public void setRowCount(int count) {
	    if(count<0)
	        rowCount.setText("unknown");
	    else
		    rowCount.setText(String.valueOf(count));
	}
	/**
	 * ����sqlִ����ѵ�ʱ��
	 * @param time  --sqlִ��ʱ�䣬���Ϊ������ϢֵΪ��
	 */
	public void setCostTime(long time)
	{
	    if(time<0)  //���timeΪ������Ϣ��Ϊ��
	    {
	        costTime.setText("");
	    }else
	    {
	        costTime.setText(costInfo+time+costInfoUnit);
	    }
	}
	public void setRangeStart(int row) {
		start = row;
	}

	public void setRangeEnd(int row) {
		end = row;
	}

	public void updateRowRange() {
		rowRange.setText(String.valueOf(start) + "-" + String.valueOf(end));
	}
    public void setSql(String sql)
    {
        this.sql.setText(sql);
    }
	public int getRowCount() {
		String txt = rowCount.getText().trim();

		if (txt.equals(""))
			return 0;
		else {
			return Integer.parseInt(txt);
		}
	}
	public int getRangeStart()
	{
		return start;
	}
	public int getRangeEnd()
	{
		return end;
	}
	public String getSql()
	{
		return sql.getText();
	}

	public Border createComponentBorder() {
		return BorderFactory.createCompoundBorder(BorderFactory
				.createBevelBorder(1), BorderFactory.createEmptyBorder(0, 4, 0,
				4));
	}

}
