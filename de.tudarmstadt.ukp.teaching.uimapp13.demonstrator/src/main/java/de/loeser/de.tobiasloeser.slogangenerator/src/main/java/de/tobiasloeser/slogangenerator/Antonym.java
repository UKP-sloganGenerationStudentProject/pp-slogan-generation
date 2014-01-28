package de.tobiasloeser.slogangenerator;

import java.util.List;

import de.tudarmstadt.ukp.lmf.api.Uby;
import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;
import de.tudarmstadt.ukp.lmf.model.core.LexicalEntry;
import de.tudarmstadt.ukp.lmf.model.core.Lexicon;
import de.tudarmstadt.ukp.lmf.model.enums.ERelTypeSemantics;
import de.tudarmstadt.ukp.lmf.model.semantics.Synset;
import de.tudarmstadt.ukp.lmf.model.semantics.SynsetRelation;

public class Antonym {

	private Uby uby;
	private Lexicon lex;
	
	public Antonym(Uby _uby) throws UbyInvalidArgumentException
	{
		uby = _uby;
		lex = uby.getLexiconByName("OmegaWikieng");   
	}
	
	public boolean HasAntonym(String word)
	{
		List<LexicalEntry> entries = uby.getLexicalEntries(word, lex);
		for(LexicalEntry entry : entries)
		{
			List<Synset> synsets = entry.getSynsets();
			for(Synset synset : synsets)
			{
				List<SynsetRelation> synsetRelations = synset.getSynsetRelations();
				for(SynsetRelation synsetRelation : synsetRelations)
				{
					if(synsetRelation.getRelType().equals(ERelTypeSemantics.complementary))
						return true;
				}
			}
		} 
		return false;
	}
	
	public String GetAntonymAsSynset(String word)
	{
		List<LexicalEntry> entries = uby.getLexicalEntries(word, lex);
		for(LexicalEntry entry : entries)
		{
			List<Synset> synsets = entry.getSynsets();
			for(Synset synset : synsets)
			{
				List<SynsetRelation> synsetRelations = synset.getSynsetRelations();
				for(SynsetRelation synsetRelation : synsetRelations)
				{
					if(synsetRelation.getRelType().equals(ERelTypeSemantics.complementary))
						return synsetRelation.getTarget().getId();
				}
			}
		} 
		return null;
	}
	
}
