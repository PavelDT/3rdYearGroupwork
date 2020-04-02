package group6.controller;

import group6.model.Gate;
import org.junit.*;
import org.junit.runners.MethodSorters;


// enforce order of testing
// A device has to be registered before it can be backed up etc.
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GateInfoTest {
    private static GateInfoDatabase gateDB;
    private static AircraftManagementDatabase aircraftDB;

    // setup singleton resources
    @BeforeClass
    public static void setUp() {
        gateDB = GateInfoDatabase.getInstance();
        aircraftDB = AircraftManagementDatabase.getInstance();
    }

    // clean up any resources after test execution
    @AfterClass
    public static void tearDown() {
        // nothing to be done
    }

    @Test
    public void test1Instance(){
        for (int i = 0; i < gateDB.maxGateNumber; i++) {
            Assert.assertNotNull(gateDB.getStatus(i));

        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void test2getStatus(){

        int upperBoundTooLarge = gateDB.maxGateNumber + 1;

        for (int i = 0; i < upperBoundTooLarge; i++) {
            Assert.assertEquals(Gate.FREE, gateDB.getStatus(i));

        }
    }

    @Test
    public void test3Allocation() {
        int gateNumber = 0;
        // use last flight management record
        int flightManagementRecordID = 9;

        // make sure gate doesn't have a flight associated with it
        Assert.assertEquals(new Integer(-1), gateDB.getFlightCodeByGate(gateNumber));

        // check gate is free
        Assert.assertEquals(Gate.FREE, gateDB.getStatus(gateNumber));
        gateDB.allocate(gateNumber, flightManagementRecordID);

        // gate should be reserved after allocation
        Assert.assertEquals(Gate.RESERVED, gateDB.getStatus(gateNumber));

        // should hold id of management record used
        Assert.assertEquals(new Integer(flightManagementRecordID), gateDB.getFlightCodeByGate(gateNumber));
    }

    @Test
    public void test4Docked() {
        int gateNumber = 0;
        int wrongGateNumber = 1;

        // try docking with incorrect status
        Assert.assertEquals(Gate.FREE, gateDB.getStatus(wrongGateNumber));
        gateDB.docked(wrongGateNumber);
        Assert.assertNotEquals(Gate.OCCUPIED, gateDB.getStatus(wrongGateNumber));

        // try docking with correct status
        Assert.assertEquals(Gate.RESERVED, gateDB.getStatus(gateNumber));
        gateDB.docked(gateNumber);
        Assert.assertEquals(Gate.OCCUPIED, gateDB.getStatus(gateNumber));
    }

    @Test
    public void test5Reassigned() {

        int gateNumber = 0;

        // verify gate is used
        Assert.assertEquals(Gate.OCCUPIED, gateDB.getStatus(gateNumber));

        gateDB.reassigned(gateNumber);
        // gate should now be free
        Assert.assertEquals(Gate.FREE, gateDB.getStatus(gateNumber));
    }

    @Test
    public void test6Departed() {
        int gateNumber = 0;
        int flightManagementRecordID = 9;

        // ensure gate is free
        Assert.assertEquals(Gate.FREE, gateDB.getStatus(gateNumber));
        gateDB.allocate(gateNumber, flightManagementRecordID);

        // sure gate is reserved
        Assert.assertEquals(Gate.RESERVED, gateDB.getStatus(gateNumber));
        gateDB.docked(gateNumber);

        // ensure gate is occupied
        Assert.assertEquals(Gate.OCCUPIED, gateDB.getStatus(gateNumber));
        gateDB.departed(gateNumber);

        // ensure gate has freed up again, and has no flight associated with it
        Assert.assertEquals(Gate.FREE, gateDB.getStatus(gateNumber));
        Assert.assertEquals(new Integer(-1), gateDB.getFlightCodeByGate(gateNumber));
    }
}
