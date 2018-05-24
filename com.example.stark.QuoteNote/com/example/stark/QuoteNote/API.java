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
	
	private static final String WRITE_OBJECT_SQL = "INSERT INTO java_objects(name, object_value) VALUES (?, ?)";	  
	private static final String READ_OBJECT_SQL = "SELECT object_value FROM java_objects WHERE id = ?";
	
	ServerSocket serverSocket;
	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	int PORT = 8888;
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
	                
	                //String request = gson.fromJson((String)(ois.readObject()),String.class);
	                System.out.println("Message received from client is "+requestMap.get("request"));
	 
	                //Multiplying the number by 2 and forming the return message
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
	                    	//select from login
	                    	/* jalarClienteSQL()
	                    	 * ClienteFree newClient = (ClienteFree) getObject(conn, objectID);
	                    	 * returnMessage = gson.toJson((ClienteFree) getObject(conn, objectID));
	                    	 * 
	                    	 */
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
	
	public String getQuoteGson(List<Quote> list) {
		return gson.toJson(list);
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
				System.out.println("Object sent");
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	            System.out.println("Object not sent");
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
	    
	    public String receiveRequestGson() throws Exception{
            try {
                //ois = new ObjectInputStream(socket.getInputStream());

                String request = gson.fromJson((String)(ois.readObject()),String.class);
                //ois.close();
                return request;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
	    
	    public List<Quote> receiveQuoteGson() throws Exception{
            try {
                //ois = new ObjectInputStream(socket.getInputStream());
                Type quoteListType = new TypeToken<List<Quote>>(){}.getType();

                List<Quote> newQuotes = gson.fromJson((String)(ois.readObject()), quoteListType);
                //ois.close();
                return newQuotes;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
	    
	    public ClienteFree receiveClientGson() throws Exception{
            try {
                //ois = new ObjectInputStream(socket.getInputStream());

                ClienteFree newCliente = gson.fromJson((String)(ois.readObject()), ClienteFree.class);
                //ois.close();
                return newCliente;
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

