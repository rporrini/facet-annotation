package it.disco.unimib.labeller.tools;

import org.apache.commons.lang3.StringUtils;

public class TrecResultPredicate implements Comparable<TrecResultPredicate>{

	private String line;

	public TrecResultPredicate(String line) {
		this.line = line;
	}
	
	public String value(){
		return StringUtils.split(line, " ")[2];
	}
	
	public Double rank(){
		return Double.parseDouble(StringUtils.split(line, " ")[4]);
	}
	
	public int groupId(){
		return Integer.parseInt(StringUtils.split(line, " ")[0]);
	}
	
	@Override
	public String toString() {
		return value() + " (" + rank() + ")";
	}
	
	@Override
	public int compareTo(TrecResultPredicate other) {
		return (int)Math.signum(other.rank() - this.rank());
	}
}