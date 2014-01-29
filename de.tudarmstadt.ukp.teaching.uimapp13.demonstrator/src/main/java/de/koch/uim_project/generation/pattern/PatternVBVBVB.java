package de.koch.uim_project.generation.pattern;

import java.util.HashSet;
import java.util.Set;

import de.koch.uim_project.database.DbException;
import de.koch.uim_project.generation.BaseWordListGen;
import de.koch.uim_project.generation.Generator;
import de.koch.uim_project.generation.RandomUtil;
import de.koch.uim_project.generation.Word;
import de.koch.uim_project.generation.exception.NoFittingWordFoundException;
import de.koch.uim_project.generation.exception.NoMorGenerationPossibleException;
import de.koch.uim_project.generation.exception.SloganNotCreatedException;
import de.koch.uim_project.generation.filter.AlliterationFilter;
import de.koch.uim_project.generation.filter.CombinedSetFilter;
import de.koch.uim_project.generation.filter.EmotionFilter;
import de.koch.uim_project.generation.filter.PosFilter;
import de.koch.uim_project.generation.filter.StartLetterFilter;
import de.koch.uim_project.util.Config;
import de.koch.uim_project.util.Pattern;
import de.koch.uim_project.util.StylisticDevice;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;
import de.tudarmstadt.ukp.lmf.model.semantics.Synset;

/**
 * This class represents the POS pattern VB,VB,[VB]?
 * 
 * @author Frerik Koch
 * 
 */
public class PatternVBVBVB extends AbstractPattern {

	public PatternVBVBVB(Config config, Generator gen) {
		super(config, gen);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see generation.patterns.AbstractPattern#generateSloganResult(generation.
	 * StylisticDevice)
	 */
	@Override
	public String generateSlogan(StylisticDevice stylisticDevice) throws DbException, SloganNotCreatedException {
		switch (stylisticDevice) {
		case Alliteration:
			return generateAlliteration();
		case None:
			return generateNoStylisticDevice();
		default:
			throw new SloganNotCreatedException("Stylistic Device not expected for this pattern: " + stylisticDevice);
		}
	}

	/**
	 * Generates a slogan of this pattern.
	 * @return
	 * @throws DbException
	 * @throws SloganNotCreatedException
	 */
	private String generateNoStylisticDevice() throws DbException, SloganNotCreatedException {
		//determine slogan length
		int verbCount = 2 + gen.getRnd().nextInt(2);
		
		int synsetDepth = 0;
		String[] verbResult = new String[verbCount];
		BaseWordListGen wordGen = gen.getGlobalWordListGen();

		//Init filters
		EmotionFilter emoFilter = new EmotionFilter(config.getEmotion());
		PosFilter posFilter = new PosFilter(EPartOfSpeech.verb);
		CombinedSetFilter combFilter = new CombinedSetFilter(posFilter, emoFilter);

		//Init word lists
		Set<Word> verbs = posFilter.filterSet(wordGen.getInitialSet());
		Set<Word> verbsEmo = emoFilter.filterSet(verbs);

		try {
			while (verbsEmo.size() <= config.getMinWordlistForGeneration()) {
				synsetDepth++;
				if (verbs.size() <= config.getMinWordlistForGeneration()) {
					//Increase all word lists
					verbs.addAll(posFilter.filterSet(wordGen.getSynsetDepthWords(synsetDepth)));
					verbsEmo.addAll(emoFilter.filterSet(verbs));
				} else {
					//Increase only emotion full word lists
					verbsEmo.addAll(combFilter.filterSet(wordGen.getSynsetDepthWords(synsetDepth)));
				}

			}
			
			//Create slogan from emotion full word lists
			for (int i = 0; i < verbResult.length; i++) {
				verbResult[i] = RandomUtil.randomWord(gen.getRnd(), verbsEmo).getLemma();
			}

		} catch (NoMorGenerationPossibleException e) {
			//Create slogan from emotion less word lists
			for (int i = 0; i < verbResult.length; i++) {
				verbResult[i] = RandomUtil.randomWordPreferEmotion(gen.getRnd(), verbs, verbsEmo).getLemma();
			}
		}

		return generateResultString(verbResult);

	}

