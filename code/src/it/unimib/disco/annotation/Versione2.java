package it.unimib.disco.annotation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.RDFNode;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//prova
public class Versione2 {
	
	private final String USER_AGENT = "Mozilla/25.0.1";
	String credenziali = "app_id=bbff1282&app_key=bfb47741212ddde1d0d56eacc0141512";
	String lang = "lang=it";
	static int contRiga = 0;
	public static String pathCartellaGroupsEvaluation = "C:/Users/Vale/Desktop/Magistrale Secondo Anno/Intelligenza Artificiale/pratica/evaluation/group";
	public static String pathEtichettaturaGR = "C:/Users/Vale/Desktop/Magistrale Secondo Anno/Intelligenza Artificiale/pratica/evaluation/";
	public static String pathfileGRXML = "C:/Users/Vale/Desktop/Magistrale Secondo Anno/Intelligenza Artificiale/pratica/evaluation/";
			
	
	public static void main(String[] args) throws Exception {	
		// TODO Auto-generated method stub
		Versione2 http = new Versione2();
		Etichetta etichetta = new Etichetta();
		 
		File dir_data = new File(pathCartellaGroupsEvaluation);
 		File[] files = dir_data.listFiles();
 		File tmp_f = null;
 		
 		for (int i=0; i < files.length; i++){
 			tmp_f = files[i];
 			contRiga = 0;
		    //crea nuovo file etichetta 
			FileWriter writer = new FileWriter(pathEtichettaturaGR + "eticGR" + i + ".txt");
         
			String path = tmp_f.getPath();
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String text = reader.readLine();
		
			contRiga++;
			//caso in cui gli passo una riga intera
		    while(text!=null) {
		    	String originalText = text;
		     	text = text.substring(1, text.length()-1);
		     	// STAMPA ->  System.out.println(text);
		     	text= text.replace(" ", "+");
		     	text= text.replace("%", "+");
		     	http.generaFileXML(contRiga, i);
		     	System.out.println("Send Http GET request");
		     	http.sendGet(text, i);
		     	etichetta.generaEtichetta(originalText, contRiga, i, pathfileGRXML, pathEtichettaturaGR);
				text = reader.readLine();
				contRiga++;
			}
		  
		//caso in cui gli passo una parola per volta (parola si intende espressione delimitata da virgole
	/*		while(text!=null) {
				String originalText = text;
				text = text.substring(1, text.length()-1);
				System.out.println(text);
				text= text.replace(" ", "+");
				text= text.replace("%", "+");
				text = text.concat(",");
				int indiceFine = text.indexOf(",");
				http.generaFileXML(contRiga, i);
				while (indiceFine != -1){
		    	
					String textWord = text.substring(0, indiceFine);
					System.out.println("Send Http GET request");
					http.sendGet(textWord, i);
					text = text.substring(indiceFine+1);
					indiceFine = text.indexOf(",");
				}
		  
				etichetta.generaEtichetta(originalText, contRiga, i);
				text = reader.readLine();
				contRiga++;
			}*/
			reader.close();
		  }
	}
 
	private void generaFileXML(int contRiga2, int i2) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder(); 
		
