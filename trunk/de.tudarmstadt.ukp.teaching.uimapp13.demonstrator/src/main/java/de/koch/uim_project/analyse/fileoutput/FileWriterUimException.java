package de.koch.uim_project.analyse.fileoutput;

/**
 * @see fileoutput related exceptions
 * @author Frerik Koch
 * 
 */
public class FileWriterUimException extends Exception {

	/**
	 * Eclipse generated serial.
	 */
	private static final long serialVersionUID = -4501604360508791551L;

	public FileWriterUimException() {
		super();
	}

	public FileWriterUimException(String msg) {
		super(msg);
	}

	public FileWriterUimException(String string, Exception e) {
		super(string, e);
	}
}
