package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.Constraint;
import it.disco.unimib.labeller.index.IndexFields;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.junit.Test;

public class ConstraintTest {

	@Test
	public void onDefaultShouldBuildAnEmptyQuery() {
		
		Constraint builder = new Constraint();
		
		assertThat(builder.build(), notNullValue());
	}
	
	@Test
	public void shouldMatchAnExactTerm() throws Exception {
		
		Constraint builder = new Constraint().matchExactly("http://term", "field");
		
		assertThat(builder.build().toString(), equalTo("+field:\"http://term\""));
	}
	
	@Test
	public void shouldMatchAllTheWordsOfATerm() throws Exception {
		
		Constraint query = new Constraint(englishAnalyzer()).all().match("c b", "field");
		
		assertThat(query.build().toString(), equalTo("+(+field:c +field:b)"));
	}
	
	@Test
	public void shouldMatchAnyWordOfATerm() throws Exception {
		
		Constraint query = new Constraint(englishAnalyzer()).any().match("c b", "field");
		
		assertThat(query.build().toString(), equalTo("+(field:c field:b)"));
	}

	@Test
	public void shouldParseMultipleValuesWithAStrangeSeparator() throws Exception {
		Constraint query = new Constraint(new IndexFields("dbpedia").analyzer())
										.all()
										.match("Film-Noir", "literal");
		
		assertThat(query.build().toString(), equalTo("+(+literal:film +literal:noir)"));
	}
	
	@Test
	public void shouldParseMultipleValuesWithOR() throws Exception {
		Constraint query = new Constraint(new IndexFields("dbpedia").analyzer())
										.all()
										.match("Film OR Noir", "literal");
		
		assertThat(query.build().toString(), equalTo("+(+literal:film +literal:noir)"));
	}
	
	@Test
	public void shouldParseMultipleValuesWithDoublePoints() throws Exception {
		Constraint query = new Constraint(new IndexFields("dbpedia").analyzer())
										.all()
										.match("Film : Noir", "literal");
		
		assertThat(query.build().toString(), equalTo("+(+literal:film +literal:noir)"));
	}
	
	@Test
	public void shouldParseAlsoQueriesWithORs() throws Exception {
		
		Constraint query = new Constraint(new IndexFields("dbpedia").analyzer())
										.all()
										.match("portland, OR", "literal");
		
		assertThat(query.build(), is(notNullValue()));
	}
	
	@Test
	public void shouldParseUri() throws Exception {
		
		Constraint query = new Constraint(new IndexFields("dbpedia").analyzer())
										.all()
										.match("http://aaa.com", "property");
		
		assertThat(query.build().toString(), equalTo("+property:http://aaa.com"));
	}
	
	@Test
	public void shouldParseQueriesWithStrangeCharacters() throws Exception {
		
		Constraint query = new Constraint(new IndexFields("dbpedia").analyzer())
										.any()
										.match("Polar-Express", "literal");
		
		assertThat(query.build().toString(), equalTo("+(literal:polar literal:express)"));
	}
	
	private EnglishAnalyzer englishAnalyzer() {
		return new EnglishAnalyzer();
	}
}
