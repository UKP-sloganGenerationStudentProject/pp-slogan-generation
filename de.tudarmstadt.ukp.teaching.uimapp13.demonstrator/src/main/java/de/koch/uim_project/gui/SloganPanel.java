package de.koch.uim_project.gui;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SloganPanel extends JPanel {

	/**
	 * Eclipse generated serial
	 */
	private static final long serialVersionUID = 2139610263920078739L;

	private JList<String> slogans = new JList<String>();
	private JScrollPane scrollPane = new JScrollPane();
	private DefaultListModel<String> listmodel = new DefaultListModel<String>();
	private JLabel title = new JLabel("Generated Slogans");

	public SloganPanel() {
		this.setLayout(new BorderLayout());
		slogans.setModel(listmodel);
		slogans.setEnabled(false);
		scrollPane.setViewportView(slogans);

		this.add(title, BorderLayout.PAGE_START);
		this.add(scrollPane, BorderLayout.CENTER);
	}

	public JList<String> getSlogans() {
		return slogans;
	}

	public DefaultListModel<String> getListmodel() {
		return listmodel;
	}

	public JLabel getTitle() {
		return title;
	}

}
