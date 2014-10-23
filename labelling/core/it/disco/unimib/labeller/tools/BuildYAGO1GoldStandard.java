package it.disco.unimib.labeller.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class BuildYAGO1GoldStandard {

	public static void main(String[] args) throws Exception {
		String targetDirectory = args[0];		
		String goldStandardPath = args[1];
		
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = documentFactory.newDocumentBuilder(); 
		Collection<File> relationsFiles = getRelationsFiles(goldStandardPath);
		int relationsNotInvolvingNulls = 0;
		int relationsWithContext = 0;
		int totalRelations = 0;
		for(File relationFiles : relationsFiles){
			NodeList relations = builder.parse(relationFiles).getElementsByTagName("relAnnos");
			totalRelations += relations.getLength();
			for(int i = 0; i < relations.getLength(); i++){
				Element relationsNode = (Element)relations.item(i);
				int secondColumnId = Integer.parseInt(relationsNode.getAttribute("col2"));
				int firstColumnId = Integer.parseInt(relationsNode.getAttribute("col1"));
				NodeList singleRelations = relationsNode.getElementsByTagName("rel");
				for(int j=0; j<singleRelations.getLength();j++){
					Element relationNode = (Element)singleRelations.item(j);
					String relation = relationNode.getTextContent();
					if(!relation.equalsIgnoreCase("NULL")){
						Document table = builder.parse(tableFrom(relationFiles));
						relationsNotInvolvingNulls++;
						String contextObtainedFromFirstColumn = contextOf(builder.parse(relationFiles), firstColumnId);
						String contextObtainedFromSecondColumn = contextOf(builder.parse(relationFiles), secondColumnId);
						if(!contextObtainedFromFirstColumn.isEmpty()){
							relationsWithContext++;
							saveGroup(targetDirectory, relationsWithContext, relationFiles, relation, groupContent(table, secondColumnId), contextObtainedFromFirstColumn);
						}
						if(!contextObtainedFromSecondColumn.isEmpty()){
							relationsWithContext++;
							saveGroup(targetDirectory, relationsWithContext, relationFiles, relation, groupContent(table, firstColumnId), contextObtainedFromSecondColumn);
						}
					}
				}
			}
		}
		System.out.println("Relations in gold standard: " + totalRelations);
		System.out.println("Relations that do not involve nulls: "+ relationsNotInvolvingNulls);
		System.out.println("Relations with context: "+ relationsWithContext);
	}

	private static void saveGroup(String targetDirectory, int relationsWithContext, File relationFiles, String relation,
								  ArrayList<String> groupContent, String context) throws Exception {
		File group = new File(targetDirectory + "/"+ relationsWithContext + "_sarawagi_" + context.trim() + "_" + relation);
		String sourceFile = tableFrom(relationFiles).getPath().replace("../", "");
		groupContent.add(0, "#" + sourceFile);
		FileUtils.writeLines(group, groupContent);
	}

	private static String contextOf(Document table, int columnId) {
		String context = "";
		NodeList columns = table.getElementsByTagName("colAnnos");
		for(int i = 0; i < columns.getLength(); i++){
			Element columnElement = (Element) columns.item(i);
			int column = Integer.parseInt(columnElement.getAttribute("col"));
			if(column == columnId){
				NodeList contexts = columnElement.getElementsByTagName("anno");
				for(int j = 0; j < contexts.getLength(); j++){
					Element contextElement = (Element) contexts.item(j);
					context += " " + contextElement.getAttribute("name");
				}
			}
		}
		return context.replace("_", "-");
	}

	private static ArrayList<String> groupContent(Document tablesDocument, int secondCell) {
		ArrayList<String> groupContent = new ArrayList<String>();
		NodeList rows = tablesDocument.getElementsByTagName("row");
		for(int i = 0; i < rows.getLength(); i++){
			Element row = (Element) rows.item(i);
			NodeList rowcells = row.getElementsByTagName("cell");
			groupContent.add(valueOf(secondCell, rowcells));
		}
		return groupContent;
	}

	private static String valueOf(int cellNumber, NodeList cells) {
		Element cell = (Element) cells.item(cellNumber);
		String cellValue = "";
		try{
			cellValue = cell.getElementsByTagName("text").item(0).getFirstChild().getNodeValue();
		}catch(Exception e){}
		return cellValue;
	}

	private static Collection<File> getRelationsFiles(String goldStandardPath) throws IOException {
		return FileUtils.listFiles(new File(goldStandardPath + "workspace/WWT_GroundTruth/annotation"), new String[]{"xml"}, true);
	}
	
	private static File tableFrom(File labelFile) {
		return new File(labelFile.getPath().replace("workspace/WWT_GroundTruth/annotation", "tablesForAnnotation"));
	}
}
