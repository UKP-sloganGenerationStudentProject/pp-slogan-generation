package de.koch.uim_project.database;


import org.apache.log4j.Logger;


import de.koch.uim_project.util.Constants;
import de.koch.uim_project.util.DbConfig;
import de.tudarmstadt.ukp.lmf.api.Uby;
import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;

/**
 * This class handles uby accesses. One global uby instance is held
 * 
 * @author Frerik Koch
 * 
 */
public class UbyConnect {

	private static Uby uby;
	private static Logger log = Logger.getRootLogger();

	/**
	 * Initializes and returns the uby instance
	 * 
	 * @return Global uby instance
	 * @throws DbException
	 */
	public static Uby getUbyInstance(DbConfig config) throws DbException {

		if (uby == null) {
			try {
				de.tudarmstadt.ukp.lmf.transform.DBConfig db = new de.tudarmstadt.ukp.lmf.transform.DBConfig(config.getUrl(),
						Constants.DATABASE.JDBC_DRIVER, Constants.DATABASE.DB_TYPE, config.getUser(), config.getPass(), false);

				uby = new Uby(db);

			} catch (UbyInvalidArgumentException e) {
				log.error("Failed to establish uby connection", e);
				throw new DbException("Failed to establish uby connection");
			}
		}
		return uby;
	}

}
