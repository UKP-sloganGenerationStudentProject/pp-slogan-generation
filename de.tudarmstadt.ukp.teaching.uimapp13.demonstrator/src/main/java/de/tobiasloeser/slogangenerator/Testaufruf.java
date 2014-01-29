package de.tobiasloeser.slogangenerator;

import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;

public class Testaufruf {

	public static void main(String[] args)
	{
		SGConfig sgc = new SGConfig();
		sgc.DBUrl = "localhost/uby_medium_0_3_0";
		sgc.DBUser = "root";
		sgc.DBPassword = "";
		sgc.TemplateId = 2;
		sgc.WordList.add("Car");
		sgc.SloganCount = 10;
		try {
			Main.main(sgc);
		} catch (UbyInvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
