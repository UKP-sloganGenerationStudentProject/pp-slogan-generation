package de.koch.uim_project.analyse;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;

import de.koch.uim_project.analyse.data.ChunkedSentence;
import de.koch.uim_project.analyse.data.FullyAnalyzedSentence;
import de.koch.uim_project.analyse.data.PosSentence;
import de.koch.uim_project.database.DbException;
import de.koch.uim_project.database.JdbcConnect;
import de.tudarmstadt.ukp.dkpro.core.api.frequency.util.FrequencyDistribution;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerChunkerTT4J;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerPosLemmaTT4J;

/**
 * This class packages pattern analysis
 * 
 * @author Frerik Koch
 * 
 */
public class PatternAnalyser {

	private static PatternAnalyser instance = null;
	private Logger log = Logger.getRootLogger();

	private PatternAnalyser() {

	}

	/**
	 * Getter for instance of {@link PatternAnalyser}
	 * 
	 * @return instance of {@link PatternAnalyser}
	 */
	public static PatternAnalyser getInstance() {
		if (instance == null) {
			instance = new PatternAnalyser();
		}
		return instance;
	}

	/**
	 * Fully analyzes all given slogans with a given chunk sentence frequency
	 * 
	 * @param slogans
	 *            Slogans to analyze
	 * @param minFrequenz
	 *            Minimal chunk sentence frequency. This minimum excludes all
	 *            slogans from analysis which chunk pattern is not found often
	 *            enough in the given corpus
	 * @param omitFunctionWordTagging
	 *            If true stop words will not be replaced by their POS tag
	 *            during POS analysis
	 * @return Fully analyzed sentences which have a higher chunk pattern
	 *         frequency then minFrequenz
	 * @throws AnalyseException
	 * @throws DbException
	 */
	public List<FullyAnalyzedSentence> getFullyAnalyzeForFrequenz(List<String> slogans, int minFrequenz, boolean omitFunctionWordTagging,JdbcConnect con)
			throws AnalyseException, DbException {
		List<FullyAnalyzedSentence> result = new ArrayList<FullyAnalyzedSentence>();
		FrequencyDistribution<ChunkedSentence> fdC = FrequencyAnalyser.getInstance().getChunkPatternFrequency(slogans, true);

		try {
			AnalysisEngineDescription pipeline = AnalysisEngineFactory.createEngineDescription(
					AnalysisEngineFactory.createEngineDescription(StanfordSegmenter.class),
					AnalysisEngineFactory.createEngineDescription(TreeTaggerPosLemmaTT4J.class),
					AnalysisEngineFactory.createEngineDescription(TreeTaggerChunkerTT4J.class));
			JCas jcas;
			for (String str : slogans) {
				jcas = JCasFactory.createJCas();
				jcas.setDocumentLanguage("en");
				jcas.setDocumentText(str);
				SimplePipeline.runPipeline(jcas, pipeline);
				ChunkedSentence cs = AnalyseUtil.getChunkedSentence(jcas, true);
				PosSentence ps = AnalyseUtil.getPosSentence(jcas, omitFunctionWordTagging,con);
				FullyAnalyzedSentence fas = new FullyAnalyzedSentence(cs, (int) fdC.getCount(cs), ps, str);
				if (fas.getChunkedSentenceFrequenz() >= minFrequenz) {
					result.add(fas);
				}
			}

		} catch (Exception e) {
			log.error("Failed to fully analyze slogan", e);
			throw new AnalyseException("Failed to fully analyze slogan", e);
		}

		return result;
	}
	
	

}
