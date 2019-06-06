/*Auskommentierter Code der Klasse OutputPUML von Patrick Otte und Tore Arndt*/
/**
     * Liefert den plantUML-Code zurueck
     * 
     * @param myParsingResult Ergebnisse des Parsens
     * @return String der den plantUML-Code enthaelt
     *
     *
     * Methode wird ersetzt durch xml basierte getPuml() Methode
     * 
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

 public String getPUML(ParsingResult myParsingResult)	//TODO eventuell ueberfluessig? http://plantuml.com/api ->Hilfe
    {
    	String pumlCode = "";
    	int from;
    	int to;
    	pumlCode+="@startuml/n";
    	for (int i = 0; i < myParsingResult.classes.size(); i++) 
    	{
    		pumlCode+="class ";
			pumlCode+=myParsingResult.classes.get(i);
    		pumlCode+="/n";
		}
    	for (int i = 0; i < myParsingResult.classConnections.size(); i++) 
    	{
    		from=myParsingResult.classConnections.get(i).getFrom();
    		to=myParsingResult.classConnections.get(i).getTo();
    		pumlCode+=myParsingResult.classes.get(from);
    		if (myParsingResult.classConnections.get(i).getConnection()==ClassConnection.connectionType.extension) 
    		{
				pumlCode+=" -- ";		//TODO eventuell Pfeile
			}
    		else if (myParsingResult.classConnections.get(i).getConnection()==ClassConnection.connectionType.aggregation) 
    		{
    			pumlCode+=" o-- ";	//TODO eventuell Richtung aendern
			}
    		else 
    		{
    			pumlCode+=" *-- ";	//TODO eventuell Richtung aendern
			}
    		pumlCode+=myParsingResult.classes.get(to);
    		pumlCode+="/n";
		}
    	pumlCode+="@enduml";
    
	return pumlCode;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    
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
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//---------------- erster Ansatz für Iteration durch gesamted Dokument ohne Verschachtelung in verschiedene Methoden ----------------

/*

//    	    for (int i = 0; i < list.getLength(); i++) //13 iterations MÜSSEN PER IF ABGEFRAGT WERDEN, DA SCHLIEẞENDE KNOTEN AUCH ANGEZEIGT WERDEN
//	    {
////    		System.out.println(list.item(i).getNodeName()); //TODO Test!!!!!!!!!!! Es gehen wieder methodes verloren!!
//		if (list.item(i).getNodeName() == "classes")
//		{
//		    list = list.item(i).getChildNodes(); // Ebene Tiefer <entry>-Ebene
//		    for (int j = 0; j < list.getLength(); j++)
//		    {
//			if (list.item(j).getNodeName() != "#text")
//			{
//			    pumlCode += "participiant " + list.item(j).getTextContent() + "\n"; //Einträge Einfügen
//			}
//		    }
//		    list = list.item(0).getParentNode().getParentNode().getChildNodes(); //Ebene hoch wechseln <classes>-Ebene
//		}
//		else if(list.item(i).getNodeName() == "entrypoint")
//		{
//		    list = list.item(i).getChildNodes(); //ebene tiefer <class>/<methods>-Ebene
//		    for (int j = 0; j < list.getLength(); j++)
//		    {
//			if (list.item(j).getNodeName() == "class") // Abfrage auf den Klassennamen
//	    		{
//	    		    tempStartClass = list.item(j).getTextContent();
//	    		}
//			else if (list.item(j).getNodeName() == "method") 
//			{
//			    tempMethod = list.item(j).getTextContent();
//			}
//		    }
//		    list = list.item(0).getParentNode().getParentNode().getChildNodes(); //<Methoddefinition>-Ebenen
//		    pumlCode += "note over "+ tempStartClass + ": " + tempMethod + "\n" + 
//		    	    	"activate " + tempStartClass + "\n";
//		}
//		else if (list.item(i).getNodeName() == "methoddefinition") //TODO Alle Implementationen der <Method>-Ebene
//		{
//		    list = list.item(i).getChildNodes(); //Unterebene Methoddefinition
//		    for (int j = 0; j < list.getLength(); j++)
//		    {
//			if (list.item(j).getNodeName() == "name") 
//			{
//			    tempMethod = "activate " + list.item(j).getTextContent(); //Called method
//			}
//			else if (list.item(j).getNodeName() == "alternative")
//			{
//			    list = list.item(j).getChildNodes(); //Unterebene alternative
////			    for (int j2 = 0; j2 < list.getLength(); j2++)
////			    {
//				helperMethodCall(list, pumlCode, j, tempStartClass);
////			    }
//			    list = list.item(0).getParentNode().getParentNode().getChildNodes(); // wieder auf <alternative>-Ebenen
//			}
//		    }
//		    list = list.item(0).getParentNode().getParentNode().getChildNodes(); // wieder auf <Methoddefinition>-Ebenen
//		}
//	    } //end initial Loop    	    
//    	    //return pumlCode;
//   


//---------------- zweiter Ansatz für Iteration durch gesamted Dokument als Verschachtelung in einer externen Methode ----------------
 public String helperMethodCall(NodeList list, String pumlCode, int i, String entry)
    {
	boolean alt = false; // wenn case geöffnet ist, sodass danach else
	for (; i < list.getLength(); i++)
	{
	    if (list.item(i).getNodeName() == "class")
	    {

	    }
	    else if (list.item(i).getNodeName() == "instance") // hier abfangen, wenn nichts direkt definiert, ebene
							       // tiefer!!!
	    {
		// Hier Einfügen
		if (list.item(i).getFirstChild().getNodeName() != "#text") // Test
		{
		    System.out.println(i + ": " + list.item(i).getNodeName() + " - " + list.item(i).getTextContent());
		}

	    }
	    else if (list.item(i).getNodeName() == "method")
	    {
		// Hier Einfügen
//		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getLocalName());
	    }
	    else if (list.item(i).getNodeName() == "type")
	    {
		// Hier Einfügen
//		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getLocalName());
	    }
	    else if (list.item(i).getNodeName() == "case")
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
		// Hier Einfügen

//		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getTextContent());
		helperMethodCall(list.item(i).getChildNodes(), pumlCode, 0, entry); // rekursiver Aufruf tieferer Ebene
	    }
	    else if (list.item(i).getNodeName() == "loop")
	    {
		// Hier Einfügen
//		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getTextContent());
		helperMethodCall(list.item(i).getChildNodes(), pumlCode, 0, entry); // rekursiver Aufruf tieferer Ebene
	    }
	    else if (list.item(i).getNodeName() == "methodcall")
	    {
		// Hier Einfügen
//		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getLocalName());
		helperMethodCall(list.item(i).getChildNodes(), pumlCode, 0, entry); // rekursiver Aufruf tieferer Ebene
	    }
	    else if (list.item(i).getNodeName() == "condition") // case
	    {
		pumlCode += list.item(i).getTextContent() + "\n";
		// Hier Einfügen
//		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getLocalName());
	    }
	}

	return pumlCode;
    }

*/

