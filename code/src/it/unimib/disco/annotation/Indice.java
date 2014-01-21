package it.unimib.disco.annotation;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

//commento
public class Indice {

	/**
	 * @param args
	 */
	String indexPath = "C:/Users/VALE/Desktop/trank-indexes/pathindex";
	String indexPathType = "C:/Users/VALE/Desktop/trank-indexes/typeindex/";
	String indexPathLabel = "C:/Users/VALE/Desktop/trank-indexes/uriindex/";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
//kk
	public void interrogaIndice(Vector vettURL) throws CorruptIndexException, IOException, ParseException {
		// TODO Auto-generated method stub
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
		//apertura indice path
		Directory index = FSDirectory.open(new File(indexPath));
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		//apertura indice Type
		Directory indexType = FSDirectory.open(new File(indexPathType));
		IndexReader readerType = DirectoryReader.open(indexType);
		IndexSearcher searcherType = new IndexSearcher(readerType);
		//apertura indice label
	/*	Directory indexLabel = FSDirectory.open(new File(indexPathLabel));
		IndexReader readerLabel = DirectoryReader.open(indexLabel);
		IndexSearcher searcherLabel = new IndexSearcher(readerLabel);
	*/	
		String querystr1 = "uri:http***dbpedia*org*class*yago*Demons";
		Query q = new QueryParser(Version.LUCENE_CURRENT, "uri", analyzer).parse(querystr1);
		//risultati
		TopScoreDocCollector collector = TopScoreDocCollector.create(10, true);
		searcher.search(q, collector);
		ScoreDoc[] docs = collector.topDocs().scoreDocs;
		for (int i = 0; i < docs.length; i++) {
		  Document result = searcher.doc(docs[i].doc);
		  System.out.println(result);
		}
		WordUrls url = (WordUrls) vettURL.elementAt(0);
		String uri = url.getVettUrl().elementAt(0);
		uri = uri.replaceAll(":", "*");
		uri = uri.replaceFirst("\\.", "*");
		uri = uri.replaceAll("/", "*");
		//query
		String querystr = "uri:"+uri;
		 querystr = "uri:http***dbpedia*org*class*yago*Demons";
		//String querystr = "uri:\"http://dbpedia.org/resource/Nature\"";
		//String querystr = "uri:http***dbpedia*org*resource*nature";
		//query
	/*	Vector URIsTipi = new Vector();
				Query qType = new QueryParser(Version.LUCENE_42, "uri", analyzer).parse(querystr);
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
						//System.out.println(result);
					}
				}
				
				String uri1 = (String) URIsTipi.elementAt(0);
				uri1 = uri1.replaceAll(":", "*");
				uri1 = uri1.replaceFirst("\\.", "*");
				uri1 = uri1.replaceAll("/", "*");
				//query
				// String querystr1 = "uri:"+uri;
	/*			String querystr1 = "uri:http***dbpedia*org*class*yago*Demons";
		Query q = new QueryParser(Version.LUCENE_42, "path", analyzer).parse(querystr1);
		//risultati
		TopScoreDocCollector collector = TopScoreDocCollector.create(10, true);
		searcher.search(q, collector);
		ScoreDoc[] docs = collector.topDocs().scoreDocs;
		for (int i = 0; i < docs.length; i++) {
		  Document result = searcher.doc(docs[i].doc);
		  System.out.println(result);
		}
		*/
		
		
	
	}
}
