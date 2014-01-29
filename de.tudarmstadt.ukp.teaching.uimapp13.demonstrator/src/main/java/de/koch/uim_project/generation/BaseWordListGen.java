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

/**
 * This class generates the basis list of {@link Word}s to use for generation.
 * It generates on demand and buffers {@link Word}s as long as necessary. Via
 * generatesEqualList one can check if for a new {@link WordlistGenConfig} a new
 * {@link BaseWordListGen} has to be initialized. If not it is advisable to
 * reuse the existing one to avoid regenerating the basis list. Generation is
 * limited through the configuration parameters maxWordListSize and maxSynsetDepth
 * MaxSynsetDepth gives the synset distance a word may have to be in the list.
 * MaxWordListSize stops the generation if the word list gets to big. 
 * ATTENTION:
 * MaxWordListSize is only a threshold. There may be many more words in basic
 * list. Every synset iteration is finished it just does not happen if there are
 * already to much words.
 * 
 * @author Frerik Koch
 * 
 */
public class BaseWordListGen {

	/**
	 * Each element of the list contains the words with the synset distance of
	 * the ID of the element So wordlists.get(0) contains the feature words (no
	 * synset distance), wordLists.get(1) contains the words in the same synset
	 * as the feature words, and so on
	 */
	private ArrayList<Set<Word>> wordLists = new ArrayList<Set<Word>>();
	private Set<Synset> visitedSynsets;
	private WordlistGenConfig config;
	private Uby uby;
	private JdbcConnect jdbcConnect;

	/**
	 * Initializes a new {@link BaseWordListGen}
	 * ATTENTION: Prefer reuse of "old" {@link BaseWordListGen} if possible
	 * @param config Configuration values for {@link BaseWordListGen}
	 * @param uby Uby connection.
	 * @param jdbcConnect Custom DB connection
	 * @throws DbException
	 */
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

	/**This method generates the next synset iteration. 
	 * This is done by retrieving all synsets for all words of the last iteration.
	 * Of this set of synsets all synsets are removed which were already visited.
	 * The words contained in the remaining synsets are added to this iterations result if they were not in an earlier iteration.
	 * @return
	 * @throws DbException
	 * @throws NoMorGenerationPossibleException Some maximum value is hit. The list is to big (maxWordList) or the synset depth has reached its limit
	 */
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

	/**
	 * @return total size of the word list of this {@link BaseWordListGen}
	 */
	public int getWordCount() {
		int result = 0;
		for (Set<Word> set : this.wordLists) {
			result += set.size();
		}
		return result;
	}

	/**
	 * This method returns the {@link Word}s for the given synset depth.
	 * It the synset depth was allready generated the buffered {@link Word}s are returned, if not a new iteration is performed
	 * @param synsetDepth Synset distance to retrieve words for
	 * @return {@link Word}s for the given synset distance
	 * @throws DbException
	 * @throws NoMorGenerationPossibleException
	 */
	public Set<Word> getSynsetDepthWords(int synsetDepth) throws DbException, NoMorGenerationPossibleException {
		if (wordLists.size() <= synsetDepth) {
			return generateMore();
		} else {
			return wordLists.get(synsetDepth);
		}
	}

	/**
	 * @return feature words as initial word list.
	 */
	public Set<Word> getInitialSet() {
		return wordLists.get(0);
	}

	/**
	 * Checks if a {@link WordlistGenConfig} would create the same results as this {@link BaseWordListGen}
	 * This can be used to determine if a {@link BaseWordListGen} can be reused.
	 * @param config Configuration parameters to check
	 * @return
	 */
	public boolean generatesEqualList(WordlistGenConfig config) {
		if (this.config.equals(config)) {
			return true;
		}
		return false;
	}

	/**
	 * If the {@link BaseWordListGen} is reused one might want to change the uby connection.
	 * @param uby
	 */
	public void setUby(Uby uby) {
		this.uby = uby;
	}

	/**
	 * If the {@link BaseWordListGen} is reused one might want to change the custom DB connection.
	 * @param jdbcConnect
	 */
	public void setJdbcConnect(JdbcConnect jdbcConnect) {
		this.jdbcConnect = jdbcConnect;
	}

}
