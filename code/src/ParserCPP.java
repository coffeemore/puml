import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ParserCPP implements ParserIf
{
    private Document document;
    private XmlHelperMethods xmlHelper = new XmlHelperMethods();
    private int[] classInterval =
    { 0, 0 };

    /**
     * leerer Konstruktor
     */
    public ParserCPP()
    {

    }

    // Getter- und Settermethoden
    public Document getDocument()
    {
	return document;
    }

    public void setDocument(Document document)
    {
	this.document = document;
    }

    // Here is where the magic happens:

    /**
     * Entfernt Kommentare aus uebergebenem String und erstellt eine passende
     * XML-Datei
     * 
     * @param sourceCode uebergebener String aus dem die Kommentare entfernt werden
     *                   sollen
     * @return XMl-Dokument
     * @throws ParserConfigurationException
     */
    private void buildTree(ArrayList<String> code) throws ParserConfigurationException
    {
	// Auskommentieren und Trimmen
	String sourceCodeHPP = deleteComStr(code.get(0));
	String sourceCodeCPP = deleteComStr(code.get(1));
	sourceCodeHPP = sourceCodeHPP.trim();
	sourceCodeCPP = sourceCodeCPP.trim();

	//XML-Dokument erstellen
	Document document = xmlHelper.createDocument();
	
	//Root-Knoten im XML-Baum erstellen
	Element root = document.createElement("source");
	document.appendChild(root);

	// Suche nach dem Index vom Wort "class" im HPP-Code
	String keyword = "class ";
	int index = sourceCodeHPP.indexOf(keyword);

	while (index >= 0)
	{
	    // Index vom Ende des Klassennamens erfassen welches mit Leerzeichen oder
	    // Zeilenumbruch endet
	    int b = Math.min(sourceCodeHPP.indexOf(" ", index + keyword.length()),
		    sourceCodeHPP.indexOf("\n", index + keyword.length()));

	    // Ausgabe des Folgewortes von "class"
	    System.out.println(sourceCodeHPP.substring(index + keyword.length(), b));

	    //Klassen-Namen herrausfinden
	    String className = sourceCodeHPP.substring(index + keyword.length(), b);
	    
	    // Klasse oder Interface oder abstrakte Klasse
	    if (isInterface(className, sourceCodeHPP))
	    {
		// Interface
		Element interfacedefinition = document.createElement("interfacedefinition");
		root.appendChild(interfacedefinition);
		// Name
		Element name = document.createElement("name");
		interfacedefinition.appendChild(name);
		name.appendChild(document.createTextNode(className));

		// Vererbung eines Interfaces
		Element implement = document.createElement("implements");
		interfacedefinition.appendChild(implement);
		// Vererbung einer Klasse
		Element extend = document.createElement("extends");
		interfacedefinition.appendChild(extend);
		// Instanzen,(public, private , protected)
		Element instance = document.createElement("instance");
		interfacedefinition.appendChild(instance);
		// Variablen
		Element var = document.createElement("var");
		interfacedefinition.appendChild(var);
		// Komposition
		Element compositions = document.createElement("compositions");
		interfacedefinition.appendChild(compositions);
		// Aggregation
		Element aggregation = document.createElement("aggregation");
		interfacedefinition.appendChild(aggregation);

		// Vererbung
		String[] tmpArray = getHeredity(sourceCodeHPP, className);
		for (int i = 0; i < tmpArray.length; i++)
		{
		    if (isInterface(tmpArray[i], sourceCodeHPP))
		    {
			Element entry = document.createElement("entry");
			implement.appendChild(entry);
			entry.appendChild(document.createTextNode(tmpArray[i]));
		    }
		    else if (!tmpArray[i].equals(""))
		    {
			Element entry = document.createElement("entry");
			extend.appendChild(entry);
			entry.appendChild(document.createTextNode(tmpArray[i]));
		    }
		}

		// Aggregation
		ArrayList<String> aggrList = aggregation(sourceCodeCPP, className);
		for (int i = 0; i < aggrList.size(); i++)
		{
		    Element entry = document.createElement("entry");
		    aggregation.appendChild(entry);
		    entry.appendChild(document.createTextNode(aggrList.get(i)));
		}

		// Komposition: Such-Konstrukt zur Einfachheit: "new Class()"
		ArrayList<String> compList = composition(sourceCodeHPP, sourceCodeCPP, className, false);
		for (int i = 0; i < compList.size(); i++)
		{
		    Element entry = document.createElement("entry");
		    compositions.appendChild(entry);
		    entry.appendChild(document.createTextNode(compList.get(i)));
		}

		// Methoden

	    }
	    else if (isAbstract(className, sourceCodeHPP))
	    {
		// Abstrakt
		Element abstractdefinition = document.createElement("abstractdefinition");
		root.appendChild(abstractdefinition);
		// Name
		Element name = document.createElement("name");
		abstractdefinition.appendChild(name);
		name.appendChild(document.createTextNode(className));

		// Vererbung eines Interfaces
		Element implement = document.createElement("implements");
		abstractdefinition.appendChild(implement);
		// Vererbung einer Klasse
		Element extend = document.createElement("extends");
		abstractdefinition.appendChild(extend);
		// Instanzen,(public, private , protected)
		Element instance = document.createElement("instance");
		abstractdefinition.appendChild(instance);
		// Variablen
		Element var = document.createElement("var");
		abstractdefinition.appendChild(var);
		// Komposition
		Element compositions = document.createElement("compositions");
		abstractdefinition.appendChild(compositions);
		// Aggregation
		Element aggregation = document.createElement("aggregation");
		abstractdefinition.appendChild(aggregation);

		// Vererbung
		String[] tmpArray = getHeredity(sourceCodeHPP, className);
		for (int i = 0; i < tmpArray.length; i++)
		{
		    if (isInterface(tmpArray[i], sourceCodeHPP))
		    {
			Element entry = document.createElement("entry");
			implement.appendChild(entry);
			entry.appendChild(document.createTextNode(tmpArray[i]));
		    }
		    else if (!tmpArray[i].equals(""))
		    {
			Element entry = document.createElement("entry");
			extend.appendChild(entry);
			entry.appendChild(document.createTextNode(tmpArray[i]));
		    }
		}

		// Aggregation
		ArrayList<String> aggrList = aggregation(sourceCodeCPP, className);
		for (int i = 0; i < aggrList.size(); i++)
		{
		    Element entry = document.createElement("entry");
		    aggregation.appendChild(entry);
		    entry.appendChild(document.createTextNode(aggrList.get(i)));
		}

		// Komposition: Such-Konstrukt zur Einfachheit: "new Class()"
		ArrayList<String> compList = composition(sourceCodeHPP, sourceCodeCPP, className, false);
		for (int i = 0; i < compList.size(); i++)
		{
		    Element entry = document.createElement("entry");
		    compositions.appendChild(entry);
		    entry.appendChild(document.createTextNode(compList.get(i)));
		}

		// Methoden
		// createMethods(sourceCodeCPP, document, classdefinition);

	    }
	    else
	    {
		// Klasse
		Element classdefinition = document.createElement("classdefinition");
		root.appendChild(classdefinition);
		// Name
		Element name = document.createElement("name");
		classdefinition.appendChild(name);
		name.appendChild(document.createTextNode(className));

		// Code Analyse:
		// Vererbung eines Interfaces
		Element implement = document.createElement("implements");
		classdefinition.appendChild(implement);
		// Vererbung einer Klasse
		Element extend = document.createElement("extends");
		classdefinition.appendChild(extend);
		// Instanzen,(public, private , protected)
		Element instance = document.createElement("instance");
		classdefinition.appendChild(instance);
		// Variablen
		Element var = document.createElement("var");
		classdefinition.appendChild(var);
		// Komposition
		Element compositions = document.createElement("compositions");
		classdefinition.appendChild(compositions);
		// Aggregation
		Element aggregation = document.createElement("aggregation");
		classdefinition.appendChild(aggregation);

		// Vererbung
		String[] tmpArray = getHeredity(sourceCodeHPP, className);
		for (int i = 0; i < tmpArray.length; i++)
		{
		    if (isInterface(tmpArray[i], sourceCodeHPP))
		    {
			Element entry = document.createElement("entry");
			implement.appendChild(entry);
			entry.appendChild(document.createTextNode(tmpArray[i]));
		    }
		    else if (!tmpArray[i].equals(""))
		    {
			Element entry = document.createElement("entry");
			extend.appendChild(entry);
			entry.appendChild(document.createTextNode(tmpArray[i]));
		    }
		}

		// Aggregation
		ArrayList<String> aggrList = aggregation(sourceCodeCPP, className);
		for (int i = 0; i < aggrList.size(); i++)
		{
		    Element entry = document.createElement("entry");
		    aggregation.appendChild(entry);
		    entry.appendChild(document.createTextNode(aggrList.get(i)));
		}

		// Komposition: Such-Konstrukt zur Einfachheit: "new Class()"
		ArrayList<String> compList = composition(sourceCodeHPP, sourceCodeCPP, className, true);
		for (int i = 0; i < compList.size(); i++)
		{
		    Element entry = document.createElement("entry");
		    compositions.appendChild(entry);
		    entry.appendChild(document.createTextNode(compList.get(i)));
		}

		// Methoden
		createMethods(sourceCodeCPP, document, classdefinition);

	    }

	    // Suche nach dem nächsten Index vom Wort "class" im HPP-Code
	    index = sourceCodeHPP.indexOf(keyword, index + keyword.length());
	}

	System.out.println("\n #################### BEGINN JANS TEST ####################\n");

	/*
	 * //Interfaces und abstrakte Klassen System.out.println("CLASS1 >>" +
	 * isInterface("Class1", sourceCodeHPP)); System.out.println("IF2 >>" +
	 * isAbstract("If2", sourceCodeHPP)); System.out.println("CLASS2 >>" +
	 * isAbstract("Class2", sourceCodeHPP)); System.out.println("IF1 >>" +
	 * isAbstract("If1", sourceCodeHPP)); System.out.println("IF3/FIGURE >>" +
	 * isAbstract("Figure", sourceCodeHPP)); System.out.println("IF4/TEST >>" +
	 * isAbstract("Test", sourceCodeHPP)); System.out.println("oA uI");
	 * System.out.println("CLASS1 >>" + isInterface("Class1", sourceCodeHPP));
	 * System.out.println("IF2 >>" + isInterface("If2", sourceCodeHPP));
	 * System.out.println("CLASS2 >>" + isInterface("Class2", sourceCodeHPP));
	 * System.out.println("IF1 >>" + isInterface("If1", sourceCodeHPP));
	 * System.out.println("IF3/FIGURE >>" + isInterface("Figure", sourceCodeHPP));
	 * System.out.println("IF4/TEST >>" + isInterface("Test", sourceCodeHPP));
	 */

	System.out.println("\n #################### ENDE JANS TEST ####################\n");
	xmlHelper.writeDocumentToConsole(document);
	
    }

    private void createMethods(String sourceCodeCPP, Document document, Element classdefinition)
    {
	calclassinterval(sourceCodeCPP);
	int j = classInterval[0], i = j, n = classInterval[1];// sourceCodeCPP.length();
	while (sourceCodeCPP.charAt(j) != '"')
	{
	    j--;
	}
	j++;
	String hppName = sourceCodeCPP.substring(j, i);
	String methodinitial = hppName + "::";
	int methodinitlength = methodinitial.length();
	while (i + methodinitlength < n && i != -1)
	{
	    if (sourceCodeCPP.substring(i, i + methodinitlength).equals(methodinitial))
	    {
		Element methoddefinition = document.createElement("methoddefinition");
		classdefinition.appendChild(methoddefinition);

		Element methName = document.createElement("name");
		methoddefinition.appendChild(methName);

		Element methResult = document.createElement("result");

		// Vorgängerwort von methodinitial suchen (entspricht Resultattyp)
		int h = i;
		while (sourceCodeCPP.charAt(h) != '\n' && h > 0)
		{
		    h--;
		}
		if (h < i - 1)
		{
		    methoddefinition.appendChild(methResult);
		    methResult.appendChild(document.createTextNode(sourceCodeCPP.substring(h + 1, i - 1)));
		}

		// Folgewort von methodinitial suchen (entspricht Methodenname)
		j = i + methodinitlength;
		while (sourceCodeCPP.charAt(j) != '(' && j < n)
		{
		    j++;
		}
		methName.appendChild(document.createTextNode(sourceCodeCPP.substring(i + methodinitlength, j)));

		i = j + 1;
		h = i;

		while (sourceCodeCPP.charAt(j) != ')' && j < n)
		{
		    j++;
		}

		Element methparam = document.createElement("parameters");
		methoddefinition.appendChild(methparam);

		while (i < j)
		{
		    // Bsp: (int param1, int param2) ist auszulesen
		    // Typ finden
		    while (sourceCodeCPP.charAt(i) != ' ' && i < j)
		    {
			i++;
		    }

		    // '*' auslassen
		    if (sourceCodeCPP.charAt(i - 1) == '*')
		    {
			i--;
		    }

		    Element entry = document.createElement("entry");
		    methparam.appendChild(entry);

		    Element paramType = document.createElement("type");
		    entry.appendChild(paramType);

		    paramType.appendChild(document.createTextNode(sourceCodeCPP.substring(h, i)));

		    if (sourceCodeCPP.charAt(i) == '*')
		    {
			i += 2;
		    }
		    else
		    {
			i++;
		    }

		    h = i;

		    // Namen finden
		    while (sourceCodeCPP.charAt(i) != ',' && i < j)
		    {
			i++;
		    }

		    Element paramName = document.createElement("name");
		    entry.appendChild(paramName);

		    paramName.appendChild(document.createTextNode(sourceCodeCPP.substring(h, i)));

		    i++;
		    // Mögliches Leerzeichen hinter dem Komma überspringen
		    if (sourceCodeCPP.charAt(i) == ' ')
		    {
			i++;
		    }
		    h = i;
		}
	    }
	    i++;
	}
    }

    /**
     * Liefert eine Liste der in HPP- uns CPP-Code vorkommenden Aufrufe mit new
     * ___()
     * 
     * @param sourceCodeCPP
     * @param className
     * @return
     * @return aggregation
     */
    private ArrayList<String> composition(String sourceCodeHPP, String sourceCodeCPP, String className, boolean hasCPP)
    {
	// Klassen-QuellCode
	sourceCodeHPP = getFormatedSourceCodeHPP(className, sourceCodeHPP);
	
	// Liste zum Speichern der Kompositionen
	ArrayList<String> komposition = new ArrayList<String>();
	// HPP nach Kompositionen durchsuchen
	String h = "";
	int fromIndex = 0;
	while (sourceCodeHPP.indexOf("new ", fromIndex + 1) >= 0)
	{
	    fromIndex = sourceCodeHPP.indexOf("new ", fromIndex + 1);
	    // Herrausschneiden des Bezeichners zwischen new_ und )
	    h = sourceCodeHPP.substring(fromIndex + "new ".length(),
		    sourceCodeHPP.indexOf("(", fromIndex + "new ".length()));
	    // Verhindern von Duplikaten
	    if (!komposition.contains(h))
	    {
		komposition.add(h);
	    }
	}
	// CPP nach Kompositionen durchsuchen
	if (hasCPP)
	{
	    // Klassen-QuellCode
	    sourceCodeCPP = getFormatedSourceCodeCPP(className, sourceCodeCPP);
	    h = "";
	    fromIndex = 0;
	    while (sourceCodeCPP.indexOf("new ", fromIndex + 1) >= 0)
	    {
		fromIndex = sourceCodeCPP.indexOf("new ", fromIndex + 1);
		// Herrausschneiden des Bezeichners zwischen new_ und )
		h = sourceCodeCPP.substring(fromIndex + "new ".length(),
			sourceCodeCPP.indexOf("(", fromIndex + "new ".length()));
		// Verhindern von Duplikaten
		if (!komposition.contains(h))
		{
		    komposition.add(h);
		}
	    }
	}
	return komposition;
    }

    /**
     * @param sourceCodeCPP
     * @param className
     * @return
     */
    public String getFormatedSourceCodeCPP(String className, String sourceCodeCPP)
    {
	// Klassen-Code bis zum Ende oder ersten #include
	if (sourceCodeCPP.indexOf("#include", sourceCodeCPP.indexOf(className + "::" + className)) != -1)
	{
	    sourceCodeCPP = sourceCodeCPP.substring(sourceCodeCPP.indexOf("#include \"" + className + ".hpp\""),
		    sourceCodeCPP.indexOf("#include", sourceCodeCPP.indexOf("#include \"" + className + ".hpp\"")));
	}
	else
	{
	    sourceCodeCPP = sourceCodeCPP.substring(sourceCodeCPP.indexOf("#include \"" + className + ".hpp\""),
		    sourceCodeCPP.length());
	}
	// Code kürzen, filtern und anpassen
	sourceCodeCPP = sourceCodeCPP.replaceAll("\n", "");
	sourceCodeCPP = sourceCodeCPP.replaceAll("\t", "");
	while (sourceCodeCPP.contains("  "))
	{
	    sourceCodeCPP = sourceCodeCPP.replaceAll("  ", " ");
	}
	sourceCodeCPP = sourceCodeCPP.trim();
	return sourceCodeCPP;
    }

    /**
     * Liefert eine Liste der im Konstruktor geforderten Klassen/Pointer zurück.
     * 
     * @param sourceCodeCPP
     * @param className
     * @return aggregation
     */
    public ArrayList<String> aggregation(String sourceCodeCPP, String className)
    {
	ArrayList<String> aggregation = new ArrayList<String>();
	String tmp = "";
	int fromIndex = 0;
	// Konstruktor-Argumente heraus filtern
	while (sourceCodeCPP.indexOf(className + "::" + className,
		fromIndex + (className + "::" + className).length()) >= 0)
	{
	    fromIndex = sourceCodeCPP.indexOf(className + "::" + className,
		    fromIndex + (className + "::" + className).length());
	    tmp = sourceCodeCPP.substring(sourceCodeCPP.indexOf("(", fromIndex),
		    sourceCodeCPP.indexOf(")", fromIndex) + 1);

	    String h = "";
	    int tmpIndex = 0;
	    // Klassen/Pointer-Argumente herausfiltern
	    while (tmp.indexOf("*", tmpIndex + 1) >= 0)
	    {
		tmpIndex = tmp.indexOf("*", tmpIndex + 1);
		h = tmp.substring(
			Math.max(tmp.lastIndexOf(" ", tmpIndex),
				Math.max(tmp.lastIndexOf("(", tmpIndex), tmp.lastIndexOf(",", tmpIndex))) + 1,
			tmpIndex);
		aggregation.add(h);
	    }
	}
	return aggregation;
    }

    /**
     * Liefert ein Array zurück mit allen Klassen von denen geerbt wird.
     * 
     * @param sourceCodeHPP
     * @param className
     * @return tmpArray
     */
    public String[] getHeredity(String sourceCodeHPP, String className)
    {
	String tmp = getFormatedSourceCodeHPP(className, sourceCodeHPP);
	tmp = tmp.substring(0, tmp.indexOf("{"));
	tmp = tmp.replaceAll("public", "");
	tmp = tmp.replaceAll("private", "");
	tmp = tmp.replaceAll("protected", "");
	tmp = tmp.replaceAll(":", "");
	tmp = tmp.replaceAll(" ", "");
	tmp = tmp.trim();
	String[] tmpArray = tmp.split(",");
	return tmpArray;
    }

    // Wertet aus, ob eine Klasse eines HPP-Quelltextes ein Interface ist
    private boolean isInterface(String className, String sourceCodeHPP)
    {
	// Code kürzen, filtern und anpassen
	sourceCodeHPP = getFormatedSourceCodeHPP(className, sourceCodeHPP);
	sourceCodeHPP = sourceCodeHPP.replaceAll("public:", "");
	sourceCodeHPP = sourceCodeHPP.replaceAll("private:", "");
	sourceCodeHPP = sourceCodeHPP.replaceAll("protected:", "");
	sourceCodeHPP = sourceCodeHPP.substring(sourceCodeHPP.indexOf("{") + 1, sourceCodeHPP.length());
	sourceCodeHPP = sourceCodeHPP.trim();

	// Prüfen ob alle Methoden virtuel und =0; sind && Dekonstruktor: virtual
	// ~IDemo() {}
	int i = 0, n = sourceCodeHPP.length();
	while (i + className.length() < n && i != -1)
	{
	    // virtuelle Methode
	    if (sourceCodeHPP.substring(i, i + "virtual ".length()).equals("virtual "))
	    {
		// Dekonstruktor
		if (sourceCodeHPP.substring(i + "virtual ".length(), i + "virtual ".length() + 1).equals("~"))
		{
		    // Soweit True
		}
		// nicht implementierte Methode
		else if (sourceCodeHPP.lastIndexOf("0", sourceCodeHPP.indexOf(";", i)) != -1
			&& sourceCodeHPP.lastIndexOf("=", sourceCodeHPP.indexOf("0",
				(sourceCodeHPP.lastIndexOf("0", sourceCodeHPP.indexOf(";", i))))) != -1)
		{
		    // Soweit True
		}
		// Fehler-Fall
		else
		{
		    return false;
		}
	    }
	    // Fehler-Fall
	    else
	    {
		return false;
	    }

	    // Nächsten Methodenanfang finden und unsinnigen Index Abfangen
	    if (Math.min(sourceCodeHPP.indexOf(";", i), sourceCodeHPP.indexOf("}", i)) >= 0)
	    {
		i = Math.min(sourceCodeHPP.indexOf(";", i), sourceCodeHPP.indexOf("}", i) + 2);
	    }
	    else if (sourceCodeHPP.indexOf(";", i) >= 0)
	    {
		i = sourceCodeHPP.indexOf(";", i) + 2;
	    }
	}
	return true;
    }

    // Wertet aus, ob eine Klasse eines HPP-QUelltextes eine abstrakte Klasse ist
    private boolean isAbstract(String className, String sourceCodeHPP)
    {
	// Code kürzen, filtern und anpassen
	sourceCodeHPP = getFormatedSourceCodeHPP(className, sourceCodeHPP);
	sourceCodeHPP = sourceCodeHPP.replaceAll("public:", "");
	sourceCodeHPP = sourceCodeHPP.replaceAll("private:", "");
	sourceCodeHPP = sourceCodeHPP.replaceAll("protected:", "");
	sourceCodeHPP = sourceCodeHPP.substring(sourceCodeHPP.indexOf("{") + 1, sourceCodeHPP.length());
	sourceCodeHPP = sourceCodeHPP.trim();

	// Prüfen ob eine Methoden virtuel und =0; sind
	int i = 0, n = sourceCodeHPP.length();
	while (i + className.length() < n && i != -1)
	{
	    // virtuelle Methode
	    if (sourceCodeHPP.substring(i, i + "virtual ".length()).equals("virtual "))
	    {
		// nicht implementierte Methode
		if (sourceCodeHPP.lastIndexOf("0", sourceCodeHPP.indexOf(";", i)) != -1
			&& sourceCodeHPP.lastIndexOf("=", sourceCodeHPP.indexOf("0",
				(sourceCodeHPP.lastIndexOf("0", sourceCodeHPP.indexOf(";", i))))) != -1)
		{
		    return true;
		}
	    }

	    // Nächsten Methodenanfang finden und unsinnigen Index Abfangen
	    if (Math.min(sourceCodeHPP.indexOf(";", i), sourceCodeHPP.indexOf("}", i)) >= 0)
	    {
		i = Math.min(sourceCodeHPP.indexOf(";", i), sourceCodeHPP.indexOf("}", i) + 2);
	    }
	    else if (sourceCodeHPP.indexOf(";", i) >= 0)
	    {
		i = sourceCodeHPP.indexOf(";", i) + 2;
	    }
	}
	return false;
    }

    // Liefert aus einem .hpp-Quellcode und einem Klassennamen den (ersten)
    // passenden Klassen-Quelltext
    public String getFormatedSourceCodeHPP(String className, String sourceCodeHPP)
    {
	// Klassen-Deklaration zusammen setzen
	className = "class " + className;
	// Klassen-Code aus Quellcode filtern
	int keyIndex = sourceCodeHPP.indexOf(className) + className.length();
	sourceCodeHPP = sourceCodeHPP.substring(keyIndex, sourceCodeHPP.indexOf("};", keyIndex));
	// Code kürzen, filtern und anpassen
	sourceCodeHPP = sourceCodeHPP.replaceAll("\n", "");
	sourceCodeHPP = sourceCodeHPP.replaceAll("\t", "");

	// Um Index 0 "perfekt" zu setzen
	// sourceCodeHPP = sourceCodeHPP.replace('{', ' ');

	while (sourceCodeHPP.contains("  "))
	{
	    sourceCodeHPP = sourceCodeHPP.replaceAll("  ", " ");
	}
	sourceCodeHPP = sourceCodeHPP.trim();
	return sourceCodeHPP;
    }

    /**
     * Liest den uebergebenen Quellcode ein und parsed die Informationen daraus
     * 
     * @param sourceCode Vollstaendiger Java-Quellcode
     */
    public void parse(ArrayList<String> sourceCode)
    {
	sourceCode.set(0, deleteComStr(sourceCode.get(0)));
	sourceCode.set(1, deleteComStr(sourceCode.get(1)));

	// Ausgabe eingelesener HPP-Dateien
	System.out.println(sourceCode.get(0));
	// Ausgabe eingelesener CPP-Dateien
	System.out.println(sourceCode.get(1));

	try
	{
	    buildTree(sourceCode);
	}
	catch (ParserConfigurationException e)
	{
	    // TODO Auto-generated catch block
		PUMLgenerator.logger.getLog().warning("@ParserCPP/parse: "+e.toString());
	    //e.printStackTrace();
	}

    }

    // Methode zum Entfernen von Kommentaren und Strings für komplikationsfreies
    // Parsen
    public String deleteComStr(String sourceCode)
    {
	String cSC = ""; // commentlessSourceCode
	for (int i = 0, n = sourceCode.length(); i < n; i++)
	{
	    // Filtern nach Kommentar mit //
	    if (sourceCode.charAt(i) == '/' && sourceCode.charAt(i + 1) == '/')
	    {
		i++;
		while (sourceCode.charAt(i) != '\n' && i < n)
		{
		    i++;
		}
		cSC += "\n"; // um Zeilenumbruch nicht auszulassen. Alternative: i--;
	    }
	    // Filtern nach Kommentar mit /* */
	    else if (sourceCode.charAt(i) == '/' && sourceCode.charAt(i + 1) == '*')
	    {
		i += 2;
		while (!(sourceCode.charAt(i) == '*' && sourceCode.charAt(i + 1) == '/') && i < n)
		{
		    i++;
		}
		i++;
	    }
	    /* Entfernt, da es Fehler hervorruft */
	    // Filtern nach Strings
	    else
	    {
		cSC += sourceCode.charAt(i);
	    }
	}
	return cSC;
    }

    private void calclassinterval(String sourceCodeCPP)
    {
	if (classInterval[0] == 0)
	{
	    classInterval[0] = sourceCodeCPP.indexOf(".hpp\"");
	}
	else
	{
	    classInterval[0] = classInterval[1];
	}
	if (sourceCodeCPP.indexOf(".hpp\"", classInterval[0] + 5) < 0)
	{
	    classInterval[1] = sourceCodeCPP.length();
	}
	else
	{
	    classInterval[1] = sourceCodeCPP.indexOf(".hpp\"", classInterval[0] + 5);
	}
    }

    @Override
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
