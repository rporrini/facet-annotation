package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.GoldStandardFacet;
import it.disco.unimib.labeller.benchmark.UnorderedFacets;

import java.io.File;

public class GoldStandardGroupName {

	public static void main(String[] args) {
		File directory = new File(args[0]);
		GoldStandardFacet foundGroup = new UnorderedFacets(directory).getGroupById(Integer.parseInt(args[1]));
		System.out.println(new File(directory, foundGroup.name()).getAbsolutePath());
	}
}
