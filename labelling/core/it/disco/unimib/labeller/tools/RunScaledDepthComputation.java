package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.Command;
import it.disco.unimib.labeller.corpus.BulkWriteFile;
import it.disco.unimib.labeller.index.InputFile;
import it.disco.unimib.labeller.index.ScaledDeptComputation;
import it.disco.unimib.labeller.index.TypeHierarchy;

import java.io.File;

public class RunScaledDepthComputation {

	public static void main(String[] args) throws Exception {
		
		Command arguments = new Command()
							.withArgument("taxonomy", "the relative path of the taxonomy")
							.withArgument("types", "the relative path of the file containing the types")
							.withArgument("destination", "the relative path of the file where to save the computed scaled depths")
							.parse(args);
		
		TypeHierarchy taxonomy = new TypeHierarchy(new InputFile(new File(arguments.argumentAsString("taxonomy"))));
		BulkWriteFile output = new BulkWriteFile(new File(arguments.argumentAsString("destination")), 10000);
		InputFile input = new InputFile(new File(arguments.argumentAsString("types")));
		
		new ScaledDeptComputation(taxonomy).persist(input, output);
	}
}
