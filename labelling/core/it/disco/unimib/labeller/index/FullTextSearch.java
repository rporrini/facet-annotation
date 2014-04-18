package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.labelling.Events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.search.grouping.GroupingSearch;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

public class FullTextSearch extends Index{

	private Index types;
	private Index labels;

	public FullTextSearch(Directory directory, Index types, Index labels) throws Exception {
		super(directory);
		this.types = types;
		this.labels = labels;
	}

	@Override
	protected Analyzer analyzer() {
		Map<String, Analyzer> analyzers = new HashMap<String, Analyzer>();
		analyzers.put(property(), new KeywordAnalyzer());
		return new PerFieldAnalyzerWrapper(new StandardAnalyzer(Version.LUCENE_45), analyzers);
	}

	@Override
	protected String toResult(Document doc) {
		return doc.get(property());
	}

	@Override
	protected Document toDocument(NTriple triple) throws Exception {
		Document document = new Document();
		document.add(new Field(property(), triple.predicate(), TextField.TYPE_STORED));
		
		String value = triple.object().contains("http://") ? "" : triple.object();
		for(String label : this.labels.get(triple.object(), "any")){
			value += " " + label;
		}		
		document.add(new Field(literal(), value, TextField.TYPE_STORED));
		
		String context = "";
		for(String type : this.types.get(triple.subject(), "any")){
			for(String label : this.labels.get(type, "any")){
				context += " " + label;
			}
		}
		document.add(new Field(context(), context, TextField.TYPE_STORED));
		return document;
	}
	
	@Override
	protected List<Integer> matchingIds(String type, String context, IndexSearcher indexSearcher) throws Exception {
		List<Integer> ids = new ArrayList<Integer>();
 		GroupingSearch groupingSearch = new GroupingSearch(property());
		groupingSearch.setGroupSort(Sort.RELEVANCE);
		groupingSearch.setIncludeScores(true);
		Query query = toQuery(type, context);
		for(GroupDocs<BytesRef> group : groupingSearch.<BytesRef>search(indexSearcher, query, 0, 1000).groups){
			new Events().info(indexSearcher.explain(query, group.scoreDocs[0].doc));
			ids.add(group.scoreDocs[0].doc);
		}
		return ids;
	}
	
	private Query toQuery(String type, String context) throws Exception {
		BooleanQuery query = new BooleanQuery();
		query.clauses().add(new BooleanClause(new StandardQueryParser(analyzer()).parse(type, literal()), Occur.MUST));
		query.clauses().add(new BooleanClause(new StandardQueryParser(analyzer()).parse(context, context()), Occur.SHOULD));
		return query;
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
