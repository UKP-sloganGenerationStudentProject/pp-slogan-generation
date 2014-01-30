package de.koch.uim_project.analyse;

import java.io.File;
import java.util.List;

import de.koch.uim_project.util.Constants;
import de.koch.uim_project.util.DbConfig;

import de.koch.uim_project.analyse.data.ChunkedSentence;
import de.koch.uim_project.analyse.data.FullyAnalyzedSentence;
import de.koch.uim_project.analyse.data.PosSentence;
import de.koch.uim_project.analyse.fileoutput.DefaultOutputGenerator;
import de.koch.uim_project.analyse.fileoutput.FileWriterUIM;
import de.koch.uim_project.analyse.fileoutput.FileWriterUimException;
import de.koch.uim_project.analyse.fileoutput.SynsetOutputGenerator;
import de.koch.uim_project.database.DbException;
import de.koch.uim_project.database.JdbcConnect;
import de.koch.uim_project.database.UbyConnect;
import de.tudarmstadt.ukp.dkpro.core.api.frequency.util.FrequencyDistribution;
import de.tudarmstadt.ukp.lmf.api.Uby;
import de.tudarmstadt.ukp.lmf.model.semantics.Synset;

/**
 * This class generates all used analysis files into resources/analysis
 * 
 * @author finwe8
 * 
 */
public class AnalyseMain {

	/**
	 * This method generates all used analysis files into resources/analysis
	 * DbConfig has to be set correctly!
	 * 
	 * @param args
	 *            no Parameters
	 * @throws DbException
	 * @throws AnalyseException
	 * @throws FileWriterUimException
	 */
	public static void main(String[] args) throws DbException, AnalyseException, FileWriterUimException {
		//DbConfig has to be set correctly!
		DbConfig config = new DbConfig(Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_URL, Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_USER, Constants.DATABASE.CUSTOM_DATABASE.DEFAULT_DB_PASS);
		DbConfig ubyConfig = new DbConfig(Constants.DATABASE.UBY.DEFAULT_UBY_URL, Constants.DATABASE.UBY.DEFAULT_UBY_LOGIN, Constants.DATABASE.UBY.DEFAULT_UBY_PASS);
		
		JdbcConnect con = new JdbcConnect(config);
		List<String> slogans = con.getSlogans();
		String allSlogans = con.getAllSlogansLineSeperated();
		Uby uby = UbyConnect.getUbyInstance(ubyConfig);
		
		FrequencyDistribution<ChunkedSentence> fdChunk = FrequencyAnalyser.getInstance().getChunkPatternFrequency(slogans, true);
		FileWriterUIM.getInstance().printFrequencyDistribution(new File(Constants.ANALYSIS.CHUNK_DISTRIBUTION_OUTPUT_PATH), fdChunk);

		FrequencyDistribution<String> fdLemma = FrequencyAnalyser.getInstance().getLemmaFrequency(allSlogans);
		FileWriterUIM.getInstance().printFrequencyDistribution(new File(Constants.ANALYSIS.LEMMA_DISTRIBUTION_OUTPUT_PATH), fdLemma,
				new DefaultOutputGenerator<String>());

		FrequencyDistribution<PosSentence> fdPos = FrequencyAnalyser.getInstance().getPosPatternFrequency(slogans, true,con);
		FileWriterUIM.getInstance().printFrequencyDistribution(new File(Constants.ANALYSIS.POS_DISTRIBUTION_OUTPUT_PATH), fdPos,
				new DefaultOutputGenerator<PosSentence>());

		FrequencyDistribution<Synset> fdSyn = FrequencyAnalyser.getInstance().getSynsetFrequency(allSlogans,uby);
		FileWriterUIM.getInstance().printFrequencyDistribution(new File(Constants.ANALYSIS.SYNSET_DISTRIBUTION_OUTPUT_PATH), fdSyn,
				new SynsetOutputGenerator());

		List<FullyAnalyzedSentence> fullyAnalyse = PatternAnalyser.getInstance().getFullyAnalyzeForFrequenz(slogans, 0, true,con);
		FileWriterUIM.getInstance().printFullyAnalyzedSlogans(fullyAnalyse, new File(Constants.ANALYSIS.FULL_ANALYSIS_OUTPUT_PATH));
		con.closeConnection();
	}
}
