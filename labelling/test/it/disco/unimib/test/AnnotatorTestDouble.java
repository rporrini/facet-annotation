package it.disco.unimib.test;

import it.disco.unimib.labelling.Annotator;

import java.util.ArrayList;
import java.util.List;

public class AnnotatorTestDouble implements Annotator{

	@Override
	public List<String> annotate(String... instances) throws Exception {
		return new ArrayList<String>();
	}
	
}