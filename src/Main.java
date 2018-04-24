import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Main /*implements Runnable*/{

	public static void main(String[] args) throws Exception {
		
		//Declarar API
		API myAPI = new API();
        URL webrates = new URL("http://webrates.truefx.com/rates/connect.html?f=html");
        //String[] data = myAPI.ParseHTML(webrates);
        //String[][] subData = myAPI.paramData(myAPI.ParseHTML(webrates)); //Lee el HTML, lo parsea y lo guarda en un arreglo de arreglos en donde cada uno es la informacion de cada quote
        List<Observable> quotes = new ArrayList<Observable>();
        quotes = myAPI.parseHTML(webrates);
        ClienteFree client = new ClienteFree();

        for(Observable q : quotes) {
        	q.addObserver(client);
        	client.setOldValues(q);
        }
        
       	run(myAPI,webrates,quotes);
       	
        
        //myAPI.printData(subData);
        //EURUSD.setParameters(data[0],data[1],)
        
        //Quote test = new EURUSD();
        //test.setParameters(subData[0]);
        //test.printParams();
        
        /*Observable EURUSD = new Quote();
        Observable USDJPY = new Quote();
        Observable GBPUSD = new Quote();
        Observable EURGBP = new Quote();
        Observable USDCHF = new Quote();
        Observable EURJPY = new Quote();
        Observable EURCHF = new Quote();
        Observable USDCAD = new Quote();
        Observable AUDUSD = new Quote();
        Observable GBPJPY = new Quote();
        
        quotes.add(EURUSD);
        quotes.add(USDJPY);
        quotes.add(GBPUSD);
        quotes.add(EURGBP);
        quotes.add(USDCHF);
        quotes.add(EURJPY);
        quotes.add(EURCHF);
        quotes.add(USDCAD);
        quotes.add(AUDUSD);
        quotes.add(GBPJPY);
        
        int cont = 0;
        for(Observable q : quotes) {
        	q.addObserver(new ClienteFree(q));
        }
        
        //while(true) {
        	for(Observable q : quotes) {
        		((Quote) q).setParameters(subData[cont]);
        		cont++;
        	}
        //}*/
       	
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