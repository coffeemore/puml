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
	int foundNameIndex = -1;
	// Erstes/Erste Zeichen werden auf die �bertragenen Tokens �berpr�ft
	for (int i = 0; i < name.length; i++)
	{
	    if (source.substring(0, name[i].length()).equals(name[i]))
	    {
		found = true;
		foundNameIndex = i;
		// source = source.substring(name[i].length());
	    }
	}
	while (!found && !source.isEmpty())
	{

	    part = part + source.substring(0, 1); // erstes Zeichen wird in Part geschrieben
	    source = source.substring(1); // erstes Zeichen wird aus dem Sourcecode entfernt
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
	return new TokenResult(foundNameIndex, part, source); // R�ckgabe welcher Token gefunden wurde und den Inhalt
							      // zwischen
	// den Tokens (zB einen Klassennamen)

    }

//    public void findToken(String source)
//    {
//	String[][] TokenArray = new String[10][10];
//
//	TokenArray[0][0] = "\"";
//	TokenArray[0][1] = "\"";
//	TokenArray[0][2] = "\\\"";
//
//	TokenArray[1][0] = "//";
//	TokenArray[1][1] = "\n";
//
//	TokenArray[2][0] = "/*";
//	TokenArray[2][1] = "*/";
//
//	TokenArray[3][0] = "import ";
//	TokenArray[3][1] = ";";
//
//	TokenArray[4][0] = "class ";
//	TokenArray[4][1] = "extends";
//	TokenArray[4][2] = "implements";
//	TokenArray[4][3] = "{";
//
//	TokenArray[5][0] = "interface ";
//	TokenArray[5][1] = "{";
//	TokenArray[5][2] = "extends";
//
//	source = source.trim();
//
//	while (!source.isEmpty())
//	{
//
//	    boolean done = false;
//	    source = source.trim();
//	    for (int i = 0; i < TokenArray.length; i++)
//	    {
//
//		if (source.substring(0, TokenArray[i][0].length()).equals(TokenArray[i][0]))
//		{
//
//		    source = source.substring(TokenArray[i][0].length());
//		    source = source.trim();
//		    TokenResult res;
//		    do
//		    {
//			res = goToTokenWithName(source, TokenArray[i]);
//
//			if (res.getFoundToken() != 1)
//			{
//			    source = source.substring(1);
//			}
//		    }
//		    while (res.getFoundToken() == 1);
//		    source = source.substring(1); // Entfernen des ersten Zeichens
//		    done = true;
//
//		}
//	    }
//
//	    if (!done)
//	    {
//		source = source.substring(1);
//	    }
//	}
//
//    }

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
	int curlBrace = 0, roundBrace = 0;
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
		    // sourcec = sourcec.trim();
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
			// Lösung 1 compString = "{";
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

				curlBrace++;
				curlBraceBool = true;
			    }

			    sourcec = sourcec.substring(1);

			    String classImplementsStr = res.getData();
			    classImplementsStr = classImplementsStr.strip();
			    System.out.println("@implements: " + classImplementsStr);

			    Element classImplementsEl = document.createElement("entry");
			    classImplementsEl.appendChild(document.createTextNode(classImplementsStr));
			    classImplements.appendChild(classImplementsEl);
			}
			while (!(curlBraceBool));
			// Lösung 1 while (!(sourcec.substring(0,
			// compString.length()).equals(compString)));

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

		    Element ifDefinition = document.createElement("interfacedefinition");
		    Element ifNameEl = document.createElement("name");

		    // Element classElement = document.createElement("class");
		    ifNameEl.appendChild(document.createTextNode(interfaceName));
		    ifDefinition.appendChild(ifNameEl);
		    curNode.appendChild(ifDefinition);
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
		////////////
		if (curNode.getNodeName().equals("classdefinition")
			|| curNode.getNodeName().equals("interfacedefinition")||curNode.getNodeName().equals("methoddefinition")
			|| curNode.getNodeName().equals("constructor"))// &&
		// (sourcec.charAt(0)==';'||sourcec.charAt(0)=='{')
		{// TODO: funktionen in funktionen haben nicht klassen als eltern

		    String[] nameArray = new String[1];
		    nameArray[0] = "(";

		    TokenResult res1 = goToTokenWithName(sourcec, nameArray);
		    String functionData = res1.getData().strip();
		    functionData=functionData.replaceAll("\n", " ");
		    functionData=functionData.replaceAll(" +", " ");
		    String[] prefixRBrace = functionData.split(" ");

		    switch (prefixRBrace.length)
		    {
		    case 0:
			System.out.println("nichts");
			break;
		    case 1:// Funktionsaufruf oder Schleifen/Anweisungen
			switch (prefixRBrace[0])
			{
			case "if":
//			    break;
			case "for":

//			    break;
			case "while":
			    Element alternativeNode = document.createElement("alternative");
			    Element caseNode = document.createElement("case");
			    Element conditionNode = document.createElement("condition");
			    curNode.appendChild(alternativeNode);
			    alternativeNode.appendChild(caseNode);
			    caseNode.appendChild(conditionNode);

			    String[] nameArray2 = new String[1];
			    nameArray2[0] = ")";
			    TokenResult res = goToTokenWithName(sourcec, nameArray);
			    conditionNode.appendChild(document.createTextNode(prefixRBrace[0] + res.getData()));
			    sourcec = res.getSourceCode();
			    break;
			case "switch":

			    break;
			default:
			    System.out.println("condition");
			    break;
			}
			break;
		    case 2:// Konstruktor oder else if
			switch (prefixRBrace[0])
			{
			case "else":

			    break;
			case "new":// Objekterzeugung
//			    String compAgr = "agregation";
//			    if (curNode.getNodeName().equals("classdefinition"))
//			    {
//				compAgr = "composition";
//			    }
//			    Element classObject = document.createElement(compAgr);
//			    curNode.appendChild(classObject);
//
//			    Element classObjectEl = document.createElement("entry");
//			    classObjectEl.appendChild(document.createTextNode(prefixRBrace[1]));
//			    classObject.appendChild(classObjectEl);
//			    // curNode = (Element) curNode.getLastChild();
//			    sourcec = res1.getSourceCode();
			    break;
			case "private":// privater Konstruktor
			case "public": // Konstruktor
			    if (prefixRBrace[1].equals(curNode.getElementsByTagName("name").item(0).getTextContent()))
			    {
				System.out.println("Konstruktor");
				sourcec = res1.getSourceCode();
				sourcec = sourcec.substring(1);
				String[] nameArray2 = new String[2];
				nameArray2[0] = ")";
				nameArray2[1] = ",";
				TokenResult res2;
				Element classComposition = document.createElement("composition");
				curNode.appendChild(classComposition);
				do
				{

				    res2 = goToTokenWithName(sourcec, nameArray2);
				    String functionData2 = res2.getData().strip();

				    String[] argumentConstructor = functionData2.split(" ");
				    if (argumentConstructor.length == 2)
				    {
					Element classCompositionEl = document.createElement("entry");
					classComposition.appendChild(classCompositionEl);
					classCompositionEl.appendChild(document.createTextNode(argumentConstructor[0]));
				    }
				    sourcec = res2.getSourceCode();
				    sourcec = sourcec.substring(1);
				}
				while (res2.getFoundToken() != 0);
				// curNode = (Element) curNode.getLastChild();
				curlBrace++;

				sourcec = sourcec.trim();
				if (sourcec.charAt(0) == '{')
				{
				    sourcec = sourcec.substring(1);
				    curlBrace++;
				    Element constNode = document.createElement("constructor");
				    curNode.appendChild(constNode);
				    curNode = (Element) curNode.getLastChild();

				}

			    }

			    break;
			default:
			    System.out.println("Konstruktor oder else if");
			    break;
			}
			break;
		    case 3:// Funktionsdeklaration

			if ((prefixRBrace[0].equals("public") || prefixRBrace[0].equals("private"))
				&& !prefixRBrace[1].equals("class"))
			{
			    Element methoddefinitionNode = document.createElement("methoddefinition");
			    Element nameNode = document.createElement("name");
			    nameNode.appendChild(document.createTextNode(prefixRBrace[2]));
			    methoddefinitionNode.appendChild(nameNode);
			    curNode.appendChild(methoddefinitionNode);
			    // ...
			    // curNode = (Element) curNode.getLastChild();
			    sourcec = res1.getSourceCode();
			    sourcec = sourcec.substring(1);
			    String[] nameArray2 = new String[2];
			    nameArray2[0] = ")";
			    nameArray2[1] = ",";
			    TokenResult res2;
			    Element parametersNode = document.createElement("parameters");

			    do
			    {

				res2 = goToTokenWithName(sourcec, nameArray2);
				String functionData2 = res2.getData().strip();

				String[] parameterFunction = functionData2.split(" ");
				if (parameterFunction.length == 2)
				{
				    Element entryPNode = document.createElement("entry");
				    Element typePNode = document.createElement("type");
				    Element namePNode = document.createElement("name");
				    typePNode.appendChild(document.createTextNode(parameterFunction[0]));
				    namePNode.appendChild(document.createTextNode(parameterFunction[1]));
				    entryPNode.appendChild(typePNode);
				    entryPNode.appendChild(namePNode);
				    parametersNode.appendChild(entryPNode);
				}
				sourcec = res2.getSourceCode();
				sourcec = sourcec.substring(1);
			    }
			    while (res2.getFoundToken() != 0);
			    if (parametersNode.hasChildNodes())
				methoddefinitionNode.appendChild(parametersNode);
			    if (!prefixRBrace[1].equals("void"))
			    {
				Element resultNode = document.createElement("result");
				resultNode.appendChild(document.createTextNode(prefixRBrace[1]));
				methoddefinitionNode.appendChild(resultNode);

			    }
			    sourcec = sourcec.trim();
			    if (sourcec.charAt(0) == '{')
			    {
				sourcec = sourcec.substring(1);
				curlBrace++;
				curNode = (Element) curNode.getLastChild();

			    }
//			    sourcec = res1.getSourceCode();
//			    curNode = (Element) curNode.getLastChild();

			}
			break;
		    case 4:
			if ((prefixRBrace[0].equals("public") || prefixRBrace[0].equals("private"))
				&& prefixRBrace[1].equals("static"))
			{
			    Element methoddefinitionNode = document.createElement("methoddefinition");
			    Element nameNode = document.createElement("name");
			    nameNode.appendChild(document.createTextNode(prefixRBrace[3]));
			    methoddefinitionNode.appendChild(nameNode);
			    curNode.appendChild(methoddefinitionNode);
			    // ...
			    // curNode = (Element) curNode.getLastChild();

			    // Parameter der Funktion bestimmen
			    sourcec = res1.getSourceCode();
			    sourcec = sourcec.substring(1);
			    String[] nameArray2 = new String[2];
			    nameArray2[0] = ")";
			    nameArray2[1] = ",";
			    TokenResult res2;
			    Element parametersNode = document.createElement("parameters");
			    do
			    {

				res2 = goToTokenWithName(sourcec, nameArray2);
				String functionData2 = res2.getData().strip();

				String[] parameterFunction = functionData2.split(" ");
				if (parameterFunction.length == 2)
				{
				    Element entryPNode = document.createElement("entry");
				    Element typePNode = document.createElement("type");
				    Element namePNode = document.createElement("name");
				    typePNode.appendChild(document.createTextNode(parameterFunction[0]));
				    namePNode.appendChild(document.createTextNode(parameterFunction[1]));
				    entryPNode.appendChild(typePNode);
				    entryPNode.appendChild(namePNode);
				    parametersNode.appendChild(entryPNode);
				}
				sourcec = res2.getSourceCode();
				sourcec = sourcec.substring(1);
			    }
			    while (res2.getFoundToken() != 0);

			    if (parametersNode.hasChildNodes())
				methoddefinitionNode.appendChild(parametersNode);
			    if (!prefixRBrace[2].equals("void"))
			    {
				Element resultNode = document.createElement("result");
				resultNode.appendChild(document.createTextNode(prefixRBrace[2]));
				methoddefinitionNode.appendChild(resultNode);
			    }
			    sourcec = sourcec.trim();
			    if (sourcec.charAt(0) == '{')
			    {
				sourcec = sourcec.substring(1);
				curlBrace++;
				curNode = (Element) curNode.getLastChild();

			    }

//			    sourcec = res1.getSourceCode();
//			    curNode = (Element) curNode.getLastChild();
			    break;
			}
			
			if ((prefixRBrace[1].equals("=") || prefixRBrace[2].equals("new")))
			{
			    //Instance-knoten erstellen
			    Element instanceNode = document.createElement("instance");
			    curNode.appendChild(instanceNode);
			    Element instanceNNode = document.createElement("name");
			    instanceNode.appendChild(instanceNNode);
			    instanceNNode.appendChild(document.createTextNode(prefixRBrace[0]));
			    Element instanceCNode = document.createElement("class");
			    instanceNode.appendChild(instanceCNode);
			    instanceCNode.appendChild(document.createTextNode(prefixRBrace[3]));
			    
			    
			    //Composition-knoten erstellen
			    Element classObject = document.createElement("composition");
			    curNode.appendChild(classObject);

			    Element classObjectEl = document.createElement("entry");
			    classObjectEl.appendChild(document.createTextNode(prefixRBrace[3]));
			    classObject.appendChild(classObjectEl);
			    // curNode = (Element) curNode.getLastChild();
			    sourcec = res1.getSourceCode();
			    
			    
			}
			break;

		    default:
			System.out.println("Keine Funktion");
			break;
		    }
		}


