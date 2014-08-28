package it.disco.unimib.labeller.corpus;

import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.NTriple;
import it.disco.unimib.labeller.index.TripleIndex;

public class TripleCorpus {

	private OutputFile file;
	private TripleIndex labels;
	private TripleIndex types;

	public TripleCorpus(TripleIndex types, TripleIndex labels, OutputFile file) {
		this.file = file;
		this.types = types;
		this.labels = labels;
	}

	public void add(NTriple triple) throws Exception {
		String value = triple.object().contains("http://") ? "" : triple.object();
		for(CandidatePredicate label : this.labels.get(triple.object(), "any")){
			value += " " + label.value();
		}
		String context = "";
		for(CandidatePredicate type : this.types.get(triple.subject(), "any")){
			for(CandidatePredicate label : this.labels.get(type.value(), "any")){
				context += " " + label.value();
			}
		}
		file.write(context + " " + triple.predicate().uri() + " " + value);
	}
}
