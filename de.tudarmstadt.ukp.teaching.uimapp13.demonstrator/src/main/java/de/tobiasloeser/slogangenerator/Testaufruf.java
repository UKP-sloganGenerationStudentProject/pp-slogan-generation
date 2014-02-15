package de.tobiasloeser.slogangenerator;

import java.util.List;

public class Testaufruf
{

    public static void main(final String[] args)
    {
        final SGConfig sgc = new SGConfig();
        sgc.DBUrl = "localhost/uby_medium_0_3_0";
        sgc.DBUser = "root";
        sgc.DBPassword = "";
        sgc.TemplateId = 9;
        sgc.SloganCount = 10;
        sgc.Web1TPath = "C:/Users/tobias/workspace/de.tobiasloeser.slogangenerator/web1t/ENGLISH";
        sgc.EmotionPath = "C:/Users/tobias/workspace/de.tobiasloeser.slogangenerator/src/main/resources/Emotionlist.sg";
        SloganGenerator generator = new SloganGenerator(sgc);
		List<Slogan> slogans = generator.generateSlogans(sgc);
		for(Slogan slogan : slogans)
		{
			System.out.println(slogan.toString());
		}
    }
}
