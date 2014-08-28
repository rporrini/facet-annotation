package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.benchmark.Events;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.util.BytesRef;

public class RankInspection implements RankingStrategy{
	
	private RankingStrategy strategy;

	public RankInspection(RankingStrategy strategToInspect){
		this.strategy = strategToInspect;
	}
	
	@Override
	public void reRank(String context, GroupDocs<BytesRef> group, IndexSearcher indexSearcher) throws Exception {
		float previousScore = group.scoreDocs[0].score;
		strategy.reRank(context, group, indexSearcher);
		float scoreAfterRanking = group.scoreDocs[0].score;
		new Events().debug(indexSearcher.doc(group.scoreDocs[0].doc).get("property") + " - " + previousScore + " - " + group.totalHits + " - " + scoreAfterRanking);
	}
}