package de.koch.uim_project.analyse.fileoutput;

import de.tudarmstadt.ukp.lmf.model.semantics.Synset;

/**
 * {@link OutputGenerator} for {@link Synset}
 * 
 * @author Frerik Koch
 * 
 */
public class SynsetOutputGenerator implements OutputGenerator<Synset> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see analyse.fileoutput.OutputGenerator#getOutputString(java.lang.Object)
	 */
	@Override
	public String getOutputString(Synset t) {
		return t.toString() + " Definition: " + t.getDefinitionText();
	}

}
