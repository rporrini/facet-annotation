package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.GoldStandardFacet;
import it.disco.unimib.labeller.benchmark.OrderedFacets;
import it.disco.unimib.labeller.benchmark.UnorderedFacets;

import java.io.File;

public class YAGO1Qrels {

	public static void main(String[] args) {
		String path = args[0];
		for(GoldStandardFacet group : new OrderedFacets(new UnorderedFacets(new File(path))).getFacets()){
			System.out.println(group.id() + " Q0 " + group.label() + " 1");
		}
	}
}
