package de.koch.uim_project.generation.filter;

import de.koch.uim_project.generation.Word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The {@link CombinedSetFilter} applies multiple {@link IFilterWord} in one iteration.
 * This allows for greater performance because only one iteration is necessary regardless of the number of {@link IFilterWord}
 * @author Frerik Koch
 *
 */
public class CombinedSetFilter implements IFilterSet{

	private List<IFilterWord> filters = new ArrayList<IFilterWord>();
	
	public CombinedSetFilter(List<IFilterWord> filters){
		this.filters = filters;
	}
	
	public CombinedSetFilter(IFilterWord... filters){
		this.filters = Arrays.asList(filters);
	}

	/* 
	 * Applies all filters to a word before accepting or rejecting it.
	 * If one filter rejects it the word is rejected
	 * (non-Javadoc)
	 * @see de.koch.uim_project.generation.filter.IFilterSet#filterSet(java.util.Set)
	 */
	@Override
	public Set<Word> filterSet(Set<Word> words) {
		Set<Word> result = new HashSet<Word>();
		for(Word word: words){
			boolean matchesFilters = true;
			for(IFilterWord filter : filters){
				matchesFilters = matchesFilters && filter.filterWord(word);
			}
			if(matchesFilters){
				result.add(word);
			}
		}
		return result;
	}
	
	public void addFilter(IFilterWord wordFilter){
		this.filters.add(wordFilter);
	}	
	
}
