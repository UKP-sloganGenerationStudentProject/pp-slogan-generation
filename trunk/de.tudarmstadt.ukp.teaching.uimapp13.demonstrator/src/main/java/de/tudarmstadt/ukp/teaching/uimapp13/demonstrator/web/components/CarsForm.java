package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters.CarsAdapter;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters.CarsAdapter.Emotion;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters.CarsAdapter.Template;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.Slogan;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.HomePage;

public class CarsForm
    extends DomainSpecificForm
{

    private static final long serialVersionUID = -7266582482549958376L;

    private static final int DEFAULT_SLOGAN_COUNT = 20;
    private static final String DEFAULT_PRODUCT_NAME = "MyCar";
    private static final List<String> DEFAULT_SUGGESTED_WORDS = Arrays.asList("fun", "speed",
            "family");

    private boolean isInitializeAdapterOnLoad = false;

    private int sloganCount;
    private String productName;
    private boolean useProductNameCreatively;
    private boolean isGoodLuck;
    private Template selectedTemplate;
    private String selectedEmotion;
    private String suggestedWords;

    private CarsAdapter adapter;

    public CarsForm(final String id)
    {
        super(id);
        this.add(new Button("cars-submit"));

        this.add(new RequiredTextField<Integer>("cars-numSlogans", this.createProperty(
                "sloganCount", Integer.class)));

        this.add(new RequiredTextField<String>("cars-productName", this.createProperty(
                "productName", String.class)));

        this.add(new CheckBox("cars-useProductNameCreatively", this
                .createBooleanProperty("useProductNameCreatively")));
        this.add(new CheckBox("cars-goodLuck", this.createBooleanProperty("isGoodLuck")));

        // Template choice
        final ChoiceRenderer<Template> templateChoiceRenderer = new ChoiceRenderer<Template>(
                "name", "id");
        final PropertyModel<Template> selectedTemplateModel = this.createProperty(
                "selectedTemplate", Template.class);
        final DropDownChoice<Template> templateChoice = new DropDownChoice<Template>(
                "cars-templateChoice", selectedTemplateModel, Template.getAllTemplates(),
                templateChoiceRenderer);
        this.add(templateChoice);

        // Emotion choice
        this.add(new DropDownChoice<String>("cars-emotionChoice", this
                .createStringProperty("selectedEmotion"), Emotion.getAllEmotions()));

        // Suggested words
        final TextArea<String> suggestedWordsTextArea = new TextArea<String>("cars-suggestedWords");
        suggestedWordsTextArea.setModel(this.createStringProperty("suggestedWords"));
        this.add(suggestedWordsTextArea);

        this.sloganCount = DEFAULT_SLOGAN_COUNT;
        this.productName = DEFAULT_PRODUCT_NAME;
        this.suggestedWords = Joiner.on("\n").join(DEFAULT_SUGGESTED_WORDS);
        this.isGoodLuck = false;
        this.useProductNameCreatively = true;
        this.selectedEmotion = Emotion.getAllEmotions().get(0);
        this.selectedTemplate = Template.getAllTemplates().get(0);
    }

    @Override
    public void onSubmit()
    {
        final Logger logger = LoggerFactory.getLogger(this.getClass());

        if (this.loadAdapterLazily()) {
            this.initializeAdapter();
        }

        final Map<String, Object> parameters = this.createGenerationParameters();

        final List<Slogan> slogans = new ArrayList<>();
        String statusMessage;
        try {
            logger.info("Generating slogans...");
            slogans.addAll(this.adapter.generateSlogans(parameters));
            logger.info("Generating slogans...Done");
            statusMessage = "";
        }
        catch (final Exception e) {
            e.printStackTrace();
            statusMessage = e.getMessage();
        }

        this.setResponsePage(new HomePage(slogans, statusMessage));
    }

    private boolean loadAdapterEagerly()
    {
        return this.isInitializeAdapterOnLoad;
    }

    private boolean loadAdapterLazily()
    {
        return !this.loadAdapterEagerly();
    }

    private void initializeAdapter()
    {
        final Logger logger = LoggerFactory.getLogger(this.getClass());
        this.adapter = new CarsAdapter();
        logger.info("Initializing " + this.adapter.getClass().getSimpleName());
        try {
            this.adapter.initialize(this.createInitializationParameters());
        }
        catch (final Exception e) {
            throw new IllegalStateException(e);
        }
        logger.info("Initializing " + this.adapter.getClass().getSimpleName() + "...Done");
    }

    private Map<String, Object> createGenerationParameters()
    {
        final HashMap<String, Object> parameters = this.createInitializationParameters();
        parameters.put(CarsAdapter.SLOGAN_COUNT, this.sloganCount);
        parameters.put(CarsAdapter.PRODUCT_NAME, this.productName);

        parameters.put(CarsAdapter.USE_PRODUCT_NAME_CREATIVELY, this.useProductNameCreatively);
        parameters.put(CarsAdapter.GOOD_LUCK, this.isGoodLuck);
        parameters.put(CarsAdapter.TEMPLATE, this.selectedTemplate);
        parameters.put(CarsAdapter.EMOTION, this.selectedEmotion);

        final String[] suggestedWordsList = this.suggestedWords.split("\\n");
        final String commaSeparatedSuggestedWords = Joiner.on(",").join(suggestedWordsList);
        parameters.put(CarsAdapter.SUGGESTED_WORDS, commaSeparatedSuggestedWords);
        return parameters;
    }

}
