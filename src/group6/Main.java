package group6;

import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import group6.controller.AircraftManagementDatabase;
import group6.controller.GateInfoDatabase;
import group6.util.UISettings;
import group6.view.CleaningSupervisor;
import group6.view.GOC;
import group6.view.GateConsole;
import group6.view.LATC;
import group6.view.MaintenanceInspector;
import group6.view.PublicInfo;
import group6.view.RadarTransceiver;
import group6.view.RefuellingSupervisor;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * This class represents Main. Mains functions to create all interfaces and
 * databases
 * 
 * @author johnr
 *
 */
public class Main extends JFrame {

	/**
	 * Insatnce of Image representing a radar
	 */
	private Image radarGif;

	/**
	 * Instance of JPanal. This panel will be used to attach components
	 */
	private final JPanel contentPanel = new JPanel();

	/**
	 * Represents a button to start the system
	 */
	private JButton btnStart;

	/**
	 * Represents a button to quit the system
	 */
	private JButton btnQuit;

	/**
	 * JLabel that encapsulates the image
	 */
	private JLabel lblImage;

	/**
	 * JLable that displays the version number
	 */
	private JLabel lblVersion;

	/**
	 * Current version number
	 */
	private double versionNo = 1.17;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Main dialog = new Main();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Main() {

		// set native look and feel, applies styles to various UI controls to
		// make the application look like the native OS controls
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		} catch (InstantiationException ie) {
			ie.printStackTrace();
		} catch (IllegalAccessException iae) {
			iae.printStackTrace();
		} catch (UnsupportedLookAndFeelException ulafe) {
			ulafe.printStackTrace();
		}

		setBounds(UISettings.MainBound);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);

		JLabel lblTitle = new JLabel("SAAMS ");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblTitle, 20, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblTitle, 148, SpringLayout.WEST, contentPanel);
		lblTitle.setFont(new Font("Arial Black", Font.BOLD, 26));
		contentPanel.add(lblTitle);

		btnStart = new JButton("Start System");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				handleStart();

			}

		});
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, btnStart, -10, SpringLayout.SOUTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, btnStart, 172, SpringLayout.WEST, contentPanel);
		contentPanel.add(btnStart);

		btnQuit = new JButton("Quit System");
		sl_contentPanel.putConstraint(SpringLayout.WEST, btnQuit, 64, SpringLayout.EAST, btnStart);
		sl_contentPanel.putConstraint(SpringLayout.EAST, btnQuit, -70, SpringLayout.EAST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, btnStart, 0, SpringLayout.NORTH, btnQuit);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, btnQuit, 207, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, btnQuit, -10, SpringLayout.SOUTH, contentPanel);
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleExit();
			}
		});
		contentPanel.add(btnQuit);

		lblImage = new JLabel("");
		sl_contentPanel.putConstraint(SpringLayout.WEST, btnStart, 0, SpringLayout.WEST, lblImage);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblImage, 6, SpringLayout.SOUTH, lblTitle);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, lblImage, -50, SpringLayout.SOUTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblImage, 54, SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, lblImage, -15, SpringLayout.EAST, contentPanel);
		contentPanel.add(lblImage);

		radarGif = new ImageIcon(this.getClass().getResource("/radarGif.gif")).getImage();
		radarGif = radarGif.getScaledInstance(300, 300, Image.SCALE_DEFAULT);
		lblImage.setIcon(new ImageIcon(radarGif));

		lblVersion = new JLabel("V." + versionNo);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblVersion, 0, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblVersion, 0, SpringLayout.WEST, contentPanel);
		contentPanel.add(lblVersion);

	}

	/**
	 * Creates the interfaces and databases
	 */
	private void createInterfaces() {

		AircraftManagementDatabase aircraftManagementDatabase = AircraftManagementDatabase.getInstance();
		GateInfoDatabase gateInfoDatabase = GateInfoDatabase.getInstance();

		new GOC(aircraftManagementDatabase, gateInfoDatabase);
		new LATC(aircraftManagementDatabase);
		new MaintenanceInspector(aircraftManagementDatabase);
		new CleaningSupervisor(aircraftManagementDatabase);
		new RefuellingSupervisor(aircraftManagementDatabase);
		new RadarTransceiver(aircraftManagementDatabase);
		for (int publicInfo = 0; publicInfo < PublicInfo.maxPublicInfoScreens; publicInfo++) {

			new PublicInfo(aircraftManagementDatabase, publicInfo);
		}
		for (int gateNumber = 0; gateNumber < GateInfoDatabase.maxGateNumber; gateNumber++) {
			new GateConsole(aircraftManagementDatabase, gateInfoDatabase, gateNumber);
		}

	}

	/**
	 * Handles the starting of the application
	 */
	private void handleStart() {
		createInterfaces();
		btnStart.setEnabled(false);

	}

	/**
	 * Handles exiting the application
	 */
	private void handleExit() {

		System.exit(EXIT_ON_CLOSE);

	}
}
