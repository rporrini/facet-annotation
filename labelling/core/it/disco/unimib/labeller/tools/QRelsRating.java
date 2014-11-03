package it.disco.unimib.labeller.tools;

import java.util.List;

class QRelsRating implements Rating{
	
	public void store(Long id, String content, String propertyScore, List<String> rows) {
		if(propertyScore.equals("#DIV/0!")){
			double rel = 0.0;
			rows.add(id + " Q0 " + content + " " + Math.round(rel));
		}
		else{
			double rel = Double.parseDouble(propertyScore);
			rows.add(id + " Q0 " + content + " " + Math.round(rel));
		}
	}
}