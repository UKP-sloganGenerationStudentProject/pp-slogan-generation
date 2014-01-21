package de.koch.uim_project.analyse.fileoutput;

/**
 * Interface for output generators used in {@link FileWriterUIM}
 * 
 * @author Frerik Koch
 * 
 * @param <T>
 *            Type to print to file
 */
public interface OutputGenerator<T> {

	/**
	 * Generates an output String for T
	 * 
	 * @param t
	 *            Object to generated output for
	 * @return Generated output
	 */
	public String getOutputString(T t);
}
