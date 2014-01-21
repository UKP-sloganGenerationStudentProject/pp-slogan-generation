package de.koch.uim_project.gui;

import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class StylisticDeviceWeightPanel extends JPanel {

	private JLabel label = new JLabel("Stylistic Device Weights");
	private LabeledInputPanel sdNone = new LabeledInputPanel("None", false);
	private LabeledInputPanel sdAlliteration = new LabeledInputPanel("Alliteration", false);
	private LabeledInputPanel sdParallelism = new LabeledInputPanel("Parallelism", false);
	private LabeledInputPanel sdOxymeron = new LabeledInputPanel("Oxymeron", false);
	private LabeledInputPanel sdMetaphor = new LabeledInputPanel("Metaphor", false);

	/**
	 * Eclipse generated serial
	 */
	private static final long serialVersionUID = -544526560973144358L;

	public StylisticDeviceWeightPanel() {
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		this.add(label);
		this.add(Box.createHorizontalStrut(5));
		this.add(new JSeparator(SwingConstants.VERTICAL));
		this.add(Box.createHorizontalStrut(5));
		this.add(sdNone);
		this.add(Box.createHorizontalStrut(5));
		this.add(new JSeparator(SwingConstants.VERTICAL));
		this.add(Box.createHorizontalStrut(5));
		this.add(sdAlliteration);
		this.add(Box.createHorizontalStrut(5));
		this.add(new JSeparator(SwingConstants.VERTICAL));
		this.add(Box.createHorizontalStrut(5));
		this.add(sdParallelism);
		this.add(Box.createHorizontalStrut(5));
		this.add(new JSeparator(SwingConstants.VERTICAL));
		this.add(Box.createHorizontalStrut(5));
		this.add(sdOxymeron);
		this.add(Box.createHorizontalStrut(5));
		this.add(new JSeparator(SwingConstants.VERTICAL));
		this.add(Box.createHorizontalStrut(5));
		this.add(sdMetaphor);
	}

	public JLabel getLabel() {
		return label;
	}

	public LabeledInputPanel getSdNone() {
		return sdNone;
	}

	public LabeledInputPanel getSdAlliteration() {
		return sdAlliteration;
	}

	public LabeledInputPanel getSdParallelism() {
		return sdParallelism;
	}

	public LabeledInputPanel getSdOxymeron() {
		return sdOxymeron;
	}

	public LabeledInputPanel getSdMetaphor() {
		return sdMetaphor;
	}

}
