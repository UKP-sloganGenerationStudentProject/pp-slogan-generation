package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.annotation;

import static org.apache.uima.fit.util.JCasUtil.select;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.emotions.EmotionAnalizer;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.emotions.EmotionModel;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation.EmotionAnnotation;

public class EmotionAnnotator extends JCasAnnotator_ImplBase
{

    //WE SUPPOSE THAT TREETAGGER LEMMA HAS BEEN ALREADY USED

    public final class Emotions
    {
        public static final String POSITIVE ="positive";
        public static final String NEGATIVE = "negative";
        public static final String ANGER = "anger";
        public static final String ANTICIPATION = "anticipation";
        public static final String DISGUST = "disgust";
        public static final String FEAR = "fear";
        public static final String JOY = "joy";
        public static final String SADNESS = "sadness";
        public static final String SURPRISE = "surprise";
        public static final String TRUST = "trust";

        private Emotions() {}
     }

    @Override
    public void process(JCas aJCas)
        throws AnalysisEngineProcessException
    {
        EmotionAnalizer analizer = new EmotionAnalizer("src/main/resources/NRCemotionlexicon.pdf");

        for( Lemma lemma : select(aJCas,Lemma.class))
        {
            EmotionModel emotion = analizer.getEmotion(lemma.getValue());

            if(emotion != null)
            {
                if(emotion.isPositive())
                {
                    EmotionAnnotation annotation = new EmotionAnnotation(aJCas,lemma.getBegin(),lemma.getEnd());
                    annotation.setValence(Emotions.POSITIVE);
                    annotation.addToIndexes();
                }
                if(emotion.isNegative())
                {
                    EmotionAnnotation annotation = new EmotionAnnotation(aJCas,lemma.getBegin(),lemma.getEnd());
                    annotation.setValence(Emotions.NEGATIVE);
                    annotation.addToIndexes();
                }
                if(emotion.isAnger())
                {
                    EmotionAnnotation annotation = new EmotionAnnotation(aJCas,lemma.getBegin(),lemma.getEnd());
                    annotation.setValence(Emotions.ANGER);
                    annotation.addToIndexes();
                }
                if(emotion.isAnticipation())
                {
                    EmotionAnnotation annotation = new EmotionAnnotation(aJCas,lemma.getBegin(),lemma.getEnd());
                    annotation.setValence(Emotions.ANTICIPATION);
                    annotation.addToIndexes();
                }
                if(emotion.isDisgust())
                {
                    EmotionAnnotation annotation = new EmotionAnnotation(aJCas,lemma.getBegin(),lemma.getEnd());
                    annotation.setValence(Emotions.DISGUST);
                    annotation.addToIndexes();
                }
                if(emotion.isFear())
                {
                    EmotionAnnotation annotation = new EmotionAnnotation(aJCas,lemma.getBegin(),lemma.getEnd());
                    annotation.setValence(Emotions.FEAR);
                    annotation.addToIndexes();
                }
                if(emotion.isJoy())
                {
                    EmotionAnnotation annotation = new EmotionAnnotation(aJCas,lemma.getBegin(),lemma.getEnd());
                    annotation.setValence(Emotions.JOY);
                    annotation.addToIndexes();
                }
                if(emotion.isSadness())
                {
                    EmotionAnnotation annotation = new EmotionAnnotation(aJCas,lemma.getBegin(),lemma.getEnd());
                    annotation.setValence(Emotions.SADNESS);
                    annotation.addToIndexes();
                }
                if(emotion.isSurprise())
                {
                    EmotionAnnotation annotation = new EmotionAnnotation(aJCas,lemma.getBegin(),lemma.getEnd());
                    annotation.setValence(Emotions.SURPRISE);
                    annotation.addToIndexes();
                }
                if(emotion.isTrust())
                {
                    EmotionAnnotation annotation = new EmotionAnnotation(aJCas,lemma.getBegin(),lemma.getEnd());
                    annotation.setValence(Emotions.TRUST);
                    annotation.addToIndexes();
                }

            }

        }


    }


}
