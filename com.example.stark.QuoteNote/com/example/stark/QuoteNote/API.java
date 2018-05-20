package com.example.stark.QuoteNote;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
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
	
	ServerSocket serverSocket;
	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	
	public List<Quote> parseHTML(URL url) throws Exception{
		List<Quote> quotes = new ArrayList<Quote>();
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
	
	public void setQuoteParameters(URL url, List<Quote> quotes) throws Exception{
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		Document doc = Jsoup.parse(url, 5000);
		Element table = doc.select("table").get(0); //select the first table.
		Elements rows = table.select("tr");

		int cont = 0;
		for(Quote q : quotes) {
			Elements cols = rows.get(cont).select("td");
			q.setParameters(parseCol(cols));
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
	
	public void Subscribe(Quote q, ClienteFree client) {
		q.addObserver(client);
		client.addQuote(q);
    	client.setOldValues(q);
	}
	
	
	public void openAndroidConnection(int port) {
		try {
			while(true)
			{
			ss = new ServerSocket(port);
			System.out.println("Server running at port " + port);
			s = ss.accept();
			//return s;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return s;
	}
	
	public void closeAndroidConnection() {
		try {
			while(true)
			{
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
	
	public void AndroidListener(String message) {
		try {
			while(true)
			{
				isr = new InputStreamReader(s.getInputStream());
				br = new BufferedReader(isr);
				message = br.readLine();
				
				switch(message) {
				case "quoteRequest":
					//Send Quotelist to Android
					break;
				case "LoginAutent":
					//Send Client to Android
					break;
					
				}
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
	
	public long writeJavaObject(Connection conn, Object object, String statement) throws Exception {
	    String className = object.getClass().getName();
	    PreparedStatement pstmt = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

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
		
		public void connectToServer(int port) {
	        try {
	        	serverSocket = new ServerSocket(port);
	        	System.out.println("Server up and ready for connection...");
	            socket = serverSocket.accept();
	            System.out.println("Connection succesful");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    public void sendObject(Object o) {
	        try {
	            oos = new ObjectOutputStream(socket.getOutputStream());
	            oos.writeObject(o);
				oos.flush();
	            //oos.close();
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public ClienteFree receiveClient() throws Exception{
	        try {
	            ois = new ObjectInputStream(socket.getInputStream());
	            ClienteFree cliente = (ClienteFree)(ois.readObject());
	            ois.close();
	            return cliente;
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	    
	    public List<Quote> receiveQuote() throws Exception{
	        try {
	        	ois = (ObjectInputStream) socket.getInputStream();
	            List<Quote> newQuotes = (List<Quote>)(ois.readObject());
	            ois.close();
	            return newQuotes;
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    public void STOP() {
	        stopOutput();
	        stopServer();
	    }

	    public void stopServer() {
	        try {
	            socket.close();
	            serverSocket.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    public void stopOutput() {
	        try {
	            oos.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public void stopInput() {
	        try {
	            ois.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}

