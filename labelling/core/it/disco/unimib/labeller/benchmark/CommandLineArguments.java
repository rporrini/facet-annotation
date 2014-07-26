package it.disco.unimib.labeller.benchmark;

import java.util.HashMap;


public class CommandLineArguments {

	HashMap<String, String> arguments;
	
	public CommandLineArguments(String[] arguments) {
		this.arguments = new HashMap<String, String>();
		for(String argument : arguments){
			String[] splitted = argument.split("=");
			this.arguments.put(splitted[0], splitted[1]);
		}
	}

	public String asString(String value) {
		return arguments.get(value);
	}
}
