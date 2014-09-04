package it.disco.unimib.labeller.benchmark;

import java.util.HashMap;

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
		CommandLineArguments args = new CommandLineArguments(commandLineArguments);
		for(String key : explanations.keySet()){
			if(args.asString(key) == null) throw new Exception();
		}
		arguments = args;
		return this;
	}

	public String argumentAsString(String key) {
		return arguments.asString(key);
	}
}
