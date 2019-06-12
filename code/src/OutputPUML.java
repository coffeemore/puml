import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
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
    PUMLgenerator puml = new PUMLgenerator();
    XmlHelperMethods helper = new XmlHelperMethods();
    boolean firstMethodCall = true;
    

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
     * @throws IOException 
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
			list = helper.getList(list.item(0), "classes/entry");
		    for (int a = 0; a < list.getLength(); a++)
		    {
				    pumlCode += "class " + list.item(a).getTextContent() + "\n";
		    }
		    list = helper.getList(list.item(0).getParentNode().getParentNode(), "interfaces/entry");
		    for (int a = 0; a < list.getLength(); a++)
		    {
			    pumlCode += "interface " + list.item(a).getTextContent() + "\n";
		    }
	
		    // EXTENSIONS
		    list = helper.getList(list.item(0).getParentNode().getParentNode(), "classrelations/extensions/entry");
		    for (int a = 0; a < list.getLength(); a++)
		    {
		    	NodeList tempList;
		    	tempList = helper.getList(list.item(a), "to");
		    	pumlCode += tempList.item(0).getTextContent() + " <|-- ";
			    tempList = helper.getList(list.item(a), "from");
		    	pumlCode += tempList.item(0).getTextContent() + "\n";
		    }
	
		    // IMPLEMENTATIONS
		    list = helper.getList(list.item(0).getParentNode().getParentNode(), "implementations/entry");
		    for (int a = 0; a < list.getLength(); a++)
		    {
		    	NodeList tempList;
		    	tempList = helper.getList(list.item(a), "to");
		    	pumlCode += tempList.item(0).getTextContent() + " <|-- ";
			    tempList = helper.getList(list.item(a), "from");
		    	pumlCode += tempList.item(0).getTextContent() + "\n";
		    }
	
		    // COMPOSITIONS
		    list = helper.getList(list.item(0).getParentNode().getParentNode(), "compositions/entry");
		    for (int a = 0; a < list.getLength(); a++)
		    {
		    	NodeList tempList;
		    	tempList = helper.getList(list.item(a), "to");
		    	pumlCode += tempList.item(0).getTextContent() + " *-- ";
				tempList = helper.getList(list.item(a), "from");
			    pumlCode += tempList.item(0).getTextContent() + "\n";
		    }
	
		    // AGGREGATIONS
		    list = helper.getList(list.item(0).getParentNode().getParentNode(), "aggregations/entry");
		    for (int a = 0; a < list.getLength(); a++)
		    {
		    	NodeList tempList;
		    	tempList = helper.getList(list.item(a), "to");
				pumlCode += tempList.item(0).getTextContent() + " o-- ";
				tempList = helper.getList(list.item(a), "from");
			    pumlCode += tempList.item(0).getTextContent() + "\n";
		    }
    	}
	
		else if (compare == "sequencediagram")
		{
	
		    String tempStartClass = "";
		    String tempStartMethod = "";
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
		    pumlCode += helperMethodCall(list.item(0), tempStartClass); //TODO Versuch

		}
		else
		{
		    PUMLgenerator.logger.getLog().warning("XML-Diagramm fehlerhaft");
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
	 Node nextNode = methodNameList.item(0);
	 
	 //Abfrage ob die Methode das erste Mal aufgerufen wird
	 if (!firstMethodCall)
	 {
	     pumlCode = startClass + " -> " + startClass + ": " + methodName + "\n";
	 }
	 firstMethodCall = false;
	 try
	{
		 while(nextNode.getNodeName() != null) //Versuch
		 {
		     if(nextNode.getNodeName()=="name")
		     {
			 nextNode = nextNode.getNextSibling();
		     }
		     pumlCode += "activate " + startClass + "\n";
			 
		     if(nextNode.getNodeName()=="alternative")
		     {
			 pumlCode += helperAlternativeCall(nextNode, startClass);
			 //nextNode = nextNode.getNextSibling();
			    
		     }
		     if(nextNode.getNodeName() == "methodcall")
		     {
			 Node methodNode = nextNode.getFirstChild();
			 pumlCode += helperMethodCallHandler(startClass, methodNode);
		     }
		     nextNode = nextNode.getNextSibling();
		 }   
	}
	catch (Exception e)
	{
	    // TODO: handle exception
	}
	 

	 
	 
	 pumlCode += "deactivate " + startClass + "\n";
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
		pumlCode += "alt " + caseName + "\n";
		first = false;
	    }
	    else
	    {
		pumlCode += "else " + caseName + "\n";
	    }
	    Node nextNode = cases.item(i).getFirstChild();
    	    try
    	    {
        	    while (nextNode.getNodeName() != null)
        	    {        		
                	    if(nextNode.getNodeName()=="condition")
                	    {
                		//nextNode = nextNode.getNextSibling();
                	    }
                	    if(nextNode.getNodeName() == "methodcall")
                	    {
                		Node aufrufNode = nextNode.getFirstChild();
                		pumlCode += helperMethodCallHandler(startClass, aufrufNode);
                	    }
                	    if(nextNode.getNodeName()== "loop")
                	    {
                		pumlCode += helperLoopCall(nextNode, startClass);
                		//TODO Weiterführend ???
                	    }
                	    nextNode = nextNode.getNextSibling();
                		
        	    }
	    	}
		catch (Exception e)
		{
		// TODO: handle exception
		}
    	    
	    
	}
	pumlCode += "end\n";
	
	
	return pumlCode;
    }

    private String helperMethodCallHandler(String startClass, Node methodCallNode)
    {
	String pumlCode ="";
	String inst ="";
	String method ="";
	String toClass ="";
	String type ="";
	Node nextNode = methodCallNode;
	try
	{
        	while (nextNode.getNodeName() != null)
        	{
        	    if (nextNode.getNodeName() == "instance")
        	    {
        		inst = nextNode.getTextContent();
        
        	    }
        	    else if(nextNode.getNodeName() == "class")
        	    {
        		toClass = nextNode.getTextContent();
        
        	    }
        	    else if(nextNode.getNodeName() == "method")
        	    {
        		method = nextNode.getTextContent();
        
        	    }
        	    else if(nextNode.getNodeName() == "type")
        	    {
        		
        		type = nextNode.getTextContent();
        	    }
        	    
        	    nextNode = nextNode.getNextSibling();
        	}
	}
	catch (Exception e)
	{
	// TODO: handle exception
	}
	
	inst = (inst != "") ? " (" + inst + ")" : inst;
	
	/*
	 * Abfragen anhand der gespeicherten Daten über die Strings werden behandelt
	 * 1.Fall
	 * 	Klasse und Methode sind beschrieben:
	 * 		Strings werden geschrieben
	 * 2.Fall
	 * 	Klasse ist nicht beschrieben, und kein unknown, recursive oder handled als type
	 * 		helperMethodCall wird aufgerufen
	 * 3.Fall/4.Fall/5.Fall 
	 * 	unknown/handled/recursive:
	 * 		Wird gemäß der Vorgaben in den pumlCode geschrieben	 
	 */
	if (!toClass.equals("") && !method.equals(""))
	{
		pumlCode += startClass + " -> " + toClass + ": " + method + inst + "\n";
		pumlCode += "activate " + toClass + "\n";
		pumlCode += toClass + " --> " + startClass + "\n";
		pumlCode += "deactivate " + toClass + "\n";
	}
	else if(toClass.equals("") && !type.equals("unknown") && !type.equals("recursive") && !type.equals("handled"))
	{
	    try {
	    NodeList callList = helper.getList(methodCallNode, "//methoddefinition[name=\"" + method + "\"]");
	    pumlCode += startClass + " -> " + startClass + ": " + method + "\n";
	    pumlCode = helperMethodCall(callList.item(0), startClass);
	    }
	    catch(Exception e)
	    {
		
	    }
	}
	else if(type.equals("unknown"))
	{
	    pumlCode += startClass + " ->x]" + toClass + ": " + method + inst + "\n";

	}
	else if(type.equals("recursive"))
	{
	    pumlCode += startClass + " ->o " + startClass + ": " + method + inst + "\n";
	    pumlCode += "activate " + startClass + "\n";
	    pumlCode += "deactivate " + startClass + "\n";
	}
	else if(type.equals("handled"))
	{
	    pumlCode += startClass + " [#0000FF]->> " + startClass + ": " + method + inst + "\n";
	    pumlCode += "activate " + startClass + "\n";
	    pumlCode += "deactivate " + startClass + "\n";
	}
	
	return pumlCode;
    }
    
    //for loops
    private String helperLoopCall(Node loopNode, String startClass) throws XPathExpressionException
    {
	String pumlCode = "loop ";
	NodeList loopList = helper.getList(loopNode, "condition");
	Node nextNode = loopList.item(0);
	
	try
	{
	    while (nextNode.getNodeName() != null)
	    {
        	if(nextNode.getNodeName()=="condition")
        	{
        	    pumlCode += nextNode.getTextContent() + "\n";
        	    //nextNode = nextNode.getNextSibling();
        	}if(nextNode.getNodeName() == "methodcall")
        	{
        	    nextNode = nextNode.getFirstChild();
        	    pumlCode += helperMethodCallHandler(startClass, nextNode);
        	}
        	nextNode = nextNode.getNextSibling();
	    }
	
	}
	catch(Exception e)
	{
	    
	}
	
	pumlCode += "end\n";
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
