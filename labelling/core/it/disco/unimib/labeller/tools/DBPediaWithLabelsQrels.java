package it.disco.unimib.labeller.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

public class DBPediaWithLabelsQrels {

	public static void main(String[] args) throws Exception {
		String questionnaire = args[0];
		SpreadSheet document = SpreadSheet.createFromFile(new File(questionnaire));
		Sheet resultSheet = document.getSheet(document.getSheetCount() - 1);
		
		List<String> rows = new ArrayList<String>();
		for(int row=1; row<=7151; row++){
			try{
				Long id = Long.parseLong(resultSheet.getCellAt("A" + row).getTextValue());
				String content = resultSheet.getCellAt("A" + row).getTextValue();
				while(!content.equals("DBPEDIA PROPERTY")){
					row++;
					content = resultSheet.getCellAt("A" + row).getTextValue();
				}
				int startingRowGroup = row + 1;
				while(!content.isEmpty()){
					if(content.startsWith("http://")){
						String propertyScore = resultSheet.getCellAt("C" + row).getTextValue();
						double rel = Double.parseDouble(propertyScore);
						String rowToAdd = id + " Q0 " + label(content) + " " + Math.round(rel);
						if(bestOrUnique(rel, content, startingRowGroup, resultSheet) && !rows.contains(rowToAdd)){
							rows.add(rowToAdd);
						}
					}
					row++;
					content = resultSheet.getCellAt("A" + row).getTextValue();
				}
			}
			catch(Exception e){}
		}
		FileUtils.writeLines(new File(args[1]), rows);
	}

	private static boolean bestOrUnique(double score, String selectedProperty, int startingRow, Sheet sheet) {
		String property = sheet.getCellAt("A" + startingRow).getTextValue();
		while(!property.isEmpty()){
			double propertyScore = Double.parseDouble(sheet.getCellAt("C"+startingRow).getTextValue());
			if(label(selectedProperty).equals(label(property)) && !(selectedProperty.equals(property)) && score < propertyScore){
				return false;
			}
			startingRow++;
			property = sheet.getCellAt("A" + startingRow).getTextValue();
		}
		return true;
	}

	private static String label(String content) {
		String[] splitted = content.split("/");
		return splitted[splitted.length-1];
	}
}
