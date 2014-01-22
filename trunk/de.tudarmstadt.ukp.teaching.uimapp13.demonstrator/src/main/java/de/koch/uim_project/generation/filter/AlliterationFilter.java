package de.koch.uim_project.generation.filter;

import de.koch.uim_project.generation.Word;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.koch.uim_project.util.Triple;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;

/**
 * This filter is used for alliteration generation. It allows for prefiltering
 * and checking if an alliteration is possible. It indirectly filters for
 * {@link EPartOfSpeech}
 * 
 * @author Frerik Koch
 * 
 */
public class AlliterationFilter implements IFilterSet {

	private int nounCount;
	private int verbCount;
	private int adjectiveCount;

	/**
	 * @param nounCount
	 *            how many nouns with the same start letter are needed
	 * @param verbCount
	 *            how many verbs with the same start letter are needed
	 * @param adjectiveCount
	 *            how many adjectives with the same start letter are needed
	 */
	public AlliterationFilter(int nounCount, int verbCount, int adjectiveCount) {
		super();
		this.nounCount = nounCount;
		this.verbCount = verbCount;
		this.adjectiveCount = adjectiveCount;
	}

	/*
	 * This method filter is an alliteration is possible and retains all words which allow for an alliteration.
	 * It ensures that for every word there are at least *Count words with the same startletter
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.koch.uim_project.generation.filter.IFilterSet#filterSet(java.util.Set)
	 */
	@Override
	public Set<Word> filterSet(Set<Word> words) {
		// Map content: <char as integer,Triple<Set<Nouns starting with
		// char>,Set<Verbs starting with char>,Set<Adjectives
		// starting with char>>
		Map<Integer, Triple<Set<Word>, Set<Word>, Set<Word>>> sortedByStartLetter = new HashMap<Integer, Triple<Set<Word>, Set<Word>, Set<Word>>>();

		// fill map from parameter list
		for (Word word : words) {
			Character startLetter = word.getLemma().charAt(0);
			//add word to list with correct start letter if already one exists
			if (sortedByStartLetter.containsKey((int) startLetter)) {
				switch (word.getPos()) {
				case noun:
					sortedByStartLetter.get((int) startLetter).getFirst().add(word);
					break;
				case verb:
					sortedByStartLetter.get((int) startLetter).getSecond().add(word);
					break;
				case adjective:
					sortedByStartLetter.get((int) startLetter).getThird().add(word);
					break;
				default:
					break;
				}

			} else {
				//create list with start letter
				Set<Word> nounList = new HashSet<Word>();
				Set<Word> verbList = new HashSet<Word>();
				Set<Word> adjectiveList = new HashSet<Word>();
				Triple<Set<Word>, Set<Word>, Set<Word>> newTriple = new Triple<Set<Word>, Set<Word>, Set<Word>>(nounList, verbList, adjectiveList);
				sortedByStartLetter.put((int) startLetter, newTriple);
			}
		}

		return filterCounts(sortedByStartLetter, nounCount, verbCount, adjectiveCount);
	}

	/**
	 * This class creates the result from the sorted Map
	 * 
	 * @param result
	 * @param sortedByStartLetter
	 */
	private Set<Word> filterCounts(Map<Integer, Triple<Set<Word>, Set<Word>, Set<Word>>> sortedByStartLetter, int nounCount, int verbCount,
			int adjectiveCount) {
		Set<Word> result = new HashSet<Word>();
		for (Integer inte : sortedByStartLetter.keySet()) {
			Triple<Set<Word>, Set<Word>, Set<Word>> entries = sortedByStartLetter.get(inte);
			//if nouns are needed and the noun list for the  start letter is greater than nounCount add to result
			if (nounCount > 0 && entries.getFirst().size() > nounCount) {
				result.addAll(entries.getFirst());
			}
			//if verbs are needed and the verb list for the  start letter is greater than verbCount add to result
			if (verbCount > 0 && entries.getSecond().size() > verbCount) {
				result.addAll(entries.getSecond());
			}
			//if adjectives are needed and the adjective list for the start letter is greater than adjectiveCount add to result
			if (adjectiveCount > 0 && entries.getThird().size() > adjectiveCount) {
				result.addAll(entries.getThird());
			}
		}
		return result;
	}

}
