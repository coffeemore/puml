import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author Klasse, die den Parser f�r Java implementiert
 */
public class ParserJava implements ParserIf
{
    Document document;

    /**
     * Konstruktor
     */
    public ParserJava()
    {

    }

    class TokenResult
    {

	public TokenResult(int foundToken, String data, String sourceCode)
	{
	    super();
	    this.foundToken = foundToken;
	    this.data = data;
	    this.sourceCode = sourceCode;
	}

	private int foundToken;
	private String data = "";
	private String sourceCode = "";

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
	String part = ""; // Variable wird genutztum zB Namen zu speichern
	boolean found = false;
	int foundNameIndex = 0;
	// Erstes/Erste Zeichen werden auf die �bertragenen Tokens �berpr�ft
	for (int i = 0; i < name.length; i++)
	{
	    if (source.substring(0, name[i].length()).equals(name[i]))
	    {
		found = true;
		foundNameIndex = i;
	    }
	}
	while (!found)
	{

	    part = part + source.substring(0, 1); // erstes Zeichen wird in Part geschrieben
	    source = source.substring(1); // erstes Zeichen wird aus dem Sourcecode entfernt
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
	return new TokenResult(foundNameIndex, part, source); // R�ckgabe welcher Token gefunden wurde und den Inhalt
							      // zwischen
	// den Tokens (zB einen Klassennamen)

    }

    public void findToken(String source)
    {
	String[][] TokenArray = new String[10][10];

	TokenArray[0][0] = "\"";
	TokenArray[0][1] = "\"";
	TokenArray[0][2] = "\\\"";

	TokenArray[1][0] = "//";
	TokenArray[1][1] = "\n";

	TokenArray[2][0] = "/*";
	TokenArray[2][1] = "*/";

	TokenArray[3][0] = "import ";
	TokenArray[3][1] = ";";

	TokenArray[4][0] = "class ";
	TokenArray[4][1] = "extends";
	TokenArray[4][2] = "implements";
	TokenArray[4][3] = "{";

	TokenArray[5][0] = "interface ";
	TokenArray[5][1] = "{";
	TokenArray[5][2] = "extends";

	source = source.trim();

	while (!source.isEmpty())
	{

	    boolean done = false;
	    source = source.trim();
	    for (int i = 0; i < TokenArray.length; i++)
	    {

		if (source.substring(0, TokenArray[i][0].length()).equals(TokenArray[i][0]))
		{

		    source = source.substring(TokenArray[i][0].length());
		    source = source.trim();
		    TokenResult res;
		    do
		    {
			res = goToTokenWithName(source, TokenArray[i]);

			if (res.getFoundToken() != 1)
			{
			    source = source.substring(1);
			}
		    }
		    while (res.getFoundToken() == 1);
		    source = source.substring(1); // Entfernen des ersten Zeichens
		    done = true;

		}
	    }

	    if (!done)
	    {
		source = source.substring(1);
	    }
	}

    }

