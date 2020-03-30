package group6.view;
// Generated by Together

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import group6.model.Itinerary;
import group6.model.ManagementRecord;
import group6.model.PassengerDetails;
import group6.controller.AircraftManagementDatabase;
import group6.controller.GateInfoDatabase;
import group6.util.UISettings;

/**
 * An interface to SAAMS: Gate Control Console: Inputs events from gate staff,
 * and displays aircraft and gate information. This class is a controller for
 * the GateInfoDatabase and the AircraftManagementDatabase: sends messages when
 * aircraft dock, have finished disembarking, and are fully emarked and ready to
 * depart. This class also registers as an observer of the GateInfoDatabase and
 * the AircraftManagementDatabase, and is notified whenever any change occurs in
 * those <<model>> elements. See written documentation.
 *
 * @stereotype boundary/view/controller
 * @url element://model:project::SAAMS/design:view:::id1un8dcko4qme4cko4sw27
 * @url element://model:project::SAAMS/design:view:::id1jkohcko4qme4cko4svww
 * @url element://model:project::SAAMS/design:node:::id1un8dcko4qme4cko4sw27.node61
 */
public class GateConsole extends JFrame implements Observer, ActionListener {
	/**
	 * The GateConsole interface has access to the GateInfoDatabase.
	 *
	 * @supplierCardinality 1
	 * @clientCardinality 0..*
	 * @label accesses/observes
	 * @directed
	 */
	GateInfoDatabase gateInfoDatabase;

	/**
	 * The GateConsole interface has access to the AircraftManagementDatabase.
	 *
	 * @supplierCardinality 1
	 * @clientCardinality 0..*
	 * @directed
	 * @label accesses/observes
	 */
	private AircraftManagementDatabase aircraftManagementDatabase;

	/**
	 * This gate's gateNumber - for identifying this gate's information in the
	 * GateInfoDatabase.
	 */
	private int gateNumber;

	private int currentRecord = -1;

	private JButton flightDocked;
	private JButton flightUnloaded;
	private JButton addPassenger;
	private JButton flightLoaded;
	private JLabel flightCode;
	private JLabel flightStatus;
	private JLabel flightDestination;
	private JLabel gateStatus;
	private JTextArea gateInformation;

	/**
	 * Class constructor
	 * @param aircraftManagementDatabase
	 * @param gateInfoDatabase
	 * @param gateNumber
	 */
	public GateConsole(AircraftManagementDatabase aircraftManagementDatabase, GateInfoDatabase gateInfoDatabase,
					   int gateNumber) {

		this.aircraftManagementDatabase = aircraftManagementDatabase;

		this.gateInfoDatabase = gateInfoDatabase;

		this.gateNumber = gateNumber;

		setTitle("Gate Console " + gateNumber);
		if(gateNumber == 0)
		{
			setLocation(UISettings.GateConsolePosition0);
		}
		else if(gateNumber == 1)
		{
			setLocation(UISettings.GateConsolePosition1);
		}
		else if(gateNumber == 2)
		{
			setLocation(UISettings.GateConsolePosition2);
		}
		setSize(UISettings.VIEW_WIDTH, UISettings.VIEW_HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Container window = getContentPane();
		window.setLayout(new BoxLayout(window, BoxLayout.PAGE_AXIS));

		JPanel row0 = new JPanel();
		row0.setBorder(new EmptyBorder(0, 0, 10, 0));
		row0.setLayout(new FlowLayout());
		JLabel gateLabel = new JLabel();
		gateLabel.setFont(new Font(gateLabel.getName(), Font.PLAIN, 20));
		gateLabel.setText("Gate " + gateNumber);
		row0.add(gateLabel);

		JPanel row1 = new JPanel();
		row1.setLayout(new GridLayout());

		flightDocked = new JButton("Plane Docked");
		row1.add(flightDocked);
		flightDocked.addActionListener(this);
		flightDocked.setEnabled(false);

		flightUnloaded = new JButton("Plane Unloaded");
		row1.add(flightUnloaded);
		flightUnloaded.addActionListener(this);
		flightUnloaded.setEnabled(false);

		addPassenger = new JButton("Add Passenger");
		row1.add(addPassenger);
		addPassenger.addActionListener(this);
		addPassenger.setEnabled(false);

		flightLoaded = new JButton("Plane Loaded");
		row1.add(flightLoaded);
		flightLoaded.addActionListener(this);
		flightLoaded.setEnabled(false);

		JPanel row2 = new JPanel();
		row2.setLayout(new FlowLayout());
		gateInformation = new JTextArea(5,20);
		gateInformation.setEditable(false);

		JScrollPane scroll = new JScrollPane(gateInformation);
		row2.add(scroll, null);

		JPanel row3 = new JPanel();
		row3.setLayout(new FlowLayout());

		flightCode = new JLabel();
		row3.add(flightCode);
		flightStatus = new JLabel();
		row3.add(flightStatus);
		flightDestination = new JLabel();
		row3.add(flightDestination);
		gateStatus = new JLabel();
		row3.add(gateStatus);

		window.add(row0);
		window.add(row1);
		window.add(row2);
		window.add(row3);

		aircraftManagementDatabase.addObserver(this);
		gateInfoDatabase.addObserver(this);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == flightDocked) {
			dockFlight();
		}

		else if (e.getSource() == flightUnloaded) {
			unloadFlight();
		}

		else if (e.getSource() == addPassenger) {
			addPassengerToFlight();
		}

		else if (e.getSource() == flightLoaded) {
			loadFlight();
		}

	}

