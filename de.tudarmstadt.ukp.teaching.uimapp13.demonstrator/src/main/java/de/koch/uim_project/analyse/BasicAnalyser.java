package de.koch.uim_project.analyse;

import org.apache.log4j.Logger;
import org.apache.uima.analysis_component.AnalysisComponent;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

public class BasicAnalyser {

	private static final Logger log = Logger.getRootLogger();

	/**
	 * Convienience method
	 * {@link BasicAnalyser#simpleAnalyse(JCas, Class, boolean)}
	 * 
	 * @param jcas
	 * @param cl
	 * @return
	 * @throws AnalyseException
	 */
	public static JCas simpleAnalyse(JCas jcas, Class<? extends AnalysisComponent> cl) throws AnalyseException {
		return simpleAnalyse(jcas, cl, true);
	}

	/**
	 * Analysis with the given @see AnalysisComponent Modifies the given @see
	 * JCas
	 * 
	 * @param jcas
	 *            JCas to analyse, is modified
	 * @param cl
	 * @see Analysis Component to use
	 * @return @see JCas with new annotations
	 * @throws AnalyseException
	 */
	public static JCas simpleAnalyse(JCas jcas, Class<? extends AnalysisComponent> cl, boolean hardFailure) throws AnalyseException {
		try {
			AnalysisEngineDescription pipeline = AnalysisEngineFactory.createEngineDescription(AnalysisEngineFactory.createEngineDescription(cl));
			SimplePipeline.runPipeline(jcas, pipeline);
		} catch (ResourceInitializationException e) {
			log.error("Failed to initialize AnalsyisEngineDescription", e);
			if (hardFailure)
				throw new AnalyseException();
		} catch (AnalysisEngineProcessException e) {
			log.error("Failed to run pipeline", e);
			if (hardFailure)
				throw new AnalyseException();
		}

		return jcas;
	}
}