//---------------- Ansatz für Iteration durch Cases ----------------

//	    else if (list.item(i).getNodeName() == "class")
//	    {
//
//	    }
//	    else if (list.item(i).getNodeName() == "instance") // hier abfangen, wenn nichts direkt definiert, ebene
//							       // tiefer!!!
//	    {
//		// Hier Einfügen
//		if (list.item(i).getFirstChild().getNodeName() != "#text") // Test
//		{
//		    System.out.println(i + ": " + list.item(i).getNodeName() + " - " + list.item(i).getTextContent());
//		}
//
//	    }
//	    else if (list.item(i).getNodeName() == "method")
//	    {
//		// Hier Einfügen
////		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getLocalName());
//	    }
//	    else if (list.item(i).getNodeName() == "type")
//	    {
//		// Hier Einfügen
////		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getLocalName());
//	    }
//	    else if (list.item(i).getNodeName() == "case")
//	    {
//		if (!inAlt)
//		{
//		    pumlCode += "alt ";
//		    inAlt = true;
//		}
//		else
//		{
//		    pumlCode += "else ";
//		}
//		// Hier Einfügen
//
////		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getTextContent());
//		helperMethodCall(list.item(i).getChildNodes(), pumlCode, inAlt); // rekursiver Aufruf tieferer Ebene
//	    }
//	    else if (list.item(i).getNodeName() == "loop")
//	    {
//		// Hier Einfügen
////		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getTextContent());
//		helperMethodCall(list.item(i).getChildNodes(), pumlCode, inAlt); // rekursiver Aufruf tieferer Ebene
//	    }
//	    else if (list.item(i).getNodeName() == "methodcall")
//	    {
//		// Hier Einfügen
////		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getLocalName());
//		helperMethodCall(list.item(i).getChildNodes(), pumlCode, inAlt); // rekursiver Aufruf tieferer Ebene
//	    }
//	    else if (list.item(i).getNodeName() == "condition") // case
//	    {
//		pumlCode += list.item(i).getTextContent() + "\n";
//		// Hier Einfügen
////		System.out.println(i + ": " + list.item(i).getNodeName()+ " - " + list.item(i).getLocalName());
//	    }


//---------------- Ansatz für Methodenkopf der helperMethodCall Methode----------------

	 /*
	 String pumlCode = "";
	    String templistPath = listPath + "/name"; //an position name in der Methoddefinition
		list = XmlHelperMethods.getList(diagramData, listPath).item(0).getChildNodes();
		for (int a = 0; a < list.getLength(); a++)
		{
		    if (list.item(a).getNodeName() == "name")
		    {
			System.out.println("-- " + list.item(a).getNodeName());
			pumlCode += "activate " + startClass + "\n";
		    }
		}
		templistPath = listPath + "/alternative";
		list = XmlHelperMethods.getList(diagramData, templistPath);
		for (int a = 0; a < list.getLength(); a++)
		{
		    if (list.item(a).getNodeName() != "#text")
		    {
			pumlCode += helperCaseCall(diagramData, templistPath, startClass, false);
		    }
		}

	return pumlCode;
	*/
