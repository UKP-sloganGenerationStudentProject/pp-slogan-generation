package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.Slogan;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components.BeautyForm;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components.CarsForm;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components.GamesForm;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components.ProductDomainForm;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components.SoftDrinksForm;

public class HomePage
    extends WebPage
{
    private static final long serialVersionUID = -6704450114690839672L;

    public HomePage()
    {
        this(new ArrayList<Slogan>(), "");
    }

    public HomePage(final List<Slogan> slogans, final String statusMessage)
    {
        this.add(new Label("message", statusMessage));

        this.add(new ProductDomainForm("productDomainForm"));

        this.add(new BeautyForm("beauty-form"));
        this.add(new CarsForm("cars-form"));
        this.add(new GamesForm("games-form"));
        this.add(new SoftDrinksForm("soft-form"));

        this.add(new ListView<Slogan>("slogans", slogans)
        {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<Slogan> item)
            {
                item.add(new Label("sloganText", item.getModel().getObject().getText()));
            }
        });

    }
}
