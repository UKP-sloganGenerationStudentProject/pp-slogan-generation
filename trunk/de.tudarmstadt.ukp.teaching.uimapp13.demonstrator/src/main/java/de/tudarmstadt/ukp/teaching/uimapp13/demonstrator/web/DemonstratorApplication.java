package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.configuration.DemonstratorConfig;

public class DemonstratorApplication
    extends WebApplication
{
    public DemonstratorApplication()
    {
        if (System.getenv(DemonstratorConfig.RESOURCES_ENVVAR) == null) {
            throw new IllegalStateException("Environment variable not set: "
                    + DemonstratorConfig.RESOURCES_ENVVAR);
        }
    }

    @Override
    public Class<? extends Page> getHomePage()
    {
        return HomePage.class;
    }

}
