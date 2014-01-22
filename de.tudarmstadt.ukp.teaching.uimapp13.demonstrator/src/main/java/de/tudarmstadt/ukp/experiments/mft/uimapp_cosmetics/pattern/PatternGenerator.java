package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;
import static org.apache.uima.fit.util.JCasUtil.select;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import com.googlecode.jweb1t.JWeb1TSearcher;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.semantics.type.SemanticField;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.Chunk;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpSegmenter;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerChunkerTT4J;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerPosLemmaTT4J;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.annotation.ChunkPatternAnnotator;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.annotation.EmotionAnnotator;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.annotation.SloganAnnotator;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.emotions.EmotionAnalizer;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPart.ChunkPart;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPart.ChunkPart.ChunkPartType;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern.ChunkHeader.ChunkType;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation.ChunkPatternAnnotation;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation.EmotionAnnotation;
import de.tudarmstadt.ukp.lmf.api.Uby;
import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;
import de.tudarmstadt.ukp.lmf.transform.DBConfig;
import de.tudarmstadt.ukp.uby.resource.UbySemanticFieldResource;
import de.tudarmstadt.ukp.uby.uima.annotator.UbySemanticFieldAnnotator;

public class PatternGenerator
{

    /*
     * resources
     */

    String _emotionFilePath;
    String _sloganBasePath;
    String _ubyDBURL;
    String _ubyDBDriver;
    String _ubyDBDriverName;
    String _ubyDBUserName;
    String _ubyDBPassword;

    List<String> _patternsToAnnotate;
    List<String> _partsOfBodyToSelect;

    PatternFactory _factory;

    Resources _resources;


    public static void main(String[] args) throws Exception
    {

        PatternGenerator generator = new PatternGenerator();
        generator.setEmotionFilePath("src/main/resources/NRCemotionlexicon.pdf");
        generator.setSloganBasePath("src/main/resources/beautySlogans.txt");
        generator.setUbyDBData("localhost/uby_medium_0_3_0","com.mysql.jdbc.Driver","mysql","root","pass");
        generator.init();


        /*
         * Generation parameters
         */
        //PATTERN
        //retrieve the list of selectable patterns (add a chekckbox with each element of the list as label)
        List<String> selectablePatterns = generator.getSelectablePatterns();

        /*Example :*/
            String pattern0 = selectablePatterns.get(0);
            /* when the associated checkbox gets checked*/
            generator.selectPattern(pattern0);
            /* when the associated checkbox gets unchecked */
//            generator.unselectPattern(pattern0);

         //PART OF BODY
         //retrieve the list of selectable part of the body
         List<String> selectablePartsOfBody = generator.getSelectablePartsOfBody();

         /*Example :*/
         String partOfBody0 = selectablePartsOfBody.get(0);
             /* when the associated checkbox gets checked*/
             generator.selectPartOfBody(partOfBody0);
             /* when the associated checkbox gets unchecked */
//             generator.unselectPartOfBodyn(partOfBody0);

         //PRODUCT NAME
         //set the product name
         generator.setProductName("myProduct");

         //PARTS OF THE BODY
         //set the suggested words (a string containing all the words separated with a coma"
         generator.setSuggestedWords("beauty,woman,colour");


         for(String slogan : generator.generatePatterns())
         {
             System.out.println(slogan);
         }
    }

    public PatternGenerator()
    {
        _emotionFilePath = "";
        _sloganBasePath = "";
        _ubyDBURL = "";
        _ubyDBDriver = "";
        _ubyDBDriverName = "";
        _ubyDBUserName = "";
        _ubyDBPassword = "";

        _resources = new Resources();

        _patternsToAnnotate =  Arrays.asList("NC_","NC_PC_NC_","VC_NC_","VC_NC_PC_NC_","VC_","NC_VC_","NC_VC_ADJC_","NC_VC_NC_");
        _partsOfBodyToSelect = Arrays.asList("eye","skin","lips","fingernails");

    }

