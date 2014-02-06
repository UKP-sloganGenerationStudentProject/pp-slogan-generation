package de.koch.uim_project.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import de.koch.uim_project.generation.Word;
import de.koch.uim_project.util.Constants;
import de.koch.uim_project.util.DbConfig;
import de.koch.uim_project.util.Emotion;

/**
 * This class handles the connection to the custom database. 
 * Each instance has its own connection which is hold for the life time of the instance for performance reasons
 * 
 * @author Frerik Koch
 * 
 */
public class JdbcConnect
{

    private static Logger log = Logger.getRootLogger();

    private static final int MAX_CONDITIONS_QUERY = 50;
    private String dbUser, dbPass, dbUrl;

    /**
     * Constructor for creating a new {@link JdbcConnect}
     * ATTENTION: the url in the given {@link DbConfig} has to be a valid jdbc url see {@link Constants.DATABASE.CUSTOM_DATABASE} for examples
     * @param customDbConfig
     * @throws DbException
     */
    public JdbcConnect(final DbConfig customDbConfig)
        throws DbException
    {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            this.dbUser = customDbConfig.getUser();
            this.dbPass = customDbConfig.getPass();
            this.dbUrl = customDbConfig.getUrl();
            this.con = DriverManager.getConnection(this.dbUrl, this.dbUser, this.dbPass);
        }
        catch (final Exception e) {
            log.error("Failed to initialize JdbcConnect", e);
            throw new DbException("Failed to initialize JdbcConnect", e);
        }
    }

    /**
     * Database Connection for this {@link JdbcConnect} instantiation. Out of
     * performance reasons one database connection is held
     */
    private Connection con;

    /**
     * Getter for database connection of {@link JdbcConnect} Initializes
     * {@link JdbcConnect#con} if necessary ATTENTION: This connection may not
     * be closed manually but only through {@link JdbcConnect#closeConnection()}
     * 
     * @return Global database connection
     * @throws DbException
     */
    public Connection getConnection()
        throws DbException
    {
        if (this.con == null) {
            try {
                this.con = DriverManager.getConnection(this.dbUrl, this.dbUser, this.dbPass);
            }
            catch (final SQLException e) {
                log.error("Failed to initialize database connection", e);
                throw new DbException("Failed to initialize database connection", e);
            }
        }
        return this.con;
    }

    /**
     * Closes the global database connection. This method should only be called
     * if a re-initialization of the connection is needed for some reason or the
     * program is about to finish
     */
    public void closeConnection()
    {
        if (this.con != null) {
            try {
                this.con.close();
            }
            catch (final SQLException e) {
                log.warn("Failed to close connection.", e);
            }
            this.con = null;
        }
    }

    /**
     * Getter for slogans from custom database
     * 
     * @return All slogans from custom database
     * @throws DbException
     */
    public ArrayList<String> getSlogans()
        throws DbException
    {
        final ArrayList<String> slogans = new ArrayList<String>();

        try {
            final PreparedStatement pstmt = this.con.prepareStatement("SELECT slogan FROM slogans");
            final ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                slogans.add(rs.getString(1));
            }

        }
        catch (final SQLException e) {
            log.warn("Failed to retrieve slogans from DB.", e);
            throw new DbException("Failed to retrieve slogans from DB.", e);
        }
        return slogans;
    }

    /**
     * Getter for all slogans in one {@link String}. Separated through line
     * separation.
     * 
     * @return All slogans from custom databse line separeted
     * @throws DbException
     */
    public String getAllSlogansLineSeperated()
        throws DbException
    {
        final ArrayList<String> slogans = this.getSlogans();
        final StringBuilder sb = new StringBuilder();
        for (final String str : slogans) {
            sb.append(str);
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    /**
     * Getter for function words from database
     * 
     * @return All function words from custom database
     * @throws DbException
     */
    public Set<String> getFunctionWordSet()
        throws DbException
    {
        return this.getSimpleTable("functionword", "word");
    }

    /**
     * Getter for stop words from database
     * 
     * @return All stop words from custom database
     * @throws DbException
     */
    public Set<String> getStopWordSet()
        throws DbException
    {
        return this.getSimpleTable("stopword", "word");
    }

    /**
     * Helper method: Returns all values from a given table and a given column
     * 
     * @param tableName
     *            Database table to extract values from
     * @param coulmnName
     *            Column to extract values from
     * @return Extracted values
     * @throws DbException
     */
    private Set<String> getSimpleTable(final String tableName, final String coulmnName)
        throws DbException
    {

        final Set<String> result = new HashSet<String>();

        try {

            final PreparedStatement pstmt = this.con.prepareStatement("SELECT " + coulmnName
                    + " FROM " + tableName + ";");
            final ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.add(rs.getString(1));
            }

        }
        catch (final SQLException e) {
            log.warn("Failed to retrieve functionwords from DB.", e);
            throw new DbException("Failed to retrieve functionwords from DB.", e);
        }

        return result;
    }

    /**
     * This method retrieves emotions for a set of words. It tries to minimize
     * DB access by batch extracting emotions for multiple words in one query.
     * 
     * @param words
     *            Words to retrieve emotions for
     * @return
     * @throws DbException
     */
    public Set<Word> getEmotionBatch(final Set<Word> words)
        throws DbException
    {
        Map<String, Set<Emotion>> result = new HashMap<String, Set<Emotion>>();
        Set<Word> tmp = new HashSet<Word>();
        if (words.size() > MAX_CONDITIONS_QUERY) {
            for (final Word word : words) {
                tmp.add(word);
                if (tmp.size() >= MAX_CONDITIONS_QUERY) {
                    result = this.getEmotionBatchHelper(tmp);
                    tmp = new HashSet<Word>();
                }
            }
        }
        else {
            result = this.getEmotionBatchHelper(words);
        }

        for (final Word word : words) {
            word.setEmotionChecked(true);
            if (result.containsKey(word.getLemma())) {
                if (result.get(word.getLemma()) != null) {
                    word.setEmotions(result.get(word.getLemma()));

                }
            }
        }

        return words;
    }

    /**
     * Helper method for {@link JdbcConnect#getEmotionBatch(Set)} This method
     * builds a query to retrieve emotions for multiple words and executes it
     * 
     * @param words
     *            Words to retrieve emotions for
     * @return
     * @throws DbException
     */
    private Map<String, Set<Emotion>> getEmotionBatchHelper(final Set<Word> words)
        throws DbException
    {
        try {
            final StringBuilder query = this.buildSqlString(words);
            final PreparedStatement pstmt = this.con.prepareStatement(query.toString());

            // fill parameters
            int i = 0;
            for (final Word word : words) {
                i++;
                pstmt.setString(i, word.getLemma());
            }

            // fill emotion map from query result
            final Map<String, Set<Emotion>> result = new HashMap<String, Set<Emotion>>();
            final ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                if (!result.containsKey(rs.getString("word"))) {
                    result.put(rs.getString("word"), new HashSet<Emotion>());
                }
                result.get(rs.getString("word")).add(Emotion.fromId(rs.getInt("idemotion")));
            }
            return result;

        }
        catch (final Exception e) {
            log.error("Failed to retrieve emotions", e);
            throw new DbException("Failed to retrieve emotions", e);
        }

    }

    /**
     * Helper method for {@link JdbcConnect#getEmotionBatch(Set)} This method
     * builds the actual SQL query from a {@link Collection} of words
     * 
     * @param words
     *            Words to retrieve emotions for
     * @return
     */
    private StringBuilder buildSqlString(final Collection<Word> words)
    {
        final StringBuilder query = new StringBuilder();
        final StringBuilder whereClause = new StringBuilder();
        query.append("SELECT word, emotion.idemotion FROM emotionword JOIN emotiontoemotionword ON emotiontoemotionword.idemotionword = emotionword.idemotionword JOIN emotion ON emotion.idemotion = emotiontoemotionword.idemotion ");
        whereClause.append("WHERE ");

        for (int i = 0; i < words.size(); i++) {
            if (i != 0) {
                whereClause.append(" OR ");
            }
            whereClause.append("word=?");
        }

        query.append(whereClause);
        return query;
    }
}
