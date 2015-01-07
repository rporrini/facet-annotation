package it.disco.unimib.labeller.index;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;

public class IndexQuery {

	private BooleanQuery query;

	public IndexQuery(){
		this.query = new BooleanQuery();
	}
	
	public Query build() {
		return query;
	}

	public IndexQuery matchExactly(String value, String field) {
		PhraseQuery phraseQuery = new PhraseQuery();
		phraseQuery.add(new Term(field, value));
		query.add(phraseQuery, Occur.MUST);
		return this;
	}
}
