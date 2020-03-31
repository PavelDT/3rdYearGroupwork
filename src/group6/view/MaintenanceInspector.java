package group6.view;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import group6.controller.AircraftManagementDatabase;
import group6.model.ManagementRecord;
import group6.util.UISettings;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;


/**
 * An interface to SAAMS:
 * Maintenance Inspector Screen:
 * Inputs events from the Maintenance Inspector, and displays aircraft information.
 * This class is a controller for the AircraftManagementDatabase: sending it messages to change the aircraft status information.
 * This class also registers as an observer of the AircraftManagementDatabase, and is notified whenever any change occurs in that <<model>> element.
 * See written documentation.
 * @stereotype boundary/view/controller
 * @url element://model:project::SAAMS/design:node:::id4tg7xcko4qme4cko4swuu.node146
 * @url element://model:project::SAAMS/design:view:::id15rnfcko4qme4cko4swib
 * @url element://model:project::SAAMS/design:view:::id4tg7xcko4qme4cko4swuu
 * @url element://model:project::SAAMS/design:node:::id15rnfcko4qme4cko4swib.node107
 * @url element://model:project::SAAMS/design:view:::id3y5z3cko4qme4cko4sw81
 */

public class MaintenanceInspector extends JFrame implements Observer {

	private final AircraftManagementDatabase aircraftManagementDatabase;
	/**
	 * The Refuelling Supervisor Screen interface has access to the
	 * AircraftManagementDatabase.
	 * 
	 * @supplierCardinality 1
	 * @clientCardinality 1
	 * @label accesses/observes
	 * @directed
	 */

	// table for the flights and their status
	private JTable table;
	// model of the flights list
	private DefaultTableModel model;
	// button for repairing faulty machines
	JButton repairedBtn;


	public MaintenanceInspector(AircraftManagementDatabase aircraftManagementDatabase) {

		this.aircraftManagementDatabase = aircraftManagementDatabase;
		setTitle("Maintenance Inspector");
		setLocation(UISettings.MaintananceInspectorPosition);
		setSize(UISettings.VIEW_WIDTH, UISettings.VIEW_HEIGHT);

		Container window = getContentPane();
		window.setLayout(new BoxLayout(window, BoxLayout.PAGE_AXIS));

		JPanel row0 = new JPanel();
		row0.setBorder(new EmptyBorder(0, 0, 10, 0));
		row0.setLayout(new BorderLayout());
		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(model);
		table.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		table.getTableHeader().setReorderingAllowed(false);
		model.addColumn("ID");
		model.addColumn("AIRCRAFT");
		model.addColumn("STATUS");
		table.setModel(model);
		table.getSelectionModel().addListSelectionListener(event -> onRowClick());
		JScrollPane tableScroll = new JScrollPane(table);
		row0.add(tableScroll);

		JPanel row1 = new JPanel();
		row1.setLayout(new GridLayout());

		JButton noFaultsBtn = new JButton("No Faults");
		// Java 8 lambda, leaner code
		noFaultsBtn.addActionListener(e -> noFaults());
		row1.add(noFaultsBtn);

		JButton faultsFoundBtn = new JButton("Faults Found");
		// Java 8 lambda, they're beautiful
		faultsFoundBtn.addActionListener(e -> faultsFound());
		row1.add(faultsFoundBtn);


		repairedBtn = new JButton("Repair Flight");
		// Java 8 lambda, they're beautiful
		repairedBtn.addActionListener(e -> repaired());
		repairedBtn.setEnabled(false);
		repairedBtn.setToolTipText("Enabled only for Cleaned AND Faulty plane");
		row1.add(repairedBtn);

		// add frames to window
		window.add(row0);
		window.add(row1);

		// observe the singleton aircraftManDB
		this.aircraftManagementDatabase.addObserver(this);

		setVisible(true);
	}

