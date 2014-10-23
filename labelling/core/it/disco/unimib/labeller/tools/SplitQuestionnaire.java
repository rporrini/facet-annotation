package it.disco.unimib.labeller.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

public class SplitQuestionnaire {

	public static void main(String[] args) throws Exception {
		
		String questionnaire = args[0];
		SpreadSheet document = SpreadSheet.createFromFile(new File(questionnaire));
		Sheet resultSheet = document.getSheet(document.getSheetCount() - 1);
		
		List<String> rows = new ArrayList<String>();
		int groups = 0;
		int part = 1;
		for(int row = 1; row<1235; row++){
			String content = cellValue(resultSheet, row, "A");
			if(content.equals("GROUP ID")){
				groups++;
			}
			if(groups == 14){
				FileUtils.writeLines(new File(args[1] + part), rows);
				part++;
				rows.clear();
				groups = 1;
			}
			rows.add(rowContent(resultSheet, row));
		}
		FileUtils.writeLines(new File(args[1] + 3), rows);
	}

	private static String rowContent(Sheet resultSheet, int row) {
		String rowContent = cellValue(resultSheet, row, "A");
		rowContent += "|" + cellValue(resultSheet, row, "B");
		rowContent += "|" + cellValue(resultSheet, row, "C");
		rowContent += "|" + cellValue(resultSheet, row, "D");
		rowContent += "|" + cellValue(resultSheet, row, "E");
		rowContent += "|" + cellValue(resultSheet, row, "F");
		rowContent += "|" + cellValue(resultSheet, row, "G");
		return rowContent;
	}

	private static String cellValue(Sheet resultSheet, int row, String column) {
		String cellValue = resultSheet.getCellAt(column+row).getFormula();
		if(cellValue == null)
			cellValue = resultSheet.getCellAt(column+row).getTextValue();
		return cellValue;
	}

}
