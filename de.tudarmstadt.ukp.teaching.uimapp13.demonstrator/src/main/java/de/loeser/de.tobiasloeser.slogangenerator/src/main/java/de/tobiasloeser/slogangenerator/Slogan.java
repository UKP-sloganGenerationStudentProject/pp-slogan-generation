package de.tobiasloeser.slogangenerator;

import java.util.ArrayList;
import java.util.List;

public class Slogan {
	
	private List<SloganPart> parts;
	private boolean listing;
	private String separator;
	
	public Slogan(boolean _listing, String _separator)
	{
		parts = new ArrayList<SloganPart>();
		listing = _listing;
		separator = _separator;
	}
	
	public List<SloganPart> getSloganParts()
	{
		return parts;
	}
	
	public void addSloganPart(SloganPart part)
	{
		parts.add(part);
	}
	
	public boolean contains(String word)
	{
		for(SloganPart part : parts)
		{
			if(part.getWord().equals(word))
				return true;
		}
		return false;
	}
	
	public String getSeparator()
	{
		if(listing)
		{
			return separator;
		}
		else
		{
			return "";
		}
	}
	
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
