package DataBase;

import java.sql.*;
public class DBConnection {
	
	static {
		try {
			System.out.println("Loading Driver.......");
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver Loaded Successfully......");
		}catch(Exception e) {
			System.out.println("Driver not found......");
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		String conn_string="jdbc:mysql://localhost:3306/Futuretech_ERP";
		String user = "root";
		String password = "Darshan@1234";
		Connection con=null;
			try {
//				System.out.println("Loading Driver.......");
//				Class.forName("com.mysql.cj.jdbc.Driver");
//				System.out.println("Driver Loaded Successfully......");
				
				System.out.println("\n\n Establishing connection with Database........");
				con=DriverManager.getConnection(conn_string,user,password);
				System.out.println("Database connection established successfully...");
			}catch(Exception e) {
				e.printStackTrace();
			}
		return con;
	}
}
