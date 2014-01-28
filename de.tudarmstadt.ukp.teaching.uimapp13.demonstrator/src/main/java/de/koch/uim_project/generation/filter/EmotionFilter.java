package de.koch.uim_project.generation.filter;

import de.koch.uim_project.generation.Word;

import java.util.HashSet;
import java.util.Set;

import de.koch.uim_project.util.Emotion;

/**
 * This filter filters a word on its {@link Emotion}s. If the given emotion is in the {@link Emotion} set of the word, the word is accepted
 * @author Frerik Koch
 *
 */
public class EmotionFilter implements IFilterSet, IFilterWord {

	
	private Emotion emotion;

	public EmotionFilter(Emotion emotion){
		this.emotion = emotion;
	}
	
	@Override
	public boolean filterWord(Word word) {
		if(emotion == Emotion.NONE){
			return true;
		}
		return (word.getEmotions().size() > 0 && word.getEmotions().contains(emotion));
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
