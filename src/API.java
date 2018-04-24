import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class API {
	
	public List<Observable> parseHTML(URL url) throws Exception{
		List<Observable> quotes = new ArrayList<Observable>();
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
	
	public void setQuoteParameters(URL url, List<Observable> quotes) throws Exception{
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		Document doc = Jsoup.parse(url, 5000);
		Element table = doc.select("table").get(0); //select the first table.
		Elements rows = table.select("tr");

		int cont = 0;
		for(Observable q : quotes) {
			Elements cols = rows.get(cont).select("td");
			q = ( (Quote) q );
			((Quote) q).setParameters(parseCol(cols));
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
	
	//Convertir el String[] con todos los datos a un arreglo de arreglos, separando los de cada quote
	/*public String[][] paramData(String[] data) {
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
	*/
}