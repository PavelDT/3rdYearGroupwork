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

		AircraftManagementDatabase aircraftManagementDatabase = AircraftManagementDatabase.getInstance();
		GateInfoDatabase gateInfoDatabase = GateInfoDatabase.getInstance();

       // Instantiate and show all interfaces as Frames

		new GOC(aircraftManagementDatabase, gateInfoDatabase);
		new LATC(aircraftManagementDatabase, gateInfoDatabase);
		new MaintenanceInspector(aircraftManagementDatabase);
		new CleaningSupervisor(aircraftManagementDatabase);
		new RefuellingSupervisor(aircraftManagementDatabase);
		new RadarTransceiver(aircraftManagementDatabase);

		for (int gateNumber = 0; gateNumber < GateInfoDatabase.maxGateNumber; gateNumber++) {
			new GateConsole(aircraftManagementDatabase, gateInfoDatabase, gateNumber);
		}
	}
}