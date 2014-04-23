package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.FileSystemConnector;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class GoldStandardGroup {

	private FileSystemConnector connector;

	public GoldStandardGroup(FileSystemConnector connector) {
		this.connector = connector;
	}

	public List<String> elements() throws Exception {
		return nonEmpty(connector.lines());
	}

	public String domain() {
		return splittedName()[0];
	}
	
	public String provider() {
		return splittedName()[1];
	}
	
	public String context() {
		return splittedName()[2];
	}

	public String label() {
		return splittedName()[3];
	}
	
	public String hyperlink() {
		try{
			String firstPart = splittedName()[4];
			int index = connector.name().indexOf(firstPart);
			return connector.name().substring(index);
		}
		catch(Exception e){
			return "";
		}
	}
	
	private String[] splittedName() {
		return StringUtils.split(connector.name(), "_");
	}
	
	private List<String> nonEmpty(List<String> lines) {
		List<String> result = new ArrayList<String>();
		for(String line : lines){
			if(!line.trim().isEmpty()) result.add(line);
		}
		return result;
	}
}
