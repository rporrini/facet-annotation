package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.GoldStandardGroup;
import it.disco.unimib.labeller.benchmark.OrderedGroups;
import it.disco.unimib.labeller.benchmark.UnorderedGroups;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GoldStandardSummary {

	public static void main(String[] args) throws Exception {
		
		HashMap<String, Integer> contexts = new HashMap<String, Integer>();
		HashMap<String, Integer> providers = new HashMap<String, Integer>();
		HashMap<String, Integer> labels = new HashMap<String, Integer>();
		for(GoldStandardGroup goldStandard: goldStandard()){
			addContent(contexts, goldStandard.context());
			addContent(providers, goldStandard.provider());
			addContent(labels, goldStandard.label());
		}
	
		printAll(providers, "Sources:");
		printAll(contexts, "Category Labels:");
		printAll(labels, "Predicates:");
	}

	private static void printAll(HashMap<String, Integer> list, String label) {
		ArrayList<String> keys = new ArrayList<String>(list.keySet());
		Collections.sort(keys);
		System.out.println(label + "(" + keys.size() + ")");
		for(String key : keys){
			System.out.print("\t" + key + " (" + list.get(key) + ")");
			System.out.println();
		}
		System.out.println();
	}

	private static void addContent(HashMap<String, Integer> list, String content) {
		if(!list.containsKey(content)) list.put(content, 0);
		list.put(content, list.get(content)+1);
	}
	
	private static GoldStandardGroup[] goldStandard() {
		return new OrderedGroups(new UnorderedGroups(new File("../evaluation/gold-standards/yago1-simple"))).getGroups();
	}
}
