package de.tobiasloeser.slogangenerator;

import java.util.ArrayList;
import java.util.List;

public class TemplatePart implements java.io.Serializable, Comparable<TemplatePart> {

	private static final long serialVersionUID = 1302916631141501576L;
	private SloganTemplate template;
	private int position;
	private String chunk;
	private boolean fixed;
	private List<String> synsets;
	private List<String> words;
	private int repeatableTimes;
	
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
	
	public int getPosition()
	{
		return position;
	}
	
	public String getChunk()
	{ 
		return chunk;
	}
	
	public boolean isFixed()
	{
		return fixed;
	}
	
	public List<String> getSynsets()
	{
		return synsets;
	}
	
	public List<String> getWords()
	{
		return words;
	}
	
	public void addWord(String word)
	{
		words.add(word);
	}
	
	public boolean hasSynset(String synset)
	{
		for(String existingSynset : synsets)
		{
			if(existingSynset.equals(synset))
				return true;
		}
		return false;
	}
	
	public void addSynset(String synset)
	{
		synsets.add(synset);
	}

	public boolean isRepeatable() {
		return (repeatableTimes > 1) ? true : false;
	}

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

	public SloganTemplate getTemplate() {
		return template;
	}

	public int getRepeatableTimes() {
		return repeatableTimes;
	}

}
