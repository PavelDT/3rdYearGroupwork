package group6.view;
// Generated by Together

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

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

	/**
	 * Instances of JButton
	 * Flight Docked
	 */
	private JButton flightDocked;
	
	/**
	 * Instances of JButton
	 * Flight Unloaded
	 */
	private JButton flightUnloaded;
	
	/**
	 * Instances of JButton
	 * Add Passenger
	 */
	private JButton addPassenger;
	
	/**
	 * Instances of JButton
	 * Ready to Depart
	 */
	private JButton departReadyBtn;
	
	/**
	 * Instances of JButton
	 * Upload Passenger List
	 */
	private JButton uploadPassengerListBtn;
	
	/**
	 * Instance of JTextArea
	 * Displays the gate information
	 */
	private JTextArea gateInformation;
	
	/**
	 * Instance of JTextArea
	 * Displays the passenger List
	 */
	private JTextArea passengerList;

	/**
	 * Instance of JLabel - displays the status of gate
	 */
	private JLabel gateStatus;

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
		Container window = getContentPane();
		window.setLayout(new BoxLayout(window, BoxLayout.PAGE_AXIS));

		JPanel row0 = new JPanel();
		row0.setLayout(new FlowLayout());
		JLabel gateLabel = new JLabel();
		gateLabel.setFont(new Font(gateLabel.getName(), Font.PLAIN, 20));
		gateLabel.setText("Gate " + gateNumber);
		row0.add(gateLabel);

		JPanel row1 = new JPanel();
		row1.setLayout(new FlowLayout());
		gateInformation = new JTextArea(3,30);
		gateInformation.setEditable(false);
		JScrollPane scroll = new JScrollPane(gateInformation);

		gateStatus = new JLabel();
		row1.add(scroll, null);
		row1.add(gateStatus);

		JPanel row2 = new JPanel();
		row2.setLayout(new GridLayout());

		flightDocked = new JButton("Plane Docked");
		row2.add(flightDocked);
		flightDocked.addActionListener(this);
		flightDocked.setEnabled(false);

		flightUnloaded = new JButton("Plane Unloaded");
		row2.add(flightUnloaded);
		flightUnloaded.addActionListener(this);
		flightUnloaded.setEnabled(false);

		addPassenger = new JButton("Add Passenger");
		row2.add(addPassenger);
		addPassenger.addActionListener(this);
		addPassenger.setEnabled(false);

		uploadPassengerListBtn = new JButton("Upload P. List");
		row2.add(uploadPassengerListBtn);
		uploadPassengerListBtn.addActionListener(this);
		uploadPassengerListBtn.setEnabled(false);

		departReadyBtn = new JButton("Depart");
		row2.add(departReadyBtn);
		departReadyBtn.addActionListener(this);
		departReadyBtn.setEnabled(false);


		JPanel row3 = new JPanel();
		row3.setLayout(new FlowLayout());

		passengerList = new JTextArea(3,30);
		passengerList.setEditable(false);
		row3.add(passengerList);

		window.add(row0);
		window.add(row1);
		window.add(row2);
		window.add(row3);

		aircraftManagementDatabase.addObserver(this);
		gateInfoDatabase.addObserver(this);
		setVisible(true);
	}

	/**
	 * JButton action Listeners
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == flightDocked) {
			dockFlight();
		} else if (e.getSource() == flightUnloaded) {
			unloadFlight();
		} else if (e.getSource() == addPassenger) {
			addPassengerToFlight();
		} else if (e.getSource() == uploadPassengerListBtn) {
			uploadPassengerList();
		} else if (e.getSource() == departReadyBtn) {
			depart();
		}
	}

	/**
	 * This method docks the flight 
	 */
	private void dockFlight() {
		gateInfoDatabase.docked(this.gateNumber);
		int mCode = gateInfoDatabase.getFlightCodeByGate(gateNumber);

		aircraftManagementDatabase.setStatus(mCode, ManagementRecord.UNLOADING);
		String flightCode = aircraftManagementDatabase.getFlightCode(mCode);

		String gateInfo = flightCode + " has now docked at " + gateNumber + "gate \n" +
				          flightCode + " is currently unloading";

		gateInformation.setText(gateInfo);
		gateStatus.setText("Flight Unloading");
	}

	/**
	 * This method unloads the flight
	 */
	private void unloadFlight() {
		int mCode = gateInfoDatabase.getFlightCodeByGate(gateNumber);
		aircraftManagementDatabase.setStatus(mCode, ManagementRecord.READY_CLEAN_AND_MAINT);

		// clear the passenger list.
		aircraftManagementDatabase.resetPassengerList(mCode);

		String flightCode = aircraftManagementDatabase.getFlightCode(mCode);

		String gateInfo = flightCode + " unloaded at gate No. " + gateNumber + "\n" +
		                  "Flight is being serviced / cleaned";

		gateInformation.setText(gateInfo);
		gateStatus.setText("Flight Unloaded");
	}

	/**
	 * This method adds the passengers to the departing flight
	 */
	private void addPassengerToFlight() {
		PassengerDetails passengerName = new PassengerDetails(JOptionPane.showInputDialog("Passenger Name"));
		int mCode = gateInfoDatabase.getFlightCodeByGate(gateNumber);
		aircraftManagementDatabase.addPassenger(mCode, passengerName);
		// no status update to be done, multiple passangers can be added.
	}

	/**
	 * This method changes the status code to Ready to depart
	 */
	private void uploadPassengerList() {
		int mCode = gateInfoDatabase.getFlightCodeByGate(gateNumber);
		aircraftManagementDatabase.setStatus(mCode, ManagementRecord.READY_DEPART);
	}

	/**
	 * This method changes the status code to Ready to awaiting taxi
	 */
	private void depart() {
		int mCode = gateInfoDatabase.getFlightCodeByGate(gateNumber);
		aircraftManagementDatabase.setStatus(mCode, ManagementRecord.AWAITING_TAXI);
	}

	@Override
	public void update(Observable o, Object arg) {
		int mCode = gateInfoDatabase.getFlightCodeByGate(gateNumber);
		passengerList.setText("");
		// safeguard against changing the flight's status until its correct to do so
		if (mCode == -1) {
			// gate shouldn't be displaying anything yet
			resetGate();
			return;
		}
		int flightStatus = aircraftManagementDatabase.getStatus(mCode);
		if (flightStatus != ManagementRecord.TAXIING &&
			flightStatus != ManagementRecord.UNLOADING &&
			flightStatus != ManagementRecord.READY_PASSENGERS &&
			flightStatus != ManagementRecord.READY_DEPART) {
			// gate shouldn't be displaying anything yet
			resetGate();
			return;
		}

		// fetch the fight record
		Itinerary i = aircraftManagementDatabase.getItinerary(mCode);
		String flightCode = aircraftManagementDatabase.getFlightCode(mCode);
		gateInformation.setText("Flight " + flightCode + "From " + i.getFrom());
		gateStatus.setText("Flight Taxing to Gate");

		// update passanger list.
		for (PassengerDetails pd : aircraftManagementDatabase.getPassengerList(mCode).getPassengerDetails()) {
			passengerList.append(pd.getName() + "\n");
		}

		if (flightStatus == ManagementRecord.TAXIING) {
			// flight is taxing enable staff to dock the flight
			flightDocked.setEnabled(true);
			flightUnloaded.setEnabled(false);
			addPassenger.setEnabled(false);
			uploadPassengerListBtn.setEnabled(false);
			departReadyBtn.setEnabled(false);
		} else if (flightStatus == ManagementRecord.UNLOADING) {
			// flight is unloading, enable staff to set the flight as unloaded
			flightDocked.setEnabled(false);
			flightUnloaded.setEnabled(true);
			addPassenger.setEnabled(false);
			uploadPassengerListBtn.setEnabled(false);
			departReadyBtn.setEnabled(false);
		} else if (flightStatus == ManagementRecord.READY_PASSENGERS) {
			// flight is unloading, enable staff to set the flight as unloaded
			flightDocked.setEnabled(false);
			flightUnloaded.setEnabled(false);
			addPassenger.setEnabled(true);
			uploadPassengerListBtn.setEnabled(true);
			departReadyBtn.setEnabled(false);
		} else if (flightStatus == ManagementRecord.READY_DEPART) {
			// flight is unloading, enable staff to set the flight as unloaded
			flightDocked.setEnabled(false);
			flightUnloaded.setEnabled(false);
			addPassenger.setEnabled(false);
			uploadPassengerListBtn.setEnabled(false);
			departReadyBtn.setEnabled(true);
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
		uploadPassengerListBtn.setEnabled(false);
		departReadyBtn.setEnabled(false);

		gateInformation.setText("");
		gateStatus.setText("Gate Closed");
	}
}