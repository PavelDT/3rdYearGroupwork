package group6;

import group6.controller.AircraftManagementDatabase;
import group6.controller.GateInfoDatabase;
import group6.view.*;

/**
 * The group6.Main class.
 *
 * The principal component is the usual main method required by Java application
 * to launch the application.
 *
 * Instantiates the databases. Instantiates and shows all the system interfaces
 * as Frames.
 * 
 * @stereotype control
 */
public class Main {

	/**
	 * Launch SAAMS.
	 */

	public static void main(String[] args) {

		// Instantiate databases

		AircraftManagementDatabase aircraftManagementDatabase = new AircraftManagementDatabase();
		GateInfoDatabase gateInfoDatabase = new GateInfoDatabase();

       // Instantiate and show all interfaces as Frames

		new GOC(aircraftManagementDatabase, gateInfoDatabase);
		new LATC(aircraftManagementDatabase, gateInfoDatabase);
		new MaintenanceInspector(aircraftManagementDatabase);
		new CleaningSupervisor(aircraftManagementDatabase);
		new RefuellingSupervisor(aircraftManagementDatabase);
		// todo this gate should come from somewhere and probably shouldn't be a param
		int dummyGateNumber = 5;
		new GateConsole(aircraftManagementDatabase, gateInfoDatabase, dummyGateNumber);
		new RadarTransceiver(aircraftManagementDatabase);
	}
}