//		    else
//		    {
//			String returnType = res2.getData();
//			functionData = functionData.substring(returnType.length());
//			functionData.trim();
//			nameArray[0] = "(";
//			TokenResult res3 = goToTokenWithName(functionData, nameArray);
//
//			String methodName = res3.getData();
//			String[] methodNamePart = methodName.split(" ");
//			
//			if (methodName.isEmpty())
//			{
//			    System.out.println("Funktionsaufruf");
//			} 
//			else if (methodNamePart.length==2)
//			{
//			    System.out.println("Funktionsdeklaration");
//
//			    nameArray[0] = "{";
//
//			    TokenResult res4 = goToTokenWithName(sourcec, nameArray);
//			    sourcec = res4.getSourceCode();
//			    sourcec = sourcec.substring(1);
//
//			    curlBrace++;
//			    Element methodDefinition = document.createElement("methoddefinition"); // WCB - with curly
//												   // brace
//			    Element methodNameNode = document.createElement("name");
//			    Element resultNameNode = document.createElement("result");
//			    methodNameNode.appendChild(document.createTextNode(methodNamePart[1]));
//			    resultNameNode.appendChild(document.createTextNode(methodNamePart[0]));
//
//			    methodDefinition.appendChild(methodNameNode);
//			    methodDefinition.appendChild(resultNameNode);
//
//			    curNode.appendChild(methodDefinition);
//			    curNode = (Element) curNode.getLastChild();
//			}else if (methodNamePart.length==1)
//			{
//			    
//			    System.out.println("Konstruktor");
//			}

