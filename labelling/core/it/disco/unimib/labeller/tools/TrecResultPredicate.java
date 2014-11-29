package it.disco.unimib.labeller.tools;

import org.apache.commons.lang3.StringUtils;

public class TrecResultPredicate implements Comparable<TrecResultPredicate>{

	private String line;

	public TrecResultPredicate(String line) {
		this.line = line;
	}
	
	public String score(){
		return split()[2];
	}
	
	public Double rank(){
		return Double.parseDouble(split()[4]);
	}
	
	public int groupId(){
		return Integer.parseInt(split()[0]);
	}
	
	@Override
	public String toString() {
		return score() + " (" + rank() + ")";
	}
	
	@Override
	public int compareTo(TrecResultPredicate other) {
		return (int)Math.signum(other.rank() - this.rank());
	}
	
	private String[] split() {
		return StringUtils.split(line, " ");
	}
}