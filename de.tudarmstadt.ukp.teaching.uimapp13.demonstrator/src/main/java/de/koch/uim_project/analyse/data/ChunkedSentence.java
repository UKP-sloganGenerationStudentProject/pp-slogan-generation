package de.koch.uim_project.analyse.data;

import java.util.List;

import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.Chunk;

/**
 * This class is a container for a chunk pattern
 * 
 * @author Frerik Koch
 * 
 */
public class ChunkedSentence {

	private final List<Chunk> chunks;

	/**
	 * 
	 * @param chunks
	 *            Chunks in order to convert to {@link ChunkedSentence}
	 */
	public ChunkedSentence(List<Chunk> chunks) {
		this.chunks = chunks;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof ChunkedSentence) {
			List<Chunk> otherTypes = ((ChunkedSentence) o).getChunks();

			// compare size
			if (chunks.size() != otherTypes.size()) {
				return false;
			}

			// compare individual chunks in order
			for (int i = 0; i < chunks.size(); i++) {
				if (!chunks.get(i).getChunkValue().equals(otherTypes.get(i).getChunkValue())) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public List<Chunk> getChunks() {
		return chunks;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Chunk chunk : chunks) {
			sb.append(chunk.getChunkValue() + "_");
		}
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		StringBuilder sb = new StringBuilder();
		for (Chunk chunk : chunks) {
			sb.append(chunk.getChunkValue());
		}
		return sb.toString().hashCode();
	}

}
