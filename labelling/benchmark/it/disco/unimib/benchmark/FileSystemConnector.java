package it.disco.unimib.benchmark;

import java.util.List;

public interface FileSystemConnector{
	
	public String name();

	public List<String> lines();
}