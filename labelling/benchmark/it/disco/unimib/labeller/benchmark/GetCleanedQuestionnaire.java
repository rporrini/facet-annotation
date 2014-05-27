package it.disco.unimib.labeller.benchmark;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jopendocument.dom.spreadsheet.MutableCell;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

public class GetCleanedQuestionnaire {

	public static void main(String[] args) throws Exception {
		String uncleanedQuestionnaire = args[0];
		
		SpreadSheet document = SpreadSheet.createFromFile(new File(uncleanedQuestionnaire));
		Sheet resultSheet = document.getSheet(document.getSheetCount() - 1);
		
		List<String> rows = new ArrayList<String>();
		String lastFirstCell = "";
		for(int row=2; row<=7151; row++){
			MutableCell<SpreadSheet> relevanceColumn = resultSheet.getCellAt("C" + row);
			String relevance = relevanceColumn.getTextValue();
			if(!relevance.equals("0")){
				String firstCell = valueOf(resultSheet.getCellAt("A" + row));
				Object[] rowContent = new Object[]{
						firstCell,
						valueOf(resultSheet.getCellAt("B" + row)),
						relevanceValueOf(relevanceColumn),
						valueOf(resultSheet.getCellAt("D" + row)),
						valueOf(resultSheet.getCellAt("E" + row)),
				};
				List<Object> content = new ArrayList<Object>(Arrays.asList(rowContent));
				if(firstCell.startsWith("=HYPERLINK")){
					content.add(2, "");
					if(!lastFirstCell.startsWith("=HYPERLINK")){
						rows.add("PROPERTY|LABEL|RELEVANCE VALUE|CORRECT LABEL|EXAMPLES");
					}
				}
				lastFirstCell = firstCell;
				rows.add(StringUtils.join(content, "|"));
			}
		}
		String cleanedQuestionnaire = args[1];
		FileUtils.writeLines(new File(cleanedQuestionnaire), rows);
	}

	public static Object relevanceValueOf(MutableCell<SpreadSheet> cell){
		String formula = cell.getFormula();
		if(formula != null && formula.startsWith("=HYPERLINK")){
			return formula;
		}
		try{
			double value = Double.parseDouble(cell.getTextValue());
			if(value <= 2.0){
				return "";
			}
		}
		catch(Exception e){
			return cell.getTextValue(); 
		}
		return cell.getTextValue();
	}
	
	public static String valueOf(MutableCell<SpreadSheet> cell){
		return cell.getFormula() == null ? cell.getTextValue() : cell.getFormula(); 
	}
}
