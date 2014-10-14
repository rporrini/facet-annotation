package it.disco.unimib.labeller.benchmark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CommandLineArguments {

	HashMap<String, List<String>> arguments;
	
	public CommandLineArguments(String[] arguments) {
		this.arguments = new HashMap<String, List<String>>();
		for(String argument : arguments){
			String[] splitted = argument.split("=");
			String key = splitted[0];
			String value = splitted[1];
			if(!this.arguments.containsKey(key)) this.arguments.put(key, new ArrayList<String>());
			this.arguments.get(key).add(value);
		}
	}

	public List<String> asString(String value) {
		return arguments.get(value);
	}
}
