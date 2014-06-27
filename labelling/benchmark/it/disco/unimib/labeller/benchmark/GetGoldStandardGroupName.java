package it.disco.unimib.labeller.benchmark;

import java.io.File;

public class GetGoldStandardGroupName {

	public static void main(String[] args) {
		File directory = new File(args[0]);
		GoldStandardGroup foundGroup = new UnorderedGroups(directory).getGroupById(Integer.parseInt(args[1]));
		System.out.println(new File(directory, foundGroup.name()).getAbsolutePath());
	}
}
