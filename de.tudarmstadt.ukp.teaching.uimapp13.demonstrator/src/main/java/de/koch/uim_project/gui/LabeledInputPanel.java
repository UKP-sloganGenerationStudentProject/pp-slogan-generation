package de.koch.uim_project.gui;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LabeledInputPanel extends JPanel {

	/**
	 * Eclipse generated serial
	 */
	private static final long serialVersionUID = -2370704655494383479L;

	private JLabel label = new JLabel();
	private JTextField textField = new JTextField();

	public LabeledInputPanel(String title, boolean vertical) {
		label.setText(title);

		if (vertical) {
			this.setLayout(new GridLayout(1, 2, 10, 0));
		} else {
			this.setLayout(new GridLayout(2, 1, 0, 10));
		}

		this.add(label);
		this.add(textField);

	}

	public JLabel getLabel() {
		return label;
	}

	public JTextField getTextField() {
		return textField;
	}

}
