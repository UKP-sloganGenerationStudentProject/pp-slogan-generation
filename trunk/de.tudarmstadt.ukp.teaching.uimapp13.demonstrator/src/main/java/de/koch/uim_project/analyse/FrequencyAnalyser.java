package de.koch.uim_project.analyse;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.koch.uim_project.analyse.data.ChunkedSentence;
import de.koch.uim_project.analyse.data.PosSentence;
import de.koch.uim_project.database.JdbcConnect;
import de.tudarmstadt.ukp.dkpro.core.api.frequency.util.FrequencyDistribution;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import de.tudarmstadt.ukp.dkpro.core.stopwordremover.StopWordRemover;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerChunkerTT4J;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerPosLemmaTT4J;
import de.tudarmstadt.ukp.lmf.api.Uby;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;
import de.tudarmstadt.ukp.lmf.model.semantics.Synset;
import de.tudarmstadt.ukp.uby.resource.UbyResourceUtils;

/**
 * This class packages frequency analysis
 * 
 * @author Frerik Koch
 * 
 */
public class FrequencyAnalyser {

	private static final Logger log = Logger.getRootLogger();
	private static FrequencyAnalyser instance;

	private FrequencyAnalyser() {

	}

	/**
	 * Returns the instance of {@link FrequencyAnalyser}
	 * 
	 * @return Instance of {@link FrequencyAnalyser}
	 */
	public static FrequencyAnalyser getInstance() {
		if (instance == null) {
			instance = new FrequencyAnalyser();
		}
		return instance;
	}

	/**
	 * Creates a {@link FrequencyDistribution} over all {@link Synset} found in
	 * the given text
	 * 
	 * @param text
	 *            Text to analyze
	 * @return {@link FrequencyDistribution} over {@link Synset}s in given text
	 * @throws AnalyseException
	 */
	public FrequencyDistribution<Synset> getSynsetFrequency(String text,Uby uby) throws AnalyseException {
		FrequencyDistribution<Synset> fd = new FrequencyDistribution<Synset>();
		try {
			Set<String> stopWordFiles = new HashSet<String>();
			stopWordFiles.add("src/main/resources/sources/stopwordlist.txt");
			JCas jcas;
			AnalysisEngineDescription pipeline = AnalysisEngineFactory.createEngineDescription(AnalysisEngineFactory
					.createEngineDescription(StanfordSegmenter.class), AnalysisEngineFactory.createEngineDescription(TreeTaggerPosLemmaTT4J.class),
					AnalysisEngineFactory.createEngineDescription(StopWordRemover.class, StopWordRemover.PARAM_STOP_WORD_LIST_FILE_NAMES,
							stopWordFiles));

			jcas = JCasFactory.createJCas();
			jcas.setDocumentLanguage("en");
			jcas.setDocumentText(text);
			SimplePipeline.runPipeline(jcas, pipeline);

			for (Token token : JCasUtil.select(jcas, Token.class)) {
				EPartOfSpeech eposes[] = UbyResourceUtils.corePosToUbyPos(token.getPos().getPosValue());
				for (EPartOfSpeech epos : eposes) {
					List<Synset> synsets = UbyAnalyser.getInstance().getSynset(token.getLemma().getValue(), epos,uby);
					for (Synset synset : synsets) {
						fd.inc(synset);
					}
				}
			}

		} catch (Exception e) {
			log.error("Failed to analyse synset frequency");
			throw new AnalyseException("Failed to analyse synset frequency");
		}
		return fd;
	}

