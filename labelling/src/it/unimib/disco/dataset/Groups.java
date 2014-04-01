package it.unimib.disco.dataset;

import java.util.ArrayList;
import java.util.List;

public class Groups{
	
	private Files files;

	public Groups(Folder folder){
		this.files = new Files(folder);
	}
	
	public List<Group> get() throws Exception{
		ArrayList<Group> groups = new ArrayList<Group>();
		for(String name : files.list()){
			groups.add(new Group(files, name));
		}
		return groups;
	}
}