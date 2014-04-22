package it.disco.unimib.labeller.labelling;

import java.util.Arrays;
import java.util.List;

public class PassTrough implements Annotator{

	@Override
	public List<String> annotate(String... instances) throws Exception {
		return Arrays.asList(instances);
	}
}