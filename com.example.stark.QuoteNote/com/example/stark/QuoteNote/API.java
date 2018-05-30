package com.example.stark.QuoteNote;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
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

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

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

	public static final String WRITE_OBJECT_SQL = "INSERT INTO java_objects(name, object_value,mail,password) VALUES (?, ?, ?, ?)";
	public static final String UPDATE_OBJECT_SQL = "UPDATE java_objects SET object_value=? WHERE mail=? AND password=?";
	public static final String READ_OBJECT_SQL = "SELECT object_value FROM java_objects WHERE mail=? and password=?";
	
	ServerSocket serverSocket;
	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	int PORT = 12345;
	Gson gson = new Gson();
	String quoteGson;
	Type HashMapType = new TypeToken<HashMap<String, String>>() { }.getType();
	Map<String,String> requestMap;
	
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
			//q.setParameters(parseCol(cols));
			q.setParameters(cols.get(0).text().toString(),
					Long.parseLong(cols.get(1).text().toString()),
					new BigDecimal(cols.get(2).text().toString()),
					Integer.parseInt(cols.get(3).text().toString()),
					new BigDecimal(cols.get(4).text().toString()),
					Integer.parseInt(cols.get(5).text().toString()),
					new BigDecimal(cols.get(6).text().toString()),
					new BigDecimal(cols.get(7).text().toString()),
					new BigDecimal(cols.get(8).text().toString()));
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
	
	public void Unsubscribe(Quote q, ClienteFree client) {
		q.deleteObserver(client);
		client.removeQuote(q);
	}
	
	public void updateQuoteList(String newquotes) {
		this.quoteGson = newquotes;
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
	
	public void AndroidListener() {
		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server Started and listening to the port " + PORT);
          
	            //Server is running always. This is done using this while(true) loop
	            while(true)
	            {
	                //Reading the message from the client
	                socket = serverSocket.accept();
	                
	                ois = new ObjectInputStream(socket.getInputStream());
	                requestMap = gson.fromJson((String)(ois.readObject()),HashMapType);
	                
	                System.out.println("Message received from client is "+requestMap.get("request"));
	 
	                String returnMessage = "Default message";
	                try
	                {
	                    switch(requestMap.get("request")) {
	                    case "quotes":
	                    	returnMessage = Main.quotesGson;
	                    	//returnMessage = gson.toJson(Main.quotes);
	                    	break;
	                    	
	                    case "subscribe":
	                    	try {
		                    	
		                    	for(Quote quote : Main.quotes) {
		                    		if(gson.fromJson(requestMap.get("quote"), Quote.class).getName().contains(quote.getName())) {
		                    			Subscribe(quote, gson.fromJson(requestMap.get("client"), ClienteFree.class));
		                    			System.out.println(quote.getName() + " Quote found!");
		                    			break;
		                    		}
		                    		else {
		                    			System.out.println(quote.getName() + " Quote not found");
		                    		}
		                    	}
		                    	returnMessage = gson.toJson(gson.fromJson(requestMap.get("client"), ClienteFree.class));
		                    	break;	
	                    	}catch(Exception e) {
	                    		e.printStackTrace();
	                    	}
	                    	
	                    case "unsubscribe":
	                    	try {
		                    	
		                    	for(Quote quote : Main.quotes) {
		                    		if(gson.fromJson(requestMap.get("quote"), Quote.class).getName().contains(quote.getName())) {
		                    			Unsubscribe(quote, gson.fromJson(requestMap.get("client"), ClienteFree.class));
		                    			System.out.println(quote.getName() + " Quote found!");
		                    			break;
		                    		}
		                    	}
		                    	returnMessage = gson.toJson(gson.fromJson(requestMap.get("client"), ClienteFree.class));
		                    	break;	
	                    	}catch(Exception e) {
	                    		e.printStackTrace();
	                    	}
	                    	
	                    case "login":
	                    	try {
	                    		String[] autent = Mapeando(requestMap.get("body"));
	                    		ClienteFree newClient = (ClienteFree) getObject(getConnection(),autent[0],autent[1],READ_OBJECT_SQL);
	                    		returnMessage = gson.toJson(newClient);
	                    	}
	                    	catch(Exception e) {
	                    		e.printStackTrace();
	                    	}
	                    	break;
	                    case "register":
	                    	try {
	                    		writeJavaObject(getConnection(), gson.fromJson(requestMap.get("body"),ClienteFree.class), 
	                    				gson.fromJson(requestMap.get("body"),ClienteFree.class).getEmail(),
	                    				gson.fromJson(requestMap.get("body"),ClienteFree.class).getPassword(), WRITE_OBJECT_SQL);
		                    returnMessage = "Succesful registration";

	                    	}
	                    	catch(Exception e) {
	                    		e.printStackTrace();
	                    	}
	                    	break;
	                    case "logout":
	                    	try {
	                    		updateJavaObject(getConnection(), gson.fromJson(requestMap.get("body"),ClienteFree.class), 
	                    				gson.fromJson(requestMap.get("body"),ClienteFree.class).getEmail(),
	                    				gson.fromJson(requestMap.get("body"),ClienteFree.class).getPassword(), UPDATE_OBJECT_SQL);
	                    		returnMessage = "Succesful logout";
	                    	}catch(Exception e) {
	                    		e.printStackTrace();
	                    	}
	                    	break;
	                    default:
	                    	break;
	                    }
	                    	
	                }
	                catch(JsonParseException e)
	                {
	                    returnMessage = "Please send a proper message";
	                }
	 
	                //Sending the response back to the client.
	                sendObject(returnMessage);
	            }
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
            try
            {
                socket.close();
            }
            catch(Exception e){
            	e.printStackTrace();
            }
        }
		
	}

	public Connection getConnection() throws Exception {
		String driver = "org.gjt.mm.mysql.Driver";
	    String url = "jdbc:mysql://localhost:3306/forex";
	    String username = "root";
	    String password = "root";
	    Class.forName(driver);
	    Connection conn = DriverManager.getConnection(url, username, password);
	    return conn;
	  }
	
	public long writeJavaObject(Connection conn, Object object, String mail, String password, String statement) throws Exception {
	    String className = object.getClass().getName();
	   
	    PreparedStatement pstmt = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

	    // set input parameters
	    pstmt.setString(1, className);
	    pstmt.setObject(2, object);
	    pstmt.setString(3, mail);
	    pstmt.setString(4, password);
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
	
	public long updateJavaObject(Connection conn, Object object, String mail, String password, String statement) throws Exception {
	    String className = object.getClass().getName();
	   
	    PreparedStatement pstmt = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

	    // set input parameters
	    pstmt.setObject(1, object);
	    pstmt.setString(2, mail);
	    pstmt.setString(3, password);
	    pstmt.executeUpdate();

	    // get the generated key for the id
	    ResultSet rs = pstmt.getGeneratedKeys();
	    int id = -1;
	    if (rs.next()) {
	      id = rs.getInt(1);
	    }

	    rs.close();
	    pstmt.close();
	    System.out.println("updateJavaObject: done serializing: " + className);
	    return id;
	  }
	
	  
	  public Object getObject(Connection conn, String mail, String password, String statement) throws Exception
	  {
	      Object rmObj=null;
	      PreparedStatement ps;
	      ResultSet rs;

	      ps=conn.prepareStatement(statement);
	      ps.setString(1, mail);
	      ps.setString(2, password);
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

	    public void sendObject(Object o) {
	        try {
	            oos = new ObjectOutputStream(socket.getOutputStream());
	            oos.writeObject(o);
				oos.flush();
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	            System.out.println("Object not sent");
	        }
	    }
	    
	    
	    public String[] Mapeando(String s) {
			   if (s.contains(",")) {
				   
				   return s.split(",");
		
				} else {
				    throw new IllegalArgumentException("String " + s + " does not contain ,");
				}
			  
		   }
}

