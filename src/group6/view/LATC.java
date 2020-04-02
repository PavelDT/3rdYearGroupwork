package group6.view;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import group6.controller.AircraftManagementDatabase;
import group6.model.ManagementRecord;
import group6.util.UISettings;

/**
 * This Class represents the LATC
 * This grants permission to incoming aircrafts to land at the airport
 *
 */
public class LATC extends JFrame implements Observer {

	/**
	 * The LATC Screen interface has access to the
	 * AircraftManagementDatabase.
	 *
	 * @clientCardinality 1
	 * @supplierCardinality 1
	 * @label accesses/observes
	 * @directed
	 */
	private AircraftManagementDatabase aircraftManagementDatabase;

	/**
	 * Instance of DefaultTableModel
	 */
	private DefaultTableModel model;

	/**
	 * Instance of JTable
	 */
	private JTable table;

	/**
	 * Constructor
	 */
	public LATC(AircraftManagementDatabase aircraftManagementDatabase) {
		this.aircraftManagementDatabase = aircraftManagementDatabase;

		setLocation(UISettings.LATCPosition);
		setSize(UISettings.VIEW_WIDTH, UISettings.VIEW_HEIGHT);
		setTitle("LATC");

		//Make model selectable but not editable
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
		model.addColumn("ID");
		table.setModel(model);


		JPanel row1 = new JPanel();
		row1.setLayout(new BorderLayout());
		JScrollPane tableScroll = new JScrollPane(table);
		row1.add(tableScroll);
		row1.setBorder(new EmptyBorder(10, 10, 10, 10));


		// for allowing a flight to enter landing stage
		JPanel row2 = new JPanel();
		row2.setLayout(new FlowLayout());
		JButton grantLandingBtn = new JButton("Grant Landing");
		grantLandingBtn.setSize(200, 50);
		grantLandingBtn.addActionListener(e ->grantLanding());
		row2.add(grantLandingBtn);

		// button for assigning gate
		JButton flightLandedBtn = new JButton("Flight Landed");
		flightLandedBtn.addActionListener(e ->flightLanded());
		flightLandedBtn.setSize(200, 50);

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		// group the combo box and assignGate button
		row2.add(flightLandedBtn);

		// add top level panel for all components
		JPanel p = new JPanel();
		p.add(row1);
		p.add(row2);
		BoxLayout boxLayout = new BoxLayout(p, BoxLayout.PAGE_AXIS);
		p.setLayout(boxLayout);

		contentPane.add(p);


		aircraftManagementDatabase.addObserver(this);

		setVisible(true);
	}

	/**
	 * Grants landing permission to incoming plane
	 */
	private void grantLanding() {
		if (table.getSelectionModel().isSelectionEmpty() == true) {
			JOptionPane.showMessageDialog(null, "Please select a flight!");
			// prevent execution.
			return;
		}
		int selectedIndex = table.getSelectedRow();
		int mCode = (int)model.getValueAt(selectedIndex, 2);

		// check if the flight that is selected for langing, has been given
		// ground clearance
		if (aircraftManagementDatabase.getStatus(mCode) != ManagementRecord.GROUND_CLEARANCE_GRANTED) {
			JOptionPane.showMessageDialog(null, "Flight doesn't have Ground Clearance!");
			// prevent execution.
			return;
		}

		// status is correct and item is selected, update the flight to landing
		aircraftManagementDatabase.setStatus(mCode, ManagementRecord.LANDING);
	}

	/**
	 * Changes the status of the plane to landed
	 */
	private void flightLanded() {
		if (table.getSelectionModel().isSelectionEmpty() == true) {
			JOptionPane.showMessageDialog(null, "Please select a flight!");
			// prevent execution.
			return;
		}
		int selectedIndex = table.getSelectedRow();
		int mCode = (int)model.getValueAt(selectedIndex, 2);

		// check if flight was in landing stage
		if (aircraftManagementDatabase.getStatus(mCode) != ManagementRecord.LANDING) {
			JOptionPane.showMessageDialog(null, "Flight doesn't have Ground Clearance!");
			// prevent execution.
			return;
		}

		// status is correct and item is selected, update the flight to landed
		aircraftManagementDatabase.setStatus(mCode, ManagementRecord.LANDED);
	}

	/**
	 * Updates the view
	 */
	@Override
	public void update(Observable observable, Object o) {
		model.setRowCount(0);
		for(int i : aircraftManagementDatabase.getWithStatus(ManagementRecord.GROUND_CLEARANCE_GRANTED)) {
			int status = aircraftManagementDatabase.getStatus(i);
			String code = aircraftManagementDatabase.getFlightCode(i);
			model.addRow(new Object[]{code, status, i});
		}

		for(int i : aircraftManagementDatabase.getWithStatus(ManagementRecord.LANDING)) {
			int status = aircraftManagementDatabase.getStatus(i);
			String code = aircraftManagementDatabase.getFlightCode(i);
			model.addRow(new Object[]{code, status, i});
		}

		for(int i : aircraftManagementDatabase.getWithStatus(ManagementRecord.LANDED)) {
			int status = aircraftManagementDatabase.getStatus(i);
			String code = aircraftManagementDatabase.getFlightCode(i);
			model.addRow(new Object[]{code, status, i});
		}
	}
}
