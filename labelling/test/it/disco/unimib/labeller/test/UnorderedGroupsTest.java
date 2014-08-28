package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.GoldStandardGroup;
import it.disco.unimib.labeller.benchmark.UnorderedGroups;
import it.disco.unimib.labeller.corpus.OutputFile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UnorderedGroupsTest {

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
		
		GoldStandardGroup[] groups = new UnorderedGroups(temporaryDirectory.get()).getGroups();
		
		assertThat(groups.length, equalTo(10));
	}
	
	@Test
	public void shouldReturnNullWhenAskedForANotKnownId() throws Exception {
		
		GoldStandardGroup group = new UnorderedGroups(temporaryDirectory.get()).getGroupById(1234);
		
		assertThat(group, is(nullValue()));
	}
	
	@Test
	public void shouldReturnTheRightGroupWhenAskedForAnExistingId() throws Exception {
		createFiles(temporaryDirectory, 2);
		
		GoldStandardGroup group = new UnorderedGroups(temporaryDirectory.get()).getGroupById(Math.abs("file-1".hashCode()));
		
		assertThat(group.elements(), hasItem("content 1"));
	}
	
	private void createFiles(TemporaryDirectory directory, int howMany) throws Exception {
		for(int i=0; i<howMany; i++){
			new OutputFile(directory.getFile("file-" + i)).write("content " + i);
		}
	}
}
