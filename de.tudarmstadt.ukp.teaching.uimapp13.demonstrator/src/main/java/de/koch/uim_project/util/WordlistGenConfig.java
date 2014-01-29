package de.koch.uim_project.util;

import java.util.Set;

import de.koch.uim_project.generation.BaseWordListGen;

/**
 * This class is a data container for configuration of {@link BaseWordListGen}
 * 
 * @author Frerik Koch
 * 
 */
public class WordlistGenConfig {

	private Set<String> featureList;
	private int minWordlistGen;
	private int maxWordlist;
	private int maxSynsetDepth;
	private String name;

	public WordlistGenConfig(Set<String> featureList, int minWordlistGen, int maxWordlist, int maxSynsetDepth, String name) {
		super();

		this.featureList = featureList;
		this.minWordlistGen = minWordlistGen;
		this.maxWordlist = maxWordlist;
		this.maxSynsetDepth = maxSynsetDepth;
		this.name = name;
	}

	public Set<String> getFeatureList() {
		return featureList;
	}

	public void setFeatureList(Set<String> featureList) {
		this.featureList = featureList;
	}

	public int getMinWordlistGen() {
		return minWordlistGen;
	}

	public void setMinWordlistGen(int minWordlistGen) {
		this.minWordlistGen = minWordlistGen;
	}

	public int getMaxWordlist() {
		return maxWordlist;
	}

	public void setMaxWordlist(int maxWordlist) {
		this.maxWordlist = maxWordlist;
	}

	public int getMaxSynsetDepth() {
		return maxSynsetDepth;
	}

	public void setMaxSynsetDepth(int maxSynsetDepth) {
		this.maxSynsetDepth = maxSynsetDepth;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((featureList == null) ? 0 : featureList.hashCode());
		result = prime * result + maxSynsetDepth;
		result = prime * result + maxWordlist;
		result = prime * result + minWordlistGen;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WordlistGenConfig other = (WordlistGenConfig) obj;
		if (featureList == null) {
			if (other.featureList != null)
				return false;
		} else if (!featureList.equals(other.featureList))
			return false;
		if (maxSynsetDepth != other.maxSynsetDepth)
			return false;
		if (maxWordlist != other.maxWordlist)
			return false;
		if (minWordlistGen != other.minWordlistGen)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
