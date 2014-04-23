package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class TrecEval implements Summary {

	private List<String> lines;
	private String name;
	
	public TrecEval(String name){
		this.lines = new ArrayList<String>();
		this.name = name;
	}
	
	@Override
	public String result() {
		return StringUtils.join(lines, "\n");
	}

	@Override
	public Summary track(GoldStandardGroup group, List<AnnotationResult> results) throws Exception {
		for(int i=0; i<results.size(); i++){
			lines.add(group.id() + " Q0 " + results.get(i).value() + " " + (i+1) + " " + results.get(i).score() + " " + name);
		}
		return this;
	}

}
