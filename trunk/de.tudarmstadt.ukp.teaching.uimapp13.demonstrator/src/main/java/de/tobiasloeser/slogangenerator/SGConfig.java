package de.tobiasloeser.slogangenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * An object of this class represents a config object for the slogan generator.  
 * @author tobias
 *
 */
public class SGConfig
    implements Serializable
{
    private static final long serialVersionUID = 4961171719238442443L;

    // Generation settings
    /*
    Template Id: 
    1: NC with Alliteration
    2: NC without Alliteration
    3: VC NC without Alliteration
    4: VC NC with Alliteration
    5: NC VC NC without Alliteration
    6: NC VC NC with Alliteration
    7: NC with Oxymoron
    8: NC PC NC without Alliteration
	9: NC PC NC with Alliteration
    */
    public int SloganCount;
    public String ProductName;

    public boolean UseProductNameCreative;
    public boolean GoodLuck; // More verbs with semantic label
    public int TemplateId;
    public String Emotion;

    public List<String> WordList;

    // Backend settings
    public String EmotionPath;
    public String Web1TPath;
    public String DBUser;
    public String DBPassword;
    public String DBUrl;

    /**
     * Constructor , which initializes the the word list. 
     */
    public SGConfig()
    {
        this.WordList = new ArrayList<String>();
    }

}
