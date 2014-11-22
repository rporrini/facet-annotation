package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.InputFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UnorderedFacets implements GoldStandard {

	private File directory;

	public UnorderedFacets(File directory) {
		this.directory = directory;
	}

	@Override
	public GoldStandardFacet[] getFacets() {
		List<InputFile> connectors = filesIn(directory);
		GoldStandardFacet[] groups = new GoldStandardFacet[connectors.size()];
		for(int i=0; i<connectors.size(); i++){
			groups[i] = new GoldStandardFacet(connectors.get(i));
		}
		return groups;
	}
	
	@Override
	public GoldStandardFacet getGroupById(int id) {
		GoldStandardFacet[] facets = getFacets();
		for(GoldStandardFacet group : facets){
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
