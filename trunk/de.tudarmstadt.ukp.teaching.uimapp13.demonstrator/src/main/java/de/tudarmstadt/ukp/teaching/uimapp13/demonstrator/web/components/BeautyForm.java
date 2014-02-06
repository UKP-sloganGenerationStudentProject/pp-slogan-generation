package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components;

import java.util.HashMap;
import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Parameters;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters.Adapter;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters.BeautyAdapter;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.utils.DemonstratorUtils;

public class BeautyForm
    extends DomainSpecificForm
{
    private static final long serialVersionUID = -4506870702347552736L;

    private String productName;
    private String suggestedWords;
    private String pattern;
    private String partOfBody;
    private int sloganCount;
    private boolean isUseUbyForNewWords;

    public BeautyForm(final String id)
    {
        super(id);

        this.add(new Button("beauty-submit"));
        final List<String> selectablePartsOfBody = Parameters.getSelectablePartsOfBody();
        final List<String> selectablePatterns = Parameters.getSelectablePatterns();

        this.add(new RequiredTextField<Integer>("beauty-numSlogans", this
                .createIntProperty("sloganCount")));

        this.add(new RequiredTextField<String>("beauty-productName", this
                .createStringProperty("productName")));

        this.add(new DropDownChoice<String>("beauty-partOfBody", this
                .createStringProperty("partOfBody"), selectablePartsOfBody));

        this.add(new DropDownChoice<String>("beauty-pattern", this.createStringProperty("pattern"),
                selectablePatterns));

        this.add(new CheckBox("beauty-useUbyForNewWords", this
                .createBooleanProperty("isUseUbyForNewWords")));

        final TextArea<String> suggestedWordsTextArea = new TextArea<String>(
                "beauty-suggestedWords");
        suggestedWordsTextArea.setModel(this.createStringProperty("suggestedWords"));
        this.add(suggestedWordsTextArea);

    }

    @Override
    protected boolean loadAdapterEagerly()
    {
        return false;
    }

    @Override
    protected void initializeDefaultValues()
    {
        this.sloganCount = this.getParam(BeautyAdapter.SLOGAN_COUNT,
                BeautyAdapter.DEFAULT_SLOGAN_COUNT);

        this.productName = this.getParam(BeautyAdapter.PRODUCT_NAME,
                BeautyAdapter.DEFAULT_PRODUCT_NAME);

        final String commaSeparatedSuggestedWords = this.getParam(BeautyAdapter.SUGGESTED_WORDS,
                BeautyAdapter.DEFAULT_SUGGESTED_WORDS);
        this.suggestedWords = DemonstratorUtils
                .convertSeparatorFromCommaToHtmlTextarea(commaSeparatedSuggestedWords);

        this.partOfBody = this
                .getParam(BeautyAdapter.BODY_PART, BeautyAdapter.DEFAULT_PART_OF_BODY);

        this.pattern = this.getParam(BeautyAdapter.PATTERN, BeautyAdapter.DEFAULT_PATTERN);

        this.isUseUbyForNewWords = this.getParam(BeautyAdapter.USE_UBY_FOR_NEW_WORDS,
                BeautyAdapter.DEFAULT_USE_UBY_FOR_NEW_WORDS);
    }

    @Override
    protected Adapter createAdapter()
    {
        return new BeautyAdapter();
    }

    @Override
    protected HashMap<String, Object> createGenerationParameters()
    {
        final String commaSeparatedSuggestedWords = DemonstratorUtils
                .convertSeparatorFromHtmlTextareaToComma(this.suggestedWords);

        final HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(BeautyAdapter.PRODUCT_NAME, this.productName);
        parameters.put(BeautyAdapter.SLOGAN_COUNT, this.sloganCount);
        parameters.put(BeautyAdapter.SUGGESTED_WORDS, commaSeparatedSuggestedWords);
        parameters.put(BeautyAdapter.BODY_PART, this.partOfBody);
        parameters.put(BeautyAdapter.PATTERN, this.pattern);
        parameters.put(BeautyAdapter.USE_UBY_FOR_NEW_WORDS, this.isUseUbyForNewWords);
        return parameters;
    }

}
