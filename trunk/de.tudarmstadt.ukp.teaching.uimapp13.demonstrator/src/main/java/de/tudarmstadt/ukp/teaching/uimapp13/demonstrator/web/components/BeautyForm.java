package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.PatternGenerator;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters.BeautyAdapter;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.Slogan;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.HomePage;

public class BeautyForm
    extends Form<Void>
{
    private static final int DEFAULT_SLOGAN_COUNT = 20;

    private static final long serialVersionUID = -4506870702347552736L;

    private static final String DEFAULT_PRODUCT_NAME = "MyBeautyProduct";

    private final String productName;
    private final String suggestedWords;
    private final String pattern;
    private final String bodyPart;
    private final int sloganCount;

    private final BeautyAdapter sloganGeneratorAdapter;


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

        this.add(new DropDownChoice<String>("beauty-bodyPart", this
                .createStringProperty("bodyPart"), selectablePartsOfBody));

        this.add(new DropDownChoice<String>("beauty-pattern", this.createStringProperty("pattern"),
                selectablePatterns));

        final TextArea<String> suggestedWordsTextArea = new TextArea<String>(
                "beauty-suggestedWords");
        suggestedWordsTextArea.setModel(this.createStringProperty("suggestedWords"));
        this.add(suggestedWordsTextArea);

        this.sloganGeneratorAdapter = new BeautyAdapter();
        this.sloganCount = DEFAULT_SLOGAN_COUNT;
        this.productName = DEFAULT_PRODUCT_NAME;
        this.suggestedWords = "beauty\nwomen\ncolor";
        this.bodyPart = selectablePartsOfBody.get(0);
        this.pattern = selectablePatterns.get(0);

    }

    private IModel<Integer> createIntProperty(final String property)
    {
        return new PropertyModel<Integer>(this, property);
    }

    private IModel<String> createStringProperty(final String property)
    {
        return new PropertyModel<String>(this, property);
    }

    @Override
    public void onSubmit()
    {
        final Logger logger = LoggerFactory.getLogger(this.getClass());

        final String[] suggestedWords = this.suggestedWords.split("\\n");
        final String commaSeparatedSuggestedWords = Joiner.on(",").join(suggestedWords);

        final HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(BeautyAdapter.PRODUCT_NAME, this.productName);
        parameters.put(BeautyAdapter.SUGGESTED_WORDS, commaSeparatedSuggestedWords);
        parameters.put(BeautyAdapter.BODY_PART, this.bodyPart);
        parameters.put(BeautyAdapter.PATTERN, this.pattern);
        parameters.put(BeautyAdapter.SLOGAN_COUNT, this.sloganCount);


        final List<Slogan> slogans = new ArrayList<Slogan>();
        String statusMessage;
        try {
            logger.info("Initializing the generator...");
            sloganGeneratorAdapter.initialize(null);
            logger.info("Generating slogans...");
            sloganGeneratorAdapter.generateSlogans(parameters);
            logger.info("Generating slogans...Done");

            statusMessage = "";
        }
        catch (final Exception e) {
            e.printStackTrace();
            statusMessage = e.getMessage();
        }

        this.setResponsePage(new HomePage(slogans, statusMessage));
    }
}
