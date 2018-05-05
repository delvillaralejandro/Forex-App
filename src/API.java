import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.PreparedStatement;
//import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
//import com.mysql.jdbc.*;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class API {
	
	private static ServerSocket ss;
	private static Socket s;
	private static BufferedReader br;
	private static InputStreamReader isr;
	private static String message = "";
	private static final String WRITE_OBJECT_SQL = "INSERT INTO java_objects(name, object_value) VALUES (?, ?)";	  
	private static final String READ_OBJECT_SQL = "SELECT object_value FROM java_objects WHERE id = ?";
	
	public List<Observable> parseHTML(URL url) throws Exception{
		List<Observable> quotes = new ArrayList<Observable>();
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		Document doc = Jsoup.parse(url, 5000);
		Element table = doc.select("table").get(0); //select the first table.
		Elements rows = table.select("tr");

		for (int i = 0; i < rows.size(); i++) { //first row is the col names so skip it.
		    Element row = rows.get(i);
		    Elements cols = row.select("td");
		    
		    quotes.add(new Quote(parseCol(cols)));
		}
		return quotes;
	}
	
	public void setQuoteParameters(URL url, List<Observable> quotes) throws Exception{
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		Document doc = Jsoup.parse(url, 5000);
		Element table = doc.select("table").get(0); //select the first table.
		Elements rows = table.select("tr");

		int cont = 0;
		for(Observable q : quotes) {
			Elements cols = rows.get(cont).select("td");
			q = ( (Quote) q );
			((Quote) q).setParameters(parseCol(cols));
			cont++;
		}
		
	}
	
	public Wrapper parseCol(Elements cols) {
		return new Wrapper(cols.get(0).text().toString(),
					Long.parseLong(cols.get(1).text().toString()),
					new BigDecimal(cols.get(2).text().toString()),
					Integer.parseInt(cols.get(3).text().toString()),
					new BigDecimal(cols.get(4).text().toString()),
					Integer.parseInt(cols.get(5).text().toString()),
					new BigDecimal(cols.get(6).text().toString()),
					new BigDecimal(cols.get(7).text().toString()),
					new BigDecimal(cols.get(8).text().toString()));
	}
	
	public void Subscribe(Observable q, ClienteFree client) {
		q.addObserver(client);
		client.addQuote(q);
    	client.setOldValues(q);
	}
	
	public void AndroidListener() {
		try {
			while(true)
			{
			ss = new ServerSocket(4444);
			System.out.println("Server running at port 4444");
			s=ss.accept();
			
			isr = new InputStreamReader(s.getInputStream());
			br = new BufferedReader(isr);
			message = br.readLine();
			
			System.out.println(message);
			isr.close();
			br.close();
			ss.close();
			s.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public Connection getConnection() throws Exception {
		String driver = "org.gjt.mm.mysql.Driver";
	    String url = "jdbc:mysql://localhost:3306/forex";
	    String username = "root";
	    String password = "123456";
	    Class.forName(driver);
	    Connection conn = DriverManager.getConnection(url, username, password);
	    return conn;
	  }
	
	public long writeJavaObject(Connection conn, Object object) throws Exception {
	    String className = object.getClass().getName();
	    PreparedStatement pstmt = conn.prepareStatement(WRITE_OBJECT_SQL, Statement.RETURN_GENERATED_KEYS);

	    // set input parameters
	    pstmt.setString(1, className);
	    pstmt.setObject(2, object);
	    pstmt.executeUpdate();

	    // get the generated key for the id
	    ResultSet rs = pstmt.getGeneratedKeys();
	    int id = -1;
	    if (rs.next()) {
	      id = rs.getInt(1);
	    }

	    rs.close();
	    pstmt.close();
	    System.out.println("writeJavaObject: done serializing: " + className);
	    return id;
	  }
	  
	  public Object getObject(Connection conn, long id) throws Exception
	  {
	      Object rmObj=null;
	      PreparedStatement ps;
	      ResultSet rs;

	      ps=conn.prepareStatement(READ_OBJECT_SQL);
	      ps.setLong(1, id);
	      rs=ps.executeQuery();

	      if(rs.next())
	      {
	          ByteArrayInputStream bais;

	          ObjectInputStream ins;

	          try {

	          bais = new ByteArrayInputStream(rs.getBytes("object_value"));

	          ins = new ObjectInputStream(bais);

	          rmObj = ins.readObject();

	          }
	          catch (Exception e) {

	          e.printStackTrace();
	          }

	      }

	      return rmObj;
	  }
}

