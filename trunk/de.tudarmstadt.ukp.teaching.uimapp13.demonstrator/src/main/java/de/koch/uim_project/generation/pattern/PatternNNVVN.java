package de.koch.uim_project.generation.pattern;

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

import java.util.Set;

import org.apache.log4j.Logger;

import de.koch.uim_project.util.Config;
import de.koch.uim_project.util.Pattern;
import de.koch.uim_project.util.StylisticDevice;
import de.koch.uim_project.database.DbException;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;

public class PatternNNVVN extends AbstractPattern {

	private Logger log = Logger.getRootLogger();
	
	public PatternNNVVN(Config config, Generator gen) {
		super(config, gen);
	}

	@Override
	public String generateSlogan(StylisticDevice stylisticDevice) throws DbException, SloganNotCreatedException {
		switch (stylisticDevice) {
		case None:
			return generateSdNone();
		default:
			throw new SloganNotCreatedException("Stylistic Device not expected for this pattern: " + stylisticDevice);
		}
	}

	private String generateSdNone() throws DbException, SloganNotCreatedException {
		int synsetDepth = 0;
		BaseWordListGen wordGen = gen.getGlobalWordListGen();

		Set<Word>  verbs, nouns, verbsEmo, nounsEmo;
		String  verbResult,nounResult;

		
		PosFilter nounFilter = new PosFilter(EPartOfSpeech.noun);
		PosFilter verbFilter = new PosFilter(EPartOfSpeech.verb);
		EmotionFilter emoFilter = new EmotionFilter(config.getEmotion());
		CombinedSetFilter nounEmo = new CombinedSetFilter(nounFilter, emoFilter);
		CombinedSetFilter verbEmo = new CombinedSetFilter(verbFilter,emoFilter);

		nouns = nounFilter.filterSet(wordGen.getInitialSet());
		verbs = verbFilter.filterSet(wordGen.getInitialSet());
	
		nounsEmo = emoFilter.filterSet(nouns);
		verbsEmo = verbFilter.filterSet(verbs);
		
		try{
		while (nounsEmo.size() < config.getMinWordlistForGeneration() || verbsEmo.size() < config.getMinWordlistForGeneration()
				) {
			synsetDepth++;
			
			if(nouns.size() < config.getMinWordlistForGeneration() || verbs.size() < config.getMinWordlistForGeneration() ){
				nouns.addAll(nounFilter.filterSet(wordGen.getMore(synsetDepth)));
				verbs.addAll(verbFilter.filterSet(wordGen.getMore(synsetDepth)));
				
				verbsEmo.addAll(emoFilter.filterSet(verbs));
				nounsEmo.addAll(emoFilter.filterSet(nouns));
				
			}else{
				verbsEmo.addAll(verbEmo.filterSet(wordGen.getMore(synsetDepth)));
				nounsEmo.addAll(nounEmo.filterSet(wordGen.getMore(synsetDepth)));
				
			}
			
		}
		
		nounResult = RandomUtil.randomWord(gen.getRnd(), nounsEmo).getLemma();
		verbResult = VerbConjugator.getInstance().getVVNForm(RandomUtil.randomWord(gen.getRnd(), verbsEmo));
		
		}catch(NoMorGenerationPossibleException e){
			log.warn("Not enough Emotion words for Pattern JJNNVVN. Falling back to prefering emotion words");
			
			nounResult = RandomUtil.randomWord(gen.getRnd(), nouns, config.getEmotion()).getLemma();
			verbResult = VerbConjugator.getInstance().getVVNForm(RandomUtil.randomWord(gen.getRnd(), verbs,config.getEmotion()));
			return "(a)" + nounResult + " " + verbResult;
		}
		
		return  "(a)" + nounResult + " " + verbResult;
	}

	@Override
	public StylisticDevice[] getPossibleStylisticDevices() {
		return new StylisticDevice[] { StylisticDevice.None };
	}

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
