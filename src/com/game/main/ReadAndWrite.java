package com.game.main;

public interface ReadAndWrite {
	
	/**
	 * This method writes text into a given file name
	 * @param file - String of which the file should be written to
	 * @param text - String the text that should be written
	 */
	public void writeTo(String file, String text);
	
	/**
	 * This method reads from a file
	 * @param file - String of which the file should be read from
	 */
	public void readFrom(String file);
}
