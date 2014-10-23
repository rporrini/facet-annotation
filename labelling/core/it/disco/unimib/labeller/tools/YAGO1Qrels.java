package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.GoldStandardGroup;
import it.disco.unimib.labeller.benchmark.OrderedGroups;
import it.disco.unimib.labeller.benchmark.UnorderedGroups;

import java.io.File;

public class YAGO1Qrels {

	public static void main(String[] args) {
		String path = args[0];
		for(GoldStandardGroup group : new OrderedGroups(new UnorderedGroups(new File(path))).getGroups()){
			System.out.println(group.id() + " Q0 " + group.label() + " 1");
		}
	}
}
