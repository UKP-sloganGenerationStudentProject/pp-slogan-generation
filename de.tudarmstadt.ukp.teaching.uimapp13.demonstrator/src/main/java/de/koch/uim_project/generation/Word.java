package de.koch.uim_project.generation;

import java.util.HashSet;
import java.util.Set;

import de.koch.uim_project.util.Emotion;
import de.tudarmstadt.ukp.lmf.model.core.LexicalEntry;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;

/**
 * This class represents a word.
 * @author Frerik Koch
 *
 */
public class Word {

	private String lemma;
	private EPartOfSpeech pos;
	private Set<Emotion> emotions = new HashSet<Emotion>();
	private boolean isFeature = false;
	private boolean emotionChecked = false;

	public Word(String lemma, EPartOfSpeech pos) {
		this.lemma = lemma.toLowerCase();
		this.pos = pos;
	}

	public Word(String lemma, EPartOfSpeech pos, Set<Emotion> emotions) {
		this.lemma = lemma.toLowerCase();
		this.pos = pos;
		this.emotions = emotions;
	}

	public Word(String lemma, EPartOfSpeech pos, boolean isFeature) {
		super();
		this.lemma = lemma;
		this.pos = pos;
		this.isFeature = isFeature;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma.toLowerCase();
	}

	public EPartOfSpeech getPos() {
		return pos;
	}

	public void setPos(EPartOfSpeech pos) {
		this.pos = pos;
	}

	@Override
	public String toString() {
		return this.pos + ":" + this.lemma;
	}

	@Override
	public int hashCode() {
		return lemma.hashCode() + pos.hashCode();
	}

	/**
	 * This class generates a {@link Word} instance from a given {@link LexicalEntry}.
	 * It ensures that the verb has POS verb/noun/adjective has no null values and has no empty lemma.
	 * If one condition does not hold: <code>null</code> is returned
	 * @param entry Entry to generate {@link Word} from
	 * @return Generated {@link Word} or <code>null</code>
	 */
	public static Word fromLexicalEntry(LexicalEntry entry) {
		if (entry.getLemmaForm() != null
				&& !entry.getLemmaForm().matches("\\s")
				&& entry.getPartOfSpeech() != null
				&& (entry.getPartOfSpeech() == EPartOfSpeech.verb || entry.getPartOfSpeech() == EPartOfSpeech.noun || entry.getPartOfSpeech() == EPartOfSpeech.adjective)) {
			return new Word(entry.getLemmaForm(), entry.getPartOfSpeech());
		} else {
			return null;
		}

	}

	public boolean isFeature() {
		return isFeature;
	}

	public void setFeature(boolean isFeature) {
		this.isFeature = isFeature;
	}

	public Set<Emotion> getEmotions() {
		return emotions;
	}

	public void addEmotion(Emotion emotion) {
		this.emotions.add(emotion);
	}

	public void setEmotions(Set<Emotion> emotions) {
		if (emotions != null) {
			this.emotions = emotions;
		}
	}

	public boolean isEmotionChecked() {
		return emotionChecked;
	}

	public void setEmotionChecked(boolean emotionChecked) {
		this.emotionChecked = emotionChecked;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Word && ((Word) o).lemma.equals(lemma) && ((Word) o).pos == pos) {
			return true;
		}
		return false;
	}

}
