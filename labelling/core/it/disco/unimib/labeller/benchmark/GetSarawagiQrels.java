package it.disco.unimib.labeller.benchmark;

import java.io.File;

public class GetSarawagiQrels {

	public static void main(String[] args) {
		String path = args[0];
		for(GoldStandardGroup group : new OrderedGroups(new UnorderedGroups(new File(path))).getGroups()){
			System.out.println(group.id() + " Q0 " + group.label() + " 1");
		}
	}
}
