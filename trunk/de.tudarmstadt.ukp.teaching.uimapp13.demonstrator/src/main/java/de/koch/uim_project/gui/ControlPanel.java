package de.koch.uim_project.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import de.koch.uim_project.main.Main;

public class ControlPanel extends JPanel implements ActionListener {

	/**
	 * Eclipse generated serial
	 */
	private static final long serialVersionUID = -4096249866634543665L;
	private static final String GENERATE_SLOGANS_ACTION = "generateSlogans";
	private static final String CLEAR_SLOGANS_ACTION = "clearSlogans";
	private static final String CLEAR_CONSOLE_ACTION = "clearConsole";

	private JButton genSlBut, clearSlBut, clearConBut;

	public ControlPanel() {
		this.setLayout(new GridLayout(4, 1, 10, 10));
		addButtons();
	}

	public void addButtons() {
		genSlBut = new JButton("Generate Slogans");
		genSlBut.setActionCommand(GENERATE_SLOGANS_ACTION);
		genSlBut.addActionListener(this);

		clearSlBut = new JButton("Clear Slogans");
		clearSlBut.setActionCommand(CLEAR_SLOGANS_ACTION);
		clearSlBut.addActionListener(this);

		clearConBut = new JButton("Clear Console");
		clearConBut.setActionCommand(CLEAR_CONSOLE_ACTION);
		clearConBut.addActionListener(this);

		this.add(genSlBut);
		this.add(clearSlBut);
		this.add(clearConBut);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case GENERATE_SLOGANS_ACTION:
			Main.generateSlogans();
			break;
		case CLEAR_SLOGANS_ACTION:
			Main.clearSlogans();
			break;
		case CLEAR_CONSOLE_ACTION:
			Main.clearConsole();
			break;
		}
	}

	public JButton getGenSlBut() {
		return genSlBut;
	}

	public JButton getClearSlBut() {
		return clearSlBut;
	}

	public JButton getClearConBut() {
		return clearConBut;
	}

	public void setButtonsEnabled(boolean bool) {
		genSlBut.setEnabled(bool);
		clearSlBut.setEnabled(bool);
		clearConBut.setEnabled(bool);

	}

}
