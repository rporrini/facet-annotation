package it.disco.unimib.labeller.index;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;


public class ScaledDepth {

	private HashMap<String, Double> depths;

	public ScaledDepth(InputFile file) throws Exception {
		this.depths = new HashMap<String, Double>();
		for(String line : file.lines()){
			String[] split = StringUtils.split(line, "|");
			depths.put(split[0], Double.parseDouble(split[1]));
		}
	}

	public Double of(String type) {
		Double depth = depths.get(type);
		if(depth == null) depth = 0.0;
		return depth;
	}
}
