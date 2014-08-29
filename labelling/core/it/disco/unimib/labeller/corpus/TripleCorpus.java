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
		String value = getLabel(triple.object());
		for(CandidatePredicate type : this.types.get(triple.subject(), "any")){
			String types = getLabel(type.value());
			file.write(types + " " + triple.predicate().uri() + " " + value);
		}
	}

	private String getLabel(String uri) throws Exception {
		String value = uri.contains("http://") ? "" : uri;
		for(CandidatePredicate label : this.labels.get(uri, "any")){
			value += " " + label.value();
		}
		return value;
	}
}
