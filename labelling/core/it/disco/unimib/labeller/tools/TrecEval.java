package it.disco.unimib.labeller.tools;

import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

class TrecEval{
	public static List<String> run(int topK, String goldStandard, String qrels, String measure) throws Exception {
		String command = "trec_eval -q -M " + topK + " -m " + measure
													+ " " + goldStandard
													+ " " + qrels;
		System.out.println(command);
		Process result = Runtime.getRuntime().exec(command);
		result.waitFor();
		System.out.println(StringUtils.join(IOUtils.readLines(result.getErrorStream()), "\n"));
		return IOUtils.readLines(result.getInputStream());
	}

}