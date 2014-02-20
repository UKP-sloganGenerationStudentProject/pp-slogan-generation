package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.annotation;

import static org.apache.uima.fit.util.JCasUtil.select;
import static org.apache.uima.fit.util.JCasUtil.selectCovered;

import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.Chunk;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation.ChunkPatternAnnotation;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation.SloganAnnotation;

/**
 *
 * This annotator annotates predefined combination of chunks.
 * These predifined combinations are extracted from PARAM_PATTERN_LIST.
 * Each combination is define in the following way : NC_VC_NC_, which is to say
 * the name of the chunk and followed by "_".
 * A detected combination of chunks is annoated with the corresponding value.
 *
 * @author matthieu Fraissinet-Tachet
 *
 */
public class ChunkPatternAnnotator extends JCasAnnotator_ImplBase
{
    public static final String PARAM_PATTERN_LIST = "patternList";
    @ConfigurationParameter(name = PARAM_PATTERN_LIST)
    private List<String> patternList;

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
        System.out.println("ChunkPatternAnnotator is looking for the folowing patterns :");
        for (String pattern : patternList)
        {
            System.out.println(pattern);
        }

      //go through slogans
        for (SloganAnnotation slogan : select(aJCas, SloganAnnotation.class))
        {
            //go through chunk
            String pattern="";
            Chunk chunk1=null;
            Chunk chunk2=null;
            boolean toInitialize=true;


            for(Chunk chunk : selectCovered(Chunk.class, slogan))
            {

                if(toInitialize)
                {
                    chunk1=chunk;
                    toInitialize=false;
                }
                if(chunk.getChunkValue().equals("O"))
                {
                    if(chunk1!=chunk)
                    {
                        //look if the pattern is good
                        if(patternList.contains(pattern))
                        {
                            ChunkPatternAnnotation annotation = new ChunkPatternAnnotation(aJCas,chunk1.getBegin(),chunk2.getEnd());
                            annotation.setPattern(pattern);
                            annotation.addToIndexes();
                        }
                    }
                    toInitialize=true;
                }
                else
                {
                    pattern=pattern+chunk.getChunkValue()+"_";
                    chunk2=chunk;
                }
            }
        }
    }

}
