package de.koch.uim_project.generation;

import simplenlg.features.Feature;
import simplenlg.features.Tense;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

/**
 * This class allows transformation of an verb to a simple past
 * @author Frerik Koch
 *
 */
public class VerbConjugator
{

    private static VerbConjugator instance;

    /**
     * Empty constructor private according to singleton pattern.
     * 
     */
    private VerbConjugator()
    {

    }

    /**
     * According to singelton pattern
     * @return Instance of {@link VerbConjugator}
     */
    public static VerbConjugator getInstance()
    {
        if (instance == null) {
            instance = new VerbConjugator();
        }
        return instance;
    }

    /**
     * Transforms a verb to simple past
     * @param word Verb in infinitve to transform
     * @return Verb in simple past
     */
    public String getVVNForm(final Word word)
    {
        String result = null;
        final Lexicon lexicon = Lexicon.getDefaultLexicon();
        final NLGFactory nlgFactory = new NLGFactory(lexicon);
        final Realiser realiser = new Realiser(lexicon);

        final SPhraseSpec p = nlgFactory.createClause();
        p.setVerb(word.getLemma());
        p.setFeature(Feature.TENSE, Tense.PAST);

        // The result of the realiser is a sentence and therefore starts with an upper letter and
        // ends with a dot
        result = realiser.realiseSentence(p).toLowerCase().replaceAll("\\.", "");

        return result;
    }

}
