/*
 * �������� 2006-12-6
 *
 */
package com.cattsoft.coolsql.pub.component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 * @author liu_xlin
 *����ѡ��ؼ�ģ��
 */
public class DateSelectorModel extends AbstractListModel implements ComboBoxModel{
	/**
	 * ���ڸ�ʽ����
	 */
    private SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");;
    /**
     * ѡ�������ֵ
     */
    private String selectedDate = "";

    public DateSelectorModel(){
    }

    public DateSelectorModel(SimpleDateFormat dateFormat){
        setDateFormat(dateFormat);
    }

    public void setDateFormat(SimpleDateFormat dateFormat){
        this.dateFormat = dateFormat;
    }

    public SimpleDateFormat getDateFormat(){
        return dateFormat;
    }
    /**
     * ʵ�ֽӿڷ�����
     * ����ģ��ѡ�����ֵ
     * @param data --������Ķ���ֵ
     */
    public void setSelectedItem(Object data){
        if(data == null){
            return;
        }
        if(data instanceof Date){
            try{
                selectedDate = this.dateFormat.format((Date)data);
            } catch(Exception ex){
                ex.printStackTrace();
            }
        } else{
            try{
                String strDate = data.toString().trim();
                if(strDate.length() != 10 && strDate.length() != 19){
                    return;
                }
                String pattern = dateFormat.toPattern();
                if(strDate.length() == 10 && pattern.length() == 19){
                    strDate = strDate + selectedDate.substring(10);
                }
                dateFormat.parse(strDate);
                selectedDate = strDate;
            } catch(ParseException ex){
                throw new UnsupportedOperationException(
                    "Invalid datetime format: [" + data
                    + "]");
            }
        }
        fireContentsChanged(this, -1, -1);
    }

    public Object getSelectedItem(){
        return selectedDate;
    }

    public Object getElementAt(int index){
        return selectedDate;
    }

    public int getSize(){
        return 1;
    }

}
