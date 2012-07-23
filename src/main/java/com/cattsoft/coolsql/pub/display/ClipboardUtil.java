/*
 * Created on 2007-1-26
 */
package com.cattsoft.coolsql.pub.display;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

/**
 * @author liu_xlin
 *
 */
public class ClipboardUtil {

    /**
     * ��ȡ���а��ϵ��ı�����
     * @return ���Ƶ��ı�����(String)
     */
    public static String getStringContent()
    {
	    Clipboard clip=Toolkit.getDefaultToolkit().getSystemClipboard();
	    Transferable transfer=clip.getContents(null);
	    if(transfer.isDataFlavorSupported(DataFlavor.stringFlavor))
	    {
	        try {
                Object ob=transfer.getTransferData(DataFlavor.stringFlavor);
                return ob.toString();
            } catch (UnsupportedFlavorException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
	        
	    }else
	        return null;
    }
    /**
     * ��ȡ���а��ϱ����Ƶ��ļ��б�
     * @return  --List ���˱����Ƶ������ļ��б�(file path)
     */
    public static List getFilesContent()
    {
	    Clipboard clip=Toolkit.getDefaultToolkit().getSystemClipboard();
	    Transferable transfer=clip.getContents(null);
	    
	    if(transfer.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
	    {
	        try {
                Object ob=transfer.getTransferData(DataFlavor.javaFileListFlavor);
                if(ob instanceof List)
                    return (List)ob;
                else
                    return null;
            } catch (UnsupportedFlavorException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
	        
	    }else
	        return null;
    }
}
