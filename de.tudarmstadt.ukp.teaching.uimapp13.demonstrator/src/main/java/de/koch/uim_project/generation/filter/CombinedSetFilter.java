package de.koch.uim_project.generation.filter;

import de.koch.uim_project.generation.Word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CombinedSetFilter implements IFilterSet{

	private List<IFilterWord> filters = new ArrayList<IFilterWord>();
	
	public CombinedSetFilter(List<IFilterWord> filters){
		this.filters = filters;
	}
	
	public CombinedSetFilter(IFilterWord... filters){
		this.filters = Arrays.asList(filters);
	}

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
