package it.disco.unimib.labeller.unit;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class TemporaryDirectory{
	
	private File directory;

	public TemporaryDirectory() throws Exception{
		this.directory = new File("tmp");
		FileUtils.forceMkdir(directory);
	}
	
	public void delete() throws Exception{
		FileUtils.forceDelete(directory);
	}
	
	public File getFile(String name){
		return new File(directory, name);
	}
	
	public File get(){
		return directory;
	}
}