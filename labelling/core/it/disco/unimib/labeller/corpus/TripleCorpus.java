package it.disco.unimib.labeller.corpus;

import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.NTriple;
import it.disco.unimib.labeller.index.TripleIndex;

public class TripleCorpus {

	private OutputFile file;
	private TripleIndex labels;

	public TripleCorpus(TripleIndex labels, OutputFile file) {
		this.file = file;
		this.labels = labels;
	}

	public void add(NTriple triple) throws Exception {
		String value = triple.object();
		if(value.startsWith("http")){
			for(CandidatePredicate label : this.labels.get(triple.object(), "any")){
				value += " " + label.value();
			}
		}
		file.write(triple.predicate().uri() + " " + value);
	}
}
