package de.koch.uim_project.generation;

import de.koch.uim_project.generation.exception.SloganNotCreatedException;
import de.koch.uim_project.generation.filter.EmotionFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import de.koch.uim_project.util.Emotion;


public class RandomUtil {

	private RandomUtil(){
		
	}
	
	public static Word randomWord(Random rnd, Set<Word> words) throws SloganNotCreatedException{
		List<Word> wordList = new ArrayList<Word>(words);
		if(wordList.size() < 1){
			throw new SloganNotCreatedException("No fitting word found");
		}
		int index = rnd.nextInt(words.size());
		return wordList.get(index);
	}
	
	public static Word randomWord(Random rnd, Set<Word> words,Emotion preferedEmotion) throws SloganNotCreatedException{
		if(preferedEmotion == null){
			return randomWord(rnd,words);
		}
		Set<Word> emoWords = new EmotionFilter(preferedEmotion).filterSet(words);
		if(emoWords.size() > 1){
			return randomWord(rnd,emoWords);
		}else{
			return randomWord(rnd,words);
		}
	}
	
	/**
	 * This method chooses a word randomly from a given list of emotion less words and a given list of emotion full words.
	 * The probability that a emotion full word is chosen is 2 times higher (results in 3 times if the word is still in the emotion less table)en
	 * @param rnd
	 * @param emotionLess
	 * @param emotionFull
	 * @return
	 */
	public static Word randomWordPreferEmotion(Random rnd, Set<Word> emotionLess, Set<Word> emotionFull){
		List<Word> words;
		int pEmoFull = emotionFull.size();
		int pEmoLess = emotionLess.size();
		pEmoFull = pEmoFull * 2;
		int chosenWord = rnd.nextInt(pEmoFull+pEmoLess);
		if(chosenWord < pEmoLess){
			words = new ArrayList<Word>(emotionLess);
			return words.get(chosenWord);
		}else{
			words = new ArrayList<Word>(emotionFull);
			return words.get(chosenWord%emotionFull.size());
		}
		
	}
	
}
