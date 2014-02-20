package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.annotation;

import java.util.ArrayList;
import java.util.List;

import opennlp.tools.util.Span;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation.SloganAnnotation;

/**
 *
 * This annotator annotates slogans starts and ends in the slogan corpus.
 * It says that at a slogan starts at the beginning of a line and ends at the end of the line, and
 * that each new line is a new slogan. So obviously the slogans have to be written in the same way
 * in the slogan corpus.
 *
 * @author Matthieu Fraissinet-Tachet
 *
 */

public class SloganAnnotator extends JCasAnnotator_ImplBase
{



    @Override
    public void initialize(UimaContext aContext)
        throws ResourceInitializationException
    {
        super.initialize(aContext);
    }

    @Override
    public void process(JCas aJCas)
        throws AnalysisEngineProcessException
    {
        Span[] spans = sloganPosDetect(aJCas.getDocumentText());
        for (Span span : spans) {
            if(span.getStart()==span.getEnd())
            {
              //we ignore "empty slogans" (which are no slogan)...
                continue;
            }
            new SloganAnnotation(aJCas,span.getStart(),span.getEnd()).addToIndexes();
        }
    }

    public Span[] sloganPosDetect(String s)
    {
        List<Integer> enders = new ArrayList<Integer>();

        int lastIndex1=0;
        while(true)
        {
            int index=s.indexOf("\n",lastIndex1);
            if(index==-1){break;}
            lastIndex1=index+1;
            enders.add(index);
        }

        if(enders.size()==0)
        {
            int start = 0;
            int end = s.length();

            if ((end - start) > 0)
            {
                return new Span[] {new Span(start, end)};
            }
            else
            {
                return new Span[0];
            }
        }

        Span[] spans = new Span[enders.size()+1];

        int lastIndex2=-1;
        for(int i=0;i<spans.length-1;i++)
        {
            int newIndex=enders.get(i);
            spans[i]=new Span(lastIndex2+1,newIndex-1);
            lastIndex2=newIndex;

        }
        spans[spans.length-1]=new Span(lastIndex2+1,s.length());

        return spans;
    }

}