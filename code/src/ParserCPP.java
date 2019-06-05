import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class ParserCPP implements ParserIf 
{
	private Document document;
	private XmlHelperMethods xmlHelper = new XmlHelperMethods();
	
	 /**
     * leerer Konstruktor
     */
    public ParserCPP()
    {
    	
    }
    
	//Getter- und Settermethoden 
	public Document getDocument() 
	{
		return document;
	}
	public void setDocument(Document document) 
	{
		this.document = document;
	}
	
	//Hilfsklasse TokenResult: 
	class TokenResult
    {
		private int foundToken;
		private String data, sourceCode;
		
		/**
		 * Konstruktor
		 * 
		 * @param foundToken
		 * @param data
		 * @param sourceCode
		 */
		public TokenResult(int foundToken, String data, String sourceCode)
		{
		    super();
		    this.foundToken = foundToken;
		    this.data = data;
		    this.sourceCode = sourceCode;
		}
		
	    
	
		//Getter- und Settermethoden
		public int getFoundToken() 
		{
			return foundToken;
		}
		public void setFoundToken(int foundToken) 
		{
			this.foundToken = foundToken;
		}
		public String getData() 
		{
			return data;
		}
		public void setData(String data) 
		{
			this.data = data;
		}
		public String getSourceCode() 
		{
			return sourceCode;
		}
		public void setSourceCode(String sourceCode) 
		{
			this.sourceCode = sourceCode;
		}
    }
	
	public TokenResult goToTokenWithName(String source, String[] name)
    {
	//Variable wird genutzt um zB Namen zu speichern
	String part = ""; 
	boolean found = false;
	int foundNameIndex = -1;
	//Erstes/Erste Zeichen werden auf die uebertragenen Tokens ueberprueft
	for (int i = 0; i < name.length; i++)
	{
	    if (source.substring(0, name[i].length()).equals(name[i]))
	    {
		found = true;
		foundNameIndex = i;
		//source = source.substring(name[i].length());
	    }
	}
	while (!found && !source.isEmpty())
	{
		//erstes Zeichen wird in Part geschrieben
	    part = part + source.substring(0, 1); 
	    //erstes Zeichen wird aus dem Sourcecode entfernt
	    source = source.substring(1); 
	   
	    if (source.isEmpty())
	    {
	    	break;
	    }
	   
	    for (int i = 0; i < name.length; i++)
	    {
			if (source.substring(0, name[i].length()).equals(name[i]))
			{
			    found = true;
			    foundNameIndex = i;
			}
	    }
	}
	
	source = source.trim();
	 //Rueckgabe welches Token gefunden wurde und des Inhalts zwischen den Tokens (zB einen Klassen-Namen)
	return new TokenResult(foundNameIndex, part, source);
 }
	
	//Here is where the magic happens:
	
	 /**
     * Entfernt Kommentare aus uebergebenem String und erstellt eine passende XML-Datei
     * 
     * @param sourceCode uebergebener String aus dem die Kommentare entfernt werden sollen
     * @return XMl-Dokument
     * @throws ParserConfigurationException
     */
	private void buildTree( ArrayList<String> code) throws ParserConfigurationException 
	{
		////JANS ZEUG////
		
		String sourceCodeHPP = deleteComStr(code.get(0));
		String sourceCodeCPP = deleteComStr(code.get(1));
		String compString;		
		/*
		// Erstellen des Dokuments
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
		document = documentBuilder.newDocument();
		//BOOL zur Abfrage ob Teilstring verarbeitet wurde (ansonsten ein Schritt weiter)
		boolean done = false;
		
		// Start-Knoten setzen
		Element curNode;
		// root Element
		Element root = document.createElement("source");
		curNode = root;
		document.appendChild(root);
		
		//KLAMMERSETZUNG
		int curlBrace = 0, roundBrace = 0;
		
		// TODO 
		//Zunächst HPP-Dateien durchgehen

		while (!sourceCodeHPP.isEmpty())
	    {
			compString= "include";
			//
			if(sourceCodeHPP.substring(0, compString.length()).equals(compString)) 
			{
				sourceCodeHPP = sourceCodeHPP.substring(compString.length());
				sourceCodeHPP = sourceCodeHPP.trim();
				
			    TokenResult res;
			    sourceCodeHPP = sourceCodeHPP.substring(compString.length());
			    sourceCodeHPP = sourceCodeHPP.trim();
			    String[] nameArray = new String[1];
			    nameArray[0] = ";";
			    res = goToTokenWithName(sourceCodeHPP, nameArray);
			    sourceCodeHPP = res.getSourceCode();
			    done = true;
			}
			
			if (!done)
			{
				sourceCodeHPP = sourceCodeHPP.substring(1);
			}
			
	    }
	    */

	//JOHANNS ZEUG:
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		documentBuilder = documentFactory.newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		
		Element root = document.createElement("source");
		document.appendChild(root);
	
	    String keyword = "class ";
	    //Suche nach dem Index vom Wort "class" im HPP-Code
	    int index = sourceCodeHPP.indexOf(keyword);
	    
	    while (index >=0)
	    {
	        //Index vom Ende des Klassennamens erfassen welches mit Leerzeichen oder Zeilenumbruch endet
	        int b = Math.min(sourceCodeHPP.indexOf(" ", index + keyword.length()),
	        		sourceCodeHPP.indexOf("\n", index + keyword.length()));
	        
	        //Ausgabe des Folgewortes von "class"
	        System.out.println(sourceCodeHPP.substring(index + keyword.length(), b));
	        
	        Element classdefinition = document.createElement("classdefinition");
			root.appendChild(classdefinition);
			
			Element name = document.createElement("name");
			classdefinition.appendChild(name);	
			name.appendChild(document.createTextNode(sourceCodeHPP.substring(index + keyword.length(), b)));
	        
			Element implement = document.createElement("implements");
			classdefinition.appendChild(implement);
			
			Element extend = document.createElement("extends");
			classdefinition.appendChild(extend);
			
			Element instance = document.createElement("instance");
			classdefinition.appendChild(instance);
			
			Element compositions = document.createElement("compositions");
			classdefinition.appendChild(compositions);
			
			Element aggregation = document.createElement("aggregation");
			classdefinition.appendChild(aggregation);
			
			Element interfacedefinition = document.createElement("interfacedefinition");
			classdefinition.appendChild(interfacedefinition);
			
		    //Suche nach dem nächsten Index vom Wort "class" im HPP-Code
	        index = sourceCodeHPP.indexOf(keyword, index + keyword.length());
	    }		
		xmlHelper.writeDocumentToConsole(document);
	}
		
	
	private void SearchInCode(String subject, String sourceCodeCPP) 
	{
		
		

	}
	
	/**
     * Liest den uebergebenen Quellcode ein und parsed die Informationen daraus
     * @param sourceCode Vollstaendiger Java-Quellcode
     */
    public void parse(ArrayList<String> sourceCode)
    {
    	sourceCode.set(0, deleteComStr(sourceCode.get(0)));
    	sourceCode.set(1, deleteComStr(sourceCode.get(1)));
    	
    	//Ausgabe eingelesener HPP-Dateien
    	System.out.println(sourceCode.get(0));
    	//Ausgabe eingelesener CPP-Dateien
    	System.out.println(sourceCode.get(1));
    	
		try 
		{
			buildTree(sourceCode);
		} 
		catch (ParserConfigurationException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
    }

	//Methode zum Entfernen von Kommentaren und Strings für komplikationsfreies Parsen
    public String deleteComStr(String sourceCode)
    {
       	String cSC = ""; //commentlessSourceCode
    	for(int i = 0, n = sourceCode.length(); i < n; i++)
    	{
    		//Filtern nach Kommentar mit //
    		if(sourceCode.charAt(i) == '/' && sourceCode.charAt(i + 1) == '/')
    		{ 
    			i++;
    			while(sourceCode.charAt(i) != '\n' && i < n)
    			{
    				i++;
    			}
    			cSC += "\n";	//um Zeilenumbruch nicht auszulassen. Alternative: i--;
    		}
    		//Filtern nach Kommentar mit /* */
    		else if(sourceCode.charAt(i) == '/' && sourceCode.charAt(i + 1) == '*')
    		{
    			i+=2;
    			while(!(sourceCode.charAt(i) == '*' && sourceCode.charAt(i + 1) == '/') && i < n)
    			{
    				i++;
    			}
    			i++;
    		}
    		//Filtern nach Strings
    		else if(sourceCode.charAt(i) == '"')
    		{
    			i++;
    			while(sourceCode.charAt(i) != '"' && i < n)
    			{
    				i++;
    			}
    		}
    		else
    		{
    			cSC += sourceCode.charAt(i);
    		}
    	}		
    	return cSC;
    }
	@Override 
	 /**
    * Liefert die Ergebnisse des Parsens zurueck
    * @return XML Document mit den Ergebnissen des Parsens
    */
	public Document getParsingResult() 
	{
		return document;
	}

}



//BSP
/*
 * compString = "class ";
		if (sourcec.substring(0, compString.length()).equals(compString))
		{
		    sourcec = sourcec.substring(compString.length());
		    sourcec = sourcec.trim();
		    String[] nameArray = new String[3];
		    nameArray[0] = "{";
		    nameArray[1] = "extends";
		    nameArray[2] = "implements";
		    TokenResult res = goToTokenWithName(sourcec, nameArray);
		    sourcec = res.getSourceCode();
		    if (res.getFoundToken() == 0)
		    {
			sourcec = sourcec.substring(1);
			curlBrace++;
		    }
		    String classNameStr = res.getData();
		    classNameStr = classNameStr.strip();
		    System.out.println("@className: " + classNameStr);

		    Element classDefinition = document.createElement("classdefinition");
		    Element classNameEl = document.createElement("name");
		    classNameEl.appendChild(document.createTextNode(classNameStr));
		    classDefinition.appendChild(classNameEl);
		    curNode.appendChild(classDefinition);
		    curNode = (Element) curNode.getLastChild();*/