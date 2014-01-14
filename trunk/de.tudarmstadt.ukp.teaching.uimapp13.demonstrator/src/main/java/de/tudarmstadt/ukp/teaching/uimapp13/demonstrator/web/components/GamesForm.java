package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components;

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

import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters.GamesAdapter;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.Slogan;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.HomePage;

public class GamesForm
    extends Form<Void>
{
    private static final long serialVersionUID = -4876200976182929042L;

    private String selectedEmotion;
    private int numSlogans = GamesAdapter.DEFAULT_NUM_SLOGANS;
    private String gameName = GamesAdapter.DEFAULT_GAME_NAME;
    private int minWordsForGeneration;
    private int maxSynsetDepth;
    private int maxWordList;
    private String featureWords;
    private String alienWords;

    private TextArea<String> featureWordsTextArea;
    private TextArea<String> alienWordsTextArea;

    public GamesForm(final String id)
    {
        super(id);
        this.add(new Button("games-submit"));
        // TODO rkluge: replace dummy emotions
        this.selectedEmotion = "Anger";
        this.add(new DropDownChoice<String>("games-emotion", new PropertyModel<String>(this,
                "selectedEmotion"), Arrays.asList("Anger", "Joy", "Fear")));

        this.featureWordsTextArea = new TextArea<String>("games-featureWords");
        this.alienWordsTextArea = new TextArea<String>("games-alienWords");

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

        this.add(this.featureWordsTextArea);
        this.featureWordsTextArea.setModel(this.createStringProperty("featureWords"));

        this.add(this.alienWordsTextArea);
        this.alienWordsTextArea.setModel(this.createStringProperty("alienWords"));
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
        final GamesAdapter adapter = new GamesAdapter();

        final HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(GamesAdapter.EMOTION, this.selectedEmotion);

        System.out.println(this.featureWordsTextArea.getInput());

        final List<Slogan> slogans = adapter.generateSlogans(parameters);
        this.setResponsePage(new HomePage(slogans));
    }

}
