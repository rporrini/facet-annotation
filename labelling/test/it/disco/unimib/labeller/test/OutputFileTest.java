package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import it.disco.unimib.labeller.corpus.OutputFile;
import it.disco.unimib.labeller.index.InputFile;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OutputFileTest {

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
	public void shouldNotWriteUntilTheFirstLineIsWritten() {
		
		new OutputFile(new File(temporaryDirectory, "out"));
		
		assertThat(temporaryDirectory.list().length, is(equalTo(0)));
	}
	
	@Test
	public void shouldWriteALine() throws Exception {
		File output = new File(temporaryDirectory, "out");
		
		new OutputFile(output).write("a line");
		List<String> writtenLines = new InputFile(output).lines();
		
		assertThat(writtenLines, hasSize(1));
		assertThat(writtenLines, hasItem("a line"));
	}
}
