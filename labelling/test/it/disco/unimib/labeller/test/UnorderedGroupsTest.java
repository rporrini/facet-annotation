package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.GoldStandardGroup;
import it.disco.unimib.labeller.benchmark.UnorderedGroups;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UnorderedGroupsTest {

	private File temporaryDirectory;
	
	@Before
	public void createTemporaryDirectory() throws Exception{
		temporaryDirectory = new File("tmp");
		FileUtils.forceMkdir(temporaryDirectory);
	}
	
	@After
	public void deleteTemporaryDirectory() throws Exception{
		FileUtils.forceDelete(temporaryDirectory);
	}
	
	@Test
	public void shouldReturnTheRightNumberOfConnectors() throws Exception {
		createFiles(temporaryDirectory, 10);
		
		GoldStandardGroup[] groups = new UnorderedGroups(temporaryDirectory).getGroups();
		
		assertThat(groups.length, equalTo(10));
	}
	
	@Test
	public void shouldReturnNullWhenAskedForANotKnownId() throws Exception {
		
		GoldStandardGroup group = new UnorderedGroups(temporaryDirectory).getGroupById(1234);
		
		assertThat(group, is(nullValue()));
	}
	
	@Test
	public void shouldReturnTheRightGroupWhenAskedForAnExistingId() throws Exception {
		createFiles(temporaryDirectory, 2);
		
		GoldStandardGroup group = new UnorderedGroups(temporaryDirectory).getGroupById(Math.abs("file-1".hashCode()));
		
		assertThat(group.elements(), hasItem("content 1"));
	}
	
	private void createFiles(File directory, int howMany) throws IOException {
		for(int i=0; i<howMany; i++){
			FileUtils.write(new File(directory, "file-" + i), "content " + i);
		}
	}
}
