import java.io.File;
import java.io.IOException;
import java.io.StringReader;
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
import org.xml.sax.InputSource;
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
     * 
     * @param xmlDoc lesende Datei
     */
    public String xmlDocToString(Document xmlDoc)
    {
	return new String();
    }

    /**
     * loescht einzelnen Knoten aus xml Datei
     * 
     * @param Knoten eines Elements
     * @param        true = Unterknoten werden nicht gelöscht; false = Unterknoten
     *               werden mit gelöscht
     * @throws XPathExpressionException
     */
    public void delNode(Node nodeName, boolean keepChildNodes) throws XPathExpressionException
    {
	Node parent = getList(nodeName, "..").item(0);
	if (keepChildNodes)
	{
	    NodeList childNodes = getList(nodeName, "child::*");
	    for (int i = 0; i < childNodes.getLength(); i++)
	    {
		parent.appendChild(childNodes.item(i).cloneNode(true));

	    }
	    parent.removeChild(nodeName);

	} else
	{
	    parent.removeChild(nodeName);
	}
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
	    PUMLgenerator.logger.getLog().warning("@XmlHelperMethods/getDocumentFrom: "+e.toString());
	}
	return null;
    }

    /**
     * Hilfsmethode zum Erstellen eines XML-Documents
     * 
     * @return Document
     */
    public Document createDocument()
    {
	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder docBuilder;
	try
	{
	    docBuilder = docFactory.newDocumentBuilder();

	    Document document = docBuilder.newDocument();
	    return document;
	} catch (ParserConfigurationException e)
	{
		PUMLgenerator.logger.getLog().warning("@XmlHelperMethods/createDocument"+e.toString());
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
	    PUMLgenerator.logger.getLog().warning("@XmlHelperMethods/writeDocumentToConsole: "+e.toString());
	}
    }

    /**
     * Erstellt eine XML-Datei
     * 
     * @param doc - Dokument, das in eine XML-Datei geschrieben werden soll
     * @throws IOException
     * @throws TransformerException
     */
    public void writeToFile(Document doc) throws IOException, TransformerException
    {
	File file = new File("../code/testfolder/tempData/TestFile.xml");
	file.createNewFile();

	TransformerFactory tFactory = TransformerFactory.newInstance();
	Transformer transformer = tFactory.newTransformer();
	transformer.setOutputProperty(OutputKeys.INDENT, "yes");

	DOMSource source = new DOMSource(doc);
	StreamResult result = new StreamResult(file);
	transformer.transform(source, result);
    }

    /**
     * Gibt den Unterbaum des übergebenen Knotens auf der Konsole aus
     * 
     * @param root
     * @throws XPathExpressionException
     */
    public void listAllNodes(Element root) throws XPathExpressionException
    {
	if (root.hasChildNodes())
	{
	    NodeList list = getList(root, "child::*");
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

    /**
     * Entfernt Kommentare unterhalb eines angegebenen Knotens
     * 
     * @param root - Knoten, unter dem Kommentare entfernt werden sollen
     * @throws XPathExpressionException
     */
    public void removeComments(Element root) throws XPathExpressionException
    {
	if (root.hasChildNodes())
	{
	    NodeList list = getList(root, "child::*");
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

    /**
     * Entfernt unnötigen Whitespace in einem Dokument
     * 
     * @param seq - Dokument, in dem unnötiger Whitespace entfernt werden soll
     * @return - Dokument ohne unnötigen Whitespace
     * @throws TransformerException
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public Document removeWhitespace(Document seq)
	    throws TransformerException, SAXException, IOException, ParserConfigurationException
    {
	StringWriter sw = new StringWriter();
	TransformerFactory tf = TransformerFactory.newInstance();
	Transformer transformer = tf.newTransformer();
	transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	transformer.transform(new DOMSource(seq), new StreamResult(sw));
	String s = sw.toString();
	String m = s.replaceAll("\\s+\\n", "\n");
	DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	seq = docBuilder.parse(new InputSource(new StringReader(m)));

	return seq;
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
	/*
	 * // XPath to find empty text nodes. XPathExpression xpathExp =
	 * xPathfactory.newXPath().compile("//text()[normalize-space(.) = '']");
	 * NodeList emptyTextNodes = (NodeList) xpathExp.evaluate(doc,
	 * XPathConstants.NODESET); // Remove each empty text node from document. for
	 * (int i = 0; i < emptyTextNodes.getLength(); i++) { Node emptyTextNode =
	 * emptyTextNodes.item(i);
	 * emptyTextNode.getParentNode().removeChild(emptyTextNode); }
	 */
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
     * @throws XPathExpressionException
     */
    public Node getChildwithName(Node parent, String name) throws XPathExpressionException
    {
	NodeList cnodes = getList(parent, "child::*");
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
     * @throws XPathExpressionException
     */
    public boolean hasChildwithName(Node parent, String name) throws XPathExpressionException
    {
	NodeList cnodes = getList(parent, "child::*");
	for (int i = 0; i < cnodes.getLength(); i++)
	{
	    if (cnodes.item(i).getNodeName().equals(name))
	    {
		return true;
	    }
	}
	return false;
    }

    /**
     * Funktion zum Vergleichen von XML Dateien über XMLUnit
     * 
     * @param doc1 - Source Doc
     * @param doc2 - Zu testendes Doc
     * @return - boolean; true, wenn XML gleich, sonst false
     */

    public boolean compareXML(Document doc1, Document doc2)
    {
	DefaultComparisonFormatter formatter = new DefaultComparisonFormatter();
	DefaultNodeMatcher nodeMatcher = new DefaultNodeMatcher(ElementSelectors.byNameAndText);
	Diff d = DiffBuilder.compare(doc1).withTest(doc2).checkForSimilar()// .checkForIdentical()
		.withNodeMatcher(nodeMatcher).ignoreWhitespace().normalizeWhitespace()
		.withComparisonFormatter(formatter).ignoreComments().ignoreElementContentWhitespace().build();
	Iterable<Difference> diffList = d.getDifferences();
	Iterator<Difference> iterator = diffList.iterator();
	while (iterator.hasNext())
	{
	    Difference next = iterator.next();
	    System.out.println("Difference: " + next);
	}
	iterator = diffList.iterator();
	if (iterator.hasNext())
	{
	    return false;
	} else
	{
	    return true;
	}
    }
}
