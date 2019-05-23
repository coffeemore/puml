
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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/**
 * 
 * @author Klasse fuer Unterstuetzungsfunktionen zur XML Handhabung
 */
public class XmlHelperMethods
{
	private XPath xpath;
	private XPathFactory xPathfactory;
	
	 /**
     * Konstruktor
     */
    public XmlHelperMethods()
    {
    	this.xPathfactory = XPathFactory.newInstance();
    	this.xpath = xPathfactory.newXPath();
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
     * @author mariangeissler - Funktion kann ggf. wieder geloescht werden
     * @return Document
     */
    public Document getDocumentFrom(String filename)
    {
	 	File file = new File(filename);
		try
		{
	    	DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			
			Document document = documentBuilder.parse(file);
			
			//document.getDocumentElement().normalize();
			
			return document;
		}
		catch (SAXException | IOException | ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * Hilfsmethode zum Ausgeben eines XML-Documents in der Console
     * 
     * @param xmlDoc
     * @author mariangeissler - Funktion kann ggf. wieder geloescht werden
     * @throws TransformerConfigurationException 
     */
    public void writeDocumentToConsole (Document xmlDoc)
    {
    	TransformerFactory transformerFactory = TransformerFactory.newInstance();
    	Transformer transformer;
    	try
    	{
			transformer = transformerFactory.newTransformer();
	    	
			//Formatierung der Ausgabe
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource domSource = new DOMSource(xmlDoc);
	    	
			//Ausgabe in Console
			StreamResult console = new StreamResult(System.out);
	    	
			//Schreibe Daten
			transformer.transform(domSource, console);
		}
    	catch (TransformerException e)
    	{
			e.printStackTrace();
		}
    }

    //Liefert NodeList zurück
    public NodeList getList(Node doc, String path) throws XPathExpressionException 
    {
        	XPathExpression expr = this.xpath.compile(path);
        	NodeList list = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

        return list;
    }
}
