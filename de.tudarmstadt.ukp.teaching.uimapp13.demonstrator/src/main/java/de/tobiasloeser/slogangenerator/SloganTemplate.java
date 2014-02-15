package de.tobiasloeser.slogangenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a slogan template. 
 * 
 * @author tobias
 *
 */
public class SloganTemplate implements java.io.Serializable {

	private static final long serialVersionUID = 6826801386024671394L;
	private String name;
	private String id;
	private List<TemplatePart> parts;
	private boolean alliteration;
	private boolean oxymoron;
	private boolean listing;
	private String separator;
	
	/**
	 * Constructor, which sets the id, name and separator of the slogan. 
	 * 
	 * @param _id id of the slogan
	 * @param _name name of the slogan
	 * @param _alliteration defines, whether the slogan is an alliteration
	 * @param _oxymoron defines, whether the slogan is an oxymoron
	 * @param _listing defines, whether the slogan is a listing
	 * @param _separator separator of the slogan
	 */
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
	
	/**
	 * Adds a template part to the slogan template
	 * @param part
	 */
	public void addTemplatePart(TemplatePart part)
	{
		parts.add(part);
	}
	
	/**
	 * Getter for the name of the slogan
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Getter for the id of the slogan
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}
	
	/**
	 * Getter for the template parts
	 * @return list of template parts
	 */
	public List<TemplatePart> getTemplateParts()
	{
		Collections.sort(parts);
		return parts;
	}
	
	/**
	 * Getter for alliteration setting
	 * 
	 * @return true, if alliteration is used
	 */
	public boolean isAlliteration()
	{
		return alliteration;
	}
	
	/**
	 * Getter for oxymoron setting
	 * @return true, if oxymoron is used
	 */
	public boolean isOxymoron()
	{
		return oxymoron;
	}

	/**
	 * Getter for listing setting
	 * @return true, if slogan is listing
	 */
	public boolean isListing() {
		return listing;
	}

	/**
	 * Getter for separator
	 * @return the separator
	 */
	public String getSeparator() {
		return separator;
	}
	
	/**
	 * Overrides the toString method
	 */
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
	
	/**
	 * Adds a synset to the template parts. 
	 * @param synset synset to be added to the template parts
	 */
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
