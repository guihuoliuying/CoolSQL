/*
 * �������� 2006-9-6
 */
package com.cattsoft.coolsql.view.log;

import java.awt.Color;
import java.awt.Container;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

import org.apache.log4j.Logger;

import com.cattsoft.coolsql.main.frame.MainFrame;
import com.cattsoft.coolsql.pub.component.CommonFrame;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.system.PropertyConstant;
import com.cattsoft.coolsql.system.Setting;
import com.cattsoft.coolsql.system.SystemConstant;
import com.cattsoft.coolsql.view.View;
import com.cattsoft.coolsql.view.ViewManage;

/**
 * @author liu_xlin ��־�����࣬��ҪΪ��־�ĵ�ģ�ͺ���־��ͼ����
 */
public class LogProxy implements BaseLog {
	private final static Logger defaultlogger=Logger.getLogger("com.coolsql.view.log.LogProxy");
	private final static Logger sqlErrorLogger=Logger.getLogger("log.error.sqlerror"); //log sql error
    /**
     * ��־����
     */
    public static final int DEBUG = 0;

    public static final int INFO = 1;

    public static final int WARN=2;
    public static final int ERROR = 3;

    /**
     * ��־����Ԫ�ص�����
     */
    public static final int ELEMENT_STRING = 0;

    public static final int ELEMENT_ICON = 1;

    private static LogProxy proxy = null;

    public static Color DEFAULT_DEBUG_COLOR = Color.GREEN;
    public static Color DEFAULT_WARN_COLOR = Color.YELLOW;
    public static Color DEFAULT_ERROR_COLOR = Color.RED;

    public static Color DEFAULT_INFO_COLOR = Color.BLUE;

    private Color debugColor;
    private Color infoColor;
    private Color warnColor;
    private Color errorColor;
    
