package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.GoldStandardFacet;
import it.disco.unimib.labeller.benchmark.UnorderedFacets;
import it.disco.unimib.labeller.corpus.WriteThroughFile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UnorderedFacetsTest {

	private TemporaryDirectory temporaryDirectory;
	
	@Before
	public void createTemporaryDirectory() throws Exception{
		temporaryDirectory = new TemporaryDirectory();
	}
	
	@After
	public void deleteTemporaryDirectory() throws Exception{
		temporaryDirectory.delete();
	}
	
	@Test
	public void shouldReturnTheRightNumberOfConnectors() throws Exception {
		createFiles(temporaryDirectory, 10);
		
		GoldStandardFacet[] groups = new UnorderedFacets(temporaryDirectory.get()).getFacets();
		
		assertThat(groups.length, equalTo(10));
	}
	
	@Test
	public void shouldReturnNullWhenAskedForANotKnownId() throws Exception {
		
		GoldStandardFacet group = new UnorderedFacets(temporaryDirectory.get()).getGroupById(1234);
		
		assertThat(group, is(nullValue()));
	}
	
	@Test
	public void shouldReturnTheRightGroupWhenAskedForAnExistingId() throws Exception {
		createFiles(temporaryDirectory, 2);
		
		GoldStandardFacet group = new UnorderedFacets(temporaryDirectory.get()).getGroupById(Math.abs("file-1".hashCode()));
		
		assertThat(group.elements(), hasItem("content 1"));
	}
	
	private void createFiles(TemporaryDirectory directory, int howMany) throws Exception {
		for(int i=0; i<howMany; i++){
			new WriteThroughFile(directory.getFile("file-" + i)).write("content " + i);
		}
	}
}
