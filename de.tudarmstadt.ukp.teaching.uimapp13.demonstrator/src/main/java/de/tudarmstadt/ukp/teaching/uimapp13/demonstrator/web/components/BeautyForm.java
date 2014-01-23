package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components;

import java.util.ArrayList;
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
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.configuration.DemonstratorConfig;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.Slogan;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.HomePage;

public class BeautyForm
    extends Form<Void>
{
    private static final int DEFAULT_SLOGAN_COUNT = 20;

    private static final long serialVersionUID = -4506870702347552736L;

    private static final String DEFAULT_PRODUCT_NAME = "MyBeauty";

    private String productName;
    private String suggestedWords;
    private String pattern;
    private String partOfBody;
    private int sloganCount;

    public BeautyForm(final String id)
    {
        super(id);
        this.add(new Button("beauty-submit"));
        final PatternGenerator generator = new PatternGenerator();
        final List<String> selectablePartsOfBody = generator.getSelectablePartsOfBody();
        final List<String> selectablePatterns = generator.getSelectablePatterns();

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
        this.suggestedWords = "beauty\nwomen\ncolor";
        this.partOfBody = selectablePartsOfBody.get(0);
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
        final PatternGenerator generator = new PatternGenerator();

        generator.setProductName(this.productName);
        generator.setWeb1TPathname(System.getenv("DKPRO_HOME") + "/web1t/ENGLISH");
        generator
                .setEmotionFilePath("/s21/studium/11_semester/master_thesis/workspace/de.tudarmstadt.ukp.teaching.uimapp13.demonstrator/src/main/resources/NRCemotionlexicon.pdf");
        generator
                .setSloganBasePath("/s21/studium/11_semester/master_thesis/workspace/de.tudarmstadt.ukp.teaching.uimapp13.demonstrator/src/main/resources/beautySlogans.txt");
        generator.setUbyDBData(DemonstratorConfig.UBY_URL, DemonstratorConfig.JDBC_DRIVER,
                DemonstratorConfig.JDBC_DRIVER_NAME, DemonstratorConfig.UBY_USER,
                DemonstratorConfig.UBY_PASSWORD);

        final String[] suggestedWords = this.suggestedWords.split("\\n");
        final String commaSeparatedSuggestedWords = Joiner.on(",").join(suggestedWords);
        generator.setSuggestedWords(commaSeparatedSuggestedWords);

        generator.selectPartOfBody(this.partOfBody);
        generator.selectPattern(this.pattern);

        final List<Slogan> slogans = new ArrayList<>();
        String statusMessage;
        try {
            generator.init();
            logger.info("Generating slogans...");
            final List<String> generatedSlogans = generator.generatePatterns();
            logger.info("Generating slogans...Done");
            final int returnedSloganCount = Math.min(generatedSlogans.size(), this.sloganCount);

            for (int i = 0; i < returnedSloganCount; ++i) {

                final Slogan slogan = new Slogan(generatedSlogans.get(i));
                slogans.add(slogan);
            }
            statusMessage = "";
        }
        catch (final Exception e) {
            e.printStackTrace();
            statusMessage = e.getMessage();
        }

        this.setResponsePage(new HomePage(slogans, statusMessage));
    }
}
