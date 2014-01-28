package de.koch.uim_project.gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.koch.uim_project.util.Emotion;

public class LabeledComboBoxPanel extends JPanel {

	private JLabel label = new JLabel();
	private JComboBox<String> emotionCombo = new JComboBox<String>();

	/**
	 * Eclipse generated serial
	 */
	private static final long serialVersionUID = 7480450208877108118L;

	public LabeledComboBoxPanel(String title) {
		label.setText(title);
		addEmotions();
		this.setLayout(new GridLayout(1, 2, 10, 0));
		this.setPreferredSize(new Dimension(500, 20));
		this.add(label);
		this.add(emotionCombo);
	}

	private void addEmotions() {
		emotionCombo.addItem(Emotion.NONE.toString());
		emotionCombo.addItem(Emotion.POSITIVE.toString());
		emotionCombo.addItem(Emotion.NEGATIVE.toString());
		emotionCombo.addItem(Emotion.ANGER.toString());
		emotionCombo.addItem(Emotion.ANTICIPATION.toString());
		emotionCombo.addItem(Emotion.DISGUST.toString());
		emotionCombo.addItem(Emotion.FEAR.toString());
		emotionCombo.addItem(Emotion.JOY.toString());
		emotionCombo.addItem(Emotion.SADNESS.toString());
		emotionCombo.addItem(Emotion.SURPRISE.toString());
		emotionCombo.addItem(Emotion.TRUST.toString());

	}

	public JLabel getLabel() {
		return label;
	}

	public JComboBox<String> getEmotionCombo() {
		return emotionCombo;
	}

}
