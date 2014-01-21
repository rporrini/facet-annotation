package it.unimib.disco.dataset;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

class Files{

	private Folder file;

	public Files(Folder folder){
		this.file = folder;
	}
	
	public List<String> lines(String resource) throws Exception {
		List<String> result;
		try{
			result = FileUtils.readLines(file.child(resource).toFile(), "utf-8");
		}
		catch(Exception e){
			result = new ArrayList<String>();
		}
		return result;
	}
	
	public List<String> list() throws Exception {
		List<String> connectors = new ArrayList<String>();
		File[] files = file.toFile().listFiles();
		if (files != null) {
			for (File file : files) {
				connectors.add(file.getName());
			}
		}
		return connectors;
	}
}