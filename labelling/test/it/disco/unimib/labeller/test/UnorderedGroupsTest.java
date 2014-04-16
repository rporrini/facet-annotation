package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import it.disco.unimib.labeller.benchmark.UnorderedGroups;
import it.disco.unimib.labeller.benchmark.GoldStandardGroup;

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

	private void createFiles(File directory, int howMany) throws IOException {
		for(int i=0; i<howMany; i++){
			FileUtils.write(new File(directory, "file-" + i), "content " + i);
		}
	}
}
