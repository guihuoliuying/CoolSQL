
package com.cattsoft.coolsql.system;

import java.io.Serializable;

import javax.swing.JFrame;

/**
 * Please get a class instance by invoking method: PropertyManage.getSystemProperty().
 * @author kenny liu
 *
 * 2007-10-30 create
 */
public class SystemProperties implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public static final String FRAME_MAIN_STATE="frame.main.state";
	/**
	 * ѡ�񱾵��ļ�ʱ���ļ�·�����������һ��ѡ���·��ֵ
	 */
	private String selectFile_addDriver=""; //��������
	private String selectFile_exportLog=""; //������־
	private String selectFile_exportData="";  //������ݿ����
	
	private String selectFile_importData="";//import data from local files
	/**
	 * frame property (such as location,size)
	 */
//	private float firstSplitRate=0.2f;  
//	private float secondSplitRate=0.4f;
	
	private int mainFrameState=JFrame.MAXIMIZED_BOTH;
	
	public SystemProperties(){}
	/**
	 * @return the selectFile_addDriver
	 */
	public String getSelectFile_addDriver() {
		return this.selectFile_addDriver;
	}
	/**
	 * @param selectFile_addDriver the selectFile_addDriver to set
	 */
	public void setSelectFile_addDriver(String selectFile_addDriver) {
		this.selectFile_addDriver = selectFile_addDriver;
	}
	/**
	 * @return the selectFile_exportData
	 */
	public String getSelectFile_exportData() {
		return this.selectFile_exportData;
	}
	/**
	 * @param selectFile_exportData the selectFile_exportData to set
	 */
	public void setSelectFile_exportData(String selectFile_exportData) {
		this.selectFile_exportData = selectFile_exportData;
	}
	/**
	 * @return the selectFile_exportLog
	 */
	public String getSelectFile_exportLog() {
		return this.selectFile_exportLog;
	}
	/**
	 * @param selectFile_exportLog the selectFile_exportLog to set
	 */
	public void setSelectFile_exportLog(String selectFile_exportLog) {
		this.selectFile_exportLog = selectFile_exportLog;
	}
	/**
	 * @return the mainFrameState
	 */
	public int getMainFrameState() {
		return this.mainFrameState;
	}
	/**
	 * @param mainFrameState the mainFrameState to set
	 */
	public void setMainFrameState(int mainFrameState) {
		this.mainFrameState = mainFrameState;
	}
	/**
	 * @return the selectFile_importData
	 */
	public String getSelectFile_importData() {
		return this.selectFile_importData;
	}
	/**
	 * @param selectFile_importData the selectFile_importData to set
	 */
	public void setSelectFile_importData(String selectFile_importData) {
		this.selectFile_importData = selectFile_importData;
	}
}
