package com.cattsoft.coolsql.sql.interfaces;

/**
 * An interface to get parts of a character source.
 * 
 * I'm not using CharSequence because I need the {@link #done()} method 
 * to cleanup any resource that were used by the sequence.
 * 
 * The IteratingParser uses a FileMappedSequence which used NIO
 * to read the characters and this implementation needs a cleanup
 * method to close the file handles, which would not be offered by 
 * the CharSequence interface.
 */
public interface CharacterSequence
	extends CharSequence
{

	/**
	 * Release any resources used by the CharacterSequence
	 */
	void done();
}
