package de.koch.uim_project.generation.exception;

import de.koch.uim_project.generation.Generator;

/**
 * This exception is thrown if the {@link Generator} is unable to create a slogan with given parameters
 * @author Frerik Koch
 *
 */
public class SloganNotCreatedException extends Exception {

	/**
	 * Eclipse generated serial.
	 */
	private static final long serialVersionUID = 1358224428329650026L;

	/**
	 * @param msg
	 *            Message for exception
	 */
	public SloganNotCreatedException(String msg) {
		super(msg);
	}

	/**
	 * @param msg
	 *            Message for exception
	 * @param t
	 *            Cause for exception
	 */
	public SloganNotCreatedException(String msg, Throwable t) {
		super(msg, t);
	}

}
