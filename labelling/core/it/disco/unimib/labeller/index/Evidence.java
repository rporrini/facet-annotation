package it.disco.unimib.labeller.index;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
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

public class Evidence extends TripleIndex{

	private TripleIndex types;
	private TripleIndex labels;
	private RankingStrategy ranking;
	private FullTextQuery query;
	private KnowledgeBase knowledgeBase;
	private AlgorithmFields algorithmFields;

	public Evidence(Directory directory, TripleIndex types, TripleIndex labels, RankingStrategy ranking, FullTextQuery query, KnowledgeBase knowledgeBase) throws Exception {
		super(directory);
		this.types = types;
		this.labels = labels;
		this.ranking = ranking;
		this.query = query;
		this.knowledgeBase = knowledgeBase;
		this.algorithmFields = new AlgorithmFields();
	}

	@Override
	protected String toResult(Document doc) {
		return doc.get(knowledgeBase.label());
	}

	@Override
	protected Document toDocument(NTriple triple) throws Exception {
		Document document = new Document();
		
		document.add(new Field(algorithmFields.property(), triple.predicate().uri(), TextField.TYPE_STORED));
		String value = triple.object().contains("http://") ? "" : triple.object();
		for(CandidatePredicate label : this.labels.get(triple.object(), "any")){
			value += " " + label.value();
		}		
		document.add(new Field(algorithmFields.literal(), value, TextField.TYPE_STORED));
		
		String context = "";
		for(CandidatePredicate type : this.types.get(triple.subject(), "any")){
			for(CandidatePredicate label : this.labels.get(type.value(), "any")){
				context += " " + label.value();
			}
		}
		document.add(new Field(algorithmFields.context(), context, TextField.TYPE_STORED));
		
		document.add(new Field(algorithmFields.namespace(), triple.predicate().namespace(), TextField.TYPE_STORED));
		document.add(new Field(algorithmFields.label(), triple.predicate().label(), TextField.TYPE_STORED));
		return document;
	}
	
	@Override
	protected List<ScoreDoc> matchingIds(String type, String context, IndexSearcher indexSearcher) throws Exception {
		List<ScoreDoc> ids = new ArrayList<ScoreDoc>();
 		GroupingSearch groupingSearch = new GroupingSearch(knowledgeBase.label());
		groupingSearch.setGroupSort(Sort.RELEVANCE);
		groupingSearch.setIncludeScores(true);
		Query query = this.query.createQuery(type, context, algorithmFields.literal(), algorithmFields.context(), algorithmFields.namespace(), analyzer());
		for(GroupDocs<BytesRef> group : groupingSearch.<BytesRef>search(indexSearcher, query, 0, 1000).groups){
			ranking.reRank(context, group, indexSearcher);
			ids.add(group.scoreDocs[0]);
		}
		return ids;
	}

	@Override
	protected Analyzer analyzer() {
		return algorithmFields.analyzer();
	}
}