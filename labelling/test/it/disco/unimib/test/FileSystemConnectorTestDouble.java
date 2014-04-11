package it.disco.unimib.test;

import it.disco.unimib.index.FileSystemConnector;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class FileSystemConnectorTestDouble extends FileSystemConnector{

	public FileSystemConnectorTestDouble() {
		super(null);
	}

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
	
	@Override
	public InputStream content() {
		return new ByteArrayInputStream(StringUtils.join(lines(), "\n").getBytes());
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