package de.tobiasloeser.slogangenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SloganTemplate implements java.io.Serializable {

	private static final long serialVersionUID = 6826801386024671394L;
	private String name;
	private String id;
	private List<TemplatePart> parts;
	private boolean alliteration;
	private boolean oxymoron;
	private boolean listing;
	private String separator;
	
	public SloganTemplate(String _id, String _name, boolean _alliteration, boolean _oxymoron, boolean _listing, String _separator)
	{
		name = _name;
		id = _id;
		parts = new ArrayList<TemplatePart>();
		alliteration = _alliteration;
		oxymoron = _oxymoron;
		listing =_listing;
		separator = _separator;
	}
	
	public void addTemplatePart(TemplatePart part)
	{
		parts.add(part);
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getId()
	{
		return id;
	}
	
	public List<TemplatePart> getTemplateParts()
	{
		Collections.sort(parts);
		return parts;
	}
	
	public boolean isAlliteration()
	{
		return alliteration;
	}
	
	public boolean isOxymoron()
	{
		return oxymoron;
	}

	public boolean isListing() {
		return listing;
	}

	public String getSeparator() {
		return separator;
	}
	
	@Override public String toString()
	{
		String template = "";
		template += "Name: " + getName();
		template += "\n Id: " + getId();
		template += "\n Alliteration: " + isAlliteration();
		template += "\n Listing: " + isListing();
		template += "\n Separator: " + getSeparator();
		template += "\n Parts: ";
		for(TemplatePart part : getTemplateParts())
		{
			template += "\n\t Position: " + part.getPosition(); 
			template += "\n\t Fixed: " + part.isFixed();
			template += "\n\t Repeatable: " + part.isRepeatable();
			template += "\n\t Synsets: ";
			for(String s : part.getSynsets())
			{
				template += "\n\t\t" + s;
			}
			template += "\n\t Words: ";
			for(String s : part.getWords())
			{
				template += "\n\t\t" + s;
			}
		}
		return template;
	}
	
	public void addSynset(String synset)
	{
		if(synset == null)
			return;
		for(TemplatePart p : parts)
		{
			p.addSynset(synset);
		}
	}
}
