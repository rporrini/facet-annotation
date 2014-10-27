package it.disco.unimib.labeller.tools;

import java.util.List;

import org.apache.commons.io.IOUtils;

class TrecEval{
	public static List<String> run(int topK, String goldStandard, String qrels, String measure) throws Exception {
		Process result = Runtime.getRuntime().exec("trec_eval -n -q -M " + topK
													+ " -m " + measure
													+ " " + goldStandard
													+ " " + qrels);
		result.waitFor();
		return IOUtils.readLines(result.getInputStream());
	}

}