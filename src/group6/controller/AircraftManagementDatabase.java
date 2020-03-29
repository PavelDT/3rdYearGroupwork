package group6.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import group6.model.FlightDescriptor;
import group6.model.Itinerary;
import group6.model.ManagementRecord;
import group6.model.PassengerDetails;
import group6.model.PassengerList;

// Generated by Together


/**
 * A central database ("model" class):
 * It is intended that there will be only one instance of this class.
 * Maintains an array of ManagementRecords (MRs), one per potential visiting aircraft. Some MRs hold information about aircraft currently being managed by SAAMS, and some may have the status "Free".
 * The index of each ManagementRecord in the array is its "management code" ("mCode"), and the mCode of any particular visiting aircraft's ManagementRecord must remain fixed once it is allocated.
 * Many classes register as observers of this class, and are notified whenever any aircraft's (MR's) state changes.
 *
 * @stereotype model
 * @url element://model:project::SAAMS/design:view:::id4tg7xcko4qme4cko4swuu
 * @url element://model:project::SAAMS/design:node:::id4tg7xcko4qme4cko4swuu.node149
 * @url element://model:project::SAAMS/design:view:::id1bl79cko4qme4cko4sw5j
 * @url element://model:project::SAAMS/design:view:::idwwyucko4qme4cko4sgxi
 * @url element://model:project::SAAMS/design:node:::id2wdkkcko4qme4cko4svm2.node39
 * @url element://model:project::SAAMS/design:view:::id2wdkkcko4qme4cko4svm2
 * @url element://model:project::SAAMS/design:node:::id3oolzcko4qme4cko4sx40.node169
 * @url element://model:project::SAAMS/design:view:::id2fh3ncko4qme4cko4swe5
 * @url element://model:project::SAAMS/design:view:::id28ykdcko4qme4cko4sx0e
 * @url element://model:project::SAAMS/design:node:::id15rnfcko4qme4cko4swib.node107
 * @url element://model:project::SAAMS/design:view:::id15rnfcko4qme4cko4swib
 * @url element://model:project::SAAMS/design:view:::id3oolzcko4qme4cko4sx40
 */
public class AircraftManagementDatabase extends Observable{

    /**
     * Return the status of the MR with the given mCode supplied as a parameter.
     */
    public int getStatus(int mCode) {
        return managementRecords[mCode].getStatus();
    }

    /**
     * Accessor for gate number
     * @param mCode
     * @return returns gate number
     */
    public int getGateNumber(int mCode) {
        return managementRecords[mCode].getGateNumber();
    }


    /**
     * The array of ManagementRecords. Attribute maxMRs specifies how large this array should be.
     * Initialize to a collection of MRs each in the FREE state.
     * Note: This array could be replaced by another other suitable collection data structure.
     *
     * @byValue
     * @clientCardinality 1
     * @directed true
     * @label contains
     * @shapeType AggregationLink
     * @supplierCardinality 0..*
     */
    private ManagementRecord[] managementRecords;//renamed MRs to managementRecords

    /**
     * The size of the array MRs holding ManagementRecords.<br /><br />In this simple system 10 will do!
     */
    public int maxMRs = 10;

    public void setManagementRecords(ManagementRecord[] mrs) {
        this.managementRecords = mrs;
    }

    /**
     * Singleton implementation to restrict only one instance of this class.
     * using static initializer and lazy instantiation.
     */
    private static AircraftManagementDatabase instance;

    /**
     * Private constructor to prevent instantiation outside of the getInstance function.
     * This implements singleton.
     */
    private AircraftManagementDatabase() {
        managementRecords = new ManagementRecord[maxMRs];
        // initialise all 10 records to FREE so that they can be utilised
        for(int i =0; i<managementRecords.length; i++) {
            managementRecords[i] = new ManagementRecord();
            managementRecords[i].setStatus(ManagementRecord.FREE);
        }
    }

    /**
     * Checks if singleton instance is instantiated. If not it instantiates it.
     * This implements lazyness and makes sure the same object is used over and over.
     * @return
     */
    public static AircraftManagementDatabase getInstance() {
        if (instance == null) {
            instance = new AircraftManagementDatabase();
            // for all observers observing the AircraftManagementDatabase
            instance.setChanged();
            instance.notifyObservers();
        }
        return instance;
    }


