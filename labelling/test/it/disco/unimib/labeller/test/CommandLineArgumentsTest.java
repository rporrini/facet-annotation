package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import it.disco.unimib.labeller.benchmark.CommandLineArguments;

import org.junit.Test;

public class CommandLineArgumentsTest {

	@Test
	public void shouldReturnNullOnEmptyArguments() {
		CommandLineArguments args = new CommandLineArguments(new String[]{});
		
		assertThat(args.asString("any"), is(nullValue()));
	}
	
	@Test
	public void shouldRecognizeParameters() throws Exception {
		CommandLineArguments args = new CommandLineArguments(new String[]{"arg=value"});
		
		assertThat(args.asString("arg"), is(equalTo("value")));
	}
}
