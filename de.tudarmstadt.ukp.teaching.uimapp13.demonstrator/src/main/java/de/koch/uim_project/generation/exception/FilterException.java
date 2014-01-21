package de.koch.uim_project.generation.exception;

/**
 * This exception should be thrown if during filtering an exception occurs.
 * Details should be placed in the msg
 * 
 * @author Frerik Koch
 * 
 */
public class FilterException extends Exception {

	/**
	 * Eclipse generated serial.
	 */
	private static final long serialVersionUID = 2702388617014357767L;

	/**
	 * 
	 * @param msg
	 *            Message for the exception
	 */
	public FilterException(String msg) {
		super(msg);
	}

	/**
	 * @param msg
	 *            Message for the exception
	 * @param t
	 *            Cause for the exception
	 */
	public FilterException(String msg, Throwable t) {
		super(msg, t);
	}
}
