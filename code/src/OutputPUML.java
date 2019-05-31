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
import org.w3c.dom.Node;
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
    XmlHelperMethods helper = new XmlHelperMethods();

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
			list = helper.getList(diagramData, "/parsed/classdiagramm/classes/entry");
		    for (int a = 0; a < list.getLength(); a++)
		    {
				if (list.item(a).getNodeName() != "#text")
				{
				    pumlCode += "class " + list.item(a).getTextContent() + "\n";
				}
		    }
		    list = helper.getList(diagramData, "/parsed/classdiagramm/interfaces/entry");
		    for (int a = 0; a < list.getLength(); a++)
		    {
				if (list.item(a).getNodeName() != "#text")
				{
				    pumlCode += "interface " + list.item(a).getTextContent() + "\n";
				}
		    }
	
		    // EXTENSIONS
		    list = helper.getList(diagramData, "/parsed/classdiagramm/classrelations/extensions/entry");
		    for (int a = 0; a < list.getLength(); a++)
		    {
				list = helper.getList(diagramData, "/parsed/classdiagramm/classrelations/extensions/entry/to");
				if (list.item(a).getNodeName() != "#text")
				{
				    pumlCode += list.item(a).getTextContent() + " <|-- ";
		
				}
				list = helper.getList(diagramData, "/parsed/classdiagramm/classrelations/extensions/entry/from");
				if (list.item(a).getNodeName() != "#text")
				{
				    pumlCode += list.item(a).getTextContent() + "\n";
				}
		    }
	
		    // IMPLEMENTATIONS
		    list = helper.getList(diagramData, "/parsed/classdiagramm/classrelations/implementations/entry");
		    for (int a = 0; a < list.getLength(); a++)
		    {
				list = helper.getList(diagramData, "/parsed/classdiagramm/classrelations/implementations/entry/to");
				if (list.item(a).getNodeName() != "#text")
				{
				    pumlCode += list.item(a).getTextContent() + " <|-- ";
		
				}
				list = helper.getList(diagramData, "/parsed/classdiagramm/classrelations/implementations/entry/from");
				if (list.item(a).getNodeName() != "#text")
				{
				    pumlCode += list.item(a).getTextContent() + "\n";
				}
		    }
	
		    // COMPOSITIONS
		    list = helper.getList(diagramData, "/parsed/classdiagramm/classrelations/compositions/entry");
		    for (int a = 0; a < list.getLength(); a++)
		    {
				list = helper.getList(diagramData, "/parsed/classdiagramm/classrelations/compositions/entry/to");
				if (list.item(a).getNodeName() != "#text")
				{
				    pumlCode += list.item(a).getTextContent() + " *-- ";
		
				}
				list = helper.getList(diagramData, "/parsed/classdiagramm/classrelations/compositions/entry/from");
				if (list.item(a).getNodeName() != "#text")
				{
				    pumlCode += list.item(a).getTextContent() + "\n";
				}
		    }
	
		    // AGGREGATIONS
		    list = helper.getList(diagramData, "/parsed/classdiagramm/classrelations/aggregations/entry");
		    for (int a = 0; a < list.getLength(); a++)
		    {
				list = helper.getList(diagramData, "/parsed/classdiagramm/classrelations/aggregations/entry/to");
				if (list.item(a).getNodeName() != "#text")
				{
				    pumlCode += list.item(a).getTextContent() + " o-- ";
		
				}
				list = helper.getList(diagramData, "/parsed/classdiagramm/classrelations/aggregations/entry/from");
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
		    list = helper.getList(diagramData, "/parsed/sequencediagram/classes/entry");
		    for (int a = 0; a < list.getLength(); a++)
		    {
			if (list.item(a).getNodeName() != "#text")
			{
			    pumlCode += "participant " + list.item(a).getTextContent() + "\n";
			}
		    }
		    list = helper.getList(diagramData, "/parsed/sequencediagram/entrypoint/class");
		    for (int a = 0; a < list.getLength(); a++)
		    {
			if (list.item(a).getNodeName() != "#text")
			{
			    pumlCode += "note over " + list.item(a).getTextContent() + ":";
			    tempStartClass = list.item(a).getTextContent();
			}
		    }
		    list = helper.getList(diagramData, "/parsed/sequencediagram/entrypoint/method");
		    for (int a = 0; a < list.getLength(); a++)
		    {
			if (list.item(a).getNodeName() != "#text")
			{
			    pumlCode += " " + list.item(a).getTextContent() + "\n";
			    tempStartMethod = list.item(a).getTextContent();
			}
		    }
		    // Einsetzen : [name=" + tempStartMethod + "]
		    list = helper.getList(diagramData, "/parsed/sequencediagram/methoddefinition[name=\"" + tempStartMethod + "\"]"); //an Position der entry Methoddefinition
		    pumlCode += helperMethodCall(list.item(0), tempStartClass);
/*		   
		    for (int a = 0; a < list.getLength(); a++)
		    {
			if (list.item(a).getNodeName() != "#text")
			{
			    System.out.println("get in there" + num);
			    //ruft die helperMethod mit der entry Methoddefinition auf
			    pumlCode += helperMethodCall(diagramData, "/parsed/sequencediagram/methoddefinition[" + num + "]", tempStartClass) + "\n";
			    pumlCode += "deactivate " + tempStartClass + "\n";
			}
		    }
*/
		}
		else
		{
		    logger.getLog().warning("XML-Diagramm fehlerhaft");
		}
		// return "Error with Loop";
		pumlCode += "@enduml";
		return pumlCode;
	    }

    //for Methoddefinitions
    private String helperMethodCall(Node methodefNode, String startClass) throws XPathExpressionException
    {
	 String pumlCode = "";
	 NodeList methodNameList = helper.getList(methodefNode, "name");
	 String methodName =  methodNameList.item(0).getTextContent();
	 System.out.println(methodName);
	 Node nextNode = methodefNode.getNextSibling();
	 System.out.println(nextNode.getNextSibling().getNodeName()); //TODO fortfahren
	 if(nextNode.getNodeName()=="name")
	 {
	     nextNode = nextNode.getNextSibling();
	 }
	 
	 pumlCode += "activate " + startClass + "\n";
	 
	 if(nextNode.getNodeName()=="alternative")
	 {
	     pumlCode += helperAlternativeCall(nextNode, startClass);
	    
	 }
	 
	 /*
	 String pumlCode = "";
	    String templistPath = listPath + "/name"; //an position name in der Methoddefinition
		list = XmlHelperMethods.getList(diagramData, listPath).item(0).getChildNodes();
		for (int a = 0; a < list.getLength(); a++)
		{
		    if (list.item(a).getNodeName() == "name")
		    {
			System.out.println("-- " + list.item(a).getNodeName());
			pumlCode += "activate " + startClass + "\n";
		    }
		}
		templistPath = listPath + "/alternative";
		list = XmlHelperMethods.getList(diagramData, templistPath);
		for (int a = 0; a < list.getLength(); a++)
		{
		    if (list.item(a).getNodeName() != "#text")
		    {
			pumlCode += helperCaseCall(diagramData, templistPath, startClass, false);
		    }
		}

	return pumlCode;
	*/
	 return pumlCode;
    }
    
    //for Cases
    private String helperAlternativeCall(Node alternativeNode, String startClass) throws XPathExpressionException
    {
	String pumlCode = "";
	NodeList cases = helper.getList(alternativeNode, "case");
	boolean first = true;
	for(int i=0; i<cases.getLength(); i++)
	{
	    String caseName = helper.getList(cases.item(i), "condition").item(0).getTextContent();
	    if(first)
	    {
		pumlCode += "alt " + caseName;
		first = false;
	    }
	    else
	    {
		pumlCode += "else " + caseName;
	    }
	    Node nextNode = cases.item(i).getFirstChild();
	    if(nextNode.getNodeName()=="conditition")
	    {
		nextNode = nextNode.getNextSibling();
	    }
	    if(nextNode.getNodeName() == "methodcall")
	    {
		if(nextNode.getFirstChild().getNodeName() == "method")
		{
		    helperMethodCall(nextNode, startClass);
		}
		else if(nextNode.getFirstChild().getNodeName() == "class")
		{
		    NodeList methodCalls = helper.getList(nextNode, "class");
		}
		
		
	    }
	}	
	return pumlCode;
    }
    
    //for loops
    private String helperLoopCall(Document diagramData, String listPath, String startClass) throws XPathExpressionException
    {
	String pumlCode = "";
	listPath += "/case"; //an position name in der Methoddefinition
	list = helper.getList(diagramData, listPath);
	for(int i = 0; i < list.getLength(); i++)
	{
	    System.out.println("-- " + i);
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
