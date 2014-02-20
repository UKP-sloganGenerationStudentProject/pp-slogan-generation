package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.annotation;

import static org.apache.uima.fit.util.JCasUtil.select;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.emotions.EmotionAnalyzer;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.emotions.EmotionModel;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation.EmotionAnnotation;

/**
 *This annotator annotates the emotion associated to a token. It uses the NRC Emotion Lexicon version 0.92

 *(Saif M. Mohammad and Peter D. Turney, NRC Technical Report,
 *December 2013, Ottawa, Canada.). You have to give the path to the pdf document in PARAM_EMOTIONS_LEXICON_PATH.
 *
 * @author Matthieu Fraissinet-Tachet
 *
 */


public class EmotionAnnotator
    extends JCasAnnotator_ImplBase
{

    // WE SUPPOSE THAT TREETAGGER LEMMA HAS BEEN ALREADY USED

    public static final String PARAM_EMOTIONS_LEXICON_PATH = "emotionsPdfFile";
    @ConfigurationParameter(name = PARAM_EMOTIONS_LEXICON_PATH, mandatory = true, description = "The path to the emotions lexicon (PDF)")
    private String emotionsPdfFile;

    public final class Emotions
    {
        public static final String POSITIVE = "positive";
        public static final String NEGATIVE = "negative";
        public static final String ANGER = "anger";
        public static final String ANTICIPATION = "anticipation";
        public static final String DISGUST = "disgust";
        public static final String FEAR = "fear";
        public static final String JOY = "joy";
        public static final String SADNESS = "sadness";
        public static final String SURPRISE = "surprise";
        public static final String TRUST = "trust";

        private Emotions()
        {
        }
    }

    @Override
    public void process(final JCas aJCas)
        throws AnalysisEngineProcessException
    {
        final EmotionAnalyzer analizer = new EmotionAnalyzer(this.emotionsPdfFile);

        for (final Lemma lemma : select(aJCas, Lemma.class)) {
            final EmotionModel emotion = analizer.getEmotion(lemma.getValue());

            if (emotion != null) {
                if (emotion.isPositive()) {
                    final EmotionAnnotation annotation = new EmotionAnnotation(aJCas,
                            lemma.getBegin(), lemma.getEnd());
                    annotation.setValence(Emotions.POSITIVE);
                    annotation.addToIndexes();
                }
                if (emotion.isNegative()) {
                    final EmotionAnnotation annotation = new EmotionAnnotation(aJCas,
                            lemma.getBegin(), lemma.getEnd());
                    annotation.setValence(Emotions.NEGATIVE);
                    annotation.addToIndexes();
                }
                if (emotion.isAnger()) {
                    final EmotionAnnotation annotation = new EmotionAnnotation(aJCas,
                            lemma.getBegin(), lemma.getEnd());
                    annotation.setValence(Emotions.ANGER);
                    annotation.addToIndexes();
                }
                if (emotion.isAnticipation()) {
                    final EmotionAnnotation annotation = new EmotionAnnotation(aJCas,
                            lemma.getBegin(), lemma.getEnd());
                    annotation.setValence(Emotions.ANTICIPATION);
                    annotation.addToIndexes();
                }
                if (emotion.isDisgust()) {
                    final EmotionAnnotation annotation = new EmotionAnnotation(aJCas,
                            lemma.getBegin(), lemma.getEnd());
                    annotation.setValence(Emotions.DISGUST);
                    annotation.addToIndexes();
                }
                if (emotion.isFear()) {
                    final EmotionAnnotation annotation = new EmotionAnnotation(aJCas,
                            lemma.getBegin(), lemma.getEnd());
                    annotation.setValence(Emotions.FEAR);
                    annotation.addToIndexes();
                }
                if (emotion.isJoy()) {
                    final EmotionAnnotation annotation = new EmotionAnnotation(aJCas,
                            lemma.getBegin(), lemma.getEnd());
                    annotation.setValence(Emotions.JOY);
                    annotation.addToIndexes();
                }
                if (emotion.isSadness()) {
                    final EmotionAnnotation annotation = new EmotionAnnotation(aJCas,
                            lemma.getBegin(), lemma.getEnd());
                    annotation.setValence(Emotions.SADNESS);
                    annotation.addToIndexes();
                }
                if (emotion.isSurprise()) {
                    final EmotionAnnotation annotation = new EmotionAnnotation(aJCas,
                            lemma.getBegin(), lemma.getEnd());
                    annotation.setValence(Emotions.SURPRISE);
                    annotation.addToIndexes();
                }
                if (emotion.isTrust()) {
                    final EmotionAnnotation annotation = new EmotionAnnotation(aJCas,
                            lemma.getBegin(), lemma.getEnd());
                    annotation.setValence(Emotions.TRUST);
                    annotation.addToIndexes();
                }

            }

        }

    }

}
