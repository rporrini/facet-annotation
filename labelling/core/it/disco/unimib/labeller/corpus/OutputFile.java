package it.disco.unimib.labeller.corpus;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class OutputFile {

	private File file;

	public OutputFile(File file) {
		this.file = file;
	}

	public OutputFile write(String line) throws Exception {
		FileUtils.writeStringToFile(file, line, true);
		return this;
	}
}
