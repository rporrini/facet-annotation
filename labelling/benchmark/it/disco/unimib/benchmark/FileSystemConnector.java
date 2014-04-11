package it.disco.unimib.benchmark;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class FileSystemConnector{
	
	private File file;

	public FileSystemConnector(File file) {
		this.file = file;
	}

	public String name(){
		return file.getName();
	}

	public List<String> lines() throws Exception{
		return FileUtils.readLines(file);
	}
}