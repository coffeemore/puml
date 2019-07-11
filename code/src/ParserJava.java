
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.*;
import java.util.Vector;
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

	public TokenResult(int foundToken, String data)
	{
	    super();
	    this.foundToken = foundToken;
	    this.data = data;
	}

	private int foundToken;
	private String data = "";

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

	return new TokenResult(foundNameIndex, part); // R�ckgabe welcher Token gefunden wurde und den Inhalt zwischen
						      // den Tokens (zB einen Klassennamen)

    }

    public void findToken(String compString, String[] token2Array, int aktion)
    {

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

	// root element
	Element root = document.createElement("source");
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
		    String[] nameArray = new String[2];
		    nameArray[0] = "\\\"";
		    nameArray[1] = "\"";

		    TokenResult res;
		    // Gehe durch den Sourcecode, bis kein String mehr gefunden wurde
		    do
		    {
			res = goToTokenWithName(sourcec, nameArray);
			// System.out.println("@res: " + res.getData());
			if (res.getFoundToken() == 0)
			{
			    sourcec = sourcec.substring(1);
			}
		    }
		    while (res.getFoundToken() != 1);
		    sourcec = sourcec.substring(1); // Entfernen des ersten Zeichens
		    done = true;
		}
		;
		// Entfernen von Zeilen-Kommentaren
		compString = "//";
		if (sourcec.substring(0, compString.length()).equals(compString))
		{
		    sourcec = sourcec.substring(compString.length());
		    sourcec = sourcec.trim();
		    String[] nameArray = new String[1];
		    nameArray[0] = "\n";
		    goToTokenWithName(sourcec, nameArray);
		    done = true;
		}
		;

		// Entfernen von Block-Kommentaren
		compString = "/*";
		if (sourcec.substring(0, compString.length()).equals(compString))
		{
		    sourcec = sourcec.substring(compString.length());
		    sourcec = sourcec.trim();
		    String[] nameArray = new String[1];
		    nameArray[0] = "*/";
		    goToTokenWithName(sourcec, nameArray);
		    done = true;
		}
		;

		compString = "import ";
		if (sourcec.substring(0, compString.length()).equals(compString))
		{
		    sourcec = sourcec.substring(compString.length());
		    sourcec = sourcec.trim();
		    String[] nameArray = new String[1];
		    nameArray[0] = ";";
		    goToTokenWithName(sourcec, nameArray);
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
		    String className = res.getData();
		    className = className.strip();
		    System.out.println("@className: " + className);
		    Element classElement = document.createElement("class");
		    classElement.appendChild(document.createTextNode(className));
		    root.appendChild(classElement);
		    compString = "extends ";
		    if (sourcec.substring(0, compString.length()).equals(compString))
		    {
			sourcec = sourcec.substring(compString.length());
			sourcec = sourcec.trim();
			nameArray = new String[2];
			nameArray[0] = "{";
			nameArray[1] = "implements";
			res = goToTokenWithName(sourcec, nameArray);
			String extendsName = res.getData();
			extendsName = extendsName.strip();
			System.out.println("@extendsName: " + extendsName);
		    }
		    compString = "implements ";
		    if (sourcec.substring(0, compString.length()).equals(compString))
		    {
			sourcec = sourcec.substring(compString.length());
			compString = "{";
			do
			{
			    sourcec = sourcec.trim();
			    nameArray = new String[2];
			    nameArray[0] = "{";
			    nameArray[1] = ",";
			    res = goToTokenWithName(sourcec, nameArray);
			    String interfaceName = res.getData();
			    interfaceName = interfaceName.strip();
			    System.out.println("@implements: " + interfaceName);
			}
			while (!(sourcec.substring(0, compString.length()).equals(compString)));

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
		    String interfaceName = res.getData();
		    interfaceName = interfaceName.strip();
		    System.out.println("@interfaceName: " + interfaceName);
		    Element classElement = document.createElement("class");
		    classElement.appendChild(document.createTextNode(interfaceName));
		    root.appendChild(classElement);
		    compString = "extends ";
		    if (sourcec.substring(0, compString.length()).equals(compString))
		    {
			sourcec = sourcec.substring(compString.length());
			sourcec = sourcec.trim();
			nameArray = new String[1];
			nameArray[0] = "{";
			res = goToTokenWithName(sourcec, nameArray);
			String extendsName = res.getData();
			extendsName = extendsName.strip();
			System.out.println("@extendsName: " + extendsName);
		    }
		    done = true;
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
