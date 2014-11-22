package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.InputFile;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class GoldStandardFacet {

	private InputFile connector;

	public GoldStandardFacet(InputFile connector) {
		this.connector = connector;
	}

	public List<String> elements() throws Exception {
		return nonEmptyAndWithNoHash(connector.lines());
	}

	public String provider() {
		return splittedName()[0];
	}
	
	public String context() {
		return splittedName()[1];
	}

	public String label() {
		return splittedName()[2];
	}
	
	public String name(){
		return connector.name();
	}
	
	public String contextHyperlink() {
		try{
			String firstPart = splittedName()[3];
			int index = connector.name().lastIndexOf(firstPart);
			return connector.name().substring(index);
		}
		catch(Exception e){
			return "";
		}
	}
	
	public int id() {
		return Math.abs(connector.name().hashCode());
	}
	
	private String[] splittedName() {
		return StringUtils.split(connector.name(), "_");
	}
	
	private List<String> nonEmptyAndWithNoHash(List<String> lines) {
		List<String> result = new ArrayList<String>();
		for(String line : lines){
			if(!line.trim().isEmpty() && line.charAt(0) != '#') result.add(line);
		}
		return result;
	}
}
