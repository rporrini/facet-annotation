package it.disco.unimib.labeller.index;

import java.util.Map;

public interface TypeConsistency {

	public double consistencyOf(Map<String, Double> objects);
}