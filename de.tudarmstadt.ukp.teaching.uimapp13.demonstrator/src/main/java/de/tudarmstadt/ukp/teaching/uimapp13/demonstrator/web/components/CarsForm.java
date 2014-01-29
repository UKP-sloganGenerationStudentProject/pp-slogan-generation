package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.PropertyModel;

import com.google.common.base.Joiner;

import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters.Adapter;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters.CarsAdapter;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters.CarsAdapter.Template;

public class CarsForm
    extends DomainSpecificForm
{

    private static final long serialVersionUID = -7266582482549958376L;

    private static final int DEFAULT_SLOGAN_COUNT = 20;
    private static final String DEFAULT_PRODUCT_NAME = "MyCar";
    private static final String DEFAULT_SUGGESTED_WORDS = Joiner.on(",").join(
            Arrays.asList("fun", "speed", "family"));

    private int sloganCount;
    private String productName;
    private boolean useProductNameCreatively;
    private boolean isGoodLuck;
    private Template selectedTemplate;
    private String selectedEmotion;
    private String suggestedWords;

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
                "cars-templateChoice", selectedTemplateModel, CarsAdapter.getAllTemplates(),
                templateChoiceRenderer);
        this.add(templateChoice);

        // Emotion choice
        this.add(new DropDownChoice<String>("cars-emotionChoice", this
                .createStringProperty("selectedEmotion"), CarsAdapter.getAllEmotions()));

        // Suggested words
        final TextArea<String> suggestedWordsTextArea = new TextArea<String>("cars-suggestedWords");
        suggestedWordsTextArea.setModel(this.createStringProperty("suggestedWords"));
        this.add(suggestedWordsTextArea);

        this.initializeDefaultValues();
    }

    private void initializeDefaultValues()
    {
        this.sloganCount = this.getParam(CarsAdapter.SLOGAN_COUNT, DEFAULT_SLOGAN_COUNT);

        this.productName = this.getParam(CarsAdapter.PRODUCT_NAME, DEFAULT_PRODUCT_NAME);

        final String commaSeparatedSuggestedWords = this.getParam(CarsAdapter.SUGGESTED_WORDS,
                DEFAULT_SUGGESTED_WORDS);
        final String nlSeparatedWords = this.convertSeparator(commaSeparatedSuggestedWords, ",",
                "\n");
        this.suggestedWords = nlSeparatedWords;

        this.isGoodLuck = this.getParam(CarsAdapter.GOOD_LUCK, false);

        this.useProductNameCreatively = this.getParam(CarsAdapter.USE_PRODUCT_NAME_CREATIVELY,
                true);

        final String firstEmotion = CarsAdapter.getAllEmotions().get(0);
        this.selectedEmotion = this.getParam(CarsAdapter.EMOTION, firstEmotion);

        final Template firstTemplate = CarsAdapter.getAllTemplates().get(0);
        this.selectedTemplate = this.getParam(CarsAdapter.TEMPLATE, firstTemplate);
    }

    private String convertSeparator(final String commaSeparatedSuggestedWords, final String sep1,
            final String sep2)
    {
        return Joiner.on(sep2).join(Arrays.asList(commaSeparatedSuggestedWords.split(sep1)));
    }

    @SuppressWarnings("unchecked")
    private <T> T getParam(final String key, final T defaultValue)
    {
        T result = defaultValue;
        if (this.previousConfiguration.containsKey(key)) {
            final Object value = this.previousConfiguration.get(key);
            try {
                result = (T) value;
            }
            catch (final ClassCastException ex) {
                this.logger
                        .warn(String
                                .format("Value for parameter %s has not the expected type %s, instead: '%s'. Using default value %s instead.",
                                        key, defaultValue.getClass().getSimpleName(), value,
                                        defaultValue.toString()));

            }
        }
        return result;
    }

    @Override
    protected HashMap<String, Object> createGenerationParameters()
    {
        final HashMap<String, Object> parameters = new HashMap<>();
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

    @Override
    protected Adapter createAdapter()
    {
        return new CarsAdapter();
    }

}
