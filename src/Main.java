import java.net.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;

public class Main {

	public static void main(String[] args) throws Exception {

		API myAPI = new API();
        URL webrates = new URL("http://webrates.truefx.com/rates/connect.html?f=html");
        String[] data = myAPI.ParseHTML(webrates);
        myAPI.printData(data);
        //EURUSD.setParameters(datap[0],data[1],)
        
        
    }

}
