package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.corpus.OutputFile;

public class ScaledDepthComputation {

	private TypeHierarchy taxonomy;

	public ScaledDepthComputation(TypeHierarchy taxonomy) {
		this.taxonomy = taxonomy;
	}

	public void persist(InputFile input, OutputFile output) throws Exception {
		for(String line : input.lines()){
			Type type = taxonomy.typeFrom(line);
			output.write(type.toString() + "|" + new ScaledDepth().of(type));
		}
	}
}
