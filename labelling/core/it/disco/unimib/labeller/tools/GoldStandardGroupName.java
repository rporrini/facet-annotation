package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.GoldStandardGroup;
import it.disco.unimib.labeller.benchmark.UnorderedGroups;

import java.io.File;

public class GoldStandardGroupName {

	public static void main(String[] args) {
		File directory = new File(args[0]);
		GoldStandardGroup foundGroup = new UnorderedGroups(directory).getGroupById(Integer.parseInt(args[1]));
		System.out.println(new File(directory, foundGroup.name()).getAbsolutePath());
	}
}
