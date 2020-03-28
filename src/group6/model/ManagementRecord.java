
package group6.model;// Generated by Together


/**
 * An individual aircraft management record:
 * Either FREE or models an aircraft currently known to SAAMS.
 * See MRState diagram for operational details, and written documentation.
 * This class has public static int identifiers for the individual status codes.
 * An MR may be "FREE", or may contain a record of the status of an individual aircraft under the management of SAAMS.
 * An instance of AircraftManagementDatabase holds a collection of ManagementRecords, and sends the ManagementRecords messages to control/fetch their status.
 * @stereotype entity
 * @url element://model:project::SAAMS/design:node:::id15rnfcko4qme4cko4swib.node107
 * @url element://model:project::SAAMS/design:view:::id3oolzcko4qme4cko4sx40
 * @url element://model:project::SAAMS/design:view:::id4tg7xcko4qme4cko4swuu
 * @url element://model:project::SAAMS/design:node:::id4tg7xcko4qme4cko4swuu.node152
 * @url element://model:project::SAAMS/design:node:::id3oolzcko4qme4cko4sx40.node171
 * @url element://model:project::SAAMS/design:view:::id2wdkkcko4qme4cko4svm2
 * @url element://model:project::SAAMS/design:view:::id15rnfcko4qme4cko4swib
 * @url element://model:project::SAAMS/design:node:::id2wdkkcko4qme4cko4svm2.node41
 */
public class ManagementRecord {

/** Status code: This MR is currently not managing any aircraft information
 *
 * See MRState diagram.*/
  public static int FREE = 0;

/** Status code
 *
 * See MRState diagram.*/
  public static int IN_TRANSIT = 1;

/** Status code
 *
 * See MRState diagram.*/
  public static int WANTING_TO_LAND = 2;

/** Status code
 *
 * See MRState diagram.*/
  public static int GROUND_CLEARANCE_GRANTED = 3;

/** Status code
 *
 * See MRState diagram.*/
  public static int LANDING = 4;

/** Status code
 *
 * See MRState diagram.*/
  public static int LANDED = 5;

/** Status code
 *
 * See MRState diagram.*/
  public static int TAXIING = 6;

/** Status code
 *
 * See MRState diagram.*/
  public static int UNLOADING = 7;

/** Status code
 *
 * See MRState diagram.*/
  public static int READY_CLEAN_AND_MAINT = 8;

/** Status code
 *
 * See MRState diagram.*/
  public static int FAULTY_AWAIT_CLEAN = 9;

  /** Status code
   *
   * See MRState diagram.*/
  public static int OK_AWAIT_CLEAN = 11;

/** Status code
 *
 * See MRState diagram.*/
  public static int CLEAN_AWAIT_MAINT = 10;

/** Status code
 *
 * See MRState diagram.*/
  public static int AWAIT_REPAIR = 12;

/** Status code
 *
 * See MRState diagram.*/
  public static int READY_REFUEL = 13;

/** Status code
 *
 * See MRState diagram.*/
  public static int READY_PASSENGERS = 14;

/** Status code
 *
 * See MRState diagram.*/
  public static int READY_DEPART = 15;

/** Status code
 *
 * See MRState diagram.*/
  public static int AWAITING_TAXI = 16;

/** Status code
 *
 * See MRState diagram.*/
  public static int AWAITING_TAKEOFF = 17;

/** Status code
 *
 * See MRState diagram.*/
  public static int DEPARTING_THROUGH_LOCAL_AIRSPACE = 18;

/** The status code for this ManagementRecord.*/
  private int status;

  /**
   * The gate number allocated to this aircraft, when there is one.
   */
  private int gateNumber;

/** A short string identifying the flight:
 *
 * Usually airline abbreviation plus number, e.g. BA127.
 * Obtained from the flight descriptor when the aircraft is first detected.
 *
 * This is the code used in timetables, and is useful to show on public information screens.*/
  private String flightCode;

  /**
 * Holds the aircraft's itinerary.
 * Downloaded from the flight descriptor when the aircraft is first detected.
 * @clientCardinality 1
 * @directed true
 * @label contains
 * @shapeType AggregationLink
 * @supplierCardinality 1
 */
  // todo - where / when do we set the itinerary
  private Itinerary itinerary;

  /**
 * The list of passengers on the aircraft.
 * Incoming flights supply their passenger list in their flight decsriptor.
 * Outbound flights have passenger lists built from passenger details supplied by the gate consoles.
 * @clientCardinality 1
 * @directed true
 * @label contains
 * @shapeType AggregationLink
 * @supplierCardinality 1
 */
  private PassengerList passengerList;

