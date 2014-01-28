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


// This class tries to add more synsets to the Slogan Template

public class GoodLuck {
	
	private Uby uby;
	Lexicon lexVerbNet;
	Lexicon lexWordNet;
	
	private Connection connection;

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
