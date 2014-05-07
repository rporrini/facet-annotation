package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.labelling.Events;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

public class GroupBySearch implements Index{

	private IndexSearcher searcher;
	private FullTextQuery query;
	private Score score;
	
	public GroupBySearch(Directory indexDirectory, Score score, FullTextQuery query) throws Exception{
		this.searcher = new IndexSearcher(DirectoryReader.open(indexDirectory));
		this.query = query;
		this.score = score;
	}
	
	@Override
	public List<AnnotationResult> get(String type, String context) throws Exception {
		TopDocs results = searcher.search(query.createQuery(type, 
										  context, 
										  literal(), 
										  context(), 
										  namespace(), 
										  analyzer()),
						  100000);
		new Events().debug("Got " + results.totalHits + " predicates.");
		for(ScoreDoc result : results.scoreDocs){
			HashSet<String> fields = new HashSet<String>(Arrays.asList(new String[]{label(), context()}));
			Document document = searcher.doc(result.doc, fields);
			score.accumulate(document.getValues(label())[0], StringUtils.join(document.getValues(context()), " "));
		}
		List<AnnotationResult> annotations = score.toResults();
		score.clear();
		return annotations;
	}
	
	private Analyzer analyzer() {
		Map<String, Analyzer> analyzers = new HashMap<String, Analyzer>();
		analyzers.put(property(), new KeywordAnalyzer());
		analyzers.put(namespace(), new KeywordAnalyzer());
		analyzers.put(label(), new KeywordAnalyzer());
		return new PerFieldAnalyzerWrapper(new EnglishAnalyzer(Version.LUCENE_45), analyzers);
	}
	
	private String label(){
		return "label";
	}
	
	private String namespace(){
		return "namespace";
	}
	
	private String literal() {
		return "literal";
	}
	
	private String property() {
		return "property";
	}
	
	private String context() {
		return "context";
	}
}