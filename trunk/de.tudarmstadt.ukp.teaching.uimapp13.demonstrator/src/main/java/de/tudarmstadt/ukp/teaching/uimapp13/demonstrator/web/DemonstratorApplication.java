package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

public class DemonstratorApplication
    extends WebApplication
{

    @Override
    public Class<? extends Page> getHomePage()
    {
        return HomePage.class;
    }

}
