package group6.controller;

import group6.model.*;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class AircraftManagementDatabaseTest {

    private static AircraftManagementDatabase airDB;

    // setup singleton resources
    @BeforeClass
    public static void setUp() {
        airDB = AircraftManagementDatabase.getInstance();
    }

    // clean up any resources after test execution
    @AfterClass
    public static void tearDown() {
        // nothing to be done
    }

    @Test
    public void testManagementRecordsInitialisation() {
        // check if there are 10 records
        ManagementRecord[] mrs = airDB.getManagementRecords();
        Assert.assertEquals(10, airDB.getManagementRecords().length);

        // make sure all of em are not null
        for (ManagementRecord mr : mrs) {
            Assert.assertNotNull(mr);

            // check default status = 0;
            Assert.assertEquals(ManagementRecord.FREE, mr.getStatus());

            // check itinerary and passenger list are null
            Assert.assertNull(mr.getItinerary());
            Assert.assertNull(mr.getPassengerList());
        }
    }

    @Test
    public void checkRadarDetect() {

        // this test works exclusively with the first entry in management record
        // aka flight with id = 0;
        int mCode = 0;

        // check starting state
        Assert.assertEquals(ManagementRecord.FREE, airDB.getStatus(mCode));

        // Build flight descriptor
        Itinerary it = new Itinerary("EDI", "LHR", "LAX");
        String flightCode = "BA0001";
        PassengerList pl = new PassengerList();
        pl.addPassenger(new PassengerDetails("Billy"));
        pl.addPassenger(new PassengerDetails("Timmy"));
        pl.addPassenger(new PassengerDetails("Jack"));

        FlightDescriptor fd = new FlightDescriptor(flightCode, it, pl);
        // should pick record 0 for the status changes as there are no updates in the
        // records yet.
        airDB.radarDetect(fd);

        // check flight status was updated
        Assert.assertNotEquals(ManagementRecord.FREE, airDB.getStatus(mCode));

        // code could be either WANTING_TO_LAND or IN_TRANSIT
        // use boolean check to allow for both
        Assert.assertTrue(airDB.getStatus(mCode) == ManagementRecord.WANTING_TO_LAND ||
                          airDB.getStatus(mCode) == ManagementRecord.IN_TRANSIT);

        // verify itenerary is correct for the flight
        Assert.assertEquals(airDB.getItinerary(0).getFrom(), it.getFrom());
        Assert.assertEquals(airDB.getItinerary(0).getTo(), it.getTo());
        Assert.assertEquals(airDB.getItinerary(0).getNext(), it.getNext());

        // verify code is correct
        Assert.assertEquals(airDB.getFlightCode(0), flightCode);
    }
}
