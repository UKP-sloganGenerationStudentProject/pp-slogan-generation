package de.koch.uim_project.gui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class BasicConfigPanel extends JPanel {

	private LabeledInputPanel gameName = new LabeledInputPanel("Game name", true);
	private LabeledInputPanel sloganCount = new LabeledInputPanel("Number of Slogans to generate", true);
	private LabeledComboBoxPanel emotion = new LabeledComboBoxPanel("Emotion");
	private LabeledInputPanel randomSeed = new LabeledInputPanel("Random Seed", true);
	private LabeledInputPanel minWordListForGeneration = new LabeledInputPanel("Minimal word list for generation",true);
	private LabeledInputPanel maxSynsetDepth = new LabeledInputPanel("Maximal Synset search depth", true);

	/**
	 * Eclipse generated serial
	 */
	private static final long serialVersionUID = 1043056131744485045L;

	public BasicConfigPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		gameName.setPreferredSize(new Dimension(500, 20));
		sloganCount.setPreferredSize(new Dimension(500, 20));
		emotion.setPreferredSize(new Dimension(500, 20));
		randomSeed.setPreferredSize(new Dimension(500, 20));
		minWordListForGeneration.setPreferredSize(new Dimension(500, 20));
		maxSynsetDepth.setPreferredSize(new Dimension(500, 20));
		add(gameName);
		add(sloganCount);
		add(emotion);
		add(randomSeed);
		add(minWordListForGeneration);
		add(maxSynsetDepth);
	}

	public LabeledInputPanel getGameName() {
		return gameName;
	}

	public LabeledInputPanel getSloganCount() {
		return sloganCount;
	}

	public LabeledComboBoxPanel getEmotion() {
		return emotion;
	}

	public LabeledInputPanel getRandomSeed() {
		return randomSeed;
	}

	public LabeledInputPanel getMinWordListForGeneration() {
		return minWordListForGeneration;
	}

	public LabeledInputPanel getMaxSynsetDepth() {
		return maxSynsetDepth;
	}
	
	

}
