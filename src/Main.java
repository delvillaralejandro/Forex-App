import java.net.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;

public class Main {

	public static void main(String[] args) throws Exception {

		API myAPI = new API();
        URL webrates = new URL("http://webrates.truefx.com/rates/connect.html?f=html");
        //String[] data = myAPI.ParseHTML(webrates);
        String[][] subData = myAPI.paramData(myAPI.ParseHTML(webrates)); //Lee el HTML, lo parsea y lo guarda en un arreglo de arreglos en donde cada uno es la informacion de cada quote
        
        myAPI.printData(subData);
        //EURUSD.setParameters(datap[0],data[1],)
        
        
    }

}
