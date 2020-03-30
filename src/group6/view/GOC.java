package group6.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import group6.controller.AircraftManagementDatabase;
import group6.controller.GateInfoDatabase;
import group6.model.Gate;
import group6.model.ManagementRecord;
import group6.util.UISettings;

public class GOC extends JDialog implements Observer {

	/**
	 * The Ground Operations Controller Screen interface has access to the
	 * AircraftManagementDatabase.
	 *
	 * @clientCardinality 1
	 * @supplierCardinality 1
	 * @label accesses/observes
	 * @directed
	 */
	private AircraftManagementDatabase aircraftManagementDatabase;
	private GateInfoDatabase gateInfoDatabase;

	// table for the flights and their status
	private JTable table;
	// for the available gates
	private JComboBox<Integer> gatesComboBox;
	// model of the flights list
	private DefaultTableModel model;

	/**
	 * An interface to SAAMS: A Ground Operations Controller Screen: Inputs events
	 * from GOC (a person), and displays aircraft and gate information. This class
	 * is a controller for the GateInfoDatabase and the AircraftManagementDatabase:
	 * sending them messages to change the gate or aircraft status information. This
	 * class also registers as an observer of the GateInfoDatabase and the
	 * AircraftManagementDatabase, and is notified whenever any change occurs in
	 * those <<model>> elements. See written documentation.
	 *
	 * @stereotype boundary/view/controller
	 * @url element://model:project::SAAMS/design:node:::id2wdkkcko4qme4cko4svm2.node36
	 * @url element://model:project::SAAMS/design:view:::id2wdkkcko4qme4cko4svm2
	 * @url element://model:project::SAAMS/design:view:::id1un8dcko4qme4cko4sw27
	 * @url element://model:project::SAAMS/design:view:::id1bl79cko4qme4cko4sw5j
	 * @url element://model:project::SAAMS/design:view:::id15rnfcko4qme4cko4swib
	 * @url element://model:project::SAAMS/design:node:::id15rnfcko4qme4cko4swib.node107
	 */
	public GOC(AircraftManagementDatabase aircraftManagementDatabase, GateInfoDatabase gateInfoDatabase) {

		this.aircraftManagementDatabase = aircraftManagementDatabase;
		this.gateInfoDatabase = gateInfoDatabase;

		setLocation(UISettings.GOCPosition);
		setSize(UISettings.VIEW_WIDTH, UISettings.VIEW_HEIGHT);

		setTitle("GOC");

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
		model.addColumn("GATE");
		table.setModel(model);


		JPanel row1 = new JPanel();
		row1.setLayout(new BorderLayout());
		JScrollPane tableScroll = new JScrollPane(table);
		row1.add(tableScroll);
		row1.setBorder(new EmptyBorder(10, 10, 10, 10));


		// for allowing a flight to land
		JPanel row2 = new JPanel();
		row2.setLayout(new FlowLayout());
		Button grantLandingPermissionBtn = new Button("Grant Ground Clearance");
		grantLandingPermissionBtn.setSize(200, 50);
		grantLandingPermissionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				grantLandingPermission();
			}
		});
		row2.add(grantLandingPermissionBtn);

		// For assigning a gate to a flight
		JPanel row3 = new JPanel();
		row3.setLayout(new FlowLayout());
		// gate selection box
		gatesComboBox = new JComboBox<Integer>();
		gatesComboBox.addItem(1);
		gatesComboBox.addItem(2);
		gatesComboBox.addItem(3);
		// button for assigning gate
		Button assignGateBtn = new Button("Assign Gate");
		assignGateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				assignGate();
			}
		});
		assignGateBtn.setSize(200, 50);

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		// group the combo box and assignGate button
		row3.add(assignGateBtn);
		row3.add(gatesComboBox);

		// add top level panel for all components
		JPanel p = new JPanel();
		p.add(row1);
		p.add(row2);
		p.add(row3);
		BoxLayout boxLayout = new BoxLayout(p, BoxLayout.PAGE_AXIS);
		p.setLayout(boxLayout);

		contentPane.add(p);

		// ensure we observe the singleton
		aircraftManagementDatabase.addObserver(this);
		gateInfoDatabase.addObserver(this);

		setVisible(true);
	}

	/**
	 * Grants permission for a flight to land
	 */
	public void grantLandingPermission() {

		if (table.getSelectionModel().isSelectionEmpty() == true) {
			JOptionPane.showMessageDialog(null, "Please select a flight!");
			// prevent execution.
			return;
		}

		int selectedIndex = table.getSelectedRow();
		// makse sure the flight's status is WAITING_TO_LAND
		// we're checking the 3nd column
		int flightStatus = ManagementRecord.stringStatusToNumber((String)model.getValueAt(selectedIndex, 2));
		if (flightStatus != ManagementRecord.WANTING_TO_LAND) {
			JOptionPane.showMessageDialog(null, "Flight isn't wanting to land!");
			// prevent execution.
			return;
		} else {
			// todo -- this is the IN_TRANSIT scenario, set the status and wait for the flight
			//         to be lost form the radar.
		}

		// grant permission for landing and update status
		aircraftManagementDatabase.setStatus(selectedIndex, ManagementRecord.GROUND_CLEARANCE_GRANTED);
	}

	public void assignGate() {
		if (table.getSelectionModel().isSelectionEmpty() == true) {
			JOptionPane.showMessageDialog(null, "Please select a flight!");
			// prevent execution.
			return;
		}
		int selectedIndex = table.getSelectedRow();

		int flightStatus = ManagementRecord.stringStatusToNumber((String)model.getValueAt(selectedIndex, 2));
		int mCode = (int)model.getValueAt(selectedIndex, 0);

		// gate reassingment
		if (flightStatus == ManagementRecord.TAXIING) {
			int newGate = (Integer)gatesComboBox.getSelectedItem();
			// handle the gate re-assignment first
			int currentGate = (int)model.getValueAt(selectedIndex, 3);
			gateInfoDatabase.allocate((int)gatesComboBox.getSelectedItem(), mCode);
			gateInfoDatabase.reassigned(currentGate);
			model.setValueAt(newGate, selectedIndex, 3);
			return;
		}
		if (flightStatus != ManagementRecord.LANDED) {
			String msg = "Flight hasn't landed!\nCan't assign gate until flight has landed.";
			JOptionPane.showMessageDialog(null, msg);
			// prevent execution.
			return;
		}

		int[] gateStats = gateInfoDatabase.getStatuses();
		int gateNumber = (int)gatesComboBox.getSelectedItem();
		if (gateStats[gateNumber] == Gate.FREE) {
			// use this gate
			gateInfoDatabase.allocate(gateNumber, mCode);
			aircraftManagementDatabase.setStatus(mCode, ManagementRecord.TAXIING);
			model.setValueAt(gateNumber, mCode, 3);
		} else {
			String msg = "Gate [" + gateNumber + "] isn't available, please chose a different gate!";
			JOptionPane.showMessageDialog(null, msg);
		}
	}

	@Override
	public void update(Observable observable, Object o) {
		model.setRowCount(0);

		// loop over every management record
		int maxRecords = aircraftManagementDatabase.maxMRs;
		for (int i=0; i<maxRecords; i++) {
			//int flightStatus = aircraftManagementDatabase.getStatus(i);
			String flightStatus = aircraftManagementDatabase.getStringStatus(i);
			if (!flightStatus.equals("FREE")) {
				String code = aircraftManagementDatabase.getFlightCode(i);
				Integer gate = gateInfoDatabase.getGateByFlightCode(i);
				model.addRow(new Object[]{i, code, flightStatus, gate});
			}
		}

		// update the available gates
		gatesComboBox.removeAllItems();
		for (int i = 0; i < gateInfoDatabase.maxGateNumber; i++) {
			if (gateInfoDatabase.getStatus(i) == Gate.FREE) {
				gatesComboBox.addItem(i);
			}
		}

	}
}
