import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
    private static NodeList list = null;
    LogMain logger = new LogMain();

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
	LogMain logger = new LogMain();
	XPathFactory xPathfactory = XPathFactory.newInstance();
	XPath xpath = xPathfactory.newXPath();
	XPathExpression expr = xpath.compile("//parsed/*"); // Startpunkt parsed Knoten
	list = (NodeList) expr.evaluate(diagramData, XPathConstants.NODESET); // in Liste
	String compare = list.item(0).getNodeName();
	String pumlCode = "@startuml \n";
	String tempString = "";
	if (compare == "classdiagramm")
	{
	    list = getList(diagramData, xpath, "//parsed/classdiagramm/classes/entry");
	    for (int a = 0; a < list.getLength(); a++)
	    {
		if (list.item(a).getNodeName() != "#text")
		{
		    pumlCode += "classes " + list.item(a).getTextContent() + " \n";
		}
	    }
	    list = getList(diagramData, xpath, "//parsed/classdiagramm/interfaces/entry");
	    for (int a = 0; a < list.getLength(); a++)
	    {
		if (list.item(a).getNodeName() != "#text")
		{
		    pumlCode += "interfaces " + list.item(a).getTextContent() + " \n";
		}
	    }

	    // EXTENSIONS
	    list = getList(diagramData, xpath, "//parsed/classdiagramm/classrelations/extensions/entry");
	    for (int a = 0; a < list.getLength(); a++)
	    {
		list = getList(diagramData, xpath, "//parsed/classdiagramm/classrelations/extensions/entry/to");
		if (list.item(a).getNodeName() != "#text")
		{
		    pumlCode += list.item(a).getTextContent() + " <|-- ";

		}
		list = getList(diagramData, xpath, "//parsed/classdiagramm/classrelations/extensions/entry/from");
		if (list.item(a).getNodeName() != "#text")
		{
		    pumlCode += list.item(a).getTextContent() + " \n";
		}
	    }

	    // IMPLEMENTATIONS
	    list = getList(diagramData, xpath, "//parsed/classdiagramm/classrelations/implementations/entry");
	    for (int a = 0; a < list.getLength(); a++)
	    {
		list = getList(diagramData, xpath, "//parsed/classdiagramm/classrelations/implementations/entry/to");
		if (list.item(a).getNodeName() != "#text")
		{
		    pumlCode += list.item(a).getTextContent() + " <|-- ";

		}
		list = getList(diagramData, xpath, "//parsed/classdiagramm/classrelations/implementations/entry/from");
		if (list.item(a).getNodeName() != "#text")
		{
		    pumlCode += list.item(a).getTextContent() + " \n";
		}
	    }

	    // COMPOSITIONS
	    list = getList(diagramData, xpath, "//parsed/classdiagramm/classrelations/compositions/entry");
	    for (int a = 0; a < list.getLength(); a++)
	    {
		list = getList(diagramData, xpath, "//parsed/classdiagramm/classrelations/compositions/entry/to");
		if (list.item(a).getNodeName() != "#text")
		{
		    pumlCode += list.item(a).getTextContent() + " *-- ";

		}
		list = getList(diagramData, xpath, "//parsed/classdiagramm/classrelations/compositions/entry/from");
		if (list.item(a).getNodeName() != "#text")
		{
		    pumlCode += list.item(a).getTextContent() + " \n";
		}
	    }

	    // AGGREGATIONS
	    list = getList(diagramData, xpath, "//parsed/classdiagramm/classrelations/aggregations/entry");
	    for (int a = 0; a < list.getLength(); a++)
	    {
		list = getList(diagramData, xpath, "//parsed/classdiagramm/classrelations/aggregations/entry/to");
		if (list.item(a).getNodeName() != "#text")
		{
		    pumlCode += list.item(a).getTextContent() + " o-- ";

		}
		list = getList(diagramData, xpath, "//parsed/classdiagramm/classrelations/aggregations/entry/from");
		if (list.item(a).getNodeName() != "#text")
		{
		    pumlCode += list.item(a).getTextContent() + " \n";
		}
	    }
	}

	else if (compare == "sequencediagram")
	{
	    NodeList methodList = (NodeList) expr.evaluate(diagramData, XPathConstants.NODESET); // für Funktionsaufruf in methoddefinition
	    String tempStartClass = "";
	    String tempEndClass = "";
	    String tempStartMethod = "";
	    String tempActiveMethod = "";
	    list = getList(diagramData, xpath, "//parsed/sequencediagram/classes/entry");
	    for (int a = 0; a < list.getLength(); a++)
	    {
		if (list.item(a).getNodeName() != "#text")
		{
		    pumlCode += "participant " + list.item(a).getTextContent() + " \n";
		}
	    }
	    list = getList(diagramData, xpath, "//parsed/sequencediagram/entrypoint/class");
	    for (int a = 0; a < list.getLength(); a++)
	    {
		if (list.item(a).getNodeName() != "#text")
		{
		    pumlCode += "note over " + list.item(a).getTextContent();
		    tempStartClass = list.item(a).getTextContent();
		}
	    }
	    list = getList(diagramData, xpath, "//parsed/sequencediagram/entrypoint/method");
	    for (int a = 0; a < list.getLength(); a++)
	    {
		if (list.item(a).getNodeName() != "#text")
		{
		    pumlCode += " " + list.item(a).getTextContent() + "\n";
		    tempStartMethod = list.item(a).getTextContent();
		}
	    }
	    list = getList(diagramData, xpath, "//parsed/sequencediagram/methoddefinition");
	    boolean finishedMethod[] = new boolean[list.getLength()]; 
	    for (int a = 0; a < list.getLength(); a++) //initializierung Abfrage, ob Methode behandelt
	    {
		finishedMethod[a] = false;
	    }
	    for(int a = 0; a < list.getLength(); a++) 
	    {
		if (list.item(a).getNodeName() != "#text") //methoddefinition TODO fortsetzen
		{
		    System.out.println("bin hier : " + list.item(a).getNodeName());
		    for(int b = 0; b < list.getLength(); b++)
		    if (list.item(a).getChildNodes().item(b).getNodeName() != "#text")
		    {
			System.out.println("Danach hier : " + a + " : "+ list.item(a).getChildNodes().item(b).getNodeName());
		    }
//		    methodList = list.item(a).getChildNodes()
		}
	    }
 		
	}
	else
	{
	    logger.getLog().warning("XML-Diagramm fehlerhaft");
	}
	// return "Error with Loop";
	pumlCode += "@enduml";
	return pumlCode;
    }

    public String helperMethodCall(NodeList list, String pumlCode, int i, String entry)
    {
	boolean caseOpen = false;
	return pumlCode;
    }

    /**
     * Liefert den plantUML-Code zurueck
     * 
     * @param myParsingResult Ergebnisse des Parsens
     * @return String der den plantUML-Code enthaelt
     *
     *
     *         Methode wird ersetzt durch xml basierte getPuml() Methode
     *
     *         public String getPUML(ParsingResult myParsingResult) { String
     *         pumlCode = ""; int counter=0; pumlCode += "@startuml\n"; while
     *         (myParsingResult.getAttributeLocalName(counter)!="parsed" &&
     *         myParsingResult.isEndElement()) {
     *         if(myParsingResult.getAttributeLocalName(counter)=="classes" &&
     *         myParsingResult.isStartElement()) {
     *         while(myParsingResult.getAttributeLocalName(counter++)=="entry") {
     *         pumlCode+= "class " + myParsingResult.getElementText() + "\n"; } }
     *         if(myParsingResult.getAttributeLocalName(counter)=="classrelations"
     *         && myParsingResult.isStartElement()) {
     *         if(myParsingResult.getAttributeLocalName(counter++)=="aggregations"
     *         && myParsingResult.isStartElement()) {
     *         if(myParsingResult.getAttributeLocalName(counter++)=="entry" &&
     *         myParsingResult.isStartElement()) {
     *         if(myParsingResult.getAttributeLocalName(counter++)=="from") {
     *         pumlCode+= myParsingResult.getElementText()+ " o-- ";
     *         if(myParsingResult.getAttributeLocalName(counter++)=="to") {
     *         pumlCode+= myParsingResult.getElementText()+"\n"; } } } } else
     *         if(myParsingResult.getAttributeLocalName(counter++)=="compositions"
     *         && myParsingResult.isStartElement()) {
     *         if(myParsingResult.getAttributeLocalName(counter++)=="entry" &&
     *         myParsingResult.isStartElement()) {
     *         if(myParsingResult.getAttributeLocalName(counter++)=="from") {
     *         pumlCode+= myParsingResult.getElementText()+ " *-- ";
     *         if(myParsingResult.getAttributeLocalName(counter++)=="to") {
     *         pumlCode+= myParsingResult.getElementText()+"\n"; } } } } else
     *         if(myParsingResult.getAttributeLocalName(counter++)=="extension" &&
     *         myParsingResult.isStartElement()) {
     *         if(myParsingResult.getAttributeLocalName(counter++)=="entry" &&
     *         myParsingResult.isStartElement()) {
     *         if(myParsingResult.getAttributeLocalName(counter++)=="from") {
     *         pumlCode+= myParsingResult.getElementText()+ " -- ";
     *         if(myParsingResult.getAttributeLocalName(counter++)=="to") {
     *         pumlCode+= myParsingResult.getElementText()+"\n"; } } } } }
     *         counter++; } myParsingResult.close(); pumlCode += "@enduml"; return
     *         pumlCode; }
     */

    /**
     * Speichert den plantUML-Code aus XML Dokument der getPUML Methode in eine
     * Datei
     * 
     * @param diagramData Xml Document durch getPUML Methode erzeugt
     * @param filePath    Pfad an den die Datei gespeichert werden soll
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
     * @param pumlCode String der durch die getPUML Methode erzeugt wird
     * @param filePath Pfad an den die Datei gespeichert werden soll
     * @throws IOException
     *
     *                     public void savePUMLtoFile(String pumlCode, String
     *                     filePath) throws IOException { BufferedWriter bw = new
     *                     BufferedWriter(new FileWriter(new File(filePath)));
     *                     bw.write(pumlCode); bw.flush(); bw.close(); }
     */

    /**
     * Erzeugt ein PlantUML-Diagramm aus der plantUML-Code-Datei am uebergebenen
     * Pfad
     * 
     * @param sourcePath Pfad an der die plantUML-Code-Datei liegt
     * @param destPath   Ordnerpfad, !!nicht Dateiname!!, an dem die png-Datei
     *                   gespeichert wird, Name der PNG=Name der Textdatei
     * @throws IOException
     */
    public void createPUMLfromFile(String sourcePath, String destPath) throws IOException //
    {
	File source = new File(sourcePath);
	File dest = new File(destPath);
	SourceFileReader reader = new SourceFileReader(source, dest);
	List<GeneratedImage> list = reader.getGeneratedImages();
	list.get(0).getPngFile();
    }

    /**
     * Erzeugt ein PlantUML-Diagramm aus dem durch die Methode getPUML erzeugten
     * String
     * 
     * @param filePath Pfad an der die plantUML-PNG gespeichert werden soll
     * @param pumlCode String der durch die getPUML Methode erzeugt wurde
     * @throws IOException
     */
    public void createPUMLfromString(String filePath, String pumlCode) throws IOException
    {
	OutputStream png = new FileOutputStream(filePath);
	SourceStringReader reader = new SourceStringReader(pumlCode);
	reader.outputImage(png).getDescription();
    }

    private static NodeList getList(Document doc, XPath xpath, String path)
    {
	try
	{
	    XPathExpression expr = xpath.compile(path);
	    list = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
	}
	catch (XPathExpressionException e)
	{
	    e.printStackTrace();
	}
	return list;
    }
}
