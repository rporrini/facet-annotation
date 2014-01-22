package it.unimib.disco.annotation;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.apache.jena.atlas.json.JSON;
import org.apache.jena.atlas.json.JsonObject;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;


public class Indice {

	/**
	 * @param args
	 */
	String indexPath = "../trank-indexes/pathindex";
	String indexPathType = "../trank-indexes/typeindex/";
	String indexPathLabel = "../trank-indexes/uriindex/";
	
	public String[] interrogaIndiceType(String uriE) throws CorruptIndexException, IOException, ParseException {
		//apertura indice Type
		Directory indexType = FSDirectory.open(new File(indexPathType));
		IndexReader readerType = DirectoryReader.open(indexType);
		IndexSearcher searcherType = new IndexSearcher(readerType);
		String queryStr = uriE;
		TermQuery qt = new TermQuery(new Term("uri", queryStr));
		//trovo il miglior tipo
		TopDocs doc = searcherType.search(qt, 1);
		int c = doc.totalHits;
		String[] risultati = new String[0];
		if (doc.scoreDocs.length>0){
			Document result = searcherType.doc((doc.scoreDocs)[0].doc);
			risultati = result.getValues("type");
		}
		return risultati;
	}
	//String querystr = "uri:\"http://dbpedia.org/resource/Nature\"";
	//String querystr = "uri:http***dbpedia*org*resource*nature";
	//query
	

	public String interrogaIndicePath(Vector tuttiTypes) throws IOException {
		int score = 0;
		int max = 0;
		String uri="";
		//apertura indice path
		Directory index = NIOFSDirectory.open(new File(indexPath));
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		
		for (int i = 0; i < tuttiTypes.size(); i++){
			String[] tipiEnt =  (String[]) tuttiTypes.elementAt(i);
			
			for (int k=0; k < tipiEnt.length; k++){
				String queryStr = tipiEnt[k];
				TermQuery qt = new TermQuery(new Term("uri", queryStr));
				ScoreDoc[] doc = searcher.search(qt, 1).scoreDocs;
				if (doc.length > 0){
					Document result = searcher.doc(doc[0].doc);
					int depth= Integer.parseInt(result.get("level"));
					//JsonObject path = JSON.parse(result.get("path").);
					String g = result.get("path");
					g = g.replace("[", "");
					g = g.replace("]", "");
					List<String> wordList = Arrays.asList(g.split(","));  
					g.toString();
					
					//calcolo score di un dato tipo
					  //fare la sommatoria della profondit�  di tutti i suoi antenati se sono tipi della risorsa
					score = 0;
					String uriAntenato="";
					  for (int y = 0; y < wordList.size() ; y++){
						  uriAntenato = wordList.get(y);
						  uriAntenato = uriAntenato.replace("\"", "");
						  
						  //if per controllare l'appartenenza dell'antenato alla lista dei tipi per quella entita
						  if (controllaAppartenenzaAtipiEnt(tipiEnt, uriAntenato)){
						  //if per NON controllare l'appartenenza
						  //if (true){
							  TermQuery qLevelAntenato = new TermQuery(new Term("uri", uriAntenato));
							  ScoreDoc[] ris = searcher.search(qLevelAntenato, 1).scoreDocs;
							  result = searcher.doc(doc[0].doc);
							  depth= Integer.parseInt(result.get("level"));
							  score = score + depth;
						  }
					  }
					  if (score > max){
						  max = score;
						  uri = uriAntenato;
					  }
				  }
			}
				
			
		}
		System.out.println(" lo score per il tipo "+ uri +" è "+ max+"\n");
		return null;
	}


	public boolean controllaAppartenenzaAtipiEnt(String[] tipiEnt, String uriAnt) throws IOException {
		// TODO Auto-generated method stub
		boolean flag = false;
		
		for (int i = 0 ; i < tipiEnt.length; i++){
			if ((tipiEnt[i]).equals(uriAnt)){
				flag = true;
				break;
			}
		}
		return flag;
	}
	
}
