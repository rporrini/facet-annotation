package it.disco.unimib.labeller.benchmark;

public interface Metric {

	public String result();

	public Metric track(String context, String expected, String actual);
}