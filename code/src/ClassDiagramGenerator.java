
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author Klasse zur Erzeugung von Klassendiagrammendaten
 */
public class ClassDiagramGenerator {
	private XmlHelperMethods xmlHelper = new XmlHelperMethods();

	/**
	 * Konstruktor
	 * 
	 * @throws ParserConfigurationException
	 * @throws TransformerConfigurationException
	 */
	public ClassDiagramGenerator() {
		System.out.println("TestXML erstellen");
		createDiagram(null);
	}

	/**
	 * Erstellt den plantUML-Code aus geparstem xmlDocument
	 * 
	 * @param parsedData xml Eingabe Dokument
	 * @return plantUML-Code zur Erzeugung in OutputPUML als xmlDoc
	 */
	public Document createDiagram(Document parsedData) {
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		try {
			// Versuch auslesen verschiedener Informationen aus parsedDataBeispiel
			File inputFile = new File("bin/xmlSpecifications/parsedData.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			parsedData = dBuilder.parse(inputFile); // nur für Testzwecke: muss noch entfernt werden
			// System.out.println("parsedData.getDocumentElement().getNodeName()");

			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();

			// Wurzel root namens "parsed" wird unter document angelegt
			Element root = document.createElement("parsed");
			document.appendChild(root);

			Element classdiagramm = document.createElement("classdiagramm");
			root.appendChild(classdiagramm);

			Element classes = document.createElement("classes");
			classdiagramm.appendChild(classes);

			Element interfaces = document.createElement("interfaces");
			classdiagramm.appendChild(interfaces);

			Element classrelations = document.createElement("classrelations");
			classdiagramm.appendChild(classrelations);

			Element extensions = document.createElement("extensions");
			classrelations.appendChild(extensions);

			Element implementations = document.createElement("implementations");
			classrelations.appendChild(implementations);

			Element compositions = document.createElement("compositions");
			classrelations.appendChild(compositions);

			Element aggregations = document.createElement("aggregations");
			classrelations.appendChild(aggregations);
			
			Element from = document.createElement("from");
			Element to = document.createElement("to");

			NodeList nList = parsedData.getElementsByTagName("classdefinition");
			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
				Element entry = document.createElement("entry");
				classes.appendChild(entry);
				Element eElement = (Element) nNode;
				entry.appendChild(document.createTextNode(eElement.getElementsByTagName("name").item(0).getTextContent()));				

				NodeList exList = eElement.getElementsByTagName("extends");
				for (int j = 0; j < exList.getLength(); j++) {
					
					Node exNode = exList.item(j);
					Element exElement = (Element) exNode;
					
					System.out.println(exElement.getElementsByTagName("entry").item(0).getTextContent());
					Element entry2 = document.createElement("entry");
					extensions.appendChild(entry2);
					entry2.appendChild(from);
					from.appendChild(document.createTextNode(eElement.getElementsByTagName("name").item(0).getTextContent()));
					entry2.appendChild(to);
					to.appendChild(document.createTextNode(exElement.getElementsByTagName("entry").item(0).getTextContent()));	}
			}

			nList = parsedData.getElementsByTagName("interfacedefinition"); //Einlesen der Inerfaces noch nicht korrekt
			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element entry = document.createElement("name");
					interfaces.appendChild(entry);
					Element eElement = (Element) nNode;
					entry.appendChild(document.createTextNode(eElement.getElementsByTagName("name").item(0).getTextContent()));
				}
			}

			try {
				xmlHelper.writeDocumentToConsole(document);
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return document;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e1) { // nur für Einleseversuch von vorgegebenem Beispiel: wird später nicht mehr gebraucht
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
		// xmlHelper.writeDocumentToConsole(document);
	}
}
