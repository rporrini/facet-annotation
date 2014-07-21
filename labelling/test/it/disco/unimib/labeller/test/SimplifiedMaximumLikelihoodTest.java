package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.index.ContextualizedPredicates;
import it.disco.unimib.labeller.index.CountPredicates;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.GroupBySearch;
import it.disco.unimib.labeller.index.KnowledgeBase;
import it.disco.unimib.labeller.index.OptionalContext;
import it.disco.unimib.labeller.index.RankByFrequency;
import it.disco.unimib.labeller.index.TripleIndex;
import it.disco.unimib.labeller.labelling.SimplifiedMaximumLikelihood;

import java.util.Arrays;
import java.util.List;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class SimplifiedMaximumLikelihoodTest {
	
	@Test
	public void exampleCase() throws Exception {
		Directory directory = new RAMDirectory();
		TripleIndex types = new EntityValues(directory).add(new TripleBuilder().withSubject("http://a_novelist")
																				.withObject("http://novelist")
																				.asTriple())
													.add(new TripleBuilder().withSubject("http://a_movie")
																				.withObject("http://movie")
																				.asTriple())
													   .closeWriter();
		TripleIndex labels = new EntityValues(directory).add(new TripleBuilder().withSubject("http://novelist")
																				.withObject("novelist")
																				.asTriple())
													.add(new TripleBuilder().withSubject("http://movie")
																				.withObject("movie")
																				.asTriple())
														.closeWriter();
		new ContextualizedPredicates(directory , types , labels , new RankByFrequency(), new OptionalContext(), new KnowledgeBase("dbpedia"))
									.add(new TripleBuilder().withSubject("http://a_novelist")
															.withPredicate("http://dateOfBirth")
															.withLiteral("2009").asTriple())
									.add(new TripleBuilder().withSubject("http://a_movie")
															.withPredicate("http://year")
															.withLiteral("2009").asTriple())
									.add(new TripleBuilder().withSubject("http://a_novelist")
															.withPredicate("http://dateOfDeath")
															.withLiteral("2009").asTriple())
									.add(new TripleBuilder().withSubject("http://a_novelist")
															.withPredicate("http://year")
															.withLiteral("2010").asTriple())
									.add(new TripleBuilder().withSubject("http://a_novelist")
															.withPredicate("http://dateOfDeath")
															.withLiteral("2010").asTriple())
									.add(new TripleBuilder().withSubject("http://a_movie")
															.withPredicate("http://year")
															.withLiteral("2014").asTriple())
									.add(new TripleBuilder().withSubject("http://a_movie")
															.withPredicate("http://year")
															.withLiteral("2011").asTriple())
									.add(new TripleBuilder().withSubject("http://a_movie")
															.withPredicate("http://year")
															.withLiteral("2012").asTriple())
									.add(new TripleBuilder().withSubject("http://a_movie")
															.withPredicate("http://year")
															.withLiteral("2013").asTriple())
									.add(new TripleBuilder().withSubject("http://a_movie")
															.withPredicate("http://year")
															.withLiteral("2015").asTriple())
									.closeWriter();
		
		GroupBySearch index = new GroupBySearch(directory, new CountPredicates(), new KnowledgeBase("dbpedia"));
		List<AnnotationResult> results = new SimplifiedMaximumLikelihood(index).typeOf("novelist", Arrays.asList(new String[]{"2009", "2010", "2014"}));
		
		assertThat(results, is(not(empty())));
		assertThat(results, hasSize(3));
		assertThat(results.get(0).score(), is(closeTo(0.810, 0.001)));
		assertThat(results.get(1).score(), is(closeTo(0.693, 0.001)));
		assertThat(results.get(2).score(), is(closeTo(0.400, 0.001)));
	}

}