/*
 * BlobDescriptor.java
 *
 * This file is part of CoolSQL, http://coolsql.dev.java.net.
 *
 * Copyright 2008-2010, kenny liu
 *
 * To contact the author please send an email to: mailforlxl@gmail.com
 *
 */
package com.cattsoft.coolsql.sql.dataview.cellcomponent;

import java.sql.Blob;
import java.util.Arrays;

import com.cattsoft.coolsql.pub.util.BinaryDisplayConverter;

/**
 * @author ��Т��(kenny liu)
 *
 * 2008-6-9 create
 */
public class BlobDescriptor {

	/**
	 * The java.sql.Blob object that was read.
	 */
	Blob _blob;
	
	/**
	 * The data read from the Blob.
	 */
	byte[] _data = null;
	
	/**
	 * If <TT>_blobRead</TT> is <TT>true</TT> then at least some
	 * of the data in the BLOB should have been read.  If <TT>false</TT>,
	 * then we have not even tried to read the data.
	 */
	private boolean _blobRead = false;
	
	/**
	 * If <TT>_wholeBlobRead</TT> is <TT>true</TT> then all of the
	 * data in this BLOB has been read into _data..
	 */
	private boolean _wholeBlobRead = false;

	/**
	 * If <TT>_wholeBlobRead</TT> is false, this is the size limit
	 * set by the user for how much to read.
	 */
	private int _userSetBlobLimit;
	
    String BLOB_LABEL = "BLOB";
    
	/**
	 * Ctor
	 */
	public BlobDescriptor (
		Blob blob, byte[] data, 
		boolean blobRead, boolean wholeBlobRead, int userSetBlobLimit) {
		_blob = blob;
		_data = data;
		_blobRead = blobRead;
		_wholeBlobRead = wholeBlobRead;
		_userSetBlobLimit = userSetBlobLimit;
	}
	
	/**
	 * Equals for Blobs means that the internal byte arrays are identical,
	 * including their length.
	 * We need to account for the fact that one or both of them may not
	 * have actually had their data read.  If both have not had their data read,
	 * then they are "equal", in a wierd kind of way.
	 */
	public boolean equals(BlobDescriptor c) {
		if (c == null) {
			// the other obj is null, so see if this non-null obj contains
			// a null value, which is equivilent.
			// Assume that if we have read some of the data and we still have
			// _data == null, then the value in the DB is actually null.
			if (_blobRead == true && _data == null)
				return true;
			else
				return false;
		}
		
		if (c.getBlobRead() == false) {
			// the other obj has not read the data yet.
			if (_blobRead == true)
				return false;	// we have read data, so that is not the same state
			else return true;	//  odd-ball case: assume if neither has read data that they are equal
		}
		
		// the other object has real data
		if (_blobRead == false)
			return false;	// this one does not, so they are not equal
		
		// both have actual data, so compare the strings
		// Note that if one has read all of the data and the other has read only part
		// of the data that we will say that they are NOT equal.
		return Arrays.equals(c.getData(), _data);
	}
	
	/**
	 * toString means print the data string, unless the data has not been
	 * read at all.
	 */
	public String toString() {
		if (_blobRead) {
			if (_data == null)
				return null;
			
			// Convert the data into an ascii representation
			// using the standard convention
			Byte[] useValue = new Byte[_data.length];
					for (int i=0; i<_data.length; i++)
						useValue[i] = Byte.valueOf(_data[i]);
			String outString = BinaryDisplayConverter.convertToString(useValue,
						                            BinaryDisplayConverter.HEX, 
                                                    false);
			if (_wholeBlobRead || _userSetBlobLimit > _data.length)
				return outString;	// we have the whole contents of the BLOB
			else return outString+"...";	// tell user the data is truncated
		}
		else return BLOB_LABEL;
	}
	
	/* 
	 * Getters and Setters
	 */
	 
	public Blob getBlob(){return _blob;}
	public void setBlob(Blob blob){_blob = blob;}
	 
	public byte[] getData(){return _data;}
	public void setData(byte[] data){_data = data;}
	
	public boolean getBlobRead(){return _blobRead;}
	public void setBlobRead(boolean blobRead){_blobRead = blobRead;}
	 
	public boolean getWholeBlobRead(){return _wholeBlobRead;}
	public void setWholeBlobRead(boolean wholeBlobRead){_wholeBlobRead = wholeBlobRead;}

	public int getUserSetBlobLimit(){return _userSetBlobLimit;}
	public void setUserSetBlobLimit(int userSetBlobLimit)
		{_userSetBlobLimit = userSetBlobLimit;}
}
