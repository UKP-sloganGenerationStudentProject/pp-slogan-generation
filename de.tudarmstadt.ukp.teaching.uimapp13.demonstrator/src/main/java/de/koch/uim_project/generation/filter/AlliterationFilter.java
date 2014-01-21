package de.koch.uim_project.generation.filter;

import de.koch.uim_project.generation.Word;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.koch.uim_project.util.Triple;

public class AlliterationFilter implements IFilterSet{

	private int nounCount;
	private int verbCount;
	private int adjectiveCount;

	public AlliterationFilter(int nounCount, int verbCount, int adjectiveCount) {
		super();
		this.nounCount = nounCount;
		this.verbCount = verbCount;
		this.adjectiveCount = adjectiveCount;
	}

	@Override
	public Set<Word> filterSet(Set<Word> words) {
		// Map content: <char as integer,Triple<Set<Nouns starting with
		// char>,Set<Verbs starting with char>,Set<Adjectives
		// starting with char>>
		Map<Integer, Triple<Set<Word>, Set<Word>, Set<Word>>> sortedByStartLetter = new HashMap<Integer, Triple<Set<Word>, Set<Word>, Set<Word>>>();

		// fill map from parameter list
		for (Word word : words) {
			Character startLetter = word.getLemma().charAt(0);
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
	 * Create results from matching list
	 * 
	 * @param result
	 * @param sortedByStartLetter
	 */
	private Set<Word> filterCounts(

	Map<Integer, Triple<Set<Word>, Set<Word>, Set<Word>>> sortedByStartLetter, int nounCount, int verbCount, int adjectiveCount) {
		Set<Word> result = new HashSet<Word>();
		for (Integer inte : sortedByStartLetter.keySet()) {
			Triple<Set<Word>, Set<Word>, Set<Word>> entries = sortedByStartLetter.get(inte);
			if (nounCount > 0 && entries.getFirst().size() > nounCount) {
				result.addAll(entries.getFirst());
			}
			if (verbCount > 0 && entries.getSecond().size() > verbCount) {
				result.addAll(entries.getSecond());
			}
			if (adjectiveCount > 0 && entries.getThird().size() > adjectiveCount) {
				result.addAll(entries.getThird());
			}
		}
		return result;
	}

}
