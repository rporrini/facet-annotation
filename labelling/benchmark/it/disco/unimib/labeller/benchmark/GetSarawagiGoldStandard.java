package it.disco.unimib.labeller.benchmark;

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

public class GetSarawagiGoldStandard {

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
				int columnId = Integer.parseInt(relationsNode.getAttribute("col2"));
				NodeList singleRelations = relationsNode.getElementsByTagName("rel");
				for(int j=0; j<singleRelations.getLength();j++){
					Element relationNode = (Element)singleRelations.item(j);
					String relation = relationNode.getTextContent();
					if(!relation.equalsIgnoreCase("NULL")){
						Document table = builder.parse(tableFrom(relationFiles));
						relationsNotInvolvingNulls++;
						ArrayList<String> groupContent = groupContent(table, columnId);
						String context = contextOf(table);
						if(!context.isEmpty()){
							relationsWithContext++;
							File group = new File(targetDirectory + "/sarawagi_" + context + "_" + relation + "_" + relationsWithContext);
							String sourceFile = tableFrom(relationFiles).getPath().replace("../", "");
							groupContent.add(0, "#" + sourceFile);
							FileUtils.writeLines(group, groupContent);
						}
					}
				}
			}
		}
		System.out.println("Relations in gold standard: " + totalRelations);
		System.out.println("Relations that do not involve nulls: "+ relationsNotInvolvingNulls);
		System.out.println("Relations with context: "+ relationsWithContext);
	}

	private static String contextOf(Document table) {
		String context = "";
		NodeList contexts = table.getElementsByTagName("context");
		for(int i=0; i<contexts.getLength(); i++){
			Element contextElement = (Element)contexts.item(i);
			Double score = Double.parseDouble(contextElement.getElementsByTagName("score").item(0).getTextContent());
			if(score == 1.0){
				context += contextElement.getElementsByTagName("text").item(0).getTextContent() + " ";
			}
		}
		return context.trim();
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
