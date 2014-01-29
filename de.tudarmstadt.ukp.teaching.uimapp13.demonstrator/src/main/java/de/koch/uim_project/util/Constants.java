package de.koch.uim_project.util;

import com.googlecode.jweb1t.JWeb1TSearcher;

import de.koch.uim_project.generation.Generator;

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
	 * 
	 * @author Frerik Koch
	 * 
	 */
	public static final class FILESYSTEM {

		/**
		 * Filesystem path to web1t corpus. If the {@link Generator} gets a path
		 * or a {@link JWeb1TSearcher} directly as argument this constant is not
		 * used.
		 */
		public static final String WEB1T_DEFAULT_PATH = "/home/finwe8/web1t";
	}

	/**
	 * Database related constants
	 * 
	 * @author Frerik Koch
	 * 
	 */
	public static final class DATABASE {

		/**
		 * Driver for JDBC connection. e.g. "com.mysql.jdbc.Driver". Corresponds
		 * to the custom database.
		 */
		public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		/**
		 * Database type for JDBC connection. e.g. "mysql". Corresponds to the
		 * custom database.
		 */
		public static final String DB_TYPE = "mysql";

		/**
		 * Uby related constants
		 * 
		 * @author Frerik Koch
		 * 
		 */
		public static final class UBY {
			/**
			 * Url for the uby database. Must be a valid Uby url parameter. E.g.
			 * "localhost/uby_open_0_3_0"
			 */
			public static final String DEFAULT_UBY_URL = "localhost/uby_open_0_3_0";
			/**
			 * Password for uby database
			 */
			public static final String DEFAULT_UBY_PASS = "root";
			/**
			 * Login for uby database
			 */
			public static final String DEFAULT_UBY_LOGIN = "root";
		}

		/**
		 * Custom database related constants
		 * 
		 * @author Frerik Koch
		 * 
		 */
		public static final class CUSTOM_DATABASE {

			/**
			 * Custom database login
			 */
			public static final String DEFAULT_DB_USER = "root";
			/**
			 * Custom database password
			 */
			public static final String DEFAULT_DB_PASS = "root";
			/**
			 * JDBC url for the custom database. Has to be a valid jdbc url.
			 * E.g. "jdbc:mysql://localhost:3306/uim_projekt"
			 */
			public static final String DEFAULT_DB_URL = "jdbc:mysql://localhost:3306/uim_projekt";

		}
	}

}
