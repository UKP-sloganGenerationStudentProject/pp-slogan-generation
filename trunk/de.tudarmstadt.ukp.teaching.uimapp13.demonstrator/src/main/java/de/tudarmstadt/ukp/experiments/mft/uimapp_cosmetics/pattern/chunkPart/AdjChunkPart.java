package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPart;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.emotions.EmotionModel;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.Resources;
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
    public ArrayList<String> generate(Resources resources)
    {

        ArrayList<String> output = new ArrayList<String>();



        output.add(_lemma);

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

                /*

                if(output.size()>5)
                {
                    return output;
                }
                */

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
                            if(!output.contains(word) && !isNegativeConnotation)
                            {
                                output.add(word);

//                                System.out.println("\t\tYES");
                            }
                        }
                    }

                }



            }
        }

        return output;
    }
}
