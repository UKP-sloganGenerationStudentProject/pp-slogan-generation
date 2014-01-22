package de.koch.uim_project.generation.pattern;

import java.util.Set;

import de.koch.uim_project.database.DbException;
import de.koch.uim_project.generation.BaseWordListGen;
import de.koch.uim_project.generation.Generator;
import de.koch.uim_project.generation.RandomUtil;
import de.koch.uim_project.generation.VerbConjugator;
import de.koch.uim_project.generation.Word;
import de.koch.uim_project.generation.exception.NoMorGenerationPossibleException;
import de.koch.uim_project.generation.exception.SloganNotCreatedException;
import de.koch.uim_project.generation.filter.CombinedSetFilter;
import de.koch.uim_project.generation.filter.EmotionFilter;
import de.koch.uim_project.generation.filter.PosFilter;
import de.koch.uim_project.util.Config;
import de.koch.uim_project.util.Pattern;
import de.koch.uim_project.util.StylisticDevice;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;

/**
 * This class represents the pattern "NN VVN"
 * @author Frerik Koch
 *
 */
public class PatternNNVVN extends AbstractPattern {
	
	public PatternNNVVN(Config config, Generator gen) {
		super(config, gen);
	}

	/* (non-Javadoc)
	 * @see de.koch.uim_project.generation.pattern.AbstractPattern#generateSlogan(de.koch.uim_project.util.StylisticDevice)
	 */
	@Override
	public String generateSlogan(StylisticDevice stylisticDevice) throws DbException, SloganNotCreatedException {
		switch (stylisticDevice) {
		case None:
			return generateSdNone();
		default:
			throw new SloganNotCreatedException("Stylistic Device not expected for this pattern: " + stylisticDevice);
		}
	}

	/**
	 * This method generates a slogan with no {@link StylisticDevice}
	 * @return Generated slogan
	 * @throws DbException
	 * @throws SloganNotCreatedException
	 */
	private String generateSdNone() throws DbException, SloganNotCreatedException {
		int synsetDepth = 0;
		BaseWordListGen wordGen = gen.getGlobalWordListGen();

		Set<Word>  verbs, nouns, verbsEmo, nounsEmo;
		String  verbResult,nounResult;

		//Init filters
		PosFilter nounFilter = new PosFilter(EPartOfSpeech.noun);
		PosFilter verbFilter = new PosFilter(EPartOfSpeech.verb);
		EmotionFilter emoFilter = new EmotionFilter(config.getEmotion());
		CombinedSetFilter nounEmo = new CombinedSetFilter(nounFilter, emoFilter);
		CombinedSetFilter verbEmo = new CombinedSetFilter(verbFilter,emoFilter);

		//Init word lists
		nouns = nounFilter.filterSet(wordGen.getInitialSet());
		verbs = verbFilter.filterSet(wordGen.getInitialSet());
		nounsEmo = emoFilter.filterSet(nouns);
		verbsEmo = verbFilter.filterSet(verbs);
		
		try{
		while (nounsEmo.size() < config.getMinWordlistForGeneration() || verbsEmo.size() < config.getMinWordlistForGeneration()
				) {
			synsetDepth++;
			
			if(nouns.size() < config.getMinWordlistForGeneration() || verbs.size() < config.getMinWordlistForGeneration() ){
				//Increase all word lists
				nouns.addAll(nounFilter.filterSet(wordGen.getMore(synsetDepth)));
				verbs.addAll(verbFilter.filterSet(wordGen.getMore(synsetDepth)));
				verbsEmo.addAll(emoFilter.filterSet(verbs));
				nounsEmo.addAll(emoFilter.filterSet(nouns));
				
			}else{
				//Increase only emotion full word lists
				verbsEmo.addAll(verbEmo.filterSet(wordGen.getMore(synsetDepth)));
				nounsEmo.addAll(nounEmo.filterSet(wordGen.getMore(synsetDepth)));
				
			}
			
		}
		
		//Create slogan from emotion full word lists
		nounResult = RandomUtil.randomWord(gen.getRnd(), nounsEmo).getLemma();
		verbResult = VerbConjugator.getInstance().getVVNForm(RandomUtil.randomWord(gen.getRnd(), verbsEmo));
		
		}catch(NoMorGenerationPossibleException e){
			//create slogan from emotion less word lists but prefer emotion full words
			nounResult = RandomUtil.randomWordPreferEmotion(gen.getRnd(), nouns, nounsEmo).getLemma();
			verbResult = VerbConjugator.getInstance().getVVNForm(RandomUtil.randomWordPreferEmotion(gen.getRnd(), verbs,verbsEmo));
			return "(a)" + nounResult + " " + verbResult;
		}
		
		return  "(a)" + nounResult + " " + verbResult;
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
		return Pattern.NNVVN;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PatternNNVVN";
	}

}
