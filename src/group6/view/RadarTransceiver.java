package group6.view;

import javax.swing.*;

import group6.controller.AircraftManagementDatabase;
import group6.model.FlightDescriptor;
import group6.model.Itinerary;
import group6.model.ManagementRecord;
import group6.model.PassengerDetails;
import group6.model.PassengerList;
import group6.util.RNG;
import group6.util.UISettings;

import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.Font;

import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;

/**
 * An interface to SAAMS: Radar tracking of arriving and departing aircraft, and
 * transceiver for downloading of flight descriptors (by aircraft entering the
 * local airspace) and uploading of passenger lists (to aircraft about to
 * depart). A screen simulation of the radar/transceiver system. This class is a
 * controller for the AircraftManagementDatabase: it sends messages to notify
 * when a new aircraft is detected (message contains a FlightDescriptor), and
 * when radar contact with an aircraft is lost. It also registers as an observer
 * of the AircraftManagementDatabase, and is notified whenever any change occurs
 * in that <<model>> element. See written documentation.
 * 
 * @stereotype boundary/view/controller
 * @url element://model:project::SAAMS/design:view:::idwwyucko4qme4cko4sgxi
 * @url element://model:project::SAAMS/design:node:::id15rnfcko4qme4cko4swib.node107
 * @url element://model:project::SAAMS/design:node:::id3oolzcko4qme4cko4sx40.node167
 * @url element://model:project::SAAMS/design:view:::id3oolzcko4qme4cko4sx40
 * @url element://model:project::SAAMS/design:view:::id15rnfcko4qme4cko4swib
 */
public class RadarTransceiver extends JFrame implements Observer {

	/**
	 * Instance of AircraftManagementDatabase
	 */
	AircraftManagementDatabase aircraftManagementDatabase;

	/**
	 * Instance of JTable
	 */
	private JTable table;

	/**
	 * Instance of DefaultTableModel
	 */
	private static DefaultTableModel model;

	/**
	 * Instance of JButton
	 */
	private JButton btnEnter;

	/**
	 * Instance of JButton
	 */
	private JButton btnLeave;

	/**
	 * Instance of JButton
	 */
	private JButton btnViewPassenger;

	/**
	 * Instance of JScrollPane
	 */
	private JScrollPane scrollPane;

	/**
	 * Instance of JTextArea
	 */
	private JTextArea textArea;

	/**
	 * Constructor
	 * 
	 * @param aircraftManagementDatabase
	 */
	public RadarTransceiver(AircraftManagementDatabase aircraftManagementDatabase) {
		this.aircraftManagementDatabase = aircraftManagementDatabase;

		setBounds(UISettings.RadarTransceiverBound);
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);

		JLabel label = new JLabel("Radar");
		sl_panel.putConstraint(SpringLayout.WEST, label, 207, SpringLayout.WEST, panel);
		label.setFont(new Font("Arial Black", Font.BOLD, 26));
		panel.add(label);

