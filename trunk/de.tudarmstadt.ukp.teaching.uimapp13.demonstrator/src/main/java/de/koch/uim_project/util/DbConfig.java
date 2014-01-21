package de.koch.uim_project.util;

public class DbConfig {

	private String url;
	private String user;
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
