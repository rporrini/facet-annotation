package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.Command;
import it.disco.unimib.labeller.benchmark.Events;
import it.disco.unimib.labeller.corpus.OutputFile;
import it.disco.unimib.labeller.corpus.WriteThroughFile;
import it.disco.unimib.labeller.index.InputFile;
import it.disco.unimib.labeller.index.ScaledDeptComputation;
import it.disco.unimib.labeller.index.TypeHierarchy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunScaledDepthComputation {

	public static void main(String[] args) throws Exception {
		
		Command arguments = new Command()
							.withArgument("taxonomy", "the relative path of the taxonomy")
							.withArgument("types", "the relative path of the directory containing the types")
							.withArgument("destination", "the relative path of the directory where to save the computed scaled depths")
							.parse(args);
		
		final File destinationDirectory = new File(arguments.argumentAsString("destination"));
		File sourceDirectory = new File(arguments.argumentAsString("types"));		
		final TypeHierarchy taxonomy = new TypeHierarchy(taxonomyFiles(arguments.argumentsAsStrings("taxonomy")));
		
		ExecutorService executor = Executors.newFixedThreadPool(10);
		for(final File file : sourceDirectory.listFiles()){
			executor.execute(new Runnable() {
				@Override
				public void run() {
					InputFile input = new InputFile(file);
					OutputFile output = new WriteThroughFile(new File(destinationDirectory, input.name()));
					try {
						new ScaledDeptComputation(taxonomy).persist(input, output);
					} catch (Exception e) {
						new Events().error("processing file: " + file, e);
					}
				}
			});
		}
		executor.shutdown();
	    while(!executor.isTerminated()){}
	}

	private static InputFile[] taxonomyFiles(List<String> argumentsAsStrings) throws Exception {
		List<InputFile> files = new ArrayList<InputFile>();
		for(String file : argumentsAsStrings){
			files.add(new InputFile(new File(file)));
		}
		return files.toArray(new InputFile[files.size()]);
	}
}
