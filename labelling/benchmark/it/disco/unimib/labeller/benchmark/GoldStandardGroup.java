package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.FileSystemConnector;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class GoldStandardGroup {

	private FileSystemConnector connector;

	public GoldStandardGroup(FileSystemConnector connector) {
		this.connector = connector;
	}

	public List<String> elements() throws Exception {
		return connector.lines();
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
	
	private String[] splittedName() {
		return StringUtils.split(connector.name(), "_");
	}
}
