package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import de.koch.uim_project.util.DbConfig;

public final class DemonstratorConfig
{

    /**
     * This resource path should work for development setup.
     * When running on a server, an absolute value for this 
     * environment variable is necessary.
     */
    public static final String RESOURCES_ENVVAR = "DEMO_RES";

    private static final String DEFAULT_RESOURCES_ENVVAR = "./src/main/resources";

    private static DemonstratorConfig instance;
    private static boolean isCachingProperties = false;

    private Properties properties;

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
        this.properties.load(new FileInputStream(getConfigFile()));
    }

    public static String getResourcePath()
    {
        final String resourcePath;
        if (null == System.getenv(RESOURCES_ENVVAR)) {
            resourcePath = DEFAULT_RESOURCES_ENVVAR;
            // throw new IllegalStateException("Need to set environment variable " +
            // RESOURCES_ENVVAR
            // + " to the absolute path of the resources directory.");
        }
        else {
            resourcePath = System.getenv(RESOURCES_ENVVAR);
        }
        return resourcePath;
    }

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

    public String getKochDbUser()
    {
        return this.getPropertyChecked("custom.db.user");
    }

    public String getKochDbPassword()
    {
        return this.getPropertyChecked("custom.db.password");
    }

    public String getKochDbUrl()
    {
        return this.getPropertyChecked("koch.db.url");
    }

    public String getWeb1TPath()
    {
        final String path = this.getPropertyChecked("web1t.pathname");
        if (!new File(path).isDirectory()) {
            throw new IllegalStateException("Web1T is not at the expected location: " + path);
        }
        return path;
    }

    public String getEmotionPath()
    {
        final String path = getResourcePath() + "/NRCemotionlexicon.pdf";
        if (!new File(path).isFile()) {
            throw new IllegalStateException("NRC emotion lexicon is not at the expected location: "
                    + path);
        }
        return path;
    }

    public String getPropertyChecked(final String property)
    {
        final String value = this.properties.getProperty(property);
        if (null == value) {
            throw new IllegalStateException("Required property " + property + " is missing!");
        }
        return value;
    }

    public String getSerializedEmotionssPath()
    {
        final String path = getResourcePath() + "/loeser/Emotionlist.sg";
        if (!new File(path).isFile()) {
            throw new IllegalStateException(
                    "Serialized NRC emotion lexicon is not at the expected location: " + path);
        }
        return path;
    }

    public DbConfig createUbyDbConfig()
    {
        return new DbConfig(this.getUbyUrl(), this.getUbyUser(), this.getUbyPassword());
    }

    public DbConfig createKochsCustomDbConfig()
    {
        return new DbConfig(this.getKochDbUrl(), this.getKochDbUser(), this.getKochDbPassword());
    }

    public static boolean canGenerateInstance()
    {
        return getConfigFile().exists();
    }

    private static File getConfigFile()
    {
        return new File(getResourcePath() + "/config.properties");
    }
}
