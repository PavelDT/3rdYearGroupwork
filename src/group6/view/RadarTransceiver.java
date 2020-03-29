package group6.view;

import java.awt.EventQueue;

import javax.swing.JDialog;

import group6.controller.AircraftManagementDatabase;
import group6.model.FlightDescriptor;
import group6.model.Itinerary;
import group6.model.ManagementRecord;
import group6.model.PassengerDetails;
import group6.model.PassengerList;
import group6.util.RNG;

import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
public class RadarTransceiver extends JDialog implements Observer {

	AircraftManagementDatabase aircraftManagementDatabase;
	private JTable table;
	private static DefaultTableModel model;
	private JTable table_1;
	private JButton button;
	private JButton button_1;
	private JButton button_2;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	FlightDescriptor fd;
	Random random = new Random();

	public RadarTransceiver(AircraftManagementDatabase aircraftManagementDatabase) {
		this.aircraftManagementDatabase = aircraftManagementDatabase;

		setBounds(100, 100, 729, 452);
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);

		JLabel label = new JLabel("Radar");
		sl_panel.putConstraint(SpringLayout.NORTH, label, 5, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, label, 295, SpringLayout.WEST, panel);
		label.setFont(new Font("Arial Black", Font.BOLD, 26));
		panel.add(label);

		button = new JButton("Enter Airspace");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				enterAirspace();
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, button, 313, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, button, -63, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, button, -541, SpringLayout.EAST, panel);
		panel.add(button);

		button_1 = new JButton("Leave Airspace");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				leaveAirspace();
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, button_1, 0, SpringLayout.NORTH, button);
		sl_panel.putConstraint(SpringLayout.WEST, button_1, 30, SpringLayout.EAST, button);
		sl_panel.putConstraint(SpringLayout.SOUTH, button_1, -63, SpringLayout.SOUTH, panel);
		panel.add(button_1);

		button_2 = new JButton("View Passenger List");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				viewPassengerList();
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, button_2, 0, SpringLayout.NORTH, button);
		sl_panel.putConstraint(SpringLayout.WEST, button_2, 33, SpringLayout.EAST, button_1);
		sl_panel.putConstraint(SpringLayout.SOUTH, button_2, -63, SpringLayout.SOUTH, panel);
		panel.add(button_2);

		textArea = new JTextArea();
		sl_panel.putConstraint(SpringLayout.NORTH, textArea, 78, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, textArea, 24, SpringLayout.EAST, button_2);
		sl_panel.putConstraint(SpringLayout.SOUTH, textArea, -63, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, textArea, -23, SpringLayout.EAST, panel);
		panel.add(textArea);

		JLabel label_1 = new JLabel("Passenger List");
		sl_panel.putConstraint(SpringLayout.SOUTH, label_1, -6, SpringLayout.NORTH, textArea);
		sl_panel.putConstraint(SpringLayout.EAST, label_1, -85, SpringLayout.EAST, panel);
		label_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(label_1);

		scrollPane = new JScrollPane();
		sl_panel.putConstraint(SpringLayout.NORTH, scrollPane, 102, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, scrollPane, 69, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, scrollPane, -7, SpringLayout.NORTH, button);
		sl_panel.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, button_1);
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
		model.addColumn("AIRCRAFT");
		model.addColumn("STATUS");
		table.setModel(model);

		scrollPane.setViewportView(table);

		setVisible(true);

		aircraftManagementDatabase.addObserver(this);
	}

	/**
	 * Uses RNG class to create an itinerary and then sneding the FlightDescriptor
	 * to the aircraftManagementDatabase
	 */
	private void enterAirspace() {

		 fd = RNG.generateFlightDescriptor();

		
		aircraftManagementDatabase.radarDetect(fd);

	}

	// THIS NEEDS TO LOOP THROUGH THE AircraftManagementDatabase AND FIND THE FLIGHT
	// CODE OF THE FLIGHT
	// IT THEN NEEDS TO CALL METHOD IN AircraftManagementDatabase CALLED
	// radarLostConatact and pass through the flight code
	private void leaveAirspace() {
		int mCode = 0;

		aircraftManagementDatabase.radarLostContact(mCode);
	}

	// NEED TO GET THE PASSENGERLIST FROM THE SELECTED ROW OF THE JLIST AND DISPLAY
	// TO JTEXT AREA
	private void viewPassengerList() {

		if (table.getSelectionModel().isSelectionEmpty() == true) {
			JOptionPane.showMessageDialog(null, "Please select a flight!");
		}

		else {

			// remove details from previous flights
			textArea.setText("");

			PassengerList passengerList = aircraftManagementDatabase.getPassengerList(table.getSelectedRow());

			// for all the passengers on the flight add them to the text field as an
			// individual line
			for (PassengerDetails passenger : passengerList.getPassengerDetails()) {
				textArea.append(passenger.getName() + "\n");
			}
		}
	}

	@Override
	public void update(Observable observable, Object o) {
		// reset the flights wanting to land list
		// model.setRowCount(0);

		// when the observer (this UI) is updated
		// update the UI to display all flights wanting to land.
		for (int i : aircraftManagementDatabase.getWithStatus(ManagementRecord.WANTING_TO_LAND)) {
			
			Itinerary itinerary = aircraftManagementDatabase.getItinerary(i);
			
			model.addRow(new Object[] { fd.getFlightCode(), "Wanting to Land" });

//			String flightDetails = aircraftManagementDatabase.getFlightCode(i) + " | " + " F: " + itinerary.getFrom()
//					+ " T: " + itinerary.getTo() + " N: " + itinerary.getNext();

			// add by being explicit on the index, allows us to use the MANAGEMENT RECORD
			// index
			// for the list model index too


		}
	}
}
