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