    public void init() throws Exception
    {

        _factory = new PatternFactory();


        Uby uby = null;
        DBConfig db = new   DBConfig(_ubyDBURL,_ubyDBDriver,_ubyDBDriverName,_ubyDBUserName, _ubyDBPassword, false);
        try
        {
            uby = new Uby(db);
        }
        catch (UbyInvalidArgumentException e)
        {
            e.printStackTrace();
        }

        _resources.setUby(uby);



        final String dkproHome = System.getenv("DKPRO_HOME");
        final JWeb1TSearcher lookup = new JWeb1TSearcher(new File(dkproHome + "/web1t/ENGLISH/"),
                1, 1);

        _resources.setWeb1tSearcher(lookup);


        EmotionAnalizer emotionAnalizer = new EmotionAnalizer(_emotionFilePath);
        _resources.setEmotionAnalizer(emotionAnalizer);


        CollectionReaderDescription reader = createReaderDescription(
                TextReader.class,
                TextReader.PARAM_SOURCE_LOCATION, _sloganBasePath,
                TextReader.PARAM_LANGUAGE, "en");

        AnalysisEngineDescription sloganAnnotator = createEngineDescription(SloganAnnotator.class);

        AnalysisEngineDescription segmentation = createEngineDescription(OpenNlpSegmenter.class);

        AnalysisEngineDescription semanticFieldAnnotator =
                createEngineDescription(
                        UbySemanticFieldAnnotator.class,
                        UbySemanticFieldAnnotator.PARAM_UBY_SEMANTIC_FIELD_RESOURCE,
                        createExternalResourceDescription(UbySemanticFieldResource.class,
                                UbySemanticFieldResource.PARAM_URL, _ubyDBURL,
                                UbySemanticFieldResource.PARAM_DRIVER, _ubyDBDriver,
                                UbySemanticFieldResource.PARAM_DRIVER_NAME, _ubyDBDriverName,
                                UbySemanticFieldResource.PARAM_USERNAME, _ubyDBUserName,
                                UbySemanticFieldResource.PARAM_PASSWORD, _ubyDBPassword));


       AnalysisEngineDescription posLemmaAnnotator = createEngineDescription(TreeTaggerPosLemmaTT4J.class);
       AnalysisEngineDescription chunkAnnotator = createEngineDescription(TreeTaggerChunkerTT4J.class);

       AnalysisEngineDescription chunkPatternAnnotator = createEngineDescription(ChunkPatternAnnotator.class,ChunkPatternAnnotator.PARAM_PATTERN_LIST,_patternsToAnnotate);
       AnalysisEngineDescription emotionAnnotator = createEngineDescription(EmotionAnnotator.class);



       JCasIterable pipeline = new JCasIterable
               (reader,
               sloganAnnotator,
               segmentation,
               posLemmaAnnotator,
               chunkAnnotator,
               semanticFieldAnnotator,
               chunkPatternAnnotator,
               emotionAnnotator);

       for (JCas jcas : pipeline)
       {
           extractPatterns(jcas);
       }

    }


    /*
     * Setters necessary to use before init();
     */

    public void setEmotionFilePath(String emotionFilePath)
    {
        this._emotionFilePath = emotionFilePath;
    }

    public void setSloganBasePath(String sloganBasePath)
    {
        this._sloganBasePath = sloganBasePath;
    }

    public void setUbyDBData(String ubyDBURL,String ubyDBDriver,String ubyDBDriverName,String ubyDBUserName,String ubyDBPassword)
    {
        this._ubyDBURL = ubyDBURL;
        this._ubyDBDriver = ubyDBDriver;
        this._ubyDBDriverName = ubyDBDriverName;
        this._ubyDBUserName = ubyDBUserName;
        this._ubyDBPassword = ubyDBPassword;
    }


    public void setProductName(String name)
    {
        _resources.setProductName(name);
    }

    public void setSuggestedWords(String suggestedWords)
    {
        _resources.setSuggestedWords(Arrays.asList(suggestedWords.split(",")));
    }

    public List<String> getSelectablePatterns()
    {
        return _patternsToAnnotate;
    }

    public void selectPattern(String chunkPatternAsString)
    {
        if(!_resources.getPatternsToGenerate().contains(chunkPatternAsString))
        {
            _resources.getPatternsToGenerate().add(chunkPatternAsString);
        }
    }

    public void unselectPattern(String chunkPatternAsString)
    {
        if(_resources.getPatternsToGenerate().contains(chunkPatternAsString))
        {
            _resources.getPatternsToGenerate().remove(chunkPatternAsString);
        }
    }

    public List<String> getSelectablePartsOfBody()
    {
        return _partsOfBodyToSelect;
    }

    public void selectPartOfBody(String part)
    {
        if(!_resources.getSelectedPartsOfBody().contains(part))
        {
            _resources.getSelectedPartsOfBody().add(part);
        }
    }

    public void unselectPartOfBodyn(String part)
    {
        if(_resources.getSelectedPartsOfBody().contains(part))
        {
            _resources.getSelectedPartsOfBody().remove(part);
        }
    }

    public List<String> generatePatterns()
    {
        return _factory.generatePatterns(_resources);
    }


