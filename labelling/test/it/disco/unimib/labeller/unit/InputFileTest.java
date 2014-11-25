package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.InputFile;

import java.io.File;

import org.junit.Test;

public class InputFileTest {

	@Test
	public void shouldReadARealFile() throws Exception {
		InputFile connector = new InputFile(new File("log4j.properties"));
		
		assertThat(connector.name(), is(equalTo("log4j.properties")));
		assertThat(connector.lines(), hasSize(greaterThan(0)));
	}
}
