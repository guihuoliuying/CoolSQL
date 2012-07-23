/*
 * �������� 2006-8-4
 *
 */
package com.cattsoft.coolsql.view.sqleditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.swing.text.DefaultStyledDocument;

import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin ���༭������Ԫ�����óɸ���
 */
public class SyntaxHighlighter extends Thread {
    /**
     * key:����󣬿������ĵ�ģ�Ͷ���   value:�����̣߳�SyntaxHighlighter��
     */
    private static Map threadMap=null;
    static
    {
        threadMap=new HashMap();
    }
    public synchronized void updateText(String text, int start) {
        if (StringUtil.trim(text).equals(""))
            return;

        requests.add(new UpdateRequest(text, start));
        notify();
    }

    public void run() {
        while (running)
            try {
                synchronized (this) {
                    if (requests.size() <= 0)
                        wait();
                    else
                        Thread.sleep(10L);
                }

                if (!running)
                    break;

                UpdateRequest request = (UpdateRequest) requests.removeFirst();
                String text = request.text.toUpperCase();
                List tokens = SQLLexx.parse(text);
                List styles = new ArrayList();
                int min = Integer.MAX_VALUE;
                int max = 0;
                for (int i = 0; i < tokens.size(); i++) {
                    Token t = (Token) tokens.get(i);
                    String value = t.getValue();
                    int start = t.getStart();
                    int length = t.getEnd() - t.getStart();
                    int realStart = request.getOffset() + start; //ʵ��λ��

                    min = Math.min(start, min);
                    max = Math.max(max, t.getEnd());
                    if (t.getType() == Token.IDENTIFIER) {
                        boolean keyword = false;
                        for (int index = 0; index < EditorUtil.KEYWORDS.length; index++)
                            if (value.equals(EditorUtil.KEYWORDS[index]))
                                keyword = true;

                        if (keyword) {
                            doc.setCharacterAttributes(realStart, length,
                                    EditorUtil.KEYWORD_SET, true);
                        } else {
                            doc.setCharacterAttributes(realStart, length,
                                    EditorUtil.NORMAL_SET, true);
                        }
                    } else if (t.getType() == Token.COMMENT) {
                        doc.setCharacterAttributes(realStart, length,
                                EditorUtil.COMMENT_SET, true);
                    } else if (t.getType() == Token.LITERAL) {
                        doc.setCharacterAttributes(realStart, length,
                                EditorUtil.VALUE_SET, true);
                    } else if (t.getType() == Token.NUMERIC) {
                        doc.setCharacterAttributes(realStart, length,
                                EditorUtil.NUMBER_SET, true);
                    } else {
                        doc.setCharacterAttributes(realStart, length,
                                EditorUtil.NORMAL_SET, true);
                    }
                }
            } catch (NoSuchElementException nosuchelementexception) {
                LogProxy.outputErrorLog(nosuchelementexception);
            } catch (InterruptedException interruptedexception) {
                LogProxy.outputErrorLog(interruptedexception);
            }
    }

    //�����̵߳����к�ֹͣ
    private boolean running;

    //��ĵı���
    private LinkedList requests;

    //����ص��ĵ�ģ��
    private DefaultStyledDocument doc;

    public SyntaxHighlighter(DefaultStyledDocument doc) {
        running = true;
        requests = new LinkedList();
        this.doc = doc;
        setPriority(1);
        start();
    }

    /**
     * ֹͣ������ʾ�̵߳�����
     *  
     */
    public void stopRun() {
        running = false;
        requests.clear();
        requests=null;
        doc=null;
        synchronized (this) {
            notify();
        }
    }
    /**
     * ���ָ���ļ�����ȡ�����߳�
     * @param ob  --�����
     * @return  --�����߳�
     */
    public static SyntaxHighlighter getThread(Object ob)
    {
        return (SyntaxHighlighter)threadMap.get(ob);
    }
    /**
     * ���浱ǰ�ĸ����߳�
     * @param ob  --��ֵ����
     * @param highlighter  --�����߳�
     */
    public static void addThread(Object ob,SyntaxHighlighter highlighter)
    {
        threadMap.put(ob,highlighter);
    }
    /**
     * ��ָֹ����ֵ�������Ӧ�ĸ����̡߳�
     * @param ob  --��ֵ����
     */
    public static void stopThread(Object ob)
    {
        SyntaxHighlighter highlight=getThread(ob);
        if(highlight!=null)
            highlight.stopRun();
    }
    /**
     * 
     * @author liu_xlin ÿһ�����ݸ�ģ���������󱣴棬�����ı����ݣ������ʼ��͸���ı�����
     */
    protected class UpdateRequest {

        private String text;

        /**
         * �ı����������༭�ĵ���ƫ��
         */
        private int offset;

        //		private int length;

        public UpdateRequest(String text, int offset) {
            this.text = text;
            this.offset = offset;
        }

        //        public int getLength() {
        //            return length;
        //        }
        //        public void setLength(int length) {
        //            this.length = length;
        //        }
        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
