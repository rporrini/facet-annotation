package it.disco.unimib.benchmark;

import java.util.List;

import org.apache.commons.lang3.StringUtils;



public class GoldStandardGroup {

	private FileSystemConnector connector;

	public GoldStandardGroup(FileSystemConnector connector) {
		this.connector = connector;
	}

	public String label() {
		return splittedName()[2];
	}

	public List<String> elements() {
		return connector.lines();
	}

	public String provider() {
		return splittedName()[0];
	}

	private String[] splittedName() {
		return StringUtils.split(connector.name(), "_");
	}
}