	/**
	 * Generates a slogan according to the alliteration stylistic device.
	 * This slogan uses all words in every iteration. Not just one specific {@link Synset} depth.
	 * For alliteration checking all words must be present to determine the count of words starting with the same letter.
	 * @return Generated slogan
	 * @throws DbException
	 * @throws SloganNotCreatedException
	 * @throws NoFittingWordFoundException
	 */
	private String generateAlliteration() throws DbException, SloganNotCreatedException {
		//Determine slogan length
		int verbCount = 2 + gen.getRnd().nextInt(2);
		
		BaseWordListGen wordGen = gen.getGlobalWordListGen();
		int synsetDepth = 0;
		String[] verbResults = new String[verbCount];

		//Init filters
		EmotionFilter emoFilter = new EmotionFilter(config.getEmotion());
		AlliterationFilter allFilter = new AlliterationFilter(0, verbCount, 0);

		//Init word lists
		Set<Word> verbs = allFilter.filterSet(wordGen.getInitialSet());
		Set<Word> verbsEmo = allFilter.filterSet(emoFilter.filterSet(wordGen.getInitialSet()));

		try {
			while (verbsEmo.size() <= config.getMinWordlistForGeneration()) {
				synsetDepth++;
				if (verbs.size() <= config.getMinWordlistForGeneration()) {
					//Increase all word lists
					Set<Word> unionSet = new HashSet<Word>();
					for (int i = 0; i <= synsetDepth; i++) {
						unionSet.addAll(wordGen.getSynsetDepthWords(i));
					}
					verbs = allFilter.filterSet(unionSet);
				}
				//Increase only emotion full list
				Set<Word> unionSet = new HashSet<Word>();
				for (int i = 0; i <= synsetDepth; i++) {
					unionSet.addAll(wordGen.getSynsetDepthWords(i));
				}
				verbsEmo = allFilter.filterSet(emoFilter.filterSet(unionSet));

			}
			//Generate slogan from emotion full word list
			verbResults[0] = RandomUtil.randomWord(gen.getRnd(), verbsEmo).getLemma();
			StartLetterFilter letFil = new StartLetterFilter(verbResults[0].charAt(0));
			Set<Word> letFilVerbsEmo = letFil.filterSet(verbsEmo);
			for (int i = 1; i < verbResults.length; i++) {
				verbResults[i] = RandomUtil.randomWord(gen.getRnd(), letFilVerbsEmo).getLemma();
			}
			
		} catch (NoMorGenerationPossibleException e) {
			//Generate slogan from emotion less word list
			verbResults[0] = RandomUtil.randomWordPreferEmotion(gen.getRnd(), verbs, verbsEmo).getLemma();
			StartLetterFilter letFil = new StartLetterFilter(verbResults[0].charAt(0));
			Set<Word> letFilVerbs = letFil.filterSet(verbs);
			for (int i = 1; i < verbResults.length; i++) {
				verbResults[i] = RandomUtil.randomWordPreferEmotion(gen.getRnd(), letFilVerbs, verbsEmo).getLemma();
			}
		}

		

		return generateResultString(verbResults);
	}

	/**
	 * Helper method parsing the result string for a slogan
	 * @param verbs Verbs to use for slogan
	 * @return Slogan as {@link String}
	 */
	private String generateResultString(String[] verbs) {
		String result = verbs[0];
		for (int i = 1; i < verbs.length; i++) {
			result += ", " + verbs[i];
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see generation.patterns.AbstractPattern#getPossibleStylisticDevices()
	 */
	@Override
	public StylisticDevice[] getPossibleStylisticDevices() {
		return new StylisticDevice[] { StylisticDevice.None, StylisticDevice.Alliteration };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PatternVBVBVB";
	}

	/* (non-Javadoc)
	 * @see de.koch.uim_project.generation.pattern.AbstractPattern#getPatternType()
	 */
	@Override
	public Pattern getPatternType() {
		return Pattern.VBVBVB;
	}

}
