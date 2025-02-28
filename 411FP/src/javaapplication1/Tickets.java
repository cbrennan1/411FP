package javaapplication1;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class Tickets extends JFrame implements ActionListener {

	// class level member objects
	Dao dao = new Dao(); // for CRUD operations
	Boolean chkIfAdmin = null;

	// Main menu object items
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAdmin = new JMenu("Admin");
	private JMenu mnuTickets = new JMenu("Tickets");

	// Sub menu item objects for all Main menu item objects
	JMenuItem mnuItemExit;
	JMenuItem mnuItemUpdate;
	JMenuItem mnuItemDelete;
	JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemViewTicket;
	JMenuItem mnuItemViewMyTicket;
	JMenuItem mnuItemViewHistory;
	JMenuItem mnuItemCloseTicket;

	public Tickets(Boolean isAdmin) {

		if(chkIfAdmin = isAdmin){
			createMenu();
			prepareGUI();
			} else {
				createRMenu();
				prepareRGUI();
			}

	}
	//Admin Menu
	private void createMenu() {

		/* Initialize sub menu items **************************************/

		// initialize sub menu item for File main menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);

		// initialize first sub menu items for Admin main menu
		mnuItemUpdate = new JMenuItem("Update Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemUpdate);

		// initialize second sub menu items for Admin main menu
		mnuItemDelete = new JMenuItem("Delete Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemDelete);

		// initialize third sub menu items for Admin main menu
		mnuItemViewHistory = new JMenuItem("View Ticket History");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemViewHistory);

		// initialize fourth sub menu items for Admin main menu
		mnuItemCloseTicket = new JMenuItem("Close a Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemCloseTicket);

		// initialize first sub menu item for Tickets main menu
		mnuItemOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemOpenTicket);

		// initialize second sub menu item for Tickets main menu
		mnuItemViewTicket = new JMenuItem("View Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemViewTicket);

		// initialize any more desired sub menu items below

		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener(this);
		mnuItemUpdate.addActionListener(this);
		mnuItemDelete.addActionListener(this);
		mnuItemOpenTicket.addActionListener(this);
		mnuItemViewTicket.addActionListener(this);
		mnuItemViewHistory.addActionListener(this);
		mnuItemCloseTicket.addActionListener(this);

		 /*
		  * continue implementing any other desired sub menu items (like 
		  * for update and delete sub menus for example) with similar 
		  * syntax & logic as shown above*
		 */
	}

	//Regular User Menu
	private void createRMenu() {

		/* Initialize sub menu items **************************************/

		// initialize sub menu item for File main menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);

		// initialize first sub menu item for Tickets main menu
		mnuItemOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemOpenTicket);

		// initialize second sub menu item for Tickets main menu
		mnuItemViewMyTicket = new JMenuItem("View My Tickets");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemViewMyTicket);

		// initialize any more desired sub menu items below

		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener(this);
		mnuItemOpenTicket.addActionListener(this);
		mnuItemViewMyTicket.addActionListener(this);

		 /*
		  * continue implementing any other desired sub menu items (like 
		  * for update and delete sub menus for example) with similar 
		  * syntax & logic as shown above*
		 */
	}

	private void prepareGUI() {

		// create JMenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); // add main menu items in order, to JMenuBar
		bar.add(mnuAdmin);
		bar.add(mnuTickets);
		// add menu bar components to frame
		setJMenuBar(bar);

		addWindowListener(new WindowAdapter() {
			// define a window close operation
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}
		});
		// set frame options
		setSize(400, 400);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	private void prepareRGUI() {

		// create JMenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); // add main menu items in order, to JMenuBar
		bar.add(mnuTickets);
		// add menu bar components to frame
		setJMenuBar(bar);

		addWindowListener(new WindowAdapter() {
			// define a window close operation
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}
		});
		// set frame options
		setSize(400, 400);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// implement actions for sub menu items

		//Exit
		if (e.getSource() == mnuItemExit) {
			System.exit(0);} 

		//Open ticket	
		else if (e.getSource() == mnuItemOpenTicket) {

			// get ticket information
			String ticketName = JOptionPane.showInputDialog(null, "Enter your name");
			String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");

			// insert ticket information to database
			int id = dao.insertRecords(ticketName, ticketDesc);

			// display results if successful or not to console / dialog box
			if (id != 0) {
				System.out.println("Ticket ID : " + id + " created successfully!!!");
				dao.insertHistoryRecords(ticketName, ticketDesc); //If successful Ticket is added to History Records as well
				JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created");
			} else
				System.out.println("Ticket cannot be created!!!");}

		//Admin View Ticket 
		else if (e.getSource() == mnuItemViewTicket) {
			
			// retrieve all tickets details for viewing in JTable
			try {

				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
				jt.setBounds(30, 40, 200, 400);
				JScrollPane sp = new JScrollPane(jt);
				add(sp);
				setVisible(true); // refreshes or repaints frame on screen

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		//User View Ticket
		else if (e.getSource() == mnuItemViewMyTicket) {

			// retrieve all tickets details for viewing in JTable
			try {

				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readMyRecords()));
				jt.setBounds(30, 40, 200, 400);
				JScrollPane sp = new JScrollPane(jt);
				add(sp);
				setVisible(true); // refreshes or repaints frame on screen

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		//Admin View History
		else if (e.getSource() == mnuItemViewHistory) {
						// retrieve all tickets details for viewing in JTable
						try {

							// Use JTable built in functionality to build a table model and
							// display the table model off your result set!!!
							JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readHistory()));
							jt.setBounds(30, 40, 200, 400);
							JScrollPane sp = new JScrollPane(jt);
							add(sp);
							setVisible(true); // refreshes or repaints frame on screen
			
						} catch (SQLException e1) {
							e1.printStackTrace();}
		}

		//Admin Update Record
		else if (e.getSource() == mnuItemUpdate){
			//Get Ticket ID to Update
			String ticketnum = JOptionPane.showInputDialog(null, "Enter Ticket ID to Update");
			int update = Integer.parseInt(ticketnum); //Turn ticket id to integer
			//Get new ticket issuer
			String upTicIssue = JOptionPane.showInputDialog(null, "Enter New Ticket Issuer");
			//Get new ticket description
			String upTicDesc = JOptionPane.showInputDialog(null, "Enter New Ticket Description");

			/*Check Value inputs
			System.out.println(update +"tickets.java");
			System.out.println(upTicIssue);
			System.out.println(upTicDesc); */

			//insert ticket info to database
			int id = dao.updateRecords(upTicIssue, upTicDesc, update);

			//display results if successful or not to console / dialog box
			if (id != 0)  {
				dao.updateHRecords(upTicIssue, upTicDesc, update);//If successfull update history records too
				System.out.println("Ticket ID : " + update + " updated successfully!!!");
				JOptionPane.showMessageDialog(null, "Ticket id: " + update + " updated");
				} else
				JOptionPane.showMessageDialog(null, "Ticket id: " + update + " cannot be updated");
		}

		//Admin Delete Record
		else if (e.getSource() == mnuItemDelete) {
			//Get Ticket ID to delete
			String ticketID = JOptionPane.showInputDialog(null, "Enter Ticket ID to Delete");
			int delid = Integer.parseInt(ticketID);

			//insert ticket info to database
			int id = dao.deleteRecords(delid); //To delete
			//display results if successful or not to console / dialog box
				if (id != 0)  {
				System.out.println("Ticket ID : " + delid + " deleted successfully!!!");
				JOptionPane.showMessageDialog(null, "Ticket id: " + delid + " deleted");
				dao.edhistory(delid);  //To add end date to history
				} else
				JOptionPane.showMessageDialog(null, "Ticket id: " + delid + " cannot be deleted");
				} 

		//Close ticket
		else if (e.getSource() == mnuItemCloseTicket) {
			//Get Ticket ID to close
			String ticketID = JOptionPane.showInputDialog(null, "Enter Ticket ID to Close");
			int cid = Integer.parseInt(ticketID);
			//insert ticket info to database
			int id = dao.closeTicket(cid); //To close (add end date) to Ticket
					//display results if successful or not to console / dialog box
						if (id != 0)  {
						System.out.println("Ticket ID : " + cid + " closed successfully!!!");
						JOptionPane.showMessageDialog(null, "Ticket id: " + cid + " closed");
						} else
						JOptionPane.showMessageDialog(null, "Ticket id: " + cid+ " cannot be closed");
						} 
	}

}
