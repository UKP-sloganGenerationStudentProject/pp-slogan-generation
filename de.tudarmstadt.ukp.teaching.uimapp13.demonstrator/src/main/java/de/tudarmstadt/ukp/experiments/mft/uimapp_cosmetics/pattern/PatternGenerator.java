package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;
import static org.apache.uima.fit.util.JCasUtil.select;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPart.ChunkPart;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPart.ChunkPartHeader.ChunkPartType;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern.ChunkHeader.ChunkType;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation.ChunkPatternAnnotation;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation.EmotionAnnotation;
import de.tudarmstadt.ukp.lmf.api.Uby;
import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;
import de.tudarmstadt.ukp.lmf.transform.DBConfig;
import de.tudarmstadt.ukp.uby.resource.UbySemanticFieldResource;
import de.tudarmstadt.ukp.uby.uima.annotator.UbySemanticFieldAnnotator;

public class PatternGenerator
    implements Serializable
{

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

    public static final String DONT_CARE = "don't care";
    public static final String NO_BODY_PART = "no body part";

    private static List<String> _patternsToAnnotate = Arrays.asList(DONT_CARE, "NC_", "NC_PC_NC_",
            "VC_NC_", "VC_NC_PC_NC_", "VC_", "NC_VC_", "NC_VC_ADJC_", "NC_VC_NC_");;
    private static List<String> _partsOfBodyToSelect = Arrays.asList(NO_BODY_PART, "eye", "skin",
            "lip", "nail", "hair", "lash");

    PatternFactory _factory;

    Resources _resources;
    private String _web1TPathname;

    public static void main(final String[] args)
        throws Exception
    {

        final PatternGenerator generator = new PatternGenerator();

        generator.useUbyForNewWords(true);

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
        final List<String> selectablePatterns = PatternGenerator.getSelectablePatterns();
        final List<String> selectablePartsOfBody = PatternGenerator.getSelectablePartsOfBody();

        /*Example :*/
        // final String pattern0 = selectablePatterns.get(6);
        /* when the associated checkbox gets checked*/
        // generator.selectPattern(pattern0);
        /* when the associated checkbox gets unchecked */
        // generator.unselectPattern(pattern0);

        // PART OF BODYye
        // retrieve the list of selectable part of the body

        /*Example :*/
        // final String partOfBody0 = selectablePartsOfBody.get(3);
        /* when the associated checkbox gets checked*/
        // generator.selectPartOfBody(partOfBody0);
        /* when the associated checkbox gets unchecked */
        // generator.unselectPartOfBodyn(partOfBody0);

        // PRODUCT NAME
        // set the product name
        generator.setProductName("myProductName");

        // PARTS OF THE BODY
        // set the suggested words (a string containing all the words separated with a coma"
        generator.setSuggestedWords("beauty,woman,colour");

        final Scanner user_input = new Scanner(System.in);

        // System.out.println(generator.toString());

        while (true) {

            System.out.println();
            System.out
                    .print("What do you want to do ? 0 : print PatternGenerator content, 1 : generate slogans, 2: exit");
            final int action = Integer.parseInt(user_input.next());
            if (action == 0) {
                System.out.println(generator.toString());
                continue;
            }
            if (action == 2) {
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

            for (final String slogan : generator.generateSlogans(10)) {
                System.out.println("\t" + slogan);
            }

        }

        user_input.close();

        // generator.generateSlogansToFile("/media/Storage/TUD/WS13-14/UIMA/Data/generatedSlogans.txt");

        // System.out.println(generator.toString());

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

        this._resources.setWordStatistic(lookup);

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
                getSelectablePatterns());
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
        this._resources.setPatternToGenerate(chunkPatternAsString);
    }

    public static List<String> getSelectablePartsOfBody()
    {
        return _partsOfBodyToSelect;
    }

    public void selectPartOfBody(final String part)
    {
        this._resources.setSelectedPartOfBody(part);
    }

    public List<String> generateSlogans(final int nbrOfSlogans)
    {
        return this._factory.generateSlogans(this._resources, nbrOfSlogans);
    }

    public void useUbyForNewWords(final boolean tof)
    {
        this._resources.useUbyForNewWords(tof);
    }

    public void generateSlogansToFile(final String path)
    {

        try {

            BufferedWriter writer = null;
            File file = null;

            file = new File(path);

            file.createNewFile();

            writer = new BufferedWriter(new FileWriter(file, false));

            writer.write("***********************Generated Slogans");
            writer.newLine();
            writer.newLine();

            writer.write(this._factory.getChunkIndex().toString());

            writer.newLine();
            writer.newLine();

            // write all the generated patterns into a file
            System.out.println("Write all the generated patterns into the file.");

            writer.write("***********************Pattern Generation");
            writer.newLine();
            writer.newLine();

            writer.write(this._factory.generateSlogansTest(this._resources));

            writer.close();

        }

        catch (final IOException e) {
            e.printStackTrace();
        }

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
                    chunkPartType = ChunkPartType.PRODUCT_NAME;
                }

                final ChunkPart chunkPart = ChunkPart.createChunkPart(chunkPartType,
                        token.getCoveredText(), lemma, sem, pos.getPosValue());

                this._factory.addPartToChunk(chunkPartType,chunkPart);

            }

            this._factory.finishChunk(this._resources);
        }

        if (prevPatternAnnot != null) {
            this._factory.finishPattern();
        }

    }

    @Override
    public String toString()
    {
        return this._factory.toString();
    }

    public class Resources
    {
        Uby _uby;
        EmotionAnalyzer _emotionAnalizer;
        JWeb1TSearcher _web1tWordStatistic;

        String _productName;
        String _patternToGenerate;
        String _selectedPartOfBody;
        List<String> _suggestedWords;

        boolean _useUbyGeneration;

        public Resources()
        {
            this._uby = null;
            this._emotionAnalizer = null;
            this._web1tWordStatistic = null;
            this._productName = "productName";
            this._patternToGenerate = "";
            this._selectedPartOfBody = "";
            this._suggestedWords = new ArrayList<String>();
            this._useUbyGeneration = true;
        }

        public Resources(final Uby uby, final EmotionAnalyzer emotionAnalizer,
                final JWeb1TSearcher web1tSearcher)
        {
            super();
            this._uby = uby;
            this._emotionAnalizer = emotionAnalizer;
            this._web1tWordStatistic = web1tSearcher;
        }

        public Uby getUby()
        {
            return this._uby;
        }

        public void setUby(final Uby uby)
        {
            this._uby = uby;
        }

        public void useUbyForNewWords(final boolean tof)
        {
            this._useUbyGeneration = tof;
        }

        public boolean isUbyGernationAllowed()
        {
            return this._useUbyGeneration;
        }

        public EmotionAnalyzer getEmotionAnalizer()
        {
            return this._emotionAnalizer;
        }

        public void setEmotionAnalizer(final EmotionAnalyzer emotionAnalizer)
        {
            this._emotionAnalizer = emotionAnalizer;
        }

        public JWeb1TSearcher getWordStatistic()
        {
            return this._web1tWordStatistic;
        }

        public void setWordStatistic(final JWeb1TSearcher web1tSearcher)
        {
            this._web1tWordStatistic = web1tSearcher;
        }

        public String getProductName()
        {
            return this._productName;
        }

        public void setProductName(final String productName)
        {
            this._productName = productName;
        }

        public String getPatternToGenerate()
        {
            return this._patternToGenerate;
        }

        public void setPatternToGenerate(final String patternToGenerate)
        {
            this._patternToGenerate = patternToGenerate;
        }

        public String getSelectedBodyPart()
        {
            return this._selectedPartOfBody;
        }

        public void setSelectedPartOfBody(final String part)
        {
            this._selectedPartOfBody = part;
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
