package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.ProductDomain;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.Slogan;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components.BeautyForm;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components.CarsForm;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components.GamesForm;
import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web.components.ProductDomainForm;

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
        final ArrayList<List<Slogan>> previousSlogans = this.addToPreviousSlogans(slogans);

        this.add(new Label("message", statusMessage));

        this.add(new ProductDomainForm("productDomainForm", ProductDomain.BEAUTY));

        this.add(new BeautyForm("beauty-form"));
        this.add(new CarsForm("cars-form"));
        this.add(new GamesForm("games-form"));

        // final WebMarkupContainer resultsContainer = new WebMarkupContainer("results");
        // if (slogans.isEmpty()) {
        // resultsContainer.setVisible(false);
        // }
        // this.add(resultsContainer);

        // slogans.add(new Slogan("Life is Life, na na nanana."));
        // slogans.add(new Slogan("Creative automobile"));
        // slogans.add(new Slogan("Nothing is impossible"));

        this.add(createSlogansList(slogans));

        // <div id="previousResults">
        // <h2>Previous Generations</h2>
        // <ul wicket:id="previousResults">
        // <li><h3>Results</h3>
        // <ul class="slogans">
        // <li>
        // <div class="slogan">
        // <span wicket:id="sloganText"></span>
        // </div>
        // </li>
        // </ul></li>
        // </ul>
        // </div>
        // this.add(new ListView<List<Slogan>>("previousResults", previousSlogans)
        // {
        // private static final long serialVersionUID = 1L;
        //
        // @Override
        // protected void populateItem(final ListItem<List<Slogan>> item)
        // {
        // item.add(new ListView<Slogan>("slogans", item.getModel().getObject())
        // {
        //
        // private static final long serialVersionUID = -8539671776074045601L;
        //
        // @Override
        // protected void populateItem(final ListItem<Slogan> item)
        // {
        // final String text = item.getModel().getObject().getText();
        // item.add(new Label("sloganText", String.format("#%d - %s", item.getIndex(),
        // text)));
        // }
        // });
        // }
        // });

    }

    private ArrayList<List<Slogan>> addToPreviousSlogans(final List<Slogan> slogans)
    {

        @SuppressWarnings("unchecked")
        ArrayList<List<Slogan>> allSlogans = (ArrayList<List<Slogan>>) this.getSession()
                .getAttribute(SessionAttributes.PREVIOUS_SLOGANS);

        if (allSlogans == null) {
            allSlogans = new ArrayList<>();
        }

        final ArrayList<List<Slogan>> previousSlogans = allSlogans;

        if (!slogans.isEmpty()) {
            allSlogans.add(slogans);
        }

        this.getSession().setAttribute(SessionAttributes.PREVIOUS_SLOGANS, allSlogans);

        return previousSlogans;
    }

    private static ListView<Slogan> createSlogansList(final List<Slogan> slogans)
    {
        return new ListView<Slogan>("slogans", slogans)
        {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<Slogan> item)
            {
                final String text = item.getModel().getObject().getText();
                item.add(new Label("sloganText", String.format("#%d - %s", item.getIndex() + 1,
                        text)));
            }
        };
    }
}
