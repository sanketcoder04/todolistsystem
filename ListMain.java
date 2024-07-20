package todolistsystem;

import java.sql.SQLException;
import java.util.Scanner;

public class ListMain {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Your Name : ");
		String name = sc.nextLine();
		DataBaseManagement system = new DataBaseManagement(name);
		try {
            system.createConnection();
            system.checkAccount();
        } 
		catch (ClassNotFoundException e) {
            System.out.println("Database driver not found: " + e.getMessage());
        } 
		catch (SQLException e) {
            System.out.println("A SQL error occurred: " + e.getMessage());
        } 
		catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        } 
		finally {
            sc.close();
        }
	}
}
