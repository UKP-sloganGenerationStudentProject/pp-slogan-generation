package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class DemonstratorConfig
{

    public static final String RESOURCES_ENVVAR = "DEMO_RES";

    private static DemonstratorConfig instance;
    private static boolean isCachingProperties = false;

    private DemonstratorConfig()
    {
        // singleton class
        this.properties = new Properties();
    }

    public static DemonstratorConfig getInstance()
    {
        if (!isCachingProperties || null == instance) {
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
        return this.getPropertyChecked("jdbc.driver");

    }

    public String getJdbcDriverName()
    {
        return this.getPropertyChecked("jdbc.driver.name");
    }

    public String getUbyUrl()
    {
        return this.getPropertyChecked("uby.url");
    }

    public String getUbyPassword()
    {
        return this.getPropertyChecked("uby.password");
    }

    public String getUbyUser()
    {
        return this.getPropertyChecked("uby.user");
    }

    public String getUbyUrlJdbc()
    {
        return this.getPropertyChecked("uby.url.jdbc");
    }

    public String getCustomDbUser()
    {
        return this.getPropertyChecked("custom.db.user");
    }

    public String getCustomDbPassword()
    {
        return this.getPropertyChecked("custom.db.password");
    }

    public String getKochDbUrl()
    {
        return this.getPropertyChecked("koch.db.url");
    }

    public String getWeb1TPathname()
    {
        return this.getPropertyChecked("web1t.pathname");
    }

    public String getEmotionPath()
    {
        return getResourcePath() + "/NRCemotionLexicon.pdf";
    }

    public String getPropertyChecked(final String property)
    {
        final String value = this.properties.getProperty(property);
        if (null == value) {
            throw new IllegalStateException("Required property " + property + " is missing!");
        }
        return value;
    }

}
