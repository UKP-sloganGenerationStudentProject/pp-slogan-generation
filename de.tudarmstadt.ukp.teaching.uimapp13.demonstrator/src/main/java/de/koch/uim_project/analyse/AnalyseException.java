package de.koch.uim_project.analyse;

/**
 * @see analyse related exceptions
 * @author Frerik Koch
 * 
 */
public class AnalyseException extends Exception {

	/**
	 * Eclipse generated serial.
	 */
	private static final long serialVersionUID = -5634816126920199125L;

	public AnalyseException(String msg) {
		super(msg);
	}

	public AnalyseException() {
		super();
	}

	public AnalyseException(String string, Throwable e) {
		super(string, e);
	}

}
