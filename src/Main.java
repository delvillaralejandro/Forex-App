import java.net.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Main{

	public static void main(String[] args) throws Exception {
		
		/*
		Connection conn = null;
	    try {
	      conn = getConnection();
	      System.out.println("conn=" + conn);
	      conn.setAutoCommit(false);
	      List<Object> list = new ArrayList<Object>();
	      list.add("This is a short string.");
	      list.add(new Integer(1234));
	      //list.add(new Date(0));
	      
	      ClienteFree cliente = new ClienteFree("Humberto","Mercury","Humberto@cetys.edu.mx","caca");
	      long objectID = writeJavaObject(conn, cliente);
	      conn.commit();
	      System.out.println("Serialized objectID => " + objectID);
	      ClienteFree cliente2 = (ClienteFree) getObject(conn, objectID);
	      System.out.println("[After De-Serialization] Client ID= " + cliente2.getID() + " Client name= "+ cliente2.getName());
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	      conn.close();
	    }
	  }
		 */
		
		// API
		API myAPI = new API();
        URL webrates = new URL("http://webrates.truefx.com/rates/connect.html?f=html");
        //String[] data = myAPI.ParseHTML(webrates);
        //String[][] subData = myAPI.paramData(myAPI.ParseHTML(webrates)); //Lee el HTML, lo parsea y lo guarda en un arreglo de arreglos en donde cada uno es la informacion de cada quote
        List<Observable> quotes = new ArrayList<Observable>();
        quotes = myAPI.parseHTML(webrates);
        ClienteFree client = new ClienteFree();

        /*for(Observable q : quotes) {
        	myAPI.Subscribe(q, client);
        }*/
        
        myAPI.Subscribe(quotes.get(0), client);
        
        //client.setPipChange(50);

       	run(myAPI,webrates,quotes);
        
        //myAPI.AndroidListener();
        //test t = new test();
        //t.conexion();
       	
	}
	
	public static void run(API myAPI, URL webrates,List<Observable> quotes) throws Exception{        
        while(true){
        	myAPI.setQuoteParameters(webrates, quotes);
        	Thread.sleep(3000);
        }
	}
	
	public void run() {
		//Do stuff here
	}

	
}