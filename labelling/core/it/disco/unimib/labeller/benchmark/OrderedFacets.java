package it.disco.unimib.labeller.benchmark;

import java.util.Arrays;
import java.util.Comparator;


public class OrderedFacets implements GoldStandard {

	private GoldStandard goldStandard;

	public OrderedFacets(GoldStandard goldStandard) {
		this.goldStandard = goldStandard;
	}

	@Override
	public GoldStandardFacet[] getFacets() {
		GoldStandardFacet[] facets = goldStandard.getFacets();
		Arrays.sort(facets, new Comparator<GoldStandardFacet>() {
			@Override
			public int compare(GoldStandardFacet o1, GoldStandardFacet o2) {
				String o1name = o1.provider() + o1.context();
				String o2name = o2.provider() + o2.context();
				return o1name.compareTo(o2name);
			}
		});
		return facets;
	}

	@Override
	public GoldStandardFacet getGroupById(int id) {
		return goldStandard.getGroupById(id);
	}
}
