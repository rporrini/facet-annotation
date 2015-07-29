package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.properties.TypeDistribution;

public interface TypeConsistency {

	public double consistencyOf(TypeDistribution types);
}