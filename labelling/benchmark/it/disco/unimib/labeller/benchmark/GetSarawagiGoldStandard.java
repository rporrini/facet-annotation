package it.disco.unimib.labeller.benchmark;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class GetSarawagiGoldStandard {

	public static void main(String[] args) throws Exception {
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = documentFactory.newDocumentBuilder(); 
		HashSet<File> labelFiles = getLabelFiles();
		int duplicated = 1;
		for(File labelFile : labelFiles){
			Document labelsDocument = builder.parse(new File(labelFile.getPath()));
			Document tablesDocument = builder.parse(getTableFile(labelFile));

			NodeList relationships = labelsDocument.getElementsByTagName("relAnnos");
			
			String cell1 = "";
			String cell2 = "";
			String label = "";
			for(int i = 0; i < relationships.getLength(); i++){
				Element relationship = (Element) relationships.item(i);
				if(!relationship.getElementsByTagName("rel").item(0).getFirstChild().getNodeValue().equalsIgnoreCase("NULL")){
					cell1 = relationship.getAttribute("col1");
					cell2 = relationship.getAttribute("col2");
					label = relationship.getElementsByTagName("rel").item(0).getFirstChild().getNodeValue();
				}
			}
			
			NodeList headers = tablesDocument.getElementsByTagName("header");

			if(headers.getLength() != 0){
				Element header = (Element) headers.item(0);
				NodeList cells = header.getElementsByTagName("cell");
				
				String context = getCellValue(cell1, cells);
				ArrayList<String> groupContent = new ArrayList<String>();
				
				NodeList rows = tablesDocument.getElementsByTagName("row");
				for(int i = 0; i < rows.getLength(); i++){
					Element row = (Element) rows.item(i);
					NodeList rowcells = row.getElementsByTagName("cell");
					groupContent.add(getCellValue(cell2, rowcells));
				}
				File group = new File("../evaluation/sarawagi-gold-standard/sarawagi_" + context + "_" + label + "_link");
				if(group.exists()){
					group = new File("../evaluation/sarawagi-gold-standard/sarawagi_" + context + "_" + label + "_link" + duplicated);
					duplicated++;
				}
				FileUtils.write(group, "#" + getTableFile(labelFile).getPath().replace("..", "") + "\n", true);
				for(String line : groupContent){
					FileUtils.write(group, line + "\n", true);
				}
			}
		}
	}

	private static String getCellValue(String cellNumber, NodeList cells) {
		Element cell = (Element) cells.item(Integer.parseInt(cellNumber));
		String cellValue = "";
		try{
			cellValue = cell.getElementsByTagName("text").item(0).getFirstChild().getNodeValue();
		}catch(Exception e){}
		
		return cellValue;
	}

	private static HashSet<File> getLabelFiles() throws IOException {
		Collection<File> listFiles = FileUtils.listFiles(new File("../evaluation/tools/annotationData/workspace/WWT_GroundTruth/annotation"), new String[]{"xml"}, true);
		HashSet<File> labelFiles = new HashSet<File>();
		for(File file : listFiles){
			List<String> lines = FileUtils.readLines(file);
			for(String line : lines){
				if(line.contains("<rel>") && !line.contains("<rel>NULL")) {
					labelFiles.add(file);
				}
			}
		}
		return labelFiles;
	}
	
	private static File getTableFile(File labelFile) {
		String newPath = labelFile.getPath().replace("workspace/WWT_GroundTruth/annotation", "tablesForAnnotation");
		return new File(newPath);
	}
}
