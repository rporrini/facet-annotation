package it.disco.unimib.index;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;

import edu.stanford.nlp.io.IOUtils;

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

	public InputStream content() {
		return IOUtils.openFile(file);
	}
}