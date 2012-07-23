/*
 * �������� 2006-12-6
 *
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import com.cattsoft.coolsql.pub.parse.PublicResource;

/**
 * @author liu_xlin
 *  �����б��ѡ�����
 */
public class DateSelectPanel extends JPanel {
    private static Border selectedBorder = new LineBorder(Color.black);

    private static Border unselectedBorder = new EmptyBorder(selectedBorder
            .getBorderInsets(new JLabel()));

    private Calendar calendar = null;

    private JLabel monthLabel = null;

    private JPanel days = null;

    private MouseListener dayBttListener = null;

    private boolean isSupportDateChangeListener = false;

    protected Color selectedBackground;

    protected Color selectedForeground;

    protected Color background;

    protected Color foreground;

    private java.util.Date selectedDate = null;

    /**
     * ���¸�ʽ
     */
    final SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");

    public DateSelectPanel() {
        this(new Date());
    }

    public DateSelectPanel(Date selectedDate) {
        this.selectedDate = selectedDate;
        calendar = Calendar.getInstance();

        selectedBackground = UIManager.getColor("ComboBox.selectionBackground");
        selectedForeground = UIManager.getColor("ComboBox.selectionForeground");
        background = UIManager.getColor("ComboBox.background");
        foreground = UIManager.getColor("ComboBox.foreground");

        dayBttListener = createDayBttListener();

        //   << < yyyy/MM/dd > >>
        JPanel pNorth = new JPanel();
        pNorth.setLayout(new BoxLayout(pNorth, BoxLayout.X_AXIS));
//        pNorth.setBackground(new Color(0, 0, 128));
        pNorth.setBackground(DisplayPanel.getThemeColor());
        pNorth.setForeground(Color.white);
        pNorth.setPreferredSize(new Dimension(1, 25));

        IDateButton btn;
        btn = createSkipButton(Calendar.YEAR, -1);
        btn.setIcon(PublicResource.getUtilIcon("date.selectdate.previousyear"));
        btn.setToolTipText(PublicResource.getUtilString("date.selectdate.previousyear.tip"));
        pNorth.add(Box.createHorizontalStrut(12));
        pNorth.add(btn);
        pNorth.add(Box.createHorizontalStrut(12));

        btn = createSkipButton(Calendar.MONTH, -1);
        btn
                .setIcon(PublicResource
                        .getUtilIcon("date.selectdate.previousmonth"));
        btn.setToolTipText(PublicResource.getUtilString("date.selectdate.previousmonth.tip"));
        pNorth.add(btn);

        monthLabel = new JLabel("", JLabel.CENTER);
        monthLabel.setBackground(new Color(0, 0, 128));
        monthLabel.setForeground(Color.white);
        pNorth.add(Box.createHorizontalGlue());
        pNorth.add(monthLabel);
        pNorth.add(Box.createHorizontalGlue());

        btn = createSkipButton(Calendar.MONTH, 1);
        btn.setIcon(PublicResource.getUtilIcon("date.selectdate.nextmonth"));
        btn.setToolTipText(PublicResource.getUtilString("date.selectdate.nextmonth.tip"));
        pNorth.add(btn);

        btn = createSkipButton(Calendar.YEAR, 1);
        btn.setIcon(PublicResource.getUtilIcon("date.selectdate.nextyear"));
        btn.setToolTipText(PublicResource.getUtilString("date.selectdate.nextyear.tip"));

        pNorth.add(Box.createHorizontalStrut(12));
        pNorth.add(btn);
        pNorth.add(Box.createHorizontalStrut(12));

        //������ ����һ ���ڶ� ������ ������ ������ ������
        JPanel pWeeks = new JPanel(new GridLayout(0, 7));
        pWeeks.setBackground(background);
        pWeeks.setOpaque(true);
        DateFormatSymbols sy = new DateFormatSymbols(Locale.getDefault());
        String strWeeks[] = sy.getShortWeekdays();
        JLabel label = null;
        for (int i = 1; i <= 7; i++) {
            label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setForeground(foreground);
            label.setText(strWeeks[i]);
            pWeeks.add(label);
        }

        //�м�����ڵ����
        days = new JPanel(new GridLayout(0, 7));
        days.setBackground(background);
        days.setOpaque(true);
        JPanel tmp = new JPanel();
        tmp.setLayout(new BorderLayout());
        tmp.add(new JSeparator(), BorderLayout.NORTH);
        tmp.add(days, BorderLayout.CENTER);
        tmp.add(new JSeparator(), BorderLayout.SOUTH);
        JPanel pCenter = new JPanel(new BorderLayout());
        pCenter.setBackground(background);
        pCenter.setOpaque(true);
        pCenter.add(pWeeks, BorderLayout.NORTH);
        pCenter.add(tmp, BorderLayout.CENTER);

        //��ʾ���������,ֱ�ӵ���ͼ�������
        JLabel lbToday = new DateLabel(new Date(), false);
        lbToday.setForeground(foreground);
        lbToday.addMouseListener(dayBttListener);
        JPanel pSouth = new JPanel(new BorderLayout());
        pSouth.setBackground(background);
        pSouth.setForeground(foreground);
        pSouth.add(lbToday, BorderLayout.CENTER);

        //renderer this
        setPreferredSize(new Dimension(280, 180));
        setForeground(foreground);
        setBackground(background);
        setBorder(BorderFactory.createLineBorder(Color.black));

        setLayout(new BorderLayout());
        add(BorderLayout.NORTH, pNorth);
        add(BorderLayout.CENTER, pCenter);
        add(BorderLayout.SOUTH, pSouth);

        updateDays();
    }

