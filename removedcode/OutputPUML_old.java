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