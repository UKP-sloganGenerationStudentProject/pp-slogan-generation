package de.koch.uim_project.generation.filter;

import de.koch.uim_project.generation.Word;

import java.util.Set;

public interface IFilterSet {

	public Set<Word> filterSet(Set<Word> words);
	
}
