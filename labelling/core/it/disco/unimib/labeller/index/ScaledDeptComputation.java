package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.corpus.OutputFile;

public class ScaledDeptComputation {

	public void persist(InputFile input, OutputFile output) throws Exception {
		TypeHierarchy types = new TypeHierarchy(input);
		for(Type type : types.getAllTypes()){
			output.write(type.toString() + "|" + type.scaledDepth());
		}
	}
}
