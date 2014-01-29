package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components;

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
    private static final List<String> DEFAULT_SUGGESTED_WORDS = Arrays.asList("fun", "speed",
            "family");

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

        this.sloganCount = DEFAULT_SLOGAN_COUNT;
        this.productName = DEFAULT_PRODUCT_NAME;
        this.suggestedWords = Joiner.on("\n").join(DEFAULT_SUGGESTED_WORDS);
        this.isGoodLuck = false;
        this.useProductNameCreatively = true;
        this.selectedEmotion = CarsAdapter.getAllEmotions().get(0);
        this.selectedTemplate = CarsAdapter.getAllTemplates().get(0);
    }

    @Override
    protected Map<String, Object> createGenerationParameters()
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
