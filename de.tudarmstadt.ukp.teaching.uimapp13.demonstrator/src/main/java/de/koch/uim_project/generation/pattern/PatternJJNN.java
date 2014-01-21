package de.koch.uim_project.generation.pattern;

import de.koch.uim_project.generation.BaseWordListGen;
import de.koch.uim_project.generation.Generator;
import de.koch.uim_project.generation.RandomUtil;
import de.koch.uim_project.generation.Word;
import de.koch.uim_project.generation.exception.NoMorGenerationPossibleException;
import de.koch.uim_project.generation.exception.PatternNotInitializeableException;
import de.koch.uim_project.generation.exception.SloganNotCreatedException;
import de.koch.uim_project.generation.filter.CombinedSetFilter;
import de.koch.uim_project.generation.filter.EmotionFilter;
import de.koch.uim_project.generation.filter.PosFilter;

import java.util.Set;

import org.apache.log4j.Logger;

import de.koch.uim_project.util.Config;
import de.koch.uim_project.util.Pattern;
import de.koch.uim_project.util.StylisticDevice;
import de.koch.uim_project.database.DbException;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;

public class PatternJJNN extends AbstractPattern {

	private Logger log = Logger.getRootLogger();

	public PatternJJNN(Config config, Generator gen) throws PatternNotInitializeableException {
		super(config, gen);
	}

	@Override
	public String generateSlogan(StylisticDevice stylisticDevice) throws DbException, SloganNotCreatedException {
		String result;
		switch (stylisticDevice) {
		case None:
			result = generateNoStylisticDevice();
			break;
		default:
			throw new SloganNotCreatedException("Stylistic Device not expected for this pattern: " + stylisticDevice);
		}
		return result;
	}

	private String generateNoStylisticDevice() throws DbException, SloganNotCreatedException {
		int synsetDepth = 0;
		
		String noun,adj;
		
		BaseWordListGen wordGen = gen.getGlobalWordListGen();
		Set<Word> nounsEmo;
		Set<Word> adjsEmo;
		Set<Word> nouns;
		Set<Word> adjs;

		EmotionFilter emoFilter = new EmotionFilter(config.getEmotion());
		PosFilter nounFilter = new PosFilter(EPartOfSpeech.noun);
		PosFilter adjFilter = new PosFilter(EPartOfSpeech.adjective);
		CombinedSetFilter emoNounFil = new CombinedSetFilter(nounFilter, emoFilter);
		CombinedSetFilter emoAdjFil = new CombinedSetFilter(adjFilter, emoFilter);

		nouns = nounFilter.filterSet(wordGen.getInitialSet());
		adjs = adjFilter.filterSet(wordGen.getInitialSet());
		nounsEmo = emoFilter.filterSet(nouns);
		adjsEmo = emoFilter.filterSet(adjs);

		try {
			while (nounsEmo.size() < config.getMinWordlistForGeneration() || adjsEmo.size() < config.getMinWordlistForGeneration()) {

				synsetDepth++;
				Set<Word> more = wordGen.getMore(synsetDepth);
				if (nouns.size() < config.getMinWordlistForGeneration() || adjs.size() < config.getMinWordlistForGeneration()) {
					nouns.addAll(nounFilter.filterSet(more));
					adjs.addAll(adjFilter.filterSet(more));
					nounsEmo.addAll(emoFilter.filterSet(nouns));
					adjsEmo.addAll(emoFilter.filterSet(adjs));
				} else {
					nounsEmo.addAll(emoNounFil.filterSet(more));
					adjsEmo.addAll(emoAdjFil.filterSet(more));
				}

			}

			noun = RandomUtil.randomWord(gen.getRnd(), nounsEmo, null).getLemma();
			adj = RandomUtil.randomWord(gen.getRnd(), adjsEmo, null).getLemma();

		} catch (NoMorGenerationPossibleException e) {
			log.warn("Not enough Emotion words for Pattern JJNN. Falling back to prefering emotion words");
			noun = RandomUtil.randomWord(gen.getRnd(), nouns, config.getEmotion()).getLemma();
			adj = RandomUtil.randomWord(gen.getRnd(), adjs, config.getEmotion()).getLemma();

		}

		return adj + " " + noun;
	}

	@Override
	public StylisticDevice[] getPossibleStylisticDevices() {
		return new StylisticDevice[] { StylisticDevice.None };
	}

	@Override
	public Pattern getPatternType() {
		return Pattern.JJNN;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PatternJJNN";
	}

}
