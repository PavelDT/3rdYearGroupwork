package group6.view;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import group6.controller.AircraftManagementDatabase;
import group6.controller.GateInfoDatabase;
import group6.model.Gate;
import group6.model.ManagementRecord;
import group6.util.UISettings;

/**
 * 
 * This Class represents the GOC The GOC (Ground Operations Controller) gives
 * ground clearance to the incoming flights
 *
 */
public class GOC extends JFrame implements Observer {

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

	/**
	 * The Ground Operations Controller Screen interface has access to the
	 * GateInfoDatabase.
	 *
	 * @clientCardinality 1
	 * @supplierCardinality 1
	 * @label accesses/observes
	 * @directed
	 */
	private GateInfoDatabase gateInfoDatabase;

	/**
	 * Instance of JTable
	 */
	private JTable table;

	/**
	 * Instance of JComboBox Conatains all the available gates
	 */
	private JComboBox<Integer> gatesComboBox;

	/**
	 * Instance of DefaultTableModel
	 */
	private DefaultTableModel model;

	/**
	 * Instance of JButton Assign Gate
	 */
	private JButton btnAssignGate;

	/**
	 * Instance of JButton Grant Ground Clearance
	 */
	private JButton btnGrantGroundClearance;

	/**
	 * Instance of JButton Take Off
	 */
	private JButton takeOffBtn;

	/**
	 * Instance of JButton Taxi the Plane
	 */
	private JButton taxiBtn;

	/**
	 * Instance of JPanel
	 */
	private JPanel panel;

