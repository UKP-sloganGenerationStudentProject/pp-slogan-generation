package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components;

import java.util.HashMap;
import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;

import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.Slogan;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.HomePage;

public class CarsForm
    extends Form<Void>
{
    private static final long serialVersionUID = -7266582482549958376L;

    public CarsForm(final String id)
    {
        super(id);
        this.add(new Button("cars-submit"));
    }

    @Override
    public void onSubmit()
    {
        final CarsAdapter adapter = new CarsAdapter();

        final HashMap<String, Object> parameters = new HashMap<String, Object>();

        final List<Slogan> slogans = adapter.generateSlogans(parameters);
        this.setResponsePage(new HomePage(slogans, ""));
    }

}
