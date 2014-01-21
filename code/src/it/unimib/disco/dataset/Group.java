package it.unimib.disco.dataset;

import java.util.List;

class Group{
	
	private Files connector;
	private String name;
	
	public Group(Files connector, String name) throws Exception{
		this.connector = connector;
		this.name = name;
	}
	
	public String name() {
		return name.toLowerCase();
	}

	public List<String> keywords() throws Exception {
		return connector.lines(name);
	}
}