package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web;

import java.util.HashMap;
import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;

import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters.BeautyAdapter;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.Slogan;

public class BeautyForm
    extends Form<Void>
{
    private static final long serialVersionUID = -4506870702347552736L;

    public BeautyForm(final String id)
    {
        super(id);
        this.add(new Button("beauty-submit"));
    }

    @Override
    public void onSubmit()
    {
        final BeautyAdapter adapter = new BeautyAdapter();

        final HashMap<String, Object> parameters = new HashMap<String, Object>();

        final List<Slogan> slogans = adapter.generateSlogans(parameters);
        this.setResponsePage(new HomePage(slogans));
    }
}
