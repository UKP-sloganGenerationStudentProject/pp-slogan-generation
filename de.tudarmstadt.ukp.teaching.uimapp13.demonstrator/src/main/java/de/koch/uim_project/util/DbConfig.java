package de.koch.uim_project.util;

/**
 * Data container for database configuration data ATTENTION: Uby urls are
 * different from JDBC urls for custom database!
 * 
 * @author Frerik Koch
 * 
 */
public class DbConfig {

	/**
	 * Url of the database. Consumated by Uby or by JDBC for custom database
	 * ATTENTION: Uby urls are different from JDBC urls for custom database!
	 */
	private String url;
	/**
	 * Login to log into database with
	 */
	private String user;
	/**
	 * Password for user
	 */
	private String pass;

	public DbConfig(String url, String user, String pass) {
		super();
		this.url = url;
		this.user = user;
		this.pass = pass;
	}

	public String getUrl() {
		return url;
	}

	public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
	}

}
