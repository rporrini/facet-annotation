package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.benchmark.Events;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

public class GroupBySearch implements Index{

	private IndexSearcher searcher;
	private Occurrences occurrences;
	private IndexFields indexFields;
	
	public GroupBySearch(Directory indexDirectory, Occurrences score, IndexFields fields) throws Exception{
		this.searcher = new IndexSearcher(DirectoryReader.open(indexDirectory));
		this.occurrences = score;
		this.indexFields = fields;
	}
	
	@Override
	public long countPredicatesInContext(String predicate, String context, TripleSelectionCriterion query) throws Exception {
		int howMany = 1;
		BooleanQuery asQuery = query.asQuery(predicate, 
														context,
														indexFields.predicateField(),
														indexFields.context(),
														indexFields.namespace(),
														indexFields.analyzer());
		TopDocs results = runQuery(howMany, asQuery);
		return results.totalHits;
	}

	public long countValuesForPredicates(String value, String predicate) throws Exception {
		int howMany = 1;
		BooleanQuery asQuery = new CompleteContext(new AllValues()).asQuery(value, 
																		predicate, 
																		indexFields.literal(), 
																		indexFields.predicateField(), 
																		indexFields.namespace(), 
																		indexFields.analyzer());
		TopDocs results = runQuery(howMany, asQuery);
		return results.totalHits;
	}
	
	@Override
	public List<CandidatePredicate> get(String value, String context, TripleSelectionCriterion query) throws Exception {
		int howMany = 1000000;
		BooleanQuery q = query.asQuery(value, 
									  context, 
									  indexFields.literal(), 
									  indexFields.context(), 
									  indexFields.namespace(), 
									  indexFields.analyzer());
		TopDocs results = runQuery(howMany, q);
		
		String stemmedContext = stem(context);
		for(ScoreDoc result : results.scoreDocs){
			HashSet<String> fields = new HashSet<String>(Arrays.asList(new String[]{indexFields.label(), indexFields.context(), indexFields.property()}));
			Document document = searcher.doc(result.doc, fields);
			occurrences.accumulate(document.getValues(indexFields.predicateField())[0], stem(StringUtils.join(document.getValues(indexFields.context()), " ")), stemmedContext);
		}
		List<CandidatePredicate> annotations = occurrences.toResults();
		occurrences.clear();
		return annotations;
	}
	
	private TopDocs runQuery(int howMany, BooleanQuery asQuery) throws Exception {
		new Events().debug(asQuery);
		return searcher.search(asQuery,howMany);
	}

	private String stem(String context) throws IOException {
		String stemmedContext = "";
		TokenStream stream = indexFields.analyzer().tokenStream("any", new StringReader(context));
		CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
		stream.reset();
		while(stream.incrementToken()) {
            stemmedContext = stemmedContext + " " + termAtt.toString();
        }
		return stemmedContext.trim();
	}
}