package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.Command;
import it.disco.unimib.labeller.corpus.OutputFile;
import it.disco.unimib.labeller.corpus.WriteThroughFile;
import it.disco.unimib.labeller.index.InputFile;
import it.disco.unimib.labeller.index.ScaledDeptComputation;
import it.disco.unimib.labeller.index.TypeHierarchy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RunScaledDepthComputation {

	public static void main(String[] args) throws Exception {
		
		Command arguments = new Command()
							.withArgument("taxonomy", "the relative path of the taxonomy")
							.withArgument("types", "the relative path of the directory containing the types")
							.withArgument("destination", "the relative path of the directory where to save the computed scaled depths")
							.parse(args);
		
		File destinationDirectory = new File(arguments.argumentAsString("destination"));
		File sourceDirectory = new File(arguments.argumentAsString("types"));		
		TypeHierarchy taxonomy = new TypeHierarchy(taxonomyFiles(arguments.argumentsAsStrings("taxonomy")));
		
		for(File file : sourceDirectory.listFiles()){
			InputFile input = new InputFile(file);
			OutputFile output = new WriteThroughFile(new File(destinationDirectory, input.name()));
			new ScaledDeptComputation(taxonomy).persist(input, output);
		}
	}

	private static InputFile[] taxonomyFiles(List<String> argumentsAsStrings) throws Exception {
		List<InputFile> files = new ArrayList<InputFile>();
		for(String file : argumentsAsStrings){
			files.add(new InputFile(new File(file)));
		}
		return files.toArray(new InputFile[files.size()]);
	}
}
