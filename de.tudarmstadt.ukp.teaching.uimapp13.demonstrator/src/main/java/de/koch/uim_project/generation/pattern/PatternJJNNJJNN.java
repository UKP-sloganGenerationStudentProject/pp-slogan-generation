package de.koch.uim_project.generation.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.koch.uim_project.analyse.UbyAnalyser;
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
import de.tudarmstadt.ukp.lmf.model.enums.ERelTypeSemantics;

/**
 * This class represents the pattern "JJ NN. JJ NN." E.g.:
 * "New universe, new rules"
 * 
 * @author Frerik Koch
 * 
 */
public class PatternJJNNJJNN
    extends AbstractPattern
{

    /**
     * Initializes a new {@link PatternJJNNJJNN}
     * 
     * @throws DbException
     * @throws PatternNotInitializeableException
     */
    public PatternJJNNJJNN(final Config config, final Generator gen)
        throws DbException, PatternNotInitializeableException
    {
        super(config, gen);
    }

    /*
     * (non-Javadoc)
     * 
     * @see generation.patterns.AbstractPattern#generateSloganResult(generation.
     * StylisticDevice)
     */
    @Override
    public String generateSlogan(final StylisticDevice stylisticDevice)
        throws DbException, SloganNotCreatedException
    {
        switch (stylisticDevice) {
        case Parallelism:
            return this.generateParallelism();
        case Oxymoron:
            return this.generateOxymoron();
        case None:
            return this.generateNoStylisticDevice();
        default:
            throw new SloganNotCreatedException("Stylistic Device not expected for this pattern: "
                    + stylisticDevice);
        }
    }

    /**
     * This method tries to generate a slogan with the {@link StylisticDevice}
     * oxymoron. This is done by choosing the first noun and searching for
     * possible oxymorons for the second noun. The Algorithm uses a hit or miss
     * strategy since it can't be guaranteed that an oxymoron is found. It
     * depends on the feature words (user input) and on the chosen first noun.
     * the oxymoron word is chosen from the same word list as the other words.
     * Any word is chosen as fallback solution.
     * 
     * @return Slogan with stylistic device oxymoron
     * @throws DbException
     * @throws SloganNotCreatedException
     */
    private String generateOxymoron()
        throws DbException, SloganNotCreatedException
    {
        int synsetDepth = 0;
        Word adj1Result, adj2Result, noun1Result, noun2Result;
        final BaseWordListGen wordGen = this.gen.getGlobalWordListGen();

        // Init filters
        final PosFilter nounFilter = new PosFilter(EPartOfSpeech.noun);
        final PosFilter adjFilter = new PosFilter(EPartOfSpeech.adjective);
        final EmotionFilter emoFilter = new EmotionFilter(this.config.getEmotion());
        final CombinedSetFilter nounEmo = new CombinedSetFilter(nounFilter, emoFilter);
        final CombinedSetFilter adjEmo = new CombinedSetFilter(adjFilter, emoFilter);

        // Init word lists
        final Set<Word> adjs = adjFilter.filterSet(wordGen.getInitialSet());
        final Set<Word> nouns = nounFilter.filterSet(wordGen.getInitialSet());
        final Set<Word> adjsEmo = emoFilter.filterSet(adjs);
        final Set<Word> nounsEmo = emoFilter.filterSet(nouns);

        try {
            while (nounsEmo.size() <= this.config.getMinWordlistForGeneration()
                    || adjsEmo.size() <= this.config.getMinWordlistForGeneration()) {
                synsetDepth++;

                if (nouns.size() <= this.config.getMinWordlistForGeneration()
                        || adjs.size() <= this.config.getMinWordlistForGeneration()) {
                    // Increase all word lists
                    nouns.addAll(nounFilter.filterSet(wordGen.getSynsetDepthWords(synsetDepth)));
                    adjs.addAll(adjFilter.filterSet(wordGen.getSynsetDepthWords(synsetDepth)));
                    nounsEmo.addAll(emoFilter.filterSet(nouns));
                    adjsEmo.addAll(emoFilter.filterSet(adjs));
                }
                else {
                    // Increase emotion full lists
                    nounsEmo.addAll(nounEmo.filterSet(wordGen.getSynsetDepthWords(synsetDepth)));
                    adjsEmo.addAll(adjEmo.filterSet(wordGen.getSynsetDepthWords(synsetDepth)));
                }

            }

            // Choose words from emotion full lists
            noun1Result = RandomUtil.randomWord(this.gen.getRnd(), nounsEmo);
            noun2Result = RandomUtil.randomWord(this.gen.getRnd(), nounsEmo);
            adj1Result = RandomUtil.findAdjectiveToNoun(this.gen.getRnd(),
                    this.gen.getW1tSearcher(), adjsEmo, noun1Result);
            adj2Result = this.searchOxymoron(adj1Result, true);

        }
        catch (final NoMorGenerationPossibleException e) {
            // Choose words from emotion less lists but prefer emotion full words

            noun1Result = RandomUtil.randomWordPreferEmotion(this.gen.getRnd(), nouns, nounsEmo);
            noun2Result = RandomUtil.randomWordPreferEmotion(this.gen.getRnd(), nouns, nounsEmo);
            adj1Result = RandomUtil.findAdjectiveToNoun(this.gen.getRnd(),
                    this.gen.getW1tSearcher(), adjs, adjsEmo, noun1Result);
            adj2Result = this.searchOxymoron(adj1Result, true);
        }

        return this.generateResultString(adj1Result, noun1Result, adj2Result, noun2Result);
    }

    /**
     * Helper method to search for fitting oxymorons
     * @param toSearchFor {@link Word} to search oxymorons for
     * @param useEmotion <code>true</code>: try to filter by emotion
     * @return Oxymoron toSearchFor
     * @throws SloganNotCreatedException
     * @throws DbException
     */
    private Word searchOxymoron(final Word toSearchFor, final boolean useEmotion)
        throws SloganNotCreatedException, DbException
    {
        int synsetDepth = 0;
        final BaseWordListGen wordGen = this.gen.getGlobalWordListGen();

        // Init filters
        final PosFilter nounFilter = new PosFilter(EPartOfSpeech.noun);
        final EmotionFilter emoFilter = new EmotionFilter(this.config.getEmotion());
        final CombinedSetFilter nounEmo = new CombinedSetFilter(nounFilter, emoFilter);

        // Init parameters
        final List<String> relNames = new ArrayList<String>();
        final List<ERelTypeSemantics> relTypes = new ArrayList<ERelTypeSemantics>();
        relNames.add("antonym");
        relTypes.add(ERelTypeSemantics.complementary);

        // Get oxymorons from uby
        final Set<Word> oxymorons = UbyAnalyser.getInstance().getRelatedWordsSense(toSearchFor,
                relNames, relTypes, this.gen.getUby());

        // No oxymorons found
        if (oxymorons.size() < 1) {
            throw new SloganNotCreatedException("No oxymorons found");
        }

        // One oxymoron found: return it directly
        if (oxymorons.size() == 1) {
            return oxymorons.iterator().next();
        }

        // Multiple oxymorons: try to find one with correct properties
        final Set<Word> result = nounFilter.filterSet(wordGen.getInitialSet());
        result.retainAll(oxymorons);
        final Set<Word> resultEmo = emoFilter.filterSet(result);
        try {
            while (resultEmo.size() < this.config.getMinWordlistForGeneration()) {
                synsetDepth++;
                try {
                    if (result.size() < this.config.getMinWordlistForGeneration()) {
                        result.addAll(wordGen.getSynsetDepthWords(synsetDepth));
                        result.retainAll(oxymorons);
                        resultEmo.addAll(emoFilter.filterSet(result));
                    }
                    else {
                        resultEmo
                                .addAll(nounEmo.filterSet(wordGen.getSynsetDepthWords(synsetDepth)));
                        resultEmo.retainAll(oxymorons);
                    }
                }
                catch (final NoMorGenerationPossibleException e) {
                    return RandomUtil.randomWordPreferEmotion(this.gen.getRnd(), result, resultEmo);
                }
            }
            return RandomUtil.randomWord(this.gen.getRnd(), resultEmo);
        }
        catch (final SloganNotCreatedException e) {
            return RandomUtil.randomWord(this.gen.getRnd(), oxymorons);
        }

    }

    /**
     * Generates a slogan with no stylistic device
     * 
     * @return Generation result
     * @throws DbException
     * @throws SloganNotCreatedException
     */
    private String generateNoStylisticDevice()
        throws DbException, SloganNotCreatedException
    {
        final BaseWordListGen wordGen = this.gen.getGlobalWordListGen();
        Word adj1, adj2, noun1, noun2;
        int synsetDepth = 0;

        // Init filters
        final PosFilter nounFilter = new PosFilter(EPartOfSpeech.noun);
        final PosFilter adjFilter = new PosFilter(EPartOfSpeech.adjective);
        final EmotionFilter emoFilter = new EmotionFilter(this.config.getEmotion());
        final CombinedSetFilter emoAndNoun = new CombinedSetFilter(nounFilter, emoFilter);
        final CombinedSetFilter emoAndAdj = new CombinedSetFilter(adjFilter, emoFilter);

        // Init word lists
        final Set<Word> nouns = nounFilter.filterSet(wordGen.getInitialSet());
        final Set<Word> adjs = adjFilter.filterSet(wordGen.getInitialSet());
        final Set<Word> nounsEmo = emoFilter.filterSet(nouns);
        final Set<Word> adjsEmo = emoFilter.filterSet(adjs);

        try {
            while (nounsEmo.size() < this.config.getMinWordlistForGeneration()
                    || adjsEmo.size() < this.config.getMinWordlistForGeneration()) {
                synsetDepth++;

                if (nouns.size() < this.config.getMinWordlistForGeneration()
                        || adjs.size() < this.config.getMinWordlistForGeneration()) {
                    // increase all word lists
                    nouns.addAll(nounFilter.filterSet(wordGen.getSynsetDepthWords(synsetDepth)));
                    adjs.addAll(adjFilter.filterSet(wordGen.getSynsetDepthWords(synsetDepth)));
                    nounsEmo.addAll(emoFilter.filterSet(nouns));
                    adjs.addAll(emoFilter.filterSet(adjs));
                }
                else {
                    // increase only emotion full word lists
                    nounsEmo.addAll(emoAndNoun.filterSet(wordGen.getSynsetDepthWords(synsetDepth)));
                    adjsEmo.addAll(emoAndAdj.filterSet(wordGen.getSynsetDepthWords(synsetDepth)));
                }

            }

            // create slogan from emotion full word lists
            noun1 = RandomUtil.randomWord(this.gen.getRnd(), nounsEmo);
            noun2 = RandomUtil.randomWord(this.gen.getRnd(), nounsEmo);
            adj1 = RandomUtil.findAdjectiveToNoun(this.gen.getRnd(), this.gen.getW1tSearcher(),
                    adjsEmo, noun1);
            adj2 = RandomUtil.findAdjectiveToNoun(this.gen.getRnd(), this.gen.getW1tSearcher(),
                    adjsEmo, noun2);

        }
        catch (final NoMorGenerationPossibleException e) {
            // create slogan from emotion less word lists but prefer emotion full words
            noun1 = RandomUtil.randomWordPreferEmotion(this.gen.getRnd(), nouns, nounsEmo);
            noun2 = RandomUtil.randomWordPreferEmotion(this.gen.getRnd(), nouns, nounsEmo);
            adj1 = RandomUtil.findAdjectiveToNoun(this.gen.getRnd(), this.gen.getW1tSearcher(),
                    adjs, adjsEmo, noun1);
            adj2 = RandomUtil.findAdjectiveToNoun(this.gen.getRnd(), this.gen.getW1tSearcher(),
                    adjs, adjsEmo, noun2);
        }

        return this.generateResultString(adj1, noun1, adj2, noun2);

    }

    /**
     * Generates a slogan with parallelism
     * 
     * @return Generation result
     * @throws DbException
     * @throws SloganNotCreatedException
     */
    private String generateParallelism()
        throws DbException, SloganNotCreatedException
    {
        Word adj, noun1, noun2;
        int synsetDepth = 0;
        final BaseWordListGen wordGen = this.gen.getGlobalWordListGen();

        // Init filters
        final PosFilter adjFilter = new PosFilter(EPartOfSpeech.adjective);
        final PosFilter nounFilter = new PosFilter(EPartOfSpeech.noun);
        final EmotionFilter emoFilter = new EmotionFilter(this.config.getEmotion());
        final CombinedSetFilter nounAndEmo = new CombinedSetFilter(nounFilter, emoFilter);
        final CombinedSetFilter adjAndEmo = new CombinedSetFilter(adjFilter, emoFilter);

        // Init word lists
        final Set<Word> adjs = adjFilter.filterSet(wordGen.getInitialSet());
        final Set<Word> nouns = nounFilter.filterSet(wordGen.getInitialSet());
        final Set<Word> nounsEmo = emoFilter.filterSet(nouns);
        final Set<Word> adjsEmo = emoFilter.filterSet(adjs);

        try {
            while (nounsEmo.size() < this.config.getMinWordlistForGeneration()
                    || adjsEmo.size() < this.config.getMinWordlistForGeneration()) {

                synsetDepth++;
                if (nouns.size() < this.config.getMinWordlistForGeneration()
                        || adjs.size() < this.config.getMinWordlistForGeneration()) {
                    // Increase all word lists
                    nouns.addAll(nounFilter.filterSet(wordGen.getSynsetDepthWords(synsetDepth)));
                    adjs.addAll(adjFilter.filterSet(wordGen.getSynsetDepthWords(synsetDepth)));
                    nounsEmo.addAll(emoFilter.filterSet(nouns));
                    adjsEmo.addAll(emoFilter.filterSet(adjs));
                }
                else {
                    // Increase only emotion full word lists
                    nounsEmo.addAll(nounAndEmo.filterSet(wordGen.getSynsetDepthWords(synsetDepth)));
                    adjsEmo.addAll(adjAndEmo.filterSet(wordGen.getSynsetDepthWords(synsetDepth)));
                }

            }

            // Generate slogan from emotion full word lists
            noun1 = RandomUtil.randomWord(this.gen.getRnd(), nounsEmo);
            noun2 = RandomUtil.randomWord(this.gen.getRnd(), nounsEmo);
            adj = RandomUtil.findAdjectiveToNoun(this.gen.getRnd(), this.gen.getW1tSearcher(),
                    adjsEmo, noun1);

        }
        catch (final NoMorGenerationPossibleException e) {
            // create slogan from emotion less word lists but prefer emotion full words
            noun1 = RandomUtil.randomWordPreferEmotion(this.gen.getRnd(), nouns, nounsEmo);
            noun2 = RandomUtil.randomWordPreferEmotion(this.gen.getRnd(), nouns, nounsEmo);
            adj = RandomUtil.findAdjectiveToNoun(this.gen.getRnd(), this.gen.getW1tSearcher(),
                    adjs, adjsEmo, noun1);
        }

        return this.generateResultString(adj, noun1, adj, noun2);
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
    private String generateResultString(final Word firstAdj, final Word firstNoun,
            final Word secondAdj, final Word secondNoun)
    {
        return firstAdj.getLemma() + " " + firstNoun.getLemma() + ". " + secondAdj.getLemma() + " "
                + secondNoun.getLemma() + ".";
    }

    /*
     * (non-Javadoc)
     * 
     * @see generation.patterns.AbstractPattern#getPossibleStylisticDevices()
     */
    @Override
    public StylisticDevice[] getPossibleStylisticDevices()
    {
        return new StylisticDevice[] { StylisticDevice.None, StylisticDevice.Parallelism,
                StylisticDevice.Oxymoron };
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "PatternJJNNJJNN";
    }

    @Override
    public Pattern getPatternType()
    {
        return Pattern.JJNNJJNN;
    }

}
