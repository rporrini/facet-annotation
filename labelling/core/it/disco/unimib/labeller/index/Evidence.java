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
	private SelectionCriterion query;
	private IndexFields indexFields;

	public Evidence(Directory directory, TripleIndex types, TripleIndex labels, RankingStrategy ranking, SelectionCriterion query, IndexFields fields) throws Exception {
		super(directory);
		this.types = types;
		this.labels = labels;
		this.ranking = ranking;
		this.query = query;
		this.indexFields = fields;
	}

	@Override
	protected String toResult(Document doc) {
		return doc.get(indexFields.predicateField());
	}

	@Override
	protected Document toDocument(NTriple triple) throws Exception {
		Document document = new Document();
		
		document.add(new Field(indexFields.property(), triple.predicate().uri(), TextField.TYPE_STORED));
		String value = triple.object().contains("http://") ? "" : triple.object();
		for(CandidatePredicate label : this.labels.get(triple.object(), "any")){
			value += " " + label.value();
		}		
		document.add(new Field(indexFields.literal(), value, TextField.TYPE_STORED));
		
		String context = "";
		for(CandidatePredicate type : this.types.get(triple.subject(), "any")){
			for(CandidatePredicate label : this.labels.get(type.value(), "any")){
				context += " " + label.value();
			}
		}
		document.add(new Field(indexFields.context(), context, TextField.TYPE_STORED));
		
		document.add(new Field(indexFields.namespace(), triple.predicate().namespace(), TextField.TYPE_STORED));
		document.add(new Field(indexFields.label(), triple.predicate().label(), TextField.TYPE_STORED));
		return document;
	}
	
	@Override
	protected List<ScoreDoc> matchingIds(String type, String context, IndexSearcher indexSearcher) throws Exception {
		List<ScoreDoc> ids = new ArrayList<ScoreDoc>();
 		GroupingSearch groupingSearch = new GroupingSearch(indexFields.predicateField());
		groupingSearch.setGroupSort(Sort.RELEVANCE);
		groupingSearch.setIncludeScores(true);
		Query query = this.query.asQuery(type, context, indexFields.literal(), indexFields.context(), indexFields.namespace(), analyzer());
		for(GroupDocs<BytesRef> group : groupingSearch.<BytesRef>search(indexSearcher, query, 0, 1000).groups){
			ranking.reRank(context, group, indexSearcher);
			ids.add(group.scoreDocs[0]);
		}
		return ids;
	}

	@Override
	protected Analyzer analyzer() {
		return indexFields.analyzer();
	}
}