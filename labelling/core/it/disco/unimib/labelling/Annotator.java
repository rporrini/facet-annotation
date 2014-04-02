package it.disco.unimib.labelling;

import java.util.List;

public interface Annotator {

	public List<String> annotate(String... instances) throws Exception;

}