	/**
	 * Instance of JScrollPane
	 */
	private JScrollPane scrollPane;

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

		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);

		scrollPane = new JScrollPane();
		sl_panel.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, scrollPane, 186, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, scrollPane, 413, SpringLayout.WEST, panel);
		panel.add(scrollPane);

		//Make model selectable but not editable
		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable();
		table.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		table.getTableHeader().setReorderingAllowed(false);
		table.setModel(model);
		// add listener for when a row is clicked in the table
		table.getSelectionModel().addListSelectionListener(event -> onRowClick());
		TableColumnModel columnModel = table.getColumnModel();

		model.addColumn("ID");
		model.addColumn("AIRCRAFT");
		model.addColumn("STATUS");
		model.addColumn("GATE");

		columnModel.getColumn(0).setPreferredWidth(0);
		columnModel.getColumn(1).setPreferredWidth(0);
		columnModel.getColumn(2).setPreferredWidth(120);
		columnModel.getColumn(3).setPreferredWidth(5);
		scrollPane.setViewportView(table);

		btnGrantGroundClearance = new JButton("Grant Ground Clearance");
		btnGrantGroundClearance.addActionListener(e -> grantLandingPermission());
		sl_panel.putConstraint(SpringLayout.NORTH, btnGrantGroundClearance, 14, SpringLayout.SOUTH, scrollPane);
		sl_panel.putConstraint(SpringLayout.WEST, btnGrantGroundClearance, 0, SpringLayout.WEST, scrollPane);
		panel.add(btnGrantGroundClearance);

		btnAssignGate = new JButton("Assign Gate");
		btnAssignGate.addActionListener(e -> assignGate());
		sl_panel.putConstraint(SpringLayout.NORTH, btnAssignGate, 0, SpringLayout.NORTH, btnGrantGroundClearance);
		sl_panel.putConstraint(SpringLayout.WEST, btnAssignGate, 46, SpringLayout.EAST, btnGrantGroundClearance);
		panel.add(btnAssignGate);

		gatesComboBox = new JComboBox<Integer>();
		gatesComboBox.addItem(0);
		gatesComboBox.addItem(1);
		gatesComboBox.addItem(2);

		sl_panel.putConstraint(SpringLayout.WEST, gatesComboBox, 6, SpringLayout.EAST, btnAssignGate);
		sl_panel.putConstraint(SpringLayout.SOUTH, gatesComboBox, 0, SpringLayout.SOUTH, btnGrantGroundClearance);
		panel.add(gatesComboBox);

		taxiBtn = new JButton("Taxi");
		taxiBtn.addActionListener(e -> taxiFlight());
		sl_panel.putConstraint(SpringLayout.NORTH, taxiBtn, 6, SpringLayout.SOUTH, btnGrantGroundClearance);
		sl_panel.putConstraint(SpringLayout.WEST, taxiBtn, 0, SpringLayout.WEST, scrollPane);
		panel.add(taxiBtn);

		takeOffBtn = new JButton("Takeoff");
		takeOffBtn.addActionListener(e -> takeOff());
		sl_panel.putConstraint(SpringLayout.SOUTH, takeOffBtn, 0, SpringLayout.SOUTH, taxiBtn);
		sl_panel.putConstraint(SpringLayout.EAST, takeOffBtn, 0, SpringLayout.EAST, btnGrantGroundClearance);
		panel.add(takeOffBtn);

		aircraftManagementDatabase.addObserver(this);
		gateInfoDatabase.addObserver(this);

		// disable all controls until a row is selected from the table
		disableAll();

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
//		int flightStatus = ManagementRecord.stringStatusToNumber((String) model.getValueAt(selectedIndex, 2));

		int mCode = (int) model.getValueAt(selectedIndex, 0);
		int flightStatus = aircraftManagementDatabase.getStatus(mCode);

		if (flightStatus != ManagementRecord.WANTING_TO_LAND) {
			JOptionPane.showMessageDialog(null, "Flight isn't wanting to land!");
			// prevent execution.
			return;
		}

		// grant permission for landing and update status
		aircraftManagementDatabase.setStatus(mCode, ManagementRecord.GROUND_CLEARANCE_GRANTED);
	}

	/**
	 * Assigsn a gate to grounded aircraft
	 */
	public void assignGate() {
		if (table.getSelectionModel().isSelectionEmpty() == true) {
			JOptionPane.showMessageDialog(null, "Please select a flight!");
			// prevent execution.
			return;
		}
		int selectedIndex = table.getSelectedRow();

		int flightStatus = ManagementRecord.stringStatusToNumber((String) model.getValueAt(selectedIndex, 2));
		int mCode = (int) model.getValueAt(selectedIndex, 0);

		// gate reassingment
		if (flightStatus == ManagementRecord.TAXIING) {
			int newGate = (Integer) gatesComboBox.getSelectedItem();
			// handle the gate re-assignment first
			int currentGate = (int) model.getValueAt(selectedIndex, 3);
			gateInfoDatabase.allocate((int) gatesComboBox.getSelectedItem(), mCode);
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
		int gateNumber = (int) gatesComboBox.getSelectedItem();
		if (gateStats[gateNumber] == Gate.FREE) {
			// use this gate
			gateInfoDatabase.allocate(gateNumber, mCode);
			aircraftManagementDatabase.setStatus(mCode, ManagementRecord.TAXIING);
		} else {
			String msg = "Gate [" + gateNumber + "] isn't available, please chose a different gate!";
			JOptionPane.showMessageDialog(null, msg);
		}
	}

	/**
	 * Taxi the aircraft
	 */
	private void taxiFlight() {
		if (table.getSelectionModel().isSelectionEmpty() == true) {
			JOptionPane.showMessageDialog(null, "Please select a flight!");
			// prevent execution.
			return;
		}

		int selectedIndex = table.getSelectedRow();
		int flightStatus = ManagementRecord.stringStatusToNumber((String) model.getValueAt(selectedIndex, 2));
		int mCode = (int) model.getValueAt(selectedIndex, 0);
		if (flightStatus == ManagementRecord.AWAITING_TAXI) {
			// update status to AWAITING_TAXI
			aircraftManagementDatabase.setStatus(mCode, ManagementRecord.AWAITING_TAKEOFF);
		} else {
			JOptionPane.showMessageDialog(null, "Flight not ready to taxi!");
			// prevent execution.
			return;
		}
	}

	/**
	 * Allow plane to depart
	 */
	private void takeOff() {
		if (table.getSelectionModel().isSelectionEmpty() == true) {
			JOptionPane.showMessageDialog(null, "Please select a flight!");
			// prevent execution.
			return;
		}

		int selectedIndex = table.getSelectedRow();
		int flightStatus = ManagementRecord.stringStatusToNumber((String) model.getValueAt(selectedIndex, 2));
		int mCode = (int) model.getValueAt(selectedIndex, 0);
		if (flightStatus == ManagementRecord.AWAITING_TAKEOFF) {
			// update status to AWAITING_TAXI
			aircraftManagementDatabase.setStatus(mCode, ManagementRecord.DEPARTING_THROUGH_LOCAL_AIRSPACE);
			// get gate number
			int gateOfFlight = gateInfoDatabase.getGateByFlightCode(mCode);
			gateInfoDatabase.departed(gateOfFlight);
		} else {
			JOptionPane.showMessageDialog(null, "Flight not ready to take off!");
			// prevent execution.
			return;
		}
	}

	/**
	 * Event Listeners for JTable
	 */
	private void onRowClick() {
		disableAll();

		// enable buttons based on flight status
		int selectedIndex = table.getSelectedRow();
		if (selectedIndex == -1) {
			// no item selected, prevent any buttons from being used.
			return;
		}

		int planeStatus = aircraftManagementDatabase.getStatus((int) model.getValueAt(selectedIndex, 0));

		if (planeStatus == ManagementRecord.WANTING_TO_LAND) {
			btnGrantGroundClearance.setEnabled(true);
		} else if (planeStatus == ManagementRecord.LANDED) {
			btnAssignGate.setEnabled(true);
			gatesComboBox.setEnabled(true);
		} else if (planeStatus == ManagementRecord.AWAITING_TAXI) {
			taxiBtn.setEnabled(true);
		} else if (planeStatus == ManagementRecord.AWAITING_TAKEOFF) {
			takeOffBtn.setEnabled(true);
		}
	}

	/**
	 * Updates the view
	 */
	@Override
	public void update(Observable observable, Object o) {

		model.setRowCount(0);

		// loop over every management record
		int maxRecords = aircraftManagementDatabase.maxMRs;
		for (int i = 0; i < maxRecords; i++) {
			String flightStatus = aircraftManagementDatabase.getStringStatus(i);
			if (!flightStatus.equals("FREE") && !flightStatus.equals("IN TRANSIT")) {
				String code = aircraftManagementDatabase.getFlightCode(i);
				Integer gate = gateInfoDatabase.getGateByFlightCode(i);
				model.addRow(new Object[] { i, code, flightStatus, gate });
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

	/**
	 * Disables all buttons
	 */
	public void disableAll() {
		btnGrantGroundClearance.setEnabled(false);
		btnAssignGate.setEnabled(false);
		gatesComboBox.setEnabled(false);
		taxiBtn.setEnabled(false);
		takeOffBtn.setEnabled(false);
	}
}
