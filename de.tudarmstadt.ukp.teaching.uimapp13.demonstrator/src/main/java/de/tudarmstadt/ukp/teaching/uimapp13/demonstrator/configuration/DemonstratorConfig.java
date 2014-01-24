package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class DemonstratorConfig
{

    public static final String RESOURCES_ENVVAR = "DEMO_RES";

    private static DemonstratorConfig instance;
    private static boolean cacheConfiguration = false;

    private DemonstratorConfig()
    {
        // singleton class
        this.properties = new Properties();
    }

    public static DemonstratorConfig getInstance()
    {
        if (!cacheConfiguration || null == instance) {
            instance = new DemonstratorConfig();
            try {
                instance.loadConfig();
            }
            catch (final IOException e) {
                throw new IllegalStateException("Could not load configuration", e);
            }
        }
        return instance;
    }

    private void loadConfig()
        throws IOException
    {
        this.properties.load(new FileInputStream(getResourcePath() + "/config.properties"));
    }

    public static String getResourcePath()
    {
        return System.getenv(RESOURCES_ENVVAR);
    }

    private Properties properties;

    public String getJdbcDriver()
    {
        return this.properties.getProperty("jdbc.driver");

    }

    public String getJdbcDriverName()
    {
        return this.properties.getProperty("jdbc.driver.name");
    }

    public String getUbyUrl()
    {
        return this.properties.getProperty("uby.url");
    }

    public String getUbyPassword()
    {
        return this.properties.getProperty("uby.password");
    }

    public String getUbyUser()
    {
        return this.properties.getProperty("uby.user");
    }

    public String getUbyUrlJdbc()
    {
        return this.properties.getProperty("uby.url.jdbc");
    }

    public String getCustomDbUser()
    {
        return this.properties.getProperty("custom.db.user");
    }

    public String getCustomDbPassword()
    {
        return this.properties.getProperty("custom.db.password");
    }

    public String getKochDbUrl()
    {
        return this.properties.getProperty("koch.db.url");
    }

}