	private void onRowClick() {
		int selectedIndex = table.getSelectedRow();
		System.out.println("zz "  + selectedIndex);
		if (selectedIndex != -1 && (int)model.getValueAt(selectedIndex, 2) == ManagementRecord.AWAIT_REPAIR) {
			repairedBtn.setEnabled(true);
		} else {
			repairedBtn.setEnabled(false);
		}
	}

	private void noFaults() {
		if (table.getSelectionModel().isSelectionEmpty() == true) {
			JOptionPane.showMessageDialog(null, "Please select a flight!");
			// prevent execution.
			return;
		}

		int selectedRowIndex = table.getSelectedRow();
		// the the id of the managent record representing the flight
		int mCode = (int)model.getValueAt(selectedRowIndex, 0);
		int status = aircraftManagementDatabase.getStatus(mCode);

		if (status == ManagementRecord.READY_CLEAN_AND_MAINT) {
			// no faults, flight hasn't yet been cleaned
			aircraftManagementDatabase.setStatus(mCode, ManagementRecord.OK_AWAIT_CLEAN);
		} else if (status == ManagementRecord.CLEAN_AWAIT_MAINT) {
			// no faults, and plane is clean, we can refuel
			aircraftManagementDatabase.setStatus(mCode, ManagementRecord.READY_REFUEL);
		}
	}

	private void faultsFound() {
		if (table.getSelectionModel().isSelectionEmpty() == true) {
			JOptionPane.showMessageDialog(null, "Please select a flight!");
			// prevent execution.
			return;
		}

		int selectedRowIndex = table.getSelectedRow();
		// the the id of the managent record representing the flight
		int mCode = (int)model.getValueAt(selectedRowIndex, 0);
		int status = aircraftManagementDatabase.getStatus(mCode);

		if (status == ManagementRecord.READY_CLEAN_AND_MAINT) {
			// faults were found, flight hasn't yet been cleaned
			aircraftManagementDatabase.setStatus(mCode, ManagementRecord.FAULTY_AWAIT_CLEAN);
		} else if (status == ManagementRecord.CLEAN_AWAIT_MAINT) {
			// faults found, flight is already clean
			// set flight to be repaired
			aircraftManagementDatabase.setStatus(mCode, ManagementRecord.AWAIT_REPAIR);
		}
	}

	private void repaired() {
		if (table.getSelectionModel().isSelectionEmpty() == true) {
			JOptionPane.showMessageDialog(null, "Please select a flight!");
			// prevent execution.
			return;
		}

		int selectedRowIndex = table.getSelectedRow();
		// the the id of the managent record representing the flight
		int mCode = (int)model.getValueAt(selectedRowIndex, 0);
		int status = aircraftManagementDatabase.getStatus(mCode);

		if (status == ManagementRecord.AWAIT_REPAIR) {
			// tell everyone the flight was repaired
			// return flight ot READY_CLEAN_AND_MAINT
			aircraftManagementDatabase.setStatus(mCode, ManagementRecord.READY_CLEAN_AND_MAINT);
		}
	}

	@Override
	public void update(Observable observable, Object o) {
		// empty the table
		model.setRowCount(0);

		// loop over every management record to update table
		int maxRecords = aircraftManagementDatabase.maxMRs;
		for (int i=0; i<maxRecords; i++) {
			//int flightStatus = aircraftManagementDatabase.getStatus(i);
			int flightStatus = aircraftManagementDatabase.getStatus(i);
			// Status is displayd if its one of the below 5:
			// READY_CLEAN_AND_MAINT = 8;
			// FAULTY_AWAIT_CLEAN = 9;
			// CLEAN_AWAIT_MAINT = 10;
			// OK_AWAIT_CLEAN = 11;
			// AWAIT_REPAIR = 12;
			if (flightStatus >= ManagementRecord.READY_CLEAN_AND_MAINT &&
				flightStatus <= ManagementRecord.AWAIT_REPAIR) {
				// update table
				String code = aircraftManagementDatabase.getFlightCode(i);
				model.addRow(new Object[]{i, code, flightStatus});
			}
		}
	}
}
