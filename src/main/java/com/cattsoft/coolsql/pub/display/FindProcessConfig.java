/*
 * Created on 2007-4-26
 */
package com.cattsoft.coolsql.pub.display;

/**
 * @author liu_xlin
 *�����������Ϣ����ʱ����Ҫ�ƶ�ƥ��ķ�ʽ�������Ƿ���ִ�Сд���Ƿ�ȫ��ƥ�䣬���ҷ�����ǰ�����
 */
public class FindProcessConfig {

    /**
     * ��ǰ����
     */
    public static final int FORWARD=0;
    /**
     * ������
     */
    public static final int BACKFORWARD=1;
    
    /**
     * ���Դ�Сд
     */
    public static final int IGNORECASE=0;
    /**
     * ��Сд����
     */
    public static final int SENSITIVECASE=1;
    /**
     * ȫ��ƥ��
     */
    public static final int MATCH_FULL=1;
    /**
     * ����ƥ��
     */
    public static final int MATCH_PART=0;   
    
    //���ҷ�ʽ
    private int forward;
    //��Сд�Ƿ�����
    private int caseMatch;
    //ƥ��ģʽ
    private int matchMode;
    
    /**
     * �Ƿ�ѭ������
     */
    private boolean isCircle;
    /**
     * ��ѯ�ؼ���
     */
    private String keyWord;
    /**
     * Ĭ�ϣ���ǰ���ң����Դ�Сд������ƥ��,��ѭ������
     *
     */
    public FindProcessConfig()
    {
        this("",0,0,0,false);
    }
    public FindProcessConfig(String keyWord,int forward,int caseMatch,int matchMode,boolean isCircle)
    {
        this.forward=forward;
        this.caseMatch=caseMatch;
        this.matchMode=matchMode;
        this.keyWord=keyWord;
        this.isCircle=isCircle;
    }
    /**
     * @return Returns the caseMatch.
     */
    public int getCaseMatch() {
        return caseMatch;
    }
    /**
     * @param caseMatch The caseMatch to set.
     */
    public void setCaseMatch(int caseMatch) {
        this.caseMatch = caseMatch;
    }
    /**
     * @return Returns the forward.
     */
    public int getForward() {
        return forward;
    }
    /**
     * @param forward The forward to set.
     */
    public void setForward(int forward) {
        this.forward = forward;
    }
    /**
     * @return Returns the matchMode.
     */
    public int getMatchMode() {
        return matchMode;
    }
    /**
     * @param matchMode The matchMode to set.
     */
    public void setMatchMode(int matchMode) {
        this.matchMode = matchMode;
    }
    /**
     * @return Returns the keyWord.
     */
    public String getKeyWord() {
        return keyWord;
    }
    /**
     * @param keyWord The keyWord to set.
     */
    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
    /**
     * @return Returns the isCircle.
     */
    public boolean isCircle() {
        return isCircle;
    }
    /**
     * @param isCircle The isCircle to set.
     */
    public void setCircle(boolean isCircle) {
        this.isCircle = isCircle;
    }
}
