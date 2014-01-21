package de.koch.uim_project.generation.filter;

import de.koch.uim_project.generation.Word;

import java.util.Set;
import java.util.HashSet;

import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;

public class PosFilter implements IFilterSet, IFilterWord {

	private Set<EPartOfSpeech> poses;
	
	public PosFilter(EPartOfSpeech pos){
		poses = new HashSet<EPartOfSpeech>();
		poses.add(pos);
	}
	
	public PosFilter(Set<EPartOfSpeech> poses){
		this.poses = poses;
	}
	
	@Override
	public boolean filterWord(Word word) {
		return poses.contains(word.getPos());
	}

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
