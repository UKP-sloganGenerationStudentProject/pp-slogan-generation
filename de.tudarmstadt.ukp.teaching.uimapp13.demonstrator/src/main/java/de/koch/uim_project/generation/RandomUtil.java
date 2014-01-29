package de.koch.uim_project.generation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import com.googlecode.jweb1t.JWeb1TSearcher;

import de.koch.uim_project.generation.exception.SloganNotCreatedException;

/**
 * This class contains methods for choosing {@link Word}s out of {@link Word} {@link Set}s.
 * @author Frerik Koch
 *
 */
public class RandomUtil {

	private static Logger log = Logger.getRootLogger();
	
	private RandomUtil() {}

	/**
	 * This method chooses a random {@link Word} out of a {@link Set} of {@link Word}s
	 * @param rnd {@link Random} to choose word
	 * @param words {@link Set} of {@link Word}s to choose one from
	 * @return Choosen {@link Word}
	 * @throws SloganNotCreatedException
	 */
	public static Word randomWord(Random rnd, Set<Word> words) throws SloganNotCreatedException {
		List<Word> wordList = new ArrayList<Word>(words);
		if (wordList.size() < 1) {
			throw new SloganNotCreatedException("No fitting word found");
		}
		int index = rnd.nextInt(words.size());
		return wordList.get(index);
	}

	/**
	 * This method chooses a word randomly from a given list of emotion less
	 * words and a given list of emotion full words. The probability that a
	 * emotion full word is chosen is 2 times higher (results in 3 times if the
	 * word is still in the emotion less table)en
	 * 
	 * @param rnd
	 * @param emotionLess
	 * @param emotionFull
	 * @return
	 * @throws SloganNotCreatedException
	 */
	public static Word randomWordPreferEmotion(Random rnd, Set<Word> emotionLess, Set<Word> emotionFull) throws SloganNotCreatedException {
		List<Word> words;
		if (emotionLess.size() == 0 && emotionFull.size() == 0) {
			throw new SloganNotCreatedException("No words in word list");
		}
		int pEmoFull = emotionFull.size();
		int pEmoLess = emotionLess.size();
		pEmoFull = pEmoFull * 2;
		int chosenWord = rnd.nextInt(pEmoFull + pEmoLess);
		if (chosenWord < pEmoLess) {
			words = new ArrayList<Word>(emotionLess);
			return words.get(chosenWord);
		} else {
			words = new ArrayList<Word>(emotionFull);
			return words.get(chosenWord % emotionFull.size());
		}

	}

	/**
	 * This method chooses an adjective for a given noun.
	 * This is done via Web1T n-grams. For all adjective noun combinations the frequency is determined.
	 * The average frequency is set as threshold and an adjective is chosen randomly from the adjectives above the threshold.
	 * @param rnd {@link Random} for choosing Words
	 * @param w1tSearcher {@link JWeb1TSearcher} for getting combination frequencies. If null n-grams are not used
	 * @param adjectives {@link Set} of adjectives to choose one from 
	 * @param noun Noun to find an adjective for
	 * @return Chosen {@link Word}
	 * @throws SloganNotCreatedException
	 */
	public static Word findAdjectiveToNoun(Random rnd, JWeb1TSearcher w1tSearcher, Set<Word> adjectives, Word noun)
			throws SloganNotCreatedException {
		if(w1tSearcher == null){
			//do not use n-grams
			return randomWord(rnd, adjectives);
		}
		//Determine frequencies for adjective noun combinations
		Map<Word, Long> frequencys = new HashMap<Word, Long>();
		long freqAdd = 0;
		for (Word word : adjectives) {
			try {
				long freq = w1tSearcher.getFrequency(word.getLemma() + " " + noun.getLemma());
				freqAdd += freq;
				frequencys.put(word, freq);
			} catch (IOException e) {
				log.error("Failed to retrieve frequency from web1t");
				return randomWord(rnd, adjectives);
			}
		}
		long avgFreq = freqAdd / adjectives.size();
		
		//remove words below the threshold
		for (Word word : frequencys.keySet()) {
			if (frequencys.get(word) <= avgFreq) {
				frequencys.remove(word);
			}
		}
		//choose word from words above the threshold
		return randomWord(rnd, frequencys.keySet());
	}

	/**
	 * This method chooses an adjective for a noun via n-grams.
	 * It works similar to {@link RandomUtil#findAdjectiveToNoun(Random, JWeb1TSearcher, Set, Word)}.
	 * The difference is that both emotionfull and emotionless words are processed to allow the usage of {@link RandomUtil#randomWordPreferEmotion(Random, Set, Set)}
	 * @param rnd {@link Random} for choosing {@link Word}
	 * @param w1tSearcher {@link JWeb1TSearcher} for determining frequencies
	 * @param adjEmoLess {@link Set} of emotion less adjectives
	 * @param adjEmoFull {@link Set} of emotion full adjectives
	 * @param noun Noun to find adjective for
	 * @return Chosen {@link Word}
	 * @throws SloganNotCreatedException
	 */
	public static Word findAdjectiveToNoun(Random rnd, JWeb1TSearcher w1tSearcher, Set<Word> adjEmoLess, Set<Word> adjEmoFull, Word noun)
			throws SloganNotCreatedException {
		if(w1tSearcher == null){
			//Do not use n-grams
			return randomWordPreferEmotion(rnd, adjEmoLess, adjEmoFull);
		}
		Map<Word, Long> frequencysEmoLess = new HashMap<Word, Long>();
		Map<Word, Long> frequencysEmoFull = new HashMap<Word, Long>();
		long freqAdd = 0;
		
		try {
			//Determine frequencies of emotionfull words
			for (Word word : adjEmoFull) {

				long freq = w1tSearcher.getFrequency(word.getLemma() + " " + noun.getLemma());
				freqAdd += freq;
				frequencysEmoFull.put(word, freq);

			}
			//Determine frequencies of emotionless words
			for (Word word : adjEmoLess) {
				long freq = w1tSearcher.getFrequency(word.getLemma() + " " + noun.getLemma());
				freqAdd += freq;
				frequencysEmoLess.put(word, freq);
			}
		} catch (IOException e) {
			log.error("Failed to retrieve frequency from web1t");
			return randomWordPreferEmotion(rnd, adjEmoLess, adjEmoFull);
		}

		long avgFreq = freqAdd / (adjEmoFull.size() + adjEmoLess.size());
		
		//remove words below threshold
		for (Word word : frequencysEmoLess.keySet()) {
			if (frequencysEmoLess.get(word) <= avgFreq) {
				frequencysEmoLess.remove(word);
			}
		}
		
		//remove words below threshold
		for (Word word : frequencysEmoFull.keySet()) {
			if (frequencysEmoFull.get(word) <= avgFreq) {
				frequencysEmoFull.remove(word);
			}
		}
		//Choose word
		return randomWordPreferEmotion(rnd, frequencysEmoLess.keySet(), frequencysEmoFull.keySet());
	}

}
