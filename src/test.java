import java.sql.*;
//download .jr into classpath to use
public class test {
	public void conexion(){
		try{
			//1. Get a connection to database
			Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo","root","123456");
			//2. Create a Statement
			Statement myStat = myConn.createStatement();
			//3- Execute SQL query 
			ResultSet myRs = myStat.executeQuery("select * from forex.client");
			//4- Process the result set 
			while(myRs.next()){
				System.out.println(myRs.getString("account_id") + " , " + myRs.getString("first_name") + " , " + myRs.getString("last_name") + " , " + myRs.getString("email") + " , " + myRs.getString("premium"));
			}
			
		}
		catch(Exception exc){
			exc.printStackTrace();
			
		}
	}
}
