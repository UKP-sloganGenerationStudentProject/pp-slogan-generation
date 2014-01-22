package de.koch.uim_project.database;

/**
 * Database related Exceptions
 * Those can not be handled in most cases since they usually show a configuration error
 * 
 * @author Frerik Koch
 * 
 */

public class DbException extends Exception {

	/**
	 * Eclipse generated serial
	 */
	private static final long serialVersionUID = -308186779577297521L;

	/**
	 * Default constructor
	 */
	public DbException() {
		super();
	}

	/**
	 * Constructor adding a message to the exception
	 * 
	 * @param msg
	 *            Message for the exception
	 */
	public DbException(String msg) {
		super(msg);
	}

	/**
	 * Adds a message and a throwable cause to exception
	 * 
	 * @param msg
	 *            Message for the exception
	 * @param cause
	 *            Cause for the exception
	 */
	public DbException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
