package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.Stems;

import org.junit.Test;

public class StemsTest {

	@Test
	public void shouldStemASingleWord() throws Exception {
		String stem = new Stems(new IndexFields("dbpedia").analyzer()).of("movies");
		
		assertThat(stem, equalTo("movi"));
	}
	
	@Test
	public void shouldStemAMultipleWords() throws Exception {
		String stem = new Stems(new IndexFields("dbpedia").analyzer()).of("movie Movie work CreativeWork 1930 films 1930s drama films Black-and-white films Films directed by Alexander Dovzhenko Films set in Ukraine Dovzhenko Film Studios films Soviet silent films Soviet-era Ukrainian films Soviet films Ukrainian films");
		
		assertThat(stem, equalTo("movi movi work creativework 1930 film 1930 drama film black white film film direct alexand dovzhenko film set ukrain dovzhenko film studio film soviet silent film soviet era ukrainian film soviet film ukrainian film"));
	}
}
