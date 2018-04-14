import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;

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
	
	//Convertir el String[] con todos los datos a un arreglo de arreglos, separando los de cada quote
	public String[][] paramData(String[] data) {
		String[][] subData = new String[10][9];
		
		int cont1 = 0;
		int cont2 = 0;
		
		for(String st : data) {
			if(cont2 == 8) {
				subData[cont1][cont2] = st;
				cont1++;
				cont2 = 0;
			}else {
				subData[cont1][cont2] = st;
				cont2++;
			}
		}
		
		return subData;
	}
	
	public void printData(String[] array) {
		for(String st : array) {
    		System.out.println(st);	
    	}
	}
	
	public void printData(String[][] array) {
		for(String[] stv : array) {
			System.out.println("nuevo vector:");
			for(String st : stv) {
				System.out.println(st);
			}
			System.out.println("");
		}
	}
}
