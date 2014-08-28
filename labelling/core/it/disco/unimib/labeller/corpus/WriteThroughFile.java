package it.disco.unimib.labeller.corpus;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class WriteThroughFile implements OutputFile {

	private File file;

	public WriteThroughFile(File file) {
		this.file = file;
	}

	@Override
	public WriteThroughFile write(String line) throws Exception {
		FileUtils.writeStringToFile(file, line, true);
		return this;
	}
}
