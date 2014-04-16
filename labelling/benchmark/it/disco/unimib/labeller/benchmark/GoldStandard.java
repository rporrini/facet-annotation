package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.FileSystemConnector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GoldStandard {

	private File directory;

	public GoldStandard(File directory) {
		this.directory = directory;
	}

	public GoldStandardGroup[] getGroups() {
		List<FileSystemConnector> connectors = filesIn(directory);
		GoldStandardGroup[] groups = new GoldStandardGroup[connectors.size()];
		for(int i=0; i<connectors.size(); i++){
			groups[i] = new GoldStandardGroup(connectors.get(i));
		}
		return groups;
	}
	
	private List<FileSystemConnector> filesIn(File directory) {
		List<FileSystemConnector> connectors = new ArrayList<FileSystemConnector>();
		for(File file : directory.listFiles()){
			connectors.add(new FileSystemConnector(file));
		}
		return connectors;
	}
}
