package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.predicates.AnnotationRequest;

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
	public long countPredicatesInContext(String predicate, String domain, TripleSelectionCriterion query) throws Exception {
		int howMany = 1;
		BooleanQuery asQuery = query.asQuery(predicate, 
											domain,
											indexFields.propertyId(),
											indexFields.context(),
											indexFields.namespace()).build();
		TopDocs results = runQuery(howMany, asQuery);
		return results.totalHits;
	}

	@Override
	public CandidateResourceSet get(AnnotationRequest request, TripleSelectionCriterion query) throws Exception {
		Stems stems = indexFields.toStems();
		ContextualizedOccurrences occurrences = new ContextualizedOccurrences(this.occurrences, stems.of(request.context()));
		int howMany = 1000000;
		BooleanQuery q = query.asQuery(request.elements()[0],
									  request.context(), 
									  indexFields.literal(), 
									  indexFields.context(), 
									  indexFields.namespace()).build();
		
		HashSet<String> fields = indexFields.fieldsToRead();
		
		for(ScoreDoc result : runQuery(howMany, q).scoreDocs){
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