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

/**
 * This class helps to work with Web1T. 
 * 
 * @author tobias
 *
 */

public class Web1THelper
{

    private HashMap<String, Long> prepositionCount;
    private JWeb1TSearcher web1TSearcher;

    /**
     * Constructor, which initializes the JWeb1TSearcher, Uby and a HashMap used find the best preposition.
     * @param uby Uby instance
     * @param web1TFolder folder with Web1T
     * @throws IOException
     */
    public Web1THelper(final Uby uby, final File web1TFolder)
        throws IOException
    {    	
        this.web1TSearcher = new JWeb1TSearcher(web1TFolder, 1, 3);

        this.prepositionCount = new HashMap<String, Long>();

        // Load all prepositions and save them in the HashMap
        Lexicon lex = null;
        try {
            lex = uby.getLexiconByName("WiktionaryEN");
        }
        catch (final UbyInvalidArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final Iterator<LexicalEntry> lexicalEntryIterator = uby.getLexicalEntryIterator(
                EPartOfSpeech.adpositionPreposition, lex);
        while (lexicalEntryIterator.hasNext()) {
            final LexicalEntry entry = lexicalEntryIterator.next();
            this.prepositionCount.put(entry.getLemmaForm(), (long) 0);
        }
    }

    /**
     * adds the word to the found preposition. 
     * 
     * @param word word for which a preposition is searched
     * @return preposition and given word as string 
     */
    public String getPrepositionAndWord(final String word)
    {
    	return getPreposition(word) + " " + word;
    	
    }
    
    /**
     * Searches for every combination of given word and preposition in Web1T, saves the frequency and returns the best preposition. 
     * 
     * @param word word for which a preposition is searched.
     * @return the preposition with the highest frequency
     */
    public String getPreposition(final String word)
    {
        // Reset the counters
        for (final String key : this.prepositionCount.keySet()) {
            this.prepositionCount.put(key, (long) 0);
        }
        // Set frequency for all prepositions in combination with the word
        for (final String key : this.prepositionCount.keySet()) {
            try {
                final long freq = this.web1TSearcher.getFrequency(key + " " + word);
                this.prepositionCount.put(key, freq);
            }
            catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // Get the preposition with the highest frequency
        long max = 0;
        String maxPreposition = null;
        for (final String key : this.prepositionCount.keySet()) {
            if (this.prepositionCount.get(key) > max) {
                max = this.prepositionCount.get(key);
                maxPreposition = key;
            }
        }
        return maxPreposition;
    }

}
