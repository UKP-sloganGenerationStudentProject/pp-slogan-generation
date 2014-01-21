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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.koch.uim_project.util.Config;
import de.koch.uim_project.util.Pattern;
import de.koch.uim_project.util.StylisticDevice;
import de.koch.uim_project.analyse.UbyAnalyser;
import de.koch.uim_project.database.DbException;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;
import de.tudarmstadt.ukp.lmf.model.enums.ERelTypeSemantics;

/**
 * This class represents the pattern "JJ NN. JJ NN." E.g.:
 * "New universe, new rules"
 * 
 * @author Frerik Koch
 * 
 */
public class PatternJJNNJJNN extends AbstractPattern {

	/**
	 * Initializes a new {@link PatternJJNNJJNN}
	 * 
	 * @throws DbException
	 * @throws PatternNotInitializeableException
	 */
	public PatternJJNNJJNN(Config config, Generator gen) throws DbException, PatternNotInitializeableException {
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
		case Parallelism:
			return generateParallelism();
		case Oxymeron:
			return generateOxymeron();
		case None:
			return generateNoStylisticDevice();
		default:
			throw new SloganNotCreatedException("Stylistic Device not expected for this pattern: " + stylisticDevice);
		}
	}

	/**
	 * This method tries to generate a slogan with the stylistic device
	 * oxymeron. This is done by choosing the first noun and searching for
	 * possible oxymerons for the second noun. The Algorithm uses a hit or miss
	 * strategy since it can't be guarenteed that an oxymeron is found. It
	 * depends on the feature words (user input) and on the choosen first noun.
	 * the oxymeron word is choosen from the same word list as the other words.
	 * It would be possible to choose any word but then there would be no
	 * semantic coherencie at all.
	 * 
	 * @return Slogan with stylistic device oxymeron
	 * @throws DbException
	 * @throws SloganNotCreatedException
	 */
	private String generateOxymeron() throws DbException, SloganNotCreatedException {
		int synsetDepth = 0;
		Word adj1Result, adj2Result, noun1Result, noun2Result;
		BaseWordListGen wordGen = gen.getGlobalWordListGen();

		PosFilter nounFilter = new PosFilter(EPartOfSpeech.noun);
		PosFilter adjFilter = new PosFilter(EPartOfSpeech.adjective);
		EmotionFilter emoFilter = new EmotionFilter(config.getEmotion());
		CombinedSetFilter nounEmo = new CombinedSetFilter(nounFilter, emoFilter);
		CombinedSetFilter adjEmo = new CombinedSetFilter(adjFilter, emoFilter);

		Set<Word> adjs = adjFilter.filterSet(wordGen.getInitialSet());
		Set<Word> nouns = nounFilter.filterSet(wordGen.getInitialSet());
		Set<Word> adjsEmo = emoFilter.filterSet(adjs);
		Set<Word> nounsEmo = emoFilter.filterSet(nouns);

		try {
			while (nounsEmo.size() <= config.getMinWordlistForGeneration() || adjsEmo.size() <= config.getMinWordlistForGeneration()) {
				synsetDepth++;

				if (nouns.size() <= config.getMinWordlistForGeneration() || adjs.size() <= config.getMinWordlistForGeneration()) {
					nouns.addAll(nounFilter.filterSet(wordGen.getMore(synsetDepth)));
					adjs.addAll(adjFilter.filterSet(wordGen.getMore(synsetDepth)));
					nounsEmo.addAll(emoFilter.filterSet(nouns));
					adjsEmo.addAll(emoFilter.filterSet(adjs));
				} else {
					nounsEmo.addAll(nounEmo.filterSet(wordGen.getMore(synsetDepth)));
					adjsEmo.addAll(adjEmo.filterSet(wordGen.getMore(synsetDepth)));
				}

			}

			adj1Result = RandomUtil.randomWord(gen.getRnd(), adjsEmo);
			adj2Result = searchOxymeron(adj1Result, true);
			noun1Result = RandomUtil.randomWord(gen.getRnd(), nounsEmo);
			noun2Result = RandomUtil.randomWord(gen.getRnd(), nounsEmo);

		} catch (NoMorGenerationPossibleException e) {
			adj1Result = RandomUtil.randomWord(gen.getRnd(), adjs, config.getEmotion());
			noun1Result = RandomUtil.randomWord(gen.getRnd(), nouns, config.getEmotion());
			noun2Result = RandomUtil.randomWord(gen.getRnd(), nouns, config.getEmotion());
			adj2Result = searchOxymeron(adj1Result, true);
		}

		return generateResultString(adj1Result, noun1Result, adj2Result, noun2Result);
	}

	private Word searchOxymeron(Word toSearchFor, boolean useEmotion) throws SloganNotCreatedException, DbException {
		int synsetDepth = 0;
		BaseWordListGen wordGen = gen.getGlobalWordListGen();

		PosFilter nounFilter = new PosFilter(EPartOfSpeech.noun);
		EmotionFilter emoFilter = new EmotionFilter(config.getEmotion());
		CombinedSetFilter nounEmo = new CombinedSetFilter(nounFilter, emoFilter);

		List<String> relNames = new ArrayList<String>();
		List<ERelTypeSemantics> relTypes = new ArrayList<ERelTypeSemantics>();
		relNames.add("antonym");
		relTypes.add(ERelTypeSemantics.complementary);
		Set<Word> oxymerons = UbyAnalyser.getInstance().getRelatedWordsSense(toSearchFor, relNames, relTypes, gen.getUby());
		if (oxymerons.size() < 1) {
			throw new SloganNotCreatedException("No oxymerons found");
		}
		if (oxymerons.size() == 1) {
			return oxymerons.iterator().next();
		}
		Set<Word> result = nounFilter.filterSet(wordGen.getInitialSet());
		result.retainAll(oxymerons);
		Set<Word> resultEmo = emoFilter.filterSet(result);
		try {
			while (resultEmo.size() < config.getMinWordlistForGeneration()) {
				synsetDepth++;
				try {
					if (result.size() < config.getMinWordlistForGeneration()) {
						result.addAll(wordGen.getMore(synsetDepth));
						result.retainAll(oxymerons);
						resultEmo.addAll(emoFilter.filterSet(result));
					} else {
						resultEmo.addAll(nounEmo.filterSet(wordGen.getMore(synsetDepth)));
						resultEmo.retainAll(oxymerons);
					}
				} catch (NoMorGenerationPossibleException e) {
					return RandomUtil.randomWord(gen.getRnd(), result, config.getEmotion());
				}
			}
			return RandomUtil.randomWord(gen.getRnd(), resultEmo);
		} catch (SloganNotCreatedException e) {
			return RandomUtil.randomWord(gen.getRnd(), oxymerons);
		}

	}

	/**
	 * Generates a slogan with no stylistic device
	 * 
	 * @return Generation result
	 * @throws DbException
	 * @throws SloganNotCreatedException
	 */
	private String generateNoStylisticDevice() throws DbException, SloganNotCreatedException {
		BaseWordListGen wordGen = gen.getGlobalWordListGen();
		Word adj1, adj2, noun1, noun2;
		int synsetDepth = 0;

		PosFilter nounFilter = new PosFilter(EPartOfSpeech.noun);
		PosFilter adjFilter = new PosFilter(EPartOfSpeech.adjective);
		EmotionFilter emoFilter = new EmotionFilter(config.getEmotion());
		CombinedSetFilter emoAndNoun = new CombinedSetFilter(nounFilter, emoFilter);
		CombinedSetFilter emoAndAdj = new CombinedSetFilter(adjFilter, emoFilter);

		Set<Word> nouns = nounFilter.filterSet(wordGen.getInitialSet());
		Set<Word> adjs = adjFilter.filterSet(wordGen.getInitialSet());
		Set<Word> nounsEmo = emoFilter.filterSet(nouns);
		Set<Word> adjsEmo = emoFilter.filterSet(adjs);

		try {
			while (nounsEmo.size() < config.getMinWordlistForGeneration() || adjsEmo.size() < config.getMinWordlistForGeneration()) {
				synsetDepth++;

				if (nouns.size() < config.getMinWordlistForGeneration() || adjs.size() < config.getMinWordlistForGeneration()) {
					nouns.addAll(nounFilter.filterSet(wordGen.getMore(synsetDepth)));
					adjs.addAll(adjFilter.filterSet(wordGen.getMore(synsetDepth)));
					nounsEmo.addAll(emoFilter.filterSet(nouns));
					adjs.addAll(emoFilter.filterSet(adjs));
				} else {
					nounsEmo.addAll(emoAndNoun.filterSet(wordGen.getMore(synsetDepth)));
					adjsEmo.addAll(emoAndAdj.filterSet(wordGen.getMore(synsetDepth)));
				}

			}

			adj1 = RandomUtil.randomWord(gen.getRnd(), adjsEmo);
			adj2 = RandomUtil.randomWord(gen.getRnd(), adjsEmo);
			noun1 = RandomUtil.randomWord(gen.getRnd(), nounsEmo);
			noun2 = RandomUtil.randomWord(gen.getRnd(), nounsEmo);

		} catch (NoMorGenerationPossibleException e) {
			adj1 = RandomUtil.randomWord(gen.getRnd(), adjs, config.getEmotion());
			adj2 = RandomUtil.randomWord(gen.getRnd(), adjs, config.getEmotion());
			noun1 = RandomUtil.randomWord(gen.getRnd(), nouns, config.getEmotion());
			noun2 = RandomUtil.randomWord(gen.getRnd(), nouns, config.getEmotion());

		}

		return generateResultString(adj1, noun1, adj2, noun2);

	}

	/**
	 * Generates a slogan with parallelism
	 * 
	 * @return Generation result
	 * @throws DbException
	 * @throws SloganNotCreatedException
	 */
	private String generateParallelism() throws DbException, SloganNotCreatedException {
		Word adj, noun1, noun2;
		int synsetDepth = 0;
		BaseWordListGen wordGen = gen.getGlobalWordListGen();

		PosFilter adjFilter = new PosFilter(EPartOfSpeech.adjective);
		PosFilter nounFilter = new PosFilter(EPartOfSpeech.noun);
		EmotionFilter emoFilter = new EmotionFilter(config.getEmotion());
		CombinedSetFilter nounAndEmo = new CombinedSetFilter(nounFilter, emoFilter);
		CombinedSetFilter adjAndEmo = new CombinedSetFilter(adjFilter, emoFilter);

		Set<Word> adjs = adjFilter.filterSet(wordGen.getInitialSet());
		Set<Word> nouns = nounFilter.filterSet(wordGen.getInitialSet());
		Set<Word> nounsEmo = emoFilter.filterSet(nouns);
		Set<Word> adjsEmo = emoFilter.filterSet(adjs);

		try {
			while (nounsEmo.size() < config.getMinWordlistForGeneration() || adjsEmo.size() < config.getMinWordlistForGeneration()) {

				synsetDepth++;
				if (nouns.size() < config.getMinWordlistForGeneration() || adjs.size() < config.getMinWordlistForGeneration()) {
					nouns.addAll(nounFilter.filterSet(wordGen.getMore(synsetDepth)));
					adjs.addAll(adjFilter.filterSet(wordGen.getMore(synsetDepth)));
					nounsEmo.addAll(emoFilter.filterSet(nouns));
					adjsEmo.addAll(emoFilter.filterSet(adjs));
				} else {
					nounsEmo.addAll(nounAndEmo.filterSet(wordGen.getMore(synsetDepth)));
					adjsEmo.addAll(adjAndEmo.filterSet(wordGen.getMore(synsetDepth)));
				}

			}

			adj = RandomUtil.randomWord(gen.getRnd(), adjsEmo);
			noun1 = RandomUtil.randomWord(gen.getRnd(), nounsEmo);
			noun2 = RandomUtil.randomWord(gen.getRnd(), nounsEmo);

		} catch (NoMorGenerationPossibleException e) {
			adj = RandomUtil.randomWord(gen.getRnd(), adjs, config.getEmotion());
			noun1 = RandomUtil.randomWord(gen.getRnd(), nouns, config.getEmotion());
			noun2 = RandomUtil.randomWord(gen.getRnd(), nouns, config.getEmotion());
		}

		return generateResultString(adj, noun1, adj, noun2);
	}

	/**
	 * Generates the slogan string according to pattern
	 * 
	 * @param firstAdj
	 * @param firstNoun
	 * @param secondAdj
	 * @param secondNoun
	 * @return
	 */
	private String generateResultString(Word firstAdj, Word firstNoun, Word secondAdj, Word secondNoun) {
		return firstAdj.getLemma() + " " + firstNoun.getLemma() + ". " + secondAdj.getLemma() + " " + secondNoun.getLemma() + ".";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see generation.patterns.AbstractPattern#getPossibleStylisticDevices()
	 */
	@Override
	public StylisticDevice[] getPossibleStylisticDevices() {
		return new StylisticDevice[] { StylisticDevice.None, StylisticDevice.Parallelism, StylisticDevice.Oxymeron };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PatternJJNNJJNN";
	}

	@Override
	public Pattern getPatternType() {
		return Pattern.JJNNJJNN;
	}

}
