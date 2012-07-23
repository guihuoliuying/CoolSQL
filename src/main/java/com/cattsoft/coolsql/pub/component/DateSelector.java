/*
 * �������� 2006-12-6
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.BorderLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;

import com.jidesoft.swing.JideComboBox;
import com.sun.java.swing.plaf.motif.MotifComboBoxUI;
import com.sun.java.swing.plaf.windows.WindowsComboBoxUI;

/**
 * @author liu_xlin ����ѡ��ؼ�
 */
public class DateSelector extends JComboBox {

	private static final long serialVersionUID = 1L;

	/**
     * ���ڸ�ʽ����
     */
    public static final String STYLE_DATE = "yyyy/MM/dd";

    public static final String STYLE_DATE1 = "yyyy-MM-dd";

    public static final String STYLE_DATETIME = "yyyy/MM/dd HH:mm:ss";

    public static final String STYLE_DATETIME1 = "yyyy-MM-dd HH:mm:ss";

    /**
     * ���ڸ�ʽ����
     */
    private String formatStyle = STYLE_DATE1;

    /**
     * ��ǰ�������ڸ�ʽ
     */
    private SimpleDateFormat dateFormat = null;

    /**
     * ����ѡ��ؼ���ģ�Ͷ���
     */
    private DateSelectorModel model = null;

    public DateSelector() throws UnsupportedOperationException {
        this(STYLE_DATE1);
    }

    public DateSelector(String formatStyle)
            throws UnsupportedOperationException {
        this(formatStyle, new Date());
    }

    public DateSelector(String formatStyle, Date initialDatetime)
            throws UnsupportedOperationException {

        this.setStyle(formatStyle);
        //���ÿɱ༭
        this.setEditable(true);

        JTextField textField = ((JTextField) getEditor().getEditorComponent());
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        //����ModelΪ��ֵModel
        model = new DateSelectorModel();
        this.setModel(model);
        //���õ�ǰѡ������
        this.setSelectedItem(initialDatetime == null ? new Date()
                : initialDatetime);
    }

    /**
     * ����������ʾ��ʽ
     * 
     * @param style
     */
    public void setStyle(String style) {
        this.formatStyle = style;
        if (style == null)
            return;
        if (dateFormat == null)
            dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern(style);
    }
    /**
     * ��������չʾ�ĸ�ʽ
     * @return
     */
    public String getStyle()
    {
        return formatStyle;
    }
    /**
     * ����UI
     */
    public void updateUI() {
        ComboBoxUI cui = (ComboBoxUI) UIManager.getUI(this);
        if (cui instanceof MetalComboBoxUI) {
            cui = new MetalDateComboBoxUI();
        } else if (cui instanceof MotifComboBoxUI) {
            cui = new MotifDateComboBoxUI();
        } else {
            cui = new WindowsDateComboBoxUI();
        }
        setUI(cui);
    }
    /**
     * ȡ�õ�ǰѡ�������
     * @return Date
     */
    public Date getSelectedDate() throws ParseException{
        return dateFormat.parse(getSelectedItem().toString());
    }

    /**
     * ���õ�ǰѡ�������
     * @return Date
     */
    public void setSelectedDate(Date date) throws ParseException{
        this.setSelectedItem(dateFormat.format(date));
    }

    public void setSelectedItem(Object anObject){
        model.setSelectedItem(anObject);
        super.setSelectedItem(anObject);
    }
    /**
     * 
     * @author liu_xlin ����metal���ʱ��comboBox�����Ӧ��ui
     */
    private class MetalDateComboBoxUI extends MetalComboBoxUI {
        protected ComboPopup createPopup() {
            return new DatePopup(comboBox);
        }
    }

    /**
     * 
     * @author liu_xlin ����windows���ʱ��comboBox�����Ӧ��ui
     */
    private class WindowsDateComboBoxUI extends WindowsComboBoxUI {
        protected ComboPopup createPopup() {
            return new DatePopup(comboBox);
        }
    }

    /**
     * 
     * @author liu_xlin ����motif���ʱ��comboBox�����Ӧ��ui
     */
    private class MotifDateComboBoxUI extends MotifComboBoxUI {
        protected ComboPopup createPopup() {
            return new DatePopup(comboBox);
        }
    }

    /**
     * 
     * @author liu_xlin �����Ͽؼ�������塣��д�����Ա仯֪ͨ����
     */
    protected class DatePopup extends BasicComboPopup implements ChangeListener {
        DateSelectPanel calendarPanel = null;

        public DatePopup(JComboBox box) {
            super(box);
            setLayout(new BorderLayout());
            calendarPanel = new DateSelectPanel();
            calendarPanel.addDateChangeListener(this);
            add(calendarPanel, BorderLayout.CENTER);
            setBorder(BorderFactory.createEmptyBorder());
        }

        /**
         * ��ʾ�������
         */
        protected void firePropertyChange(String propertyName, Object oldValue,
                Object newValue) {
            if (propertyName.equals("visible")) {
                if (oldValue.equals(Boolean.FALSE)
                        && newValue.equals(Boolean.TRUE)) { //SHOW
                    try {
                        String strDate = comboBox.getSelectedItem().toString();
                        Date selectionDate = dateFormat.parse(strDate);
                        calendarPanel.setSelectedDate(selectionDate);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (oldValue.equals(Boolean.TRUE)
                        && newValue.equals(Boolean.FALSE)) { //HIDE
                }
            }
            super.firePropertyChange(propertyName, oldValue, newValue);
        }

        public void stateChanged(ChangeEvent e) {
            Date selectedDate = (Date) e.getSource();
            String strDate = dateFormat.format(selectedDate);
            if (comboBox.isEditable() && comboBox.getEditor() != null) {
                comboBox.configureEditor(comboBox.getEditor(), strDate);
            }
            comboBox.setSelectedItem(strDate);
            comboBox.setPopupVisible(false);
        }
    }

}
