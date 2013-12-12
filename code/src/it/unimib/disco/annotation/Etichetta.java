package it.unimib.disco.annotation;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;


import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Etichetta {
	
	public Etichetta(){
	}
	
	public void generaEtichetta(String originalText, int contRiga, int i2, String pathfileGRXML, String pathEtichettaturaGR){

		 try {

	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            Document doc = factory.newDocumentBuilder().parse(new File(pathfileGRXML + "fileGR" + i2 + contRiga + ".xml"));

	            XPathFactory xFactory = XPathFactory.newInstance();
	            XPath xPath = xFactory.newXPath();
	            XPathExpression exp2 = xPath.compile("/group/Entity");

	            //estraggo tutte le entità trovate per una data riga
	            NodeList nl1 = (NodeList)exp2.evaluate(doc.getFirstChild(), XPathConstants.NODESET);
	            System.out.println("numero entità per questa riga: " + nl1.getLength());
	          
	            Vector<Integer> indiciNot1 = new Vector<Integer>();
	            Vector<String> vettNomiEnt = new Vector<String>();
	            Vector<Float> vettPesiEnt = new Vector<Float>();
	            int cont1 = 0;
	            int contEntDiverse = 0;
	            for(int i = 0; i < nl1.getLength(); i++) {
	            	if (indiciNot1.contains(i)==false){
	            		cont1=1;
	            		int j = i + 1;
	            		NamedNodeMap p = null;
	            		p =  nl1.item(i).getAttributes();
						Node label = p.getNamedItem("nome");
						String labelStr= label.getNodeValue();
	            		while (j < nl1.getLength()){
	            			if (indiciNot1.contains(j)==false){
								p =  nl1.item(j).getAttributes();
								label = p.getNamedItem("nome");
								String labelStr2= label.getNodeValue();
								if(labelStr.equals(labelStr2)) {
	            					cont1++;
	            					indiciNot1.add(j);
	            				}
	            			}
	            			j++;
	            		}
	            		vettNomiEnt.add(labelStr);
	            		vettPesiEnt.add(((float)cont1/((float)nl1.getLength())));
	            		//System.out.println("il peso di "+ labelStr +" è "+ (float)cont1/((float)nl1.getLength()));
	            		contEntDiverse++;
	            	}
	            }
	            //System.out.println(vettNomiEnt.toString());
	            //System.out.println(vettPesiEnt.toString());
				
				System.out.println("TYPE");
				XPathExpression exp = xPath.compile("//TYPE/text()");
				NodeList nl = (NodeList)exp.evaluate(doc.getFirstChild(), XPathConstants.NODESET);
		           
				Vector<Integer> indiciNot = new Vector<Integer>();
				Vector<String> vettNomiType = new Vector<String>();
				Vector<Float> vettPesiType = new Vector<Float>();
				int cont = 0;
				int contTypeDiverse = 0;
				for(int i = 0; i < nl.getLength(); i++) {
					if (indiciNot.contains(i)==false){
						cont=1;
						int j = i + 1;
						while (j < nl.getLength()){
							if (indiciNot.contains(j)==false)
								if(nl.item(i).getTextContent().equals(nl.item(j).getTextContent())) {
									cont++;
									indiciNot.add(j);
								}
							j++;
						} 
						//System.out.println("il peso di "+ nl.item(i).getNodeValue() +" è "+ (float)cont/((float)nl.getLength()));
						contTypeDiverse++;
						vettNomiType.add(nl.item(i).getNodeValue());
						vettPesiType.add((float)cont/((float)nl.getLength()));
					}
				}
				//System.out.println(vettNomiType.toString());
				//System.out.println(vettPesiType.toString());
		            
	            System.out.println("categ");
	            exp = xPath.compile("//CATEGORY/text()");
	            nl = (NodeList)exp.evaluate(doc.getFirstChild(), XPathConstants.NODESET);
	           
	            indiciNot = new Vector<Integer>();
	            Vector<String> vettNomiCat = new Vector<String>();
	            Vector<Float> vettPesiCat = new Vector<Float>();
	            cont = 0;
	            int contCatDiverse = 0;
	            for(int i = 0; i < nl.getLength(); i++) {
	            	if (indiciNot.contains(i)==false){
	            		cont=1;
	            		int j = i + 1;
	            		while (j < nl.getLength()){
	            			if (indiciNot.contains(j)==false)
	            				if(nl.item(i).getTextContent().equals(nl.item(j).getTextContent())) {
	            					cont++;
	            					indiciNot.add(j);
	            				}
	            			j++;
	            		} 
	            		/*NamedNodeMap attributi = nl1.item(0).getAttributes();
	            		String str = attributi.item(0).getTextContent();
	            		String str1 = nl.item(i).getNodeValue();
	            		//if (str1.equals(str))
	            		//	cont++;*/
	            	//	System.out.println("il peso di "+ nl.item(i).getNodeValue() +" è "+ (float)cont/((float)nl.getLength()));
	            		contCatDiverse++;
	            		vettNomiCat.add(nl.item(i).getNodeValue());
	            		vettPesiCat.add((float)cont/((float)nl.getLength()));
	            	}
	            }
	       /*per normalizzare     for (int i = 0; i < vettPesiCat.size(); i++){
	            	vettPesiCat.setElementAt(((float)vettPesiCat.elementAt(i)/(float)contCatDiverse), i);
	            }
	           */ 
	            //System.out.println(vettNomiCat.toString());
	            // System.out.println(vettPesiCat.toString());
	            // exp = xPath.compile("//PROPERTY/text()");
	            
	            System.out.println("proprerty");
	            exp = xPath.compile("//PROPERTY");  //per calcolarmi il fattore di normalizzazione
	            nl = (NodeList)exp.evaluate(doc.getFirstChild(), XPathConstants.NODESET);
	            indiciNot = new Vector<Integer>();
	            int numeroProp =0;
        		int contTot=0;
	            for(int i = 0; i < nl.getLength(); i++) {
	            	if (indiciNot.contains(i)==false){
	            		Node n = nl.item(i);
	            		NamedNodeMap attributi = n.getAttributes();
	            		String str = attributi.item(0).getTextContent();
	            		numeroProp = Integer.parseInt(str);
	            		int j = i + 1;
	            		while (j < nl.getLength()){
	            			if (indiciNot.contains(j)==false){
	            				if(nl.item(i).getTextContent().equals(nl.item(j).getTextContent())) {
	            					indiciNot.add(j);
	            					n = nl.item(j);
	                    			attributi = n.getAttributes();
	                    			str = attributi.item(0).getTextContent();
	        	            		numeroProp = numeroProp + Integer.parseInt(str);	        	            		
	            				}
	            			}  					
	            			j++;
	            		}
	            		contTot = numeroProp+contTot;
	            	}
	            }
	            
	            //ora calcolo i pesi veri e propri delle property
	            exp = xPath.compile("//PROPERTY");
	            nl = (NodeList)exp.evaluate(doc.getFirstChild(), XPathConstants.NODESET);
	           
	            indiciNot = new Vector<Integer>();
	            Vector<String> vettNomiProp = new Vector<String>();
	            Vector<Float> vettPesiProp = new Vector<Float>();
	            cont = 0;
	            numeroProp =0;
	            for(int i = 0; i < nl.getLength(); i++) {
	            	if (indiciNot.contains(i)==false){
	            		cont=1;
	            		Node n = nl.item(i);
            			NamedNodeMap attributi = n.getAttributes();
	            		String str = attributi.item(0).getTextContent();
	            		numeroProp = Integer.parseInt(str);
	            		String str1 = nl.item(i).getTextContent();
	            		int j = i + 1;
	            		while (j < nl.getLength()){
	            			if (indiciNot.contains(j)==false){
	            				if(nl.item(i).getTextContent().equals(nl.item(j).getTextContent())) {
	            					cont++;
	            					indiciNot.add(j);
	            					n = nl.item(j);
	                    			attributi = (nl.item(j)).getAttributes();
	                    			str = attributi.item(0).getTextContent();
	        	            		numeroProp = numeroProp + Integer.parseInt(str);	        	            		
	            				}
	            			}  					
	            			j++;
	            		} 
	            		//info stampa video -> System.out.println("il peso di "+ n.getTextContent() +" è "+ (float)cont /((float)nl.getLength()));
	            		//                     System.out.println("il contatore è "+ numeroProp +" peso -> "+ (float)numeroProp/(float)contTot);
	            		vettPesiProp.add((float)numeroProp/(float)contTot);
	            		vettNomiProp.add(str1);	            		
	            	}
	            }

	            // Stampo a video i vettori -> System.out.println(vettNomiProp.toString());
	            //                             System.out.println(vettPesiProp.toString());
	            //trova i MASSIMI
	            //scrivo il file
	            FileWriter writer = new FileWriter(pathEtichettaturaGR + "eticGR" + i2 + ".txt", true);
	            BufferedWriter b=new BufferedWriter(writer);

	            b.write(originalText+"\n");
	            if (vettPesiEnt.size() > 0){
	            	float valMaxEntity = trovaMassimo(vettPesiEnt.toArray());
	            	for (int i = 0; i < vettPesiEnt.size(); i++){
	            		if (vettPesiEnt.elementAt(i) == valMaxEntity)
	            			b.write("Etichetta entità: "+ vettNomiEnt.elementAt(i) +" -> peso: "+ valMaxEntity +"\n");
	            	}
	            }
	            
	            if (vettPesiType.size() > 0){
	            	float valMaxType = trovaMassimo(vettPesiType.toArray());
	            	for (int i = 0; i < vettPesiType.size(); i++){
	            		if (vettPesiType.elementAt(i) == valMaxType)
	            			b.write("Etichetta type: "+ vettNomiType.elementAt(i) +" -> peso: "+ valMaxType +"\n");
	            	}
	            }
	            
	            if (vettPesiCat.size() > 0){
	            	float valMaxCat = trovaMassimo(vettPesiCat.toArray());
	 	            for (int i = 0; i < vettPesiCat.size(); i++){
		            	if (vettPesiCat.elementAt(i) == valMaxCat)
		            		b.write("Etichetta category: "+ vettNomiCat.elementAt(i) +" -> peso: "+ valMaxCat +"\n");
		            }
	            }
	            
	            if (vettPesiProp.size() > 0){
	            	float valMaxProp = trovaMassimo(vettPesiProp.toArray());
	            	for (int i = 0; i < vettPesiProp.size(); i++){
	            		if (vettPesiProp.elementAt(i) == valMaxProp)
	            			b.write("Etichetta property: "+ vettNomiProp.elementAt(i) +" -> peso: "+ valMaxProp +"\n");
	            	}
	            }
	            
	            b.write("\n");
	            b.flush();
	            b.close();
	          	
	        } catch (Exception ex) {
	        	System.out.println("si è verificato un errore");
	        }    
	}

	private static float trovaMassimo(Object[] array) {
		Arrays.sort(array);
		float massimo = (float) array[array.length-1];
		return massimo;
	}

}
