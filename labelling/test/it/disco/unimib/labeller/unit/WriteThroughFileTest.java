package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.corpus.WriteThroughFile;
import it.disco.unimib.labeller.index.InputFile;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WriteThroughFileTest {

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
	public void shouldNotWriteUntilTheFirstLineIsWritten() {
		
		new WriteThroughFile(temporaryDirectory.getFile("out"));
		
		assertThat(temporaryDirectory.get().list().length, is(equalTo(0)));
	}
	
	@Test
	public void shouldWriteALine() throws Exception {
		File output = temporaryDirectory.getFile("out");
		
		new WriteThroughFile(output).write("a line");
		List<String> writtenLines = new InputFile(output).lines();
		
		assertThat(writtenLines, hasSize(1));
		assertThat(writtenLines, hasItem("a line"));
	}
}
