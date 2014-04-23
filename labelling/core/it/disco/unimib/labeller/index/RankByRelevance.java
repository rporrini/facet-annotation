package it.disco.unimib.labeller.index;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.util.BytesRef;

public class RankByRelevance implements RankingStrategy{

	@Override
	public void reRank(String context, GroupDocs<BytesRef> group, IndexSearcher indexSearcher) throws Exception {
		group.scoreDocs[0].score = group.scoreDocs[0].score * group.totalHits;
	}
	
}