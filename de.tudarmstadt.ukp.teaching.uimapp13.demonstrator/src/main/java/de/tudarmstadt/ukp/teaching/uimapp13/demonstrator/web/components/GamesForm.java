package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;

public class GamesForm
    extends Form<Void>
{
    private static final long serialVersionUID = -4876200976182929042L;

    public GamesForm(final String id)
    {
        super(id);
        this.add(new DropDownChoice<String>("games-emotion"));
    }
}
