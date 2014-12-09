package it.disco.unimib.labeller.index;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class ScaledDepths implements TypeConsistency {

	private HashMap<String, Double> depths;

	public ScaledDepths(InputFile file) throws Exception {
		this.depths = new HashMap<String, Double>();
		for(String line : file.lines()){
			String[] split = StringUtils.split(line, "|");
			depths.put(split[0], Double.parseDouble(split[1]));
		}
	}

	public double of(String type) {
		Double depth = depths.get(type);
		if(depth == null) depth = 1.0;
		return depth;
	}

	public double consistencyOf(Map<String, Double> objects) {
		double tot=0;
		for(String type : objects.keySet()){
			tot += (objects.get(type) * of(type));
		}
		if(!objects.isEmpty()) tot = tot / (double)objects.size();
		return tot;
	}
}
