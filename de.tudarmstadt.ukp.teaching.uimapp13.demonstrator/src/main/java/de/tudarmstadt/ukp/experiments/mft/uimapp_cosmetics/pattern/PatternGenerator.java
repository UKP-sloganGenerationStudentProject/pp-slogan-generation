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
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.emotions.EmotionAnalyzer;
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

    static List<String> _patternsToAnnotate = Arrays.asList("NC_", "NC_PC_NC_", "VC_NC_", "VC_NC_PC_NC_",
            "VC_", "NC_VC_", "NC_VC_ADJC_", "NC_VC_NC_");
    static List<String> _partsOfBodyToSelect = Arrays.asList("eye", "skin", "lips", "fingernails");

    PatternFactory _factory;

    Resources _resources;
    private String _web1TPathname;

    public static void main(final String[] args)
        throws Exception
    {

        final PatternGenerator generator = new PatternGenerator();
        generator.setEmotionFilePath("src/main/resources/NRCemotionlexicon.pdf");
        generator.setSloganBasePath("src/main/resources/beautySlogans.txt");
        generator.setUbyDBData("localhost/uby_medium_0_3_0", "com.mysql.jdbc.Driver", "mysql",
                "root", "pass");
        generator.init();

        /*
         * Generation parameters
         */
        // PATTERN
        // retrieve the list of selectable patterns (add a chekckbox with each element of the list
        // as label)
        final List<String> selectablePatterns = generator.getSelectablePatterns();

        /*Example :*/
        final String pattern0 = selectablePatterns.get(0);
        /* when the associated checkbox gets checked*/
        generator.selectPattern(pattern0);
        /* when the associated checkbox gets unchecked */
        // generator.unselectPattern(pattern0);

        // PART OF BODY
        // retrieve the list of selectable part of the body
        final List<String> selectablePartsOfBody = generator.getSelectablePartsOfBody();

        /*Example :*/
        final String partOfBody0 = selectablePartsOfBody.get(0);
        /* when the associated checkbox gets checked*/
        generator.selectPartOfBody(partOfBody0);
        /* when the associated checkbox gets unchecked */
        // generator.unselectPartOfBodyn(partOfBody0);

        // PRODUCT NAME
        // set the product name
        generator.setProductName("myProduct");

        // PARTS OF THE BODY
        // set the suggested words (a string containing all the words separated with a coma"
        generator.setSuggestedWords("beauty,woman,colour");

        for (final String slogan : generator.generatePatterns()) {
            System.out.println(slogan);
        }
    }

    public PatternGenerator()
    {
        this._emotionFilePath = "";
        this._sloganBasePath = "";
        this._ubyDBURL = "";
        this._ubyDBDriver = "";
        this._ubyDBDriverName = "";
        this._ubyDBUserName = "";
        this._ubyDBPassword = "";

        this._resources = new Resources();


    }

    public void init()
        throws Exception
    {

        this._factory = new PatternFactory();

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

        final String dkproHome = System.getenv("DKPRO_HOME");
        String web1tPathname = dkproHome + "/web1t/ENGLISH/";
        if (this._web1TPathname != null) {
            web1tPathname = this._web1TPathname;
        }
        final JWeb1TSearcher lookup = new JWeb1TSearcher(new File(web1tPathname), 1, 1);

        this._resources.setWeb1tSearcher(lookup);

        final EmotionAnalyzer emotionAnalizer = new EmotionAnalyzer(this._emotionFilePath);
        this._resources.setEmotionAnalizer(emotionAnalizer);

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
                this._patternsToAnnotate);
        final AnalysisEngineDescription emotionAnnotator = createEngineDescription(
                EmotionAnnotator.class, EmotionAnnotator.PARAM_EMOTIONS_LEXICON_PATH,
                this._emotionFilePath);

        final JCasIterable pipeline = new JCasIterable(reader, sloganAnnotator, segmentation,
                posLemmaAnnotator, chunkAnnotator, semanticFieldAnnotator, chunkPatternAnnotator,
                emotionAnnotator);

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
        this._resources.setSuggestedWords(Arrays.asList(suggestedWords.split(",")));
    }

    public static List<String> getSelectablePatterns()
    {
        return _patternsToAnnotate;
    }

    public void selectPattern(final String chunkPatternAsString)
    {
        if (!this._resources.getPatternsToGenerate().contains(chunkPatternAsString)) {
            this._resources.getPatternsToGenerate().add(chunkPatternAsString);
        }
    }

    public void unselectPattern(final String chunkPatternAsString)
    {
        if (this._resources.getPatternsToGenerate().contains(chunkPatternAsString)) {
            this._resources.getPatternsToGenerate().remove(chunkPatternAsString);
        }
    }

    public static List<String> getSelectablePartsOfBody()
    {
        return _partsOfBodyToSelect;
    }

    public void selectPartOfBody(final String part)
    {
        if (!this._resources.getSelectedPartsOfBody().contains(part)) {
            this._resources.getSelectedPartsOfBody().add(part);
        }
    }

    public void unselectPartOfBodyn(final String part)
    {
        if (this._resources.getSelectedPartsOfBody().contains(part)) {
            this._resources.getSelectedPartsOfBody().remove(part);
        }
    }

    public List<String> generatePatterns()
    {
        return this._factory.generatePatterns(this._resources);
    }

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
                    isNewPattern = true;
                }
                else {
                    if (prevPatternAnnot.getBegin() != patterns.get(0).getBegin()) {
                        this._factory.finishPattern();
                        isNewPattern = true;
                    }
                    else {
                        isNewPattern = false;
                    }

                }
                prevPatternAnnot = patterns.get(0);

            }
            else {
                if (prevPatternAnnot != null) {
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
                    this._factory.setPatternValue(pattern.getCoveredText());
                }

            }

            /*
            *
            * PROCESS THE CHUNK
            *
            */

            ChunkType chunkType = null;

            try {
                chunkType = ChunkType.valueOf(chunk.getChunkValue());
            }
            catch (final IllegalArgumentException e) {
                // the chunk type is not implemented in our system (because we are not interested in
                // it)
                System.out.println("Chunk " + chunk.getChunkValue() + " ignored.");
                continue;
            }

            this._factory.startNewChunk(chunkType);

            for (final Token token : JCasUtil.selectCovered(Token.class, chunk)) {
                final List<POS> poss = JCasUtil.selectCovered(POS.class, token);
                POS pos = new POS(aJCas);
                if (poss.size() > 0) {
                    pos = poss.get(0);
                }

                ChunkPartType chunkPartType = ChunkPartType.getTypeOf(pos.getType().getShortName());

                final List<Lemma> lemmas = JCasUtil.selectCovered(Lemma.class, token);
                String lemma = "";
                if (lemmas.size() > 0) {
                    lemma = lemmas.get(0).getValue();
                }

                final List<SemanticField> sems = JCasUtil
                        .selectCovering(SemanticField.class, token);
                String sem = "";
                if (sems.size() > 0) {
                    sem = sems.get(0).getValue();
                }

                if (token.getCoveredText().equals(ChunkPartType.PRODUCT_NAME.toString())) {
                    chunkPartType = chunkPartType.PRODUCT_NAME;
                }

                final ChunkPart chunkPart = ChunkPart.createChunkPart(chunkPartType,
                        token.getCoveredText(), lemma, sem, pos.getPosValue());

                this._factory.addPartToChunk(chunkPart);

            }

            this._factory.finishChunk();
        }

        if (prevPatternAnnot != null) {
            this._factory.finishPattern();
        }

    }

    public class Resources
    {
        Uby _uby;
        EmotionAnalyzer _emotionAnalizer;
        JWeb1TSearcher _web1tSearcher;

        String _productName;
        List<String> _patternsToGenerate;
        List<String> _selectedPartsOfBody;
        List<String> _suggestedWords;

        public Resources()
        {
            this._uby = null;
            this._emotionAnalizer = null;
            this._web1tSearcher = null;
            this._productName = "productName";
            this._patternsToGenerate = new ArrayList<String>();
            this._selectedPartsOfBody = new ArrayList<String>();
            this._suggestedWords = new ArrayList<String>();
        }

        public Resources(final Uby uby, final EmotionAnalyzer emotionAnalizer,
                final JWeb1TSearcher web1tSearcher)
        {
            super();
            this._uby = uby;
            this._emotionAnalizer = emotionAnalizer;
            this._web1tSearcher = web1tSearcher;
        }

        public Uby getUby()
        {
            return this._uby;
        }

        public void setUby(final Uby uby)
        {
            this._uby = uby;
        }

        public EmotionAnalyzer getEmotionAnalizer()
        {
            return this._emotionAnalizer;
        }

        public void setEmotionAnalizer(final EmotionAnalyzer emotionAnalizer)
        {
            this._emotionAnalizer = emotionAnalizer;
        }

        public JWeb1TSearcher getWeb1tSearcher()
        {
            return this._web1tSearcher;
        }

        public void setWeb1tSearcher(final JWeb1TSearcher web1tSearcher)
        {
            this._web1tSearcher = web1tSearcher;
        }

        public String getProductName()
        {
            return this._productName;
        }

        public void setProductName(final String productName)
        {
            this._productName = productName;
        }

        public List<String> getPatternsToGenerate()
        {
            return this._patternsToGenerate;
        }

        public void setPatternsToGenerate(final List<String> patternsToGenerate)
        {
            this._patternsToGenerate = patternsToGenerate;
        }

        public List<String> getSelectedPartsOfBody()
        {
            return this._selectedPartsOfBody;
        }

        public void setSelectedPartsOfBody(final List<String> selectedPartsOfBody)
        {
            this._selectedPartsOfBody = selectedPartsOfBody;
        }

        public List<String> getSuggestedWords()
        {
            return this._suggestedWords;
        }

        public void setSuggestedWords(final List<String> suggestedWords)
        {
            this._suggestedWords = suggestedWords;
        }
    }

}
