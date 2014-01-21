package de.koch.uim_project.generation.filter;

import de.koch.uim_project.generation.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Filters for specific words
 * 
 * @author Frerik Koch
 * 
 */
public class WordFilter implements IFilterWord {

	private List<Word> wordsToFilter;

	
	public WordFilter(Word word){
		this.wordsToFilter = new ArrayList<Word>();
		this.wordsToFilter.add(word);
	}
	
	/**
	 * @param wordsToFilter
	 *            Words to filter for
	 */
	public WordFilter(List<Word> wordsToFilter) {
		this.wordsToFilter = wordsToFilter;
	}

	
	public boolean filterWord(Word entry) {
		return !wordsToFilter.contains(entry);
	}

}
