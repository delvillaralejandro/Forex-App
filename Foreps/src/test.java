import java.sql.*;
//download .jr into classpath to use
public class test {
	public static void main(String args[]) {
		System.out.println("hello world");
		System.out.println("test2");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sono", "root", "root");
			//sono is database name root1 = username & root2 = pasword
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from emp");
			
			while(rs.next()) {
				System.out.println(rs.getInt(1) + " " + rs.getString(2) /*+ " " rs.getString(3)*/);
				con.close();
			}
		}catch(Exception e) {System.out.println(e);}
	}
}
