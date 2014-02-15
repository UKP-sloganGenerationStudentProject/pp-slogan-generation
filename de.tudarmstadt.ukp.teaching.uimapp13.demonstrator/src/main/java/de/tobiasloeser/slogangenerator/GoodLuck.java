package de.tobiasloeser.slogangenerator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.lmf.api.Uby;
import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;
import de.tudarmstadt.ukp.lmf.model.core.LexicalEntry;
import de.tudarmstadt.ukp.lmf.model.core.Lexicon;
import de.tudarmstadt.ukp.lmf.model.core.Sense;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;
import de.tudarmstadt.ukp.lmf.model.meta.SemanticLabel;
import de.tudarmstadt.ukp.lmf.model.semantics.Synset;
import de.tudarmstadt.ukp.lmf.transform.DBConfig;


/**
 *  This class tries to add more synsets of verbs to the Slogan Templates.
 * 
 * @author tobias
 *
 */

public class GoodLuck {
	
	private Uby uby;
	Lexicon lexVerbNet;
	Lexicon lexWordNet;
	private Connection connection;

	/**
	 * Constructor which initializes UBY and the lexicons VerbNet and WordNet
	 * 
	 * @param database URL to the database
	 * @param dbuser database user
	 * @param dbpassword database password
	 */
	public GoodLuck(String database, String dbuser, String dbpassword)
	{
		{
			DBConfig db = new DBConfig(database, "com.mysql.jdbc.Driver", "mysql", dbuser, dbpassword, true);
			try {
				uby = new Uby(db);
				lexVerbNet = uby.getLexiconByName("VerbNet");
				lexWordNet = uby.getLexiconByName("WordNet");
			} catch (UbyInvalidArgumentException e) {
				e.printStackTrace();
			}
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + database + "?" + "user=" + dbuser + "&password=" + dbpassword);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This function goes through all templates, all template parts and all synsets. 
	 * If it is a verb, it tries to find more verbs with the same semantic label and adds them to the template part.
	 * 
	 * @param templates a list of templates, to add more verb synsets
	 * @return templates list of templates with new verb synsets 
	 * @throws UbyInvalidArgumentException
	 */
	public List<SloganTemplate> AddMoreVerbs(List<SloganTemplate> templates) throws UbyInvalidArgumentException
	{
		// For every Template
		for(SloganTemplate template : templates)
		{
			// For every part of the template
			for(TemplatePart part : template.getTemplateParts())
			{
				// For every Synset of the part
				for(String synsetString : part.getSynsets())
				{
					// For every Sense of the synset
					for(Sense sense : uby.getSynsetById(synsetString).getSenses())
					{
						// If the synset has verbs
						if(sense.getLexicalEntry().getPartOfSpeech().equals(EPartOfSpeech.verb))
						{
							// Get all verbs from uby, but this time with VerbNet
							List<LexicalEntry> entries = uby.getLexicalEntries(sense.getLexicalEntry().getLemmaForm(), lexVerbNet);
							// For every found entry
							for(LexicalEntry entry : entries)
							{
								// For every Sense
								for(Sense verbSense : entry.getSenses())
								{
									// For every semantic label
									for(SemanticLabel semLab : verbSense.getSemanticLabels())
									{
										if(getSynsetsBySemanticLabel(semLab.getLabel()) != null)
										{
											for(String newSynset : getSynsetsBySemanticLabel(semLab.getLabel()))
												part.addSynset(newSynset);
										}
										
									}
								}
							}
						}
						
					}
				}
			}
		}
		return templates;
	}
	
	/**
	 * Same as AddMoreVerbs with a list but needed for only one template
	 * @param template the template
	 * @return the template with more verbs
	 * @throws UbyInvalidArgumentException
	 */
	public SloganTemplate AddMoreVerbs(SloganTemplate template) throws UbyInvalidArgumentException
	{
		List<SloganTemplate> templates = new ArrayList<SloganTemplate>();
		templates = this.AddMoreVerbs(templates);
		for(SloganTemplate templateWithMoreVerbs : templates)
			return templateWithMoreVerbs;
		return template;
	}
	
	/**
	 * This function searches synsets with the given semantic label
	 * 
	 * @param semanticLabel the semantic label
	 * @return a list with synsets as string
	 */
	private List<String> getSynsetsBySemanticLabel(String semanticLabel)
	{
		List<String> senses = new ArrayList<String>();
		List<String> synsets = new ArrayList<String>();
		try {
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery("SELECT senseId FROM semanticlabel WHERE label = '"+ semanticLabel +"'");
			while(results.next())
				senses.add(results.getString("senseId"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(String senseId : senses)
		{
			try {
				Sense sense = uby.getSenseById(senseId);
				if(sense != null)
				{
					Synset synset = sense.getSynset();
					if(synset != null)
					{
						synsets.add(synset.getId());
					}
				}
			} catch (UbyInvalidArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return synsets;
	}
	
}
