package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import it.disco.unimib.labeller.benchmark.Command;
import it.disco.unimib.labeller.benchmark.CommandLineArguments;

import org.junit.Test;

public class CommandTest {

	@Test
	public void shouldPrintOutTheUsage() {
		
		Command command = new Command();
		
		assertThat(command.explainArguments(), containsString("arguments:"));
	}
	
	@Test
	public void shouldParseDeclaredArguments() throws Exception {
		
		Command command = new Command().withArgument("kb", "sets the knowledge base")
									   .parse(new CommandLineArguments(new String[]{"kb=any"}));
		
		assertThat(command.argumentAsString("kb"), equalTo("any"));
	}
	
	@Test(expected=Exception.class)
	public void shouldRaiseExceptionIfOneArgumentIsMissing() throws Exception {
		
		new Command().withArgument("kb", "sets the knowledge base")
					 .parse(new CommandLineArguments(new String[]{}));
	}
	
	@Test
	public void shouldIncludeTheExplanationOfArguments() throws Exception {
		Command command = new Command().withArgument("kb", "sets the knowledge base");
		
		assertThat(command.explainArguments(), containsString("the knowledge base"));
	}
}
