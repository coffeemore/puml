
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author Klasse zur Erzeugung von Klassendiagrammendaten
 */
public class ClassDiagramGenerator 
{
	private XmlHelperMethods xmlHelper = new XmlHelperMethods();
	/**
     * Konstruktor
     */
    public ClassDiagramGenerator()
    {
    	// xmlHelper.readFile();
    }
    
	/**
     * Erstellt den plantUML-Code aus geparstem xmlDocument
     * 
     * @param parsedData xml Eingabe Dokument
     * @return plantUML-Code zur Erzeugung in OutputPUML als xmlDoc
     */
    public Document createDiagram(Document parsedData)
    {
    	DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
    	try 
    	{
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
					
			//Wurzel root namens "parsed" wird unter document angelegt
			Element root = document.createElement("parsed");
			document.appendChild(root);
			
			Element classdiagramm = document.createElement("classdiagramm");
			root.appendChild(classdiagramm);
			
			Element entry = document.createElement("entry");
			
			Element classes = document.createElement("classes");
			classdiagramm.appendChild(classes);
			
			Element interfaces = document.createElement("interfaces");
			classdiagramm.appendChild(interfaces);
			
			Element classrelations = document.createElement("classrelations");
			classdiagramm.appendChild(classrelations);
			
			Element from = document.createElement("from");
			Element to = document.createElement("to");
			
			Element extensions = document.createElement("extensions");
			classrelations.appendChild(extensions);
			
			Element implementations = document.createElement("implementations");
			classrelations.appendChild(implementations);
			
			Element compositions = document.createElement("compositions");
			classrelations.appendChild(compositions);
			
			Element aggregations = document.createElement("aggregations");
			classrelations.appendChild(aggregations);
			
	    	return document;
		}
    	catch (ParserConfigurationException e)
    	{
			e.printStackTrace();
		}
		return null;
    }
    
}