    public void extractPatterns(JCas aJCas)
            throws AnalysisEngineProcessException
    {

        ChunkPatternAnnotation prevPatternAnnot = null;
        boolean isNewPattern = false;

        //we get through all chunk and then we check if they belong to a (the same?) ChunkPatternAnnotation
        for (Chunk chunk : select(aJCas, Chunk.class))
        {

            if(chunk.getChunkValue().equals("O"))
            {
                continue;
            }

            /*
             *
             * LOOK FOR THE PATTERN IT MIGHT BE INTO
             *
             */

            //look for ChunkPatternAnnotation
            List<ChunkPatternAnnotation> patterns = JCasUtil.selectCovering(ChunkPatternAnnotation.class, chunk);
            if(patterns.size()>0)
            {
                if(prevPatternAnnot == null)
                {
                   isNewPattern = true;
                }
                else
                {
                    if(prevPatternAnnot.getBegin()!=patterns.get(0).getBegin())
                    {
                        _factory.finishPattern();
                        isNewPattern = true;
                    }
                    else
                    {
                        isNewPattern = false;
                    }

                }
                prevPatternAnnot = patterns.get(0);

            }
            else
            {
                if(prevPatternAnnot!=null)
                {
                    _factory.finishPattern();
                }
                prevPatternAnnot = null;
                isNewPattern = false;
            }

            if(isNewPattern)
            {
                ChunkPatternAnnotation pattern = patterns.get(0);

                //we don't consider slgoans containing negative emotion
                boolean isNegative=false;
                for(EmotionAnnotation emotion : JCasUtil.selectCovered(EmotionAnnotation.class,pattern))
                {
                   if(emotion.getValence().equals(EmotionAnnotator.Emotions.NEGATIVE))
                   {
                       isNegative = true;
                       break;
                   }
                }

                if(!isNegative)
                 {
                    _factory.startNewPattern();
                    _factory.setPatternValue(pattern.getCoveredText());
                 }

            }

            /*
            *
            * PROCESS THE CHUNK
            *
            */

            ChunkType chunkType = null;

            try
            {
                chunkType = ChunkType.valueOf(chunk.getChunkValue());
            }
            catch(IllegalArgumentException e)
            {
                //the chunk type is not implemented in our system (because we are not interested in it)
                System.out.println("Chunk "+chunk.getChunkValue()+" ignored.");
                continue;
            }

            _factory.startNewChunk(chunkType);

            for(Token token : JCasUtil.selectCovered(Token.class, chunk))
            {
                List<POS> poss = JCasUtil.selectCovered(POS.class,token);
                POS pos = new POS(aJCas);
                if(poss.size()>0)
                {
                    pos = poss.get(0);
                }

                ChunkPartType chunkPartType = ChunkPartType.getTypeOf(pos.getType().getShortName());

                List<Lemma> lemmas = JCasUtil.selectCovered(Lemma.class,token);
                String lemma = "";
                if(lemmas.size()>0)
                {
                    lemma = lemmas.get(0).getValue();
                }

                List<SemanticField> sems = JCasUtil.selectCovering(SemanticField.class,token);
                String sem = "";
                if(sems.size()>0)
                {
                    sem = sems.get(0).getValue();
                }

                if(token.getCoveredText().equals(ChunkPartType.PRODUCT_NAME.toString()))
                {
                    chunkPartType = chunkPartType.PRODUCT_NAME;
                }

                ChunkPart chunkPart = ChunkPart.createChunkPart(chunkPartType,token.getCoveredText(),lemma,sem,pos.getPosValue());

                _factory.addPartToChunk(chunkPart);

            }

            _factory.finishChunk();
        }

        if(prevPatternAnnot != null)
        {
            _factory.finishPattern();
        }

    }

    public class Resources
    {
        Uby _uby;
        EmotionAnalizer _emotionAnalizer;
        JWeb1TSearcher  _web1tSearcher;

        String _productName;
        List<String> _patternsToGenerate;
        List<String> _selectedPartsOfBody;
        List<String> _suggestedWords;


        public Resources()
        {
            _uby = null;
            _emotionAnalizer = null;
            _web1tSearcher = null;
            _productName = "productName";
            _patternsToGenerate = new ArrayList<String>();
            _selectedPartsOfBody = new ArrayList<String>();
            _suggestedWords = new ArrayList<String>();
        }

        public Resources(Uby uby, EmotionAnalizer emotionAnalizer, JWeb1TSearcher web1tSearcher)
        {
            super();
            this._uby = uby;
            this._emotionAnalizer = emotionAnalizer;
            this._web1tSearcher = web1tSearcher;
        }

        public Uby getUby()
        {
            return _uby;
        }

        public void setUby(Uby uby)
        {
            this._uby = uby;
        }

        public EmotionAnalizer getEmotionAnalizer()
        {
            return _emotionAnalizer;
        }

        public void setEmotionAnalizer(EmotionAnalizer emotionAnalizer)
        {
            this._emotionAnalizer = emotionAnalizer;
        }

        public JWeb1TSearcher getWeb1tSearcher()
        {
            return _web1tSearcher;
        }

        public void setWeb1tSearcher(JWeb1TSearcher web1tSearcher)
        {
            this._web1tSearcher = web1tSearcher;
        }


        public String getProductName()
        {
            return _productName;
        }

        public void setProductName(String productName)
        {
            this._productName = productName;
        }

        public List<String> getPatternsToGenerate()
        {
            return _patternsToGenerate;
        }

        public void setPatternsToGenerate(List<String> patternsToGenerate)
        {
            this._patternsToGenerate = patternsToGenerate;
        }

        public List<String> getSelectedPartsOfBody()
        {
            return _selectedPartsOfBody;
        }

        public void setSelectedPartsOfBody(List<String> selectedPartsOfBody)
        {
            this._selectedPartsOfBody = selectedPartsOfBody;
        }

        public List<String> getSuggestedWords()
        {
            return _suggestedWords;
        }

        public void setSuggestedWords(List<String> suggestedWords)
        {
            this._suggestedWords = suggestedWords;
        }
    }


}
