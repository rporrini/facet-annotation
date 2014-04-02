package it.disco.unimib.test;

import it.disco.unimib.benchmark.FileSystemConnector;

import java.util.ArrayList;
import java.util.List;

public class FileSystemConnectorTestDouble implements FileSystemConnector{

	private String name;
	private List<String> lines = new ArrayList<String>();

	@Override
	public String name() {
		return this.name;
	}
	
	@Override
	public List<String> lines() {
		return this.lines;
	}

	public FileSystemConnectorTestDouble withName(String name) {
		this.name = name;
		return this;
	}

	public FileSystemConnectorTestDouble withLine(String line) {
		lines.add(line);
		return this;
	}
}