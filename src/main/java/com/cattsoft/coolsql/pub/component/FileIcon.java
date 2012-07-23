/*
 * �������� 2006-6-2
 *
 */
package com.cattsoft.coolsql.pub.component;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import sun.awt.shell.ShellFolder;

import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 * ��ȡ�ļ�ϵͳ���ļ�ͼ��
 * 
 */
public class FileIcon {
	private static ShellFolder getShellFolder(File f) {
		if (!(f instanceof ShellFolder) && !(f instanceof FileSystemRoot)
				&& isFileSystemRoot(f)) {

			f = createFileSystemRoot(f);
		}
		try {
			return ShellFolder.getShellFolder(f);
		} catch (FileNotFoundException e) {
		    LogProxy.internalError(e);
			return null;
		} catch (InternalError e) {
			System.err.println("FileSystemView.getShellFolder: f=" + f);
			e.printStackTrace();
			return null;
		}
	}
	public static ImageIcon getSystemIcon(File f) {
		if (f != null) {
		    ShellFolder sf = getShellFolder(f);
		    ImageIcon img = new ImageIcon(sf.getIcon(false));
		    if (img != null) {
			return img;
		    } else {
			return (ImageIcon)UIManager.getIcon(f.isDirectory() ? "FileView.directoryIcon" : "FileView.fileIcon");
		    }
		} else {
		    return null;
		}
	}
	private static boolean isFileSystemRoot(File dir) {
		return (dir != null && dir.getAbsolutePath().equals("/"));
	}

	protected static File createFileSystemRoot(File f) {
		// Problem: Removable drives on Windows return false on f.exists()
		// Workaround: Override exists() to always return true.
		return new FileSystemRoot(f) {
			public boolean exists() {
				return true;
			}
		};
	}

	static class FileSystemRoot extends File {
		public FileSystemRoot(File f) {
			super(f, "");
		}

		public FileSystemRoot(String s) {
			super(s);
		}

		public boolean isDirectory() {
			return true;
		}

		public String getName() {
			return getPath();
		}
	}
}
