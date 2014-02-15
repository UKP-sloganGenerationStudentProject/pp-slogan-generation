package de.tobiasloeser.slogangenerator;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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

public class SloganGenerator
    implements Serializable
{
    private static final long serialVersionUID = 8708478297667499513L;
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
    public String database = "localhost/uby_medium_0_3_0";
    public String dbuser = "root";
    public String dbpassword = "";
    private Antonym antonym;
    private String oxymoronSynset;
    private Web1THelper web1tHelper;
    public String emotionPath;
    public String web1TPath;

    /**
     * Possible Constructor for manual settings
     * @param _database url of the database
     * @param _dbuser user of the database
     * @param _dbpassword password for the database
     * @param _emotionPath path to the emotion list
     * @param _web1TPath path to Web1T
     */
    public SloganGenerator(final String _database, final String _dbuser, final String _dbpassword,
            final String _emotionPath, final String _web1TPath)
    {
        this.web1TPath = _web1TPath;
        this.emotionPath = _emotionPath;
        this.database = _database;
        this.dbuser = _dbuser;
        this.dbpassword = _dbpassword;
        this.Init();
    }

    /**
     * Constructor for the use of the config object
     * @param config SGConfig object
     */
    public SloganGenerator(SGConfig config)
    {
        this.web1TPath = config.Web1TPath;
        this.emotionPath = config.EmotionPath;
        this.database = config.DBUrl;
        this.dbuser = config.DBUser;
        this.dbpassword = config.DBPassword;
        this.Init();    	
    }
    
    public SloganGenerator()
    {
        this.Init();
    }

    public List<String> getSynsetByWord(final String word)
        throws UbyInvalidArgumentException
    {
        Lexicon lex;
        lex = this.uby.getLexiconByName("WordNet");
        final List<LexicalEntry> entries = this.uby.getLexicalEntries(word, lex);
        if (entries == null || entries.size() == 0) {
            return null;
        }

        final List<String> synsets = new ArrayList<String>();
        for (final LexicalEntry entry : entries) {
            for (final Synset synset : entry.getSynsets()) {
                synsets.add(synset.getId());
            }
        }
        return synsets;
    }

    /**
     * Initializes the slogan generator, lists and Uby. 
     */
    public void Init()
    {
        this.oxymoronSynset = null;
        this.hasProductname = false;
        this.random = new Random();
        final DBConfig db = new DBConfig(this.database, "com.mysql.jdbc.Driver", "mysql",
                this.dbuser, this.dbpassword, true);
        try {
            this.uby = new Uby(db);
        }
        catch (final UbyInvalidArgumentException e) {
            e.printStackTrace();
        }
        final ImportExport ie = new ImportExport();

        if(this.emotionPath != null && this.emotionPath != "")
        {
        	this.emotionList = ie.importEmotions(this.emotionPath);
        }
        try {
            this.antonym = new Antonym(this.uby);
        }
        catch (final UbyInvalidArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(this.web1TPath != null && this.web1TPath != "")
        {
	        try {
	            final File web1TFolder = new File(this.web1TPath);
	            this.web1tHelper = new Web1THelper(this.uby, web1TFolder);
	        }
	        catch (final IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
        }
    }

    /**
     * another possible start point to generate slogans, but without any settings except the template and the count
     * @param template the slogan template
     * @param count the count of slogans to be generated
     * @return
     */
    public List<Slogan> generateSlogans(final SloganTemplate template, final int count)
    {
        final List<Slogan> slogans = new ArrayList<Slogan>();
        while (slogans.size() < count) {
            final Slogan slogan = this.generateSlogan(template);
            if (!slogans.contains(slogan)) {
                slogans.add(slogan);
            }
        }
        return slogans;
    }

    /**
     * The same like generateSlogans, but for the use of the config object. 
     * @param config the SGConfig object
     * @return a list of slogans
     */
    public List<Slogan> generateSlogans(SGConfig config)
    {
    	SloganTemplate template = TemplateGenerator.getTemplateById(config.TemplateId);
        for (final String word : config.WordList) {
            try {
				for (final String synset : this.getSynsetByWord(word)) {
				    template.addSynset(synset);
				}
			} catch (UbyInvalidArgumentException e) {
				e.printStackTrace();
			}
        }
    	
    	return generateSlogans(TemplateGenerator.getTemplateById(config.TemplateId), 
    			config.SloganCount, config.ProductName, 
    			config.UseProductNameCreative, config.Emotion, config.GoodLuck);    	
    }
    
    /**
     *  start point for the generation process. 
     *  
     * @param template the slogan template
     * @param count the count of slogans to be generated
     * @param _productname a product name
     * @param _useProductnameCreative true, if product name can be used creatively
     * @param _emotion an emotion
     * @return a list of slogans
     */
    public List<Slogan> generateSlogans(SloganTemplate template, final int count,
            final String _productname, final boolean _useProductnameCreative, final String _emotion, boolean goodLuck)
    {
        this.useProductnameCreative = _useProductnameCreative;
        if (_productname != null) {
            this.hasProductname = true;
        }
        this.productname = _productname;
        this.emotion = _emotion;
        if(goodLuck)
        {
        	final GoodLuck gl = new GoodLuck(this.database, this.dbuser, this.dbpassword);
        	try {
				template = gl.AddMoreVerbs(template);
			} catch (UbyInvalidArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return this.generateSlogans(template, count);
    }

    /**
     * Generates a slogan for the template. 
     * 
     * @param template The template for the generated slogan
     * @return An Object of the class Slogan, which represents a Slogan
     */
    public Slogan generateSlogan(final SloganTemplate template)
    {
    	if(template == null)
    		return null;
        Slogan slogan = new Slogan(template.isListing(), template.getSeparator());
        this.position = 0;
        this.firstWord = true;
        for (final TemplatePart part : template.getTemplateParts()) {
            if (part.isRepeatable()) {
                slogan = this.addMultipleWords(slogan, part);
                this.position += part.getRepeatableTimes();
            }
            else {
                slogan = this.addOneWord(slogan, part);
                this.position++;
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
    private Slogan addOneWord(final Slogan slogan, final TemplatePart part)
    {
        String word;
        final boolean onlyNoun = (this.getRandom(2) < 1) ? true : false;
        do {
            word = this.getWordByChunk(slogan, part, onlyNoun);
        }
        while (slogan.contains(word));
        slogan.addSloganPart(new SloganPart(word, this.position));
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
    private Slogan addMultipleWords(final Slogan slogan, final TemplatePart part)
    {
        String word;
        int repeated = 0;
        final boolean onlyNoun = (this.getRandom(2) < 1) ? true : false;
        while (part.isRepeatable() && repeated < part.getRepeatableTimes()) {
            word = this.getWordByChunk(slogan, part, onlyNoun);
            if (!slogan.contains(word)) {
                slogan.addSloganPart(new SloganPart(word, this.position));
                this.position++;
                repeated++;
            }
        }
        return slogan;
    }

    /**
     * Splits the chunk in part of speeches
     * 
     * @param slogan current slogan to generate
     * @param part current part of the template
     * @param onlyNoun if nc chunk and only noun, then true, else false and adjective plus noun
     * @return the word, which represents the part of the slogan
     */
    private String getWordByChunk(final Slogan slogan, final TemplatePart part,
            final boolean onlyNoun)
    {
        if (part.getChunk().toLowerCase().equals("nc")) {
            return this.getNC(part, onlyNoun);
        }
        else if (part.getChunk().toLowerCase().equals("vc")) {
            return this.getWordByPos(part, EPartOfSpeech.verb);
        }
        else if (part.getChunk().toLowerCase().equals("pcnc")) {
            String word;
            do {
                word = this.getWordByPos(part, EPartOfSpeech.noun);
            }
            while (slogan.contains(word));
            if(web1tHelper != null)
            	return this.web1tHelper.getPrepositionAndWord(word);
            return word;
        }
        else {
            return "";
        }
    }

    /**
     * Generates either an adjective plus a noun or just a noun by random.
     * 
     * @param Part The template part which represents the template for the words
     * @return Either an adjective plus a noun or just a noun
     */
    private String getNC(final TemplatePart part, final boolean onlyNoun)
    {
        if (!onlyNoun) {
            return this.getWordByPos(part, EPartOfSpeech.adjective) + " "
                    + this.getWordByPos(part, EPartOfSpeech.noun);
        }
        if (this.hasProductname && this.getRandom(2) < 1) {
            return this.productname;
        }
        return this.getWordByPos(part, EPartOfSpeech.noun);
    }

    /**
     * Searches for a possible word for a given part of speech, but with conditions like alliteration, oxymoron and so on
     * @param part current template part
     * @param pos needed part of speech
     * @return possible word as string
     */
    private String getWordByPos(final TemplatePart part, final EPartOfSpeech pos)
    {
        // if product name shall be used as a POS and it is possible to use it as the searched POS
        // it is used with a chance of 50 %
        if (this.useProductnameCreative && this.isWordPos(this.productname, pos)
                && this.getRandom(2) < 1) {
            return this.productname;
        }

        String result = "";

        for (int i = this.emotionChances; i >= 0; i++) {
            if (part.getTemplate().isAlliteration() && !this.firstWord) {
                do {
                    result = this.getWordAsString(part, pos);
                    this.alliterationChances--;
                }
                while (!result.substring(0, 1).equals(this.alliteration)
                        && this.alliterationChances > 0);
                this.alliterationChances = this.resetchances;
            }
            else {
                result = this.getWordAsString(part, pos);
            }
            if (this.isDesiredEmotion(result)) {
                break;
            }
        }
        this.emotionChances = this.resetchances;

        // if template is oxymoron, then save possible synset in oxymoronSynset for use in following
        // words
        if (this.firstWord && part.getTemplate().isOxymoron()) {
            this.oxymoronSynset = this.antonym.GetAntonymAsSynset(result);
        }
        else {
            // remove possible synset, because it already has been used
            this.oxymoronSynset = null;
        }

        // if template is alliteration, then save first character for following words
        if (this.firstWord && part.getTemplate().isAlliteration()) {
            this.alliteration = result.substring(0, 1);
        }
        // Set firstWord to false for alliteration
        this.firstWord = false;

        // Get a LexicalEntry by the given Synset by random and return lemma
        return result;
    }

    /**
     * Simple method to get a word which is a given part of speech
     * 
     * @param part current template part
     * @param pos needed part of speech
     * @return found word as string
     */
    private String getWordAsString(final TemplatePart part, final EPartOfSpeech pos)
    {
        Synset synset = new Synset();
        do {
            try {
                // if oxymoronSynset is null, use synset of part by random
                // else use antonym synset
                if (this.oxymoronSynset == null) {
                    synset = this.uby.getSynsetById(part.getSynsets().get(
                            this.getRandom(part.getSynsets().size())));
                }
                else {
                    synset = this.uby.getSynsetById(this.oxymoronSynset);
                }
            }
            catch (final UbyInvalidArgumentException e) {
                e.printStackTrace();
            }
        }
        while (!this.isSynsetPos(synset, pos));
        return synset.getSenses().get(this.getRandom(synset.getSenses().size())).getLexicalEntry()
                .getLemmaForm();
    }

    /**
     * Simple method to get a random integer, with 0 <= x < max.
     * @param max integer under which the result has to be
     * @return random integer
     */
    private int getRandom(final int max)
    {
        return this.random.nextInt(max);
    }

    /**
     * Verifies that the synset consists of words with the given part of speech
     * @param synset The synset to be verified
     * @param pos The part of speech
     * @return true, if the synset consists of word with the given part of speech
     */
    private boolean isSynsetPos(final Synset synset, final EPartOfSpeech pos)
    {
        // if oxymoronSynset is used, ignore POS
        if (this.oxymoronSynset != null) {
            return true;
        }
        for (final Sense sense : synset.getSenses()) {
            if (sense.getLexicalEntry().getPartOfSpeech().equals(pos)) {
                return true;
            }
            break;
        }
        return false;
    }

    /**
     * Checks, whether a given word is a given part of speech
     * @param word given word
     * @param pos given part of speech
     * @return true, if word is part of speech
     */
    private boolean isWordPos(final String word, final EPartOfSpeech pos)
    {
        try {
            final Lexicon lex = this.uby.getLexiconByName("WordNet");
            for (final LexicalEntry le : this.uby.getLexicalEntries(word, lex)) {
                if (le.getPartOfSpeech().equals(pos)) {
                    return true;
                }
            }
        }
        catch (final UbyInvalidArgumentException e) {
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
    private boolean isDesiredEmotion(final String word)
    {
        if (this.emotion != null && this.emotionList != null) {
            for (final Emotion e : this.emotionList) {
                if (e.getWord().equals(word)) {
                    if (e.is(this.emotion)) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

}
