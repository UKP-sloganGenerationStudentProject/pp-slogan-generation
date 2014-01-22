package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.configuration;

public final class DemonstratorConfig
{
    private DemonstratorConfig()
    {

    }

    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    public static final String UBY_URL = "localhost/uby_medium_0_3_0";
    public static final String UBY_PASSWORD = "uby";
    public static final String UBY_USER = "uby";
    public static final String UBY_URL_JDBC = "jdbc:mysql://127.0.0.1/uby_medium_0_3_0";

    public static final String CUSTOM_DB_USER = "uimpp";
    public static final String CUSTOM_DB_PASSWORD = "uimpp";

    public static final String KOCH_DB_URL = "jdbc:mysql://localhost:3306/uimpp_koch";
}
