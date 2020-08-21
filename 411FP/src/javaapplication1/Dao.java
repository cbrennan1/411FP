package javaapplication1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;






public class Dao {
	// instance fields
	static Connection connect = null;
	Statement statement = null;

	// constructor
	public Dao() {
	  
	}

	public Connection getConnection() {
		// Setup the connection with the DB
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false"
							+ "&user=fp411&password=411");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connect;
	}

	// CRUD implementation

	public void createTables() {
		// variables for SQL Query table creations
		//Tickets1 Table
		final String createTicketsTable = "CREATE TABLE cbren_tickets1(ticket_id INT AUTO_INCREMENT PRIMARY KEY, uid INT, ticket_issuer VARCHAR(30), ticket_description VARCHAR(200), start_date DATE, end_date DATE)";
		//User Table
		final String createUsersTable = "CREATE TABLE cbren_users(uid INT AUTO_INCREMENT PRIMARY KEY, uname VARCHAR(30), upass VARCHAR(30), admin int)";
		//History Table which mirrors Tickets Table holds active and closed tickets
		final String createHistoryTable = "CREATE TABLE cbren_history(ticket_id INT AUTO_INCREMENT PRIMARY KEY, uid INT, ticket_issuer VARCHAR(30), ticket_description VARCHAR(200), start_date DATE, end_date DATE)";

		try {

			// execute queries to create tables

			statement = getConnection().createStatement();

			statement.executeUpdate(createTicketsTable);
			statement.executeUpdate(createUsersTable);
			statement.executeUpdate(createHistoryTable);
			System.out.println("Created tables in given database...");

			// end create table
			// close connection/statement object
			statement.close();
			connect.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("^^CREATETABLES CATCH MESSAGE^^");
		}
		// add users to user table
		addUsers();
	}

	public void addUsers() {
		//Clear initial user list
		final String delUsers = "DELETE FROM cbren_users";
		//Reset primary key back to 1
		final String rpk = "ALTER TABLE cbren_users AUTO_INCREMENT = 1";
		//Will add list of users from userlist.csv file to users table

		// variables for SQL Query inserts
		String sql;
		Statement statement;
		BufferedReader br;
		List<List<String>> array = new ArrayList<>(); // list to hold (rows & cols)

		// read data from file
		try {
			br = new BufferedReader(new FileReader(new File("./userlist.csv")));

			String line;
			while ((line = br.readLine()) != null) {
				array.add(Arrays.asList(line.split(",")));
			}
		} catch (Exception e) {
			System.out.println("There was a problem loading the file");}

		try {

			// Setup the connection with the DB

			statement = getConnection().createStatement();
			statement.executeUpdate(delUsers);
			statement.executeUpdate(rpk);


			// create loop to grab each array index containing a list of values
			// and PASS (insert) that data into your User table
			for (List<String> rowData : array) {

				sql = "insert into cbren_users(uname,upass,admin) " + "values('" + rowData.get(0) + "'," + " '"
						+ rowData.get(1) + "','" + rowData.get(2) + "');";
				statement.executeUpdate(sql);
			}
			System.out.println("Inserts completed in the user database...");

			// close statement object
			statement.close();
			//connect.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("ADDUSER CATCH MESSAGE");}
	}

	//Insert Record to Tickets1 Table
	public int insertRecords(String ticketName, String ticketDesc) {
		int id = 0;
		try {
			statement = getConnection().createStatement();
			statement.executeUpdate("Insert into cbren_tickets1" + "(uid, ticket_issuer, ticket_description, start_date) values(" + " '"
					+ Login.guid + "','" + ticketName + "','" + ticketDesc + "','"+ Login.now + "')", Statement.RETURN_GENERATED_KEYS);
	
			// retrieve ticket id number newly auto generated upon record insertion
			ResultSet resultSet = null;
			resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				// retrieve first field in table
				id = resultSet.getInt(1);
			}
			//statement.close();
			//connect.close();
		} catch (SQLException e) {
			System.out.println("Error Inserting into Records (DOA)");
			e.printStackTrace();}
		System.out.println("Insert Record ID: "+id);
		return id;}

	//Add Records To History Table
	public int insertHistoryRecords(String ticketName, String ticketDesc) {
		int id = 0;
		try {
			statement = getConnection().createStatement();
			statement.executeUpdate("Insert into cbren_history" + "(uid, ticket_issuer, ticket_description, start_date) values(" + " '"
					+ Login.guid + "','" + ticketName + "','" + ticketDesc + "','"+ Login.now + "')", Statement.RETURN_GENERATED_KEYS);
	
			// retrieve ticket id number newly auto generated upon record insertion
			ResultSet resultSet = null;
			resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				// retrieve first field in table
				id = resultSet.getInt(1);
			}
			//statement.close();
			//connect.close();
		} catch (SQLException e) {
			System.out.println("Error Inserting into History (DOA)");
			e.printStackTrace();}
		System.out.println("(History ID: "+id+")");
		return id;}

	//Admin Read Records
	public ResultSet readRecords() {

		ResultSet results = null;
		try {
			statement = getConnection().createStatement();
			statement = connect.createStatement();
			results = statement.executeQuery("SELECT * FROM cbren_tickets1");
			//statement.close();
			//connect.close();
			System.out.println("Viewing Admin Tickets1 Table");
		} catch (SQLException e1) {
			System.out.println("Error Viewing Admin Tickets1 Table");
			e1.printStackTrace();
		}
		return results;}

	//Regular User Read Records	
	public ResultSet readMyRecords() {
		ResultSet results = null;
		String sql = "SELECT * FROM cbren_tickets1 WHERE uid = '"+Login.guid+"';";
		try {
			statement = getConnection().createStatement();
			statement = connect.createStatement();
			results = statement.executeQuery(sql);
			//statement.close();
			//connect.close();
			System.out.println("Viewing '"+Login.guname+ "' Table");
		}catch (SQLException e1) {
			System.out.println("Error Viewing '"+Login.guname+ "' Table");
			e1.printStackTrace();	}
		return results;}

	//Read history
	public ResultSet readHistory() {

		ResultSet results = null;
		try {
			statement = getConnection().createStatement();
			statement = connect.createStatement();
			results = statement.executeQuery("SELECT * FROM cbren_history");
			System.out.println("Viewing History Table");
			//statement.close();
			//connect.close();
		} catch (SQLException e1) {
			System.out.println("Error Reading History (DAO)");
			e1.printStackTrace();}
		return results;}

	// continue coding for updateRecords implementation
	public int updateRecords(String upTicIssue, String upTicDesc, int update){
		String sql = "UPDATE cbren_tickets1 SET ticket_issuer = ?, ticket_description = ? WHERE ticket_id = ?";
		try (Connection conn = this.getConnection();
		PreparedStatement pstmt1 = conn.prepareStatement(sql)) {
			pstmt1.setString(1, upTicIssue);
			pstmt1.setString(2, upTicDesc);
			pstmt1.setInt(3, update);
			int affectedRows = pstmt1.executeUpdate();
			System.out.println(affectedRows+" row affected (Tickets1)");
			update = affectedRows;
			//statement.close();
			//connect.close();
		} catch (SQLException e) {
			System.out.println("Error updating tickets (DAO)");
			System.out.println(e.getMessage());}
		return update;}

	//Update History Records
	public int updateHRecords(String upTicIssue, String upTicDesc, int update){
		String hsql = "UPDATE cbren_history SET ticket_issuer = ?, ticket_description = ? WHERE ticket_id = ?";
		try (Connection conn = this.getConnection();
		PreparedStatement pstmt1 = conn.prepareStatement(hsql)) {
			pstmt1.setString(1, upTicIssue);
			pstmt1.setString(2, upTicDesc);
			pstmt1.setInt(3, update);
			int affectedRows = pstmt1.executeUpdate();
			System.out.println(affectedRows+" row affected (History)");
			update = affectedRows;
			//statement.close();
			//connect.close();
		} catch (SQLException e) {
			System.out.println("Error updating history tickets (DAO)");
			System.out.println(e.getMessage());	}
		return update;}

	// continue coding for deleteRecords implementation
	public int deleteRecords(int delid){
		System.out.println("Creating DELETE statement...");
		String sql = "DELETE FROM cbren_tickets1 WHERE ticket_id = ?";
		int response = JOptionPane.showConfirmDialog(null, "Delete ticket # " + delid + "?",
		"Confirm",  JOptionPane.YES_NO_OPTION, 
		JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.NO_OPTION) {
					System.out.println("No record deleted");
					delid = 0;}
			    else if (response == JOptionPane.YES_OPTION) {
					try (Connection conn = this.getConnection();
					PreparedStatement pstmt1 = conn.prepareStatement(sql)) {
					pstmt1.setInt( 1, delid);
					int affectedRows = pstmt1.executeUpdate();
					System.out.println(affectedRows+" row affected (IN DELETERECORDS)");
					delid = affectedRows;
					//statement.close();
					//connect.close();
						} catch (SQLException e) {
							System.out.println("Error deleting ticket (DAO)");
							System.out.println(e.getMessage());}}						
			  	else if (response == JOptionPane.CLOSED_OPTION) {
					delid = 0;
				System.out.println("Request cancelled");}
	return delid;}

	//when ticket is deleted the end date is updated in history
	public int edhistory(int delid){
		String datenowString = Login.now.toString();  //Convert Login now date to a string
		String hsql = "update cbren_history SET end_date = '"+datenowString+"' WHERE ticket_id = ?;";
		try (Connection conn = this.getConnection();
			PreparedStatement pstmt2 = conn.prepareStatement(hsql)) {
				pstmt2.setInt(1, delid);
				pstmt2.executeUpdate();
				int affectedRows = pstmt2.executeUpdate();
				System.out.println("("+affectedRows+" end date added (IN HISTORY))");
				delid = affectedRows;
		//	statement.close();}
		//	connect.close();}
			 } catch (SQLException e) {
				System.out.println("Error w end date history tickets (DAO)");
				System.out.println(e.getMessage());}
	return delid;}

	//Close Ticket 	
	public int closeTicket(int cid) {
		String datenowString = Login.now.toString();  //Convert Login now date to a string
		String hsql = "update cbren_tickets1 SET end_date = '"+datenowString+"' WHERE ticket_id = ?;";
		try (Connection conn = this.getConnection();
		PreparedStatement pstmt2 = conn.prepareStatement(hsql)) {
			pstmt2.setInt(1, cid);
			pstmt2.executeUpdate();
			int affectedRows = pstmt2.executeUpdate();
			System.out.println(affectedRows+" end date added ( IN CLOSING)");
			cid = affectedRows;
			//statement.close();
			//connect.close();}
		}catch (SQLException e) {
			System.out.println("Error closing tickets (DAO)");
			System.out.println(e.getMessage());}
	return cid;}

}