		Element rootElement = new Element("group");
		Document document = new Document(rootElement);
		XMLOutputter outputter = new XMLOutputter();
		outputter.output(document, new FileOutputStream(pathfileGRXML + "fileGR"+ i2 + contRiga2 +".xml"));
		
	}

	// HTTP GET request
	private void sendGet(String text, int i2) throws Exception {

		Vector<WordUrls> vettWordUrls = new Vector<WordUrls>();
		try{
			//String url = "http://api.machinelinking.com/annotate?"+ credenziali +"&text="+ text + "&"+ lang +"&disambiguation=1&" +
			String url = "http://api.machinelinking.com/annotate?"+ credenziali +"&text="+ text + "&disambiguation=1&" +
					"link=1&output_format=json";
			System.out.print(url);
		  
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
		
			//print result -> System.out.println(response.toString());
		
			Vector<String> vettUrl = new Vector<String>();
			vettWordUrls = new Vector<WordUrls>();
		     
			//creo un oggetto formato json e lo leggo
			// { = oggetto Json    [ = array json
			JSONObject jsonObj = JSONObject.fromObject(response.toString());
			JSONObject jsonObjAnnotation = (JSONObject) jsonObj.get("annotation");   //estraggo l'oggetto "annotation"
			String language = jsonObjAnnotation.getString("lang");					 //estraggo l'informazione riguardante la lingua
			if (language.equals("it") || (language.equals("en"))){                   //se è inglese o italiano vado avanti con la lingua (altrimenti problemi con endpoint)
				
				JSONArray jsonArrayKeyWord = (JSONArray) jsonObjAnnotation.get("keyword");   //estraggo l'oggetto contenente le diverse entità trovate
				for (int i=0; i<jsonArrayKeyWord.size(); i++) {
					//estraggo le informazioni riferite ad un entità
					JSONObject jObjWord= jsonArrayKeyWord.getJSONObject(i);
					String parola = jObjWord.getString("form");   	//nome entità
					double relevance = jObjWord.getLong("rel");     //rilevanza entità
			    
					JSONArray linksEsterni = jObjWord.getJSONArray("external");   //estraggo array di link esterni
					vettUrl = new Vector<String>();
					for (int a = 0; a < linksEsterni.size(); a++){
						JSONObject link = linksEsterni.getJSONObject(a); 
						String risorsa = link.getString("resource");
						if (risorsa.equals("DBPedia")){   							//se proviene da dbpedia salvo la sua url per uso successivo
							String urlResource = link.getString("url").replaceFirst("page", "resource");
							if (urlResource.contains("Ã")){                     //non so xchè ma se ho un a accentata nell url la risposta http la modifica in a-tilde
								urlResource = urlResource.replace("Ã", "à");    //la rimodifico in à
								urlResource =  (String) urlResource.subSequence(0, urlResource.length()-1);  //tolgo lo spazio aggiunto alla fine
							}
							vettUrl.add(urlResource);
						}
					}
					//per ogni entità trovata salvo parola-rilevanza-url verso DBpedia per poi salvare le informazioni utili in un file xml
					vettWordUrls.add(new WordUrls(parola, relevance, vettUrl));
				}
		     
				//dato il link a DBPEDIA vado a trovare il type, le proprieties e la categoria  
				for (int j = 0; j < vettWordUrls.size(); j++){
					WordUrls ogg = vettWordUrls.elementAt(j);
			    	
					Query query;
					QueryExecution qexec;
					String endpoint = "http://it.dbpedia.org/sparql";
					if (language.equals("en"))    //se la lingua è inglese cambio endpoint
						endpoint = "http://dbpedia.org/sparql";
					ResultSet results;
				    	 
					for (int k = 0; k < ogg.getVettUrl().size(); k++){
						try {
							//apro il file xml su cui scriverò i risultati delle query
							SAXBuilder builder = new SAXBuilder(); 
							Document document = (Document) builder.build(new File(pathfileGRXML + "fileGR" + i2 + contRiga + ".xml"));
			    				
							Element root = document.getRootElement();    //estraggo la radice GRUPPO e vado ad aggiungergli un nuovo nodo entità
							Element newEntity = new Element("Entity").setAttribute("words", ogg.getParola());
							//estraggo label dell'entità associata ad una certa entità associata alla parola in ingresso
							String sparqlQueryString1= "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
									"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
									"select ?l "+
									"where{ <"+ ogg.getVettUrl().elementAt(k) +"> rdfs:label ?l." +
											 "FILTER ( lang(?l) = '"+ language +"')}";
							query = QueryFactory.create(sparqlQueryString1);
							qexec = QueryExecutionFactory.sparqlService(endpoint, query);
							results = qexec.execSelect();
			    				
							while (results.hasNext()){ //metto un ciclo anche se solitamente la label riferita ad un'entità dovrebbe essere una
								QuerySolution soln = results.nextSolution() ;
								RDFNode x = soln.get("?l") ;       
								newEntity = newEntity.setAttribute("nome", x.toString());
								newEntity = newEntity.setAttribute("relevance", Double.toString(ogg.getRelevance()));
								root.addContent(newEntity);   //AGGIUNGO LA NUOVA ENTITA' "ENTITY"
							}
			    				
							//query per estrarre proprietà TYPE
							Element NewSubEntity = null;    //PER OGNI PROPRIETA' AGGIUNGO UN TAG TYPE CONTENENTE IL NOME DI ESSA
							sparqlQueryString1= "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
												"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
												"select ?type ?l "+
												"where{ <"+ ogg.getVettUrl().elementAt(k) +"> rdf:subClassOf ?type." +
														 "?type rdfs:label ?l}";
							query = QueryFactory.create(sparqlQueryString1);
							qexec = QueryExecutionFactory.sparqlService(endpoint, query);
							results = qexec.execSelect();
			    				
							while (results.hasNext()){
								QuerySolution soln = results.nextSolution() ;
								/* SE VOGLIO MANTENERE ANCHE IL LINK VERO E PROPRIO -> RDFNode x = soln.get("?type") ;       // Get a result variable by name.
			    				//  												   String s = x.toString(); //tiro fuori il link
								* */
								RDFNode x = soln.get("?l") ;       //PER ADESSO ESTRAGGO SOLO LA LABEL DEL TYPE
								String s = x.toString();
								NewSubEntity = new Element("TYPE").setText(s);
								newEntity.addContent(NewSubEntity);   //AGGIUNGO IL NODO type AD ENTITY
							}
							// PER MOSTRARE I RISULTATI A VIDEO ResultSetFormatter.out(System.out, results, query);   
			    		 
							//query per estrarre proprietà CATEGORY
							sparqlQueryString1= "PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
												"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
												"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
												"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
												"PREFIX dcterms: <http://purl.org/dc/terms/>"+
												"select ?category ?l "+
												"where { <"+ ogg.getVettUrl().elementAt(k) +"> dcterms:subject ?category." +
														  "?category rdfs:label ?l }";
							query = QueryFactory.create(sparqlQueryString1);
							qexec = QueryExecutionFactory.sparqlService(endpoint, query);
							results = qexec.execSelect();
			    		 
							while (results.hasNext()){
								QuerySolution soln = results.nextSolution() ;
								/* SE VOGLIO MANTENERE ANCHE IL LINK VERO E PROPRIO -> RDFNode x = soln.get("?category") ;      
			    				//                                                     String s = x.toString();
			    				*/
								RDFNode x = soln.get("?l") ;       //PER ADESSO ESTRAGGO SOLO LA LABEL DEL TYPE
								String s = x.toString();
								NewSubEntity = new Element("CATEGORY").setText(s);
								newEntity.addContent(NewSubEntity);   //AGGIUNGO IL NODO category AD ENTITY
							}
			    		 
							//query per property  select distinct ?property COUNT (distinct ?link)
							//                    where {?link ?property <http://dbpedia.org/resource/Red>}
							sparqlQueryString1= "PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
												"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
												"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
												"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
												"select distinct ?property (COUNT(DISTINCT ?link) AS ?count) ?l "+
												"where{?link ?property <"+ ogg.getVettUrl().elementAt(k) +">" +
															"FILTER regex(?property, \"http://it.dbpedia.org/property/\")" +    //seleziono solo le proprietà con un certo prefisso
														"?property rdfs:label ?l}" +   
												"GROUP BY ?property ?l";
							query = QueryFactory.create(sparqlQueryString1, Syntax.syntaxARQ);
							qexec = QueryExecutionFactory.sparqlService(endpoint, query);
							results = qexec.execSelect();
			    		
							while (results.hasNext()){
								QuerySolution soln = results.nextSolution() ;
								/* SE VOGLIO MANTENERE ANCHE IL LINK VERO E PROPRIO -> RDFNode x = soln.get("?property") ; 
			   					//                                                     String s = x.toString();
								*/
								RDFNode x = soln.get("?l") ;       //ESTRAGGO LA LABEL DEL TYPE
								String s = x.toString();
								NewSubEntity = new Element("PROPERTY").setText(s);
								x = soln.get("?count") ;       //ESTRAGGO il contatore degli elementi per cui vale quella proprietà
								com.hp.hpl.jena.graph.Node y = x.asNode();
								int valore = (int) y.getLiteralValue();
								NewSubEntity = NewSubEntity.setAttribute("countLink", Integer.toString(valore));
								newEntity.addContent(NewSubEntity);  //AGGIUNGO IL NODO AD ENTITY
							}
							//FINE QUERY
							qexec.close();
							//scrivi sul file
							XMLOutputter outputter = new XMLOutputter();
							outputter.output(document, new FileOutputStream(pathfileGRXML + "fileGR"+ i2 + contRiga +".xml"));
						} 
						catch (Exception e) {
							e.printStackTrace();
							System.out.println("Si è verificato un errore In versione2");
						}
		    			 //System.out.println(ogg.getParola()+" "+ogg.getRelevance()+ " "+ ogg.getVettUrl());
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Si è verificato un errore 22222222222");
		}
	}
}
