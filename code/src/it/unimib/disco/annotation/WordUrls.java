package it.unimib.disco.annotation;
import java.util.Vector;


public class WordUrls {

	String parola;
	double relevance;
	Vector<String> vettUrl;
	
	public WordUrls(String parola, double relevance, Vector<String> vettUrl){
		this.parola = parola;
		this.relevance = relevance;
		this.vettUrl = vettUrl;
	}

	public String getParola() {
		return parola;
	}

	public double getRelevance() {
		return relevance;
	}
	
	public Vector<String> getVettUrl() {
		return vettUrl;
	}
}