    private int level;//log level.
    private LogProxy() {
    	debugColor=DEFAULT_DEBUG_COLOR;
    	infoColor=Setting.getInstance().getColorProperty(PropertyConstant.PROPERTY_VIEW_LOG_INFOCOLOR,DEFAULT_INFO_COLOR);
    	warnColor=Setting.getInstance().getColorProperty(PropertyConstant.PROPERTY_VIEW_LOG_WARNCOLOR,DEFAULT_WARN_COLOR);
    	errorColor=Setting.getInstance().getColorProperty(PropertyConstant.PROPERTY_VIEW_LOG_ERRORCOLOR,DEFAULT_ERROR_COLOR);
    	
    	Setting.getInstance().addPropertyChangeListener(new PropertyChangeListener()
    	{

			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getPropertyName().equals(PropertyConstant.PROPERTY_VIEW_LOG_INFOCOLOR))
				{
					infoColor=Setting.getInstance().getColorProperty(PropertyConstant.PROPERTY_VIEW_LOG_INFOCOLOR,DEFAULT_INFO_COLOR);
				}else if(evt.getPropertyName().equals(PropertyConstant.PROPERTY_VIEW_LOG_WARNCOLOR))
				{
					warnColor=Setting.getInstance().getColorProperty(PropertyConstant.PROPERTY_VIEW_LOG_WARNCOLOR,DEFAULT_WARN_COLOR);
				}else if(evt.getPropertyName().equals(PropertyConstant.PROPERTY_VIEW_LOG_ERRORCOLOR))
				{
					errorColor=Setting.getInstance().getColorProperty(PropertyConstant.PROPERTY_VIEW_LOG_ERRORCOLOR,DEFAULT_ERROR_COLOR);
				}
			}
    		
    	}, new String[]{PropertyConstant.PROPERTY_VIEW_LOG_INFOCOLOR,
    			PropertyConstant.PROPERTY_VIEW_LOG_WARNCOLOR,
    			PropertyConstant.PROPERTY_VIEW_LOG_ERRORCOLOR});
    	level=Setting.getInstance().getIntProperty(PropertyConstant.PROPERTY_VIEW_LOG_LEVEL, INFO);
    	Setting.getInstance().addPropertyChangeListener(new PropertyChangeListener()
    			{
		    		public void propertyChange(PropertyChangeEvent evt) {
		    			level=Setting.getInstance().getIntProperty(PropertyConstant.PROPERTY_VIEW_LOG_LEVEL, INFO);
		    		}
    			}
    			, PropertyConstant.PROPERTY_VIEW_LOG_LEVEL);
    	
    }

    public static LogProxy getProxy() {
        if (proxy == null)
            proxy = new LogProxy();
        return proxy;
    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.pub.display.BaseLog#debug(java.lang.Object)
     */
    public void debug(Object ob) {
    	if(level>DEBUG)
    		return;
        LogDocument.getInstance().setColor(debugColor);
        insertObject("DEBUG "+getTime() + ob, ELEMENT_STRING);
    }
    public void warning(Object ob)
    {
    	if(level>WARN)
    		return;
    	LogDocument.getInstance().setColor(warnColor);
        insertObject("WARN  "+getTime()+ob, ELEMENT_STRING);
    }
    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.pub.display.BaseLog#error(java.lang.Object)
     */
    public void error(Object ob) {
    	if(level>ERROR)
    		return;
        LogDocument.getInstance().setColor(errorColor);
        insertObject("ERROR "+getTime() + ob, ELEMENT_STRING);
    }

    /*
     * ���� Javadoc��
     * 
     * @see com.coolsql.pub.display.BaseLog#Info(java.lang.Object)
     */
    public void info(Object ob) {
    	if(level>INFO)
    		return;
        LogDocument.getInstance().setColor(infoColor);
        insertObject("INFO  "+getTime() + ob, ELEMENT_STRING);
    }

    /**
     * �����¶��󣬿��������֣�ͼƬ��Ԫ��
     * 
     * @param ob
     *            ����Ķ���
     * @param type
     *            Ԫ������
     */
    public void insertObject(Object ob, int type) {
        try {
            if (type == ELEMENT_STRING) //�����ַ�
                LogDocument.getInstance().insertString(ob);
            else if (type == ELEMENT_ICON) //����ͼƬ
            {

            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * ��ȡִ�ж�����ʱ��
     * 
     * @return
     */
    public String getTime() {
        return "[" + getCurrentTime() + "] ";
    }

	/**
	 * @return the level
	 */
	public int getLevel() {
		return this.level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}
    /**
     * 
     * ��ȡ��ǰʱ��
     */
    public static String getCurrentTime() {
        String deaultDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(deaultDateTimeFormat);
        return sdf.format(new Date());
    }

    /**
     * һ�����ı���
     * 
     * @param e
     */
    public static void errorReport(Throwable e) {
    	errorReport((String)null,e);
    }
    public static void errorReport(String message,Throwable e)
    {
    	errorReport(message,null,e);
    }
    public static void errorReport(Container con,Throwable e)
    {
    	errorReport(con,null,e);
    }
    public static void errorReport(Logger logger,Throwable e)
    {
    	errorReport(null,logger,e);
    }
    public static void errorReport(String message,Logger logger,Throwable e)
    {
    	errorReport(null,message,logger,e);
    }
    public static void errorReport(Container con,String message,Throwable e) {
    	errorReport(con,message,defaultlogger,e);
    }
    public static void errorReport(Container con,String message,Logger logger,Throwable e) {
        if(message==null||message.equals(""))
        	message = StringUtil.formatMessage(e.getMessage(), 60);

        if(con==null)
        	con=GUIUtil.getMainFrame();
        
        if(e instanceof SQLException)
        {
        	SQLErrorReport(con,message,(SQLException)e);
        	return;
        }
        
        if(logger==null)
        	logger=defaultlogger;
        logger.error(message,e);
        
        messageReport(con,message, JOptionPane.ERROR_MESSAGE);
    }
    /**
     * SQL����Ĵ�����ʾ
     * 
     * @param e
     */
    public static void SQLErrorReport(SQLException e) {
    	SQLErrorReport((String)null,e);
    }
    public static void SQLErrorReport(String message,SQLException e) {
    	SQLErrorReport(null,message,e);
    }
    public static void SQLErrorReport(Container con,SQLException e)
    {
    	SQLErrorReport(con,null,e);
    }
    public static void SQLErrorReport(Container con,String message,SQLException e)
    {
        String errorCode = String.valueOf(e.getErrorCode());
        String sqlState = e.getSQLState();
        if (message == null) {
			message = e.getMessage();
			message = StringUtil.formatMessage(message, 54);
			message += "    SQLState=" + sqlState + "\n    errorCode="
					+ errorCode;
		}
        sqlErrorLogger.error(message,e);
        messageReport(con,message, JOptionPane.ERROR_MESSAGE);
    }
    /**
     * ��Ϣ��ʾ��ʾ
     * 
     * @param message
     */
    public static void messageReport(String message, int messageType) {

        messageReport(GUIUtil.getMainFrame(),message,messageType);
    }
    /**
     * ��Ϣ��ʾ��ʾ
     * 
     * @param message
     */
    private static void messageReport(Container con,String message, int messageType) {
    	if (messageType == JOptionPane.ERROR_MESSAGE) {
	        LogProxy log = LogProxy.getProxy();
	        log.error(message);
    	}

        if(con==null)
            con=GUIUtil.getMainFrame();
        if(!con.isVisible())
            con=GUIUtil.getUpParent(con,Window.class,true);
        JOptionPane.showMessageDialog(con,
                message, "error", messageType);
    }
    /**
     * ��ʾ������Ϣ
     * @param message
     */
    public static void errorMessage(String message)
    {
        message(message,JOptionPane.ERROR_MESSAGE);
    }
    public static void errorMessage(Logger logger,String message)
    {
    	logger.error(message);
    	message(message,JOptionPane.ERROR_MESSAGE);
    }
    public static void errorMessage(Container con,String message)
    {
        message(con,message,JOptionPane.ERROR_MESSAGE);
    }
    /**
     * print error message to file,but error message wouldn't be printed on the log view
     * @param s
     */
    public static void errorLog(String s)
    {
    	defaultlogger.error(s);
    }
    public static void errorLog(String s,Throwable t)
    {
    	defaultlogger.error(s,t);
    }
    public static void sqlErrorLog(String s)
    {
    	sqlErrorLogger.error(s);
    }
    public static void sqlErrorLog(String s,Throwable t)
    {
    	sqlErrorLogger.error(s,t);
    }
    /**
     * ��ʾ��Ϣ
     * @param message
     */
    public static void infoMessage(String message)
    {
        message(message,JOptionPane.INFORMATION_MESSAGE);
    }
    public static void message(String message,int messageType)
    {
        message(GUIUtil.getMainFrame(),
                message, messageType);
    }
    public static void message(Container con,String message,int messageType)
    {
        if(con!=null&&!con.isVisible())
            con=GUIUtil.getUpParent(con,Window.class,true);
        JOptionPane.showMessageDialog(con,
                message, "message", messageType);
    }
    public static void internalError(Throwable e) {
        outputErrorLog(e);
        
        
        View view = ViewManage.getInstance().getBookmarkView();
        int result = JOptionPane.showConfirmDialog(CommonFrame
                .getParentFrame(view), PublicResource
                .getSQLString("sql.common.error"), "error occur",
                JOptionPane.YES_NO_OPTION);
        if(result==JOptionPane.YES_OPTION) //ѡ�����˳�
        {          
            ((MainFrame)GUIUtil.getMainFrame()).closeSystem();
        }
    }
    /**
     * ��������־��ӡ���ļ���
     * @param e
     */
    public static void outputErrorLog(Throwable e)
    {
        PrintStream out=null;
        infoMessage("exception occur!");
        try {
            out=new PrintStream(new FileOutputStream(SystemConstant.userPath+"errorLog.txt",true));
            out.write(("["+getCurrentTime()+"]").getBytes());
            e.printStackTrace(out);
        } catch (Exception e1) {
            
        }finally
        {
            if(out!=null)
            {
                try
                {
                    out.close();
                }catch(Exception e1){}
            }
        }
    }
}
