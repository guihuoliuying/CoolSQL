/**
 * 
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.border.Border;

import com.cattsoft.coolsql.pub.parse.StringManager;
import com.cattsoft.coolsql.pub.parse.StringManagerFactory;
import com.cattsoft.coolsql.system.menubuild.IconResource;
import com.l2fprod.common.swing.StatusBar;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-4-4 create
 */
public class MainFrameStatusBar extends StatusBar {
	private static final StringManager stringMgr=StringManagerFactory.getStringManager(MainFrameStatusBar.class);
	private static final long serialVersionUID = 1L;

	private final JLabel _textLbl;
	private final JLabel sqlEditorStatus; //Server for sql editor, displaying information related with SQL editor .
	private final MemoryInfo memoryPane;
	public MainFrameStatusBar()
	{
		super();
		_textLbl = new JLabel();
		sqlEditorStatus=new JLabel();
		memoryPane=new MemoryInfo();
		String[] ids=new String[]{"text","sqleditor","memoryInfo","timeInfo"};
		Component[] zones=new Component[]{_textLbl,sqlEditorStatus,memoryPane,new TimeInfo()};
		String[] constraints=new String[]{"*","8%","15%","14%"};
		setZones(ids, zones, constraints);
	}
	public void setTextInfo(String str)
	{
		_textLbl.setText(str);
	}
	public void setEditorInfo(String str)
	{
		sqlEditorStatus.setText(str);
	}
	public void dispose()
	{
		memoryPane.updateTimer.stop();
		removeAll();
	}
	final class TimeInfo extends JLabel implements ActionListener
	{
		private static final long serialVersionUID = 1L;

		/** Timer that updates time. */
		private Timer _timer;

		/** Used to format the displayed date. */
		private DateFormat _fmt = DateFormat.getTimeInstance(DateFormat.LONG);
		private Dimension _prefSize;
		private Calendar _calendar = Calendar.getInstance();

		/**
		 * Default ctor.
		 */
		public TimeInfo()
		{
			super("", JLabel.CENTER);
		}

		/**
		 * Add component to its parent. Start the timer for auto-update.
		 */
		public void addNotify()
		{
			super.addNotify();
			_timer = new Timer(1000, this);
			_timer.start();
		}

		/**
		 * Remove component from its parent. Stop the timer.
		 */
		public void removeNotify()
		{
			super.removeNotify();
			if (_timer != null)
			{
				_timer.stop();
				_timer = null;
			}
		}

		/**
		 * Update component with the current time.
		 *
		 * @param	evt		The current event.
		 */
		public void actionPerformed(ActionEvent evt)
		{
			_calendar.setTimeInMillis(System.currentTimeMillis());
			setText(_fmt.format(_calendar.getTime()));
		}

		/**
		 * Return the preferred size of this component.
		 *
		 * @return	the preferred size of this component.
		 */
		public Dimension getPreferredSize()
		{
			if(null == _prefSize)
			{
				// This was originaly done every time.
				// and the count of instantiated objects was amazing
				_prefSize = new Dimension();
				_prefSize.height = 20;
				FontMetrics fm = getFontMetrics(getFont());
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
				_prefSize.width = fm.stringWidth(_fmt.format(cal.getTime()));
				Border border = getBorder();
				if (border != null)
				{
					Insets ins = border.getBorderInsets(this);
					if (ins != null)
					{
						_prefSize.width += (ins.left + ins.right);
					}
				}
				Insets ins = getInsets();
				if (ins != null)
				{
					_prefSize.width += (ins.left + ins.right) + 20;
				}
			}
			return _prefSize;
		}
	}
	final class MemoryInfo extends JPanel
	{
		private static final long serialVersionUID = 1L;

		private JProgressBar _bar;
		
		private Timer updateTimer;
		public MemoryInfo()
		{
			_bar=new JProgressBar();
			_bar.setStringPainted(true);
			
			IconButton trashIcon=new IconButton(IconResource.getIcon("system.icon.trash"));
			trashIcon.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					System.gc();
				}
			}
			);
			trashIcon.setToolTipText(stringMgr.getString("systemgc.invoke"));
			this.setLayout(new BorderLayout(5,0));
			this.add(trashIcon, BorderLayout.EAST);
			this.add(_bar, BorderLayout.CENTER);

			this.setBorder(null);
			updateTimer = new Timer(500, new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					updateLabel();
				}
			});
			updateTimer.start();
		}
		private void updateLabel()
		{
			long total = Runtime.getRuntime().totalMemory() >> 10 >> 10;
			long free = Runtime.getRuntime().freeMemory() >> 10 >> 10;
			long just = total-free;

			_bar.setMinimum(0);
			_bar.setMaximum((int)total);
			_bar.setValue((int)just);

			Object[] params = new Long[]
				{
					Long.valueOf(just),
					Long.valueOf(total)
				};

			// i18n[MemoryPanel.memSize={0} of {1} MB];
			String msg = stringMgr.getString("MemoryPanel.memSize", params);
			_bar.setString(msg);
		}
	}
	
}
