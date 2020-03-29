package group6.test;


import group6.controller.AircraftManagementDatabase;
import group6.model.ManagementRecord;
import group6.view.PublicInfo;

//Test class for public info screen
class PublicInfoTest {

    public static void main(String[] args) {
        AircraftManagementDatabase aircraftManagementDatabase;
        ManagementRecord mr1 = new ManagementRecord("BA265");
        mr1.setStatus(ManagementRecord.WANTING_TO_LAND);
        ManagementRecord mr2 = new ManagementRecord("EK524");
        mr2.setStatus(ManagementRecord.GROUND_CLEARANCE_GRANTED);
        ManagementRecord mr3 = new ManagementRecord("AIR111");
        mr3.setStatus(ManagementRecord.LANDING);

        ManagementRecord[] mrs = new ManagementRecord[3];
        mrs[0] = mr1;
        mrs[1] = mr2;
        mrs[2] = mr3;

        aircraftManagementDatabase = AircraftManagementDatabase.getInstance();
        aircraftManagementDatabase.setManagementRecords(mrs);
        new PublicInfo(aircraftManagementDatabase);
    }

}