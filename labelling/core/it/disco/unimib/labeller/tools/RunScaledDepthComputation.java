package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.Command;
import it.disco.unimib.labeller.corpus.BulkWriteFile;
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
		TypeHierarchy taxonomy = new TypeHierarchy(taxonomyFiles(arguments));
		
		for(File file : sourceDirectory.listFiles()){
			InputFile input = new InputFile(file);
			BulkWriteFile output = new BulkWriteFile(new File(destinationDirectory, input.name()), 10000);
			new ScaledDeptComputation(taxonomy).persist(input, output);
		}
	}

	private static InputFile[] taxonomyFiles(Command arguments) throws Exception {
		List<InputFile> files = new ArrayList<InputFile>();
		for(String file : arguments.argumentsAsStrings("taxonomy")){
			files.add(new InputFile(new File(file)));
		}
		return files.toArray(new InputFile[files.size()]);
	}
}
