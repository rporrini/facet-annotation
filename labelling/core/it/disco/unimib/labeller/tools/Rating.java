package it.disco.unimib.labeller.tools;

import java.util.List;

interface Rating {

	public void store(Long id, String content, String propertyScore,
			List<String> rows);

}