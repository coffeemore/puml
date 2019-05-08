import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;
import net.sourceforge.plantuml.SourceStringReader;


/**
 * 
 * @author developer Klasse welche die Ausgabe des plantUML-Codes und die
 *         Klassendiagramme erzeugt
 */
public class OutputPUML
{
    /**
     * Konstruktor
     */
    public OutputPUML()
    {
    };

    /**
     * Liefert den plantUML - Code aus xml zurueck
     * 
     * @param diagramData plantUML-Code zur Erzeugung als xmlDoc
     * @return plantUML Code zur Erstellung mit plantuml.jar
     * @throws XPathExpressionException 
     */
    public String getPUML(Document diagramData) throws XPathExpressionException
    {
	    LogMain logger= new LogMain();
	    XPathFactory xPathfactory = XPathFactory.newInstance();
	    XPath xpath = xPathfactory.newXPath();
	    XPathExpression expr = xpath.compile("//parsed/*"); // Startpunkt parse Knoten
    	    NodeList list = (NodeList) expr.evaluate(diagramData, XPathConstants.NODESET); //in Liste
    	    String compare = list.item(0).getNodeName();
	if(compare == "classdiagram") {
    		
    	}
    	else if(compare == "sequencediagram"){

    	    String pumlCode = "@startuml \n";
    	    list = list.item(0).getChildNodes(); //Stelle: <classes>-Ebene
    	    for (int i = 0; i < list.getLength(); i++) //13 iterations MÜSSEN PER IF ABGEFRAGT WERDEN, DA SCHLIEẞENDE KNOTEN AUCH ANGEZEIGT WERDEN
	    {
		if (list.item(i).getNodeName() == "classes")
		{
		    list = list.item(i).getChildNodes(); // Ebene Tiefer <entry>-Ebene
		    for (int j = 0; j < list.getLength(); j++)
		    {
			if (list.item(j).getNodeName() != "#text")
			{
			    pumlCode += "participiant " + list.item(j).getTextContent() + "\n"; //Einträge Einfügen
			}
		    }
		    list = list.item(0).getParentNode().getChildNodes(); //Ebene hoch wechseln <classes>-Ebene
		}
		else if(list.item(i).getNodeName() == "entrypoint")
		{
		    if (list.item(i).getNodeName() == "class") // Abfrage auf den Klassennamen TODO hier anpassen der Abfrage
	    		{
	    		    tempClass = list.item(i).getTextContent();
	    		}
			else if (list.item(i).getNodeName() == "method") {
			    tempMethod = list.item(i).getTextContent();
			}
		}
		
	    }
    	    
//    	    //////////////////////////////////TEST
//    	    
//    	    expr = xpath.compile("//parsed/*"); //Entrypoint als Parent festlegen
//	    list = (NodeList) expr.evaluate(diagramData, XPathConstants.NODESET);
//	    list = list.item(0).getChildNodes().item(5).getChildNodes().item(1).getParentNode().getChildNodes();
//	    /*Rücksprung auf item(5) mit !! .item(1).getParentNode().getChildNodes() !!*/
//	    for (int i = 0; i < list.getLength(); i++)
//	    {
//		System.out.println(list.item(i).getNodeName());
//	    }
////	    System.out.println(list.item(0).getChildNodes().item(3).getNodeName()); //Hier kann durchitteriert werden  !!!!!
//	    System.out.println(list.item(0).getChildNodes().getLength());
//	    System.out.println("\n\n");
//	    
//	    //////////////////////////////////TEST ENDE
//    	    
    	    
    	    return pumlCode;
    		
    	}
    	else {
    		logger.getLog().warning("XML-Diagramm fehlerhaft");
    	}
	return "Error with Loop";
    }
    
