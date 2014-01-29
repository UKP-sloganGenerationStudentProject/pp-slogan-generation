package de.koch.uim_project.generation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import de.koch.uim_project.analyse.FeatureListConverter;
import de.koch.uim_project.analyse.UbyAnalyser;
import de.koch.uim_project.database.DbException;
import de.koch.uim_project.database.JdbcConnect;
import de.koch.uim_project.generation.exception.NoMorGenerationPossibleException;
import de.koch.uim_project.util.WordlistGenConfig;
import de.tudarmstadt.ukp.lmf.api.Uby;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;
import de.tudarmstadt.ukp.lmf.model.semantics.Synset;

public class BaseWordListGen {

	private ArrayList<Set<Word>> wordLists = new ArrayList<Set<Word>>();
	private Set<Synset> visitedSynsets;
	private WordlistGenConfig config;
	private Uby uby;
	private JdbcConnect jdbcConnect;

	public BaseWordListGen(WordlistGenConfig config, Uby uby, JdbcConnect jdbcConnect) throws DbException {

		this.config = config;
		this.uby = uby;
		this.jdbcConnect = jdbcConnect;

		wordLists.add(jdbcConnect.getEmotionBatch(FeatureListConverter.getInstance().transformFeatureList(config.getFeatureList(), uby)));

		if (!config.getName().matches("\\s*") && !config.getName().equals("No Name")) {
			wordLists.get(0).add((new Word(config.getName(), EPartOfSpeech.noun, true)));
		}
		visitedSynsets = new HashSet<Synset>();

	}

	private Set<Word> generateMore() throws DbException, NoMorGenerationPossibleException {

		if (this.getWordCount() > config.getMaxWordlist() || this.wordLists.size() > config.getMaxSynsetDepth()) {
			throw new NoMorGenerationPossibleException();
		}
		Set<Word> result = new HashSet<Word>();
		Set<Synset> canidates = new HashSet<Synset>();
		canidates.addAll(UbyAnalyser.getInstance().generateSynsetsFromWords(wordLists.get(wordLists.size() - 1), uby));
		canidates.removeAll(visitedSynsets);
		visitedSynsets.addAll(canidates);
		result.addAll(UbyAnalyser.getInstance().retrieveWordsFromSynsets(canidates));

		for (Set<Word> wSet : wordLists) {
			result.removeAll(wSet);
		}

		wordLists.add(jdbcConnect.getEmotionBatch(result));

		return result;
	}

	public int getWordCount() {
		int result = 0;
		for (Set<Word> set : this.wordLists) {
			result += set.size();
		}
		return result;
	}

	public Set<Word> getMore(int synsetDepth) throws DbException, NoMorGenerationPossibleException {
		if (wordLists.size() <= synsetDepth) {
			return generateMore();
		} else {
			return wordLists.get(synsetDepth);
		}
	}

	public Set<Word> getInitialSet() {
		return wordLists.get(0);
	}

	public boolean generatesEqualList(WordlistGenConfig config) {
		if (this.config.equals(config)) {
			return true;
		}
		return false;
	}

	public void setUby(Uby uby) {
		this.uby = uby;
	}

	public void setJdbcConnect(JdbcConnect jdbcConnect) {
		this.jdbcConnect = jdbcConnect;
	}

}
