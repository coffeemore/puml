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
			boolean gotInstances = false;
			
			//CLASSES
			list = helper.getList(list.item(0), "classes/entry/name");
		    for (int a = 0; a < list.getLength(); a++)
		    {
				    pumlCode += "class " + list.item(a).getTextContent();
				    NodeList instanceList = helper.getList(list.item(a).getParentNode(), "instance");
				    NodeList varList = helper.getList(list.item(a).getParentNode(), "var");
				    NodeList methodList = helper.getList(list.item(a).getParentNode(), "methoddefinition");
				    if ((instanceList.getLength() > 0) || (varList.getLength()>0) || (methodList.getLength()>0)) 
				    {
				    	pumlCode += "{\n";
				    	gotInstances = true;
				    }
				    else 
				    {
				    	pumlCode += "\n";
				    }
				    
				    //INSTANCES
				    for (int b=0; b<instanceList.getLength(); b++)
				    {
				    	NodeList tempList = helper.getList(instanceList.item(b), "access");
				    	//System.out.println(tempList.item(0).getTextContent());
					    if (tempList.item(0).getTextContent().equals("private")) 
					    {
					    	pumlCode += "-";
					    }
					    else if (tempList.item(0).getTextContent().equals("public")) 
					    {
					    	pumlCode += "+";
					    }
					    else if (tempList.item(0).getTextContent().equals("protected")) 
					    {
					    	pumlCode += "#";
					    }
					    else if (tempList.item(0).getTextContent().equals("pprivate")) 
					    {
					    	pumlCode += "~";
					    }
					    tempList = helper.getList(instanceList.item(b), "class");
					    pumlCode += tempList.item(0).getTextContent()+" ";
					    tempList = helper.getList(instanceList.item(b), "name");
					    pumlCode += tempList.item(0).getTextContent()+"\n";
				    }
				    
				    //VAR
				    for (int b=0; b<varList.getLength(); b++) 
				    {
				    	NodeList tempList = helper.getList(varList.item(b), "access");
					    if (tempList.item(0).getTextContent().equals("private")) 
					    {
					    	pumlCode += "-";
					    }
					    else if (tempList.item(0).getTextContent().equals("public")) 
					    {
					    	pumlCode += "+";
					    }
					    else if (tempList.item(0).getTextContent().equals("protected")) 
					    {
					    	pumlCode += "#";
					    }
					    else if (tempList.item(0).getTextContent().equals("pprivate")) 
					    {
					    	pumlCode += "~";
					    }
					    tempList = helper.getList(varList.item(b), "type");
					    pumlCode += tempList.item(0).getTextContent()+" ";
					    tempList = helper.getList(varList.item(b), "name");
					    pumlCode += tempList.item(0).getTextContent()+"\n";
				    }

				    //METHODDEFINITION
				    for (int b=0; b<methodList.getLength(); b++) 
				    {
				    	NodeList tempList = helper.getList(methodList.item(b), "access");
					    if (tempList.item(0).getTextContent().equals("private")) 
					    {
					    	pumlCode += "-";
					    }
					    else if (tempList.item(0).getTextContent().equals("public")) 
					    {
					    	pumlCode += "+";
					    }
					    else if (tempList.item(0).getTextContent().equals("protected")) 
					    {
					    	pumlCode += "#";
					    }
					    else if (tempList.item(0).getTextContent().equals("pprivate")) 
					    {
					    	pumlCode += "~";
					    }
					    tempList = helper.getList(methodList.item(b), "type");
					    if(tempList.getLength()>=1) 
					    {
					    	pumlCode += "{"+tempList.item(0).getTextContent()+"}" +" ";
					    }
					    tempList = helper.getList(methodList.item(b), "result");
					    if(tempList.getLength()>=1) 
					    {
					    	pumlCode += tempList.item(0).getTextContent()+" ";
					    }
					    tempList = helper.getList(methodList.item(b), "name");
					    pumlCode += tempList.item(0).getTextContent()+"(";
					    NodeList paramList = helper.getList(methodList.item(b), "parameters/entry");
					    for(int c=0; c<paramList.getLength(); c++)
					    {
					    	NodeList tList = helper.getList(paramList.item(c), "type");
					    	pumlCode+=tList.item(0).getTextContent()+" ";
					    	tList = helper.getList(paramList.item(c), "name");
					    	pumlCode+=tList.item(0).getTextContent();
					    	if(c < 1) 
					    	{
					    		pumlCode += ", ";
					    	}
					    }
					    pumlCode += ")\n";
				    }
				    if(gotInstances) 
				    {
				    	pumlCode+="}\n";
				    	gotInstances=false;
				    }
		    }
		    
		    //INTERFACES
		    //TODO Kann Interface vars haben? (nein: vars teil löschen, ja: weiter fragen, gotInstances-Part?
		    list = helper.getList(list.item(0).getParentNode().getParentNode().getParentNode(), "interfaces/entry/name");
		    for (int a = 0; a < list.getLength(); a++)
		    {
				    pumlCode += "interface " + list.item(a).getTextContent() + "{\n";
				    NodeList instanceList = helper.getList(list.item(a).getParentNode(), "instance");
				    for (int b=0; b<instanceList.getLength(); b++)
				    {
				    	NodeList tempList = helper.getList(instanceList.item(b), "access");
					    if (tempList.item(0).getTextContent().equals("private")) 
					    {
					    	pumlCode += "-";
					    }
					    else if (tempList.item(0).getTextContent().equals("public")) 
					    {
					    	pumlCode += "+";
					    }
					    else if (tempList.item(0).getTextContent().equals("protected")) 
					    {
					    	pumlCode += "#";
					    }
					    else if (tempList.item(0).getTextContent().equals("pprivate")) 
					    {
					    	pumlCode += "~";
					    }
					    tempList = helper.getList(instanceList.item(b), "class");
					    pumlCode += tempList.item(0).getTextContent()+" ";
					    tempList = helper.getList(instanceList.item(b), "name");
					    pumlCode += tempList.item(0).getTextContent()+"\n";
				    }
				    NodeList varList = helper.getList(list.item(a).getParentNode(), "var");
				    for (int b=0; b<varList.getLength(); b++) 
				    {
				    	NodeList tempList = helper.getList(varList.item(b), "access");
					    if (tempList.item(0).getTextContent().equals("private")) 
					    {
					    	pumlCode += "-";
					    }
					    else if (tempList.item(0).getTextContent().equals("public")) 
					    {
					    	pumlCode += "+";
					    }
					    else if (tempList.item(0).getTextContent().equals("protected")) 
					    {
					    	pumlCode += "#";
					    }
					    else if (tempList.item(0).getTextContent().equals("pprivate")) 
					    {
					    	pumlCode += "~";
					    }
					    tempList = helper.getList(varList.item(b), "type");
					    pumlCode += tempList.item(0).getTextContent()+" ";
					    tempList = helper.getList(varList.item(b), "name");
					    pumlCode += tempList.item(0).getTextContent()+"\n";
				    }
				    NodeList methodList = helper.getList(list.item(a).getParentNode(), "methoddefinition");
				    for (int b=0; b<methodList.getLength(); b++) 
				    {
				    	NodeList tempList = helper.getList(methodList.item(b), "access");
					    if (tempList.item(0).getTextContent().equals("private")) 
					    {
					    	pumlCode += "-";
					    }
					    else if (tempList.item(0).getTextContent().equals("public")) 
					    {
					    	pumlCode += "+";
					    }
					    else if (tempList.item(0).getTextContent().equals("protected")) 
					    {
					    	pumlCode += "#";
					    }
					    else if (tempList.item(0).getTextContent().equals("pprivate")) 
					    {
					    	pumlCode += "~";
					    }
					    tempList = helper.getList(methodList.item(b), "type");
					    if(tempList.getLength()>=1) 
					    {
					    	pumlCode += "{"+tempList.item(0).getTextContent()+"}" +" ";
					    }
					    tempList = helper.getList(methodList.item(b), "result");
					    if(tempList.getLength()>=1) 
					    {
					    	pumlCode += tempList.item(0).getTextContent()+" ";
					    }
					    tempList = helper.getList(methodList.item(b), "name");
					    pumlCode += tempList.item(0).getTextContent()+"(";
					    NodeList paramList = helper.getList(methodList.item(b), "parameters/entry");
					    for(int c=0; c<paramList.getLength(); c++)
					    {
					    	NodeList tList = helper.getList(paramList.item(c), "type");
					    	pumlCode+=tList.item(0).getTextContent()+" ";
					    	tList = helper.getList(paramList.item(c), "name");
					    	pumlCode+=tList.item(0).getTextContent();
					    	if(c < 1) 
					    	{
					    		pumlCode += ", ";
					    	}
					    }
					    pumlCode += ")\n";
				    }
				    pumlCode+="}\n";
		    }

		    // EXTENSIONS
		    list = helper.getList(list.item(0).getParentNode().getParentNode().getParentNode(), "classrelations/extensions/entry");
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
			if (!list.item(a).getNodeName().equals("#text"))
			{
			    pumlCode += "participant " + list.item(a).getTextContent() + "\n";
			}
		    }
		    list = helper.getList(diagramData, "/parsed/sequencediagram/entrypoint/class");
		    for (int a = 0; a < list.getLength(); a++)
		    {
			if (!list.item(a).getNodeName().equals("#text") )
			{
			    pumlCode += "note over " + list.item(a).getTextContent() + ":";
			    tempStartClass = list.item(a).getTextContent();
			}
		    }
		    list = helper.getList(diagramData, "/parsed/sequencediagram/entrypoint/method");
		    for (int a = 0; a < list.getLength(); a++)
		    {
			if (!list.item(a).getNodeName().equals("#text"))
			{
			    pumlCode += " " + list.item(a).getTextContent() + "\n";
			    pumlCode += "activate " + tempStartClass + "\n";
			    tempStartMethod = list.item(a).getTextContent();
			}
		    }
		    // Einsetzen : [name=" + tempStartMethod + "]
		    list = helper.getList(diagramData, "/parsed/sequencediagram/methoddefinition[name=\"" + tempStartMethod + "\"]"); //an Position der entry Methoddefinition
		    pumlCode += helperMethodCall(list.item(0), tempStartClass);

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
	     pumlCode += "activate " + startClass + "\n";
	 }
	 firstMethodCall = false;
	 try
	{
		 while(nextNode.getNodeName() != null) //Versuch
		 {
		     if(nextNode.getNodeName().equals("name"))
		     {
			 nextNode = helper.getList(nextNode,"following-sibling::*").item(0);
		     }
		     //pumlCode += "activate " + startClass + "\n"; //benötigt
			 
		     if(nextNode.getNodeName().equals("alternative"))
		     {
			 pumlCode += helperAlternativeCall(nextNode, startClass);
			 //nextNode = nextNode.getNextSibling();
			    
		     }
		     if(nextNode.getNodeName().equals("methodcall"))
		     {
			 Node methodNode = helper.getList(nextNode, "child::*").item(0);
			 pumlCode += helperMethodCallHandler(startClass, methodNode);
		     }
		     nextNode = helper.getList(nextNode,"following-sibling::*").item(0);
		 }   
	}
	catch (Exception e)
	{
	  //No more Items
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
	    Node nextNode = helper.getList(cases.item(i), "child::*").item(0);
    	    try
    	    {
        	    while (nextNode.getNodeName() != null)
        	    {        		
                	    if(nextNode.getNodeName().equals("condition"))
                	    {
                		//nextNode = nextNode.getNextSibling();
                	    }
                	    if(nextNode.getNodeName().equals("methodcall"))
                	    {
                		Node aufrufNode = helper.getList(nextNode, "child::*").item(0);
                		pumlCode += helperMethodCallHandler(startClass, aufrufNode);
                	    }
                	    if(nextNode.getNodeName().equals("loop"))
                	    {
                		pumlCode += helperLoopCall(nextNode, startClass);
                	    }
                	    nextNode = helper.getList(nextNode,"following-sibling::*").item(0);
                		
        	    }
	    	}
		catch (Exception e)
		{
		  //No more Items
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
        	    if (nextNode.getNodeName().equals("instance"))
        	    {
        		inst = nextNode.getTextContent();
        
        	    }
        	    else if(nextNode.getNodeName().equals("class"))
        	    {
        		toClass = nextNode.getTextContent();
        
        	    }
        	    else if(nextNode.getNodeName().equals("method"))
        	    {
        		method = nextNode.getTextContent();
        
        	    }
        	    else if(nextNode.getNodeName().equals("type"))
        	    {
        		
        		type = nextNode.getTextContent();
        	    }
        	    
        	    nextNode = helper.getList(nextNode,"following-sibling::*").item(0);
        	}
	}
	catch (Exception e)
	{
	    //No more Items
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
		PUMLgenerator.logger.getLog().warning(e + " :: " + method + " Node not Found");
	    }
	}
	else if(type.equals("unknown"))
	{
	    pumlCode += startClass + " ->x]" + toClass + ": " + method + inst + "\n";

	}
	else if(type.equals("recursive"))
	{
	    pumlCode += startClass + " ->o " + startClass + ": " + method + inst + "\n";
	    //pumlCode += "activate " + startClass + "\n";
	    //pumlCode += "deactivate " + startClass + "\n";
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
        	if(nextNode.getNodeName().equals("condition"))
        	{
        	    pumlCode += nextNode.getTextContent() + "\n";
        	    //nextNode = nextNode.getNextSibling();
        	}if(nextNode.getNodeName().equals("methodcall"))
        	{
        	    nextNode = helper.getList(nextNode, "child::*").item(0);
        	    pumlCode += helperMethodCallHandler(startClass, nextNode);
        	}
        	nextNode = helper.getList(nextNode,"following-sibling::*").item(0);
	    }
	
	}
	catch(Exception e)
	{
	  //No more Items
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
     * !VERALTET!
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
