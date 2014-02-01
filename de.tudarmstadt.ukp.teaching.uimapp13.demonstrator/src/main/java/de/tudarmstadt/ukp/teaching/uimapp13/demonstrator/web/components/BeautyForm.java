package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;

import com.google.common.base.Joiner;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.PatternGenerator;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters.Adapter;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters.BeautyAdapter;

public class BeautyForm
    extends DomainSpecificForm
{
    private static final int DEFAULT_SLOGAN_COUNT = 20;

    private static final long serialVersionUID = -4506870702347552736L;

    private static final String DEFAULT_PRODUCT_NAME = "MyBeauty";

    private final String productName;
    private final String suggestedWords;
    private final String pattern;
    private final String partOfBody;
    private final int sloganCount;
    private final boolean isUseUbyForNewWords;

    public BeautyForm(final String id)
    {
        super(id);

        this.add(new Button("beauty-submit"));
        final List<String> selectablePartsOfBody = PatternGenerator.getSelectablePartsOfBody();
        final List<String> selectablePatterns = PatternGenerator.getSelectablePatterns();

        this.add(new RequiredTextField<Integer>("beauty-numSlogans", this
                .createIntProperty("sloganCount")));

        this.add(new RequiredTextField<String>("beauty-productName", this
                .createStringProperty("productName")));

        this.add(new DropDownChoice<String>("beauty-partOfBody", this
                .createStringProperty("partOfBody"), selectablePartsOfBody));

        this.add(new DropDownChoice<String>("beauty-pattern", this.createStringProperty("pattern"),
                selectablePatterns));

        this.add(new CheckBox("beauty-useUbyForNewWords", this.createBooleanProperty("isUseUbyForNewWords")));

        final TextArea<String> suggestedWordsTextArea = new TextArea<String>(
                "beauty-suggestedWords");
        suggestedWordsTextArea.setModel(this.createStringProperty("suggestedWords"));
        this.add(suggestedWordsTextArea);

        this.sloganCount = DEFAULT_SLOGAN_COUNT;
        this.productName = DEFAULT_PRODUCT_NAME;
        this.suggestedWords = Joiner.on("\n").join(Arrays.asList("women", "color", "life"));
        this.partOfBody = selectablePartsOfBody.get(0);
        this.pattern = selectablePatterns.get(0);
        this.isUseUbyForNewWords = true;

    }

    @Override
    protected Adapter createAdapter()
    {
        return new BeautyAdapter();
    }

    @Override
    protected HashMap<String, Object> createGenerationParameters()
    {
        final String[] suggestedWordsList = this.suggestedWords.split("\\n");
        final String commaSeparatedSuggestedWords = Joiner.on(",").join(suggestedWordsList);

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
