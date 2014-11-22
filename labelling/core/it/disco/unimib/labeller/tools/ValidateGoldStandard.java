package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.GoldStandardFacet;
import it.disco.unimib.labeller.benchmark.OrderedFacets;
import it.disco.unimib.labeller.benchmark.UnorderedFacets;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class ValidateGoldStandard {

	public static void main(String[] args) throws IOException {
		String qRels = args[0];
		String groups = args[1];
		
		System.out.println("Validating " + qRels + " against groups " + groups);
		
		List<String> lines = FileUtils.readLines(new File(qRels));
		List<String> currentIds = new ArrayList<String>();
		for(String line : lines){
			currentIds.add(StringUtils.split(line, " ")[0]);
		}
		
		List<String> expectedIds = new ArrayList<String>();
		for(GoldStandardFacet group : goldStandard(groups)){
			expectedIds.add(group.id() + "");
		}
		
		Collections.sort(currentIds);
		Collections.sort(expectedIds);
		String previousId = currentIds.get(0);
		int i = 0;
		for(String id : currentIds){
			if(!previousId.equals(id)){
				previousId = id;
				i++;
			}
			String expectedId = expectedIds.get(i);
			if(!expectedId.equals(id)){
				System.out.println("Validation Failed");
				System.out.println("Expected " + expectedId + " but was " + id);
				System.exit(1);
			}
		}
		System.out.println("Validation Succedeed: id of groups are consistent.");
	}
	
	private static GoldStandardFacet[] goldStandard(String groups) {
		return new OrderedFacets(new UnorderedFacets(new File(groups))).getFacets();
	}
}
