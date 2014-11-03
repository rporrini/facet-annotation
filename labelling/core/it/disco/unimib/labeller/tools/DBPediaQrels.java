package it.disco.unimib.labeller.tools;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

public class DBPediaQrels {

	public static void main(String[] args) throws Exception {
		String questionnaire = args[0];
		
		SpreadSheet document = SpreadSheet.createFromFile(new File(questionnaire));
		Sheet resultSheet = document.getSheet(document.getSheetCount() - 1);
		
		QuestionnaireRatings questionaireRatings = new QuestionnaireRatings(new QRelsRating());
		
		FileUtils.writeLines(new File(args[1]), questionaireRatings.getAnswers(resultSheet));
	}
}
