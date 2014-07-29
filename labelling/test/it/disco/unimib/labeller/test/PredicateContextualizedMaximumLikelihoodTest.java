package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.Evidence;
import it.disco.unimib.labeller.index.GroupBySearch;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.RankByFrequency;
import it.disco.unimib.labeller.index.SimpleOccurrences;
import it.disco.unimib.labeller.index.TripleIndex;
import it.disco.unimib.labeller.labelling.PredicateContextualizedMaximumLikelihood;

import java.util.Arrays;
import java.util.List;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class PredicateContextualizedMaximumLikelihoodTest {

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
		new Evidence(directory , types , labels , new RankByFrequency(), new NoContext(), new IndexFields("dbpedia"))
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
		
		GroupBySearch index = new GroupBySearch(directory, new SimpleOccurrences(), new IndexFields("dbpedia"));
		List<CandidatePredicate> results = new PredicateContextualizedMaximumLikelihood(index).typeOf("novelist", Arrays.asList(new String[]{"2009", "2010", "2014"}));
		
		assertThat(results, is(not(empty())));
		assertThat(results, hasSize(3));
		assertThat(results.get(0).score(), is(closeTo(1.203, 0.001)));
		assertThat(results.get(1).score(), is(closeTo(0.693, 0.001)));
		assertThat(results.get(2).score(), is(closeTo(0.510, 0.001)));
	}
}
