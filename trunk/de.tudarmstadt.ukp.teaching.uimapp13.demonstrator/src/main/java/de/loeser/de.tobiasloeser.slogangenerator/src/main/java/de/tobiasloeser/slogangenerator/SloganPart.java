package de.tobiasloeser.slogangenerator;

public class SloganPart implements Comparable<SloganPart> {

	private String word;
	private int position;
	
	public SloganPart(String _word, int _position)
	{
		word = _word;
		position = _position;
	}
	
	public String getWord()
	{
		return word;
	}
	
	public int getPosition()
	{
		return position;
	}

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
