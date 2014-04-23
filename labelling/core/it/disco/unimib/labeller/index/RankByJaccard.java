package it.disco.unimib.labeller.index;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.util.BytesRef;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaccardSimilarity;

public class RankByJaccard implements RankingStrategy{

	@Override
	public void reRank(String context, GroupDocs<BytesRef> group, IndexSearcher indexSearcher) throws Exception {
		String c = indexSearcher.doc(group.scoreDocs[0].doc).get("context");
		group.scoreDocs[0].score = (1 + new JaccardSimilarity().getSimilarity(context, c)) * (float)Math.sqrt(group.totalHits);
	}
	
}