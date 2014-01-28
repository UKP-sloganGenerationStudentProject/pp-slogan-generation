package de.tobiasloeser.slogangenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.tudarmstadt.ukp.lmf.api.Uby;
import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;
import de.tudarmstadt.ukp.lmf.model.core.LexicalEntry;
import de.tudarmstadt.ukp.lmf.model.core.Lexicon;
import de.tudarmstadt.ukp.lmf.model.core.Sense;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;
import de.tudarmstadt.ukp.lmf.model.semantics.Synset;
import de.tudarmstadt.ukp.lmf.transform.DBConfig;

public class SloganGenerator {
	
	private int alliterationChances = 30;
	private int resetchances = 30;
	private Random random;
	private Uby uby;
	private String alliteration;
	private int position;
	private boolean firstWord;
	private String productname;
	private boolean hasProductname;
	private boolean useProductnameCreative;
	private String emotion;
	private int emotionChances = 30;
	private List<Emotion> emotionList;
	private String database = "localhost/uby_medium_0_3_0";
	private String dbuser = "root";
	private String dbpassword = "";
	private Antonym antonym;
	private String oxymoronSynset;
	private Web1THelper web1tHelper;
	private String emotionPath;
	
	public SloganGenerator(String _database, String _dbuser, String _dbpassword, String _emotionPath)
	{
		emotionPath = _emotionPath;
		database = _database;
		dbuser = _dbuser;
		dbpassword = _dbpassword;
		Init();
	}
	
	public SloganGenerator()
	{
		Init();		
	}
	
	public List<String> getSynsetByWord(String word) throws UbyInvalidArgumentException
	{
		Lexicon lex;
		lex = uby.getLexiconByName("WordNet");
		List<LexicalEntry> entries = uby.getLexicalEntries(word, lex);
		if(entries == null || entries.size() == 0)
			return null;

		List<String> synsets = new ArrayList<String>();
		for(LexicalEntry entry : entries)
		{
			for(Synset synset : entry.getSynsets())
				synsets.add(synset.getId());
		}
		return synsets;
	}
		
