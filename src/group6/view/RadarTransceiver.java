package group6.view;

import java.awt.EventQueue;

import javax.swing.JDialog;

import group6.controller.AircraftManagementDatabase;
import group6.model.Itinerary;
import group6.model.ManagementRecord;
import group6.model.PassengerDetails;
import group6.model.PassengerList;
import group6.util.UISettings;

import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;

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
public class RadarTransceiver extends JDialog implements ListSelectionListener, Observer {

	AircraftManagementDatabase aircraftManagementDatabase;
	private JButton btnEnter;
	private JButton btnLeave;
	private JButton btnPassengers;
	private JTextArea textArea;
	private JList list;
	private DefaultListModel<String> listModel;

	public RadarTransceiver(AircraftManagementDatabase aircraftManagementDatabase) {
		this.aircraftManagementDatabase = aircraftManagementDatabase;

		setBounds(UISettings.RadarTransceiverBound);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);

		JLabel lblTitle = new JLabel("Radar");
		springLayout.putConstraint(SpringLayout.NORTH, lblTitle, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblTitle, 312, SpringLayout.WEST, getContentPane());
		lblTitle.setFont(new Font("Arial Black", Font.BOLD, 26));
		getContentPane().add(lblTitle);

		btnEnter = new JButton("Enter Airspace");
		springLayout.putConstraint(SpringLayout.NORTH, btnEnter, 337, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, btnEnter, 63, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, btnEnter, -32, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnEnter, 189, SpringLayout.WEST, getContentPane());
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enterAirspace();
			}

		});
		getContentPane().add(btnEnter);

		btnLeave = new JButton("Leave Airspace");
		springLayout.putConstraint(SpringLayout.NORTH, btnLeave, 0, SpringLayout.NORTH, btnEnter);
		springLayout.putConstraint(SpringLayout.WEST, btnLeave, 6, SpringLayout.EAST, btnEnter);
		springLayout.putConstraint(SpringLayout.SOUTH, btnLeave, 44, SpringLayout.NORTH, btnEnter);
		springLayout.putConstraint(SpringLayout.EAST, btnLeave, 132, SpringLayout.EAST, btnEnter);
		btnLeave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				leaveAirspace();
			}

		});
		getContentPane().add(btnLeave);

		btnPassengers = new JButton("View Passenger List");
		btnPassengers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewPassengerList();
			}

		});
		springLayout.putConstraint(SpringLayout.NORTH, btnPassengers, 0, SpringLayout.NORTH, btnEnter);
		springLayout.putConstraint(SpringLayout.WEST, btnPassengers, 6, SpringLayout.EAST, btnLeave);
		springLayout.putConstraint(SpringLayout.SOUTH, btnPassengers, 0, SpringLayout.SOUTH, btnEnter);
		getContentPane().add(btnPassengers);

		textArea = new JTextArea();
		springLayout.putConstraint(SpringLayout.NORTH, textArea, 81, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, textArea, 22, SpringLayout.EAST, btnPassengers);
		springLayout.putConstraint(SpringLayout.SOUTH, textArea, -32, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, textArea, -39, SpringLayout.EAST, getContentPane());
		getContentPane().add(textArea);

		JLabel lblPassengerList = new JLabel("Passenger List");
		springLayout.putConstraint(SpringLayout.SOUTH, lblPassengerList, -7, SpringLayout.NORTH, textArea);
		springLayout.putConstraint(SpringLayout.EAST, lblPassengerList, -95, SpringLayout.EAST, getContentPane());
		lblPassengerList.setFont(new Font("Tahoma", Font.BOLD, 11));
		getContentPane().add(lblPassengerList);

		JScrollPane scrollPane = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 0, SpringLayout.NORTH, textArea);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, btnEnter);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -6, SpringLayout.NORTH, btnEnter);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, btnLeave);
		getContentPane().add(scrollPane);

		//still working on list
		listModel = new DefaultListModel<String>();
		list = new JList(listModel);
		scrollPane.setViewportView(list);
		// observer of the aircraft db
		aircraftManagementDatabase.addObserver(this);

		setVisible(true);

	}

	// I NEED THIS METHOD TO SIMULATE AN AIRCRAFT ENTERING THE AIRSPACE
	// IT NEEDS TO DOWNLAOD THE FLIGHT DESCRIPTOR OF THIS AIRCRAFT AND SEND IT TO
	// THE AircraftManagementDatabase THROUGH THE METHOD radarConatact()
	private void enterAirspace() {

		
	}

	// THIS NEEDS TO LOOP THROUGH THE AircraftManagementDatabase AND FIND THE FLIGHT
	// CODE OF THE FLIGHT
	// IT THEN NEEDS TO CALL METHOD IN AircraftManagementDatabase CALLED
	// radarLostConatact and pass through the flight code
	private void leaveAirspace() {

	}

	// NEED TO GET THE PASSENGERLIST FROM THE SELECTED ROW OF THE JLIST AND DISPLAY
	// TO JTEXT AREA
	private void viewPassengerList() {


		if(list.isSelectionEmpty())
		{
			JOptionPane.showMessageDialog(null, "Please select a flight!");
		}
		else {
			// remove details from previous flights
			textArea.setText("");

			PassengerList passengerList = aircraftManagementDatabase.getPassengerList(list.getSelectedIndex());
			// for all the passengers on the flight add them to the text field as an individual line
			for (PassengerDetails passenger : passengerList.getPassengerDetails()) {
				textArea.append(passenger.getName() + "\n");
			}
		}
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		
	}

	@Override
	public void update(Observable observable, Object o) {
		// reset the flights wanting to land list
		listModel.removeAllElements();

		// when the observer (this UI) is updated
		// update the UI to display all flights wanting to land.
		for (int i : aircraftManagementDatabase.getWithStatus(ManagementRecord.WANTING_TO_LAND)) {

			Itinerary itinerary = aircraftManagementDatabase.getItinerary(i);

			String flightDetails = aircraftManagementDatabase.getFlightCode(i) + " | " +
					               " F: " + itinerary.getFrom() +
					               " T: " + itinerary.getTo() +
					               " N: " + itinerary.getNext();

			// add by being explicit on the index, allows us to use the MANAGEMENT RECORD index
			// for the list model index too
			listModel.add(i, flightDetails);

		}
	}
}