	private void dockFlight() {
		gateInfoDatabase.docked(this.gateNumber);
		int mCode = gateInfoDatabase.getFlightCodeByGate(gateNumber);
		aircraftManagementDatabase.setStatus(mCode, ManagementRecord.UNLOADING);

		String gateInfo = flightCode + " has now docked at " + gateNumber + "gate \n" +
				          flightCode + " is currently unloading";

		gateInformation.setText(gateInfo);
		gateStatus.setText("Flight Unloading");
	}

	private void unloadFlight() {
		int mCode = gateInfoDatabase.getFlightCodeByGate(gateNumber);
		aircraftManagementDatabase.setStatus(mCode, ManagementRecord.READY_CLEAN_AND_MAINT);

		String gateInfo = flightCode + " unloaded at " + gateNumber + "gate \n" +
		flightCode + " is being serviced / cleaned";

		gateInformation.setText(gateInfo);
		gateStatus.setText("Flight Unloaded");
	}

	private void addPassengerToFlight() {
		// UI THIS
		PassengerDetails passengerName = new PassengerDetails(JOptionPane.showInputDialog("Passenger Name"));
		int[] mCodes = aircraftManagementDatabase.getWithStatus(14);
		int mCode = mCodes[0];
		aircraftManagementDatabase.addPassenger(mCode, passengerName);
	}

	private void loadFlight() {
		// UI THIS
		aircraftManagementDatabase.getWithStatus(14);
		// todo -- rentriduce this but locally to this funtion
		// updateGateStatus();
	}

	//Taken from somewhere else. Will fix and update as im doing UI
	@Override
	public void update(Observable o, Object arg) {
		int mCode = gateInfoDatabase.getFlightCodeByGate(gateNumber);
		if (mCode == -1 || aircraftManagementDatabase.getStatus(mCode) != ManagementRecord.TAXIING) {
			resetGate();
			// gate shouldn't be displaying anything yet
			return;
		}

		int flightStatus = aircraftManagementDatabase.getStatus(mCode);
		// fetch the fight record
		Itinerary i = aircraftManagementDatabase.getItinerary(mCode);
		String flightCode = aircraftManagementDatabase.getFlightCode(mCode);
		gateInformation.setText("Flight " + flightCode + "From " + i.getFrom());
		gateStatus.setText("Flight Taxing to Gate");

		if (flightStatus == ManagementRecord.TAXIING) {
			// flight is taxing enable staff to dock the flight
			flightDocked.setEnabled(true);
			flightUnloaded.setEnabled(false);
			addPassenger.setEnabled(false);
			flightLoaded.setEnabled(false);
		} else if (flightStatus == ManagementRecord.UNLOADING) {
			// flight is unloading, enable staff to set the flight as unloaded
			flightDocked.setEnabled(false);
			flightUnloaded.setEnabled(true);
			addPassenger.setEnabled(false);
			flightLoaded.setEnabled(false);
		} else if (flightStatus == ManagementRecord.READY_CLEAN_AND_MAINT) {
			// todo -- ...
		} else {
			// disable everything, flight status shouldn't be manipulated by the gate
			resetGate();
		}
	}

	/**
	 * Disables all buttons and removes gate info
	 */
	public void resetGate() {
		flightDocked.setEnabled(false);
		flightUnloaded.setEnabled(false);
		addPassenger.setEnabled(false);
		flightLoaded.setEnabled(false);

		gateInformation.setText("");
		gateStatus.setText("Gate Closed");
	}
}