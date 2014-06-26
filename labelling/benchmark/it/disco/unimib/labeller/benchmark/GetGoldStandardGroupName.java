package it.disco.unimib.labeller.benchmark;

import java.io.File;

public class GetGoldStandardGroupName {

	public static void main(String[] args) {
		String goldStandard = args[0];
		int id = Integer.parseInt(args[1]);
		File directory = new File(goldStandard);
		for(GoldStandardGroup group : new UnorderedGroups(directory).getGroups()){
			if(group.id() == id){
				System.out.println(new File(directory, group.name()).getAbsolutePath());
			}
		}
	}
}
