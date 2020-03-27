package group6.model;
import group6.controller.AircraftManagementDatabase;
import group6.controller.GateInfoDatabase;
import group6.view.CleaningSupervisor;
import group6.view.GateConsole;
import group6.view.MaintenanceInspector;
import group6.view.RadarTransceiver;

/**
 * The Main class.
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
		new LATC(aircraftManagementDatabase);
		new MaintenanceInspector(aircraftManagementDatabase);
		new CleaningSupervisor(aircraftManagementDatabase);
		new RefuellingSupervisor(aircraftManagementDatabase);
		new GateConsole(aircraftManagementDatabase, gateInfoDatabase);
		new RadarTransceiver(aircraftManagementDatabase);

	}
}