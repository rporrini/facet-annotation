package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.ContextualizedPredicates;
import it.disco.unimib.labeller.index.CountPredicates;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.GroupBySearch;
import it.disco.unimib.labeller.index.KnowledgeBase;
import it.disco.unimib.labeller.index.OptionalContext;
import it.disco.unimib.labeller.index.RankByFrequency;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class GroupBySearchTest {

	@Test
	public void shouldGive0ResultsWhenQueriedAgainstAnEmptyIndex() throws Exception {
		Directory directory = new RAMDirectory();
		
		new EntityValues(directory).closeWriter();
		
		GroupBySearch index = new GroupBySearch(directory , new CountPredicates(), new KnowledgeBase("dbpedia"));
		
		assertThat(index.count("any", "any", new OptionalContext()), is(equalTo(0l)));
	}
	
	@Test
	public void shouldGiveOneResultWhenMatchesPredicate() throws Exception {
		Directory directory = new RAMDirectory();
		new ContextualizedPredicates(directory, 
									new EntityValues(new RAMDirectory()).closeWriter(),
									new EntityValues(new RAMDirectory()).closeWriter(),
									new RankByFrequency(),
									new OptionalContext(),
									new KnowledgeBase("dbpedia"))
								.add(new TripleBuilder().withPredicate("predicate").asTriple())
								.closeWriter();
		
		GroupBySearch index = new GroupBySearch(directory , new CountPredicates(), new KnowledgeBase("dbpedia"));
		
		assertThat(index.count("predicate", "any", new OptionalContext()), is(equalTo(1l)));
	}
	
	@Test
	public void shouldGiveManyResultWhenMatchesPredicate() throws Exception {
		Directory directory = new RAMDirectory();
		new ContextualizedPredicates(directory, 
									new EntityValues(new RAMDirectory()).closeWriter(),
									new EntityValues(new RAMDirectory()).closeWriter(),
									new RankByFrequency(),
									new OptionalContext(),
									new KnowledgeBase("dbpedia"))
								.add(new TripleBuilder().withPredicate("http://predicate").asTriple())
								.add(new TripleBuilder().withPredicate("http://predicate").asTriple())
								.closeWriter();
		
		GroupBySearch index = new GroupBySearch(directory , new CountPredicates(), new KnowledgeBase("dbpedia"));
		
		assertThat(index.count("http://predicate", "any", new OptionalContext()), is(equalTo(2l)));
	}
}
