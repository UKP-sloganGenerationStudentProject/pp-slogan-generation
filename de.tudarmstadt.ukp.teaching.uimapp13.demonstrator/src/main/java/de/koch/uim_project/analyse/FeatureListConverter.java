package de.koch.uim_project.analyse;

import de.koch.uim_project.generation.Word;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.koch.uim_project.main.Main;

import org.apache.log4j.Logger;

import de.koch.uim_project.database.DbException;
import de.tudarmstadt.ukp.lmf.api.Uby;
import de.tudarmstadt.ukp.lmf.model.core.LexicalEntry;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;

public class FeatureListConverter {

	private static FeatureListConverter instance = null;
	private static Logger log = Logger.getRootLogger();

	private FeatureListConverter() {

	}

	public static FeatureListConverter getInstance() {
		if (instance == null) {
			instance = new FeatureListConverter();
		}
		return instance;
	}

	public Set<Word> transformFeatureList(Set<String> words,Uby uby) {
		Set<Word> result = new HashSet<Word>();
		for (String str : words) {
			try {
			List<LexicalEntry> entries = UbyAnalyser.getInstance().getLexicalEntries(str.toLowerCase(), null, null,uby);
			for (LexicalEntry entry : entries) {
				if (entry.getLemmaForm() != null && !entry.getLemmaForm().matches("\\s*")
						&& (entry.getPartOfSpeech() == EPartOfSpeech.noun || entry.getPartOfSpeech() == EPartOfSpeech.verb || entry.getPartOfSpeech() == EPartOfSpeech.adjective)) {
					result.add( new Word(entry.getLemmaForm().toLowerCase(), entry.getPartOfSpeech(),true));
				}
			}
			} catch (DbException e) {
				log.error("Couldn't extract LexicalEntry for feature word: " + str.toLowerCase(), e);
				Main.writeToConsole("Couldn\'t process (alien)feature word: " + str.toLowerCase());
			}

		}
		return result;
	}
}
