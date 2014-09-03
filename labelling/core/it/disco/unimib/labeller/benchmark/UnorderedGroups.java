package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.InputFile;

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
		List<InputFile> connectors = filesIn(directory);
		GoldStandardGroup[] groups = new GoldStandardGroup[connectors.size()];
		for(int i=0; i<connectors.size(); i++){
			groups[i] = new GoldStandardGroup(connectors.get(i));
		}
		return groups;
	}
	
	@Override
	public GoldStandardGroup getGroupById(int id) {
		GoldStandardGroup[] groups = getGroups();
		for(GoldStandardGroup group : groups){
			if(group.id() == id) return group;
		}
		return null;
	}
	
	private List<InputFile> filesIn(File directory) {
		List<InputFile> connectors = new ArrayList<InputFile>();
		for(File file : directory.listFiles()){
			connectors.add(new InputFile(file));
		}
		return connectors;
	}
}
