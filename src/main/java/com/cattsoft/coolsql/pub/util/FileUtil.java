package com.cattsoft.coolsql.pub.util;

import java.awt.Container;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.system.PropertyManage;
import com.cattsoft.coolsql.view.log.LogProxy;


/**
 */
public class FileUtil
{
	private static final int BUFF_SIZE = 16*1024;
	
	/*
	 * Closes all streams in the list.
	 * @param a list of streams to close
	 */
	public static void closeStreams(List<Closeable> streams)
	{
		if (streams == null) return;

		for (Closeable str : streams)
		{
			closeQuitely(str);
		}
	}
	
	/**
	 * Read the contents of the Reader into the provided StringBuilder.
	 * Max. numLines lines are read.
	 * @param in the Reader to be used 
	 * @param buffer the StringBuilder to received the lines
	 * @param numLines the max. number of lines to be read
	 * @param lineEnd the lineEnding to be used
	 * @return the number of lines read
	 */
	public static final int readLines(BufferedReader in, StringBuilder buffer, int numLines, String lineEnd)
		throws IOException
	{
		int lines = 0;
		String line = in.readLine();
		while (line != null && lines < numLines) 
		{
			buffer.append(line);
			buffer.append(lineEnd);
			lines ++;
			line = in.readLine();
		}
		if (line != null) 
		{
			// loop was ended due to numLines reached, so append the 
			// last line retrieved
			buffer.append(line);
			buffer.append(lineEnd);
		}
		return lines;
	}
	
	public static final String getLineEnding(Reader in)
		throws IOException
	{
		String ending = null;
		char c = (char)in.read();
		while (c != -1)
		{
			if (c == '\r')
			{ 
				char n = (char)in.read();
				if (n == '\n')
				{
					ending = "\r\n";
					break;
				}
			}
			else if (c == '\n')
			{
				ending = "\n";
				break;
			}
			c = (char)in.read();
		}
		return ending;
	}
	
	
	public static final long estimateRecords(File f)
		throws IOException
	{
		return estimateRecords(f, 5);
	}

	/**
	 * Tries to estimate the number of records in the given file.
	 * This is done by reading the first <tt>sampleLines</tt> records
	 * of the file and assuming the average size of an row in the first
	 * lines is close to the average row in the complete file.
	 */
	public static final long estimateRecords(File f, long sampleLines)
		throws IOException
	{
		if (sampleLines <= 0) throw new IllegalArgumentException("Sample size must be greater then zero");
		if (!f.exists()) return -1;
		if (!f.isFile()) return -1;
		long size = f.length();
		if (size == 0) return 0;
		
		long lineSize = 0;

		BufferedReader in = null;
		try
		{
			in = new BufferedReader(new FileReader(f), 8192);
			in.readLine(); // skip the first line
			int lfSize = StringUtil.LINE_TERMINATOR.length();
			for (int i=0; i < sampleLines; i++)
			{
				String line = in.readLine();
				if (line == null) return i + 1;
				lineSize += (line.length() + lfSize);
			}
		}
		finally
		{
			closeQuitely(in);
		}
		return (size / (lineSize / sampleLines));

	}

	/**
	 * Copies the content of the InputStream to the OutputStream.
	 * Both streams are closed automatically.
	 */
	public static long copy(InputStream in, OutputStream out)
		throws IOException
	{
		long filesize = 0;
		try
		{
			byte[] buffer = new byte[BUFF_SIZE];
			int bytesRead = in.read(buffer);
			while (bytesRead != -1)
			{
				filesize += bytesRead;
				out.write(buffer, 0, bytesRead);
				bytesRead = in.read(buffer);
			}
		}
		finally
		{
			closeQuitely(out);
			closeQuitely(in);
		}
		return filesize;
	}

	/**
	 * Read the content of the Reader into a String.
	 * The Reader is closed automatically.
	 */
	public static String readCharacters(Reader in)
		throws IOException
	{
		if (in == null) return null;
		StringBuilder result = new StringBuilder(1024);
		char[] buff = new char[BUFF_SIZE];
		int bytesRead = in.read(buff);
		try
		{
			while (bytesRead > -1)
			{
				result.append(buff, 0, bytesRead);
				bytesRead = in.read(buff);
			}
		}
		finally
		{
			closeQuitely(in);
		}
		return result.toString();
	}

	/**
	 * Read the content of the InputStream into a ByteArray.
	 * The InputStream is closed automatically.
	 */
	public static byte[] readBytes(InputStream in)
		throws IOException
	{
		if (in == null) return null;
		ByteBuffer result = new ByteBuffer();
		byte[] buff = new byte[BUFF_SIZE];	

		try
		{
			int bytesRead = in.read(buff);

			while (bytesRead > -1)
			{
				result.append(buff, 0, bytesRead);
				bytesRead = in.read(buff);
			}
		}
		finally
		{
			closeQuitely(in);
		}
		return result.getBuffer();
	}

	/**
	 * Returns the number of characters according to the 
	 * encoding in the specified file. For single-byte 
	 * encodings this should be identical to source.length()
	 * 
	 * For large files this might take some time!
	 * 
	 * @param source the (text) file to check
	 * @param encoding the encoding of the text file
	 * @return the number of characters (not bytes) in the file
	 */
	public static long getCharacterLength(File source, String encoding)
		throws IOException
	{
		BufferedReader r = null;
		long result = 0;
		try
		{
			r = EncodingUtil.createBufferedReader(source, encoding, 32*1024);
			// Not very efficient, but I can't think of a different solution
			// to retrieve the number of characters
			result = r.skip(Long.MAX_VALUE);
		}
		finally
		{
			closeQuitely(r);
		}

		return result;
	}
	
