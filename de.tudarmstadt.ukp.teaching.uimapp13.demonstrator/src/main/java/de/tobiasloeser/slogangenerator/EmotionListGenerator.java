package de.tobiasloeser.slogangenerator;

import java.util.List;

public class EmotionListGenerator {
	
	// Input: Path to Emotion Lexicon, Path to store Emotion List
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
