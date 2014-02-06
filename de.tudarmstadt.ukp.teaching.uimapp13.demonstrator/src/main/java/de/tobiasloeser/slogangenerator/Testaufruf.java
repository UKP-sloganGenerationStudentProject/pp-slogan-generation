package de.tobiasloeser.slogangenerator;

import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;

public class Testaufruf
{

    public static void main(final String[] args)
    {
        final SGConfig sgc = new SGConfig();
        sgc.DBUrl = "localhost/uby_medium_0_3_0";
        sgc.DBUser = "root";
        sgc.DBPassword = "";
        sgc.TemplateId = 9;
        // sgc.WordList.add("Car");
        sgc.SloganCount = 10;
        sgc.Web1TPath = "C:/Users/tobias/workspace/de.tobiasloeser.slogangenerator/web1t/ENGLISH";
        sgc.EmotionPath = "C:/Users/tobias/workspace/de.tobiasloeser.slogangenerator/src/main/resources/Emotionlist.sg";
        try {
            Main.main(sgc);
        }
        catch (final UbyInvalidArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