	public static void closeQuitely(Closeable c)
	{
		if (c == null) return;
		
		try
		{
			c.close();
		}
		catch (IOException e)
		{
			// ignore
		}
	}
	public static List<String> readMultiLineFile(File file)
	{
		if(file==null||!file.exists())
			return null;
		
		try {
			LineNumberReader reader=new LineNumberReader(new FileReader(file));
			String lineStr=null;
			List<String> data=new ArrayList<String>();
			while((lineStr=reader.readLine())!=null)
			{
				lineStr=StringUtil.trim(lineStr);
				if(lineStr.equals(""))
					continue;
				
				data.add(lineStr);
			}
			
			return data;
		} catch (Exception e) {
			LogProxy.errorLog("readMultiLineFile failed!", e);
			return null;
		} 
	}
	/**
	 * This method used to write some simple text to file,suggest that the length of text doesn't exceed 1000.
	 * @param textContent text that need to be saved
	 * @param file --local file object
	 */
	public static void writeSimpleTextToFile(String textContent,File file)
	{
		if(file==null||file.isDirectory())
			return;
		OutputStream out=null;
		try {
			out=new FileOutputStream(file);
			out.write(textContent.getBytes());
		} catch (Exception e) {
			LogProxy.errorReport("export simple text to file error"+e.getMessage(), e);
			return ;
		}finally
		{
			try {
				if(out!=null)
					out.close();
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * Enclosing  a method responsible for selecting file which the data are export into .
	 * @param con --the owner container of dialog used to select files.
	 */
	public static File selectFileForExportData(Container con)
	{
		File file=GUIUtil.selectFileNoFilter(con,PropertyManage.getSystemProperty().getSelectFile_exportData());
		if(file!=null)
			PropertyManage.getSystemProperty().setSelectFile_exportData(file.getAbsolutePath());
		
		return file;
	}
	/**
	 * There is a similar function with above method,differently this method can check exist of file selected
	 * @param con 
	 * @param isPromptOnExist --will prompt whether overlaying the exist file with new created file.
	 * @return
	 */
	public static File selectFileForExportData(Container con,boolean isPromptOnExist)
	{
		File file=GUIUtil.selectFileNoFilter(con,PropertyManage.getSystemProperty().getSelectFile_exportData(),isPromptOnExist);
		if(file!=null)
			PropertyManage.getSystemProperty().setSelectFile_exportData(file.getAbsolutePath());
		
		return file;
	}
	/**
	 * Be similar with method selectFileForExportData(),this method for exporting log
	 * @return
	 */
	public static File selectFileForExportLog(Container con)
	{
		File file=GUIUtil.selectFileNoFilter(con,PropertyManage.getSystemProperty().getSelectFile_exportLog());
		if(file!=null)
			PropertyManage.getSystemProperty().setSelectFile_exportLog(file.getAbsolutePath());
		
		return file;
	}
	/**
	 * There is a similar function with above method,differently this method can check exist of file selected
	 * @param con 
	 * @param isPromptOnExist --will prompt whether overlaying the exist file with new created file.
	 * @return
	 */
	public static File selectFileForExportLog(Container con,boolean isPromptOnExist)
	{
		File file=GUIUtil.selectFileNoFilter(con,PropertyManage.getSystemProperty().getSelectFile_exportLog(),isPromptOnExist);
		if(file!=null)
			PropertyManage.getSystemProperty().setSelectFile_exportLog(file.getAbsolutePath());
		
		return file;
	}
    /**
     * Deletes a directory recursively. 
     *
     * @param directory  directory to delete
     * @throws IOException in case deletion is unsuccessful
     */
    public static void deleteDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        cleanDirectory(directory);
        if (!directory.delete()) {
            String message =
                "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }

    /**
     * Cleans a directory without deleting it.
     *
     * @param directory directory to clean
     * @throws IOException in case cleaning is unsuccessful
     */
    public static void cleanDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        File[] files = directory.listFiles();
        if (files == null) {  // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            try {
                forceDelete(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }
    /**
     * Deletes a file. If file is a directory, delete it and all sub-directories.
     * <p>
     * The difference between File.delete() and this method are:
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>You get exceptions when a file or directory cannot be deleted.
     *      (java.io.File methods returns a boolean)</li>
     * </ul>
     *
     * @param file  file or directory to delete, must not be <code>null</code>
     * @throws NullPointerException if the directory is <code>null</code>
     * @throws FileNotFoundException if the file was not found
     * @throws IOException in case deletion is unsuccessful
     */
    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            boolean filePresent = file.exists();
            if (!file.delete()) {
                if (!filePresent){
                    throw new FileNotFoundException("File does not exist: " + file);
                }
                String message =
                    "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }
    
    public static String getFileName(File file) {
    	if (file == null) {
    		return null;
    	}
    	return getFileName(file.getAbsoluteFile());
    }
    /**
     * Return the name of file or directory.
     * @param path the file or directory.
     * @return file name.
     */
    public static String getFileName(String path) {
    	path = StringUtils.trimToEmpty(path);
    	int index = path.lastIndexOf(File.separator);
    	if (index == (path.length() - 1)) {
    		path = path.substring(0, index);
    		index = path.lastIndexOf(File.separator);
    	}
    	if (index == -1) {
    		return path;
    	} else {
    		return path.substring(index + 1);
    	}
    }
}
