package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.corpus.BulkWriteFile;
import it.disco.unimib.labeller.index.InputFile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BulkWriteFileTest {

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
	public void shouldNotWriteUnderASpecifiedThreshold() throws Exception {
		int threshold = 2;
		
		new BulkWriteFile(temporaryDirectory.getFile("out"), threshold).write("line");
		
		assertThat(temporaryDirectory.get().list().length, is(equalTo(0)));
	}
	
	@Test
	public void shouldFlusAfterTheThreshold() throws Exception {
		int threshold = 2;
		
		new BulkWriteFile(temporaryDirectory.getFile("out"), threshold)
							.write("line")
							.write("line")
							.write("line");
		
		assertThat(new InputFile(temporaryDirectory.getFile("out")).lines(), hasSize(2));
	}
	
	@Test
	public void shouldFlusAfterTheThresholdManyTimes() throws Exception {
		int threshold = 1;
		
		new BulkWriteFile(temporaryDirectory.getFile("out"), threshold)
							.write("line")
							.write("line")
							.write("line");
		
		assertThat(new InputFile(temporaryDirectory.getFile("out")).lines(), hasSize(3));
	}
}
