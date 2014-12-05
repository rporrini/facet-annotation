package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.Command;
import it.disco.unimib.labeller.corpus.BulkWriteFile;
import it.disco.unimib.labeller.index.InputFile;
import it.disco.unimib.labeller.index.ScaledDeptComputation;

import java.io.File;

public class RunScaledDepthComputation {

	public static void main(String[] args) throws Exception {
		
		Command arguments = new Command().withArgument("types", "the relative path of the file containing the types")
					 .withArgument("destination", "the relative path of the file where to save the computed scaled depths")
					 .parse(args);
		
		InputFile input = new InputFile(new File(arguments.argumentAsString("types")));
		BulkWriteFile output = new BulkWriteFile(new File(arguments.argumentAsString("destination")), 10000);
		
		new ScaledDeptComputation().persist(input, output);
	}
}
