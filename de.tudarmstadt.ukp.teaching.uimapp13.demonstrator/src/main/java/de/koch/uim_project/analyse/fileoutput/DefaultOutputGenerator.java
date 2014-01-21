package de.koch.uim_project.analyse.fileoutput;

/**
 * Default {@link OutputGenerator} utilizing {@link Object#toString()}
 * 
 * @author Frerik Koch
 * 
 * @param <T>
 *            Type to generate output for
 */
public class DefaultOutputGenerator<T> implements OutputGenerator<T> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see analyse.fileoutput.OutputGenerator#getOutputString(java.lang.Object)
	 */
	@Override
	public String getOutputString(T t) {
		return t.toString();
	}

}
