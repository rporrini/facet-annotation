package it.unimib.disco.annotation;


import it.unimib.disco.dataset.Folder;
import it.unimib.disco.dataset.Group;
import it.unimib.disco.dataset.Groups;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.filechooser.FileFilter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class Trank {

	/**
	 * @param args
	 */
	private final static String USER_AGENT = "Mozilla/25.0.1";
	static String credenziali = "app_id=bbff1282&app_key=bfb47741212ddde1d0d56eacc0141512";
	static String lang = "lang=it";
	static int contRiga = 0;
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Trank http = new Trank();
		Indice indLucene = new Indice();
		Vector tuttiTypes = new Vector();
		
		List<File> groupDirectory = recuperaCartelleGruppi("../evaluation/");
		
		//per ogni cartella recupero tutti i gruppi appartenenti ad una certa categoria
		for (int j = 0; j < groupDirectory.size(); j++){
			String idCat = groupDirectory.get(j).getName();
			//recuper le keyword di un gruppo
			List<Group> cartelleGruppi = new Groups(Folder.evaluation(idCat)).get();
			for (int i = 0; i < cartelleGruppi.size(); i++){
				Group singleGroup = cartelleGruppi.get(i);
				List<String> str = singleGroup.keywords();
				System.out.println(str.toString());
				
				String text = str.toString();
		     	text = text.substring(1, text.length()-1);
		     	text= text.replace(" ", "+");
		     	text= text.replace("%", "+");
		     	//System.out.println("Send Http GET request");
		     	Vector vettURLEntities = sendGet(text);
		      
		     	//per ogni entit� recupera i tipi a cui � associata
		     	tuttiTypes = new Vector();
		     	int cont = 0;
		     	for (int n = 0; n< vettURLEntities.size(); n++){	
		     		Vector ris = indLucene.interrogaIndiceType((String)vettURLEntities.elementAt(n));
		     		if (ris.size()==0){
		     			cont++;
		     			//System.out.println("\n "+(String)vettURLEntit�.elementAt(n));
		     		}
		     		else{
		     			tuttiTypes.addAll(ris);
		     		}		     		
		     	}
		     	System.out.println("di "+cont+" su "+vettURLEntities.size()+" non è stato trovato il tipo");
		     	System.out.println("Si vorrebbe ottenere "+singleGroup.name());
		     	//dati i type recupero depth e ancestor
		     	indLucene = new Indice();
		     	String tipoConScoreMax = indLucene.interrogaIndicePath(tuttiTypes);
		     	
			}
		
		}
		
	}

	private static Vector sendGet(String text) {

		Vector<String> vettUrl = new Vector<String>();
		try{
			//String url = "http://api.machinelinking.com/annotate?"+ credenziali +"&text="+ text + "&"+ lang +"&disambiguation=1&" +
			String url = "http://api.machinelinking.com/annotate?"+ credenziali +"&text="+ text + "&disambiguation=1&" +
					"link=1&output_format=json";
			//System.out.print(url);
		  
			HttpURLConnection con = null;
			int responseCode= 0;
			while (responseCode != 200){
				Thread.sleep(1000);
				URL obj = new URL(url);
				con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("GET");
				//add request header
				con.setRequestProperty("User-Agent", USER_AGENT);
				responseCode= con.getResponseCode();
				
			}
			//LEGGO LA RISPOSTA HTTP
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		
			//creo un oggetto formato json e lo leggo
			// { = oggetto Json    [ = array json
			JSONObject jsonObj = JSONObject.fromObject(response.toString());
			JSONObject jsonObjAnnotation = (JSONObject) jsonObj.get("annotation");   //estraggo l'oggetto "annotation"
			String language = jsonObjAnnotation.getString("lang");					 //estraggo l'informazione riguardante la lingua
			if (language.equals("it") || (language.equals("en"))){                   //se � inglese o italiano vado avanti con la lingua (altrimenti problemi con endpoint)
				
				JSONArray jsonArrayKeyWord = (JSONArray) jsonObjAnnotation.get("keyword");   //estraggo l'oggetto contenente le diverse entit� trovate
				for (int i1=0; i1<jsonArrayKeyWord.size(); i1++) {
					//estraggo le informazioni riferite ad un entit�
					JSONObject jObjWord= jsonArrayKeyWord.getJSONObject(i1);
					JSONArray linksEsterni = jObjWord.getJSONArray("external");   //estraggo array di link esterni
					
					for (int a = 0; a < linksEsterni.size(); a++){
						JSONObject link = linksEsterni.getJSONObject(a); 
						String risorsa = link.getString("resource");
						if (risorsa.equals("DBPedia")){   							//se proviene da dbpedia salvo la sua url per uso successivo
							String urlResource = link.getString("url").replaceFirst("page", "resource");
							if (urlResource.contains("à")){                     //non so xch� ma se ho un a accentata nell url la risposta http la modifica in a-tilde
								urlResource = urlResource.replace("à", "a");    //la rimodifico in �
								urlResource =  (String) urlResource.subSequence(0, urlResource.length()-1);  //tolgo lo spazio aggiunto alla fine
							}
							String urlResourceInglese = urlResource;
							if (language.equals("it") ){
								//trova l'URL della risorsa in inglese
								urlResourceInglese = interrogazioniSPARQL(urlResource);
							}
							if (!urlResourceInglese.equals(""))
								vettUrl.add(urlResourceInglese);
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Si è verificato un errore");
		}
		return vettUrl;
	}

	private static String interrogazioniSPARQL(String urlResource) {
		String label="";
		String endpoint = "http://dbpedia.org/sparql";
		urlResource = urlResource.replace("/page/", "/resource/");
		String sparqlQuery = "PREFIX  owl:  <http://www.w3.org/2002/07/owl#>"+
				"select ?urlInglese "+
				"where{ <"+ urlResource +"> owl:sameAs ?urlInglese FILTER regex(?urlInglese, \"http://dbpedia.org/\") }";
		
	    com.hp.hpl.jena.query.Query query = QueryFactory.create(sparqlQuery);
		 QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);
		ResultSet results = qexec.execSelect();
			
		while (results.hasNext()){ //metto un ciclo anche se solitamente la label riferita ad un'entit� dovrebbe essere una
			QuerySolution soln = results.nextSolution() ;
			RDFNode x = soln.get("?urlInglese") ; 
			 label = x.toString();
		}
		return label;
	}

	private static List<File> recuperaCartelleGruppi(String pathEvaluation) {
		File folder = new File(pathEvaluation);
		File[] listOfFiles = folder.listFiles();
		List<File> groupDirectory = new ArrayList<File>();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isDirectory()) {
				groupDirectory.add(listOfFiles[i]);
			} 
		}
		return groupDirectory;
	}
}

	


