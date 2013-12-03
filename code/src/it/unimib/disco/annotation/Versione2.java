package it.unimib.disco.annotation;

import java.io.InputStream;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;


public class Versione2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		HttpResponse<JsonNode> request = Unirest.post("https://debaetsa-machine-linking.p.mashape.com/annotate")
				  .header("X-Mashape-Authorization", "xsEnP4ZtUxeT0c7pLkzh211z7VYRvauL")
				  .field("text", "Hello")
				  .field("lang", "en")
				  .field("min_weight", "<min_weight>")
				  .field("disambiguation", "<disambiguation>")
				  .field("link", "<link>")
				  .field("form", "<form>")
				  .field("cross", "<cross>")
				  .field("category", "<category>")
				  .field("abstract", "<abstract>")
				  .field("class", "<class>")
				  .field("output_format", "json")
				  .field("id", "<id>")
				  .field("include_text", "<include_text>")
				  .field("jsonp", "<jsonp>")
				  .asJson();
		
		   InputStream is = request.getRawBody();
	        int c;
	        while((c = is.read()) >= 0) {
	            System.out.print((char) c);
		
    }
	}

}
