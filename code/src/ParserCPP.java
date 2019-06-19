import java.util.ArrayList;

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
		//TODO Auskommentieren richtig
		String sourceCodeHPP = deleteComStr(code.get(0));
		String sourceCodeCPP = deleteComStr(code.get(1));
		//String sourceCodeHPP = code.get(0);
		//String sourceCodeCPP = code.get(1);
		sourceCodeHPP = sourceCodeHPP.trim();
		sourceCodeCPP = sourceCodeCPP.trim();

		//JOHANNS ZEUG:
		Document document = xmlHelper.createDocument();
		
		
		Element root = document.createElement("source");
		document.appendChild(root);
	
		//Suche nach dem Index vom Wort "class" im HPP-Code
		String keyword = "class ";
	    int index = sourceCodeHPP.indexOf(keyword);
	    
	    while (index >=0)
	    {
	        //Index vom Ende des Klassennamens erfassen welches mit Leerzeichen oder Zeilenumbruch endet
	        int b = Math.min(sourceCodeHPP.indexOf(" ", index + keyword.length()),
	        				 sourceCodeHPP.indexOf("\n", index + keyword.length()));
	        
	        //Ausgabe des Folgewortes von "class"
	        System.out.println(sourceCodeHPP.substring(index + keyword.length(), b));
	       
			
			//Klasse oder Interface oder abstract
	        String className =sourceCodeHPP.substring(index + keyword.length(), b);
	        
	        if(isInterface(className,sourceCodeHPP))
	        {	
	        	//Interface
				Element interfacedefinition = document.createElement("interfacedefinition");
				root.appendChild(interfacedefinition);
				Element name = document.createElement("name");
				interfacedefinition.appendChild(name);	
	 			name.appendChild(document.createTextNode(className));
	 			
	 			//Code Analyse:
	 			//Vererbung eines Interfaces
				Element implement = document.createElement("implements");
				interfacedefinition.appendChild(implement);
				
				//Vererbung einer Klasse
				Element extend = document.createElement("extends");
				interfacedefinition.appendChild(extend);
				
				//Instanzen,(public, private , protected)
				Element instance = document.createElement("instance");
				interfacedefinition.appendChild(instance);
				
				//variablen
				Element var = document.createElement("var");
				interfacedefinition.appendChild(var);
				
				//Komposition 
				Element compositions = document.createElement("compositions");
				interfacedefinition.appendChild(compositions);
				
				//Aggregation
				Element aggregation = document.createElement("aggregation");
				interfacedefinition.appendChild(aggregation);
				
				//Methoden
				
	        }
	        else if(isAbstract(className,sourceCodeHPP)) 
	        {
	        	//Abstract
				Element abstractdefinition = document.createElement("abstractdefinition");
				root.appendChild(abstractdefinition);
				Element name = document.createElement("name");
				abstractdefinition.appendChild(name);	
	 			name.appendChild(document.createTextNode(className));
	 			
	 			//Code Analyse:
	 			//Vererbung eines Interfaces
				Element implement = document.createElement("implements");
				abstractdefinition.appendChild(implement);
				
				//Vererbung einer Klasse
				Element extend = document.createElement("extends");
				abstractdefinition.appendChild(extend);
				
				//Instanzen,(public, private , protected)
				Element instance = document.createElement("instance");
				abstractdefinition.appendChild(instance);
				
				//variablen
				Element var = document.createElement("var");
				abstractdefinition.appendChild(var);
				
				//Komposition 
				Element compositions = document.createElement("compositions");
				abstractdefinition.appendChild(compositions);
				
				//Aggregation
				Element aggregation = document.createElement("aggregation");
				abstractdefinition.appendChild(aggregation);
				
				//Methoden
				
	        }
	        else 
	        {
	        	//Klasse
		        Element classdefinition = document.createElement("classdefinition");
	 			root.appendChild(classdefinition);
	 			Element name = document.createElement("name");
	 			classdefinition.appendChild(name);	
	 			name.appendChild(document.createTextNode(className));
	 			
	 			//Code Analyse:
	 			//Vererbung eines Interfaces
				Element implement = document.createElement("implements");
				classdefinition.appendChild(implement);
				
				//Vererbung einer Klasse
				Element extend = document.createElement("extends");
				classdefinition.appendChild(extend);
				
				//Instanzen,(public, private , protected)
				Element instance = document.createElement("instance");
				classdefinition.appendChild(instance);
				
				//variablen
				Element var = document.createElement("var");
				classdefinition.appendChild(var);
				
				//Komposition 
				Element compositions = document.createElement("compositions");
				classdefinition.appendChild(compositions);
				
				//Aggregation
				Element aggregation = document.createElement("aggregation");
				classdefinition.appendChild(aggregation);
				
				//Methoden
				
	        }
	        
	        
		    //Suche nach dem nächsten Index vom Wort "class" im HPP-Code
	        index = sourceCodeHPP.indexOf(keyword, index + keyword.length());
	    }
	    	// System.out.println("\n #################### BEGINN JANS TEST ####################\n");
	    	//System.out.println("\n ORIGINAL SOURCECODE \n"+sourceCodeHPP+"\n ENDE \n");
	    //isInterface("Class2",sourceCodeHPP);
	    System.out.println("IF2 >>"+isAbstract("If2",sourceCodeHPP));
	    System.out.println("CLASS2 >>"+isAbstract("Class2",sourceCodeHPP));
	    System.out.println("IF1 >>"+isAbstract("If1",sourceCodeHPP));
	    System.out.println("IF3/FIGURE >>"+isAbstract("Figure",sourceCodeHPP));
	    System.out.println("IF4/TEST >>"+isAbstract("Test",sourceCodeHPP));
	    /////////////////////////////////////////////////////////////////////////////////////////
		xmlHelper.writeDocumentToConsole(document);
	}
	
	//Wertet aus, ob eine Klasse eines HPP-QUelltextes ein Interface ist
	private boolean isInterface(String className,String sourceCodeHPP)
	{	
		//Klassen-Deklaration zusammen setzen
		String keyword = "class "+className;
		
		// Klassen-Code (hpp) aus Quellcode filtern
		int keyIndex = sourceCodeHPP.indexOf(keyword) + keyword.length();
		String sourcec = sourceCodeHPP.substring(keyIndex,sourceCodeHPP.indexOf("};", keyIndex));
			
		// Code kürzen, filtern und anpassen
		sourcec =  sourcec.replaceAll("public:", "");
		sourcec =  sourcec.replaceAll("private:", "");
		sourcec =  sourcec.replaceAll("protected:", "");
		sourcec =  sourcec.replaceAll("\n", "");
		sourcec =  sourcec.replaceAll("\t", "");
		sourcec = sourcec.replace('{', ' ');
		while(sourcec.contains("  ")) 
		{
			sourcec =  sourcec.replaceAll("  ", " ");
		}
		sourcec = sourcec.trim();
		
		// Prüfen ob alle Methoden virtuel und =0; sind && Dekonstruktor: virtual ~IDemo() {} 
		int i = 0, n = sourcec.length();
		while( i+keyword.length() < n && i!=-1 )
    	{
    		//virtuelle Methode
    		if(sourcec.substring( i, i+"virtual ".length()).equals("virtual "))
    		{ 
    			//Dekonstruktor
    		    if(sourcec.substring( i+"virtual ".length(), i+"virtual ".length()+1).equals("~"))
    			{
    					//Soweit True
    			}
    			//nicht implementierte Methode
    			else if(sourcec.lastIndexOf("0", sourcec.indexOf(";",i)) != -1 && sourcec.lastIndexOf("=", sourcec.indexOf("0", (sourcec.lastIndexOf("0", sourcec.indexOf(";",i)))   )) != -1) 
    			{
    					//Soweit True	
    			}
    			//Fehler-Fall
    			else
    			{
    				return false;
    			}
    		}
    		//Fehler-Fall
    		else
    		{
    			return false;
    		}
    		
    		//Nächsten Methodenanfang finden und unsinnigen Index Abfangen	
    		if(Math.min( sourcec.indexOf(";", i), sourcec.indexOf("}", i))>=0)
    		{
    			i= Math.min( sourcec.indexOf(";", i), sourcec.indexOf("}", i)+2); 
    		}
    		else if (sourcec.indexOf(";", i)>=0)
    		{
    			i= sourcec.indexOf(";", i)+2;
    		}
    	}	
		return true;
	}
	
	//Wertet aus, ob eine Klasse eines HPP-QUelltextes eine abstrakte Klasse ist
	private boolean isAbstract(String className,String sourceCodeHPP)
	{	
		//Klassen-Deklaration zusammen setzen
		String keyword = "class "+className;
		
		// Klassen-Code (hpp) aus Quellcode filtern
		int keyIndex = sourceCodeHPP.indexOf(keyword) + keyword.length();
		String sourcec = sourceCodeHPP.substring(keyIndex,sourceCodeHPP.indexOf("};", keyIndex));
		
		// Code kürzen, filtern und anpassen
		sourcec =  sourcec.replaceAll("public:", "");
		sourcec =  sourcec.replaceAll("private:", "");
		sourcec =  sourcec.replaceAll("protected:", "");
		
		sourcec =  sourcec.replaceAll("\n", "");
		sourcec =  sourcec.replaceAll("\t", "");
		sourcec = sourcec.replace('{', ' ');
		while(sourcec.contains("  ")) 
		{
			sourcec =  sourcec.replaceAll("  ", " ");
		}
		sourcec = sourcec.trim();
		
		// Prüfen ob eine Methoden virtuel und =0; sind 
		int i = 0, n = sourcec.length();
		while( i+keyword.length() < n && i!=-1 )
    	{
    		//virtuelle Methode
    		if(sourcec.substring( i, i+"virtual ".length()).equals("virtual "))
    		{ 
    			//nicht implementierte Methode
    			if(sourcec.lastIndexOf("0", sourcec.indexOf(";",i)) != -1 && sourcec.lastIndexOf("=", sourcec.indexOf("0", (sourcec.lastIndexOf("0", sourcec.indexOf(";",i)))   )) != -1) 
    			{
    					return true;
    			}
    		}
    		
    		//Nächsten Methodenanfang finden und unsinnigen Index Abfangen	
    		if(Math.min( sourcec.indexOf(";", i), sourcec.indexOf("}", i))>=0)
    		{
    			i= Math.min( sourcec.indexOf(";", i), sourcec.indexOf("}", i)+2); 
    		}
    		else if (sourcec.indexOf(";", i)>=0)
    		{
    			i= sourcec.indexOf(";", i)+2;
    		}
    	}	
		return false;
	}
	
	//Liefert aus einem .hpp-Quellcode und einem Klassennamen den (ersten) passenden Klassen-Quelltext
	public String getFormatedSourceCodeHPP(String className, String sourceCode)
	{
		//Klassen-Deklaration zusammen setzen
		className= "class "+className;
		// Klassen-Code aus Quellcode filtern
		int keyIndex = sourceCode.indexOf(className) + className.length();
		sourceCode = sourceCode.substring(keyIndex,sourceCode.indexOf("};", keyIndex));
		// Code kürzen, filtern und anpassen
		sourceCode = sourceCode.replaceAll("\n", "");
		sourceCode = sourceCode.replaceAll("\t", "");
		sourceCode = sourceCode.replace('{', ' ');
		while(sourceCode.contains("  ")) 
		{
			sourceCode =  sourceCode.replaceAll("  ", " ");
		}
		sourceCode = sourceCode.trim();
		return sourceCode;
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
    		/*Entfernt, da es Fehler hervorruft*/
    		//Filtern nach Strings
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



