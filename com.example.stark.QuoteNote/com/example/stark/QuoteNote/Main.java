package com.example.stark.QuoteNote;
import java.net.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class Main{
	
	public static API myAPI;
	public static URL webrates;
    public static List<Quote> quotes;
    public static String quotesGson;
    //public static Gson gson = new Gson();
    static String WRITE_OBJECT_SQL = "INSERT INTO java_objects(name, object_value,mail,password) VALUES (?, ?, ?, ?)";	

	public static void main(String[] args) throws Exception {
		
		// API
		myAPI = new API();
        webrates = new URL("http://webrates.truefx.com/rates/connect.html?f=html");
        quotes = new ArrayList<Quote>();
        quotes = myAPI.parseHTML(webrates);
        quotesGson = myAPI.gson.toJson(quotes);
        
        ClienteFree client = new ClienteFree();
        client.setToken("clYm_BOo62Q:APA91bGf4fQpmNtVuIiIrw1-7986S8caLk2-gfxPIJ8x8gM3K0VRp4OvlkCHNwQIFH-6pIp6SUR74qX5KwjxYvYjYTUfFQzKU5BmZzFNufBEgzNbrCueTYfHLcnantvMui0UWxm0x8ep");
        ClienteFree client2 = new ClienteFree();
        client2.setToken("daDmHJWj-bE:APA91bFDNSUpnOmbZkMCK4XIru-jg7g61HdGDc4lq9NoyJafqBd65bgpzzHZMdq1sYM52tUah2SGH44EFhrS4YOtgaD7xWeTCxCW7FBlgRiydCAyJ2eTKweOXnUFu9M56t2a3AeiZioe");
        
        try {
        	new Thread(new Runnable() {
            public void run(){
            	try {
            		while(true){
                		myAPI.setQuoteParameters(webrates, quotes);
                		quotesGson = myAPI.gson.toJson(myAPI.parseHTML(webrates));
                		Thread.sleep(3000);
                	}
            	}catch(Exception e) {
            		e.printStackTrace();
            	}
            }
        }).start();
        }catch(Exception e) {
        	e.printStackTrace();
        }
        
        
        try {
        	new Thread(new Runnable() {
	            public void run(){
	            	try {
	            		myAPI.AndroidListener();
	            	}catch(Exception e) {
	            		e.printStackTrace();
	            	}
	            }
	        }).start();
        }catch(Exception e) {
        	e.printStackTrace();
        }
       	
	}
	
}