		btnEnter = new JButton("Enter Airspace");
		sl_panel.putConstraint(SpringLayout.SOUTH, btnEnter, -47, SpringLayout.SOUTH, panel);
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				enterAirspace();

			}
		});
		panel.add(btnEnter);

		btnLeave = new JButton("Leave Airspace");
		sl_panel.putConstraint(SpringLayout.NORTH, btnLeave, 0, SpringLayout.NORTH, btnEnter);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnLeave, -47, SpringLayout.SOUTH, panel);
		btnLeave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				leaveAirspace();
			}
		});
		panel.add(btnLeave);

		btnViewPassenger = new JButton("View Passenger List");
		sl_panel.putConstraint(SpringLayout.NORTH, btnViewPassenger, 0, SpringLayout.NORTH, btnEnter);
		sl_panel.putConstraint(SpringLayout.WEST, btnViewPassenger, 59, SpringLayout.EAST, btnLeave);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnViewPassenger, -47, SpringLayout.SOUTH, panel);
		btnViewPassenger.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				viewPassengerList();
			}
		});
		panel.add(btnViewPassenger);

		JLabel label_1 = new JLabel("Passenger List");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(label_1);

		scrollPane = new JScrollPane();
		sl_panel.putConstraint(SpringLayout.NORTH, label_1, 1, SpringLayout.NORTH, scrollPane);
		sl_panel.putConstraint(SpringLayout.WEST, label_1, 71, SpringLayout.EAST, scrollPane);
		sl_panel.putConstraint(SpringLayout.EAST, btnLeave, 0, SpringLayout.EAST, scrollPane);
		sl_panel.putConstraint(SpringLayout.NORTH, scrollPane, 78, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, scrollPane, -90, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.NORTH, btnEnter, 6, SpringLayout.SOUTH, scrollPane);
		sl_panel.putConstraint(SpringLayout.WEST, btnEnter, 0, SpringLayout.WEST, scrollPane);
		sl_panel.putConstraint(SpringLayout.SOUTH, label, -20, SpringLayout.NORTH, scrollPane);
		sl_panel.putConstraint(SpringLayout.WEST, scrollPane, 69, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, scrollPane, -246, SpringLayout.EAST, panel);
		panel.add(scrollPane);

		model = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(model);
		table.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		table.getTableHeader().setReorderingAllowed(false);
		TableColumnModel columnModel = table.getColumnModel();

		model.addColumn("ID");
		model.addColumn("AIRCRAFT");
		model.addColumn("DESCRIPTION");
		model.addColumn("TIME");

		columnModel.getColumn(0).setPreferredWidth(1);
		columnModel.getColumn(1).setPreferredWidth(5);
		columnModel.getColumn(2).setPreferredWidth(120);
		columnModel.getColumn(3).setPreferredWidth(5);

		table.setModel(model);

		scrollPane.setViewportView(table);

		setVisible(true);

		btnLeave.setEnabled(false);
		btnViewPassenger.setEnabled(false);

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		sl_panel.putConstraint(SpringLayout.NORTH, textArea, 6, SpringLayout.SOUTH, label_1);
		sl_panel.putConstraint(SpringLayout.SOUTH, textArea, -6, SpringLayout.NORTH, btnViewPassenger);
		sl_panel.putConstraint(SpringLayout.WEST, textArea, 38, SpringLayout.EAST, scrollPane);
		sl_panel.putConstraint(SpringLayout.EAST, textArea, 202, SpringLayout.EAST, scrollPane);
		textArea.setVisible(true);
		panel.add(textArea);

		aircraftManagementDatabase.addObserver(this);

		// TODO change colours of rows if planes are just passing by or in transit

		table.getSelectionModel().addListSelectionListener(e -> {
			ListSelectionModel lsm = (ListSelectionModel) e.getSource();

			if (!lsm.isSelectionEmpty()) {
				int mCode = (int) model.getValueAt(table.getSelectedRow(), 0);
				if (aircraftManagementDatabase.getStatus(mCode) == ManagementRecord.DEPARTING_THROUGH_LOCAL_AIRSPACE
						|| aircraftManagementDatabase.getStatus(mCode) == ManagementRecord.IN_TRANSIT) {
					btnLeave.setEnabled(true);
				}
				btnViewPassenger.setEnabled(true);

			} else {
				btnLeave.setEnabled(false);
				btnViewPassenger.setEnabled(false);
			}
		});
	}

	/**
	 * Uses RNG class to create an itinerary and then sending the FlightDescriptor
	 * to the aircraftManagementDatabase
	 */
	private void enterAirspace() {

		boolean foundSlot = aircraftManagementDatabase.radarDetect(RNG.generateFlightDescriptor());
		if (!foundSlot) {
			JOptionPane.showMessageDialog(null, "There are no free management records.\nCannot accept flight!");
		}
	}

	/**
	 * Aircraft Leaving the airpsace
	 */
	private void leaveAirspace() {

		if (table.getSelectionModel().isSelectionEmpty() == true) {
			JOptionPane.showMessageDialog(null, "Please select a flight!");
			return;
		}

		// fetch the first value of the currently selected row
		// which represents the id of the flight aka mCode
		int mCode = (int) model.getValueAt(table.getSelectedRow(), 0);
		aircraftManagementDatabase.radarLostContact(mCode);
		textArea.setText("");
	}

	/**
	 * Views the passenger List
	 */
	private void viewPassengerList() {

		if (table.getSelectionModel().isSelectionEmpty() == true) {
			JOptionPane.showMessageDialog(this, "Please select a flight!");
			return;
		}

		// fetch the first value of the currently selected row
		// which represents the id of the flight aka mCode
		int mCode = (int) model.getValueAt(table.getSelectedRow(), 0);
		// remove details from previous flights
		textArea.setText("");

		PassengerList passengerList = aircraftManagementDatabase.getPassengerList(mCode);

		// for all the passengers on the flight add them to the text field as an
		// individual line
		for (PassengerDetails passenger : passengerList.getPassengerDetails()) {
			textArea.append(passenger.getName() + "\n");
		}

	}

	/**
	 * Updates the view
	 */
	@Override
	public void update(Observable observable, Object o) {
		// reset the flights wanting to land list
		model.setRowCount(0);

		// when the observer (this UI) is updated
		// update the UI to display all flights wanting to land.
		for (int i : aircraftManagementDatabase.getWithStatus(ManagementRecord.WANTING_TO_LAND)) {

			String flightCode = aircraftManagementDatabase.getFlightCode(i);
			Itinerary itinerary = aircraftManagementDatabase.getItinerary(i);
			PassengerList passengerList = aircraftManagementDatabase.getPassengerList(i);

			FlightDescriptor fd = new FlightDescriptor(flightCode, itinerary, passengerList);

			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			// problem, all the times get updated
			model.addRow(new Object[] { i, fd.getFlightCode(), fd.toString(), formatter.format(date) });
		}

		for (int i : aircraftManagementDatabase.getWithStatus(ManagementRecord.DEPARTING_THROUGH_LOCAL_AIRSPACE)) {

			String flightCode = aircraftManagementDatabase.getFlightCode(i);
			Itinerary itinerary = aircraftManagementDatabase.getItinerary(i);
			PassengerList passengerList = aircraftManagementDatabase.getPassengerList(i);

			FlightDescriptor fd = new FlightDescriptor(flightCode, itinerary, passengerList);

			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();

			model.addRow(new Object[] { i, fd.getFlightCode(), fd.toString(), formatter.format(date) });
		}

		for (int i : aircraftManagementDatabase.getWithStatus(ManagementRecord.IN_TRANSIT)) {

			String flightCode = aircraftManagementDatabase.getFlightCode(i);
			Itinerary itinerary = aircraftManagementDatabase.getItinerary(i);
			PassengerList passengerList = aircraftManagementDatabase.getPassengerList(i);

			FlightDescriptor fd = new FlightDescriptor(flightCode, itinerary, passengerList);

			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();

			model.addRow(new Object[] { i, fd.getFlightCode(), fd.toString(), formatter.format(date) });
		}
	}
}
