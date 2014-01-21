package de.koch.uim_project.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

public class FeatureListPanel extends JPanel implements ActionListener {

	/**
	 * Eclipse generated serial
	 */
	private static final long serialVersionUID = -3311046406162928353L;
	private static final String ADD_ACTION = "add";
	private static final String DELETE_ACTION = "delete";

	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	private JList<String> featureList = new JList<String>(listModel);
	private JButton addButton, deleteButton;
	private JScrollPane scrollPane = new JScrollPane(featureList);
	private JTextField featureToAdd = new JTextField();
	private JLabel titleLabel = new JLabel();

	public FeatureListPanel(String title) {
		this.setLayout(new BorderLayout());

		titleLabel.setText(title);

		featureList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		addButton = new JButton("Add");
		addButton.setActionCommand(ADD_ACTION);
		addButton.addActionListener(this);

		deleteButton = new JButton("Delete");
		deleteButton.setActionCommand(DELETE_ACTION);
		deleteButton.addActionListener(this);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));

		buttonPanel.add(deleteButton);
		buttonPanel.add(Box.createHorizontalStrut(5));
		buttonPanel.add(new JSeparator(SwingConstants.VERTICAL));
		buttonPanel.add(Box.createHorizontalStrut(5));
		buttonPanel.add(featureToAdd);
		buttonPanel.add(addButton);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		add(titleLabel, BorderLayout.PAGE_START);
		add(scrollPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.PAGE_END);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case ADD_ACTION:
			listModel.addElement(featureToAdd.getText());
			break;
		case DELETE_ACTION:
			listModel.remove(featureList.getSelectedIndex());
			break;
		}
	}

	public void setList(Set<String> words) {
		for (String word : words) {
			this.listModel.addElement(word);
		}
	}

	public Set<String> getList() {
		Set<String> result = new HashSet<String>();
		for (int i = 0; i < listModel.getSize(); i++) {
			result.add(listModel.get(i));
		}
		return result;
	}

	public DefaultListModel<String> getListModel() {
		return listModel;
	}

	public JList<String> getFeatureList() {
		return featureList;
	}

	public JButton getAddButton() {
		return addButton;
	}

	public JButton getDeleteButton() {
		return deleteButton;
	}

	public JTextField getFeatureToAdd() {
		return featureToAdd;
	}

	public JLabel getTitleLabel() {
		return titleLabel;
	}

}
