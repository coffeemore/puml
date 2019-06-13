
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.xpath.XPathExpressionException;

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
				Element name = document.createElement("Name");
				Element elClassdef = (Element) classdefinitionNode;
				entry.appendChild(name);
				//Erzeugt "name"-tag mit Classennamen als Texteintrag
				name.appendChild(document.createTextNode(
						elClassdef.getElementsByTagName("name").item(0).getTextContent()));
				
				//Kopieren der "instance"-Knoten von parsedData zu document
				NodeList instanceList = xmlHelper.getList(elClassdef, "./instance");
				for(int j = 0; instanceList.getLength() > j; j++)
				{
				    entry.appendChild(document.importNode(instanceList.item(j), true));
				}
				
				//Kopieren der "var"-Knoten von parsedData zu document
				NodeList varList = xmlHelper.getList(elClassdef, "./var");
				for(int j = 0; varList.getLength() > j; j++)
				{
				    entry.appendChild(document.importNode(varList.item(j), true));
				}
				
				/*
				//löschen übrflüssiger Knoten im "methoddefinition"-Knoten
				NodeList delList = xmlHelper.getList(elClassdef, "./methoddefinition/alternative");
				for(int j = 0; delList.getLength() > j; j++)
				{
					Element elementremove = (Element) delList.item(j);
			        elementremove.getParentNode().removeChild(elementremove);
				}*/

				//Kopieren der "methoddefinition"-Knoten von parsedData zu document
				NodeList methoddefList = xmlHelper.getList(elClassdef, "./methoddefinition");
				for(int j = 0; methoddefList.getLength() > j; j++)
				{
					//Löschen irrelevanter Knoten im "methoddefinition"-Knoten
					NodeList children = methoddefList.item(j).getChildNodes();
					Node current = null;
					int count = children.getLength();
					for (int k = 0; k < count; k++)
						{
							current = children.item(k);
							//System.out.println(j + "  " + current);
							if(current != null) {
								if (current.getNodeType() == Node.ELEMENT_NODE)
								{
									String currentNode = current.getNodeName();
									
									if(!(currentNode.equals("access") ||
										currentNode.equals("name") ||
										currentNode.equals("parameters") ||
										currentNode.equals("result")))
									{
										Element elementremove = (Element) current;
										elementremove.getParentNode().removeChild(elementremove);
										System.out.println("Unterknoten " + currentNode + " wird nicht übernommen.");
									}
								}
							}
						}

					Node methoddefnode = document.importNode(methoddefList.item(j), true);
				    entry.appendChild(methoddefnode);
				}
								
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
										elClassdef.getElementsByTagName("name").item(0).getTextContent()));
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
			document.normalize();
			xmlHelper.writeDocumentToConsole(document);
	    	return document;
		}
    	catch (ParserConfigurationException e)
    	{
			e.printStackTrace();
		}
    	catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
