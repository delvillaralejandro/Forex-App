package jdbcdemo;

import java.sql.*;

public class Driver {

	public static void main(String[] args) {
		
		try{
			
			//1. Get a connection to database
			Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo","root","123456");
			//2. Create a Statement
			Statement myStat = myConn.createStatement();
			//3- Execute SQL query 
			ResultSet myRs = myStat.executeQuery("select * from employees");
			//4- Process the result set 
			while(myRs.next()){
				System.out.println(myRs.getString("last_name") + " , " + myRs.getString("first_name"));
				
			}
		}
		catch(Exception exc){
			exc.printStackTrace();
			
		}
		
	}

}
