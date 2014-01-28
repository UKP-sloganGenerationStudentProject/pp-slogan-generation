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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 * Eclipse generated
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pass == null) ? 0 : pass.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 * eclipse generated
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DbConfig other = (DbConfig) obj;
		if (pass == null) {
			if (other.pass != null)
				return false;
		} else if (!pass.equals(other.pass))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
	

}