    /**
     * ������һ��,��һ��,��һ��,��һ��"��ť"
     * 
     * @param field
     *            int
     * @param amount
     *            int
     * @return JLabel
     */
    protected IDateButton createSkipButton(final int field, final int amount) {
        IDateButton btn = new IDateButton();
        btn.addActionListener(createSkipListener(field, amount));
        return btn;
    }

    protected ActionListener createSkipListener(final int field,
            final int amount) {
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calendar.add(field, amount);
                updateDays();
            }
        };
    }

    /**
     * ��������
     */
    protected void updateDays() {
        //�����·�
        monthLabel.setText(monthFormat.format(calendar.getTime()));

        days.removeAll();
        Calendar selectedCalendar = Calendar.getInstance();
        selectedCalendar.setTime(selectedDate);
        /**
         * 1 2 3 4 5 6 7 2 3 4 5 6 7 8 9 10 11 12 13 8 9 10 11 12 13 14 15 16 17
         * 18 19 14 15 16 17 18 19 20 21 22 23 24 25 20 21 22 23 24 25 26 27 28
         * 29 30 31 26 27 28 29 30 31
         */
        Calendar setupCalendar = (Calendar) calendar.clone();
        setupCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int first = setupCalendar.get(Calendar.DAY_OF_WEEK);
        setupCalendar.add(Calendar.DATE, -first);

        boolean isCurrentMonth = false;
        for (int i = 0; i < 42; i++) {
            setupCalendar.add(Calendar.DATE, 1);
            JLabel label = new DateLabel(setupCalendar.getTime());
            label.setForeground(foreground);
            label.addMouseListener(dayBttListener);

            if ("1".equals(label.getText())) {
                isCurrentMonth = !isCurrentMonth;
            }
            label.setEnabled(isCurrentMonth);
            //��ǰѡ�������
            if (setupCalendar.get(Calendar.YEAR) == selectedCalendar
                    .get(Calendar.YEAR)
                    && setupCalendar.get(Calendar.MONTH) == selectedCalendar
                            .get(Calendar.MONTH)
                    && setupCalendar.get(Calendar.DAY_OF_MONTH) == selectedCalendar
                            .get(Calendar.DAY_OF_MONTH)) {
                label.setBorder(new LineBorder(selectedBackground, 1));
            }
            days.add(label);
        }
        days.validate();
    }

    protected MouseListener createDayBttListener() {
        return new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
            }

            public void mouseClicked(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
                DateLabel com = (DateLabel) e.getComponent();
                if (isEnabled()) {
                    com.setOpaque(false);
                    com.setBackground(background);
                    com.setForeground(foreground);
                }
                DateSelectPanel.this.isSupportDateChangeListener = true;
                DateSelectPanel.this.setSelectedDate(com.getDate());
                DateSelectPanel.this.isSupportDateChangeListener = false;
            }

            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    JComponent com = (JComponent) e.getComponent();
                    com.setOpaque(true);
                    com.setBackground(selectedBackground);
                    com.setForeground(selectedForeground);
                }
            }

            public void mouseExited(MouseEvent e) {
                if (isEnabled()) {
                    JComponent com = (JComponent) e.getComponent();
                    com.setOpaque(false);
                    com.setBackground(background);
                    com.setForeground(foreground);
                }
            }
        };
    }

    protected EventListenerList listenerList = new EventListenerList();

    public void addDateChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    public void removeDateChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    protected void fireDateChanged(ChangeEvent e) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                ((ChangeListener) listeners[i + 1]).stateChanged(e);
            }
        }
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
        this.calendar.setTime(selectedDate);
        updateDays();
        if (isSupportDateChangeListener) {
            fireDateChanged(new ChangeEvent(selectedDate));
        }
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    /**
     * 
     * @author liu_xlin ������ݺ��·ݵİ�ť
     */
    protected class IDateButton extends IconButton {
        public IDateButton() {
            this(null);
        }

        public IDateButton(Icon icon) {
            super(icon);
            this.setRequestFocusEnabled(false);
        }
    }
}
