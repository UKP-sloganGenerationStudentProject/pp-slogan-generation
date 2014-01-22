package de.koch.uim_project.generation.filter;

import de.koch.uim_project.generation.Word;

import java.util.Set;
import java.util.HashSet;

import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;

/**
 * This class filters the given {@link Word} or {@link Word}s by their {@link EPartOfSpeech} tag
 * @author Frerik Koch
 *
 */
public class PosFilter implements IFilterSet, IFilterWord {

	private Set<EPartOfSpeech> poses;
	
	/**
	 * {@link PosFilter} filtering for one {@link EPartOfSpeech}
	 * @param pos {@link EPartOfSpeech} to filter for
	 */
	public PosFilter(EPartOfSpeech pos){
		poses = new HashSet<EPartOfSpeech>();
		poses.add(pos);
	}
	
	/**
	 * {@link PosFilter} filtering for multiple {@link EPartOfSpeech}
	 * @param poses {@link EPartOfSpeech}s to filter for
	 */
	public PosFilter(Set<EPartOfSpeech> poses){
		this.poses = poses;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.koch.uim_project.generation.filter.IFilterWord#filterWord(de.koch.uim_project.generation.Word)
	 */
	@Override
	public boolean filterWord(Word word) {
		return poses.contains(word.getPos());
	}

	/* (non-Javadoc)
	 * @see de.koch.uim_project.generation.filter.IFilterSet#filterSet(java.util.Set)
	 */
	@Override
	public Set<Word> filterSet(Set<Word> words) {
		Set<Word> result = new HashSet<Word>();
		for(Word word : words){
			if(filterWord(word)){
				result.add(word);
			}
		}
		return result;
	}

}
