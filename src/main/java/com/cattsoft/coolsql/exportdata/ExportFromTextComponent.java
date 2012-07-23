/*
 * �������� 2006-10-18
 */
package com.cattsoft.coolsql.exportdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.text.JTextComponent;

import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;

/**
 * @author liu_xlin ��ɽ��ı��ؼ������ݵ����Ĺ�����
 */
public class ExportFromTextComponent extends ExportComponentData {

    public ExportFromTextComponent(JTextComponent source) {
        super(source);
    }

    /**
     * ����Ϊ�ı��ļ�
     */
    public void exportToTxt() throws UnifyException {
        File file = this.selectFile(null);
        if (file != null) {
            FileOutputStream out = null;
            try {
                GUIUtil.createDir(file.getAbsolutePath(),false, false);
                out = new FileOutputStream(file);

                byte[] info = ((JTextComponent) this.getSource()).getText()
                        .getBytes();
                out.write(info);
            } catch (FileNotFoundException e) {
                throw new UnifyException(PublicResource
                        .getSQLString("export.filenotfound")+e.getMessage());
            } catch (IOException e) {
                throw new UnifyException(PublicResource
                        .getSQLString("export.filewriteerror"));
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e) {
                    }
                }
            }
        }

    }

}
