package de.tobiasloeser.slogangenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a part of a slogan template
 * @author tobias
 *
 */
public class TemplatePart implements java.io.Serializable, Comparable<TemplatePart> {

	private static final long serialVersionUID = 1302916631141501576L;
	private SloganTemplate template;
	private int position;
	private String chunk;
	private boolean fixed;
	private List<String> synsets;
	private List<String> words;
	private int repeatableTimes;
	
	/**
	 * Constructor, which sets the template, position and chunk
	 * @param _template the slogan template, which this this object is a part of
	 * @param _position position of this part in the slogan
	 * @param _chunk chunk of this part
	 * @param _fixed true, if this part is set to one or more words in the word list
	 * @param _repeatableTimes how many times this part is repeatable
	 */
	public TemplatePart(SloganTemplate _template, int _position, String _chunk, boolean _fixed, int _repeatableTimes)
	{
		template = _template;
		position = _position;
		chunk = _chunk;
		fixed = _fixed;
		synsets = new ArrayList<String>();
		words = new ArrayList<String>();
		repeatableTimes = _repeatableTimes;
	}
	
	/**
	 * Getter for position
	 * @return position of part in slogan
	 */
	public int getPosition()
	{
		return position;
	}
	
	/**
	 * Getter for chunk
	 * @return the chunk of the part
	 */
	public String getChunk()
	{ 
		return chunk;
	}
	
	/**
	 * Getter for setting fixed
	 * @return true, if part is fixed
	 */
	public boolean isFixed()
	{
		return fixed;
	}
	
	/**
	 * Getter for the list of synsets
	 * @return list of string with synsets
	 */
	public List<String> getSynsets()
	{
		return synsets;
	}
	
	/**
	 * Getter for list with words
	 * @return list of string with words
	 */
	public List<String> getWords()
	{
		return words;
	}
	
	/**
	 * Adds a word to the list of words
	 * @param word word to be added
	 */
	public void addWord(String word)
	{
		words.add(word);
	}
	
	/**
	 * Searches for a given synset in the list of synsets
	 * @param synset synset to be searched for
	 * @return true, if synset has been found
	 */
	public boolean hasSynset(String synset)
	{
		for(String existingSynset : synsets)
		{
			if(existingSynset.equals(synset))
				return true;
		}
		return false;
	}
	
	/**
	 * Adds a synset to the list of synsets
	 * 
	 * @param synset synset to be added
	 */
	public void addSynset(String synset)
	{
		synsets.add(synset);
	}

	/**
	 * Getter for repeatable
	 * @return true, if part is repeatable
	 */
	public boolean isRepeatable() {
		return (repeatableTimes > 1) ? true : false;
	}

	/**
	 * Compares the part with another part
	 */
	public int compareTo(TemplatePart part) {
		if(position > part.getPosition())
		{
			return 1;
		}
		else if(position == part.getPosition())
		{
			return 0;
		}
		else
		{
			return -1;
		}
	}

	/**
	 * Getter for the slogan template
	 * @return the slogan template
	 */
	public SloganTemplate getTemplate() {
		return template;
	}

	/**
	 * Getter for repeatable times
	 * @return repeatable times as int
	 */
	public int getRepeatableTimes() {
		return repeatableTimes;
	}

}
