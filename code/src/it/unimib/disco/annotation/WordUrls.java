package it.unimib.disco.annotation;
import java.util.Vector;


public class WordUrls {

	String urlTypeVincitrice;
	Vector<String> vettUrlType;
	
	public WordUrls(String urlTypeVincitrice, Vector<String> vettUrlType){
		this.urlTypeVincitrice = urlTypeVincitrice;
		this.vettUrlType = vettUrlType;
	}

	public String getUrlTypeVincitrice() {
		return urlTypeVincitrice;
	}

	public void setUrlTypeVincitrice(String urlTypeVincitrice) {
		this.urlTypeVincitrice = urlTypeVincitrice;
	}

	public Vector<String> getVettUrlType() {
		return vettUrlType;
	}

	public void setVettUrlType(Vector<String> vettUrlType) {
		this.vettUrlType = vettUrlType;
	}

	
}
