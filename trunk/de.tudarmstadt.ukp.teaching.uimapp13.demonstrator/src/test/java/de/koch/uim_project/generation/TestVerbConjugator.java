package de.koch.uim_project.generation;

import static org.junit.Assert.*;

import org.junit.Test;

import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;

public class TestVerbConjugator {

	@Test
	public void testGetVVNForm(){
		Word chase = new Word("chase", EPartOfSpeech.verb);
		assertTrue("chased".equals(VerbConjugator.getInstance().getVVNForm(chase)));
		
		Word arise = new Word("arise",EPartOfSpeech.verb);
		assertTrue("arose".equals(VerbConjugator.getInstance().getVVNForm(arise)));
	}
	
}
