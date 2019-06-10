import java.io.StringWriter;

import java.util.ArrayList;

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
import org.w3c.dom.Node;

/**
 * 
 * @author Klasse, die den Parser f�r Java implementiert
 */
public class ParserJava extends XmlHelperMethods implements ParserIf
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

    /**
     * Entfernt Kommentare aus uebergebenem String
     * 
     * @param sourcece uebergebener String aus dem Kommentare entfernt werden
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

	String[] switchCaseCondition = new String[10];
	for (int i = 0; i < switchCaseCondition.length; i++)
	{
	    switchCaseCondition[i] = "";
	}

	boolean[] switchCaseOn = new boolean[10];
	for (int i = 0; i < switchCaseOn.length; i++)
	{
	    switchCaseOn[i] = false;
	}
	int curSwitch = 0;

	document.appendChild(root);
	// System.out.println(sourcec);
	String compString;

	try
	{
	    while (!sourcec.isEmpty())
	    {

		sourcec = sourcec.trim(); // Entfernen von Leerzeichen und Zeilenumbruechen
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
		    continue;
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
		    continue;
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
		    continue;
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
		    continue;
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
		    Element compNode = document.createElement("compositions");
		    curNode.appendChild(compNode);
		    Element agrNode = document.createElement("aggregations");
		    curNode.appendChild(agrNode);

		    done = true;
		    continue;
		}
		;

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
		    Element compNode = document.createElement("compositions");
		    curNode.appendChild(compNode);
		    Element agrNode = document.createElement("aggregations");
		    curNode.appendChild(agrNode);
		    done = true;
		    continue;
		}
		;
		////////////
//				if (curNode.getNodeName().equals("classdefinition")
//						|| curNode.getNodeName().equals("interfacedefinition")
//						|| curNode.getNodeName().equals("methoddefinition"))
		//
		// || curNode.getNodeName().equals("constructor"))// &&

		// (sourcec.charAt(0)==';'||sourcec.charAt(0)=='{')
		// TODO: funktionen in funktionen haben nicht klassen als eltern
		if (!(curNode.getNodeName().equals("source")))
		{

		    String[] nameArray = new String[1];
		    nameArray[0] = "(";

		    TokenResult res1 = goToTokenWithName(sourcec, nameArray);
		    String functionData = res1.getData().strip();
		    functionData = functionData.replaceAll("\n", " ");
		    functionData = functionData.replaceAll(" +", " ");

		    if (!(functionData.contains("{") || functionData.contains(";")))
		    {
			if (functionData.contains("="))
			{
			    functionData.replaceAll("=", " = ");
			}

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
				Element ifAlternativeNode = document.createElement("alternative");
				Element ifCaseNode = document.createElement("case");
				Element ifConditionNode = document.createElement("condition");
				curNode.appendChild(ifAlternativeNode);
				ifAlternativeNode.appendChild(ifCaseNode);
				ifCaseNode.appendChild(ifConditionNode);

				sourcec = res1.getSourceCode();
				String[] ifNameArray = new String[1];
				ifNameArray[0] = ")";
				TokenResult ifRes = goToTokenWithName(sourcec, ifNameArray);
				ifConditionNode
					.appendChild(document.createTextNode(prefixRBrace[0] + ifRes.getData() + ")"));
				sourcec = ifRes.getSourceCode();
				sourcec = sourcec.substring(1);
				sourcec = sourcec.trim();
				// TODO: if ohne geschweifte Klammern
				if (sourcec.charAt(0) == '{')
				{
				    sourcec = sourcec.substring(1);
				    curlBrace++;
				    curNode = (Element) curNode.getLastChild();
				    System.out.println(curNode.getNodeName());
				    curNode = (Element) curNode.getLastChild();
				    System.out.println(curNode.getNodeName());
				}
				done = true;
				continue;

			    // break;
			    case "for":
				Element forLoopNode = document.createElement("loop");
				Element forConditionNode = document.createElement("condition");

				forLoopNode.appendChild(forConditionNode);

				sourcec = res1.getSourceCode();
				String[] forNameArray = new String[1];
				forNameArray[0] = ")";
				TokenResult forRes = goToTokenWithName(sourcec, forNameArray);
				forConditionNode
					.appendChild(document.createTextNode(prefixRBrace[0] + forRes.getData() + ")"));
				curNode.appendChild(forLoopNode);
				sourcec = forRes.getSourceCode();
				sourcec = sourcec.substring(1);
				sourcec = sourcec.trim();
				// TODO: for ohne geschweifte Klammern
				if (sourcec.charAt(0) == '{')
				{
				    sourcec = sourcec.substring(1);
				    curlBrace++;
				    curNode = (Element) curNode.getLastChild();
				}
				done = true;
				continue;

			    // break;
			    case "while":
				try
				{
				    if (curNode.getLastChild().getFirstChild().getTextContent().equals("do"))
				    {

					sourcec = res1.getSourceCode();
					String[] dowhileNameArray = new String[1];
					dowhileNameArray[0] = ")";
					TokenResult whileRes = goToTokenWithName(sourcec, dowhileNameArray);
					curNode.getLastChild().getFirstChild()
						.setTextContent("do/" + prefixRBrace[0] + whileRes.getData() + ")");

					sourcec = whileRes.getSourceCode();
					sourcec = sourcec.substring(1);
					sourcec = sourcec.trim();
					done = true;
					continue;

				    }
				    else
				    {
					Element whileLoopNode = document.createElement("loop");
					Element whileConditionNode = document.createElement("condition");

					whileLoopNode.appendChild(whileConditionNode);

					sourcec = res1.getSourceCode();
					String[] whileNameArray = new String[1];
					whileNameArray[0] = ")";
					TokenResult whileRes = goToTokenWithName(sourcec, whileNameArray);
					whileConditionNode.appendChild(
						document.createTextNode(prefixRBrace[0] + whileRes.getData() + ")"));
					curNode.appendChild(whileLoopNode);
					sourcec = whileRes.getSourceCode();
					sourcec = sourcec.substring(1);
					sourcec = sourcec.trim();
					if (sourcec.charAt(0) == '{')
					{
					    sourcec = sourcec.substring(1);
					    curlBrace++;
					    curNode = (Element) curNode.getLastChild();
					}
					done = true;
					continue;
				    }
				}
				catch (Exception e)
				{
				    Element whileLoopNode = document.createElement("loop");
				    Element whileConditionNode = document.createElement("condition");

				    whileLoopNode.appendChild(whileConditionNode);

				    sourcec = res1.getSourceCode();
				    String[] whileNameArray = new String[1];
				    whileNameArray[0] = ")";
				    TokenResult whileRes = goToTokenWithName(sourcec, whileNameArray);
				    whileConditionNode.appendChild(
					    document.createTextNode(prefixRBrace[0] + whileRes.getData() + ")"));
				    curNode.appendChild(whileLoopNode);
				    sourcec = whileRes.getSourceCode();
				    sourcec = sourcec.substring(1);
				    sourcec = sourcec.trim();
				    if (sourcec.charAt(0) == '{')
				    {
					sourcec = sourcec.substring(1);
					curlBrace++;
					curNode = (Element) curNode.getLastChild();
				    }
				    done = true;
				    continue;
				}

				// break;
				// TODO: den switch case wegen verschachtelten switch cases überarbeiten
			    case "switch":
				Element switchAlternativeNode = document.createElement("alternative");

				curNode.appendChild(switchAlternativeNode);

				sourcec = res1.getSourceCode();

				String[] switchNameArray = new String[1];
				switchNameArray[0] = ")";
				TokenResult switchRes = goToTokenWithName(sourcec, switchNameArray);

				if (switchCaseOn[curSwitch])
				{
				    curSwitch++;
				}
				switchCaseOn[curSwitch] = true;

				switchCaseCondition[curSwitch] = (prefixRBrace[0] + switchRes.getData() + ")");
				sourcec = switchRes.getSourceCode();

				sourcec = sourcec.substring(1);
				sourcec = sourcec.trim();
				if (sourcec.charAt(0) == '{')
				{
				    sourcec = sourcec.substring(1);
				    curlBrace++;
				    curNode = (Element) curNode.getLastChild();

				}
				else
				{
				    System.out.println("Fehler bei switch");
				}
				done = true;
				continue;

			    // break;
			    default:
				// TODO: muss noch erweitert werden für method(method()) und
				// Object.method1().method2()
				System.out.println("Funktionsaufruf");
				if (prefixRBrace[0].contains("."))
				{

				    String[] methodArray = prefixRBrace[0].split("\\.");

				    Element methodCallNode = document.createElement("methodcall");
				    Element methodInstanceNode = document.createElement("instance");
				    Element methodNode = document.createElement("method");

				    curNode.appendChild(methodCallNode);
				    methodCallNode.appendChild(methodNode);

				    if (methodArray[0].equals("this"))
				    {
					if (prefixRBrace[0].split("\\.").length >= 3)
					{

					    methodCallNode.appendChild(methodInstanceNode);

					    Element validNode = document.createElement("validity");
					    validNode.appendChild(document.createTextNode("class"));
					    methodCallNode.appendChild(validNode);

					    methodNode.appendChild(document.createTextNode(methodArray[2]));
					    methodInstanceNode.appendChild(document.createTextNode(methodArray[1]));
					    sourcec = res1.getSourceCode();
					}
					else
					{

					    prefixRBrace[0] = prefixRBrace[0].substring(5);
					    methodNode.appendChild(document.createTextNode(prefixRBrace[0]));
					    sourcec = res1.getSourceCode();
					}

				    }
				    else
				    {

					methodCallNode.appendChild(methodInstanceNode);
					methodNode.appendChild(document.createTextNode(methodArray[1]));
					methodInstanceNode.appendChild(document.createTextNode(methodArray[0]));
					sourcec = res1.getSourceCode();
				    }

				}
				else
				{

				    Element methodCallNode = document.createElement("methodcall");
				    Element methodNode = document.createElement("method");

				    curNode.appendChild(methodCallNode);
				    methodCallNode.appendChild(methodNode);

				    methodNode.appendChild(document.createTextNode(prefixRBrace[0]));
				    sourcec = res1.getSourceCode();

				}
				break;
			    }
			    break;
			case 2:// Konstruktor oder else if
			    switch (prefixRBrace[0])
			    {
//							case "else":
//
//								break;
//
//							case "new":// Objekterzeugung
//
//								break;
			    case "private":// privater Konstruktor
			    case "public": // Konstruktor

				if (prefixRBrace[1]
					.equals(curNode.getElementsByTagName("name").item(0).getTextContent()))
				{

				    System.out.println("Konstruktor");
				    sourcec = res1.getSourceCode();
				    sourcec = sourcec.substring(1);
				    String[] nameArray2 = new String[2];
				    nameArray2[0] = ")";
				    nameArray2[1] = ",";
				    TokenResult res2;

				    // Aggreagations-eintrag erstellen

				    Element classAggr = (Element) getChildwithName(curNode, "aggregations");
				    Element classComp = (Element) getChildwithName(curNode, "compositions");
				    do
				    {

					res2 = goToTokenWithName(sourcec, nameArray2);
					String functionData2 = res2.getData().strip();

					String[] argumentConstructor = functionData2.split(" ");
					if (argumentConstructor.length == 2)
					{
					    boolean inCompositions = false;

					    for (int i = 0; i < classComp.getElementsByTagName("entry")
						    .getLength(); i++)
					    {
						if (classComp.getElementsByTagName("entry").item(i).getTextContent()
							.equals(argumentConstructor[0]))
						    inCompositions = true;

					    }

					    if (inCompositions == false)
					    {
						boolean inAggregations = false;

						for (int i = 0; i < classAggr.getElementsByTagName("entry")
							.getLength(); i++)
						{
						    if (classAggr.getElementsByTagName("entry").item(i).getTextContent()
							    .equals(argumentConstructor[0]))
							inAggregations = true;

						}
						if (inAggregations == false)
						{
						    Element classCompositionEl = document.createElement("entry");
						    classAggr.appendChild(classCompositionEl);
						    classCompositionEl.appendChild(
							    document.createTextNode(argumentConstructor[0]));
						}
					    }
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
					// Element constNode = document.createElement("constructor");
					// curNode.appendChild(constNode);
					curNode = (Element) curNode.getLastChild();
					done = true;
					continue;

				    }

				}

				break;
			    default:
				System.out.println("Konstruktor oder else if");
				break;
			    }
			    break;
			case 3:// Funktionsdeklaration
			   
			    String[] nameArrayFD = new String[1];
			    nameArrayFD[0] = "{";
			    TokenResult resFD = goToTokenWithName(sourcec, nameArrayFD);
			    String functionDataFD = resFD.getData().strip();

			    if ((prefixRBrace[0].equals("public") || prefixRBrace[0].equals("private"))
				    && !prefixRBrace[1].equals("class") && !functionDataFD.contains(";"))
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

				if (sourcec.substring(0, 6).equals("throws"))
				{
				    while (!(sourcec.charAt(0) == '{'))
				    {
					sourcec = sourcec.substring(1);

				    }
				}
				if (sourcec.charAt(0) == '{')
				{
				    sourcec = sourcec.substring(1);
				    curlBrace++;
				    curNode = (Element) curNode.getLastChild();
				    done = true;
				    continue;
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
				    done = true;
				    continue;

				}

//			    sourcec = res1.getSourceCode();
//			    curNode = (Element) curNode.getLastChild();
				break;
			    }

			    if ((prefixRBrace[1].equals("=") || prefixRBrace[2].equals("new")))
			    {
				// Instance-knoten erstellen

				boolean doneClass = false;

				Element instanceNode = document.createElement("instance");
				curNode.appendChild(instanceNode);
				Element instanceNNode = document.createElement("name");
				instanceNode.appendChild(instanceNNode);
				instanceNNode.appendChild(document.createTextNode(prefixRBrace[0]));
				Element instanceCNode = document.createElement("class");
				instanceNode.appendChild(instanceCNode);
				instanceCNode.appendChild(document.createTextNode(prefixRBrace[3]));
				Element goToClassNode = curNode;
				do
				{
				    if (goToClassNode.getNodeName().equals("classdefinition"))
				    {
					doneClass = true;
					Element classComp = (Element) getChildwithName(goToClassNode, "compositions");
					Element classAggr = (Element) getChildwithName(goToClassNode, "aggregations");

					boolean inCompositions = false;

					for (int i = 0; i < classComp.getElementsByTagName("entry").getLength(); i++)
					{
					    if (classComp.getElementsByTagName("entry").item(i).getTextContent()
						    .equals(prefixRBrace[3]))
					    {

						inCompositions = true;
					    }
					    ;
					}

					for (int i = 0; i < classAggr.getElementsByTagName("entry").getLength(); i++)
					{
					    if (classAggr.getElementsByTagName("entry").item(i).getTextContent()
						    .equals(prefixRBrace[3]))
					    {
						classAggr.removeChild(classAggr.getElementsByTagName("entry").item(i));
					    }
					    ;
					}

					if (inCompositions == false)
					{
					    Element classCompEntry = document.createElement("entry");
					    classCompEntry.appendChild(document.createTextNode(prefixRBrace[3]));
					    classComp.appendChild(classCompEntry);
					    // curNode = (Element) curNode.getLastChild();
					}
				    }
				    goToClassNode = (Element) goToClassNode.getParentNode();

				}
				while (!doneClass);

				String[] newNameArray = new String[1];
				newNameArray[0] = ")";

				TokenResult newRes = goToTokenWithName(sourcec, newNameArray);

				sourcec = newRes.getSourceCode();
				done = true;
				continue;
			    }
			    break;

			default:
			    // System.out.println("Keine Funktion");
			    break;
			}
		    }
		}

		////////////

		// Temporäre Lösung
		// TODO: switch case
		compString = "case";
		if (switchCaseOn[curSwitch])
		{
		    if (sourcec.substring(0, compString.length()).equals(compString))
		    {

			while (!(curNode.getNodeName().equals("alternative") || curNode.getNodeName().equals("source")))
			{
			    if (curNode.getNodeName().equals("alternative"))
			    {
				System.out.println("Fehler bei case");
				break;
			    }
			    curNode = (Element) curNode.getParentNode();
			}

			sourcec = sourcec.substring(compString.length());
			sourcec.trim();

			Element switchCaseNode = document.createElement("case");
			Element switchConditionNode = document.createElement("condition");

			curNode.appendChild(switchCaseNode);
			switchCaseNode.appendChild(switchConditionNode);
			String[] caseNameArray = new String[1];
			caseNameArray[0] = ":";
			TokenResult caseRes = goToTokenWithName(sourcec, caseNameArray);

			switchConditionNode.appendChild(document.createTextNode(
				switchCaseCondition[curSwitch] + "/" + compString + " " + caseRes.getData()));
			sourcec = caseRes.getSourceCode();

			curNode = (Element) curNode.getLastChild();

			done = true;
			continue;

		    }

		}

		compString = "default";
		if (switchCaseOn[curSwitch])
		{
		    if (sourcec.substring(0, compString.length()).equals(compString))
		    {

			while (!(curNode.getNodeName().equals("alternative") || curNode.getNodeName().equals("source")))
			{
			    if (curNode.getNodeName().equals("alternative"))
			    {
				System.out.println("Fehler bei case");
				break;
			    }
			    curNode = (Element) curNode.getParentNode();
			}

			sourcec = sourcec.substring(compString.length());
			sourcec.trim();

			Element switchCaseNode = document.createElement("case");
			Element switchConditionNode = document.createElement("condition");

			curNode.appendChild(switchCaseNode);
			switchCaseNode.appendChild(switchConditionNode);

			switchConditionNode
				.appendChild(document.createTextNode(switchCaseCondition[curSwitch] + "/" + "default"));

			if (sourcec.substring(0, 1).equals(":"))
			{
			    sourcec = sourcec.substring(1);
			}
			else
			{
			    System.out.println("Fehler bei default");
			}
			curNode = (Element) curNode.getLastChild();

			done = true;
			continue;

		    }

		}
		// TODO: entfernen
		/////// nur zum debuggen
		if (sourcec.substring(0, 5).equals("break"))
		{
		    System.out.println("break gefunden");
		}
		///////
		compString = "else";
		if (sourcec.substring(0, compString.length()).equals(compString))
		{
		    sourcec = sourcec.substring(compString.length());

		    sourcec.trim();

		    if (sourcec.substring(0, 2).equals("if"))
		    {

			Element ifCaseNode = document.createElement("case");
			Element ifConditionNode = document.createElement("condition");
			curNode.appendChild(ifCaseNode);
			ifCaseNode.appendChild(ifConditionNode);

			sourcec = sourcec.substring(2);
			String[] ifNameArray = new String[1];
			ifNameArray[0] = ")";
			TokenResult ifRes = goToTokenWithName(sourcec, ifNameArray);
			ifConditionNode.appendChild(document.createTextNode("if" + ifRes.getData() + ")"));
			sourcec = ifRes.getSourceCode();

			sourcec = sourcec.substring(1);
			sourcec = sourcec.trim();
			if (sourcec.charAt(0) == '{')
			{
			    sourcec = sourcec.substring(1);
			    curlBrace++;
			    curNode = (Element) curNode.getLastChild();

			}
			done = true;
			continue;
		    }
		    else
		    {

			Element ifCaseNode = document.createElement("case");
			Element ifConditionNode = document.createElement("condition");
			curNode.appendChild(ifCaseNode);
			ifCaseNode.appendChild(ifConditionNode);

			ifConditionNode.appendChild(document.createTextNode(compString));
			sourcec = sourcec.trim();

			if (sourcec.charAt(0) == '{')
			{
			    sourcec = sourcec.substring(1);
			    curlBrace++;
			    curNode = (Element) curNode.getLastChild();
			    System.out.println(curNode.getNodeName());
			}
			done = true;
			continue;

		    }
		}
		;

		compString = "do";
		if (sourcec.substring(0, compString.length()).equals(compString))
		{
		    String[] nameArray = new String[1];
		    nameArray[0] = "{";
		    TokenResult res = goToTokenWithName(sourcec, nameArray);
		    String afterDo = res.getData().substring(2);
		    if (afterDo.trim().length() == 0)
		    {

			Element doWhileLoopNode = document.createElement("loop");
			Element doWhileConditionNode = document.createElement("condition");

			doWhileLoopNode.appendChild(doWhileConditionNode);

			sourcec = sourcec.substring(2);

			doWhileConditionNode.appendChild(document.createTextNode("do"));
			curNode.appendChild(doWhileLoopNode);

			sourcec = sourcec.trim();
			if (sourcec.charAt(0) == '{')
			{
			    sourcec = sourcec.substring(1);
			    curlBrace++;
			    curNode = (Element) curNode.getLastChild();
			}
			else
			{
			    System.out.println("Fehler bei DoWhile");
			}

			done = true;
			continue;
		    }
		}
		;

		compString = "{";
		if (sourcec.substring(0, compString.length()).equals(compString))
		{
		    sourcec = sourcec.substring(1);
		    curlBrace++;
		    Element somethingWCB = document.createElement("something"); // WCB - with curly brace
		    //somethingWCB.appendChild(document.createTextNode(sourcec.substring(0, 50)));
		    curNode.appendChild(somethingWCB);
		    curNode = (Element) curNode.getLastChild();
		    done = true;
		    continue;
		}
		;
		compString = "}";
		if (sourcec.substring(0, compString.length()).equals(compString))
		{
		    sourcec = sourcec.substring(1);
		    curlBrace--;
		    curNode = (Element) curNode.getParentNode();
		    sourcec = sourcec.trim();
		    if (curNode.getFirstChild().getTextContent().equals("else"))
		    {
			curNode = (Element) curNode.getParentNode();
		    }
		    else if (curNode.getNodeName().equals("alternative") && curNode.getFirstChild().getFirstChild()
			    .getTextContent().substring(0, 6).equals("switch"))
		    {
			switchCaseCondition[curSwitch] = "";
			switchCaseOn[curSwitch] = false;
			if (curSwitch != 0)
			{
			    curSwitch--;
			}
			curNode = (Element) curNode.getParentNode();
		    }
		    else if (!(sourcec.substring(0, 4).equals("else")))
		    {
			if (curNode.getFirstChild().getTextContent().substring(0, 2).equals("if"))
			{
			    curNode = (Element) curNode.getParentNode();
			}
		    }

		    done = true;
		    continue;
		}
		;

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
	// System.out.println(curNode.getNodeName() + " " + curNode.getTextContent());
    }

    /**
     * Liest den �bergebenen Quellcode ein und parsed die Informationen daraus
     * 
     * @param sourceCode Vollstaendiger Java-Quellcode
     */
    public void parse(ArrayList<String> sourceCode)
    {
	sourceCode.get(0).trim();
	// sourceCode = sourceCode.replaceAll("=", " = ");
	System.out.println(sourceCode);
	try
	{
	    buildTree(sourceCode.get(0));
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
