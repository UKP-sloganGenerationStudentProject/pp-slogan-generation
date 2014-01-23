package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;

import de.koch.uim_project.util.Config;
import de.koch.uim_project.util.Emotion;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters.GamesAdapter;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.Slogan;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.HomePage;

public class GamesForm
    extends Form<Void>
{
    private static final long serialVersionUID = -4876200976182929042L;

    private String selectedEmotion;
    private int numSlogans;
    private String gameName;
    private int minWordsForGeneration;
    private int maxSynsetDepth;
    private int maxWordList;
    private long randomSeed;

    private String featureWords;
    private String alienWords;

    public GamesForm(final String id)
    {
        super(id);
        this.add(new Button("games-submit"));
        final List<String> emotions = new ArrayList<>(Collections2.transform(
                Arrays.asList(Emotion.values()), Functions.toStringFunction()));
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

        final TextArea<String> featureWordsTextArea = new TextArea<String>("games-featureWords");
        featureWordsTextArea.setModel(this.createStringProperty("featureWords"));
        this.add(featureWordsTextArea);

        final TextArea<String> alienWordsTextArea = new TextArea<String>("games-alienWords");
        alienWordsTextArea.setModel(this.createStringProperty("alienWords"));
        this.add(alienWordsTextArea);

        /*
         * Set default values
         */
        final Config defaultConfig = Config.getDefaultConfig();
        this.selectedEmotion = emotions.get(0);
        this.numSlogans = defaultConfig.getSloganCount();
        this.gameName = defaultConfig.getGameName();
        this.minWordsForGeneration = defaultConfig.getMinWordlistForGeneration();
        this.maxSynsetDepth = defaultConfig.getMaxSynsetDepth();
        this.maxWordList = 1500;
        this.randomSeed = defaultConfig.getRandomSeed();
        this.alienWords = Joiner.on("\n").join(defaultConfig.getAlienFeatureList());
        this.featureWords = Joiner.on("\n").join(defaultConfig.getFeatureList());
    }

    private IModel<Integer> createIntProperty(final String property)
    {
        return new PropertyModel<Integer>(this, property);
    }

    private IModel<String> createStringProperty(final String property)
    {
        return new PropertyModel<String>(this, property);
    }

    private IModel<Long> createLongProperty(final String property)
    {
        return new PropertyModel<Long>(this, property);
    }

    @Override
    public void onSubmit()
    {
        final GamesAdapter adapter = new GamesAdapter();

        final HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(GamesAdapter.GAME_NAME, this.gameName);
        parameters.put(GamesAdapter.RANDOM_SEED, this.randomSeed);
        parameters.put(GamesAdapter.SLOGAN_COUNT, this.numSlogans);
        parameters.put(GamesAdapter.EMOTION, Emotion.valueOf(this.selectedEmotion));
        // TODO rkluge: allow to configure this
        parameters.put(GamesAdapter.PATTERN_WEIGHTS, Arrays.asList(1.0, 1.0, 1.0, 1.0));
        parameters.put(GamesAdapter.STYLISTIC_DEV_WEIGHTS, Arrays.asList(1.0, 1.0, 1.0, 1.0, 1.0));
        parameters.put(GamesAdapter.FEATURES, this.featureWords);
        parameters.put(GamesAdapter.ALIEN_FEATURES, this.alienWords);
        parameters.put(GamesAdapter.MAX_SYNSET_DEPTH, this.maxSynsetDepth);
        parameters.put(GamesAdapter.MIN_WORD_LIST_FOR_GENERATION, this.minWordsForGeneration);
        parameters.put(GamesAdapter.MAX_WORD_LIST_LENGTH, this.maxWordList);

        List<Slogan> slogans;
        String statusMessage;
        try {
            slogans = adapter.generateSlogans(parameters);
            statusMessage = "";
        }
        catch (final Exception e) {
            e.printStackTrace();
            statusMessage = e.getMessage();
            slogans = Arrays.asList();
        }
        this.setResponsePage(new HomePage(slogans, statusMessage));
    }

}
