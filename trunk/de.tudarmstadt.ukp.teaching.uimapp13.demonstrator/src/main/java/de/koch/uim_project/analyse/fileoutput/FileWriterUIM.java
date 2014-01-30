package de.koch.uim_project.analyse.fileoutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import de.koch.uim_project.analyse.data.FullyAnalyzedSentence;
import de.tudarmstadt.ukp.dkpro.core.api.frequency.util.FrequencyDistribution;

/**
 * This class provides methods to print different Objects to files
 * 
 * @author Frerik Koch
 * 
 */
public class FileWriterUIM
{

    private static FileWriterUIM instance;
    private Logger log = Logger.getRootLogger();

    private FileWriterUIM()
    {
    }

    /**
     * Getter for Instance of {@link FileWriterUIM}
     * 
     * @return Instance of {@link FileWriterUIM}
     */
    public static FileWriterUIM getInstance()
    {
        if (instance == null) {
            instance = new FileWriterUIM();
        }
        return instance;
    }

    public <T> void printFrequencyDistribution(final File file, final FrequencyDistribution<T> fd)
        throws FileWriterUimException
    {
        this.printFrequencyDistribution(file, fd, new DefaultOutputGenerator<T>());
    }

    /**
     * This method prints a
     * {@link de.tudarmstadt.ukp.dkpro.core.api.frequency.util.FrequencyDistribution}
     * sorted in descending order to a given file. For the elements of the
     * {@link de.tudarmstadt.ukp.dkpro.core.api.frequency.util.FrequencyDistribution}
     * the output generator is used to generate an output string
     * 
     * @param <T>
     *            Type the frequencies is build for
     * @param file
     *            File to write to. It is overridden if existent
     * @param fd
     *            {@link de.tudarmstadt.ukp.dkpro.core.api.frequency.util.FrequencyDistribution}
     *            to print
     * @param defaultOutputGenerator
     *            {@link OutputGenerator} to generate Output for given type
     *            {@link OutputGenerator}
     * @throws FileWriterUimException
     */

    public <T> void printFrequencyDistribution(final File file, final FrequencyDistribution<T> fd,
            final OutputGenerator<T> defaultOutputGenerator)
        throws FileWriterUimException
    {
        BufferedWriter bw = null;  
        try {
        	file.createNewFile();
            bw = new BufferedWriter(new FileWriter(file));

            final List<SortPair<T>> sos = new ArrayList<SortPair<T>>();
            final Comparator<SortPair<T>> comp = Collections.reverseOrder();

            for (final T o : fd.getKeys()) {
                sos.add(new SortPair<T>(fd.getCount(o), o));
            }
            Collections.sort(sos, comp);
            for (final SortPair<T> sp : sos) {
                bw.append(defaultOutputGenerator.getOutputString(sp.obj));
                bw.append("\t");
                bw.append(Long.toString(sp.frequency));
                bw.newLine();
            }

            bw.flush();
            bw.close();

        }
        catch (final Exception e) {
            if (bw != null) {
                try {
                    bw.close();
                }
                catch (final IOException e1) {
                    this.log.error("Failed to close FileWriter", e1);
                }
            }
            this.log.error("Exception while writing frequency Distribution", e);
            throw new FileWriterUimException("Exception while writing frequency Distribution", e);
        }
    }

    /**
     * Prints {@link FullyAnalyzedSentence}s to a given file
     * 
     * @param fslogans
     *            {@link FullyAnalyzedSentence}s to print to file
     * @param file
     *            File to print to
     * @throws FileWriterUimException
     */
    public void printFullyAnalyzedSlogans(final List<FullyAnalyzedSentence> fslogans,
            final File file)
        throws FileWriterUimException
    {
        BufferedWriter bw = null;
        try {
        	file.createNewFile();
            bw = new BufferedWriter(new FileWriter(file));
            final Comparator<FullyAnalyzedSentence> comp = Collections.reverseOrder();
            Collections.sort(fslogans, comp);
            int actualFrequenzy = fslogans.get(0).getChunkedSentenceFrequenz() + 1;
            for (final FullyAnalyzedSentence fas : fslogans) {
                if (fas.getChunkedSentenceFrequenz() < actualFrequenzy) {
                    actualFrequenzy = fas.getChunkedSentenceFrequenz();
                    bw.newLine();
                    bw.newLine();
                    bw.append("############## With Chunk Frequenz of " + actualFrequenzy
                            + "##############");
                    bw.newLine();
                }
                bw.newLine();
                bw.append("------------- Slogan: " + fas.getOriginalSlogan() + " -------------");
                bw.newLine();
                bw.append("Chunked Sentence: " + fas.getChunkedSentence());
                bw.newLine();
                bw.append("Pos Sentence: " + fas.getPosSentence());
                bw.newLine();
                bw.append("------------- ------------- ");
                bw.newLine();
            }
            bw.flush();
        }
        catch (final Exception e) {
            this.log.error("Exception while writing fully analyzed slogans", e);
            throw new FileWriterUimException("Exception while writing fully analyzed slogans", e);
        }
        finally {
            if (bw != null) {
                try {
                    bw.close();
                }
                catch (final IOException e1) {
                    this.log.error("Failed to close FileWriter", e1);
                }
            }
        }

    }

    /**
     * Helper data class to sort {@link Object}s by their frequency
     * 
     * @author Frerik Koch
     * 
     * @param <T>
     *            Type to sort
     */
    private class SortPair<T>
        implements Comparable<SortPair<T>>
    {

        public final Long frequency;
        public final T obj;

        public SortPair(final Long frequency, final T obj)
        {
            this.frequency = frequency;
            this.obj = obj;
        }

        @Override
        public int compareTo(final SortPair<T> o)
        {
            return this.frequency.compareTo(o.frequency);
        }

    }

}
