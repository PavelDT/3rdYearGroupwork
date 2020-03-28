package group6.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import group6.controller.AircraftManagementDatabase;
import group6.controller.GateInfoDatabase;
import group6.util.UISettings;

import javax.swing.SpringLayout;
import javax.swing.JLabel;
import java.awt.Font;

public class GOC extends JDialog implements Observer{

	private final JPanel contentPanel = new JPanel();

	private GateInfoDatabase gateInfoDatabase;
	/**
	 * The Ground Operations Controller Screen interface has access to the
	 * AircraftManagementDatabase.
	 * 
	 * @clientCardinality 1
	 * @supplierCardinality 1
	 * @label accesses/observes
	 * @directed
	 */
	private AircraftManagementDatabase aircraftManagementDatabase;
	private JLabel lblTitle;

	/**
	 * An interface to SAAMS: A Ground Operations Controller Screen: Inputs events
	 * from GOC (a person), and displays aircraft and gate information. This class
	 * is a controller for the GateInfoDatabase and the AircraftManagementDatabase:
	 * sending them messages to change the gate or aircraft status information. This
	 * class also registers as an observer of the GateInfoDatabase and the
	 * AircraftManagementDatabase, and is notified whenever any change occurs in
	 * those <<model>> elements. See written documentation.
	 * 
	 * @stereotype boundary/view/controller
	 * @url element://model:project::SAAMS/design:node:::id2wdkkcko4qme4cko4svm2.node36
	 * @url element://model:project::SAAMS/design:view:::id2wdkkcko4qme4cko4svm2
	 * @url element://model:project::SAAMS/design:view:::id1un8dcko4qme4cko4sw27
	 * @url element://model:project::SAAMS/design:view:::id1bl79cko4qme4cko4sw5j
	 * @url element://model:project::SAAMS/design:view:::id15rnfcko4qme4cko4swib
	 * @url element://model:project::SAAMS/design:node:::id15rnfcko4qme4cko4swib.node107
	 */
	public GOC(AircraftManagementDatabase aircraftManagementDatabase, GateInfoDatabase gateInfoDatabase) {
		
		this.aircraftManagementDatabase = aircraftManagementDatabase;
		this.gateInfoDatabase = gateInfoDatabase;
		
		setLocation(UISettings.GOCPosition);
		setSize(UISettings.VIEW_WIDTH, UISettings.VIEW_HEIGHT);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		
		lblTitle = new JLabel("GOC");
		lblTitle.setFont(new Font("Arial Black", Font.BOLD, 26));
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblTitle, 10, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblTitle, 170, SpringLayout.WEST, contentPanel);
		contentPanel.add(lblTitle);
		
		setVisible(true);
	}
	
	public String[] checkForAircraft() {
		List<Integer> MRs = aircraftManagementDatabase.getWithStatus(1);
		String[] aircraftCodes = new String[MRs.size()];
		for (int i = 0; i < MRs.size(); i++) {
			aircraftCodes[i] = aircraftManagementDatabase.getFlightCode(MRs.get(i));
		}
		return aircraftCodes;
	}

	@Override
	public void update(Observable observable, Object o) {
		AircraftManagementDatabase aircraftDatabase = null;
		GateInfoDatabase gateDatabase = null;
		try {
			aircraftDatabase = (AircraftManagementDatabase) o;
		} catch (ClassCastException e) {
			try {
				gateDatabase = (GateInfoDatabase) o;
			} catch (ClassCastException f) {
				System.out.println(f.getMessage());
			}
		}
		if (aircraftDatabase != null) {
			aircraftManagementDatabase = aircraftDatabase;
		} else if (gateDatabase != null) {
			gateInfoDatabase = gateDatabase;
		}
	}
}