  /**
   * Contains a description of what is wrong with the aircraft if it is found to be faulty during maintenance inspection.
   */
  private String faultDescription;

  public ManagementRecord() {
  }

  public ManagementRecord(String flightCode) {
    this.flightCode = flightCode;
  }


/**
  * Request to set the MR into a new status.
  *
  * Only succeeds if the state change conforms to the MRState diagram.
  *
  * This is a general purpose state change request where no special details accompany the state change.
  * [Special status changers are, for example, "taxiTo", where a gate number is supplied.]
  * @preconditions Valid transition requested*/
  public void setStatus(int newStatus){
    // todo - how to check valid transition requested?
    // todo - simple state change implies just change the state and nothing more.
    status = newStatus;
  }

  /**
   * Return the status code of this MR.
   */
  public int getStatus(){
    return status;
  }

  /**
   * Return the flight code of this MR.
   */
  public String getFlightCode(){
    return flightCode;
  }

/** Sets up the MR with details of newly detected flight
  *
  * Status must be FREE now, and becomes either IN_TRANSIT or WANTING_TO_LAND depending on the details in the flight descriptor.
  * @preconditions Status is FREE*/
  public void radarDetect(FlightDescriptor fd){
    if (status != FREE) {
      System.out.println("Invalid status: " + status);
      return;
    }

    // todo - status = either IN_TRANSIT || WANTING_TO_LAND depending on the FlightDescriptor details

  }

/** This aircraft has departed from local airspace.
  *
  * Status must have been either IN_TRANSIT or DEPARTING_THROUGH_LOCAL_AIRSPACE, and becomes FREE (and the flight details are cleared).
  * @preconditions Status is IN_TRANSIT or DEPARTING_THROUGH_LOCAL_AIRSPACE*/
  public void radarLostContact(){
    if (status != IN_TRANSIT || status != DEPARTING_THROUGH_LOCAL_AIRSPACE) {
      System.out.println("Invalid status: " + status);
      return;
    }

    System.out.println("Radar lost contact. Changing status to FREE");
    status = FREE;
  }

/** GOC has allocated the given gate for unloading passengers.
  *
  * The gate number is recorded.The status must have been LANDED and becomes TAXIING.
  * @preconditions Status is LANDED*/
  public void taxiTo(int gateNumber){
    if (status != LANDED) {
      System.out.println("Invalid status: " + status);
      return;
    }

    System.out.println("Flight landed and taxiing. Changing status to TAXIING");
    status = TAXIING;
  }

  /**
   * Accessor for the gate number
   * @return Returns the number of the gate
   */
  public int getGateNumber() {
    return gateNumber;
  }


  /** The Maintenance Supervisor has reported faults.
  *
  * The problem description is recorded.
  *
  * The status must have been READY_FOR_CLEAN_MAINT or CLEAN_AWAIT_MAINT and becomes FAULTY_AWAIT_CLEAN or AWAIT_REPAIR respectively.
  * @preconditions Status is READY_FOR_CLEAN_MAINT or CLEAN_AWAIT_MAINT*/
  public void faultsFound(String description){
    if (status == READY_CLEAN_AND_MAINT) {
      System.out.println("There was a fault found: " + description + ". Changing status to FAULTY_AWAIT_CLEAN");
      status = FAULTY_AWAIT_CLEAN;
      // set the faults description
      faultDescription = description;
    }
    else if (status == CLEAN_AWAIT_MAINT) {
      System.out.println("There was a fault found: " + description + ". Changing status to AWAIT_REPAIR");
      status = AWAIT_REPAIR;
      // set the faults description
      faultDescription = description;
    } else {
      System.out.println("Invalid status: " + status + ". There was a fault found: " + description);
    }
  }

/** The given passenger is boarding this aircraft.
  *
  * Their details are recorded in the passengerList.
  *
  * For this operation to be applicable, the status must be READY_PASSENGERS, and it doesn't change.
  * @preconditions Status is READY_PASSENGERS*/
  public void addPassenger(PassengerDetails details){
    if (status != READY_PASSENGERS) {
      System.out.println("Invalid status: " + status);
      return;
    }

    passengerList.addPassenger(details);
  }

/** Return the entire current PassengerList.*/
  public PassengerList getPassengerList(){
    return passengerList;
  }

/** Return the aircraft's Itinerary.*/
  public Itinerary getItinerary(){
    return itinerary;
  }

  // NOTE: this was added by Pavel to enable editing of the itinerary, it's not in the SAAMS Class Diagram diagram
  // This might have to be a function that builds the itinerary from the passenger list. To be determined...
/** Sets the aircraft's Itinerary. */
  public void setItinerary(Itinerary itinerary) {
    this.itinerary = itinerary;
  }

}
