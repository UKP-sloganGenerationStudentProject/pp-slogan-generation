package de.koch.uim_project.generation.pattern;

import java.util.Set;

import de.koch.uim_project.database.DbException;
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
import de.koch.uim_project.util.Config;
import de.koch.uim_project.util.Pattern;
import de.koch.uim_project.util.StylisticDevice;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;

/**
 * This class represents the Pattern "JJ NN" 
 * @author Frerik Koch
 *
 */
public class PatternJJNN extends AbstractPattern {

	public PatternJJNN(Config config, Generator gen) throws PatternNotInitializeableException {
		super(config, gen);
	}

	/* (non-Javadoc)
	 * @see de.koch.uim_project.generation.pattern.AbstractPattern#generateSlogan(de.koch.uim_project.util.StylisticDevice)
	 */
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

	/**
	 * This method generates a slogan without {@link StylisticDevice}
	 * @return Generated slogan
	 * @throws DbException
	 * @throws SloganNotCreatedException
	 */
	private String generateNoStylisticDevice() throws DbException, SloganNotCreatedException {
		int synsetDepth = 0;
		
		String noun,adj;
		
		BaseWordListGen wordGen = gen.getGlobalWordListGen();
		Set<Word> nounsEmo;
		Set<Word> adjsEmo;
		Set<Word> nouns;
		Set<Word> adjs;

		//Init filters
		EmotionFilter emoFilter = new EmotionFilter(config.getEmotion());
		PosFilter nounFilter = new PosFilter(EPartOfSpeech.noun);
		PosFilter adjFilter = new PosFilter(EPartOfSpeech.adjective);
		CombinedSetFilter emoNounFil = new CombinedSetFilter(nounFilter, emoFilter);
		CombinedSetFilter emoAdjFil = new CombinedSetFilter(adjFilter, emoFilter);

		//Init wordLists
		nouns = nounFilter.filterSet(wordGen.getInitialSet());
		adjs = adjFilter.filterSet(wordGen.getInitialSet());
		nounsEmo = emoFilter.filterSet(nouns);
		adjsEmo = emoFilter.filterSet(adjs);

		try {
			//Try to increase word lists
			while (nounsEmo.size() < config.getMinWordlistForGeneration() || adjsEmo.size() < config.getMinWordlistForGeneration()) {

				synsetDepth++;
				Set<Word> more = wordGen.getMore(synsetDepth);
				
				//increase emotion less lists and emotion full lists
				if (nouns.size() < config.getMinWordlistForGeneration() || adjs.size() < config.getMinWordlistForGeneration()) {
					nouns.addAll(nounFilter.filterSet(more));
					adjs.addAll(adjFilter.filterSet(more));
					nounsEmo.addAll(emoFilter.filterSet(nouns));
					adjsEmo.addAll(emoFilter.filterSet(adjs));
					
				//increase emotion full lists	
				} else {
					nounsEmo.addAll(emoNounFil.filterSet(more));
					adjsEmo.addAll(emoAdjFil.filterSet(more));
				}

			}
			
			//generate from emotion lists
			noun = RandomUtil.randomWord(gen.getRnd(), nounsEmo, null).getLemma();
			adj = RandomUtil.randomWord(gen.getRnd(), adjsEmo, null).getLemma();

		} catch (NoMorGenerationPossibleException e) {
			
			//generate from emotion less lists but prefer emotion fitting words
			noun = RandomUtil.randomWordPreferEmotion(gen.getRnd(), nouns, nounsEmo).getLemma();
			adj = RandomUtil.randomWordPreferEmotion(gen.getRnd(), adjs, adjsEmo).getLemma();

		}

		return adj + " " + noun;
	}

	/* (non-Javadoc)
	 * @see de.koch.uim_project.generation.pattern.AbstractPattern#getPossibleStylisticDevices()
	 */
	@Override
	public StylisticDevice[] getPossibleStylisticDevices() {
		return new StylisticDevice[] { StylisticDevice.None };
	}

	/* (non-Javadoc)
	 * @see de.koch.uim_project.generation.pattern.AbstractPattern#getPatternType()
	 */
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