	public void Init()
	{
		oxymoronSynset = null;
		hasProductname = false;
		random = new Random();
		DBConfig db = new DBConfig(database, "com.mysql.jdbc.Driver", "mysql", dbuser, dbpassword, true);
		try {
			uby = new Uby(db);
		} catch (UbyInvalidArgumentException e) {
			e.printStackTrace();
		}
		ImportExport ie = new ImportExport();
		emotionList = ie.importEmotions(emotionPath);
		try {
			antonym = new Antonym(uby);
		} catch (UbyInvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			web1tHelper = new Web1THelper(uby);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Slogan> generateSlogans(SloganTemplate template, int count)
	{
		List<Slogan> slogans = new ArrayList<Slogan>();
		while(slogans.size() < count)
		{
			Slogan slogan = generateSlogan(template);
			if(!slogans.contains(slogan))
			{
				slogans.add(slogan);
			}
		}
		return slogans;
	}
	
	public List<Slogan> generateSlogans(SloganTemplate template, int count, String _productname, boolean _useProductnameCreative, String _emotion)
	{
		useProductnameCreative = _useProductnameCreative;
		if(_productname != null)
			hasProductname = true;
		productname = _productname;
		emotion = _emotion;
		return generateSlogans(template, count);
	}

	/**
	 * Generates a slogan for the template. 
	 * 
	 * @param Template The template for the generated slogan
	 * @return An Object of the class Slogan, which represents a Slogan
	 */
	public Slogan generateSlogan(SloganTemplate template)
	{
		Slogan slogan = new Slogan(template.isListing(), template.getSeparator());
		position = 0;
		firstWord = true;
		for(TemplatePart part : template.getTemplateParts())
		{
			if(part.isRepeatable())
			{
				slogan = addMultipleWords(slogan, part);
				position += part.getRepeatableTimes();
			}
			else
			{
				slogan = addOneWord(slogan, part);
				position++;
			}
		}
		return slogan;
	}

	/**
	 * Adds one word to the slogan, which is suitable to the template part.
	 * 
	 * @param Slogan The slogan to which the word is added
	 * @param Position The position of the word in the slogan
	 * @param Part The template part which represents the template for the word
	 */
	private Slogan addOneWord(Slogan slogan, TemplatePart part) {
		String word;
		boolean onlyNoun = (getRandom(2) < 1) ? true : false;
		do{
			word = getWordByChunk(part, onlyNoun);
		} while(slogan.contains(word));
		slogan.addSloganPart(new SloganPart(word, position));
		return slogan;
	}

	/**
	 * Adds multiple words to the slogan. The words are suitable to the template.
	 * How many words are added depends on the value of repeatableTimes.
	 * 
	 * @param Slogan The slogan to which the word is added
	 * @param Position The position of the next word in the slogan
	 * @param Part The template part which represents the template for the words
	 * @return Current position after last added word
	 */
	private Slogan addMultipleWords(Slogan slogan, TemplatePart part) {
		String word;
		int repeated = 0;
		boolean onlyNoun = (getRandom(2) < 1) ? true : false;
		while(part.isRepeatable() && repeated < part.getRepeatableTimes())
		{
			word = getWordByChunk(part, onlyNoun);
			if(!slogan.contains(word))
			{
				slogan.addSloganPart(new SloganPart(word, position));
				position++;
				repeated++;
			}
		}
		return slogan;
	}
	
	private String getWordByChunk(TemplatePart part, boolean onlyNoun) {
		if(part.getChunk().toLowerCase().equals("nc"))
		{
			return getNC(part, onlyNoun);
		}
		else if(part.getChunk().toLowerCase().equals("vc"))
		{
			return getWordByPos(part, EPartOfSpeech.verb);
		}
		else
		{
			return "";
		}
	}

	/**
	 * Generates either an adjective plus a noun or just a noun by random.
	 * 
	 * @param Part The template part which represents the template for the words
	 * @return Either an adjective plus a noun or just a noun
	 */
	private String getNC(TemplatePart part, boolean onlyNoun) {
		if(!onlyNoun)
		{
			return getWordByPos(part, EPartOfSpeech.adjective) + " " + getWordByPos(part, EPartOfSpeech.noun);
		}
		else
		{
			if(hasProductname && getRandom(2) < 1)
			{
				return productname;
			}
			return getWordByPos(part, EPartOfSpeech.noun);
		}
	}
	
	private String getWordByPos(TemplatePart part, EPartOfSpeech pos)
	{
		// if product name shall be used as a POS and it is possible to use it as the searched POS
		// it is used with a chance of 50 %
		if(useProductnameCreative && isWordPos(productname, pos) && getRandom(2) < 1)
		{
			return productname;
		}
		
		String result = "";
		
		
		
		for(int i = emotionChances; i >= 0; i++)
		{
			if(part.getTemplate().isAlliteration() && !firstWord)
			{
				do{
					result = getWordAsString(part, pos);
					alliterationChances--;
				} while(!result.substring(0,1).equals(alliteration) && alliterationChances > 0);
				alliterationChances = resetchances;
			}
			else
			{
				result = getWordAsString(part, pos);
			}
			if(isDesiredEmotion(result))
			{
				break;
			}
		}
		emotionChances = resetchances;
		
		// if template is oxymoron, then save possible synset in oxymoronSynset for use in following words
		if(firstWord && part.getTemplate().isOxymoron())
		{
			oxymoronSynset = antonym.GetAntonymAsSynset(result);
		}
		else
		{
			// remove possible synset, because it already has been used
			oxymoronSynset = null;
		}
		
		
		
		// if template is alliteration, then save first character for following words
		if(firstWord && part.getTemplate().isAlliteration())
		{
			alliteration = result.substring(0,1);
		}
		// Set firstWord to false for alliteration
		firstWord = false;
		
		
		// Get a LexicalEntry by the given Synset by random and return lemma
		return result;
	}

	private String getWordAsString(TemplatePart part, EPartOfSpeech pos) 
	{
		Synset synset = new Synset();
		do {
			try {
				// if oxymoronSynset is null, use synset of part by random
				// else use antonym synset
				if(oxymoronSynset == null)
				{
					synset = uby.getSynsetById(part.getSynsets().get(getRandom(part.getSynsets().size())));
				}
				else
				{
					synset = uby.getSynsetById(oxymoronSynset);
				}
			} catch (UbyInvalidArgumentException e) {
				e.printStackTrace();
			}
		} while(!isSynsetPos(synset, pos));
		return synset.getSenses().get(getRandom(synset.getSenses().size())).getLexicalEntry().getLemmaForm();
	}
	
	private int getRandom(int max)
	{
		return random.nextInt(max);
	}
	
	/**
	 * Verifies that the synset consists of words with the given part of speech
	 * @param synset The synset to be verified
	 * @param pos The part of speech
	 * @return true, if the synset consists of word with the given part of speech
	 */
	private boolean isSynsetPos(Synset synset, EPartOfSpeech pos)
	{
		// if oxymoronSynset is used, ignore POS
		if(oxymoronSynset != null)
			return true;
		for(Sense sense : synset.getSenses())
		{
			if(sense.getLexicalEntry().getPartOfSpeech().equals(pos))
				return true;
			break;
		}
		return false;
	}
	
	private boolean isWordPos(String word, EPartOfSpeech pos)
	{
		try {
			Lexicon lex = uby.getLexiconByName("WordNet");
			for(LexicalEntry le : uby.getLexicalEntries(word, lex))
			{
				if(le.getPartOfSpeech().equals(pos))
					return true;
			}
		} catch (UbyInvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param word Word, which shall has the desired emotion
	 * @return true, if the word has the desired emotion
	 */
	private boolean isDesiredEmotion(String word)
	{
		if(emotion != null && emotionList != null)
		{
			for(Emotion e : emotionList)
			{
				if(e.getWord().equals(word))
				{
					if(e.is(emotion))
						return true;
				}
			}
			return false;
		}
		return true;
	}

}
