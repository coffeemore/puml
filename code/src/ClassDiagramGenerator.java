
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

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
	 * @throws ParserConfigurationException 
	 * @throws TransformerConfigurationException 
     */
    public ClassDiagramGenerator()
    {
    	
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
		
			//Wurzel anlegen
			Element root = document.createElement("parsed");
			
	    	return document;
		}
    	catch (ParserConfigurationException e)
    	{
			e.printStackTrace();
		}
		return null;
		//xmlHelper.writeDocumentToConsole(document);
    }
    
}
