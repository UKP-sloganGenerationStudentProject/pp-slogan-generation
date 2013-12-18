package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

public class HomePage
    extends WebPage
{
    private static final long serialVersionUID = -6704450114690839672L;

    public HomePage()
    {
        add(new Label("message", "Hello World!"));
    }
}
