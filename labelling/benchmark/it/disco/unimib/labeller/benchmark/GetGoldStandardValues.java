package it.disco.unimib.labeller.benchmark;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class GetGoldStandardValues {

	public static void main(String[] args) throws Exception {
		
		ArrayList<String> contexts = new ArrayList<String>();
		ArrayList<String> providers = new ArrayList<String>();
		ArrayList<String> labels = new ArrayList<String>();
		for(GoldStandardGroup goldStandard: goldStandard()){
			addContent(contexts, goldStandard.context());
			addContent(providers, goldStandard.provider());
			addContent(labels, goldStandard.label());
		}
	
		printAll(providers, "Providers:");
		printAll(contexts, "Contexts:");
		printAll(labels, "Labels:");
		
	}

	private static void printAll(ArrayList<String> list, String label) {
		Collections.sort(list);
		System.out.println(label + "(" + list.size() + ")");
		for(int i = 0; i < list.size(); i++){
			System.out.print("	" + list.get(i));
			System.out.println();
		}
		System.out.println();
	}

	private static void addContent(ArrayList<String> list, String content) {
		if(!find(list, content))
			list.add(content);
	}

	private static boolean find(ArrayList<String> list, String value){
		boolean b = false;
		for(int i = 0; i < list.size(); i++){
			if(list.get(i).equalsIgnoreCase(value))
				b = true;
		}
		return b;
	}
	
	private static GoldStandardGroup[] goldStandard() {
		return new OrderedGroups(new UnorderedGroups(new File("../evaluation/gold-standard-enhanced"))).getGroups();
	}
}
