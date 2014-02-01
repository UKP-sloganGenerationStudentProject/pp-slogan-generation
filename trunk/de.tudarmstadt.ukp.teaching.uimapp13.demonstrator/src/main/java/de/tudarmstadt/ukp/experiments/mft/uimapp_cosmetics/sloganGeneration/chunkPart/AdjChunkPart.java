package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.emotions.EmotionModel;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Resources;
import de.tudarmstadt.ukp.lmf.model.core.LexicalEntry;
import de.tudarmstadt.ukp.lmf.model.core.Sense;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;
import de.tudarmstadt.ukp.lmf.model.meta.SemanticLabel;
import de.tudarmstadt.ukp.lmf.model.semantics.Synset;


public class AdjChunkPart
extends ChunkPart
{
    public AdjChunkPart()
    {
        super();
        _isValueDerivable = true;
    }

    @Override
    public ArrayList<ChunkPartSolution> generate(Resources resources, ChunkPart orginialPart)
    {

        ArrayList<ChunkPartSolution> output = new ArrayList<>();
        ChunkPartSolution basicSolution = new ChunkPartSolution(orginialPart, null, _lemma);
        if(hasConstraint())
        {
            basicSolution.addConstraintId(this.getConstraintId());
        }
        output.add(basicSolution);

        if(!_isValueDerivable || !resources.isUbyGernationAllowed())
        {
            return output;
        }

        String ubySemantic = "adj."+_semanticValue;

        /*
        System.out.println("[");
        System.out.println(ubySemantic);
        System.out.println(_lemma);
        System.out.println("]");
        */

        List<LexicalEntry> lexicalEntries = resources.getUby().getLexicalEntries(_lemma, EPartOfSpeech.adjective, null);

        for (LexicalEntry lexEntry : lexicalEntries) {


            for (Sense sense:lexEntry.getSenses())
            {

                Synset synset = sense.getSynset();

                if(synset == null)
                {
                    continue;
                }

                for(Sense sense2 : synset.getSenses())
                {
//                    System.out.println("\t"+sense2.getLexicalEntry().getLemmaForm());

                    for(SemanticLabel sem : sense2.getSemanticLabels())
                    {
//                        System.out.println("\t\t"+sem.getLabel());

                        if(sem.getLabel().equals(ubySemantic))
                        {
                            String word = sense2.getLexicalEntry().getLemmaForm();
                            boolean isNegativeConnotation = false;
                            EmotionModel emotion = resources.getEmotionAnalizer().getEmotion(word);
                            if(emotion!=null)
                            {
                                isNegativeConnotation= emotion.isNegative();
                            }
                            if(!isNegativeConnotation)
                            {
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
        }

        return output;
    }
}