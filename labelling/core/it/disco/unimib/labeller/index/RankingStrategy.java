package it.disco.unimib.labeller.index;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.util.BytesRef;

public interface RankingStrategy{
	
	public void reRank(GroupDocs<BytesRef> group, IndexSearcher indexSearcher) throws Exception;
}