    /**
     * Forward a status change request to the MR given by the mCode supplied as a parameter.
     * Parameter newStatus is the requested new status.
     * No effect is expected if the current status is not a valid preceding status.
     * This operation is appropriate when the status change does not need any additional information to be noted.
     * It is present instead of a large collection of public operations for requesting specific status changes.
     */
    public void setStatus(int mCode, int newStatus) {
        if(managementRecords[mCode].getStatus() != newStatus) {
            managementRecords[mCode].setStatus(newStatus);
            setChanged();
            notifyObservers(instance);
        }
    }

    /**
     * Return the flight code from the given MR supplied as a parameter.
     * The request is forwarded to the MR.
     */
    public String getFlightCode(int mCode) {
        // safeguard against a null
        if (managementRecords[mCode] == null) {
            return "No such management record";
        }

        return managementRecords[mCode].getFlightCode();
    }

    /**
     * Returns an array of mCodes:
     * Just the mCodes of those MRs with the given status supplied as a parameter.
     * Principally for call by the various interface screens.
     */
    public int[] getWithStatus(int statusCode) {
        List<Integer> matchedStatusList = new ArrayList<Integer>();
        for (int i = 0; i < managementRecords.length; i++) {
            if (statusCode == managementRecords[i].getStatus()) {
                matchedStatusList.add(i);
            }
        }

        // use java 8 streams to convert the list of Integer to an array of native ints
        // this means the project requires java 8
        // source: https://stackoverflow.com/a/23945015
        return matchedStatusList.stream().mapToInt(item->item).toArray();
    }

    /**
     * The radar has detected a new aircraft, and has obtained flight descriptor fd from it.
     * <p>
     * This operation finds a currently FREE MR and forwards the radarDetect request to it for recording.
     */
    public void radarDetect(FlightDescriptor fd) {

        for(int i =0; i<managementRecords.length; i++) {
            if(managementRecords[i].getStatus() == ManagementRecord.FREE) {
                // update status
                managementRecords[i].setStatus(ManagementRecord.WANTING_TO_LAND);
                // carry out radar detection
                managementRecords[i].radarDetect(fd);

                setChanged();
                notifyObservers();

                // A free management record was found
                // stop looping for efficiency.
                break;
            }
        }

        // todo -- do something if all 10 slots are full.
        //         e.g. raise an alert in the radar
    }

    /**
     * The aircraft in the MR given by mCode supplied as a parameter has departed from the local airspace.
     * The message is forwarded to the MR, which can then delete/archive its contents and become FREE.
     */
    public void radarLostContact(int mCode) {
        managementRecords[mCode].radarLostContact();
        setChanged();
        notifyObservers(instance);
    }

    /**
     * A GOC has allocated the given gate to the aircraft with the given mCode supplied as a parameter for unloading passengers.
     * The message is forwarded to the given MR for status update.
     */
    public void taxiTo(int mCode, int gateNumber) {
        managementRecords[mCode].taxiTo(gateNumber);
        setChanged();
        notifyObservers(instance);
    }

    /**
     * The Maintenance Supervisor has reported faults with the given description in the aircraft with the given mCode.
     * The message is forwarded to the given MR for status update.
     */
    public void faultsFound(int mCode, String description) {
        managementRecords[mCode].faultsFound(description);
        setChanged();
        notifyObservers(instance);
    }

    /**
     * The given passenger is boarding the aircraft with the given mCode.
     * Forward the message to the given MR for recording in the passenger list.
     */
    public void addPassenger(int mCode, PassengerDetails details) {
        managementRecords[mCode].addPassenger(details);
        setChanged();
        notifyObservers(instance);
    }

    /**
     * Return the PassengerList of the aircraft with the given mCode.
     */
    public PassengerList getPassengerList(int mCode) {
        return managementRecords[mCode].getPassengerList();
    }

    /**
     * Return the Itinerary of the aircraft with the given mCode.
     */
    public Itinerary getItinerary(int mCode) {
        return managementRecords[mCode].getItinerary();
    }

}
