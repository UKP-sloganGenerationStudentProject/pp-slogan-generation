package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.emotions.EmotionModel;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.Resources;
import de.tudarmstadt.ukp.lmf.model.core.LexicalEntry;
import de.tudarmstadt.ukp.lmf.model.core.Sense;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;
import de.tudarmstadt.ukp.lmf.model.meta.SemanticLabel;
import de.tudarmstadt.ukp.lmf.model.semantics.Synset;

public class NounChunkPart
    extends ChunkPart
{
    public NounChunkPart()
    {
        super();
        this._isValueDerivable = true;
    }

    @Override
    public ArrayList<ChunkPartSolution> generate(final Resources resources,ChunkPart orginialPart)
    {

        ArrayList<ChunkPartSolution> output = new ArrayList<>();
        ChunkPartSolution basicSolution = new ChunkPartSolution(orginialPart, null, _lemma);
        if(hasConstraint())
        {
            basicSolution.addConstraintId(this.getConstraintId());
        }
        output.add(basicSolution);

        /*
         * we don't want this because would make mix the different expression between the different part of the body
         */
//
//        if(PatternGenerator.getSelectablePartsOfBody().contains(this._lemma) && !PatternGenerator.NO_BODY_PART.equals(resources.getSelectedBodyPart()))
//        {
//            output.add(resources.getSelectedBodyPart());
//            return output;
//        }


        if (!this._isValueDerivable || !resources.isUbyGernationAllowed()
                || resources.getSelectedBodyPart().equals(this._lemma)) {
            return output;
        }



        final String ubySemantic = "noun." + this._semanticValue;

        // System.out.println("[");
        // System.out.println(ubySemantic);
        // System.out.println(_lemma);
        // System.out.println("]");

        final List<LexicalEntry> lexicalEntries = resources.getUby().getLexicalEntries(this._lemma,
                EPartOfSpeech.noun, null);

        for (final LexicalEntry lexEntry : lexicalEntries) {

            for (final Sense sense : lexEntry.getSenses()) {

                /*
                if(output.size()>5)
                {
                    return output;
                }
                */

                final Synset synset = sense.getSynset();

                if (synset == null) {
                    continue;
                }

                for (final Sense sense2 : synset.getSenses()) {
                    // System.out.println("\t"+sense2.getLexicalEntry().getLemmaForm());

                    for (final SemanticLabel sem : sense2.getSemanticLabels()) {
                        // System.out.println("\t\t"+sem.getLabel());

                        if (sem.getLabel().equals(ubySemantic)) {
                            final String word = sense2.getLexicalEntry().getLemmaForm();
                            boolean isNegativeConnotation = false;
                            final EmotionModel emotion = resources.getEmotionAnalizer().getEmotion(
                                    word);
                            if (emotion != null) {
                                isNegativeConnotation = emotion.isNegative();
                            }

                            if(isNegativeConnotation)
                            {
                                continue;
                            }

                            boolean isFreqToSmall = false;

                            final String[] subwords = word.split(" ");
                            final List<String> subwordWfreqs = new ArrayList<String>();

                            try {
                                for (final String subword : subwords) {
                                    if (subword.length() == 0) {
                                        continue;
                                    }
                                    final long freq = resources.getWordStatistic().getFrequency(
                                            subword);
                                    if (freq < 1000) {
                                        isFreqToSmall = true;
                                        break;
                                    }
                                    subwordWfreqs.add("(" + subword + ")[" + freq + "] ");
                                }
                            }
                            catch (final IOException e) {
                            }

                            if (isFreqToSmall) {
                                System.out.println("Too small frequence : " + word);
                                continue;
                            }

//                            String wordfreq = "";
//
//                            for (final String val : subwordWfreqs) {
//                                wordfreq = wordfreq + val;
//                            }


                            ChunkPartSolution ncps = new ChunkPartSolution(this, null, word);
                            if(hasConstraint())
                            {
                                ncps.addConstraintId(this.getConstraintId());
                            }
                            if(!output.contains(ncps))
                            {
                                output.add(ncps);
                            }
                        }
                    }
                }
            }
        }
        return output;
    }

}
