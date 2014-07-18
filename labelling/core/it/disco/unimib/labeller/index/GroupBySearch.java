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
	private Score score;
	private KnowledgeBase knowledgeBase;
	private AlgorithmFields algorithmFields;
	
	public GroupBySearch(Directory indexDirectory, Score score, KnowledgeBase knowledgeBase) throws Exception{
		this.searcher = new IndexSearcher(DirectoryReader.open(indexDirectory));
		this.score = score;
		this.knowledgeBase = knowledgeBase;
		this.algorithmFields = new AlgorithmFields();
	}
	
	public long count(String predicate, String context, FullTextQuery query) throws Exception {
		TopDocs results = searcher.search(query.createQuery(predicate, 
															context,
															algorithmFields.property(),
															algorithmFields.context(),
															algorithmFields.namespace(),
															algorithmFields.analyzer()),
															1);
		return results.totalHits;
	}
	
	@Override
	public List<AnnotationResult> get(String value, String context, FullTextQuery query) throws Exception {
		TopDocs results = searcher.search(query.createQuery(value, 
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
			score.accumulate(document.getValues(knowledgeBase.label())[0], stem(StringUtils.join(document.getValues(algorithmFields.context()), " ")), stemmedContext);
		}
		List<AnnotationResult> annotations = score.toResults();
		score.clear();
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