package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components;

import java.util.HashMap;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.PropertyModel;

import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters.Adapter;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters.CarsAdapter;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters.CarsAdapter.Template;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.utils.DemonstratorUtils;

public class CarsForm
    extends DomainSpecificForm
{

    private static final long serialVersionUID = -7266582482549958376L;

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

    }

    @Override
    protected void initializeDefaultValues()
    {

        this.productName = this
                .getParam(CarsAdapter.PRODUCT_NAME, CarsAdapter.DEFAULT_PRODUCT_NAME);

        this.sloganCount = this
                .getParam(CarsAdapter.SLOGAN_COUNT, CarsAdapter.DEFAULT_SLOGAN_COUNT);

        final String commaSeparatedSuggestedWords = this.getParam(CarsAdapter.SUGGESTED_WORDS,
                CarsAdapter.DEFAULT_SUGGESTED_WORDS);
        this.suggestedWords = DemonstratorUtils
                .convertSeparatorFromCommaToHtmlTextarea(commaSeparatedSuggestedWords);

        this.useProductNameCreatively = this.getParam(CarsAdapter.USE_PRODUCT_NAME_CREATIVELY,
                CarsAdapter.DEFAULT_USE_PRODUCT_NAME_CREATIVELY);

        this.isGoodLuck = this.getParam(CarsAdapter.GOOD_LUCK, CarsAdapter.DEFAULT_GOOD_LUCK);

        this.selectedEmotion = this.getParam(CarsAdapter.EMOTION, CarsAdapter.DEFAULT_EMOTION);

        final int defaultId = CarsAdapter.DEFAULT_TEMPLATE.getId();
        final int selectedTemplateId = this.getParam(CarsAdapter.TEMPLATE_ID, defaultId);
        this.selectedTemplate = CarsAdapter.getTemplateById(selectedTemplateId);
    }

    @Override
    protected boolean loadAdapterEagerly()
    {
        return false;
    }

    @Override
    protected HashMap<String, Object> createGenerationParameters()
    {
        final HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(CarsAdapter.SLOGAN_COUNT, this.sloganCount);
        parameters.put(CarsAdapter.PRODUCT_NAME, this.productName);

        parameters.put(CarsAdapter.SUGGESTED_WORDS,
                DemonstratorUtils.convertSeparatorFromHtmlTextareaToComma(this.suggestedWords));

        parameters.put(CarsAdapter.USE_PRODUCT_NAME_CREATIVELY, this.useProductNameCreatively);
        parameters.put(CarsAdapter.GOOD_LUCK, this.isGoodLuck);

        parameters.put(CarsAdapter.TEMPLATE_ID, this.selectedTemplate.getId());
        parameters.put(CarsAdapter.EMOTION, this.selectedEmotion);
        return parameters;
    }

    @Override
    protected Adapter createAdapter()
    {
        return new CarsAdapter();
    }

}
