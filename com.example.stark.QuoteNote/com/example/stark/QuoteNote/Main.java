package com.example.stark.QuoteNote;
import java.net.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class Main{
	
	public static API myAPI;
	public static URL webrates;
    public static List<Quote> quotes;
    public static final int PORT = 9000;

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
		myAPI = new API();
        webrates = new URL("http://webrates.truefx.com/rates/connect.html?f=html");
        quotes = new ArrayList<Quote>();
        quotes = myAPI.parseHTML(webrates);
        ClienteFree client = new ClienteFree();
        client.setToken("clYm_BOo62Q:APA91bGf4fQpmNtVuIiIrw1-7986S8caLk2-gfxPIJ8x8gM3K0VRp4OvlkCHNwQIFH-6pIp6SUR74qX5KwjxYvYjYTUfFQzKU5BmZzFNufBEgzNbrCueTYfHLcnantvMui0UWxm0x8ep");
        ClienteFree client2 = new ClienteFree();
        client2.setToken("daDmHJWj-bE:APA91bFDNSUpnOmbZkMCK4XIru-jg7g61HdGDc4lq9NoyJafqBd65bgpzzHZMdq1sYM52tUah2SGH44EFhrS4YOtgaD7xWeTCxCW7FBlgRiydCAyJ2eTKweOXnUFu9M56t2a3AeiZioe");
        
        /*for(Quote q : quotes) {
        	myAPI.Subscribe(q, client);
        }*/
        
        myAPI.Subscribe(quotes.get(0), client);
        myAPI.Subscribe(quotes.get(1), client2);
        
        //client.setPipChange(50);
        
       /* Thread T = new Thread(new Runnable() {
            public void run(){
            	try {
            		while(true){
                		myAPI.setQuoteParameters(webrates, quotes);
                		Thread.sleep(3000);
                	}
            	}catch(Exception e) {
            		e.printStackTrace();
            	}
            }
        });
        T.start();
        */
        
        try {
			
			myAPI.connectToServer(PORT);
			
			new Thread(new Runnable() {
	            public void run(){
	            	try {
	            		while(true) {
	            			myAPI.sendObject(quotes);
	            			//Thread.sleep(3000);
	            			//client = server.receiveClient();
	            			//System.out.println(client.getName());
	            			//Thread.sleep(3000);
	        				}
	            	}catch(Exception e) {
	            		e.printStackTrace();
	            	}
	            }
	        }).start();
	       
			//}
		} catch (Exception e) {
			e.printStackTrace();
			myAPI.stopOutput();
			myAPI.stopServer();
		}

        
        /*
        Thread T2 = new Thread(new Runnable() {
            public void run(){
            	try {
            		while(true){
                		//myAPI.AndroidListener(message);
                	}
            	}catch(Exception e) {
            		e.printStackTrace();
            	}
            }
        });
        T2.start();
        */
        
        
       	//run(myAPI,webrates,quotes);
        
        //myAPI.AndroidListener();
        //test t = new test();
        //t.conexion();
       	
	}
	
	/*
	public static void run(API myAPI, URL webrates,List<Quote> quotes) throws Exception{        
        while(true){
        	myAPI.setQuoteParameters(webrates, quotes);
        	Thread.sleep(3000);
        }
	}*/
	

	
}