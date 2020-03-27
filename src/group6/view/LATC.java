package group6.view;
import javax.swing.*;

import group6.controller.AircraftManagementDatabase;
import group6.controller.GateInfoDatabase;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * An interface to SAAMS: A Ground Operations Controller Screen: Inputs events
 * from LATC, and displays aircraft and gate information.This class is a
 * controller for the GateInfoDatabase and the AircraftManagementDatabase:
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
public class LATC extends JFrame implements Observer {
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
	 * The Ground Operations Controller Screen interface has access to the
	 * AircraftManagementDatabase.
	 * 
	 * @clientCardinality 1
	 * @supplierCardinality 1
	 * @label accesses/observes
	 * @directed
	 */

	private AircraftManagementDatabase aircraftManagementDatabase;

	public LATC(AircraftManagementDatabase aircraftManagementDatabase, GateInfoDatabase gateInfoDatabase) {

		this.aircraftManagementDatabase = aircraftManagementDatabase;
		this.gateInfoDatabase = gateInfoDatabase;
		setTitle("GOC View");
		setLocation(200, 200);
		setSize(350, 150);
		Container window = getContentPane();
		window.setLayout(new FlowLayout());
		String[] aircraftCodes = checkForAircraft();
		JList<String> aircrafts = new JList<>(aircraftCodes);
		aircrafts.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		aircrafts.setLayoutOrientation(JList.VERTICAL_WRAP);
		JScrollPane aircraftScroll = new JScrollPane(aircrafts);
		aircraftScroll.setPreferredSize(new Dimension(350, 150));
		aircraftManagementDatabase.addObserver(this);
		gateInfoDatabase.addObserver(this);

	}

	public String[] checkForAircraft() {
		int[] MRs = aircraftManagementDatabase.getWithStatus(1);
		String[] aircraftCodes = new String[MRs.length];
		for (int i = 0; i < MRs.length; i++) {
			aircraftCodes[i] = aircraftManagementDatabase.getFlightCode(MRs[i]);
		}
		return aircraftCodes;
	}

	@Override
	public void update(Observable observable, Object o) {
		AircraftManagementDatabase aircraftDatabase = null;
		GateInfoDatabase gateDatabase = null;

		try {
			aircraftDatabase = (AircraftManagementDatabase) o;
		} catch (ClassCastException e) {
			try {
				gateDatabase = (GateInfoDatabase) o;
			} catch (ClassCastException f) {
				System.out.println(f.getMessage());
			}
		}

		if (aircraftDatabase != null) {
			aircraftManagementDatabase = aircraftDatabase;
		}
		if (gateDatabase != null) {
			gateInfoDatabase = gateDatabase;
		}
	}
}