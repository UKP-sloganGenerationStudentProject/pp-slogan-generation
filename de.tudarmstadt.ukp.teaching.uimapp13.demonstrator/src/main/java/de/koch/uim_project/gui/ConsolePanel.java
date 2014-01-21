package de.koch.uim_project.gui;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ConsolePanel extends JPanel {

	/**
	 * Eclipse generated Serial
	 */
	private static final long serialVersionUID = 6015470667507522161L;

	private JList<String> console = new JList<String>();
	private JScrollPane scrollPane = new JScrollPane();
	private DefaultListModel<String> listmodel = new DefaultListModel<String>();
	private JLabel title = new JLabel("Console");

	public ConsolePanel() {
		this.setLayout(new BorderLayout());
		console.setModel(listmodel);
		console.setEnabled(false);
		scrollPane.setViewportView(console);

		this.add(title, BorderLayout.PAGE_START);
		this.add(scrollPane, BorderLayout.CENTER);

	}

	public JList<String> getConsole() {
		return console;
	}

	public DefaultListModel<String> getListmodel() {
		return listmodel;
	}

	public JLabel getTitle() {
		return title;
	}

}
