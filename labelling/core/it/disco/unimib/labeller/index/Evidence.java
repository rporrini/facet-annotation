package it.disco.unimib.labeller.index;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.search.grouping.GroupingSearch;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

public class Evidence implements TripleStore{

	private IndexWriter writer;
	private Directory directory;
	private TripleIndex types;
	private TripleIndex labels;
	private RankingStrategy ranking;
	private TripleSelectionCriterion query;
	private IndexFields indexFields;
	private DirectoryReader reader;

	public Evidence(Directory directory, TripleIndex types, TripleIndex labels, RankingStrategy ranking, TripleSelectionCriterion query, IndexFields fields) throws Exception {
		this.directory = directory;
		this.types = types;
		this.labels = labels;
		this.ranking = ranking;
		this.query = query;
		this.indexFields = fields;
	}

	private String toResult(Document doc) {
		return doc.get(indexFields.predicateField());
	}

	private Document toDocument(NTriple triple) throws Exception {
		Document document = new Document();
		
		document.add(new Field(indexFields.property(), triple.predicate().uri(), TextField.TYPE_STORED));
		
		String value = triple.object().contains("http://") ? "" : triple.object();
		for(CandidateResource label : this.labels.get(triple.object(), "any")){
			value += " " + label.value();
		}		
		document.add(new Field(indexFields.literal(), value, TextField.TYPE_STORED));
		
		for(CandidateResource type : this.types.get(triple.object(), "any")){
			document.add(new Field(indexFields.objectType(), type.value(), TextField.TYPE_STORED));
		}
		
		String context = "";
		for(CandidateResource type : this.types.get(triple.subject(), "any")){
			for(CandidateResource label : this.labels.get(type.value(), "any")){
				context += " " + label.value();
			}
			document.add(new Field(indexFields.subjectType(), type.value(), TextField.TYPE_STORED));
		}
		document.add(new Field(indexFields.context(), context, TextField.TYPE_STORED));
		
		document.add(new Field(indexFields.namespace(), triple.predicate().namespace(), TextField.TYPE_STORED));
		document.add(new Field(indexFields.label(), triple.predicate().label(), TextField.TYPE_STORED));
		return document;
	}
	
	private List<ScoreDoc> matchingIds(String type, String context, IndexSearcher indexSearcher) throws Exception {
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

	private Analyzer analyzer() {
		return indexFields.analyzer();
	}
	
	public Evidence add(NTriple triple) throws Exception {
		openWriter().addDocument(toDocument(triple));
		return this;
	}
	
	public Evidence closeWriter() throws Exception {
		openWriter().close();
		return this;
	}
	
	private synchronized IndexWriter openWriter() throws Exception{
		if(writer == null){
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_45, analyzer())
																					.setRAMBufferSizeMB(95));
		}
		return writer;
	}
	
	public List<CandidateResource> get(String type, String context) throws Exception {
		ArrayList<CandidateResource> results = new ArrayList<CandidateResource>();
		IndexSearcher indexSearcher = new IndexSearcher(openReader());
		for(ScoreDoc documentPointer : matchingIds(type, context, indexSearcher)){
			Document indexedDocument = indexSearcher.doc(documentPointer.doc);
			results.add(new CandidateResource(toResult(indexedDocument), documentPointer.score));
		}
		return results;
	}
	
	private IndexReader openReader() throws Exception{
		if(reader == null){
			 reader = DirectoryReader.open(directory);
		}
		return reader;
	}
}