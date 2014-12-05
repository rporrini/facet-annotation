package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.corpus.OutputFile;

public class ScaledDeptComputation {

	private TypeHierarchy taxonomy;

	public ScaledDeptComputation(TypeHierarchy taxonomy) {
		this.taxonomy = taxonomy;
	}

	public void persist(InputFile input, OutputFile output) throws Exception {
		for(String line : input.lines()){
			Type type = taxonomy.of(line);
			output.write(type.toString() + "|" + type.scaledDepth());
		}
	}
}
