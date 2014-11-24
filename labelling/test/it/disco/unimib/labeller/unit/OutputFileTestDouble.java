package it.disco.unimib.labeller.unit;

import it.disco.unimib.labeller.corpus.OutputFile;

import java.util.ArrayList;
import java.util.List;

public class OutputFileTestDouble implements OutputFile{

	private List<String> lines = new ArrayList<String>();
	
	@Override
	public OutputFile write(String line) throws Exception {
		lines.add(line);
		return this;
	}
	
	public List<String> getWrittenLines(){
		return lines;
	}
}