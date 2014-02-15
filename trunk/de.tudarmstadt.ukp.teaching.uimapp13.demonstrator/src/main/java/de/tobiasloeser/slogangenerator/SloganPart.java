package de.tobiasloeser.slogangenerator;

/**
 * This class represents a part of a slogan. 
 * 
 * @author tobias
 *
 */
public class SloganPart implements Comparable<SloganPart> {

	private String word;
	private int position;
	
	/**
	 * Constructor, which sets the word and its position. 
	 * 
	 * @param _word word, which represents the slogan part
	 * @param _position position for the word in the slogan
	 */
	public SloganPart(String _word, int _position)
	{
		word = _word;
		position = _position;
	}
	
	/**
	 * Getter for the word. 
	 * @return the word
	 */
	public String getWord()
	{
		return word;
	}
	
	/**
	 * Getter for the position
	 * @return the position
	 */
	public int getPosition()
	{
		return position;
	}

	/**
	 * Compares this slogan part with another given slogan part.
	 * 
	 */
	public int compareTo(SloganPart part) {
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
	 * Overrides the equals method. 
	 */
	@Override public boolean equals(Object obj)
	{
		try
		{
			SloganPart s = (SloganPart)obj;
			if(s.getWord().toLowerCase().equals(getWord().toLowerCase()))
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
