package group6.test;


import group6.controller.AircraftManagementDatabase;
import group6.model.ManagementRecord;
import group6.view.CleaningSupervisor;

//Test class for Cleaning supervisor screen
class CleaningSupervisorTest {

    public static void main(String[] args) {
        AircraftManagementDatabase aircraftManagementDatabase;
        ManagementRecord mr1 = new ManagementRecord("BA256");
        mr1.setStatus(ManagementRecord.OK_AWAIT_CLEAN);
        ManagementRecord mr2 = new ManagementRecord("EK524");
        mr2.setStatus(ManagementRecord.OK_AWAIT_CLEAN);
        ManagementRecord mr3 = new ManagementRecord("AIR111");
        mr3.setStatus(ManagementRecord.CLEAN_AWAIT_MAINT);

        ManagementRecord[] mrs = new ManagementRecord[3];
        mrs[0] = mr1;
        mrs[1] = mr2;
        mrs[2] = mr3;

        aircraftManagementDatabase = AircraftManagementDatabase.getInstance();
        aircraftManagementDatabase.setManagementRecords(mrs);
        new CleaningSupervisor(aircraftManagementDatabase);
    }

}