import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

public class ParserCPP implements ParserIf 
{
	private Document document;
	
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
	
	//Here is where the magic happens:
	
	 /**
     * Entfernt Kommentare aus uebergebenem String und erstellt eine passende XML-Datei
     * 
     * @param sourceCode uebergebener String aus dem die Kommentare entfernt werden sollen
     * @return XMl-Dokument
     * @throws ParserConfigurationException
     */
	private void buildTree(String sourceCode) 
	{
		// TODO 
		
	}
	
	/**
     * Liest den �bergebenen Quellcode ein und parsed die Informationen daraus
     * @param sourceCode Vollstaendiger Java-Quellcode
     */
    public void parse(String sourceCode)
    {
  
		/*try
		{
		    buildTree(sourceCode);
		}
		catch (ParserConfigurationException e)
		{
			//Wird später noch geworfen
			//Eintrag in den Logger
			PUMLgenerator.logger.getLog().warning("ParserConfigurationException: Aufbau des Build-Trees");
		    e.printStackTrace();
		}*/
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
    			i+=2;;
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