    /**
     * Entfernt Kommentare aus �bergebenem String
     * 
     * @param sourcece �bergebener String aus dem Kommentare entfernt werden
     * @return XMl-Dokument
     * @throws ParserConfigurationException
     */
    public void buildTree(String sourcec) throws ParserConfigurationException
    {
	// Erstellen des Dokuments
	DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

	DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

	document = documentBuilder.newDocument();
	Element curNode;
	// root element
	Element root = document.createElement("source");
	curNode = root;
	int curlBrace = 0;
	document.appendChild(root);
	// System.out.println(sourcec);
	String compString;

	try
	{
	    while (!sourcec.isEmpty())
	    {
		sourcec = sourcec.trim(); // Entfernen von Leerzeichen und Zeilenumbr�chen
		boolean done = false;

		compString = "\""; // Entfernen von Strings zur Vermeidung von Problemen beim Entfernen von
				   // Kommentaren
		if (sourcec.substring(0, compString.length()).equals(compString))
		{
		    sourcec = sourcec.substring(compString.length());
		    sourcec = sourcec.trim();
		    String[] nameArray = new String[3];
		    nameArray[0] = "\\\\";
		    nameArray[1] = "\\\"";
		    nameArray[2] = "\"";

		    TokenResult res;
		    // Gehe durch den Sourcecode, bis kein String mehr gefunden wurde
		    do
		    {
			res = goToTokenWithName(sourcec, nameArray);
			sourcec = res.getSourceCode();
			// System.out.println("@res: " + res.getData());
			if (res.getFoundToken() != 2)
			{
			    sourcec = sourcec.substring(2);
			}

		    }
		    while (res.getFoundToken() != 2);
		    sourcec = sourcec.substring(1);
		    done = true;
		}
		;
		// Entfernen von Zeilen-Kommentaren
		compString = "//";
		if (sourcec.substring(0, compString.length()).equals(compString))
		{
		    TokenResult res;
		    sourcec = sourcec.substring(compString.length());
		    //sourcec = sourcec.trim();
		    String[] nameArray = new String[1];
		    nameArray[0] = "\n";
		    res = goToTokenWithName(sourcec, nameArray);
		    sourcec = res.getSourceCode();
		    done = true;
		}
		;

		// Entfernen von Block-Kommentaren
		compString = "/*";
		if (sourcec.substring(0, compString.length()).equals(compString))
		{
		    TokenResult res;
		    sourcec = sourcec.substring(compString.length());
		    sourcec = sourcec.trim();
		    String[] nameArray = new String[1];
		    nameArray[0] = "*/";
		    res = goToTokenWithName(sourcec, nameArray);
		    sourcec = res.getSourceCode();
		    done = true;
		}
		;

		compString = "import ";
		if (sourcec.substring(0, compString.length()).equals(compString))
		{
		    TokenResult res;
		    sourcec = sourcec.substring(compString.length());
		    sourcec = sourcec.trim();
		    String[] nameArray = new String[1];
		    nameArray[0] = ";";
		    res = goToTokenWithName(sourcec, nameArray);
		    sourcec = res.getSourceCode();
		    done = true;
		}
		;

		compString = "class ";
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
		    curNode = (Element) curNode.getLastChild();

		    compString = "extends ";
		    if (sourcec.substring(0, compString.length()).equals(compString))
		    {
			sourcec = sourcec.substring(compString.length());
			sourcec = sourcec.trim();
			nameArray = new String[2];
			nameArray[0] = "{";
			nameArray[1] = "implements";
			res = goToTokenWithName(sourcec, nameArray);
			sourcec = res.getSourceCode();
			String classExtendsStr = res.getData();
			if (res.getFoundToken() == 0)
			{
			    sourcec = sourcec.substring(1);
			    curlBrace++;
			}
			classExtendsStr = classExtendsStr.strip();
			System.out.println("@extendsName: " + classExtendsStr);

			Element classExtends = document.createElement("extends");
			Element classExtendsEl = document.createElement("entry");
			classExtendsEl.appendChild(document.createTextNode(classExtendsStr));
			classExtends.appendChild(classExtendsEl);
			classDefinition.appendChild(classExtends);

		    }
		    compString = "implements ";
		    if (sourcec.substring(0, compString.length()).equals(compString))
		    {

			Element classImplements = document.createElement("implements");
			classDefinition.appendChild(classImplements);

			sourcec = sourcec.substring(compString.length());
			boolean curlBraceBool = false;
			//Lösung 1 compString = "{";
			do
			{
			    sourcec = sourcec.trim();
			    nameArray = new String[2];
			    nameArray[0] = "{";
			    nameArray[1] = ",";
			    res = goToTokenWithName(sourcec, nameArray);
			    sourcec = res.getSourceCode();
			    if (res.getFoundToken() == 0)
			    {
				sourcec = sourcec.substring(1);
				curlBrace++;
				curlBraceBool = true;
			    }
			    String classImplementsStr = res.getData();
			    classImplementsStr = classImplementsStr.strip();
			    System.out.println("@implements: " + classImplementsStr);

			    Element classImplementsEl = document.createElement("entry");
			    classImplementsEl.appendChild(document.createTextNode(classImplementsStr));
			    classImplements.appendChild(classImplementsEl);
			}
			while(!(curlBraceBool));
			//Lösung 1 while (!(sourcec.substring(0, compString.length()).equals(compString)));

		    }
		    done = true;
		}
		compString = "interface ";
		if (sourcec.substring(0, compString.length()).equals(compString))
		{
		    sourcec = sourcec.substring(compString.length());
		    sourcec = sourcec.trim();
		    String[] nameArray = new String[2];
		    nameArray[0] = "{";
		    nameArray[1] = "extends";
		    TokenResult res = goToTokenWithName(sourcec, nameArray);
		    sourcec = res.getSourceCode();
		    String interfaceName = res.getData();
		    if (res.getFoundToken() == 0)
		    {
			sourcec = sourcec.substring(1);
			curlBrace++;
		    }

		    interfaceName = interfaceName.strip();
		    System.out.println("@interfaceName: " + interfaceName);

		    Element classDefinition = document.createElement("classdefinition");
		    Element classNameEl = document.createElement("name");

		    // Element classElement = document.createElement("class");
		    classNameEl.appendChild(document.createTextNode(interfaceName));
		    classDefinition.appendChild(classNameEl);
		    curNode.appendChild(classDefinition);
		    curNode = (Element) curNode.getLastChild();

		    //////// Temporär///////
		    // Name der Klasse/des Interfaces
		    System.out.println(curNode.getFirstChild().getTextContent());
		    ///////////////////////

		    compString = "extends ";
		    if (sourcec.substring(0, compString.length()).equals(compString))
		    {
			sourcec = sourcec.substring(compString.length());
			sourcec = sourcec.trim();
			nameArray = new String[1];
			nameArray[0] = "{";
			res = goToTokenWithName(sourcec, nameArray);
			String extendsName = res.getData();
			if (res.getFoundToken() == 0)
			{
			    sourcec = sourcec.substring(1);
			    curlBrace++;
			}
			extendsName = extendsName.strip();
			System.out.println("@extendsName: " + extendsName);
		    }
		    done = true;
		}
		;
		
		
		//Temporäre Lösung
		compString = "{";
		if (sourcec.substring(0, compString.length()).equals(compString))
		{
		    sourcec = sourcec.substring(1);
		    curlBrace++;
		    Element somethingWCB = document.createElement("something"); //WCB - with curly brace
		    somethingWCB.appendChild(document.createTextNode(" Funktion || Schleife || Abfrage "));
		    curNode.appendChild(somethingWCB);
		    curNode = (Element) curNode.getLastChild();
		}
		;
		compString = "}";
		if (sourcec.substring(0, compString.length()).equals(compString))
		{
		    sourcec = sourcec.substring(1);
		    curlBrace--;
		    curNode = (Element) curNode.getParentNode();
		}
		;
		if (!done)
		{
		    sourcec = sourcec.substring(1);
		}
	    }
	}
	catch (Exception e)
	{
	    // TODO: handle exception
	}
	TransformerFactory tf = TransformerFactory.newInstance();
	Transformer transformer;
	try
	{
	    transformer = tf.newTransformer();
	    StringWriter writer = new StringWriter();

	    transformer.transform(new DOMSource(document), new StreamResult(writer));

	    String xmlString = writer.getBuffer().toString();
	    System.out.println(xmlString); // Print to console or logs
	}
	catch (TransformerException e)
	{
	    e.printStackTrace();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	// System.out.println(document.getTextContent());

    }

    /**
     * Liest den �bergebenen Quellcode ein und parsed die Informationen daraus
     * 
     * @param sourceCode Vollst�ndiger Java-Quellcode
     */
    public void parse(String sourceCode)
    {
	sourceCode.trim();
	System.out.println(sourceCode);
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

    /**
     * Liefert die Ergebnisse des Parsens zurueck
     * 
     * @return XML Document mit den Ergebnissen des Parsens
     */
    public Document getParsingResult()
    {

	return document;
    }
}