//		    }

//		}

		////////////

		// Temporäre Lösung
		compString = "{";
		if (sourcec.substring(0, compString.length()).equals(compString))
		{
		    sourcec = sourcec.substring(1);
		    curlBrace++;
		    Element somethingWCB = document.createElement("something"); // WCB - with curly brace
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
//		compString = "(";
//		if (sourcec.substring(0, compString.length()).equals(compString))
//		{
//		    sourcec = sourcec.substring(1);
//		    roundBrace++;
//		    
//		}
//		;
//		compString = ")";
//		if (sourcec.substring(0, compString.length()).equals(compString))
//		{
//		    sourcec = sourcec.substring(1);
//		    roundBrace--;
//		   
//		}
//		;
//		compString = "if";
//		if (sourcec.substring(0, compString.length()).equals(compString))
//		{
//		    
//		    int countb = roundBrace;
//		    String conditionText="if";
//		    sourcec = sourcec.substring(compString.length());
//		    sourcec = sourcec.trim();
//		    
//		    String[] nameArray = new String[2];
//		    nameArray[0] = "(";
//		    nameArray[1] = ")";
//		    do
//		    {
//			TokenResult res = goToTokenWithName(sourcec, nameArray);
//			sourcec = res.getSourceCode();
//			if(res.getFoundToken() == 0) countb++; else countb--;
//			conditionText+=res.getData();
//			
//		    }
//		    while (countb != roundBrace);
//		    
//		    Element somethingWCB = document.createElement("alternative");
//		    Element caseEl = document.createElement("case");
//		    Element conditionEl = document.createElement("condition"); //WCB - with curly brace
//		    
//		    somethingWCB.appendChild(caseEl);
//		    caseEl.appendChild(conditionEl);
//		    conditionEl.appendChild(document.createTextNode(conditionText));
//		    
//		    curNode.appendChild(somethingWCB);
//		    curNode = (Element) curNode.getLastChild();
//		    done = true;
//		}
//		;
//		compString = "while";
//		if (sourcec.substring(0, compString.length()).equals(compString))
//		{
//		    
//		    int countb = roundBrace;
//		    String conditionText="while";
//		    sourcec = sourcec.substring(compString.length());
//		    sourcec = sourcec.trim();
//		    
//		    String[] nameArray = new String[2];
//		    nameArray[0] = "(";
//		    nameArray[1] = ")";
//		    do
//		    {
//			TokenResult res = goToTokenWithName(sourcec, nameArray);
//			sourcec = res.getSourceCode();
//			if(res.getFoundToken() == 0) countb++; else countb--;
//			conditionText+=res.getData();
//			
//		    }
//		    while (countb != roundBrace);
//		    
//		    Element somethingWCB = document.createElement("alternative");
//		    Element caseEl = document.createElement("case");
//		    Element conditionEl = document.createElement("condition"); //WCB - with curly brace
//		    
//		    somethingWCB.appendChild(caseEl);
//		    caseEl.appendChild(conditionEl);
//		    conditionEl.appendChild(document.createTextNode(conditionText));
//		    
//		    curNode.appendChild(somethingWCB);
//		    curNode = (Element) curNode.getLastChild();
//		    done = true;
//		}
//		;
		if (!done)
		{
		    sourcec = sourcec.substring(1);
		}
	    }
	}
	catch (

	Exception e)
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
	//sourceCode = sourceCode.replaceAll("=", " = ");
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
