package group6;

import group6.controller.AircraftManagementDatabaseTest;
import group6.controller.GateInfoTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        AircraftManagementDatabaseTest.class,
        GateInfoTest.class

})

/**
 * JUnit test suite for running all unit tests for the backend of the Backup Application
 */
public class TestSuite {
    // class stays empty
    // it's only used to hold the suite annotations
}