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
import javax.swing.table.TableColumnModel;

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
	private JButton btnAssignGate;
	private JButton btnGrantGroundClearance;
	private JPanel panel;
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
		btnGrantGroundClearance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				grantLandingPermission();
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, btnGrantGroundClearance, 14, SpringLayout.SOUTH, scrollPane);
		sl_panel.putConstraint(SpringLayout.WEST, btnGrantGroundClearance, 0, SpringLayout.WEST, scrollPane);
		panel.add(btnGrantGroundClearance);

		btnAssignGate = new JButton("Assign Gate");
		btnAssignGate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				assignGate();
			}
		});
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
		
		JButton taxiBtn = new JButton("Taxi");
		taxiBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//handleTaxi()
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, taxiBtn, 6, SpringLayout.SOUTH, btnGrantGroundClearance);
		sl_panel.putConstraint(SpringLayout.WEST, taxiBtn, 0, SpringLayout.WEST, scrollPane);
		panel.add(taxiBtn);
		
		JButton takeOffBtn = new JButton("Takeoff");
		takeOffBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//handleTakeOff()
			}
		});
		sl_panel.putConstraint(SpringLayout.SOUTH, takeOffBtn, 0, SpringLayout.SOUTH, taxiBtn);
		sl_panel.putConstraint(SpringLayout.EAST, takeOffBtn, 0, SpringLayout.EAST, btnGrantGroundClearance);
		panel.add(takeOffBtn);

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
		int flightStatus = ManagementRecord.stringStatusToNumber((String) model.getValueAt(selectedIndex, 2));
		if (flightStatus != ManagementRecord.WANTING_TO_LAND) {
			JOptionPane.showMessageDialog(null, "Flight isn't wanting to land!");
			// prevent execution.
			return;
		} else {
			// todo -- this is the IN_TRANSIT scenario, set the status and wait for the
			// flight
			// to be lost form the radar.
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
		for (int i = 0; i < maxRecords; i++) {
			// int flightStatus = aircraftManagementDatabase.getStatus(i);
			String flightStatus = aircraftManagementDatabase.getStringStatus(i);
			if (!flightStatus.equals("FREE")) {
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
}

