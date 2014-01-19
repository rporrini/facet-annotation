package it.unimib.disco.annotation;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

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

public class Trank {

	/**
	 * @param args
	 */
	private final String USER_AGENT = "Mozilla/25.0.1";
	String credenziali = "app_id=bbff1282&app_key=bfb47741212ddde1d0d56eacc0141512";
	String lang = "lang=it";
	static int contRiga = 0;
	public static String pathCartellaGroupsEvaluation = "C:/Users/Vale/Desktop/Magistrale Secondo Anno/Intelligenza Artificiale/pratica/evaluation/group";
	public static String pathEtichettaturaGR = "C:/Users/Vale/Desktop/Magistrale Secondo Anno/Intelligenza Artificiale/pratica/evaluation/";
	public static String pathfileGRXML = "C:/Users/Vale/Desktop/Magistrale Secondo Anno/Intelligenza Artificiale/pratica/evaluation/";
	
	public static String pathGroups = "C:/Users/VALE/evaluation/group/";
	
	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		Trank http = new Trank();
		Indice indLucene = new Indice();
		
		String indexPath = "C:/Users/VALE/Desktop/trank-indexes/pathindex";
		String indexPathType = "C:/Users/VALE/Desktop/trank-indexes/typeindex/";
		String indexPathLabel = "C:/Users/VALE/Desktop/trank-indexes/uriindex/";
		
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
		//apertura indice path
		Directory index = FSDirectory.open(new File(indexPath));
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		//apertura indice Type
		Directory indexType = FSDirectory.open(new File(indexPathType));
		IndexReader readerType = DirectoryReader.open(indexType);
		IndexSearcher searcherType = new IndexSearcher(readerType);
		
		//con machine linking il primo riferimento che ottengo a DBpedia � questa URI
		String uri = "uri:http***dbpedia*org*resource*Nature";
		Vector URIsTipi = new Vector();
		Query qType = new QueryParser(Version.LUCENE_42, "uri", analyzer).parse(uri);
		//risultati
		TopScoreDocCollector collectorType = TopScoreDocCollector.create(10, true);
		searcherType.search(qType, collectorType);
		ScoreDoc[] docsType = collectorType.topDocs().scoreDocs;
		for (int i = 0; i < docsType.length; i++) {
			Document result = searcherType.doc(docsType[i].doc);
			List<IndexableField> listaTipi = result.getFields();
			for (int j=0; j< listaTipi.size(); j++){
				String URItipo = (listaTipi.get(j)).stringValue();
				URIsTipi.add(URItipo);
				System.out.println(result);
			}
		}
		
		//esempio query perch� nella fase prima http://dbpedia/org/class/yago/Demons � il primo tipo che recupero
		//se cerco la stessa identica stringa con luke funziona qui no
		String querystr1 = "uri:http***dbpedia*org*class*yago*Demons";
		
		Directory directory = FSDirectory.open(new File("C:/Users/VALE/Desktop/trank-indexes/pathindex"));
		IndexReader indexReader = IndexReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);

		analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
		QueryParser queryParser = new QueryParser(Version.LUCENE_CURRENT, "uri", analyzer);
		Query query = queryParser.parse(querystr1);
		TopDocs hits = indexSearcher.search(query, 10);
		System.out.println("Number of hits: " + hits.totalHits);

		
		
		
		  }
	}

	


