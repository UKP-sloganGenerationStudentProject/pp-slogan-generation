package de.tobiasloeser.slogangenerator;

import java.util.List;

/**
 * A simple class for generating the emotion list.
 * 
 * @author tobias
 *
 */
public class EmotionListGenerator {
	
	/**
	 * The methodm which generates the emotion list.
	 * 
	 * @param args Array of string which needs the path to the NRC Emotion Lexicon in index1 and the path to store the emotion list in index 2
	 */
	public static void main(final String[] args)
    {
		String emotionLexiconPath = args[0];
		String pathToStore = args[1];
		textextraction te = new textextraction();
		List<Emotion> emotionList = te.getEmotionsByPath(emotionLexiconPath);
		ImportExport ie = new ImportExport();
		ie.exportEmotions(emotionList, pathToStore);
    }

}
