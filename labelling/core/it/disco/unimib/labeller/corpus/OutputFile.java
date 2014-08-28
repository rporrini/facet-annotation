package it.disco.unimib.labeller.corpus;

public interface OutputFile {

	public OutputFile write(String line) throws Exception;
}