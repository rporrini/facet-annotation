package it.disco.unimib.benchmark;

public interface Metric {

	public String result();

	public Metric track(String context, String expected, String actual);
}