package de.koch.uim_project.util;

/**
 * This class gathers constants used by the project. Grouping is done by
 * subclasses.
 * 
 * @author Frerik Koch
 * 
 */
public class Constants {

	private Constants() {
	}
	
	/**
	 * Filesystem related constants.
	 * @author Frerik Koch
	 *
	 */
	public static final class FILESYSTEM{
		
		public static final String WEB1T_DEFAULT_PATH ="/home/finwe8/web1t" ;
	}

	/**
	 * Database related constants
	 * 
	 * @author Frerik Koch
	 * 
	 */
	public static final class DATABASE {

		public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		public static final String DB_TYPE = "mysql";

		/**
		 * Uby related constants
		 * 
		 * @author Frerik Koch
		 * 
		 */
		public static final class UBY {
			public static final String DEFAULT_UBY_URL = "localhost/uby_open_0_3_0";
			public static final String DEFAULT_UBY_PASS = "root";
			public static final String DEFAULT_UBY_LOGIN = "root";
			public static final String DEFAULT_UBY_URL_JDBC = "jdbc:mysql://localhost:3306/uby_open_0_3_0";
		}

		/**
		 * Custom database related constants
		 * 
		 * @author Frerik Koch
		 * 
		 */
		public static final class CUSTOM_DATABASE {

			public static final String DEFAULT_DB_USER = "root";
			public static final String DEFAULT_DB_PASS = "root";
			public static final String DEFAULT_DB_URL = "jdbc:mysql://localhost:3306/uim_projekt";

		}
	}

}
