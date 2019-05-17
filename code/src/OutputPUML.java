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
    public NodeList list = null;
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
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = xpath.compile("/parsed/*"); // Startpunkt parsed Knoten
		list = (NodeList) expr.evaluate(diagramData, XPathConstants.NODESET); // in Liste
		String compare = list.item(0).getNodeName();
		String pumlCode = "@startuml\n";
		if (compare == "classdiagramm")
		{
		    list = XmlHelperMethods.getList(diagramData, "/parsed/classdiagramm/classes/entry");
		    for (int a = 0; a < list.getLength(); a++)
		    {
				if (list.item(a).getNodeName() != "#text")
				{
				    pumlCode += "class " + list.item(a).getTextContent() + "\n";
				}
		    }
		    list = XmlHelperMethods.getList(diagramData, "/parsed/classdiagramm/interfaces/entry");
		    for (int a = 0; a < list.getLength(); a++)
		    {
				if (list.item(a).getNodeName() != "#text")
				{
				    pumlCode += "interface " + list.item(a).getTextContent() + "\n";
				}
		    }
	
		    // EXTENSIONS
		    list = XmlHelperMethods.getList(diagramData, "/parsed/classdiagramm/classrelations/extensions/entry");
		    for (int a = 0; a < list.getLength(); a++)
		    {
				list = XmlHelperMethods.getList(diagramData, "/parsed/classdiagramm/classrelations/extensions/entry/to");
				if (list.item(a).getNodeName() != "#text")
				{
				    pumlCode += list.item(a).getTextContent() + " <|-- ";
		
				}
				list = XmlHelperMethods.getList(diagramData, "/parsed/classdiagramm/classrelations/extensions/entry/from");
				if (list.item(a).getNodeName() != "#text")
				{
				    pumlCode += list.item(a).getTextContent() + "\n";
				}
		    }
	
		    // IMPLEMENTATIONS
		    list = XmlHelperMethods.getList(diagramData, "/parsed/classdiagramm/classrelations/implementations/entry");
		    for (int a = 0; a < list.getLength(); a++)
		    {
				list = XmlHelperMethods.getList(diagramData, "/parsed/classdiagramm/classrelations/implementations/entry/to");
				if (list.item(a).getNodeName() != "#text")
				{
				    pumlCode += list.item(a).getTextContent() + " <|-- ";
		
				}
				list = XmlHelperMethods.getList(diagramData, "/parsed/classdiagramm/classrelations/implementations/entry/from");
				if (list.item(a).getNodeName() != "#text")
				{
				    pumlCode += list.item(a).getTextContent() + "\n";
				}
		    }
	
		    // COMPOSITIONS
		    list = XmlHelperMethods.getList(diagramData, "/parsed/classdiagramm/classrelations/compositions/entry");
		    for (int a = 0; a < list.getLength(); a++)
		    {
				list = XmlHelperMethods.getList(diagramData, "/parsed/classdiagramm/classrelations/compositions/entry/to");
				if (list.item(a).getNodeName() != "#text")
				{
				    pumlCode += list.item(a).getTextContent() + " *-- ";
		
				}
				list = XmlHelperMethods.getList(diagramData, "/parsed/classdiagramm/classrelations/compositions/entry/from");
				if (list.item(a).getNodeName() != "#text")
				{
				    pumlCode += list.item(a).getTextContent() + "\n";
				}
		    }
	
		    // AGGREGATIONS
		    list = XmlHelperMethods.getList(diagramData, "/parsed/classdiagramm/classrelations/aggregations/entry");
		    for (int a = 0; a < list.getLength(); a++)
		    {
				list = XmlHelperMethods.getList(diagramData, "/parsed/classdiagramm/classrelations/aggregations/entry/to");
				if (list.item(a).getNodeName() != "#text")
				{
				    pumlCode += list.item(a).getTextContent() + " o-- ";
		
				}
				list = XmlHelperMethods.getList(diagramData, "/parsed/classdiagramm/classrelations/aggregations/entry/from");
				if (list.item(a).getNodeName() != "#text")
				{
				    pumlCode += list.item(a).getTextContent() + "\n";
				}
		    }
		}
	
		else if (compare == "sequencediagram")
		{
	
		    String tempStartClass = "";
		    String tempEndClass = "";
		    String tempStartMethod = "";
		    String tempActiveMethod = "";
		    list = XmlHelperMethods.getList(diagramData, "/parsed/sequencediagram/classes/entry");
		    for (int a = 0; a < list.getLength(); a++)
		    {
			if (list.item(a).getNodeName() != "#text")
			{
			    pumlCode += "participant " + list.item(a).getTextContent() + "\n";
			}
		    }
		    list = XmlHelperMethods.getList(diagramData, "/parsed/sequencediagram/entrypoint/class");
		    for (int a = 0; a < list.getLength(); a++)
		    {
			if (list.item(a).getNodeName() != "#text")
			{
			    pumlCode += "note over " + list.item(a).getTextContent();
			    tempStartClass = list.item(a).getTextContent();
			}
		    }
		    list = XmlHelperMethods.getList(diagramData, "/parsed/sequencediagram/entrypoint/method");
		    for (int a = 0; a < list.getLength(); a++)
		    {
			if (list.item(a).getNodeName() != "#text")
			{
			    pumlCode += " " + list.item(a).getTextContent() + "\n";
			    tempStartMethod = list.item(a).getTextContent();
			}
		    }
		    list = XmlHelperMethods.getList(diagramData, "/parsed/sequencediagram/methoddefinition");
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
//			    methodList = list.item(a).getChildNodes()
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
	boolean alt = false; // wenn case geöffnet ist, sodass danach else
	for (; i < list.getLength(); i++)
	{
	    if (list.item(i).getNodeName() == "class")
	    {

	    }
	    else if (list.item(i).getNodeName() == "instance") // hier abfangen, wenn nichts direkt definiert, ebene
							       // tiefer!!!
	    {
		// Hier Einfügen
		if (list.item(i).getFirstChild().getNodeName() != "#text") // Test
		{
		    System.out.println(i + ": " + list.item(i).getNodeName() + " - " + list.item(i).getTextContent());
		}

	    }
	    else if (list.item(i).getNodeName() == "method")
	    {
		// Hier Einfügen
//		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getLocalName());
	    }
	    else if (list.item(i).getNodeName() == "type")
	    {
		// Hier Einfügen
//		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getLocalName());
	    }
	    else if (list.item(i).getNodeName() == "case")
	    {
		if (!alt)
		{
		    pumlCode += "alt ";
		    alt = true;
		}
		else
		{
		    pumlCode += "else ";
		}
		// Hier Einfügen

//		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getTextContent());
		helperMethodCall(list.item(i).getChildNodes(), pumlCode, 0, entry); // rekursiver Aufruf tieferer Ebene
	    }
	    else if (list.item(i).getNodeName() == "loop")
	    {
		// Hier Einfügen
//		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getTextContent());
		helperMethodCall(list.item(i).getChildNodes(), pumlCode, 0, entry); // rekursiver Aufruf tieferer Ebene
	    }
	    else if (list.item(i).getNodeName() == "methodcall")
	    {
		// Hier Einfügen
//		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getLocalName());
		helperMethodCall(list.item(i).getChildNodes(), pumlCode, 0, entry); // rekursiver Aufruf tieferer Ebene
	    }
	    else if (list.item(i).getNodeName() == "condition") // case
	    {
		pumlCode += list.item(i).getTextContent() + "\n";
		// Hier Einfügen
//		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getLocalName());
	    }
	}
	return pumlCode;
    }
    /**
     * Speichert den plantUML-Code aus dem String der getPUML Methode in eine Datei
     * 
     * @param pumlCode		String der durch die getPUML Methode erzeugt wird
     * @param filePath		Pfad an den die Datei gespeichert werden soll
     * @throws IOException 
     */
    public void savePUMLtoFile(String pumlCode, String filePath) throws IOException
    {
	    BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filePath)));
	    bw.write(pumlCode);
	    bw.flush();
	    bw.close();
    }

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
