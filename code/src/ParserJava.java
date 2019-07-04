
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Klasse, die den Parser fuer Java implementiert
 */
public class ParserJava extends XmlHelperMethods implements ParserIf
{
    Document document;

    LogMain logger = new LogMain();

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

    // nimmt Methode und gibt Inhalt zwischen runden Klammern wieder
    public TokenResult rBraceContent(String sourcec)
    {
	String[] nameArray = new String[4];
	nameArray[0] = ")";
	nameArray[1] = "(";
	nameArray[2] = "\"";
	nameArray[3] = "'";
	int rBrace = 0;
	String resData = "";
	TokenResult res;

	String conditionCompString = "";

	do
	{
	    conditionCompString = "\"";
	    if (sourcec.startsWith(conditionCompString))
	    {
		resData += sourcec.substring(0, conditionCompString.length());
		sourcec = sourcec.substring(conditionCompString.length());

		String[] conditionNameArray = new String[3];
		conditionNameArray[0] = "\\\\";
		conditionNameArray[1] = "\\\"";
		conditionNameArray[2] = "\"";

		TokenResult conditionCompRes;
		// Gehe durch den Sourcecode, bis kein String mehr gefunden wurde
		do
		{
		    conditionCompRes = goToTokenWithName(sourcec, conditionNameArray);
		    sourcec = conditionCompRes.getSourceCode();
		    resData += conditionCompRes.getData();
		    // System.out.println("@res: " + res.getData());
		    if (conditionCompRes.getFoundToken() != 2)
		    {
			resData += sourcec.substring(0, 2);
			sourcec = sourcec.substring(2);
		    }

		}
		while (conditionCompRes.getFoundToken() != 2);
		resData += sourcec.substring(0, 1);
		sourcec = sourcec.substring(1);
	    }
	    ;
	    conditionCompString = "'";
	    if (sourcec.startsWith(conditionCompString))
	    {
		resData += sourcec.substring(0, conditionCompString.length());
		sourcec = sourcec.substring(conditionCompString.length());

		String[] conditionNameArray = new String[1];
		conditionNameArray[0] = "'";

		TokenResult conditionCompRes;

		conditionCompRes = goToTokenWithName(sourcec, conditionNameArray);
		sourcec = conditionCompRes.getSourceCode();
		resData += conditionCompRes.getData();

		resData += sourcec.substring(0, 1);
		sourcec = sourcec.substring(1);
	    }
	    ;

	    res = goToTokenWithName(sourcec, nameArray);
	    resData += res.getData();

	    if (res.getFoundToken() == 2 || res.getFoundToken() == 3)
	    {
		sourcec = res.getSourceCode();
		continue;
	    }

	    if (res.getFoundToken() == 0)
	    {
		resData += ")";
		rBrace--;
	    }
	    else
	    {
		resData += "(";
		rBrace++;
	    }
	    sourcec = res.getSourceCode().substring(1);
	}
	while (rBrace != 0);

	return new TokenResult(0, resData, sourcec);
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
	document = createDocument();
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

	boolean oneInstructionIf1 = false;
	boolean oneInstructionIf2 = false;

	document.appendChild(root);
	String compString;

	while (!sourcec.isEmpty())
	{
	    try
	    {

		sourcec = sourcec.trim(); // Entfernen von Leerzeichen und Zeilenumbruechen
		boolean done = false;

		compString = "\""; // Entfernen von Strings zur Vermeidung von Problemen beim Entfernen von
		// Kommentaren
		if (sourcec.startsWith(compString))
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
		///// Nur zum debuggen////////////
		compString = "while (!(sourceCode.charAt(i) == '*' && sourceCode.charAt(i + 1) == '/') && i < n)";
		if (sourcec.startsWith(compString))
		{
		    // System.out.println("Debugger hier platzieren");
		    PUMLgenerator.logger.getLog().warning("Debugger hier platzieren");
		}
		/////////////////////
		// Entfernen von Zeilen-Kommentaren
		compString = "//";

		if (sourcec.startsWith(compString))
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
		if (sourcec.startsWith(compString))
		{
		    TokenResult res;
		    sourcec = sourcec.substring(compString.length());
		    sourcec = sourcec.trim();
		    String[] nameArray = new String[1];
		    nameArray[0] = "*/";
		    res = goToTokenWithName(sourcec, nameArray);
		    sourcec = res.getSourceCode().substring(2);
		    done = true;
		    continue;
		}
		;

		compString = "import ";
		if (sourcec.startsWith(compString))
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
		if (sourcec.startsWith(compString))
		{
		    sourcec = sourcec.substring(compString.length());
		    sourcec = deleteComments(sourcec);
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
		    PUMLgenerator.logger.getLog().warning("@className: " + classNameStr);
		    // System.out.println("@className: " + classNameStr);

		    Element classDefinition = document.createElement("classdefinition");
		    Element classNameEl = document.createElement("name");
		    classNameEl.appendChild(document.createTextNode(classNameStr));
		    classDefinition.appendChild(classNameEl);
		    curNode.appendChild(classDefinition);
		    curNode = (Element) curNode.getLastChild();

		    compString = "extends ";
		    if (sourcec.startsWith(compString))
		    {
			sourcec = sourcec.substring(compString.length());
			sourcec = deleteComments(sourcec);
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
			PUMLgenerator.logger.getLog().warning("@extendsName: " + classExtendsStr);
			// System.out.println("@extendsName: " + classExtendsStr);

			Element classExtends = document.createElement("extends");
			Element classExtendsEl = document.createElement("entry");
			classExtendsEl.appendChild(document.createTextNode(classExtendsStr));
			classExtends.appendChild(classExtendsEl);
			classDefinition.appendChild(classExtends);

		    }
		    compString = "implements ";
		    if (sourcec.startsWith(compString))
		    {

			Element classImplements = document.createElement("implements");
			classDefinition.appendChild(classImplements);

			sourcec = sourcec.substring(compString.length());
			boolean curlBraceBool = false;
			// Lösung 1 compString = "{";
			do
			{
			    sourcec = deleteComments(sourcec);
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
			    PUMLgenerator.logger.getLog().warning("@implements: " + classImplementsStr);
			    // System.out.println("@implements: " + classImplementsStr);

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
		if (sourcec.startsWith(compString))
		{
		    sourcec = sourcec.substring(compString.length());
		    sourcec = deleteComments(sourcec);
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
		    PUMLgenerator.logger.getLog().warning("@interfaceName: " + interfaceName);
		    // System.out.println("@interfaceName: " + interfaceName);

		    Element ifDefinition = document.createElement("interfacedefinition");
		    Element ifNameEl = document.createElement("name");

		    // Element classElement = document.createElement("class");
		    ifNameEl.appendChild(document.createTextNode(interfaceName));
		    ifDefinition.appendChild(ifNameEl);
		    curNode.appendChild(ifDefinition);
		    curNode = (Element) curNode.getLastChild();

		    //////// Temporär///////
		    // Name der Klasse/des Interfaces
		    PUMLgenerator.logger.getLog().warning(curNode.getFirstChild().getTextContent());
		    // System.out.println(curNode.getFirstChild().getTextContent());
		    ///////////////////////

		    compString = "extends ";
		    if (sourcec.startsWith(compString))
		    {
			sourcec = sourcec.substring(compString.length());
			sourcec = deleteComments(sourcec);
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
			PUMLgenerator.logger.getLog().warning("@extendsName: " + extendsName);
			// System.out.println("@extendsName: " + extendsName);
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
		if ((!(curNode.getNodeName().equals("source")) && sourcec.contains("(")))
		{

		    String[] nameArray = new String[1];
		    nameArray[0] = "(";

		    TokenResult res1 = goToTokenWithName(sourcec, nameArray);
		    String functionData = res1.getData().strip();
		    functionData = functionData.replaceAll("\n", " ");
		    functionData = functionData.replaceAll(" +", " ");

		    // testen ob leerer Methodcall bei cast verschwindet zB (Dokument)
		    if (functionData.isEmpty())
		    {
			TokenResult emptyWordRes = rBraceContent(sourcec);
			sourcec = emptyWordRes.getSourceCode().substring(1);
		    }

		    if (!(functionData.contains("{") || functionData.contains(";") || functionData.contains("\"")
			    || functionData.contains("}")
			    || (functionData.contains("=") && !functionData.contains("new"))))
		    {
			if (functionData.contains("."))
			{
			    functionData.replaceAll("\\s*\\.\\s*", ".");
			}
			if (functionData.contains("="))
			{
			    functionData.replaceAll("=", " = ");
			}

			String[] prefixRBrace = functionData.split(" ");

			if (!prefixRBrace[0].equals("else"))
			{
			    switch (prefixRBrace.length)
			    {
			    case 0:
				PUMLgenerator.logger.getLog().warning("nichts");
				// System.out.println("nichts");
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

				    TokenResult ifRes = rBraceContent(sourcec);

				    ifConditionNode
					    .appendChild(document.createTextNode(prefixRBrace[0] + ifRes.getData()));

				    sourcec = ifRes.getSourceCode();
				    // sourcec = sourcec.substring(1);
				    sourcec = deleteComments(sourcec);
				    // TODO: if ohne geschweifte Klammern
				    if (sourcec.charAt(0) == '{')
				    {
					sourcec = sourcec.substring(1);
					curlBrace++;
					curNode = (Element) curNode.getLastChild();
					// System.out.println(curNode.getNodeName());
					curNode = (Element) curNode.getLastChild();
					// System.out.println(curNode.getNodeName());
				    }
				    else
				    {
					curNode = (Element) curNode.getLastChild();
					curNode = (Element) curNode.getLastChild();
					oneInstructionIf2 = true;

				    }
				    done = true;
				    continue;

				// break;
				case "catch":

				    sourcec = res1.getSourceCode();

				    TokenResult catchRes = rBraceContent(sourcec);

				    sourcec = catchRes.getSourceCode();
				    sourcec = sourcec.trim();

				    done = true;
				    continue;

				// break;
				case "for":
				    Element forLoopNode = document.createElement("loop");
				    Element forConditionNode = document.createElement("condition");

				    forLoopNode.appendChild(forConditionNode);

				    sourcec = res1.getSourceCode();
				    TokenResult forRes = rBraceContent(sourcec);

				    forConditionNode
					    .appendChild(document.createTextNode(prefixRBrace[0] + forRes.getData()));
				    curNode.appendChild(forLoopNode);

				    sourcec = forRes.getSourceCode();
				    sourcec = sourcec.substring(1);
				    sourcec = deleteComments(sourcec);

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

					    TokenResult whileRes = rBraceContent(sourcec);

					    curNode.getLastChild().getFirstChild()
						    .setTextContent("do/" + prefixRBrace[0] + whileRes.getData());

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
					    TokenResult whileRes = rBraceContent(sourcec);
					    whileConditionNode.appendChild(
						    document.createTextNode(prefixRBrace[0] + whileRes.getData()));
					    curNode.appendChild(whileLoopNode);
					    sourcec = whileRes.getSourceCode();
					    sourcec = sourcec.substring(1);
					    sourcec = deleteComments(sourcec);
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
					PUMLgenerator.logger.getLog().warning("@ParserJava: " + e.toString());
					Element whileLoopNode = document.createElement("loop");
					Element whileConditionNode = document.createElement("condition");

					whileLoopNode.appendChild(whileConditionNode);

					sourcec = res1.getSourceCode();
					TokenResult whileRes = rBraceContent(sourcec);
					whileConditionNode.appendChild(
						document.createTextNode(prefixRBrace[0] + whileRes.getData()));
					curNode.appendChild(whileLoopNode);
					sourcec = whileRes.getSourceCode();
					sourcec = sourcec.substring(1);
					sourcec = deleteComments(sourcec);
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

				    TokenResult switchRes = rBraceContent(sourcec);

				    if (switchCaseOn[curSwitch])
				    {
					curSwitch++;
				    }
				    switchCaseOn[curSwitch] = true;

				    switchCaseCondition[curSwitch] = (prefixRBrace[0] + switchRes.getData());
				    sourcec = switchRes.getSourceCode();

				    sourcec = sourcec.substring(1);
				    sourcec = deleteComments(sourcec);
				    if (sourcec.charAt(0) == '{')
				    {
					sourcec = sourcec.substring(1);
					curlBrace++;
					curNode = (Element) curNode.getLastChild();

				    }
				    else
				    {
					PUMLgenerator.logger.getLog().warning("Fehler bei Switch");
					// System.out.println("Fehler bei switch");
				    }
				    done = true;
				    continue;

				// break;
				default:
				    // TODO: muss noch erweitert werden für method(method()) und
				    // Object.method1().method2()
				    PUMLgenerator.logger.getLog().warning("Funktionsaufruf");
				    // System.out.println("Funktionsaufruf");
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
					    }
					    else
					    {

						prefixRBrace[0] = prefixRBrace[0].substring(5);
						methodNode.appendChild(document.createTextNode(prefixRBrace[0]));
					    }

					}
					else
					{

					    methodCallNode.appendChild(methodInstanceNode);
					    methodNode.appendChild(document.createTextNode(methodArray[1]));
					    methodInstanceNode.appendChild(document.createTextNode(methodArray[0]));

					}
				    }
				    else
				    {

					Element methodCallNode = document.createElement("methodcall");
					Element methodNode = document.createElement("method");

					curNode.appendChild(methodCallNode);
					methodCallNode.appendChild(methodNode);

					methodNode.appendChild(document.createTextNode(prefixRBrace[0]));

				    }
				    sourcec = res1.getSourceCode();
				    if (sourcec.charAt(0) == '(')
				    {
					TokenResult methodcallRes = rBraceContent(sourcec);
					sourcec = methodcallRes.getSourceCode();
				    }
				    else
				    {
					sourcec.substring(1);
					PUMLgenerator.logger.getLog().warning("Fehler bei Funktionsaufruf mit .");
					// System.out.println("Fehler bei Funktionsaufruf mit .");
				    }

				    while (!done)
				    {
					sourcec = deleteComments(sourcec);
					if (sourcec.charAt(0) == '.')
					{
					    sourcec = sourcec.substring(1);

					    String[] moreMethodsNameArray = new String[1];
					    nameArray[0] = "(";

					    TokenResult moreMethodsRes = goToTokenWithName(sourcec, nameArray);
					    sourcec = moreMethodsRes.getSourceCode();

					    Element methodNode = document.createElement("method");
					    methodNode.appendChild(document.createTextNode(moreMethodsRes.getData()));
					    curNode.getLastChild().appendChild(methodNode);

					    if (sourcec.charAt(0) == '(')
					    {
						TokenResult moreMethods = rBraceContent(sourcec);
						sourcec = moreMethods.getSourceCode();
					    }
					    else
					    {
						PUMLgenerator.logger.getLog()
							.warning("Fehler bei Funktionsaufruf mit .");
						// System.out.println("Fehler bei Funktionsaufruf mit .");
					    }

					}
					else
					{
					    done = true;
					}
				    }

				    continue;
				}
			    case 2:
				if (!prefixRBrace[0].contains(".") && !prefixRBrace[1].contains("."))
				{
				    Element methoddefinitionNode = document.createElement("methoddefinition");

				    Element accessNode = document.createElement("access");

				    Element nameNode = document.createElement("name");
				    nameNode.appendChild(document.createTextNode(prefixRBrace[1]));

				    methoddefinitionNode.appendChild(accessNode);
				    methoddefinitionNode.appendChild(nameNode);

				    curNode.appendChild(methoddefinitionNode);

				    Element parametersNode = document.createElement("parameters");

				    sourcec = res1.getSourceCode();
				    sourcec = sourcec.substring(1);
				    String[] nameArray2 = new String[2];
				    nameArray2[0] = ")";
				    nameArray2[1] = ",";
				    TokenResult res2;

				    if ((prefixRBrace[0].equals("public") || prefixRBrace[0].equals("private"))
					    && prefixRBrace[1].equals(
						    curNode.getElementsByTagName("name").item(0).getTextContent()))
				    {

					accessNode.appendChild(document.createTextNode(prefixRBrace[0]));

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
						Element entryPNode = document.createElement("entry");
						Element typePNode = document.createElement("type");
						Element namePNode = document.createElement("name");
						typePNode.appendChild(document.createTextNode(argumentConstructor[0]));
						namePNode.appendChild(document.createTextNode(argumentConstructor[1]));
						entryPNode.appendChild(typePNode);
						entryPNode.appendChild(namePNode);
						parametersNode.appendChild(entryPNode);

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
							if (classAggr.getElementsByTagName("entry").item(i)
								.getTextContent().equals(argumentConstructor[0]))
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
					if (parametersNode.hasChildNodes())
					    methoddefinitionNode.appendChild(parametersNode);

				    }
				    else
				    {
					accessNode.appendChild(document.createTextNode("pprivate"));
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

					Element resultNode = document.createElement("result");
					resultNode.appendChild(document.createTextNode(prefixRBrace[0]));
					methoddefinitionNode.appendChild(resultNode);

				    }
				    sourcec = deleteComments(sourcec);
				    if (sourcec.substring(0, 6).equals("throws"))
				    {
					while (!(sourcec.charAt(0) == '{'))
					{
					    sourcec = sourcec.substring(1);

					}
				    }
				    else if (sourcec.charAt(0) == '{')
				    {
					sourcec = sourcec.substring(1);
					curlBrace++;
					curNode = (Element) curNode.getLastChild();
					done = true;
					continue;

				    }
				}
				break;

			    case 3:// Funktionsdeklaration

				String[] nameArrayFD = new String[1];
				nameArrayFD[0] = "{";
				TokenResult resFD = goToTokenWithName(sourcec, nameArrayFD);
				String functionDataFD = resFD.getData().strip();
				// finden des Bugs beim rausspringen hier
				if ((prefixRBrace[0].equals("public") || prefixRBrace[0].equals("private")
					|| prefixRBrace[0].equals("protected")) && !prefixRBrace[1].equals("class"))
				// rausgenommen, weil manche Deklarationen keinen Koerper haben
				// && !functionDataFD.contains(";"))
				{
				    Element methoddefinitionNode = document.createElement("methoddefinition");

				    Element accessNode = document.createElement("access");
				    accessNode.appendChild(document.createTextNode(prefixRBrace[0]));

				    Element nameNode = document.createElement("name");
				    nameNode.appendChild(document.createTextNode(prefixRBrace[2]));

				    methoddefinitionNode.appendChild(accessNode);
				    methoddefinitionNode.appendChild(nameNode);

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

				    Element resultNode = document.createElement("result");
				    resultNode.appendChild(document.createTextNode(prefixRBrace[1]));
				    methoddefinitionNode.appendChild(resultNode);

				    sourcec = deleteComments(sourcec);

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
				    Element methoddefinitionNode1 = document.createElement("methoddefinition");

				    Element accessNode1 = document.createElement("access");
				    accessNode1.appendChild(document.createTextNode(prefixRBrace[0]));

				    Element nameNode1 = document.createElement("name");
				    nameNode1.appendChild(document.createTextNode(prefixRBrace[3]));

				    Element typeNode = document.createElement("type");
				    typeNode.appendChild(document.createTextNode(prefixRBrace[1]));

				    methoddefinitionNode1.appendChild(accessNode1);
				    methoddefinitionNode1.appendChild(nameNode1);
				    methoddefinitionNode1.appendChild(typeNode);

				    curNode.appendChild(methoddefinitionNode1);
				    // ...
				    // curNode = (Element) curNode.getLastChild();

				    // Parameter der Funktion bestimmen
				    sourcec = res1.getSourceCode();
				    sourcec = sourcec.substring(1);
				    String[] nameArray3 = new String[2];
				    nameArray3[0] = ")";
				    nameArray3[1] = ",";
				    TokenResult res3;
				    Element parametersNode1 = document.createElement("parameters");
				    do
				    {

					res3 = goToTokenWithName(sourcec, nameArray3);
					String functionData2 = res3.getData().strip();

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
					    parametersNode1.appendChild(entryPNode);
					}
					sourcec = res3.getSourceCode();
					sourcec = sourcec.substring(1);
				    }
				    while (res3.getFoundToken() != 0);

				    if (parametersNode1.hasChildNodes())
					methoddefinitionNode1.appendChild(parametersNode1);

				    Element resultNode = document.createElement("result");
				    resultNode.appendChild(document.createTextNode(prefixRBrace[2]));
				    methoddefinitionNode1.appendChild(resultNode);

				    sourcec = deleteComments(sourcec);
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
//				break;
				}
				break;
			    case 5:
				if ((prefixRBrace[2].equals("=") || prefixRBrace[3].equals("new")))
				{
				    // Instance-knoten erstellen

				    boolean doneClass = false;

				    Element instanceNode = document.createElement("instance");
				    curNode.appendChild(instanceNode);

				    Element instanceANode = document.createElement("access");
				    instanceANode.appendChild(document.createTextNode("pprivate"));

				    Element instanceNNode = document.createElement("name");
				    instanceNNode.appendChild(document.createTextNode(prefixRBrace[1]));

				    Element instanceCNode = document.createElement("class");
				    instanceCNode.appendChild(document.createTextNode(prefixRBrace[4]));

				    instanceNode.appendChild(instanceANode);
				    instanceNode.appendChild(instanceNNode);
				    instanceNode.appendChild(instanceCNode);

				    Element goToClassNode = curNode;
				    do
				    {
					if (goToClassNode.getNodeName().equals("classdefinition"))
					{
					    doneClass = true;
					    Element classComp = (Element) getChildwithName(goToClassNode,
						    "compositions");
					    Element classAggr = (Element) getChildwithName(goToClassNode,
						    "aggregations");

					    boolean inCompositions = false;

					    for (int i = 0; i < classComp.getElementsByTagName("entry")
						    .getLength(); i++)
					    {
						if (classComp.getElementsByTagName("entry").item(i).getTextContent()
							.equals(prefixRBrace[4]))
						{

						    inCompositions = true;
						}
						;
					    }

					    for (int i = 0; i < classAggr.getElementsByTagName("entry")
						    .getLength(); i++)
					    {
						if (classAggr.getElementsByTagName("entry").item(i).getTextContent()
							.equals(prefixRBrace[4]))
						{
						    classAggr.removeChild(
							    classAggr.getElementsByTagName("entry").item(i));
						}
						;
					    }

					    if (inCompositions == false)
					    {
						Element classCompEntry = document.createElement("entry");
						classCompEntry.appendChild(document.createTextNode(prefixRBrace[4]));
						classComp.appendChild(classCompEntry);
						// curNode = (Element) curNode.getLastChild();
					    }
					}
//				    goToClassNode = (Element) goToClassNode.getParentNode();
					goToClassNode = (Element) getList(goToClassNode, "..").item(0);

				    }
				    while (!doneClass);

				    sourcec = res1.getSourceCode();
				    TokenResult newRes = rBraceContent(sourcec);

				    sourcec = newRes.getSourceCode();
				    done = true;
				    continue;
				}
				break;
			    case 6:
				if ((prefixRBrace[3].equals("=") || prefixRBrace[4].equals("new")))
				{
				    // Instance-knoten erstellen

				    boolean doneClass = false;

				    Element instanceNode = document.createElement("instance");
				    curNode.appendChild(instanceNode);

				    Element instanceANode = document.createElement("access");
				    instanceANode.appendChild(document.createTextNode(prefixRBrace[0]));

				    Element instanceNNode = document.createElement("name");
				    instanceNNode.appendChild(document.createTextNode(prefixRBrace[2]));

				    Element instanceCNode = document.createElement("class");
				    instanceCNode.appendChild(document.createTextNode(prefixRBrace[5]));

				    instanceNode.appendChild(instanceANode);
				    instanceNode.appendChild(instanceNNode);
				    instanceNode.appendChild(instanceCNode);

				    Element goToClassNode = curNode;
				    do
				    {
					if (goToClassNode.getNodeName().equals("classdefinition"))
					{
					    doneClass = true;
					    Element classComp = (Element) getChildwithName(goToClassNode,
						    "compositions");
					    Element classAggr = (Element) getChildwithName(goToClassNode,
						    "aggregations");

					    boolean inCompositions = false;

					    for (int i = 0; i < classComp.getElementsByTagName("entry")
						    .getLength(); i++)
					    {
						if (classComp.getElementsByTagName("entry").item(i).getTextContent()
							.equals(prefixRBrace[5]))
						{

						    inCompositions = true;
						}
						;
					    }

					    for (int i = 0; i < classAggr.getElementsByTagName("entry")
						    .getLength(); i++)
					    {
						if (classAggr.getElementsByTagName("entry").item(i).getTextContent()
							.equals(prefixRBrace[5]))
						{
						    classAggr.removeChild(
							    classAggr.getElementsByTagName("entry").item(i));
						}
						;
					    }

					    if (inCompositions == false)
					    {
						Element classCompEntry = document.createElement("entry");
						classCompEntry.appendChild(document.createTextNode(prefixRBrace[5]));
						classComp.appendChild(classCompEntry);
						// curNode = (Element) curNode.getLastChild();
					    }
					}
//				    goToClassNode = (Element) goToClassNode.getParentNode();
					goToClassNode = (Element) getList(goToClassNode, "..").item(0);

				    }
				    while (!doneClass);
				    sourcec = res1.getSourceCode();
				    TokenResult newRes = rBraceContent(sourcec);

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
		}

		////////////

		// Temporäre Lösung
		// TODO: switch case
		compString = "case";
		if (switchCaseOn[curSwitch])
		{
		    if (sourcec.startsWith(compString))
		    {

			while (!(curNode.getNodeName().equals("alternative") || curNode.getNodeName().equals("source")))
			{
			    if (curNode.getNodeName().equals("alternative"))
			    {
				PUMLgenerator.logger.getLog().warning("Fehler bei case");
				// System.out.println("Fehler bei case");
				break;
			    }
//			    curNode = (Element) curNode.getParentNode();
			    curNode = (Element) getList(curNode, "..").item(0);
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
			sourcec = sourcec.substring(1);

			curNode = (Element) curNode.getLastChild();

			done = true;
			continue;

		    }

		}

		compString = "default";
		if (switchCaseOn[curSwitch])
		{
		    if (sourcec.startsWith(compString))
		    {

			while (!(curNode.getNodeName().equals("alternative") || curNode.getNodeName().equals("source")))
			{
			    if (curNode.getNodeName().equals("alternative"))
			    {
				PUMLgenerator.logger.getLog().warning("Fehler bei case");
				// System.out.println("Fehler bei case");
				break;
			    }
//			    curNode = (Element) curNode.getParentNode();
			    curNode = (Element) getList(curNode, "..").item(0);
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
			    PUMLgenerator.logger.getLog().warning("Fehler bei default");
			    // System.out.println("Fehler bei default");
			}
			curNode = (Element) curNode.getLastChild();

			done = true;
			continue;

		    }

		}
		// TODO: entfernen
		/////// nur zum debuggen

		if (sourcec.startsWith("break"))
		{
		    PUMLgenerator.logger.getLog().warning("break gefunden");
		    // System.out.println("break gefunden");
		}
		///////
		compString = "else";
		if (sourcec.startsWith(compString))
		{
		    sourcec = sourcec.substring(compString.length());

		    sourcec = sourcec.trim();

		    if (sourcec.startsWith("if"))
		    {

			Element ifCaseNode = document.createElement("case");
			Element ifConditionNode = document.createElement("condition");
			curNode.appendChild(ifCaseNode);
			ifCaseNode.appendChild(ifConditionNode);

			sourcec = sourcec.substring(2);

			TokenResult ifRes = rBraceContent(sourcec);
			ifConditionNode.appendChild(document.createTextNode("if" + ifRes.getData()));
			sourcec = ifRes.getSourceCode();

			// sourcec = sourcec.substring(1);
			sourcec = deleteComments(sourcec);
			if (sourcec.charAt(0) == '{')
			{
			    sourcec = sourcec.substring(1);
			    curlBrace++;
			    curNode = (Element) curNode.getLastChild();

			}
			else
			{
			    curNode = (Element) curNode.getLastChild();
			    oneInstructionIf1 = true;

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

			sourcec = deleteComments(sourcec);

			if (sourcec.charAt(0) == '{')
			{
			    sourcec = sourcec.substring(1);
			    curlBrace++;
			    curNode = (Element) curNode.getLastChild();
			    PUMLgenerator.logger.getLog().warning(curNode.getNodeName());
			    // System.out.println(curNode.getNodeName());
			}
			else
			{
			    curNode = (Element) curNode.getLastChild();
			    oneInstructionIf1 = true;

			}
			done = true;
			continue;

		    }
		}
		;

		compString = "do";
		if (sourcec.startsWith(compString))
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

			sourcec = deleteComments(sourcec);
			if (sourcec.charAt(0) == '{')
			{
			    sourcec = sourcec.substring(1);
			    curlBrace++;
			    curNode = (Element) curNode.getLastChild();
			}
			else
			{
			    PUMLgenerator.logger.getLog().warning("Fehler bei DoWhile");
			    // System.out.println("Fehler bei DoWhile");
			}

			done = true;
			continue;
		    }
		}
		;

		compString = "{";
		if (sourcec.startsWith(compString))
		{
		    sourcec = sourcec.substring(1);
		    curlBrace++;
		    Element frameNode = document.createElement("frame"); // WCB - with curly brace
		    // somethingWCB.appendChild(document.createTextNode(sourcec.substring(0, 50)));
		    curNode.appendChild(frameNode);
		    curNode = (Element) curNode.getLastChild();
		    done = true;
		    continue;
		}
		;
		compString = "}";
		if (sourcec.startsWith(compString))
		{
		    sourcec = sourcec.substring(1);
		    curlBrace--;
//		    curNode = (Element) curNode.getParentNode();
		    curNode = (Element) getList(curNode, "..").item(0);

		    sourcec = deleteComments(sourcec);

		    if (curNode.getLastChild().getFirstChild().getTextContent().equals("else"))
		    {
//			curNode = (Element) curNode.getParentNode();
			curNode = (Element) getList(curNode, "..").item(0);
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
//			curNode = (Element) curNode.getParentNode();
			curNode = (Element) getList(curNode, "..").item(0);
		    }
		    else if (!sourcec.startsWith("else"))

		    {
			if (curNode.getNodeName().equals("alternative")
				&& curNode.getFirstChild().getFirstChild().getTextContent().startsWith("if"))
			{
//			    curNode = (Element) curNode.getParentNode();
			    curNode = (Element) getList(curNode, "..").item(0);
			}

		    }

		    done = true;
		    continue;
		}
		;

		if (curNode.getNodeName().equals("classdefinition") && !(sourcec.charAt(0) == ';'))
		{
		    // TODO: var einlesen

		    String[] nameArray = new String[1];
		    nameArray[0] = ";";

		    TokenResult varRes = goToTokenWithName(sourcec, nameArray);

		    String varData = varRes.getData().strip();
		    varData = varData.replaceAll("\n", " ");
		    varData = varData.replaceAll(" +", " ");

		    String[] pureVarSplit =
		    { ";", ";", ";" };
		    boolean splitDone = false;

		    if (varData.contains("="))
		    {
			varData.replaceAll("=", " = ");
			if (varData.contains(" new "))
			{
			    sourcec = sourcec.substring(1);
			    continue;
			}
			String[] varSplit = varData.split(" ");
			for (int i = 0; i < varSplit.length; i++)
			{
			    if (varSplit[i].equals("="))
			    {
				if (i <= 3)
				{
				    for (int j = 0; j < i; j++)
				    {
					pureVarSplit[j] = varSplit[j];
				    }
				    splitDone = true;
				}
				else
				{
				    PUMLgenerator.logger.getLog().warning("Fehler bei Klassen-Variablen erkennen");
				    // System.out.println("Fehler bei Klassen-Variablen erkennen");
				}

			    }
			}

		    }

		    String[] varSplit = varData.split(" ");
		    if ((varSplit.length <= 3) && !splitDone)
		    {
			for (int j = 0; j < varSplit.length; j++)
			{
			    pureVarSplit[j] = varSplit[j];
			}

		    }
		    else
		    {
			PUMLgenerator.logger.getLog().warning("Fehler bei Klassen-Variablen erkennen");
			// System.out.println("Fehler bei Klassen-Variablen erkennen");
			sourcec = sourcec.substring(1);
			continue;
		    }
		    String[] varTypeArray =
		    { "int", "boolean", "char", "byte", "short", "long", "float", "double" };
		    boolean createVarNode = false;
		    int typeNumber = -1;

		    if (pureVarSplit[2].equals(";"))
		    {

			if (pureVarSplit[1].equals(";"))
			{
			    sourcec = varRes.getSourceCode();
			    continue;
			}

			for (int i = 0; i < varTypeArray.length; i++)
			{
			    if (varTypeArray[i].equals(pureVarSplit[0]))
			    {
				createVarNode = true;
				typeNumber = i;
			    }
			}
			if (createVarNode)
			{
			    Element varMainNode = document.createElement("var");
			    Element varNameNode = document.createElement("name");
			    Element varTypeNode = document.createElement("type");
			    Element varAccessNode = document.createElement("access");

			    varMainNode.appendChild(varTypeNode);
			    varMainNode.appendChild(varNameNode);
			    varMainNode.appendChild(varAccessNode);
			    curNode.appendChild(varMainNode);

			    varTypeNode.appendChild(document.createTextNode(varTypeArray[typeNumber]));
			    varNameNode.appendChild(document.createTextNode(pureVarSplit[1]));
			    varAccessNode.appendChild(document.createTextNode("pprivate"));

			}
			else
			{

			    Element instanceMainNode = document.createElement("instance");
			    Element instanceAccessNode = document.createElement("access");
			    Element instanceNameNode = document.createElement("name");
			    Element instanceClassNode = document.createElement("class");

			    instanceAccessNode.appendChild(document.createTextNode("pprivate"));
			    instanceNameNode.appendChild(document.createTextNode(pureVarSplit[1]));
			    instanceClassNode.appendChild(document.createTextNode(pureVarSplit[0]));

			    instanceMainNode.appendChild(instanceAccessNode);
			    instanceMainNode.appendChild(instanceNameNode);
			    instanceMainNode.appendChild(instanceClassNode);
			    curNode.appendChild(instanceMainNode);

			}
			sourcec = varRes.getSourceCode();
			continue;

		    }
		    else
		    {
			for (int i = 0; i < varTypeArray.length; i++)
			{
			    if (varTypeArray[i].equals(pureVarSplit[1]))
			    {
				createVarNode = true;
				typeNumber = i;
			    }
			}

			if (createVarNode)
			{
			    Element varMainNode = document.createElement("var");
			    Element varNameNode = document.createElement("name");
			    Element varAccessNode = document.createElement("access");
			    Element varTypeNode = document.createElement("type");

			    varAccessNode.appendChild(document.createTextNode(pureVarSplit[0]));
			    varTypeNode.appendChild(document.createTextNode(varTypeArray[typeNumber]));
			    varNameNode.appendChild(document.createTextNode(pureVarSplit[2]));

			    varMainNode.appendChild(varAccessNode);
			    varMainNode.appendChild(varTypeNode);
			    varMainNode.appendChild(varNameNode);
			    curNode.appendChild(varMainNode);
			}
			else
			{

			    Element instanceMainNode = document.createElement("instance");
			    Element instanceNameNode = document.createElement("name");
			    Element instanceClassNode = document.createElement("class");
			    Element instanceAccessNode = document.createElement("access");

			    instanceNameNode.appendChild(document.createTextNode(pureVarSplit[2]));
			    instanceClassNode.appendChild(document.createTextNode(pureVarSplit[1]));
			    instanceAccessNode.appendChild(document.createTextNode(pureVarSplit[0]));

			    instanceMainNode.appendChild(instanceAccessNode);
			    instanceMainNode.appendChild(instanceNameNode);
			    instanceMainNode.appendChild(instanceClassNode);
			    curNode.appendChild(instanceMainNode);
			}
			sourcec = varRes.getSourceCode();
			continue;

		    }

		}
		if (sourcec.startsWith(";"))
		{
		    if (oneInstructionIf1)
		    {
			curNode = (Element) getList(curNode, "..").item(0);
			oneInstructionIf1 = false;
		    }
		    else if (oneInstructionIf2)
		    {
			curNode = (Element) getList(curNode, "..").item(0);
			curNode = (Element) getList(curNode, "..").item(0);
			oneInstructionIf2 = false;
		    }

		}

		if (!done)
		{
		    sourcec = sourcec.substring(1);
		}
	    }
	    catch (StringIndexOutOfBoundsException e)
	    {

		PUMLgenerator.logger.getLog().warning("@ParserJava: " + e.toString());
		// System.out.println(e.getCause());
		boolean sourceEnd = true;
		if (sourcec.length() <= 10)
		{
		    for (int i = 0; i < sourcec.length(); i++)
		    {
			if (sourcec.charAt(i) != ';')
			{
			    sourceEnd = false;
			}
		    }

		}
		if (!sourceEnd)
		{
		    sourcec = sourcec + ";;;;;;;;;;;";
		}
		else
		{
		    sourcec = "";
		}
		PUMLgenerator.logger.getLog().warning("Source erweitert");
		// System.out.println("Source erweitert");

	    }
	    catch (java.lang.ClassCastException e)
	    {
		String pos = "";
		if (sourcec.length() > 10)
		    pos = sourcec.substring(0, 10);
		else
		    pos = sourcec;
		PUMLgenerator.logger.getLog()
			.warning("@ParserJava: Fehler in der While: " + e.toString() + " bei Position: " + pos);
		// System.out.println("Fehler in der While: " + e.toString());
		// System.out.println("Bei Position: " + sourcec.substring(0, 10));

		if (!sourcec.isEmpty())
		{
		    String[] excNameArray = new String[1];
		    excNameArray[0] = ";";
		    TokenResult excRes = goToTokenWithName(sourcec, excNameArray);

		    sourcec = excRes.getSourceCode();
		}
	    }
	    catch (Exception e)
	    {
		String pos = "";
		if (sourcec.length() > 10)
		    pos = sourcec.substring(0, 10);
		else
		    pos = sourcec;
		PUMLgenerator.logger.getLog()
			.warning("@ParserJava: Fehler in der While: " + e.toString() + " bei Position: " + pos);
		// System.out.println("Fehler in der While: " + e.toString());
		// System.out.println("Bei Position: " + sourcec.substring(0, 10));

	    }
	}

	TransformerFactory tf = TransformerFactory.newInstance();
	Transformer transformer;
	try
	{
	    transformer = tf.newTransformer();
	    StringWriter writer = new StringWriter();

	    transformer.transform(new DOMSource(document), new StreamResult(writer));

	    String xmlString = writer.getBuffer().toString();
	    PUMLgenerator.logger.getLog().warning(xmlString);
	    // System.out.println(xmlString); // Print to console or logs

	    String home;
	    home = System.getProperty("user.home");
	    File pumlDir = new File(home + "/tempLogger");

	    String path = home + "/tempLogger/" + "PUMLlog.xml";

	    if (!pumlDir.exists())
	    {

		try
		{
		    pumlDir.mkdir();
		}
		catch (SecurityException se)
		{
		    PUMLgenerator.logger.getLog().warning("@ParserJava: " + se.toString());
		    // handle it
		}
	    }
	    File tempLogger = new File(path);
	    try
	    {
		FileWriter fileWriter = new FileWriter(tempLogger);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.print(xmlString);
		printWriter.close();
	    }
	    catch (Exception e)
	    {
		PUMLgenerator.logger.getLog().warning("@ParserJava: " + e.toString());
		// TODO Auto-generated catch block
		// e.printStackTrace();
	    }

	}
	catch (TransformerException e)
	{
	    // e.printStackTrace();
	    PUMLgenerator.logger.getLog().warning("@ParserJava: " + e.toString());
	}
	catch (Exception e)
	{
	    PUMLgenerator.logger.getLog().warning("@ParserJava: " + e.toString());
	    // e.printStackTrace();
	}
	// System.out.println(document.getTextContent());
	// System.out.println(curNode.getNodeName() + " " + curNode.getTextContent());
    }

    private String deleteComments(String sourcec)
    {
	// TODO Auto-generated method stub

	// Entfernen von Zeilen-Kommentaren
	boolean foundComment = true;
	while (foundComment)
	{
	    foundComment = false;
	    sourcec = sourcec.trim();
	    String compString = "//";
	    if (sourcec.startsWith(compString))
	    {
		TokenResult res;
		sourcec = sourcec.substring(compString.length());
		String[] nameArray = new String[1];
		nameArray[0] = "\n";
		res = goToTokenWithName(sourcec, nameArray);
		sourcec = res.getSourceCode();
		// .substring(2);
		foundComment = true;
	    }
	    ;

	    // Entfernen von Block-Kommentaren
	    compString = "/*";
	    if (sourcec.startsWith(compString))
	    {
		TokenResult res;
		sourcec = sourcec.substring(compString.length());
		sourcec = sourcec.trim();
		String[] nameArray = new String[1];
		nameArray[0] = "*/";
		res = goToTokenWithName(sourcec, nameArray);
		sourcec = res.getSourceCode().substring(2);
		foundComment = true;
	    }
	    ;
	}

	return sourcec;
    }

    /**
     * Liest den uebergebenen Quellcode ein und parsed die Informationen daraus
     *
     * @param sourceCode Vollstaendiger Java-Quellcode
     */
    public void parse(ArrayList<String> sourceCode)
    {
	sourceCode.get(0).trim();
	// sourceCode = sourceCode.replaceAll("=", " = ");
	// System.out.println(sourceCode);
	StringWriter writer = new StringWriter();
	String xmlString = writer.getBuffer().toString();
	PUMLgenerator.logger.getLog().warning(xmlString);
	// System.out.println(xmlString); // Print to console or logs

	String home;
	home = System.getProperty("user.home");
	File pumlDir = new File(home + "/tempLogger");

	String path = home + "/tempLogger/" + "PUMLsource.dat";

	if (!pumlDir.exists())
	{

	    try
	    {
		pumlDir.mkdir();
	    }
	    catch (SecurityException se)
	    {
		PUMLgenerator.logger.getLog().warning("@ParserJava: " + se.toString());
		// handle it
	    }
	}

	File tempLogger = new File(path);

	try
	{
	    FileWriter fileWriter = new FileWriter(tempLogger);
	    PrintWriter printWriter = new PrintWriter(fileWriter);
	    printWriter.print(sourceCode);
	    printWriter.close();
	}
	catch (Exception e)
	{
	    PUMLgenerator.logger.getLog().warning("@ParserJava: " + e.toString());
	    // TODO Auto-generated catch block
	    // e.printStackTrace();
	}

	try
	{
	    buildTree(sourceCode.get(0));

	}
	catch (ParserConfigurationException e)
	{
	    PUMLgenerator.logger.getLog().warning("@ParserJava: " + e.toString());
	    // TODO Auto-generated catch block
	    // e.printStackTrace();
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
