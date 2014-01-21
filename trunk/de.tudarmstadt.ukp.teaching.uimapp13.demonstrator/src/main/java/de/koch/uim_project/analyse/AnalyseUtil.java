package de.koch.uim_project.analyse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.koch.uim_project.analyse.data.ChunkedSentence;
import de.koch.uim_project.analyse.data.PosSentence;
import de.koch.uim_project.database.DbException;
import de.koch.uim_project.database.JdbcConnect;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.Chunk;

/**
 * Utility class for {@link analyse}
 * 
 * @author Frerik Koch
 * 
 */
public class AnalyseUtil {

	private AnalyseUtil() {

	}

	/**
	 * Converts a given {@link JCas} to a {@link ChunkedSentence} The
	 * {@link JCas} has to be chunked tagged
	 * 
	 * @param jcas
	 *            {@link JCas} to convert
	 * @param ignorePunctuationMark
	 *            If true punctuation mark chunks are ignored
	 * @return Resulting {@link ChunkedSentence}
	 */
	public static ChunkedSentence getChunkedSentence(JCas jcas, boolean ignorePunctuationMark) {
		List<Chunk> chunks = new ArrayList<Chunk>(JCasUtil.select(jcas, Chunk.class));
		if (ignorePunctuationMark) {
			List<Chunk> chunksToRemove = new ArrayList<Chunk>();
			for (Chunk chunk : chunks) {
				if (chunk.getChunkValue().equals("0") || chunk.getChunkValue().equals("O")) {
					chunksToRemove.add(chunk);
				}
			}
			chunks.removeAll(chunksToRemove);
		}
		return new ChunkedSentence(chunks);
	}

	/**
	 * Converts a {@link JCas} to a {@link PosSentence}. The {@link JCas} has to
	 * be POS tagged.
	 * 
	 * @param jcas
	 *            {@link JCas} to convert
	 * @param omitStopWordTagging
	 *            If true stop words are not replaced by their POS tag but left
	 *            standing
	 * @return Resulting {@link PosSentence}
	 * @throws DbException
	 */
	public static PosSentence getPosSentence(JCas jcas, boolean omitStopWordTagging, JdbcConnect con) throws DbException {
		Set<String> functionwords = new HashSet<String>();
		if (omitStopWordTagging) {
			functionwords = con.getFunctionWordSet();

		}

		List<String> posValues = new ArrayList<String>();
		for (Token token : JCasUtil.select(jcas, Token.class)) {
			if (functionwords.contains(token.getCoveredText())) {
				posValues.add(token.getCoveredText());
			} else {
				posValues.add(token.getPos().getPosValue());
			}

		}
		
		return new PosSentence(posValues);
	}
}
