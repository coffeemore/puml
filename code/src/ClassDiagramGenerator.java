
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

			NodeList classList = parsedData.getElementsByTagName("classdefinition");
			for (int i = 0; i < classList.getLength(); i++) {
				Node classdefinitionNode = classList.item(i);
				Element entry = document.createElement("entry");
				classes.appendChild(entry);
				Element Eclassdefinition = (Element) classdefinitionNode;
				entry.appendChild(document
						.createTextNode(Eclassdefinition.getElementsByTagName("name").item(0).getTextContent()));

				if (Eclassdefinition.getElementsByTagName("extends").getLength() > 0) {
					NodeList extendsList = Eclassdefinition.getElementsByTagName("extends");
					Node extendsNode = extendsList.item(0);
					Element Eextends = (Element) extendsNode;

					NodeList entryList = Eextends.getElementsByTagName("entry");
					for (int j = 0; j < entryList.getLength(); j++) {

						Element entry2 = document.createElement("entry");
						extensions.appendChild(entry2);
						Element from = document.createElement("from");
						entry2.appendChild(from);
						from.appendChild(document.createTextNode(
								Eclassdefinition.getElementsByTagName("name").item(0).getTextContent()));
						Element to = document.createElement("to");
						entry2.appendChild(to);
						to.appendChild(document
								.createTextNode(Eextends.getElementsByTagName("entry").item(j).getTextContent()));
					}
				}

				if (Eclassdefinition.getElementsByTagName("implements").getLength() > 0) {
					NodeList implementsList = Eclassdefinition.getElementsByTagName("implements");
					Node implementsNode = implementsList.item(0);
					Element Eimplements = (Element) implementsNode;

					NodeList entryList = Eimplements.getElementsByTagName("entry");
					for (int j = 0; j < entryList.getLength(); j++) {
						Element entry2 = document.createElement("entry");
						implementations.appendChild(entry2);
						Element from = document.createElement("from");
						entry2.appendChild(from);
						from.appendChild(document.createTextNode(
								Eclassdefinition.getElementsByTagName("name").item(0).getTextContent()));
						Element to = document.createElement("to");
						entry2.appendChild(to);
						to.appendChild(document
								.createTextNode(Eimplements.getElementsByTagName("entry").item(j).getTextContent()));

						Element entry3 = document.createElement("entry");
						interfaces.appendChild(entry3);
						entry3.appendChild(document
								.createTextNode(Eimplements.getElementsByTagName("entry").item(j).getTextContent()));

					}
				}

				if (Eclassdefinition.getElementsByTagName("compositions").getLength() > 0) {
					NodeList compositionsList = Eclassdefinition.getElementsByTagName("compositions");
					Node compositionsNode = compositionsList.item(0);
					Element Ecompositions = (Element) compositionsNode;

					NodeList entryList = Ecompositions.getElementsByTagName("entry");
					for (int j = 0; j < entryList.getLength(); j++) {
						Element entry2 = document.createElement("entry");
						compositions.appendChild(entry2);
						Element from = document.createElement("from");
						entry2.appendChild(from);
						from.appendChild(document
								.createTextNode(Ecompositions.getElementsByTagName("entry").item(j).getTextContent()));
						Element to = document.createElement("to");
						entry2.appendChild(to);
						to.appendChild(document.createTextNode(
								Eclassdefinition.getElementsByTagName("name").item(0).getTextContent()));
					}
				}

				if (Eclassdefinition.getElementsByTagName("aggregations").getLength() > 0) {
					NodeList aggregationsList = Eclassdefinition.getElementsByTagName("aggregations");
					Node aggregationsNode = aggregationsList.item(0);
					Element Eaggregation = (Element) aggregationsNode;

					NodeList entryList = Eaggregation.getElementsByTagName("entry");
					for (int j = 0; j < entryList.getLength(); j++) {
						Element entry2 = document.createElement("entry");
						aggregations.appendChild(entry2);
						Element from = document.createElement("from");
						entry2.appendChild(from);
						from.appendChild(document
								.createTextNode(Eaggregation.getElementsByTagName("entry").item(j).getTextContent()));
						Element to = document.createElement("to");
						entry2.appendChild(to);
						to.appendChild(document.createTextNode(
								Eclassdefinition.getElementsByTagName("name").item(0).getTextContent()));
					}
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
		} catch (SAXException e1) { // nur für Einleseversuch von vorgegebenem Beispiel: wird später nicht mehr
									// gebraucht
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
