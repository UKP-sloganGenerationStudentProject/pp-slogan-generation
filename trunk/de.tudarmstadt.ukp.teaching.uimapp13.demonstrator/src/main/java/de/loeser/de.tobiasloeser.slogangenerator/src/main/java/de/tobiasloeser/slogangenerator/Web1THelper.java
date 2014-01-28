package de.tobiasloeser.slogangenerator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import com.googlecode.jweb1t.JWeb1TSearcher;

import de.tudarmstadt.ukp.lmf.api.Uby;
import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;
import de.tudarmstadt.ukp.lmf.model.core.LexicalEntry;
import de.tudarmstadt.ukp.lmf.model.core.Lexicon;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;

public class Web1THelper {

	private HashMap<String, Long> prepositionCount;
	private JWeb1TSearcher web1TSearcher;
	
	public Web1THelper(Uby uby) throws IOException
	{
		String dkproHome = System.getenv("DKPRO_HOME");
		web1TSearcher = new JWeb1TSearcher(new File(dkproHome, "web1t/ENGLISH"), 1, 3);
		
		prepositionCount = new HashMap<String, Long>();
		
		// Load all prepositions and save them in the HashMap
		Lexicon lex = null;
		try {
			lex = uby.getLexiconByName("WiktionaryEN");
		} catch (UbyInvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator<LexicalEntry> lexicalEntryIterator = uby.getLexicalEntryIterator(EPartOfSpeech.adpositionPreposition, lex);
		while(lexicalEntryIterator.hasNext())
		{
			LexicalEntry entry = lexicalEntryIterator.next();
			prepositionCount.put(entry.getLemmaForm(), (long) 0);
		}		
	}
	
	public String getPreposition(String word)
	{
		// Reset the counters
		for(String key : prepositionCount.keySet())
			prepositionCount.put(key, (long) 0);
		// Set frequency for all prepositions in combination with the word
		for(String key : prepositionCount.keySet())
		{
			try {
				long freq = web1TSearcher.getFrequency(key + " " + word);
				prepositionCount.put(key, freq);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Get the preposition with the highest frequency
		long max = 0;
		String maxPreposition = null;
		for(String key : prepositionCount.keySet())
		{
			if(prepositionCount.get(key) > max )
			{
				max = prepositionCount.get(key);
				maxPreposition = key;
			}
		}		
		return maxPreposition;
	}
	
	
}
