package de.koch.uim_project.gui;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ConfigPanel extends JPanel {

	private BasicConfigPanel basicConfigPanel = new BasicConfigPanel();
	private PatternWeightPanel patternWeightPanel = new PatternWeightPanel();
	private StylisticDeviceWeightPanel stylisticDeviceWeightPanel = new StylisticDeviceWeightPanel();

	/**
	 * Eclipse generated Serial
	 */
	private static final long serialVersionUID = 4327526735864723755L;

	public ConfigPanel() {
		this.setLayout(new GridLayout(3, 1, 0, 10));
		this.add(new JScrollPane(basicConfigPanel));
		this.add(new JScrollPane(patternWeightPanel));
		this.add(new JScrollPane(stylisticDeviceWeightPanel));

	}

	public BasicConfigPanel getBasicConfigPanel() {
		return basicConfigPanel;
	}

	public PatternWeightPanel getPatternWeightPanel() {
		return patternWeightPanel;
	}

	public StylisticDeviceWeightPanel getStylisticDeviceWeightPanel() {
		return stylisticDeviceWeightPanel;
	}

}
