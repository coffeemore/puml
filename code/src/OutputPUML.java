import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;
import net.sourceforge.plantuml.SourceStringReader;


/**
 * 
 * @author developer Klasse welche die Ausgabe des plantUML-Codes und die
 *         Klassendiagramme erzeugt
 */
public class OutputPUML
{
    private static NodeList list = null;
	LogMain logger= new LogMain();
    /**
     * Konstruktor
     */
    public OutputPUML()
    {
    };

    /**
     * Liefert den plantUML - Code aus xml zurueck
     * 
     * @param diagramData plantUML-Code zur Erzeugung als xmlDoc
     * @return plantUML Code zur Erstellung mit plantuml.jar
     * @throws XPathExpressionException 
     */

    public String getPUML(Document diagramData) throws XPathExpressionException
    {
	    LogMain logger= new LogMain();
	    XPathFactory xPathfactory = XPathFactory.newInstance();
	    XPath xpath = xPathfactory.newXPath();
	    XPathExpression expr = xpath.compile("//parsed/*"); // Startpunkt parsed Knoten
	    list = (NodeList) expr.evaluate(diagramData, XPathConstants.NODESET); //in Liste
	    String compare = list.item(0).getNodeName();
	    String pumlCode="@startuml \n";
	    String tempString="";
	    if(compare == "classdiagramm")
	    {
	    	list = getList(diagramData, xpath, "//parsed/classdiagramm/classes/entry");
	    	for(int a=0; a<list.getLength(); a++) {
	    		if(list.item(a).getNodeName() != "#text") {
	    			System.out.println(list.item(a).getTextContent());
	    		}
	    	}

	    	//for (int a=0; a)
	    	//list=list.item(h).getChildNodes;
    	    
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	for (int i = 0; i < list.getLength(); i++)
		    {
				if (list.item(i).getNodeName() == "classes")
				{
				    list = list.item(i).getChildNodes();
				    for (int j = 0; j < list.getLength(); j++)
				    {
						if (list.item(j).getNodeName() != "#text")
						{
						    pumlCode += "classes " + list.item(j).getTextContent() + " \n"; //Einträge Einfügen
						}
				    }
				    list = list.item(0).getParentNode().getParentNode().getChildNodes(); //Ebene hoch wechseln <classes>-Ebene
				}
				else if(list.item(i).getNodeName() == "interfaces")
				{	
					list = list.item(i).getChildNodes();
					for (int j = 0; j < list.getLength(); j++)
				    {
						if (list.item(j).getNodeName() != "#text")
						{
						    pumlCode += "interface " + list.item(j).getTextContent() + " \n"; //Einträge Einfügen
						}
				    }
					list = list.item(0).getParentNode().getParentNode().getChildNodes();
				}
				else if(list.item(i).getNodeName() == "classrelations")
				{
					list = list.item(i).getChildNodes();					
					for (int h = 0; h < list.getLength(); h++)
				    {
						if (list.item(h).getNodeName() == "extensions")
						{
							list = list.item(h).getChildNodes();
							for(int j=0; j<list.getLength(); j++) 
							{
								if (list.item(j).getNodeName() == "entry")
								{
									list = list.item(j).getChildNodes();
									for(int k=0; k<list.getLength(); k++) 
									{
										if ((list.item(k).getNodeName() == "from")&&(list.item(j).getNodeName() != "#text"))
										{
											pumlCode +=list.item(k).getTextContent() + " --|> ";
										}
										else if((list.item(k).getNodeName() == "to")&&(list.item(j).getNodeName() != "#text"))
										{
											pumlCode +=list.item(k).getTextContent()+" \n";
										}
										else
										{
											logger.getLog().warning("Fehler: extension->from/to");
										}
									}
								}
							}
						}
						else if (list.item(h).getNodeName() == "implementations")
						{
							list = list.item(h).getChildNodes();
							for(int j=0; j<list.getLength(); j++) 
							{
								if (list.item(j).getNodeName() == "entry")
								{
									list = list.item(j).getChildNodes();
									for(int k=0; k<list.getLength(); k++) 
									{
										if ((list.item(k).getNodeName() == "from")&&(list.item(j).getNodeName() != "#text"))
										{
											pumlCode +=list.item(k).getTextContent() + " --|> ";
										}
										else if((list.item(k).getNodeName() == "to")&&(list.item(j).getNodeName() != "#text"))
										{
											pumlCode +=list.item(k).getTextContent()+" \n";
										}
										else
										{
											logger.getLog().warning("Fehler: implementations->from/to");
										}
									}
								}
							}
						}
						else if (list.item(h).getNodeName() == "compositions")
						{
							list = list.item(h).getChildNodes();
							for(int j=0; j<list.getLength(); j++) 
							{
								if (list.item(j).getNodeName() == "entry")
								{
									list = list.item(j).getChildNodes();
									for(int k=0; k<list.getLength(); k++) 
									{
										if ((list.item(k).getNodeName() == "from")&&(list.item(j).getNodeName() != "#text"))
										{
											pumlCode +=list.item(k).getTextContent() + " --* ";
										}
										else if((list.item(k).getNodeName() == "to")&&(list.item(j).getNodeName() != "#text"))
										{
											pumlCode +=list.item(k).getTextContent()+" \n";
										}
										else
										{
											logger.getLog().warning("Fehler: compositions->from/to");
										}
									}
								}
							}
						}
						else if (list.item(h).getNodeName() == "aggregations")
						{
							list = list.item(h).getChildNodes();
							for(int j=0; j<list.getLength(); j++) 
							{
								if (list.item(j).getNodeName() == "entry")
								{
									list = list.item(j).getChildNodes();
									for(int k=0; k<list.getLength(); k++) 
									{
										if ((list.item(k).getNodeName() == "from")&&(list.item(j).getNodeName() != "#text"))
										{
											pumlCode +=list.item(k).getTextContent() + " --o ";
										}
										else if((list.item(k).getNodeName() == "to")&&(list.item(j).getNodeName() != "#text"))
										{
											pumlCode +=list.item(k).getTextContent()+" \n";
										}
										else
										{
											logger.getLog().warning("Fehler: aggregations->from/to");
										}
									}
								}
							}
						}
				    }
				}
		    }
	    }
    
				
				
				
else if(compare == "sequencediagram"){
    	    
    	    
    	    String tempStartClass = "";
    	    String tempEndClass = "";
    	    String tempMethod= "";
    	    String pumlCode = "@startuml \n";
    	    list = list.item(0).getChildNodes(); //Stelle: <classes>-Ebene
    	    for (int i = 0; i < list.getLength(); i++) //13 iterations MÜSSEN PER IF ABGEFRAGT WERDEN, DA SCHLIEẞENDE KNOTEN AUCH ANGEZEIGT WERDEN
	    {
//    		System.out.println(list.item(i).getNodeName()); //TODO Test!!!!!!!!!!! Es gehen wieder methodes verloren!!
		if (list.item(i).getNodeName() == "classes")
		{
		    list = list.item(i).getChildNodes(); // Ebene Tiefer <entry>-Ebene
		    for (int j = 0; j < list.getLength(); j++)
		    {
			if (list.item(j).getNodeName() != "#text")
			{
			    pumlCode += "participiant " + list.item(j).getTextContent() + "\n"; //Einträge Einfügen
			}
		    }
		    list = list.item(0).getParentNode().getParentNode().getChildNodes(); //Ebene hoch wechseln <classes>-Ebene
		}
		else if(list.item(i).getNodeName() == "entrypoint")
		{
		    list = list.item(i).getChildNodes(); //ebene tiefer <class>/<methods>-Ebene
		    for (int j = 0; j < list.getLength(); j++)
		    {
			if (list.item(j).getNodeName() == "class") // Abfrage auf den Klassennamen
	    		{
	    		    tempStartClass = list.item(j).getTextContent();
	    		}
			else if (list.item(j).getNodeName() == "method") 
			{
			    tempMethod = list.item(j).getTextContent();
			}
		    }
		    list = list.item(0).getParentNode().getParentNode().getChildNodes(); //<Methoddefinition>-Ebenen
		    pumlCode += "note over "+ tempStartClass + ": " + tempMethod + "\n" + 
		    	    	"activate " + tempStartClass + "\n";
		}
		else if (list.item(i).getNodeName() == "methoddefinition") //TODO Alle Implementationen der <Method>-Ebene
		{
		    list = list.item(i).getChildNodes(); //Unterebene Methoddefinition
		    for (int j = 0; j < list.getLength(); j++)
		    {
			if (list.item(j).getNodeName() == "name") 
			{
			    tempMethod = "activate " + list.item(j).getTextContent(); //Called method
			}
			else if (list.item(j).getNodeName() == "alternative")
			{
			    list = list.item(j).getChildNodes(); //Unterebene alternative
//			    for (int j2 = 0; j2 < list.getLength(); j2++)
//			    {
				helperMethodCall(list, pumlCode, j, tempStartClass);
//			    }
			    list = list.item(0).getParentNode().getParentNode().getChildNodes(); // wieder auf <alternative>-Ebenen
			}
		    }
		    list = list.item(0).getParentNode().getParentNode().getChildNodes(); // wieder auf <Methoddefinition>-Ebenen
		}
	    } //end initial Loop    	    
    	    return pumlCode;
    		
    	}
    	else {
    		logger.getLog().warning("XML-Diagramm fehlerhaft");
    	}
	return "Error with Loop";
    }
    
