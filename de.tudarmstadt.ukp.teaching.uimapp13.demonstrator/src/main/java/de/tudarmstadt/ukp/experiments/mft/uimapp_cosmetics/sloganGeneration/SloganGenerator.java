package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;
import static org.apache.uima.fit.util.JCasUtil.select;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.emotions.EmotionAnalyzer;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart.ChunkPart;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation.ChunkPatternAnnotation;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation.EmotionAnnotation;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkPartType;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkType;
import de.tudarmstadt.ukp.lmf.api.Uby;
import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;
import de.tudarmstadt.ukp.lmf.transform.DBConfig;
import de.tudarmstadt.ukp.uby.resource.UbySemanticFieldResource;
import de.tudarmstadt.ukp.uby.uima.annotator.UbySemanticFieldAnnotator;

public class SloganGenerator
    implements Serializable
{

    /*
     * This class enables to create the patterns and generate slogans
     * in an easy way. As input, it requires the path of the different resources
     * and also some parameters for the slogan generation.
     * An example of how to use this class is given in main().
     */

    private static final long serialVersionUID = 8409089998966336600L;
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

    PatternFactory _factory;

    Resources _resources;
    private String _web1TPathname;
    private final String _allowedWordsPath;
    private String _rejectedWordsPath;


    public static void main(final String[] args)
        throws Exception
    {

        final SloganGenerator generator = new SloganGenerator();

        generator.useUbyForNewWords(true);

        generator.setEmotionFilePath("src/main/resources/NRCemotionlexicon.pdf");
        generator.setSloganBasePath("src/main/resources/beautySlogans.txt");
        generator.setUbyDBData("localhost/uby_medium_0_3_0", "com.mysql.jdbc.Driver", "mysql",
                "root", "pass");

        //generator.setAllowedWordSearcher("/home/matthieuft/Documents/cosmeticsNgrams", "/home/matthieuft/Documents/rejectedWords.txt");

        generator.init();

        /*
         * Generation parameters
         */
        final List<String> selectablePatterns = Parameters.getSelectablePatterns();
        final List<String> selectablePartsOfBody = Parameters.getSelectablePartsOfBody();

        generator.setProductName("myProductName");

        final Scanner user_input = new Scanner(System.in);


        while (true) {

            System.out.println();
            System.out
                    .print("What do you want to do ? 0 : print PatternGenerator content, 1 : generate slogans, 2: check for constraints, 3: exit");
            final int action = Integer.parseInt(user_input.next());
            if (action == 0) {
                System.out.println(generator.toString());
                continue;
            }
            if (action == 2) {
                generator.testConstraints();
            }
            if (action == 3) {
                break;
            }


            System.out.println();
            System.out.println();
            int i = 0;
            for (final String patternType : selectablePatterns) {
                System.out.print(i + " : " + patternType + ";  ");
                i = i + 1;
            }
            System.out.println();
            System.out.print("Give the number of the pattern you want to select :");
            final int selectedPatternNumber = Integer.parseInt(user_input.next());
            generator.selectPattern(selectablePatterns.get(selectedPatternNumber));

            System.out.println();
            System.out.println();
            int j = 0;
            for (final String bodyPart : selectablePartsOfBody) {
                System.out.print(j + " : " + bodyPart + ";  ");
                j = j + 1;
            }
            System.out.println();
            System.out.print("Give the number of the body part you want to select :");
            final int selectedBodyPartNumber = Integer.parseInt(user_input.next());
            generator.selectPartOfBody(selectablePartsOfBody.get(selectedBodyPartNumber));

            System.out.println();
            System.out.print("Suggested words (must be separated with a comma) :");
            final String suggestedWords = user_input.next();
            generator.setSuggestedWords(suggestedWords);


            System.out.println();
            System.out.println();
            for (final String slogan : generator.generateSlogans(10)) {
                System.out.println("\t" + slogan);
            }

        }

        user_input.close();

    }

    public SloganGenerator()
    {
        this._emotionFilePath = "";
        this._sloganBasePath = "";
        this._ubyDBURL = "";
        this._ubyDBDriver = "";
        this._ubyDBDriverName = "";
        this._ubyDBUserName = "";
        this._ubyDBPassword = "";
        this._allowedWordsPath = "";
        this._resources = new Resources();
    }

    public void init()
        throws Exception
    {

        this._factory = new PatternFactory();

        /*
         * connect uby
         */
        Uby uby = null;
        final DBConfig db = new DBConfig(this._ubyDBURL, this._ubyDBDriver, this._ubyDBDriverName,
                this._ubyDBUserName, this._ubyDBPassword, false);
        try {
            uby = new Uby(db);
        }
        catch (final UbyInvalidArgumentException e) {
            e.printStackTrace();
        }
        this._resources.setUby(uby);

        /*
         * setup web1t
         */
        final String dkproHome = System.getenv("DKPRO_HOME");
        String web1tPathname = dkproHome + "/web1t/ENGLISH/";
        if (this._web1TPathname != null) {
            web1tPathname = this._web1TPathname;
        }
        final JWeb1TSearcher lookup = new JWeb1TSearcher(new File(web1tPathname), 1, 1);
        this._resources.setWordStatistic(lookup);

//
//        String allowedWordsPath = "";
//        if (this._allowedWordsPath != null) {
//            allowedWordsPath = this._allowedWordsPath;
//        }
//
//        String rejectedWordsPath = "";
//        if (this._rejectedWordsPath != null) {
//            rejectedWordsPath = this._rejectedWordsPath;
//        }
//
//
//        JWeb1TSearcher allowedWordsSearcher = null;
//        if(!allowedWordsPath.equals(""))
//        {
//            allowedWordsSearcher = new JWeb1TSearcher(new File(allowedWordsPath), 1, 1);
//        }
//
//        File rejWordsFile = new File(rejectedWordsPath);

//        this._resources.setAllowedWordSearcher(allowedWordsSearcher, rejWordsFile);

        /*
         * Emotion analyzer
         */
        final EmotionAnalyzer emotionAnalizer = new EmotionAnalyzer(this._emotionFilePath);
        this._resources.setEmotionAnalizer(emotionAnalizer);

        /*
         * set the pipe for the creation of the patterns
         */
        final CollectionReaderDescription reader = createReaderDescription(TextReader.class,
                TextReader.PARAM_SOURCE_LOCATION, this._sloganBasePath, TextReader.PARAM_LANGUAGE,
                "en");

        final AnalysisEngineDescription sloganAnnotator = createEngineDescription(SloganAnnotator.class);

        final AnalysisEngineDescription segmentation = createEngineDescription(OpenNlpSegmenter.class);

        final AnalysisEngineDescription semanticFieldAnnotator = createEngineDescription(
                UbySemanticFieldAnnotator.class,
                UbySemanticFieldAnnotator.PARAM_UBY_SEMANTIC_FIELD_RESOURCE,
                createExternalResourceDescription(UbySemanticFieldResource.class,
                        UbySemanticFieldResource.PARAM_URL, this._ubyDBURL,
                        UbySemanticFieldResource.PARAM_DRIVER, this._ubyDBDriver,
                        UbySemanticFieldResource.PARAM_DRIVER_NAME, this._ubyDBDriverName,
                        UbySemanticFieldResource.PARAM_USERNAME, this._ubyDBUserName,
                        UbySemanticFieldResource.PARAM_PASSWORD, this._ubyDBPassword));

        final AnalysisEngineDescription posLemmaAnnotator = createEngineDescription(TreeTaggerPosLemmaTT4J.class);
        final AnalysisEngineDescription chunkAnnotator = createEngineDescription(TreeTaggerChunkerTT4J.class);

        final AnalysisEngineDescription chunkPatternAnnotator = createEngineDescription(
                ChunkPatternAnnotator.class, ChunkPatternAnnotator.PARAM_PATTERN_LIST,
                Parameters.getSelectablePatterns());
        final AnalysisEngineDescription emotionAnnotator = createEngineDescription(
                EmotionAnnotator.class, EmotionAnnotator.PARAM_EMOTIONS_LEXICON_PATH,
                this._emotionFilePath);

        final JCasIterable pipeline = new JCasIterable(reader, sloganAnnotator, segmentation,
                posLemmaAnnotator, chunkAnnotator, semanticFieldAnnotator, chunkPatternAnnotator,
                emotionAnnotator);

        /*
         * run the pipeline and extract the results to create the patterns
         */
        for (final JCas jcas : pipeline) {
            this.extractPatterns(jcas);
        }

    }

    /*
     * Setters necessary to use before init();
     */

    public void setEmotionFilePath(final String emotionFilePath)
    {
        this._emotionFilePath = emotionFilePath;
    }

    public void setSloganBasePath(final String sloganBasePath)
    {
        this._sloganBasePath = sloganBasePath;
    }

    public void setUbyDBData(final String ubyDBURL, final String ubyDBDriver,
            final String ubyDBDriverName, final String ubyDBUserName, final String ubyDBPassword)
    {
        this._ubyDBURL = ubyDBURL;
        this._ubyDBDriver = ubyDBDriver;
        this._ubyDBDriverName = ubyDBDriverName;
        this._ubyDBUserName = ubyDBUserName;
        this._ubyDBPassword = ubyDBPassword;
    }

    public void setProductName(final String name)
    {
        this._resources.setProductName(name);
    }

    public void setWeb1TPathname(final String web1tPathname)
    {
        this._web1TPathname = web1tPathname;
    }

    public void setSuggestedWords(final String suggestedWords)
    {
        List<String> suggestedWordList = new ArrayList<>();
        for(String sugWord : Arrays.asList(suggestedWords.split(",")))
        {
            String trimmed = sugWord.trim();
            if(!trimmed.equals(""))
            {
                suggestedWordList.add(trimmed);
            }
        }
        this._resources.setSuggestedWords(suggestedWordList);
    }

    public void selectPattern(final String chunkPatternAsString)
    {
        this._resources.setPatternToGenerate(chunkPatternAsString);
    }

    public void selectPartOfBody(final String part)
    {
        this._resources.setSelectedPartOfBody(part);
    }

    public void useUbyForNewWords(final boolean tof)
    {
        this._resources.useUbyForNewWords(tof);
    }



    /*
     * test
     */
    public void testConstraints()
    {
        this._factory.checkForConstraints(this._resources);
        System.out.println(this._factory);
        /*
        this._factory.releaseConstraints();
        System.out.println(this._factory);
        */
    }




    /*
     * generation of the logans
     */

    public List<String> generateSlogans(final int nbrOfSlogans)
    {
        return this._factory.generateSlogans(this._resources, nbrOfSlogans);
    }



    /*
     * generation of the patterns
     */
    public void extractPatterns(final JCas aJCas)
        throws AnalysisEngineProcessException
    {

        ChunkPatternAnnotation prevPatternAnnot = null;
        boolean isNewPattern = false;

        // we get through all chunk and then we check if they belong to a (the same?)
        // ChunkPatternAnnotation
        for (final Chunk chunk : select(aJCas, Chunk.class)) {

            if (chunk.getChunkValue().equals("O")) {
                continue;
            }

            /*
             *
             * LOOK FOR THE PATTERN IT MIGHT BE INTO
             *
             */

            // look for ChunkPatternAnnotation
            final List<ChunkPatternAnnotation> patterns = JCasUtil.selectCovering(
                    ChunkPatternAnnotation.class, chunk);
            if (patterns.size() > 0) {
                if (prevPatternAnnot == null) {
                    //there was no previous pattern
                    //we start a new one
                    isNewPattern = true;
                }
                else {
                    if (prevPatternAnnot.getBegin() != patterns.get(0).getBegin()) {
                        //we finish the previous pattern and start a new one
                        this._factory.finishPattern();
                        isNewPattern = true;
                    }
                    else {
                        //we don't start any new pattern
                        //we keep working on the previous one
                        isNewPattern = false;
                    }

                }
                prevPatternAnnot = patterns.get(0);

            }
            else {
                if (prevPatternAnnot != null) {
                    // if the chunk correspond to no pattern, we terminate the previous one
                    this._factory.finishPattern();
                }
                prevPatternAnnot = null;
                isNewPattern = false;
            }

            if (isNewPattern) {
                final ChunkPatternAnnotation pattern = patterns.get(0);

                // we don't consider slgoans containing negative emotion
                boolean isNegative = false;
                for (final EmotionAnnotation emotion : JCasUtil.selectCovered(
                        EmotionAnnotation.class, pattern)) {
                    if (emotion.getValence().equals(EmotionAnnotator.Emotions.NEGATIVE)) {
                        isNegative = true;
                        break;
                    }
                }

                if (!isNegative) {
                    this._factory.startNewPattern();
                    this._factory.setSloganToCurrentPattern(pattern.getCoveredText());
                }

            }

            /*
            *
            * PROCESS THE CHUNK
            *
            */

            ChunkType chunkType = null;

            try {
                //map the chunkvalue from annotation to our ChunkType enumeration
                chunkType = ChunkType.valueOf(chunk.getChunkValue());
            }
            catch (final IllegalArgumentException e) {
                // the chunk type is not implemented in our system (because we are not interested in
                // it)
                System.out.println("Chunk " + chunk.getChunkValue() + " ignored.");
                continue;
            }

            this._factory.startNewChunk(chunkType);

            // go through every token of the chunk
            for (final Token token : JCasUtil.selectCovered(Token.class, chunk)) {
                final List<POS> poss = JCasUtil.selectCovered(POS.class, token);

                //get the part of speech
                POS pos = new POS(aJCas);
                if (poss.size() > 0) {
                    pos = poss.get(0);
                }

                //map the pos type to our ChunkPartType enumeration
                ChunkPartType chunkPartType = ChunkPartType.getTypeOf(pos.getType().getShortName());

                //get the corresponding lemma
                final List<Lemma> lemmas = JCasUtil.selectCovered(Lemma.class, token);
                String lemma = "";
                if (lemmas.size() > 0) {
                    lemma = lemmas.get(0).getValue();
                }

                //get the corresponding semantics
                final List<SemanticField> sems = JCasUtil
                        .selectCovering(SemanticField.class, token);
                String sem = "";
                if (sems.size() > 0) {
                    sem = sems.get(0).getValue();
                }

                //look if the token corresponds to the place of a productname
                if (token.getCoveredText().equals(ChunkPartType.PRODUCT_NAME.toString())) {
                    chunkPartType = ChunkPartType.PRODUCT_NAME;
                }

                //create a chunkpart with the previous pieces of information
                final ChunkPart chunkPart = ChunkPart.createChunkPart(chunkPartType,
                        token.getCoveredText(), lemma, sem, pos.getPosValue());

                //add the new chunkpart to the current chunk
                this._factory.addPartToChunk(chunkPartType,chunkPart);

            }

            //finish the chunk
            this._factory.finishChunk(this._resources);
        }

        if (prevPatternAnnot != null) {
            //if a pattern has been started, finish it
            this._factory.finishPattern();
        }

    }

//    public void setAllowedWordSearcher(String allowedWordSearcherPath)
//    {
//        _allowedWordsPath = allowedWordSearcherPath;
//    }
//
//    public void setAllowedWordSearcher(String allowedWordSearcherPath,String rejectedWordsOutputPath)
//    {
//        _allowedWordsPath = allowedWordSearcherPath;
//        _rejectedWordsPath = rejectedWordsOutputPath;
//    }

    @Override
    public String toString()
    {
        return this._factory.toString();
    }

}
