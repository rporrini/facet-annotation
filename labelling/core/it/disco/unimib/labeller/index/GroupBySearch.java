package it.disco.unimib.labeller.index;

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
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

public class GroupBySearch implements Index{

	private IndexSearcher searcher;
	private Occurrences occurrences;
	private KnowledgeBase knowledgeBase;
	private AlgorithmFields algorithmFields;
	
	public GroupBySearch(Directory indexDirectory, Occurrences score, KnowledgeBase knowledgeBase) throws Exception{
		this.searcher = new IndexSearcher(DirectoryReader.open(indexDirectory));
		this.occurrences = score;
		this.knowledgeBase = knowledgeBase;
		this.algorithmFields = new AlgorithmFields();
	}
	
	@Override
	public long count(String predicate, String context, SelectionCriterion query) throws Exception {
		TopDocs results = searcher.search(query.asQuery(predicate, 
															context,
															algorithmFields.property(),
															algorithmFields.context(),
															algorithmFields.namespace(),
															algorithmFields.analyzer()),
															1);
		return results.totalHits;
	}
	
	@Override
	public List<CandidatePredicate> get(String value, String context, SelectionCriterion query) throws Exception {
		TopDocs results = searcher.search(query.asQuery(value, 
										  context, 
										  algorithmFields.literal(), 
										  algorithmFields.context(), 
										  algorithmFields.namespace(), 
										  algorithmFields.analyzer()),
										  1000000);
		
		String stemmedContext = stem(context);
		for(ScoreDoc result : results.scoreDocs){
			HashSet<String> fields = new HashSet<String>(Arrays.asList(new String[]{algorithmFields.label(), algorithmFields.context(), algorithmFields.property()}));
			Document document = searcher.doc(result.doc, fields);
			occurrences.accumulate(document.getValues(knowledgeBase.label())[0], stem(StringUtils.join(document.getValues(algorithmFields.context()), " ")), stemmedContext);
		}
		List<CandidatePredicate> annotations = occurrences.toResults();
		occurrences.clear();
		return annotations;
	}

	private String stem(String context) throws IOException {
		String stemmedContext = "";
		TokenStream stream = algorithmFields.analyzer().tokenStream("any", new StringReader(context));
		CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
		stream.reset();
		while(stream.incrementToken()) {
            stemmedContext = stemmedContext + " " + termAtt.toString();
        }
		return stemmedContext.trim();
	}
}