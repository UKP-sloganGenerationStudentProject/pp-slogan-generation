package de.koch.uim_project.generation;

import de.koch.uim_project.generation.exception.SloganNotCreatedException;
import de.koch.uim_project.generation.filter.EmotionFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import com.googlecode.jweb1t.JWeb1TSearcher;

import de.koch.uim_project.util.Emotion;

public class RandomUtil {

	private static Logger log = Logger.getRootLogger();

	private RandomUtil() {

	}

	public static Word randomWord(Random rnd, Set<Word> words) throws SloganNotCreatedException {
		List<Word> wordList = new ArrayList<Word>(words);
		if (wordList.size() < 1) {
			throw new SloganNotCreatedException("No fitting word found");
		}
		int index = rnd.nextInt(words.size());
		return wordList.get(index);
	}

	public static Word randomWord(Random rnd, Set<Word> words, Emotion preferedEmotion) throws SloganNotCreatedException {
		if (preferedEmotion == null) {
			return randomWord(rnd, words);
		}
		Set<Word> emoWords = new EmotionFilter(preferedEmotion).filterSet(words);
		if (emoWords.size() > 1) {
			return randomWord(rnd, emoWords);
		} else {
			return randomWord(rnd, words);
		}
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

	public static Word findAdjectiveToNoun(Random rnd, JWeb1TSearcher w1tSearcher, Set<Word> adjectivesEmo, Word noun)
			throws SloganNotCreatedException {
		if(w1tSearcher == null){
			return randomWord(rnd, adjectivesEmo);
		}
		Map<Word, Long> frequencys = new HashMap<Word, Long>();
		long freqAdd = 0;
		for (Word word : adjectivesEmo) {
			try {
				long freq = w1tSearcher.getFrequency(word.getLemma() + " " + noun.getLemma());
				freqAdd += freq;
				frequencys.put(word, freq);
			} catch (IOException e) {
				log.error("Failed to retrieve frequency from web1t");
				return randomWord(rnd, adjectivesEmo);
			}
		}
		long avgFreq = freqAdd / adjectivesEmo.size();
		for (Word word : frequencys.keySet()) {
			if (frequencys.get(word) <= avgFreq) {
				frequencys.remove(word);
			}
		}
		return randomWord(rnd, frequencys.keySet());
	}

	public static Word findAdjectiveToNoun(Random rnd, JWeb1TSearcher w1tSearcher, Set<Word> adjEmoLess, Set<Word> adjEmoFull, Word noun)
			throws SloganNotCreatedException {
		if(w1tSearcher == null){
			return randomWordPreferEmotion(rnd, adjEmoLess, adjEmoFull);
		}
		Map<Word, Long> frequencysEmoLess = new HashMap<Word, Long>();
		Map<Word, Long> frequencysEmoFull = new HashMap<Word, Long>();
		long freqAdd = 0;
		try {
			for (Word word : adjEmoFull) {

				long freq = w1tSearcher.getFrequency(word.getLemma() + " " + noun.getLemma());
				freqAdd += freq;
				frequencysEmoFull.put(word, freq);

			}
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
		for (Word word : frequencysEmoLess.keySet()) {
			if (frequencysEmoLess.get(word) <= avgFreq) {
				frequencysEmoLess.remove(word);
			}
		}
		for (Word word : frequencysEmoFull.keySet()) {
			if (frequencysEmoFull.get(word) <= avgFreq) {
				frequencysEmoFull.remove(word);
			}
		}
		return randomWordPreferEmotion(rnd, frequencysEmoLess.keySet(), frequencysEmoFull.keySet());
	}

}
