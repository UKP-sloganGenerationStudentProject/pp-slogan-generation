package de.koch.uim_project.generation.filter;

import de.koch.uim_project.generation.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Filters specific words out.
 * 
 * @author Frerik Koch
 * 
 */
public class WordFilter implements IFilterWord {

	private List<Word> wordsToFilter;

	
	/**
	 * {@link WordFilter} to filter one {@link Word} out
	 * @param word {@link Word} to filter out
	 */
	public WordFilter(Word word){
		this.wordsToFilter = new ArrayList<Word>();
		this.wordsToFilter.add(word);
	}
	
	/**
	 * Filters multiple {@link Word}s out
	 * @param wordsToFilter
	 *            {@link Word}s to filter for
	 */
	public WordFilter(List<Word> wordsToFilter) {
		this.wordsToFilter = wordsToFilter;
	}

	
	/* (non-Javadoc)
	 * @see de.koch.uim_project.generation.filter.IFilterWord#filterWord(de.koch.uim_project.generation.Word)
	 */
	@Override
	public boolean filterWord(Word entry) {
		return !wordsToFilter.contains(entry);
	}

}
