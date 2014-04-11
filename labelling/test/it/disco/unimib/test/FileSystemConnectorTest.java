package it.disco.unimib.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.index.FileSystemConnector;

import java.io.File;

import org.junit.Test;

public class FileSystemConnectorTest {

	@Test
	public void shouldReadARealFile() throws Exception {
		FileSystemConnector connector = new FileSystemConnector(new File("log4j.properties"));
		
		assertThat(connector.name(), is(equalTo("log4j.properties")));
		assertThat(connector.lines(), hasSize(greaterThan(0)));
	}
}
