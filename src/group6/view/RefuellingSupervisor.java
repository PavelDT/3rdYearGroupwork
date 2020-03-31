package group6.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import group6.controller.AircraftManagementDatabase;
import group6.model.ManagementRecord;
import group6.util.UISettings;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * An interface to SAAMS:
 * Refuelling Supervisor Screen:
 * Inputs events from the Refuelling Supervisor, and displays aircraft information.
 * This class is a controller for the AircraftManagementDatabase: sending it messages to change the aircraft status information.
 * This class also registers as an observer of the AircraftManagementDatabase, and is notified whenever any change occurs in that <<model>> element.
 * See written documentation.
 * @stereotype boundary/view/controller
 * @url element://model:project::SAAMS/design:view:::id15rnfcko4qme4cko4swib
 * @url element://model:project::SAAMS/design:node:::id15rnfcko4qme4cko4swib.node107
 * @url element://model:project::SAAMS/design:view:::id3y5z3cko4qme4cko4sw81
 */

public class RefuellingSupervisor extends JFrame implements Observer {

	private final AircraftManagementDatabase aircraftManagementDatabase;
	/**
	 * Refuelling Supervisor interface has access to the AircraftManagementDatabase.
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

	public RefuellingSupervisor(AircraftManagementDatabase aircraftManagementDatabase) {
		this.aircraftManagementDatabase = aircraftManagementDatabase;
		
		setTitle("Refuelling Supervisor");
		setLocation(UISettings.RefuelingSupervisorPosition);
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
		JScrollPane tableScroll = new JScrollPane(table);
		row0.add(tableScroll);

		JPanel row1 = new JPanel();
		row1.setLayout(new GridLayout());

		JButton refuleBtn = new JButton("Refueling Completed");
		// java 8 lambda - less code, easier to read
		refuleBtn.addActionListener(e -> refuel());
		row1.add(refuleBtn);

		// add frames to window
		window.add(row0);
		window.add(row1);

		// observe the singleton aircraftManDB
		this.aircraftManagementDatabase.addObserver(this);

		setVisible(true);
	}

	private void refuel() {
		if (table.getSelectionModel().isSelectionEmpty() == true) {
			JOptionPane.showMessageDialog(null, "Please select a flight!");
			// prevent execution.
			return;
		}

		int selectedRowIndex = table.getSelectedRow();
		// the the id of the managent record representing the flight
		int mCode = (int)model.getValueAt(selectedRowIndex, 0);
		int status = aircraftManagementDatabase.getStatus(mCode);

		if (status == ManagementRecord.READY_REFUEL) {
			// set status to CLEAN_AWAIT_MAINT
			aircraftManagementDatabase.setStatus(mCode, ManagementRecord.READY_PASSENGERS);
		} else {
			JOptionPane.showMessageDialog(null, "Flight cant be refueled!");
		}
	}

	public void update(Observable o, Object a) {
		// empty the table
		model.setRowCount(0);

		// loop over every management record to update table
		int maxRecords = aircraftManagementDatabase.maxMRs;
		for (int i=0; i<maxRecords; i++) {
			//int flightStatus = aircraftManagementDatabase.getStatus(i);
			int flightStatus = aircraftManagementDatabase.getStatus(i);
			// Status is displayd if its READY_REFUEL
			if (flightStatus == ManagementRecord.READY_REFUEL) {
				// update table
				String code = aircraftManagementDatabase.getFlightCode(i);
				model.addRow(new Object[]{i, code, flightStatus});
			}
		}
	}
}