import java.net.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;

public class Main {

	public static void main(String[] args) throws Exception {

        URL oracle = new URL("http://webrates.truefx.com/rates/connect.html?f=html");
        BufferedReader in = new BufferedReader(
        new InputStreamReader(oracle.openStream()));

        Document doc;
        String inputLine, html;
        while ((inputLine = in.readLine()) != null) {
        	System.out.println(inputLine);
        	//System.out.println(Jsoup.parse(inputLine).body().text());
        }
        in.close();
    }

}
