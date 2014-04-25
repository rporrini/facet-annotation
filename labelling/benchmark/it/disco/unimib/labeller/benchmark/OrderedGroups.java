package it.disco.unimib.labeller.benchmark;

import java.util.Arrays;
import java.util.Comparator;


public class OrderedGroups implements GoldStandard {

	private GoldStandard goldStandard;

	public OrderedGroups(GoldStandard goldStandard) {
		this.goldStandard = goldStandard;
	}

	@Override
	public GoldStandardGroup[] getGroups() {
		GoldStandardGroup[] unorderedGroups = goldStandard.getGroups();
		Arrays.sort(unorderedGroups, new Comparator<GoldStandardGroup>() {
			@Override
			public int compare(GoldStandardGroup o1, GoldStandardGroup o2) {
				String o1name = o1.provider() + o1.context();
				String o2name = o2.provider() + o2.context();
				return o1name.compareTo(o2name);
			}
		});
		return unorderedGroups;
	}
}
