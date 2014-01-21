package de.koch.uim_project.gui;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class FeaturePanel extends JPanel {

	private FeatureListPanel features = new FeatureListPanel("Features");
	private FeatureListPanel alienFeatures = new FeatureListPanel("Alien Features");
	/**
	 * Eclipse generated serial
	 */
	private static final long serialVersionUID = 631377231664137841L;

	public FeaturePanel() {
		this.setLayout(new GridLayout(1, 2, 10, 0));
		this.add(features);
		this.add(alienFeatures);
	}

	public FeatureListPanel getFeatures() {
		return features;
	}

	public FeatureListPanel getAlienFeatures() {
		return alienFeatures;
	}

}
