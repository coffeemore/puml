
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

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
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.DefaultComparisonFormatter;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;
import org.xmlunit.diff.ElementSelectors;
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
     * @param        true = Unterknoten werden nicht gelöscht; false = Unterknoten
     *               werden mit gelöscht
     */
    public void delNode(Element nodeName, boolean keepChildNodes)
    {
    }
    /**
     * Hilfsmethode zum Laden eines XML-Documents fuer diverse Zwecke
     * 
     * @param filename Name/Ort des files
     * @author mariangeissler - Funktion kann ggf. wieder geloescht werden
     * @return Document
     */
    public Document getDocumentFrom(String filepath)
    {
	File file = new File(filepath);
	try
	{
	    DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

	    Document document = documentBuilder.parse(file);

	    // document.getDocumentElement().normalize();

	    return document;
	} catch (SAXException | IOException | ParserConfigurationException e)
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
    public void writeDocumentToConsole(Document xmlDoc)
    {
	TransformerFactory transformerFactory = TransformerFactory.newInstance();
	Transformer transformer;
	try
	{
	    transformer = transformerFactory.newTransformer();

	    // Formatierung der Ausgabe
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    DOMSource domSource = new DOMSource(xmlDoc);

	    // Ausgabe in Console
	    StreamResult console = new StreamResult(System.out);

	    // Schreibe Daten
	    transformer.transform(domSource, console);
	} catch (TransformerException e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * Gibt den Unterbaum des übergebenen Knotens auf der Konsole aus
     * 
     * @param root
     */

    public void listAllNodes(Element root)
    {
	if (root.hasChildNodes())
	{
	    NodeList list = root.getChildNodes();
	    for (int i = 0; i < list.getLength(); i++)
	    {
		Node node = list.item(i);

		if (node.getNodeType() == Node.ELEMENT_NODE)
		{
		    Element e = (Element) node;
		    String m = e.getTagName();
		    System.out.println(m);
		    listAllNodes(e);
		    System.out.println("/" + e.getTagName());
		}
	    }
	}
    }

    public void removeComments(Element root)
    {
	if (root.hasChildNodes())
	{
	    NodeList list = root.getChildNodes();
	    for (int i = 0; i < list.getLength(); i++)
	    {
		Node node = list.item(i);

		if (node.getNodeType() == Node.ELEMENT_NODE)
		{
		    Element e = (Element) node;
		    removeComments(e);
		} else if (node.getNodeType() == Node.COMMENT_NODE)
		{
		    root.removeChild(node);
		}
	    }
	}
    }

    public String removeWhitespace(Document seq) throws TransformerException
    {
	StringWriter sw = new StringWriter();
	TransformerFactory tf = TransformerFactory.newInstance();
	Transformer transformer = tf.newTransformer();
	transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	transformer.transform(new DOMSource(seq), new StreamResult(sw));
	String s = sw.toString();
	String m = s.replaceAll("\\s+\\n", "\n");
	return m;
    }

    /**
     * Funktion zum Suchen aller Knoten mit einem bestimmten Pfad
     * 
     * 
     * @param doc  - xml-Dokument, das durchsucht werden soll
     * @param name - Pfad, nach dem gesucht werden soll
     * @return - NodeList aller gefundenen Knoten
     * @throws XPathExpressionException
     */
    public NodeList getList(Node doc, String path) throws XPathExpressionException 
    {
    	/*// XPath to find empty text nodes.
    	XPathExpression xpathExp = xPathfactory.newXPath().compile("//text()[normalize-space(.) = '']");  
    	NodeList emptyTextNodes = (NodeList) xpathExp.evaluate(doc, XPathConstants.NODESET);
    	// Remove each empty text node from document.
    	for (int i = 0; i < emptyTextNodes.getLength(); i++) {
    	  Node emptyTextNode = emptyTextNodes.item(i);
    	  emptyTextNode.getParentNode().removeChild(emptyTextNode);
    	}*/
    	XPathExpression expr = this.xpath.compile(path);
    	NodeList list = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

	return list;
    }

    /**
     * Funktion zur Suche eines Childnodes mit einem bestimmten Namen; gibt ersten
     * ChildNode mit diesem Namen zurück
     * 
     * @param parent übergebener Knoten
     * @param name   Name des gesuchten Unterknotens
     * @return
     */
    public Node getChildwithName(Node parent, String name)
    {
	NodeList cnodes = parent.getChildNodes();
	for (int i = 0; i < cnodes.getLength(); i++)
	{
	    if (cnodes.item(i).getNodeName().equals(name))
	    {
		return cnodes.item(i);
	    }
	}
	return null;
    }

    /**
     * Funktion zum Abtesten, ob ein Knoten Childnodes mit einem bestimmten Namen
     * hat
     * 
     * @param parent - übergebener Node
     * @param name   - gesuchter ChildNode
     * @return - boolean; true, wenn ChildNode vorhanden, sonst false
     */
    public boolean hasChildwithName(Node parent, String name)
    {
	NodeList cnodes = parent.getChildNodes();
	for (int i = 0; i < cnodes.getLength(); i++)
	{
	    if (cnodes.item(i).getNodeName().equals(name))
	    {
		return true;
	    }
	}
	return false;
    }
    
    public boolean compareXML(Document doc1, Document doc2)
	{
		DefaultComparisonFormatter formatter = new DefaultComparisonFormatter();
		DefaultNodeMatcher nodeMatcher = new DefaultNodeMatcher(ElementSelectors.byNameAndText);
		Diff d = DiffBuilder.compare(doc1).withTest(doc2)
				 .checkForSimilar()//.checkForIdentical()
				 .withNodeMatcher(nodeMatcher)
				 .ignoreWhitespace()
				 .normalizeWhitespace()
				 .withComparisonFormatter(formatter)
				 .ignoreComments()
				 .ignoreElementContentWhitespace()
				 .build();
		Iterable<Difference> diffList = d.getDifferences();
	    Iterator<Difference> iterator = diffList.iterator();
	    while(iterator.hasNext()) 
	    {
	        Difference next = iterator.next();
	        System.out.println("Difference: " + next);
		}
	    if (iterator.hasNext()) 
	    {
	    	return false;
	    }
	    else 
	    {
	    	return true;
	    }
	}
}
