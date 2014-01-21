package de.koch.uim_project.generation.filter;

import java.util.HashSet;
import java.util.Set;

import de.koch.uim_project.generation.Word;

/**
 * This class is a filter for the stylistic device Alliteration.
 * 
 * @author Frerik Koch
 * 
 */
public class StartLetterFilter implements IFilterWord,IFilterSet {

	private char startLetter;

	/**
	 * Constructor for {@link StartLetterFilter}
	 * 
	 * @param startLetter
	 *            Starting letter to filter for
	 */
	public StartLetterFilter(char startLetter) {
		this.startLetter = startLetter;
	}

	@Override
	public boolean filterWord(Word word) {
		return (word.getLemma().startsWith(Character.toString(startLetter)));
	}

	/**
	 * Getter for {@link StartLetterFilter#startLetter}
	 * 
	 * @return Start letter to filter for
	 */
	public char getStartLetter() {
		return startLetter;
	}

	@Override
	public String toString() {
		return "StartLetterFilter [startLetter=" + startLetter + "]";
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
