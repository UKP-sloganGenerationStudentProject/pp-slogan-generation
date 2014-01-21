package de.koch.uim_project.analyse.data;

/**
 * Container class for a full analysis of a slogan. Contains the chunk pattern,
 * the POS pattern, the chunk pattern frequency and the original slogan
 * 
 * @author Frerik Koch
 * 
 */
public class FullyAnalyzedSentence implements Comparable<FullyAnalyzedSentence> {

	/**
	 * Chunk pattern for the original slogan
	 */
	private ChunkedSentence chunkedSentence;

	/**
	 * Chunk pattern frequency for the original slogan
	 */
	private int chunkedSentenceFrequenz;

	/**
	 * POS pattern for the original slogan
	 */
	private PosSentence posSentence;

	/**
	 * Original slogan
	 */
	private String originalSlogan;

	public FullyAnalyzedSentence() {

	}

	/**
	 * @param chunkedSentence
	 *            Chunk pattern for the original slogan
	 * @param chunkedSentenceFrequenz
	 *            Chunk pattern frequency for the original slogan
	 * @param posSentence
	 * @param originalSlogan
	 */
	public FullyAnalyzedSentence(ChunkedSentence chunkedSentence, int chunkedSentenceFrequenz, PosSentence posSentence, String originalSlogan) {

		this.chunkedSentence = chunkedSentence;
		this.chunkedSentenceFrequenz = chunkedSentenceFrequenz;
		this.posSentence = posSentence;
		this.originalSlogan = originalSlogan;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(FullyAnalyzedSentence o) {
		// check for frequency
		if (this.chunkedSentenceFrequenz == o.chunkedSentenceFrequenz) {
			// check for number of chunks, fewer chunks is greater
			if (this.chunkedSentence.getChunks().size() > o.chunkedSentence.getChunks().size()) {
				return -1;
			} else {
				if (this.chunkedSentence.getChunks().size() < o.chunkedSentence.getChunks().size()) {
					return 1;
				} else {
					// to secure elements with the same chunk pattern are
					// neighbours
					if (this.chunkedSentence.hashCode() > o.chunkedSentence.hashCode()) {
						return 1;
					} else {
						if (this.chunkedSentence.hashCode() < o.chunkedSentence.hashCode()) {
							return -1;
						} else {
							return 0;
						}
					}
				}
			}
		} else {
			if (this.chunkedSentenceFrequenz < o.chunkedSentenceFrequenz) {
				return -1;
			} else {
				return 1;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof FullyAnalyzedSentence) {
			if (chunkedSentence.equals(((FullyAnalyzedSentence) o).chunkedSentence)
					&& chunkedSentenceFrequenz == ((FullyAnalyzedSentence) o).chunkedSentenceFrequenz
					&& posSentence.equals(((FullyAnalyzedSentence) o).posSentence)
					&& originalSlogan.equals(((FullyAnalyzedSentence) o).originalSlogan)) {
				return true; // total field equality
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (chunkedSentence.hashCode() + chunkedSentenceFrequenz + posSentence.hashCode() + originalSlogan.hashCode());
	}

	/**
	 * Getter {@link FullyAnalyzedSentence#chunkedSentence}
	 * 
	 * @return
	 */
	public ChunkedSentence getChunkedSentence() {
		return chunkedSentence;
	}

	/**
	 * Setter {@link FullyAnalyzedSentence#chunkedSentence}
	 * 
	 * @param chunkedSentence
	 */
	public void setChunkedSentence(ChunkedSentence chunkedSentence) {
		this.chunkedSentence = chunkedSentence;
	}

	/**
	 * Getter {@link FullyAnalyzedSentence#chunkedSentenceFrequenz}
	 * 
	 * @return
	 */
	public int getChunkedSentenceFrequenz() {
		return chunkedSentenceFrequenz;
	}

	/**
	 * Setter {@link FullyAnalyzedSentence#chunkedSentenceFrequenz}
	 * 
	 * @param chunkedSentenceFrequenz
	 */
	public void setChunkedSentenceFrequenz(int chunkedSentenceFrequenz) {
		this.chunkedSentenceFrequenz = chunkedSentenceFrequenz;
	}

	/**
	 * Getter {@link FullyAnalyzedSentence#posSentence}
	 * 
	 * @return
	 */
	public PosSentence getPosSentence() {
		return posSentence;
	}

	/**
	 * Setter {@link FullyAnalyzedSentence#posSentence}
	 * 
	 * @param posSentence
	 */
	public void setPosSentence(PosSentence posSentence) {
		this.posSentence = posSentence;
	}

	/**
	 * Getter {@link FullyAnalyzedSentence#originalSlogan}
	 * 
	 * @return
	 */
	public String getOriginalSlogan() {
		return originalSlogan;
	}

	/**
	 * Setter {@link FullyAnalyzedSentence#originalSlogan}
	 * 
	 * @param originalSlogan
	 */
	public void setOriginalSlogan(String originalSlogan) {
		this.originalSlogan = originalSlogan;
	}

}
