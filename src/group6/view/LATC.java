package group6.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import group6.controller.AircraftManagementDatabase;
import group6.controller.GateInfoDatabase;
import group6.util.UISettings;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class LATC extends JDialog implements Observer {

	private final JPanel contentPanel = new JPanel();

	private JTable table;
	private GateInfoDatabase gateInfoDatabase;

	private AircraftManagementDatabase aircraftManagementDatabase;

	/** The user-defined model of the basket */
	private static DefaultTableModel model;

	/**
	 * Create the dialog.
	 */
	public LATC(AircraftManagementDatabase aircraftManagementDatabase, GateInfoDatabase gateInfoDatabase) {
		this.aircraftManagementDatabase = aircraftManagementDatabase;
		this.gateInfoDatabase = gateInfoDatabase;

		setLocation(UISettings.LATCPosition);
		setSize(UISettings.VIEW_WIDTH, UISettings.VIEW_HEIGHT);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		
		JLabel lblTitle = new JLabel("LATC");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblTitle, 10, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblTitle, 197, SpringLayout.WEST, contentPanel);
		lblTitle.setFont(new Font("Arial Black", Font.BOLD, 26));
		contentPanel.add(lblTitle);
		
		JTextArea textArea = new JTextArea();
		sl_contentPanel.putConstraint(SpringLayout.NORTH, textArea, 86, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, textArea, 21, SpringLayout.WEST, contentPanel);
		contentPanel.add(textArea);
		
		JScrollPane scrollPane = new JScrollPane();
		sl_contentPanel.putConstraint(SpringLayout.NORTH, scrollPane, 0, SpringLayout.NORTH, textArea);
		sl_contentPanel.putConstraint(SpringLayout.WEST, scrollPane, 5, SpringLayout.EAST, textArea);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, scrollPane, -71, SpringLayout.SOUTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, scrollPane, 226, SpringLayout.WEST, contentPanel);
		scrollPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPanel.add(scrollPane);
		
		
		model = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(model);
		table.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		table.getTableHeader().setReorderingAllowed(false);
		model.addColumn("AIRCRAFT");
		model.addColumn("STATUS");
		table.setModel(model);

		scrollPane.setViewportView(table);

		setVisible(true);
	}

	public String[] checkForAircraft() {
		int[] MRs = aircraftManagementDatabase.getWithStatus(1);
		String[] aircraftCodes = new String[MRs.length];
		for (int i = 0; i < MRs.length; i++) {
			aircraftCodes[i] = aircraftManagementDatabase.getFlightCode(MRs[i]);
		}
		return aircraftCodes;
	}

	@Override
	public void update(Observable observable, Object o) {
		AircraftManagementDatabase aircraftDatabase = null;
		GateInfoDatabase gateDatabase = null;

		try {
			aircraftDatabase = (AircraftManagementDatabase) o;
		} catch (ClassCastException e) {
			try {
				gateDatabase = (GateInfoDatabase) o;
			} catch (ClassCastException f) {
				System.out.println(f.getMessage());
			}
		}

		if (aircraftDatabase != null) {
			aircraftManagementDatabase = aircraftDatabase;
		}
		if (gateDatabase != null) {
			gateInfoDatabase = gateDatabase;
		}
	}
}
