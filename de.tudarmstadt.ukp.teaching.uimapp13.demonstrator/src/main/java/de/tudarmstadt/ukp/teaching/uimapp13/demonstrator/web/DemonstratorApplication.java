package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.web;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.caching.FilenameWithVersionResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.version.LastModifiedResourceVersion;

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
    protected void init()
    {
        final FilenameWithVersionResourceCachingStrategy strategy = new FilenameWithVersionResourceCachingStrategy(
                new LastModifiedResourceVersion());
        this.getResourceSettings().setCachingStrategy(strategy);
    }

    @Override
    public Class<? extends Page> getHomePage()
    {
        return HomePage.class;
    }

}
