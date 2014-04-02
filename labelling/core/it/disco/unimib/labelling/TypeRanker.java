package it.disco.unimib.labelling;

import java.net.URISyntaxException;

public interface TypeRanker {

	public String typeOf(String... entity) throws URISyntaxException;

}