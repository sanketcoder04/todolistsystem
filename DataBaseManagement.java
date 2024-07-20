package todolistsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

@SuppressWarnings("resource")
public class DataBaseManagement {
	private String url = "jdbc:mysql://localhost:3306/todolistdb";
	private String username = "root";
	private String password = "---";	// password
	
	Connection connection;
	String user;
	
	public DataBaseManagement(String user) {
		this.user = user.toLowerCase();
	}

	public void createConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection(url,username,password);
	}
	
	private void selectOperation() throws SQLException {
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			System.out.println("Enter Which types of Operation you want to perform : Insert Task | Delete Tasks | Update Task | Show Task | Delete Account | Exit ");
			String operation = sc.nextLine();
			if(operation.equalsIgnoreCase("Insert Task")) {
				insertData();
			}
			else if(operation.equalsIgnoreCase("Delete Task")) {
				deleteData();
			}
			else if(operation.equalsIgnoreCase("Update Task")) {
				updateData(); 
			}
			else if(operation.equalsIgnoreCase("Show Task")) {
				printDetails();
			}
			else if(operation.equalsIgnoreCase("Delete Account")) {
				deleteAccount();
			}
			else if(operation.equalsIgnoreCase("Exit")){
				connection.close();
				System.out.println("Thanks for Using To-Do Application....");
				return;
			}
			else {
				System.out.println("Invalid Option Choosen");
			}
		}
	}
	
	private void createTable() throws SQLException {
		String query = "CREATE TABLE " + user + "( id INT PRIMARY KEY AUTO_INCREMENT, tasks VARCHAR(100) NOT NULL, status BOOLEAN DEFAULT FALSE)";
		Statement statement = connection.createStatement();
		statement.executeUpdate(query);
		
		System.out.println("TABLE CREATION SUCCESSFUL....");
		selectOperation();
	}
	
	private void insertData() throws SQLException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Insert Task : ");
		String task = sc.nextLine();
		
		String query = "INSERT INTO " + user + "(tasks) VALUES (?)";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, task);
		statement.executeUpdate();
		System.out.println("DATA INSERTION SUCCESSFUL");
		statement.close();
	}
	
	private void deleteData() throws SQLException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter id of particular Task : ");
		int id = sc.nextInt();
		
		String query = "DELETE FROM " + user + " WHERE id = " + id;
		Statement statement = connection.createStatement();
		statement.executeUpdate(query);
		System.out.println("DATA DELETION SUCCESSFUL");
		statement.close();
	}
	
	private void updateData() throws SQLException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the id for which data you want to mark as done : ");
		int id = sc.nextInt();
		
		String query = "UPDATE " + user + " SET status = TRUE WHERE id = " + id;
		Statement statement = connection.createStatement();
		statement.executeUpdate(query);
		System.out.println("DATA UPDATION SUCCESSFUL");
		statement.close();
	}
	
	private void printDetails() throws SQLException {
		String query = "SELECT * FROM " + user;
		Statement statement = connection.createStatement();
		ResultSet resultset = statement.executeQuery(query);
		
		System.out.println("LIST OF TASKS : ");
		while(resultset.next()) {
			System.out.println("Task : " + resultset.getInt("id") + "    " + resultset.getBoolean("status") + "    " + resultset.getString("tasks"));
		}
		resultset.close();
		statement.close();
	}
	
	private void deleteAccount() throws SQLException {
		String query = "DROP TABLE " + user;
		Statement statement = connection.createStatement();
		statement.executeUpdate(query);
		System.out.println("ACCOUNT DELETION SUCCESSFUL....");
	}
	
	public void checkAccount() throws SQLException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter CREATE if you want to create an account or LOGIN if your account existed");
		String choice = sc.nextLine();
		
		String query = "SHOW TABLES LIKE '" + user + "'";
        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery(query);
		
		if(choice.equalsIgnoreCase("CREATE")) {
            if(set.next()) {
            	System.out.println("ACCOUNT CREATION UNSUCCESSFUL....ACCOUNT ALREADY EXISTS, SELECT OPERATIONS AT EXISTING ACCOUNT");
            	selectOperation();
            }
            else {
            	System.out.println("ACCOUNT CREATION SUCCESSFUL....");
            	createTable();
            }
		}
		else if(choice.equalsIgnoreCase("LOGIN")) {
            if (set.next()) {
                System.out.println("LOGIN SUCCESSFUL....");
                selectOperation();
            } 
            else {
                System.out.println("LOGIN UNSUCCESSFUL....ACCOUNT DOES NOT EXISTS, CREATE A NEW ACCOUNT");
                createTable();
            }
		}
		else {
			System.out.println("Invalid Option Selected...");
		}
	}
}