    /**
     * Liefert den plantUML-Code zurueck
     * 
     * @param myParsingResult Ergebnisse des Parsens
     * @return String der den plantUML-Code enthaelt
     *
     *
     * Methode wird ersetzt durch xml basierte getPuml() Methode
     *
    public String getPUML(ParsingResult myParsingResult)
    {
	String pumlCode = "";
	int counter=0;
	pumlCode += "@startuml\n";
	while (myParsingResult.getAttributeLocalName(counter)!="parsed" && myParsingResult.isEndElement()) 
	{
		if(myParsingResult.getAttributeLocalName(counter)=="classes" && myParsingResult.isStartElement()) {
			while(myParsingResult.getAttributeLocalName(counter++)=="entry") {
					pumlCode+= "class " + myParsingResult.getElementText() + "\n";
			}
		}
		if(myParsingResult.getAttributeLocalName(counter)=="classrelations" && myParsingResult.isStartElement()) {
			if(myParsingResult.getAttributeLocalName(counter++)=="aggregations" && myParsingResult.isStartElement()) {
				if(myParsingResult.getAttributeLocalName(counter++)=="entry" && myParsingResult.isStartElement()) {
					if(myParsingResult.getAttributeLocalName(counter++)=="from") {
						pumlCode+= myParsingResult.getElementText()+ " o-- ";
						if(myParsingResult.getAttributeLocalName(counter++)=="to") {
							pumlCode+= myParsingResult.getElementText()+"\n";
						}
					}
				}
			}
			else if(myParsingResult.getAttributeLocalName(counter++)=="compositions" && myParsingResult.isStartElement()) {
				if(myParsingResult.getAttributeLocalName(counter++)=="entry" && myParsingResult.isStartElement()) {
					if(myParsingResult.getAttributeLocalName(counter++)=="from") {
						pumlCode+= myParsingResult.getElementText()+ " *-- ";
						if(myParsingResult.getAttributeLocalName(counter++)=="to") {
							pumlCode+= myParsingResult.getElementText()+"\n";
						}
					}
				}
			}
			else if(myParsingResult.getAttributeLocalName(counter++)=="extension" && myParsingResult.isStartElement()) {
				if(myParsingResult.getAttributeLocalName(counter++)=="entry" && myParsingResult.isStartElement()) {
					if(myParsingResult.getAttributeLocalName(counter++)=="from") {
						pumlCode+= myParsingResult.getElementText()+ " -- ";
						if(myParsingResult.getAttributeLocalName(counter++)=="to") {
							pumlCode+= myParsingResult.getElementText()+"\n";
						}
					}
				}
			}
		}
	counter++;
	}
	myParsingResult.close();
	pumlCode += "@enduml";
	return pumlCode;
    }
    */

    /**
     * Speichert den plantUML-Code aus XML Dokument der getPUML Methode in eine Datei
     * 
     * @param diagramData	Xml Document durch getPUML Methode erzeugt
     * @param filePath		Pfad an den die Datei gespeichert werden soll
     * @throws IOException 
     */
    public void savePUMLtoFile(Document diagramData, String filePath) throws IOException
    {
    	
    }
    
    /**
     * alte String basierte Methode
     * 
     * Speichert den plantUML-Code aus dem String der getPUML Methode in eine Datei
     * 
     * @param pumlCode		String der durch die getPUML Methode erzeugt wird
     * @param filePath		Pfad an den die Datei gespeichert werden soll
     * @throws IOException 
     *
    public void savePUMLtoFile(String pumlCode, String filePath) throws IOException
    {
	    BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filePath)));
	    bw.write(pumlCode);
	    bw.flush();
	    bw.close();
    }
    */

    /**
     * Erzeugt ein PlantUML-Diagramm aus der plantUML-Code-Datei am uebergebenen Pfad
     * 
     * @param sourcePath	Pfad an der die plantUML-Code-Datei liegt
     * @param destPath		Ordnerpfad, !!nicht Dateiname!!, an dem die png-Datei gespeichert wird, Name der PNG=Name der Textdatei
     * @throws IOException
     */
    public void createPUMLfromFile(String sourcePath, String destPath) throws IOException //
    {
	File source = new File(sourcePath);
	File dest = new File (destPath);
	SourceFileReader reader = new SourceFileReader(source, dest);
	List<GeneratedImage> list = reader.getGeneratedImages();
	list.get(0).getPngFile();
    }
    
    
    /**
     * Erzeugt ein PlantUML-Diagramm aus dem durch die Methode getPUML erzeugten String
     * 
     * @param filePath	Pfad an der die plantUML-PNG gespeichert werden soll
     * @param pumlCode	String der durch die getPUML Methode erzeugt wurde
     * @throws IOException
     */
    public void createPUMLfromString(String filePath, String pumlCode) throws IOException
    {
    	OutputStream png = new FileOutputStream(filePath);
    	SourceStringReader reader = new SourceStringReader(pumlCode);
    	reader.outputImage(png).getDescription();
    }
}
