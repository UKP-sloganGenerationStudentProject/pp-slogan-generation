package de.koch.uim_project.gui;

import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class PatternWeightPanel extends JPanel {

	private JLabel label = new JLabel("Pattern weights");
	private LabeledInputPanel patternJJNNJJNN = new LabeledInputPanel("JJ NN. JJ NN.", false);
	private LabeledInputPanel patternVBVBVB = new LabeledInputPanel("VB,VB,VB", false);
	private LabeledInputPanel patternJJNN = new LabeledInputPanel("JJ NN", false);
	private LabeledInputPanel patternNCVC = new LabeledInputPanel("NN VVN", false);

	/**
	 * Eclipse generated serial
	 */
	private static final long serialVersionUID = 249250912039753379L;

	public PatternWeightPanel() {
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		this.add(label);
		this.add(Box.createHorizontalStrut(5));
		this.add(new JSeparator(SwingConstants.VERTICAL));
		this.add(Box.createHorizontalStrut(5));
		this.add(patternJJNN);
		this.add(Box.createHorizontalStrut(5));
		this.add(new JSeparator(SwingConstants.VERTICAL));
		this.add(Box.createHorizontalStrut(5));
		this.add(patternJJNNJJNN);
		this.add(Box.createHorizontalStrut(5));
		this.add(new JSeparator(SwingConstants.VERTICAL));
		this.add(Box.createHorizontalStrut(5));
		this.add(patternNCVC);
		this.add(Box.createHorizontalStrut(5));
		this.add(new JSeparator(SwingConstants.VERTICAL));
		this.add(Box.createHorizontalStrut(5));
		this.add(patternVBVBVB);
		this.add(Box.createHorizontalStrut(5));
		this.add(new JSeparator(SwingConstants.VERTICAL));
		this.add(Box.createHorizontalStrut(5));
	}

	public JLabel getLabel() {
		return label;
	}

	public LabeledInputPanel getPatternJJNNJJNN() {
		return patternJJNNJJNN;
	}

	public LabeledInputPanel getPatternVBVBVB() {
		return patternVBVBVB;
	}

	public LabeledInputPanel getPatternJJNN() {
		return patternJJNN;
	}

	public LabeledInputPanel getPatternNCVC() {
		return patternNCVC;
	}

}
