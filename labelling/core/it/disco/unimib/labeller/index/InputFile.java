package it.disco.unimib.labeller.index;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class InputFile{
	
	private File file;

	public InputFile(File file) {
		this.file = file;
	}

	public String name(){
		return file.getName();
	}

	public List<String> lines() throws Exception{
		return FileUtils.readLines(file);
	}

	public InputStream content() throws IOException {
		return FileUtils.openInputStream(file);
	}
}