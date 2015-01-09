package it.disco.unimib.labeller.index;

import java.util.HashSet;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

public class ContextualizedEvidence implements Index{

	private IndexSearcher searcher;
	private SimilarityMetric occurrences;
	private IndexFields indexFields;
	
	public ContextualizedEvidence(Directory indexDirectory, SimilarityMetric score, IndexFields fields) throws Exception{
		this.searcher = new IndexSearcher(DirectoryReader.open(indexDirectory));
		this.occurrences = score;
		this.indexFields = fields;
	}
	
	@Override
	public long count(ContextualizedValues request, SelectionCriterion query) throws Exception {
		int howMany = 1;
		return runQuery(howMany, query.asQuery(request).build()).totalHits;
	}

	@Override
	public CandidateResourceSet get(ContextualizedValues request, SelectionCriterion query) throws Exception {
		Stems stems = indexFields.toStems();
		ContextualizedOccurrences occurrences = new ContextualizedOccurrences(this.occurrences, stems.of(request.domain()));
		int howMany = 1000000;
		HashSet<String> fields = indexFields.fieldsToRead();
		
		for(ScoreDoc result : runQuery(howMany, query.asQuery(request).build()).scoreDocs){
			Document document = searcher.doc(result.doc, fields);
			String predicate = document.getValues(indexFields.propertyId())[0];
			String context = stems.of(document.getValues(indexFields.context())[0]);
			String[] subjectTypes = document.getValues(indexFields.subjectType());
			String[] objectTypes = document.getValues(indexFields.objectType());
			occurrences.accumulate(predicate, context, subjectTypes, objectTypes);
		}
		return occurrences.asResults();
	}

	private TopDocs runQuery(int howMany, BooleanQuery asQuery) throws Exception {
		return searcher.search(asQuery, null, howMany, Sort.INDEXORDER, false, false);
	}
}