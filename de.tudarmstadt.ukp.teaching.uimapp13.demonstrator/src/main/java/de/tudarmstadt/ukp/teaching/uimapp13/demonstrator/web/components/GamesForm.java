package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;

import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;

import de.koch.uim_project.util.Config;
import de.koch.uim_project.util.Emotion;
import de.koch.uim_project.util.Pattern;
import de.koch.uim_project.util.StylisticDevice;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters.Adapter;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters.GamesAdapter;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.utils.DemonstratorUtils;

public class GamesForm
    extends DomainSpecificForm
{
    private static final long serialVersionUID = -4876200976182929042L;

    private String selectedEmotion;
    private int numSlogans;
    private String gameName;
    private int minWordsForGeneration;
    private int maxSynsetDepth;
    private int maxWordList;
    private long randomSeed;

    private double patternWeight0;
    private double patternWeight1;
    private double patternWeight2;
    private double patternWeight3;

    private double styleDevWeight0;
    private double styleDevWeight1;
    private double styleDevWeight2;
    private double styleDevWeight3;
    private double styleDevWeight4;

    private String featureWords;
    private String alienWords;

    public GamesForm(final String id)
    {
        super(id);
        this.add(new Button("games-submit"));
        final List<String> emotions = Emotion.valuesAsString();

        this.add(new DropDownChoice<String>("games-emotion", this
                .createStringProperty("selectedEmotion"), emotions));

        this.add(new RequiredTextField<Integer>("games-numSlogans", this
                .createIntProperty("numSlogans")));

        this.add(new RequiredTextField<String>("games-gameName", this
                .createStringProperty("gameName")));

        this.add(new RequiredTextField<Integer>("games-minWordsForGeneration", this
                .createIntProperty("minWordsForGeneration")));

        this.add(new RequiredTextField<Integer>("games-maxSynsetDepth", this
                .createIntProperty("maxSynsetDepth")));

        this.add(new RequiredTextField<Integer>("games-maxWordList", this
                .createIntProperty("maxWordList")));

        this.add(new RequiredTextField<Long>("games-randomSeed", this
                .createLongProperty("randomSeed")));

        /*
         * Pattern weights
         */
        // TODO rkluge : this is extremely ugly...
        final Pattern[] patterns = Pattern.values();
        this.add(new Label("games-pattern0", patterns[0].toString()));
        this.add(new Label("games-pattern1", patterns[1].toString()));
        this.add(new Label("games-pattern2", patterns[2].toString()));
        this.add(new Label("games-pattern3", patterns[3].toString()));

        this.add(new RequiredTextField<Double>("games-patternWeight0", this.createProperty(
                "patternWeight0", Double.class)));
        this.add(new RequiredTextField<Double>("games-patternWeight1", this.createProperty(
                "patternWeight1", Double.class)));
        this.add(new RequiredTextField<Double>("games-patternWeight2", this.createProperty(
                "patternWeight2", Double.class)));
        this.add(new RequiredTextField<Double>("games-patternWeight3", this.createProperty(
                "patternWeight3", Double.class)));

        /*
         * Stylistic devices weights
         */
        final StylisticDevice[] stylisticDevices = StylisticDevice.values();
        this.add(new Label("games-styleDev0", stylisticDevices[0].toString()));
        this.add(new Label("games-styleDev1", stylisticDevices[1].toString()));
        this.add(new Label("games-styleDev2", stylisticDevices[2].toString()));
        this.add(new Label("games-styleDev3", stylisticDevices[3].toString()));
        this.add(new Label("games-styleDev4", stylisticDevices[4].toString()));

        this.add(new RequiredTextField<Double>("games-styleDevWeight0", this.createProperty(
                "styleDevWeight0", Double.class)));
        this.add(new RequiredTextField<Double>("games-styleDevWeight1", this.createProperty(
                "styleDevWeight1", Double.class)));
        this.add(new RequiredTextField<Double>("games-styleDevWeight2", this.createProperty(
                "styleDevWeight2", Double.class)));
        this.add(new RequiredTextField<Double>("games-styleDevWeight3", this.createProperty(
                "styleDevWeight3", Double.class)));
        this.add(new RequiredTextField<Double>("games-styleDevWeight4", this.createProperty(
                "styleDevWeight4", Double.class)));

        /*
         * Features
         */
        final TextArea<String> featureWordsTextArea = new TextArea<String>("games-featureWords");
        featureWordsTextArea.setModel(this.createStringProperty("featureWords"));
        this.add(featureWordsTextArea);

        final TextArea<String> alienWordsTextArea = new TextArea<String>("games-alienWords");
        alienWordsTextArea.setModel(this.createStringProperty("alienWords"));
        this.add(alienWordsTextArea);

    }

    @Override
    protected void initializeDefaultValues()
    {
        final Config defaultConfig = Config.getDefaultConfig();

        this.gameName = this.getParam(GamesAdapter.GAME_NAME, defaultConfig.getGameName());
        this.randomSeed = this.getParam(GamesAdapter.RANDOM_SEED, defaultConfig.getRandomSeed());
        this.numSlogans = this.getParam(GamesAdapter.SLOGAN_COUNT, defaultConfig.getSloganCount());
        this.selectedEmotion = this.getParam(GamesAdapter.EMOTION, defaultConfig.getEmotion()
                .toString());
        this.minWordsForGeneration = this.getParam(GamesAdapter.MIN_WORD_LIST_FOR_GENERATION,
                defaultConfig.getMinWordlistForGeneration());
        this.maxSynsetDepth = this.getParam(GamesAdapter.MAX_SYNSET_DEPTH,
                defaultConfig.getMaxSynsetDepth());
        this.maxWordList = this.getParam(GamesAdapter.MAX_WORD_LIST_LENGTH,
                defaultConfig.getMaxWordListLength());

        final String defaultAlienWords = Joiner.on(",").join(defaultConfig.getAlienFeatureList());
        final String commaSeparatedAlienWords = this.getParam(GamesAdapter.ALIEN_FEATURES,
                defaultAlienWords);
        this.alienWords = DemonstratorUtils
                .convertSeparatorFromCommaToHtmlTextarea(commaSeparatedAlienWords);

        final String defaultFeatureWords = Joiner.on(",").join(defaultConfig.getFeatureList());
        final String commaSeparatedFeatureWords = this.getParam(GamesAdapter.FEATURES,
                defaultFeatureWords);
        this.featureWords = DemonstratorUtils
                .convertSeparatorFromCommaToHtmlTextarea(commaSeparatedFeatureWords);

        final List<Pattern> patterns = Pattern.getAll();
        final Map<Pattern, Double> defaultPatternToWeight = defaultConfig.getPatternweights();

        final List<Double> patternWeights = this.getParam(GamesAdapter.PATTERN_WEIGHTS,
                this.getValues(defaultPatternToWeight, patterns));
        this.patternWeight0 = patternWeights.get(0);
        this.patternWeight1 = patternWeights.get(1);
        this.patternWeight2 = patternWeights.get(2);
        this.patternWeight3 = patternWeights.get(3);

        final List<StylisticDevice> stylisticDevices = StylisticDevice.getAll();
        final Map<StylisticDevice, Double> defaultStyleDevToWeight = defaultConfig.getSdweights();

        final List<Double> styleDevWeights = this.getParam(GamesAdapter.STYLISTIC_DEV_WEIGHTS,
                this.getValues(defaultStyleDevToWeight, stylisticDevices));
        this.styleDevWeight0 = styleDevWeights.get(0);
        this.styleDevWeight1 = styleDevWeights.get(1);
        this.styleDevWeight2 = styleDevWeights.get(2);
        this.styleDevWeight3 = styleDevWeights.get(3);
        this.styleDevWeight4 = styleDevWeights.get(4);
    }

    private <S, T> List<T> getValues(final Map<S, T> mapping, final Collection<S> keys)
    {
        return new ArrayList<>(Collections2.transform(keys, Functions.forMap(mapping)));
    }

    @Override
    protected HashMap<String, Object> createGenerationParameters()
    {
        final List<Double> patternWeights = Arrays.asList(this.patternWeight0, this.patternWeight1,
                this.patternWeight2, this.patternWeight3);

        final List<Double> styleDevWeights = Arrays.asList(this.styleDevWeight0,
                this.styleDevWeight1, this.styleDevWeight2, this.styleDevWeight3,
                this.styleDevWeight4);

        final String commaSepFeatures = DemonstratorUtils
                .convertSeparatorFromHtmlTextareaToComma(this.featureWords);

        final String commaSepAlienWords = DemonstratorUtils
                .convertSeparatorFromHtmlTextareaToComma(this.alienWords);

        final HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(GamesAdapter.GAME_NAME, this.gameName);
        parameters.put(GamesAdapter.RANDOM_SEED, this.randomSeed);
        parameters.put(GamesAdapter.SLOGAN_COUNT, this.numSlogans);
        parameters.put(GamesAdapter.EMOTION, this.selectedEmotion);
        parameters.put(GamesAdapter.PATTERN_WEIGHTS, patternWeights);
        parameters.put(GamesAdapter.STYLISTIC_DEV_WEIGHTS, styleDevWeights);
        parameters.put(GamesAdapter.FEATURES, commaSepFeatures);
        parameters.put(GamesAdapter.ALIEN_FEATURES, commaSepAlienWords);
        parameters.put(GamesAdapter.MAX_SYNSET_DEPTH, this.maxSynsetDepth);
        parameters.put(GamesAdapter.MIN_WORD_LIST_FOR_GENERATION, this.minWordsForGeneration);
        parameters.put(GamesAdapter.MAX_WORD_LIST_LENGTH, this.maxWordList);
        return parameters;
    }

    @Override
    protected Adapter createAdapter()
    {
        return new GamesAdapter();
    }

}
