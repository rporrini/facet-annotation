package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.FileSystemConnector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UnorderedGroups implements GoldStandard {

	private File directory;

	public UnorderedGroups(File directory) {
		this.directory = directory;
	}

	@Override
	public GoldStandardGroup[] getGroups() {
		List<FileSystemConnector> connectors = filesIn(directory);
		GoldStandardGroup[] groups = new GoldStandardGroup[connectors.size()];
		for(int i=0; i<connectors.size(); i++){
			groups[i] = new GoldStandardGroup(connectors.get(i));
		}
		return groups;
	}
	
	public GoldStandardGroup getGroupById(int id) {
		GoldStandardGroup[] groups = getGroups();
		for(GoldStandardGroup group : groups){
			if(group.id() == id) return group;
		}
		return null;
	}
	
	private List<FileSystemConnector> filesIn(File directory) {
		List<FileSystemConnector> connectors = new ArrayList<FileSystemConnector>();
		for(File file : directory.listFiles()){
			connectors.add(new FileSystemConnector(file));
		}
		return connectors;
	}
}
