package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.emotions.EmotionModel;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Parameters;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Resources;
import de.tudarmstadt.ukp.lmf.model.core.LexicalEntry;
import de.tudarmstadt.ukp.lmf.model.core.Sense;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;
import de.tudarmstadt.ukp.lmf.model.meta.SemanticLabel;
import de.tudarmstadt.ukp.lmf.model.semantics.Synset;

/**
*
*Inherits from {@link ChunkPart}.
 * For nouns.
 *
 * @author Matthieu Fraissinet-Tachet
*/

public class NounChunkPart
    extends ChunkPart
{

    private static final long serialVersionUID = 9147130913729196541L;

    public NounChunkPart()
    {
        super();
        this._isValueDerivable = true;
    }

    @Override
    public void generateInformation()
    {

    }

    @Override
    public ArrayList<ChunkPartSolution> generateChunkPartSolution(final Resources resources,ChunkPart orginialPart)
    {

        ArrayList<ChunkPartSolution> output = new ArrayList<>();
        ChunkPartSolution basicSolution = new ChunkPartSolution(orginialPart, null, _lemma);
        if(hasConstraint())
        {
            basicSolution.addConstraintId(this.getConstraintId());
        }

        if(resources.hasBodyPartConstraint())
        {
            if(resources.getSelectedBodyPart().equals(this._lemma))
            {
                basicSolution.setHasBodyPart(true);
            }
            else
            {
                if(Parameters.getSelectablePartsOfBody().contains(this._lemma))
                {
                    //in this case we are not interested in this chunkPart because it is a body part that is not the one we are looking for
                    //we return an empty list
                    return output;
                }
            }
        }
        else
        {
            if(Parameters.getSelectablePartsOfBody().contains(this._lemma))
            {
                //in this case we are not interested in this chunkPart because it is a body part and we don't want any body part
                //we return an empty list
                return output;
            }
        }


        output.add(basicSolution);

        if (!this._isValueDerivable || !resources.isUbyGernationAllowed()
                || basicSolution.hasBodyPart()) {
            //we dont derive the value if
            // - _isValueDerivable is false
            // - it is not allowed with uby
            // - the corresponding chunkpart corresponds to the selected part of body
            return output;
        }



        final String ubySemantic = "noun." + this._semanticValue;


        final List<LexicalEntry> lexicalEntries = resources.getUby().getLexicalEntries(this._lemma,
                EPartOfSpeech.noun, null);

        for (final LexicalEntry lexEntry : lexicalEntries) {

            for (final Sense sense : lexEntry.getSenses()) {


                final Synset synset = sense.getSynset();

                if (synset == null) {
                    continue;
                }

                for (final Sense sense2 : synset.getSenses()) {
                    for (final SemanticLabel sem : sense2.getSemanticLabels()) {
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

                            boolean isFreqToSmall = true;

                            final String[] subwords = word.split(" ");

//                            for(String subword : subwords)
//                            {
//                                if (subword.length() == 0) {
//                                  continue;
//                              }

//                                if(!resources.checkLemmaInCosmeticsCorpus(subword))
//                                {
//                                    isLemmaAllowed = false;
//                                    break;
//                                }
//                            }

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
