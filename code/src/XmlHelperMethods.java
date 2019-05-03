
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
/**
 * 
 * @author Klasse fuer Unterstuetzungsfunktionen zur XML Handhabung
 */
public class XmlHelperMethods
{
	 /**
     * Konstruktor
     */
    public XmlHelperMethods()
    {
	
    }
    
    /**
     * Konvertiert xml Datei in String
     * @param xmlDoc lesende Datei
     */
    public String xmlDocToString(Document xmlDoc)
    {
    	return new String();
    }
    
    /**
     * loescht einzelnen Knoten aus xml Datei
     * @param Knoten eines Elements
     * @param Wahrheitswert, loeschen/behalten des Knoten
     */
    public void delNode(Element nodeName, boolean keepNodeName)
    {
  
    }
    
    /**
     * Hilfsmethode zum Laden eines XML-Documents fuer diverse Zwecke
     * 
     * @param filename Name/Ort des files
     * @return Document
     * @throws ParserConfigurationException 
     * @throws IOException 
     * @throws SAXException 
     */
    public Document getDocumentFrom(String filename) throws ParserConfigurationException, SAXException, IOException
    {
    	File file = new File(filename);
    	DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(file);
		
    	return document;
    }
    
    /**
     * Hilfsmethode zum Ausgeben eines XML-Documents in der Console
     * 
     * @param xmlDoc 
     * @throws TransformerConfigurationException 
     */
    public void writeDocumentToConsole (Document xmlDoc) throws TransformerConfigurationException
    {
    	TransformerFactory transformerFactory = TransformerFactory.newInstance();
    	Transformer transformer = transformerFactory.newTransformer();
    	
    	//Formatierung der Ausgabe
    	transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    	DOMSource domSource = new DOMSource(xmlDoc);
    	
    	//Ausgabe in Console
    	StreamResult console = new StreamResult(System.out);
    	
    	//Schreibe Daten
    	try 
    	{
			transformer.transform(domSource, console);
		}
    	catch (TransformerException e)
    	{
			e.printStackTrace();
		}
    }
}
