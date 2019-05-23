
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
					
			//Wurzel root namens "parsed" wird unter document angelegt
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

			//Knoten auflisten 
			NodeList classList = parsedData.getElementsByTagName("classdefinition");
			for (int i = 0; i < classList.getLength(); i++)
			{
				Node classdefinitionNode = classList.item(i);
				Element entry = document.createElement("entry");
				classes.appendChild(entry);
				Element elClassdef = (Element) classdefinitionNode;
				//Erzeugt "Entry"-Tag mit Classennamen als Texteintrag
				entry.appendChild(document
						.createTextNode(elClassdef.getElementsByTagName("name").item(0).getTextContent()));
				//Pruefen auf Vererbungsstruktur
				if (elClassdef.getElementsByTagName("extends").getLength() > 0)
				{
					//Knoten fuer Vererbung eintragen
					NodeList extendsList = elClassdef.getElementsByTagName("extends");
					Node extendsNode = extendsList.item(0);
					Element elExtends = (Element) extendsNode;
					//Alle mit "entry" eingeleiteten Vererbungen unter extends durchgehen
					NodeList entryList = elExtends.getElementsByTagName("entry");
					for (int j = 0; j < entryList.getLength(); j++)
					{
						Element nextEntry = document.createElement("entry");
						extensions.appendChild(nextEntry);
						//Vererbungsstruktur von "klasse" zu "klasse" eintragen
						Element from = document.createElement("from");
						nextEntry.appendChild(from);
						from.appendChild(document.createTextNode(
								elClassdef.getElementsByTagName("name").item(0).getTextContent())); /*0 durch Laufvariable i ersetzen? TODO*/
						Element to = document.createElement("to");
						nextEntry.appendChild(to);
						to.appendChild(document
								.createTextNode(elExtends.getElementsByTagName("entry").item(j).getTextContent()));
					}
				}
				//Pruefen auf Interfaces
				if (elClassdef.getElementsByTagName("implements").getLength() > 0)
				{
					NodeList implementsList = elClassdef.getElementsByTagName("implements");
					Node implementsNode = implementsList.item(0);
					Element elImplements = (Element) implementsNode;

					NodeList entryList = elImplements.getElementsByTagName("entry");
					for (int j = 0; j < entryList.getLength(); j++)
					{
						Element nextEntry = document.createElement("entry");
						implementations.appendChild(nextEntry);
						Element from = document.createElement("from");
						nextEntry.appendChild(from);
						from.appendChild(document.createTextNode(
								elClassdef.getElementsByTagName("name").item(0).getTextContent()));
						Element to = document.createElement("to");
						nextEntry.appendChild(to);
						to.appendChild(document
								.createTextNode(elImplements.getElementsByTagName("entry").item(j).getTextContent()));

						Element veryNextEntry = document.createElement("entry");
						interfaces.appendChild(veryNextEntry);
						veryNextEntry.appendChild(document
								.createTextNode(elImplements.getElementsByTagName("entry").item(j).getTextContent()));
					}
				}
				//Pruefen auf Komposition
				if (elClassdef.getElementsByTagName("compositions").getLength() > 0)
				{
					NodeList compositionsList = elClassdef.getElementsByTagName("compositions");
					Node compositionsNode = compositionsList.item(0);
					Element Ecompositions = (Element) compositionsNode;

					NodeList entryList = Ecompositions.getElementsByTagName("entry");
					for (int j = 0; j < entryList.getLength(); j++) {
						Element nextEntry = document.createElement("entry");
						compositions.appendChild(nextEntry);
						Element from = document.createElement("from");
						nextEntry.appendChild(from);
						from.appendChild(document
								.createTextNode(Ecompositions.getElementsByTagName("entry").item(j).getTextContent()));
						Element to = document.createElement("to");
						nextEntry.appendChild(to);
						to.appendChild(document.createTextNode(
								elClassdef.getElementsByTagName("name").item(0).getTextContent()));
					}
				}
				//Pruefen auf Aggregation
				if (elClassdef.getElementsByTagName("aggregations").getLength() > 0)
				{
					NodeList aggregationsList = elClassdef.getElementsByTagName("aggregations");
					Node aggregationsNode = aggregationsList.item(0);
					Element elAggregation = (Element) aggregationsNode;

					NodeList entryList = elAggregation.getElementsByTagName("entry");
					for (int j = 0; j < entryList.getLength(); j++)
					{
						Element nextEntry = document.createElement("entry");
						aggregations.appendChild(nextEntry);
						Element from = document.createElement("from");
						nextEntry.appendChild(from);
						from.appendChild(document
								.createTextNode(elAggregation.getElementsByTagName("entry").item(j).getTextContent()));
						Element to = document.createElement("to");
						nextEntry.appendChild(to);
						to.appendChild(document.createTextNode(
								elClassdef.getElementsByTagName("name").item(0).getTextContent()));
					}
				}
			}
			//Ausgabe Konsole
			xmlHelper.writeDocumentToConsole(document);
	    	return document;
		}
    	catch (ParserConfigurationException e)
    	{
			e.printStackTrace();
		}
		return null;
	}
}