    public String helperMethodCall(NodeList list, String pumlCode, int i, String entry)
    {
	boolean alt = false; //wenn case geöffnet ist, sodass danach else
	for (; i < list.getLength(); i++)
	{
	    if (list.item(i).getNodeName() == "class")
	    {
		
	    }
	    else if(list.item(i).getNodeName() == "instance") //hier abfangen, wenn nichts direkt definiert, ebene tiefer!!!
	    {
		//Hier Einfügen
		    System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getTextContent());
		
	    }
	    else if(list.item(i).getNodeName() == "method")
	    {
		//Hier Einfügen
//		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getLocalName());
	    }
	    else if(list.item(i).getNodeName() == "type")
	    {
		//Hier Einfügen
//		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getLocalName());
	    }
	    else if(list.item(i).getNodeName() == "case") 
	    {
		if (!alt)
		{
		    pumlCode += "alt ";
		    alt = true;
		}
		else
		{
		    pumlCode += "else ";
		}
		//Hier Einfügen
		
		
//		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getTextContent());
		helperMethodCall(list.item(i).getChildNodes(), pumlCode, 0, entry); //rekursiver Aufruf tieferer Ebene
	    }
	    else if(list.item(i).getNodeName() == "loop")
	    {
		//Hier Einfügen
//		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getTextContent());
		helperMethodCall(list.item(i).getChildNodes(), pumlCode, 0, entry); //rekursiver Aufruf tieferer Ebene
	    }
	    else if(list.item(i).getNodeName() == "methodcall")
	    {
		//Hier Einfügen
//		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getLocalName());
		helperMethodCall(list.item(i).getChildNodes(), pumlCode, 0, entry); //rekursiver Aufruf tieferer Ebene
	    }
	    else if(list.item(i).getNodeName() == "condition") //case
	    {
		pumlCode += list.item(i).getTextContent() + "\n";
		//Hier Einfügen
//		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getLocalName());
	    }
	}
	
	
	return pumlCode;
    }
    
    /**
     * Liefert den plantUML-Code zurueck
     * 
     * @param myParsingResult Ergebnisse des Parsens
     * @return String der den plantUML-Code enthaelt
     *
     *
     * Methode wird ersetzt durch xml basierte getPuml() Methode
     *
    public String getPUML(ParsingResult myParsingResult)
    {
	String pumlCode = "";
	int counter=0;
	pumlCode += "@startuml\n";
	while (myParsingResult.getAttributeLocalName(counter)!="parsed" && myParsingResult.isEndElement()) 
	{
		if(myParsingResult.getAttributeLocalName(counter)=="classes" && myParsingResult.isStartElement()) {
			while(myParsingResult.getAttributeLocalName(counter++)=="entry") {
					pumlCode+= "class " + myParsingResult.getElementText() + "\n";
			}
		}
		if(myParsingResult.getAttributeLocalName(counter)=="classrelations" && myParsingResult.isStartElement()) {
			if(myParsingResult.getAttributeLocalName(counter++)=="aggregations" && myParsingResult.isStartElement()) {
				if(myParsingResult.getAttributeLocalName(counter++)=="entry" && myParsingResult.isStartElement()) {
					if(myParsingResult.getAttributeLocalName(counter++)=="from") {
						pumlCode+= myParsingResult.getElementText()+ " o-- ";
						if(myParsingResult.getAttributeLocalName(counter++)=="to") {
							pumlCode+= myParsingResult.getElementText()+"\n";
						}
					}
				}
			}
			else if(myParsingResult.getAttributeLocalName(counter++)=="compositions" && myParsingResult.isStartElement()) {
				if(myParsingResult.getAttributeLocalName(counter++)=="entry" && myParsingResult.isStartElement()) {
					if(myParsingResult.getAttributeLocalName(counter++)=="from") {
						pumlCode+= myParsingResult.getElementText()+ " *-- ";
						if(myParsingResult.getAttributeLocalName(counter++)=="to") {
							pumlCode+= myParsingResult.getElementText()+"\n";
						}
					}
				}
			}
			else if(myParsingResult.getAttributeLocalName(counter++)=="extension" && myParsingResult.isStartElement()) {
				if(myParsingResult.getAttributeLocalName(counter++)=="entry" && myParsingResult.isStartElement()) {
					if(myParsingResult.getAttributeLocalName(counter++)=="from") {
						pumlCode+= myParsingResult.getElementText()+ " -- ";
						if(myParsingResult.getAttributeLocalName(counter++)=="to") {
							pumlCode+= myParsingResult.getElementText()+"\n";
						}
					}
				}
			}
		}
	counter++;
	}
	myParsingResult.close();
	pumlCode += "@enduml";
	return pumlCode;
    }
    */

    /**
     * Speichert den plantUML-Code aus XML Dokument der getPUML Methode in eine Datei
     * 
     * @param diagramData	Xml Document durch getPUML Methode erzeugt
     * @param filePath		Pfad an den die Datei gespeichert werden soll
     * @throws IOException 
     */
    public void savePUMLtoFile(Document diagramData, String filePath) throws IOException
    {
    	
    }
    
    /**
     * alte String basierte Methode
     * 
     * Speichert den plantUML-Code aus dem String der getPUML Methode in eine Datei
     * 
     * @param pumlCode		String der durch die getPUML Methode erzeugt wird
     * @param filePath		Pfad an den die Datei gespeichert werden soll
     * @throws IOException 
     *
    public void savePUMLtoFile(String pumlCode, String filePath) throws IOException
    {
	    BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filePath)));
	    bw.write(pumlCode);
	    bw.flush();
	    bw.close();
    }
    */

    /**
     * Erzeugt ein PlantUML-Diagramm aus der plantUML-Code-Datei am uebergebenen Pfad
     * 
     * @param sourcePath	Pfad an der die plantUML-Code-Datei liegt
     * @param destPath		Ordnerpfad, !!nicht Dateiname!!, an dem die png-Datei gespeichert wird, Name der PNG=Name der Textdatei
     * @throws IOException
     */
    public void createPUMLfromFile(String sourcePath, String destPath) throws IOException //
    {
	File source = new File(sourcePath);
	File dest = new File (destPath);
	SourceFileReader reader = new SourceFileReader(source, dest);
	List<GeneratedImage> list = reader.getGeneratedImages();
	list.get(0).getPngFile();
    }
    
    
    /**
     * Erzeugt ein PlantUML-Diagramm aus dem durch die Methode getPUML erzeugten String
     * 
     * @param filePath	Pfad an der die plantUML-PNG gespeichert werden soll
     * @param pumlCode	String der durch die getPUML Methode erzeugt wurde
     * @throws IOException
     */
    public void createPUMLfromString(String filePath, String pumlCode) throws IOException
    {
    	OutputStream png = new FileOutputStream(filePath);
    	SourceStringReader reader = new SourceStringReader(pumlCode);
    	reader.outputImage(png).getDescription();
    }
    
    private static NodeList getList(Document doc, XPath xpath, String path) {
        try {
           XPathExpression expr = xpath.compile(path);
           list = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return list;
    }
}
