package de.koch.uim_project.generation.filter;

import de.koch.uim_project.generation.Word;

import java.util.Set;

/**
 * Classes implementing this interface filter a set of {@link Word}s. 
 * @author Frerik Koch
 *
 */
public interface IFilterSet {

	/**
	 * Apply the filter of the implementing class
	 * ATTENTION: Implementing classes must not alter the given {@link Word}s!
	 * @param words {@link Word}s to filter. 
	 * @return
	 */
	public Set<Word> filterSet(final Set<Word> words);
	
}
