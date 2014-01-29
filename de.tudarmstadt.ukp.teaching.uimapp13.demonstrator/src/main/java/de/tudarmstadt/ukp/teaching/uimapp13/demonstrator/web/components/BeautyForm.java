package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.PatternGenerator;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters.BeautyAdapter;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.Slogan;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.HomePage;

public class BeautyForm
    extends DomainSpecificForm
{
    private static final int DEFAULT_SLOGAN_COUNT = 20;

    private static final long serialVersionUID = -4506870702347552736L;

    private static final String DEFAULT_PRODUCT_NAME = "MyBeauty";

    private boolean isInitializeAdapterOnLoad = false;

    private String productName;
    private String suggestedWords;
    private String pattern;
    private String partOfBody;
    private int sloganCount;

    private BeautyAdapter adapter;

    public BeautyForm(final String id)
    {
        super(id);

        if (this.loadAdapterEagerly()) {
            this.initializeAdapter();
        }

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

        final TextArea<String> suggestedWordsTextArea = new TextArea<String>(
                "beauty-suggestedWords");
        suggestedWordsTextArea.setModel(this.createStringProperty("suggestedWords"));
        this.add(suggestedWordsTextArea);

        this.sloganCount = DEFAULT_SLOGAN_COUNT;
        this.productName = DEFAULT_PRODUCT_NAME;
        this.suggestedWords = Joiner.on("\n").join(Arrays.asList("women", "color", "life"));
        this.partOfBody = selectablePartsOfBody.get(0);
        this.pattern = selectablePatterns.get(0);

    }

    private void initializeAdapter()
    {
        final Logger logger = LoggerFactory.getLogger(this.getClass());
        this.adapter = new BeautyAdapter();
        logger.info("Initializing " + this.adapter.getClass().getSimpleName());
        try {
            this.adapter.initialize(null);
        }
        catch (final Exception e) {
            throw new IllegalStateException(e);
        }
        logger.info("Initializing " + this.adapter.getClass().getSimpleName() + "...Done");
    }

    @Override
    public void onSubmit()
    {
        final Logger logger = LoggerFactory.getLogger(this.getClass());

        if (this.loadAdapterLazily()) {
            this.initializeAdapter();
        }

        final Map<String, Object> parameters = this.createParameters();

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

    private Map<String, Object> createParameters()
    {
        final String[] suggestedWordsList = this.suggestedWords.split("\\n");
        final String commaSeparatedSuggestedWords = Joiner.on(",").join(suggestedWordsList);

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put(BeautyAdapter.PRODUCT_NAME, this.productName);
        parameters.put(BeautyAdapter.SLOGAN_COUNT, this.sloganCount);
        parameters.put(BeautyAdapter.SUGGESTED_WORDS, commaSeparatedSuggestedWords);
        parameters.put(BeautyAdapter.BODY_PART, this.partOfBody);
        parameters.put(BeautyAdapter.PATTERN, this.pattern);
        return parameters;
    }
}
