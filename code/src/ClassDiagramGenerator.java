
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
 * @author Johann Gerhardt
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
    	//createDiagram(xmlHelper.getDocumentFrom("testfolder/xmlSpecifications/parsedData.xml"));
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
			
			//Knoten namens "classdefinition" auflisten 
			NodeList classList = parsedData.getElementsByTagName("classdefinition");
			for (int i = 0; i < classList.getLength(); i++)
			{
				Node classdefinitionNode = classList.item(i);
				Element entry = document.createElement("entry");
				classes.appendChild(entry);
				Element elClassdef = (Element) classdefinitionNode;
				//Erzeugt "Entry"-tag mit Classennamen als Texteintrag
				entry.appendChild(document.createTextNode(
						elClassdef.getElementsByTagName("name").item(0).getTextContent()));				
				//StringArray um in einer Schleife nach Vererbungen, Kompositionen, Aggregationen und Implementierungen zu suchen
				String[] excoagim = {"extends", "compositions", "aggregations", "implements"};
				for(int k = 0; k < 4; k++)
				{
					if (elClassdef.getElementsByTagName(excoagim[k]).getLength() > 0)
					{
						NodeList nodeList = elClassdef.getElementsByTagName(excoagim[k]);
						Element element = (Element) nodeList.item(0);						
						NodeList entryList = element.getElementsByTagName("entry");
						
						for (int j = 0; j < entryList.getLength(); j++)
						{
							Element nextEntry = document.createElement("entry");
							Element from = document.createElement("from");
							Element to = document.createElement("to");
							nextEntry.appendChild(from);
							nextEntry.appendChild(to);
							
							//switch-Anweisung um Vererbung, Komposition, Aggregation und Implementierung individuell auszuführen 
							switch(k) {
							case 0:
								//Erzeugt "Entry"-tag mit "from" und "to"-tag in extensions mit ausgelesenem Inhalt aus parsedData
								extensions.appendChild(nextEntry);
								from.appendChild(document.createTextNode(
										elClassdef.getElementsByTagName("name").item(i).getTextContent()));
								to.appendChild(document.createTextNode(
										element.getElementsByTagName("entry").item(j).getTextContent()));
								break;
							case 1:
								compositions.appendChild(nextEntry);
								from.appendChild(document.createTextNode(
										element.getElementsByTagName("entry").item(j).getTextContent()));
								to.appendChild(document.createTextNode(
										elClassdef.getElementsByTagName("name").item(0).getTextContent()));
								break;
							case 2:
								aggregations.appendChild(nextEntry);
								from.appendChild(document.createTextNode(
										element.getElementsByTagName("entry").item(j).getTextContent()));
								to.appendChild(document.createTextNode(
										elClassdef.getElementsByTagName("name").item(0).getTextContent()));
								break;
							default:
								implementations.appendChild(nextEntry);
								from.appendChild(document.createTextNode(
										elClassdef.getElementsByTagName("name").item(0).getTextContent()));
								to.appendChild(document.createTextNode(
										element.getElementsByTagName("entry").item(j).getTextContent()));

								Element veryNextEntry = document.createElement("entry");
								interfaces.appendChild(veryNextEntry);
								veryNextEntry.appendChild(document.createTextNode(
										element.getElementsByTagName("entry").item(j).getTextContent()));
							}
						}
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
