package it.disco.unimib.labeller.corpus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class BulkWriteFile implements OutputFile{

	private File file;
	private int threshold;
	private List<String> currentLines;

	public BulkWriteFile(File file, int threshold) {
		this.file = file;
		this.threshold = threshold;
		this.currentLines = new ArrayList<String>();
	}

	public BulkWriteFile write(String line) throws Exception {
		currentLines.add(line);
		if(thresholdIsReached()){
			flush();
		}
		return this;
	}

	private boolean thresholdIsReached() {
		return currentLines.size() == threshold;
	}

	public void flush() throws IOException {
		FileUtils.writeLines(file, currentLines, true);
		currentLines = new ArrayList<String>();
	}
}
