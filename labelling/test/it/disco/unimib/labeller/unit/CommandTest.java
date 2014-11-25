package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.Command;

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
									   .parse(new String[]{"kb=any"});
		
		assertThat(command.argumentAsString("kb"), equalTo("any"));
	}
	
	@Test(expected=Exception.class)
	public void shouldRaiseExceptionIfOneArgumentIsMissing() throws Exception {
		
		Command command = new Command().withArgument("kb", "sets the knowledge base").parse(new String[]{});
		
		command.argumentAsString("kb");
	}
	
	@Test
	public void shouldIncludeTheExplanationOfArguments() throws Exception {
		Command command = new Command().withArgument("kb", "sets the knowledge base");
		
		assertThat(command.explainArguments(), containsString("the knowledge base"));
	}
	
	@Test
	public void shouldParseArgumentsWithSpaces() throws Exception {
		Command command = new Command().withArgument("argument", "the argument").parse(new String[]{
																					"argument=the",
																					"argument=argument"
																				});
		
		assertThat(command.argumentsAsStrings("argument"), hasSize(2));
	}
}
