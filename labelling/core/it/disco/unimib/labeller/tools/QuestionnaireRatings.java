package it.disco.unimib.labeller.tools;

import java.util.ArrayList;
import java.util.List;

import org.jopendocument.dom.spreadsheet.Sheet;

class QuestionnaireRatings{
	
	private Rating questionnaireRating;

	QuestionnaireRatings(Rating rating) {
		this.questionnaireRating = rating;
	}

	public List<String> getAnswers(Sheet resultSheet) {
		
		List<String> rows = new ArrayList<String>();
		for(int row=1; row<=7151; row++){
			try{
				Long id = Long.parseLong(resultSheet.getCellAt("A" + row).getTextValue());
				String content = resultSheet.getCellAt("A" + row).getTextValue();
				while(!content.equals("DBPEDIA PROPERTY")){
					row++;
					content = resultSheet.getCellAt("A" + row).getTextValue();
				}
				while(!content.isEmpty()){
					if(content.startsWith("http://")){
						String propertyScore = resultSheet.getCellAt("C" + row).getTextValue();
						questionnaireRating.store(id, content, propertyScore, rows);
					}
					row++;
					content = resultSheet.getCellAt("A" + row).getTextValue();
				}
			}
			catch(Exception e){
				//e.printStackTrace();
			}
		}
		return rows;
	}
}