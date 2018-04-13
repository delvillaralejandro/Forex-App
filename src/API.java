import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class API {
	
	public String[] ParseHTML(URL url) throws Exception{
		
		BufferedReader in = new BufferedReader(
		new InputStreamReader(url.openStream()));
        Document doc;
        String inputLine, data;
        String[] newData = null;
        
        while ((inputLine = in.readLine()) != null) {
        	data = Jsoup.parse(inputLine).body().text(); //Parsing de los datos HTML a String
        	newData = data.split(" "); //separa los valores por espacios y lo guarda al arreglo
        }
        in.close();
        return newData;
	}
	
	public void printData(String[] array) {
		for(String st : array) {
    		System.out.println(st);	
    	}
	}
}
