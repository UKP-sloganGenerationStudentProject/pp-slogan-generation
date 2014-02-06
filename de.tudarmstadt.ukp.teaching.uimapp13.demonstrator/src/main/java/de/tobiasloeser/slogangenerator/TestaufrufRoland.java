package de.tobiasloeser.slogangenerator;

import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.configuration.DemonstratorConfig;

public class TestaufrufRoland
{

    public static void main(final String[] args)
    {
        final DemonstratorConfig demoConfig = DemonstratorConfig.getInstance();
        final SGConfig sgc = new SGConfig();
        sgc.DBUrl = demoConfig.getUbyUrl();
        sgc.DBUser = demoConfig.getUbyUser();
        sgc.DBPassword = demoConfig.getUbyPassword();
        sgc.TemplateId = 9;
        // sgc.WordList.add("Car");
        sgc.SloganCount = 10;
        sgc.Web1TPath = demoConfig.getWeb1TPath();
        sgc.EmotionPath = demoConfig.getSerializedEmotionssPath();
        try {
            Main.main(sgc);
        }
        catch (final UbyInvalidArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
