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