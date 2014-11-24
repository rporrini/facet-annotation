package it.disco.unimib.labeller.unit;

import it.disco.unimib.labeller.index.InputFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class InputFileTestDouble extends InputFile{

	public InputFileTestDouble() {
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

	public InputFileTestDouble withName(String name) {
		this.name = name;
		return this;
	}

	public InputFileTestDouble withLine(String line) {
		lines.add(line);
		return this;
	}
}