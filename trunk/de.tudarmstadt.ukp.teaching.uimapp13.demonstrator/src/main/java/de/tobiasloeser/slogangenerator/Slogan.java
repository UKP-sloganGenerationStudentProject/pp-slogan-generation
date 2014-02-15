package de.tobiasloeser.slogangenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a generated slogan. 
 * 
 * @author tobias
 *
 */
public class Slogan {
	
	private List<SloganPart> parts;
	private boolean listing;
	private String separator;
	
	/**
	 * Constructor, which initializes the part list
	 * @param _listing defines, whether the slogan is a listing
	 * @param _separator defines, whether there is a separator for the listing
	 */
	public Slogan(boolean _listing, String _separator)
	{
		parts = new ArrayList<SloganPart>();
		listing = _listing;
		separator = _separator;
	}
	
	/**
	 * Returns all slogan parts as a list. 
	 * @return a list of slogan parts
	 */
	public List<SloganPart> getSloganParts()
	{
		return parts;
	}
	
	/**
	 * Adds a slogan part to the slogan
	 * 
	 * @param part the part to be added
	 */
	public void addSloganPart(SloganPart part)
	{
		parts.add(part);
	}
	
	/**
	 * Returns true, if there is already a slogan part like word in the part list. 
	 * 
	 * @param word the word, which is searched for in the list
	 * @return true, if word has been found
	 */
	public boolean contains(String word)
	{
		for(SloganPart part : parts)
		{
			if(part.getWord().equals(word))
				return true;
		}
		return false;
	}
	
	/**
	 * Getter for the separator
	 * @return the separator
	 */
	public String getSeparator()
	{
		if(listing)
		{
			return separator;
		}
		return "";
	}
	
	/**
	 * Overrides the toString method 
	 */
	@Override public String toString()
	{
		String slogan = "";
		for(SloganPart part : parts)
		{
			slogan += part.getWord() + getSeparator() + " ";
		}
		if(listing) // if listing remove also last separator
		{
			slogan = slogan.substring(0, slogan.length() - 2);
		}
		else // else remove just last space
		{
			slogan = slogan.substring(0, slogan.length() - 1);
		}
		return slogan;
	}
	
	/**
	 * Overrides the equals method
	 */
	@Override public boolean equals(Object obj)
	{
		try
		{
			Slogan s = (Slogan)obj;
			if(s.toString().toLowerCase().equals(toString().toLowerCase()))
			{
				return true;
			}
		}
		catch(Exception e)
		{
			System.out.println("Cast not possible!");
		}
		return false;
	}
	
}
