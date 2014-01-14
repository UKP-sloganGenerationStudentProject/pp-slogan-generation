package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.Slogan;
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
        add(new Label("message", ""));

        add(new ProductDomainForm("productDomainForm"));

        add(new BeautyForm("beauty-form"));
        add(new CarsForm("cars-form"));
        add(new GamesForm("games-form"));
        add(new SoftDrinksForm("soft-form"));

        final List<Slogan> slogans = Arrays.asList(new Slogan("Testslogan"));
        add(new ListView<Slogan>("slogans", slogans)
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
