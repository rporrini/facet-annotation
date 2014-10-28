package it.disco.unimib.labeller.benchmark;

import java.util.HashMap;
import java.util.List;

public class Command {

	private CommandLineArguments arguments;
	private HashMap<String, String> explanations;
	
	public Command() {
		explanations = new HashMap<String, String>();
	}
	
	public String explainArguments() {
		String explanation = "Some arguments are missing. Expected arguments:\n";
		for(String argument : explanations.keySet()){
			explanation += " " + argument + "\t=\t" + explanations.get(argument) + "\n";
		}
		return explanation;
	}

	public Command withArgument(String argument, String explanation) {
		explanations.put(argument, explanation);
		return this;
	}

	public Command parse(String[] commandLineArguments) throws Exception {
		arguments = new CommandLineArguments(commandLineArguments);
		return this;
	}

	public String argumentAsString(String key) throws Exception {
		return get(key).get(0);
	}

	public List<String> argumentsAsStrings(String value) throws Exception {
		return get(value);
	}
	
	private List<String> get(String key) throws Exception {
		List<String> asString = arguments.asString(key);
		if(asString == null) throw new Exception(explainArguments());
		return asString;
	}
}