	/**
	 * Creates a {@link FrequencyDistribution} over all Lemma in the given text
	 * 
	 * @param text
	 *            Text to analyze
	 * @return {@link FrequencyDistribution} over all {@link Lemma}
	 * @throws AnalyseException
	 */
	public FrequencyDistribution<String> getLemmaFrequency(String text) throws AnalyseException {
		FrequencyDistribution<String> fd = new FrequencyDistribution<String>();
		try {
			Set<String> stopWordFiles = new HashSet<String>();
			stopWordFiles.add("src/main/resources/sources/stopwordlist.txt");
			JCas jcas;
			AnalysisEngineDescription pipeline = AnalysisEngineFactory.createEngineDescription(AnalysisEngineFactory
					.createEngineDescription(StanfordSegmenter.class), AnalysisEngineFactory.createEngineDescription(TreeTaggerPosLemmaTT4J.class),
					AnalysisEngineFactory.createEngineDescription(StopWordRemover.class, StopWordRemover.PARAM_STOP_WORD_LIST_FILE_NAMES,
							stopWordFiles));

			jcas = JCasFactory.createJCas();
			jcas.setDocumentLanguage("en");
			jcas.setDocumentText(text);
			SimplePipeline.runPipeline(jcas, pipeline);

			for (Token token : JCasUtil.select(jcas, Token.class)) {
				fd.inc(token.getLemma().getValue());
			}

		} catch (Exception e) {
			log.error("Failed to analyse Frequency", e);
			throw new AnalyseException("Failed to analyse Frequency");
		}
		return fd;
	}

	/**
	 * Creates a {@link FrequencyDistribution} over all POS patterns in the
	 * given slogans. For each slogan the POS pattern is generated and added to
	 * the {@link FrequencyDistribution}
	 * 
	 * @param slogans
	 *            Slogans to analyze
	 * @param omitStopWordTagging
	 *            If true stop words are not replaced with their POS tag but
	 *            left
	 * @return {@link FrequencyDistribution} over POS patterns
	 * @throws AnalyseException
	 */
	public FrequencyDistribution<PosSentence> getPosPatternFrequency(List<String> slogans, boolean omitStopWordTagging,JdbcConnect con) throws AnalyseException {

		FrequencyDistribution<PosSentence> fd = new FrequencyDistribution<PosSentence>();

		try {

			JCas jcas;
			AnalysisEngineDescription pipeline = AnalysisEngineFactory.createEngineDescription(
					AnalysisEngineFactory.createEngineDescription(StanfordSegmenter.class),
					AnalysisEngineFactory.createEngineDescription(TreeTaggerPosLemmaTT4J.class));
			for (String str : slogans) {
				jcas = JCasFactory.createJCas();
				jcas.setDocumentLanguage("en");
				jcas.setDocumentText(str);
				SimplePipeline.runPipeline(jcas, pipeline);

				fd.inc(AnalyseUtil.getPosSentence(jcas, omitStopWordTagging,con));
			}

		} catch (Exception e) {
			log.error("Failed to generate POS Frequency Distribution", e);
			throw new AnalyseException("Failed to generate POS Frequency Distribution", e);
		}
		return fd;

	}

	/**
	 * Creates a {@link FrequencyDistribution} over all chunk patterns for the
	 * given slogans For each slogan the chunk pattern is generated and added to
	 * the {@link FrequencyDistribution}
	 * 
	 * @param slogans
	 *            Slogans to analyze
	 * @param ignorePunctuationMark
	 *            If true punctuation chunks are ignored and not added to chunk
	 *            patterns
	 * @return {@link FrequencyDistribution} over chunk patterns
	 * @throws AnalyseException
	 */
	public FrequencyDistribution<ChunkedSentence> getChunkPatternFrequency(List<String> slogans, boolean ignorePunctuationMark)
			throws AnalyseException {
		FrequencyDistribution<ChunkedSentence> fd = new FrequencyDistribution<ChunkedSentence>();
		try {
			JCas jcas;
			AnalysisEngineDescription pipeline = AnalysisEngineFactory.createEngineDescription(
					AnalysisEngineFactory.createEngineDescription(StanfordSegmenter.class),
					AnalysisEngineFactory.createEngineDescription(TreeTaggerPosLemmaTT4J.class),
					AnalysisEngineFactory.createEngineDescription(TreeTaggerChunkerTT4J.class));

			for (String str : slogans) {

				jcas = JCasFactory.createJCas();
				jcas.setDocumentLanguage("en");
				jcas.setDocumentText(str);
				SimplePipeline.runPipeline(jcas, pipeline);

				fd.inc(AnalyseUtil.getChunkedSentence(jcas, ignorePunctuationMark));

			}
		} catch (UIMAException e) {
			log.error("Failed to analyse Frequency", e);
			throw new AnalyseException("Failed to analyse Frequency");
		}
		return fd;
	}

}
