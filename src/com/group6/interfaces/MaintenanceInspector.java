package com.group6.interfaces;
import javax.swing.*;

import com.group6.database.AircraftManagementDatabase;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * An interface to SAAMS:
 * Maintenance Inspector Screen:
 * Inputs events from the Maintenance Inspector, and displays aircraft information.
 * This class is a controller for the AircraftManagementDatabase: sending it messages to change the aircraft status information.
 * This class also registers as an observer of the AircraftManagementDatabase, and is notified whenever any change occurs in that <<model>> element.
 * See written documentation.
 * @stereotype boundary/view/controller
 * @url element://model:project::SAAMS/design:node:::id4tg7xcko4qme4cko4swuu.node146
 * @url element://model:project::SAAMS/design:view:::id15rnfcko4qme4cko4swib
 * @url element://model:project::SAAMS/design:view:::id4tg7xcko4qme4cko4swuu
 * @url element://model:project::SAAMS/design:node:::id15rnfcko4qme4cko4swib.node107
 * @url element://model:project::SAAMS/design:view:::id3y5z3cko4qme4cko4sw81
 */

public class MaintenanceInspector extends JFrame implements ActionListener {

	private final AircraftManagementDatabase aircraftManagementDatabase;
	/**
	 * The Refuelling Supervisor Screen interface has access to the
	 * AircraftManagementDatabase.
	 * 
	 * @supplierCardinality 1
	 * @clientCardinality 1
	 * @label accesses/observes
	 * @directed
	 */

	private JButton theStatus;
	private JButton update;
	private JButton exit;

	public MaintenanceInspector(AircraftManagementDatabase aircraftManagementDatabase, String title, int locationX, int locationY) {

		this.aircraftManagementDatabase = aircraftManagementDatabase;
		setTitle("Controller1");
		setLocation(locationX, locationY);
		setSize(450, 150);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		Container window = getContentPane();
		window.setLayout(new FlowLayout());

		// status button
		update = new JButton("Status");
		window.add(update);
		update.addActionListener(this);

		// update button
		theStatus = new JButton("Update");
		window.add(update);
		theStatus.addActionListener(this);

		// quit button
		exit = new JButton("Quit");
		window.add(exit);
		exit.addActionListener(this);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == theStatus)
			if (e.getSource() == update)
				if (e.getSource() == exit)
					System.exit(0);
	}
}
