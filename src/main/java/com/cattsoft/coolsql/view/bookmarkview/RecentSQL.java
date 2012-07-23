/*
 * �������� 2006-11-9
 */
package com.cattsoft.coolsql.view.bookmarkview;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

import com.cattsoft.coolsql.bookmarkBean.Bookmark;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.StringUtil;

/**
 * @author liu_xlin ���ִ��sql����Ϣ�� 1��sql��� 2��ִ��sql����Ҫ��ʱ�� 3��ִ��sql��ʱ���
 */
public class RecentSQL implements Serializable {
    private String sql = null; //sql���

    private long costTime = 0L; //ִ��sql����ʱ��

    private transient Bookmark bookmark;//������ǩ

    private long time = 0; //ִ��sql��ʱ���

    public RecentSQL() {
        this("", 0, 0, null);
    }

    public RecentSQL(String sql, long costTime, long time, Bookmark bookmark) {
        this.sql = sql;
        this.costTime = costTime;
        this.bookmark = bookmark;
        this.time = time;
    }

    /**
     * �÷�����ֹ�ڱ����ʱ�򣬽���ǩ������Ϊ����Ԫ��
     * 
     * @return
     */
    public Bookmark bookmark() {
        return bookmark;
    }

    public void setBookmark(Bookmark bookmark) {
        this.bookmark = bookmark;
    }

    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean equals(Object ob) {
        if (ob == null)
            return false;
        if (!(ob instanceof RecentSQL))
            return false;
        RecentSQL tmp = (RecentSQL) ob;
        if (sql.equals(tmp.sql) && costTime == tmp.costTime && time == tmp.time
                && bookmark.getAliasName().equals(tmp.bookmark.getAliasName()))
            return true;
        else
            return false;
    }

    /**
     * ������ת����������
     * @param seqNo  --��ǰsql�������ţ�����ֵС��0������Ӵ���
     * @return   --ת�������������
     */
    public Vector converToVector(int seqNo) {
        Vector v = new Vector();
        if (seqNo >= 0)
            v.add(seqNo + ""); //���
        v.add(bookmark.getAliasName()); //��ǩ
        v.add(getSql()); //sql
        v.add(StringUtil.transDate(new Date(getTime()))); //ִ��ʱ���
        v.add(String.valueOf(getCostTime())
                + PublicResource.getSQLString("sql.execute.time.unit")); //����ʱ��
        return v;
    }
}
