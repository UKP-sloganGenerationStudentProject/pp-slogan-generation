package de.tobiasloeser.slogangenerator;

import java.util.ArrayList;
import java.util.List;

public class SGConfig
{
    /*
    * Template Id: 
    1: NC with Alliteration
    2: NC without Alliteration
    3: VC NC without Alliteration
    4: VC NC with Alliteration
    5: NC VC NC without Alliteration
    6: NC VC NC with Alliteration
    7: NC with Oxymoron
    8: ?
    */
    public int TemplateId;
    public int SloganCount;
    public List<String> WordList;
    public String Web1TPath;
    public String DBUser;
    public String DBPassword;
    public String DBUrl;
    public String ProductName;
    public boolean UseProductNameCreative;
    public String Emotion;
    public boolean GoodLuck; // More verbs with semantic label
    public String EmotionPath;

    public SGConfig()
    {
        this.WordList = new ArrayList<String>();
    }

}
