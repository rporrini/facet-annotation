package it.disco.unimib.labeller.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.search.grouping.GroupingSearch;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

public class ContextualizedPredicates extends TripleIndex{

	private TripleIndex types;
	private TripleIndex labels;
	private RankingStrategy ranking;
	private FullTextQuery query;
	private KnowledgeBase knowledgeBase;

	public ContextualizedPredicates(Directory directory, TripleIndex types, TripleIndex labels, RankingStrategy ranking, FullTextQuery query, KnowledgeBase knowledgeBase) throws Exception {
		super(directory);
		this.types = types;
		this.labels = labels;
		this.ranking = ranking;
		this.query = query;
		this.knowledgeBase = knowledgeBase;
	}

	@Override
	protected String toResult(Document doc) {
		return doc.get(knowledgeBase.label());
	}

	@Override
	protected Document toDocument(NTriple triple) throws Exception {
		Document document = new Document();
		
		document.add(new Field(property(), triple.predicate().uri(), TextField.TYPE_STORED));
		String value = triple.object().contains("http://") ? "" : triple.object();
		for(AnnotationResult label : this.labels.get(triple.object(), "any")){
			value += " " + label.value();
		}		
		document.add(new Field(literal(), value, TextField.TYPE_STORED));
		
		String context = "";
		for(AnnotationResult type : this.types.get(triple.subject(), "any")){
			for(AnnotationResult label : this.labels.get(type.value(), "any")){
				context += " " + label.value();
			}
		}
		document.add(new Field(context(), context, TextField.TYPE_STORED));
		
		document.add(new Field(namespace(), triple.predicate().namespace(), TextField.TYPE_STORED));
		document.add(new Field(label(), triple.predicate().label(), TextField.TYPE_STORED));
		return document;
	}
	
	@Override
	protected List<ScoreDoc> matchingIds(String type, String context, IndexSearcher indexSearcher) throws Exception {
		List<ScoreDoc> ids = new ArrayList<ScoreDoc>();
 		GroupingSearch groupingSearch = new GroupingSearch(knowledgeBase.label());
		groupingSearch.setGroupSort(Sort.RELEVANCE);
		groupingSearch.setIncludeScores(true);
		Query query = this.query.createQuery(type, context, literal(), context(), namespace(), analyzer());
		for(GroupDocs<BytesRef> group : groupingSearch.<BytesRef>search(indexSearcher, query, 0, 1000).groups){
			ranking.reRank(context, group, indexSearcher);
			ids.add(group.scoreDocs[0]);
		}
		return ids;
	}

	@Override
	protected Analyzer analyzer() {
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