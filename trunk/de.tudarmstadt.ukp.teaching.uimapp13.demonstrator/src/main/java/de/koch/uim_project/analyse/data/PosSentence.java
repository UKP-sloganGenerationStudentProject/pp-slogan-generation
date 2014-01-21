package de.koch.uim_project.analyse.data;

import java.util.List;

public class PosSentence {

	private List<String> posValues;

	public PosSentence(List<String> posValues) {
		this.posValues = posValues;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PosSentence) {
			List<String> otherTypes = ((PosSentence) o).getPosValues();
			if (posValues.size() != otherTypes.size()) {
				return false;
			}
			for (int i = 0; i < posValues.size(); i++) {
				if (!posValues.get(i).equals(otherTypes.get(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public List<String> getPosValues() {
		return this.posValues;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String str : posValues) {
			sb.append(str + "_");
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		StringBuilder sb = new StringBuilder();
		for (String str : posValues) {
			sb.append(str);
		}
		return sb.toString().hashCode();
	}
}
