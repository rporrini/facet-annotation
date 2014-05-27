package it.disco.unimib.labeller.benchmark;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

public class GetGoldStandardQrels {

	public static void main(String[] args) throws Exception {
		String questionnaire = args[0];
		
		SpreadSheet document = SpreadSheet.createFromFile(new File(questionnaire));
		Sheet resultSheet = document.getSheet(document.getSheetCount() - 1);
		
		List<String> rows = new ArrayList<String>();
		for(int row=1; row<=7151; row++){
			try{
				Long id = Long.parseLong(resultSheet.getCellAt("A" + row).getTextValue());
				String content = resultSheet.getCellAt("A" + row).getTextValue();
				while(!content.isEmpty()){
					if(content.startsWith("http://")){
						double rel = Double.parseDouble(resultSheet.getCellAt("C" + row).getTextValue());
						rows.add(id + " Q0 " + content + " " + Math.round(rel));
					}
					row++;
					content = resultSheet.getCellAt("A" + row).getTextValue();
				}
			}
			catch(Exception e){}
		}
		FileUtils.writeLines(new File(args[1]), rows);
	}
}
