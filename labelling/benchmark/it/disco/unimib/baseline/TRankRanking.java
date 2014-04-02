package it.disco.unimib.baseline;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import scala.collection.Seq;

public interface TRankRanking{
	
	public HashMap<String, Double> evaluateScoresOf(Map<URI, Seq<URI>> types);
}