package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.emotions.EmotionModel;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Resources;
import de.tudarmstadt.ukp.lmf.model.core.LexicalEntry;
import de.tudarmstadt.ukp.lmf.model.core.Sense;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;
import de.tudarmstadt.ukp.lmf.model.meta.SemanticLabel;
import de.tudarmstadt.ukp.lmf.model.semantics.Synset;

public class VerbChunkPart
    extends ChunkPart
{

   public VerbChunkPart()
   {
       super();
       _isValueDerivable = false;
   }




   @Override
   public List<ChunkPartSolution> generateChunkPartSolution(Resources resources,ChunkPart orginialPart)
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

       String ubySemantic = "verb."+_semanticValue;

//
//       System.out.println("[");
//       System.out.println(ubySemantic);
//       System.out.println(_lemma);
//       System.out.println("]");
//

       List<LexicalEntry> lexicalEntries = resources.getUby().getLexicalEntries(_lemma, EPartOfSpeech.verb, null);

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
//                   System.out.println("\t"+sense2.getLexicalEntry().getLemmaForm());

                   for(SemanticLabel sem : sense2.getSemanticLabels())
                   {
//                       System.out.println("\t\t"+sem.getLabel());

                       if(sem.getLabel().equals(ubySemantic))
                       {
                           String word = sense2.getLexicalEntry().getLemmaForm();
                           boolean isNegativeConnotation = false;
                           EmotionModel emotion = resources.getEmotionAnalizer().getEmotion(word);
                           if(emotion!=null)
                           {
                               isNegativeConnotation= emotion.isNegative();
                           }

                           if(isNegativeConnotation)
                           {
                               continue;
                           }

                           boolean isFreqToSmall = false;

                           String[] subwords = word.split(" ");
                           List<String> subwordWfreqs = new ArrayList<String>();




                           try
                           {
                               for(String subword : subwords)
                               {
                                   if(subword.length()==0)
                                   {
                                       continue;
                                   }
                                   long freq = resources.getWordStatistic().getFrequency(subword);
                                   if(freq<1000)
                                   {
                                       isFreqToSmall = true;
                                       break;
                                   }
                                   subwordWfreqs.add("("+subword+")["+freq+"] ");
                               }
                           }
                           catch (IOException e) {
                           }

                           if(isFreqToSmall)
                           {
                               System.out.println("Too smal frequence : "+word);
                               continue;
                           }



                           /*
                           String wordfreq = "";

                           for(String val : subwordWfreqs)
                           {
                               wordfreq = wordfreq + val;
                           }
                           */

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

   @Override
public String deriveFromLemmaForm(String lemma)
{
       if (_pos.equals("VB") || _pos.equals("VV")) {
           return lemma;
       }

       if (_pos.equals("VBD") || _pos.equals("VBN") || _pos.equals("VVD") || _pos.equals("VVN")) {
           // verb is past tense or past participle
           if(lemma.endsWith("e"))
           {
               return lemma+"d";
           }
           else
           {
               return lemma+"ed";
           }
       }

       if (_pos.equals("VVG")) {
           String optimizedLemma = lemma;
           if(lemma.endsWith("e") && lemma.length()>1)
           {
               optimizedLemma = lemma.substring(0, lemma.length()-1);
           }
           return optimizedLemma+"ing";
       }

       if (_pos.equals("VBP") || _pos.equals("VBZ") || _pos.equals("VVP") || _pos.equals("VVZ")) {
           return lemma;
       }

       return lemma;
   }
}