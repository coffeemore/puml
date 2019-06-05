    	private ParsingResult result = null;
	private Vector<String> className = new Vector<String>();
	private Vector<String> classConnection = new Vector<String>();
	private Vector<String> interfaceName = new Vector<String>();
	private Vector<String> interfaceConnection = new Vector<String>();
	private ArrayList<String> bothName = new ArrayList<String>();
	private ArrayList<ClassConnection> classConnectionArray = new ArrayList<ClassConnection>();
	private ClassConnection.connectionType extensionType = ClassConnection.connectionType.extension;
	private ClassConnection.connectionType aggregationType = ClassConnection.connectionType.aggregation;
	private ClassConnection.connectionType compositionType = ClassConnection.connectionType.composition;
	private ArrayList<Integer> classPos = new ArrayList<Integer>();
	
	public String getCommentlessSourceCode(String sourcec) // Entfernt Kommentare eines Strings
    {

//		sourcec = sourcec.replaceAll("\".[^\"]*\"", "");
	// sourcec = sourcec.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)",
	// "");
	// "(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)"
//	System.out.println("============================== Commentless Sourcecode: ==============================\n"
//		+ sourcec + "\n====================================================================================");
	return sourcec;
    }

    /**
     * Ermittelt Klassenbeziehungen Agregation und Komposition
     * 
     * @param substring       Inhalt der Klasse, welcher untersucht wird
     * @param parentClassName Klassenname der Klasse, dessen Inhalt untersucht wird
     */
    private void analyzeClassBody(String substring, String parentClassName)
    {
//	System.out.println("============================== Inhalt von Sourcecode ab Klasse " + parentClassName
//		+ " ==============================\n" + substring
//		+ "\n===================================================================================================");

	ArrayList<Integer> CurlyPos = new ArrayList<>();
	int counter = 0;
	Matcher Curly = Pattern.compile("(\\{)|(\\})").matcher(substring);
	while (Curly.find())
	{
	    CurlyPos.add(Curly.start());

	    if (Curly.start(1) == Curly.start())
	    {
		// System.out.println("{");
		counter++;

	    }
	    else
	    {
		// System.out.println("}");
		counter--;
	    }
	    if (counter == 0)
	    {
		break;
	    }
	}
	String classBody = substring.substring(CurlyPos.get(0), CurlyPos.get(CurlyPos.size() - 1) + 1);

//		System.out.println("============================== Inhalt von Klasse " + parentClassName
//	     		+ " ==============================\n" + classBody
//				+ "\n===================================================================================================");
	int parentClassNo = bothName.indexOf(parentClassName); // Klassennummer des Kï¿½rpers
	String parentClass; // Klassenname des Kï¿½rpers

	System.out.println("Klassenname:" + parentClassName);
	for (String classname : className)
	{
	    ArrayList<Integer> newClassPos = new ArrayList<>();
	    ArrayList<Integer> dataTypeClassPos = new ArrayList<>();
	    ArrayList<Integer> constructorPos = new ArrayList<>();
	    ArrayList<Integer> constructorWithNewClassPos = new ArrayList<>();
	    ArrayList<Integer> constructorWithDataTypeClassPos = new ArrayList<>();
	    ArrayList<Integer> methodsWithNewClassPos = new ArrayList<>();
	    ArrayList<Integer> methodsWithDataTypeClassPos = new ArrayList<>();

	    if (classBody.contains(classname) && classname != parentClassName)
	    {
		System.out.println("enthï¿½lt '" + classname + "'");
		Matcher newClass = Pattern.compile("new +" + classname + " *\\(.*\\)").matcher(classBody);
		while (newClass.find())
		{
		    newClassPos.add(newClass.start());
		}
		Matcher dataTypeClass = Pattern.compile("< *" + classname + " *>").matcher(classBody);
		while (dataTypeClass.find())
		{
		    dataTypeClassPos.add(dataTypeClass.start());
		}
		Matcher constructor = Pattern.compile(parentClassName + " *\\(.*\\) *\\{.*\\}").matcher(classBody);
		while (constructor.find())
		{
		    constructorPos.add(constructor.start());
		}
		Matcher constructorWithNewClass = Pattern
			.compile(parentClassName + " *\\(.*\\) *\\{.*new +" + classname + " *\\(.*\\).*\\}")
			.matcher(classBody);
		while (constructorWithNewClass.find())
		{
		    constructorWithNewClassPos.add(constructorWithNewClass.start());
		}
		Matcher constructorWithDataTypeClass = Pattern
			.compile(parentClassName + " *\\(.*< *" + classname + " *>.*\\) *\\{.*\\}").matcher(classBody);
		while (constructorWithDataTypeClass.find())
		{
		    constructorWithDataTypeClassPos.add(constructorWithDataTypeClass.start());
		}
		Matcher methodsWithNewClass = Pattern.compile("\\{.*new +" + classname + " *\\(.*\\).*\\}")
			.matcher(classBody);
		while (methodsWithNewClass.find())
		{
		    methodsWithNewClassPos.add(methodsWithNewClass.start());
		}
		Matcher methodsWithDataTypeClass = Pattern.compile("\\(.*< *" + classname + " *>.*\\) *\\{.*\\}")
			.matcher(classBody);
		while (methodsWithDataTypeClass.find())
		{
		    methodsWithDataTypeClassPos.add(methodsWithDataTypeClass.start());
		}
		System.out.println("Vorkommen: \nals new class(): " + newClassPos.size() + "\nals Datentyp: "
			+ dataTypeClassPos.size() + "\nin Konstruktoren: " + constructorPos.size()
			+ "\nin Konstruktoren als new class() : " + constructorWithNewClassPos.size()
			+ "\nin Konstruktoren als Datentyp: " + constructorWithDataTypeClassPos.size()
			+ "\nin Methoden als new class(): " + methodsWithNewClassPos.size()
			+ "\nin Methoden als Datentyp: " + methodsWithDataTypeClassPos.size());
		if (!newClassPos.isEmpty())
		{
		    if (constructorPos.size() == constructorWithNewClassPos.size()
			    || newClassPos.size() > methodsWithNewClassPos.size())
		    {
			System.out.println("Ergebnis: enthï¿½lt " + classname + " als Komposition");
			ClassConnection cc = new ClassConnection(className.indexOf(classname), parentClassNo,
				compositionType);
			classConnectionArray.add(cc);
		    }
		    else
		    {
			System.out.println("Ergebnis: enthï¿½lt " + classname + " als Agregation");
			ClassConnection cc = new ClassConnection(className.indexOf(classname), parentClassNo,
				aggregationType);
			classConnectionArray.add(cc);
		    }
		}
		System.out.println("-------------------------------------------------------------------------");
	    }
	}

    }
	
	


    /**
     * Ermittelt Knotennummer der Klasse
     * 
     * @param parent Name der Klasse
     * @return Nummer der Klasse
     */
    private int bothNumber(String parent)
    {
	int classNu = -1; // Bei nicht vorhandenem Knoten kommt -1 zurï¿½ck
	for (int i = 0; i < bothName.size(); i++)
	{
	    if (bothName.get(i).equals(parent))
	    {
		classNu = i;
	    }
	}
	if (classNu == -1)
	{
	    System.out.println("Die Klasse " + parent + " existiert nicht");
	}
	return classNu;
    }

//  private int interfaceNumber(String interName)
//  {
//	int interNu = -1;
//	for (int i = 0; i < interfaceName.size(); i++)
//	{
//	    if (className.get(i).equals(interName))
//	    {
//	    	interNu = i;
//	    }
//	}
//	return interNu;
//  }

    /**
     * Liest Vererbung der angegebenen Klasse aus
     * 
     * @param i
     * @param countIN
     */
    private void exClassConnection(int i, int countIN)
    {
	String parent = "keinem";
	ClassConnection cc;

	// Pattern um den Namen auszulesen erstellen
	Matcher mEx = Pattern.compile("extends +([a-zA-Z0-9]*).*\\{").matcher(classConnection.get(i));
	while (mEx.find())
	{
	    parent = mEx.group(1);
	    System.out.println(bothNumber(parent));

	}
	if (parent != "keinem")
	{
	    System.out.println("Klasse " + bothName.get(i + countIN) + " erbt von " + bothName.get(bothNumber(parent)));
	    // Hinzufuegen der Verbindung ins Array
	    cc = new ClassConnection((i + countIN), bothNumber(parent), extensionType);
	    classConnectionArray.add(cc);
	    // System.out.println("G " + classConnectionArray.size())
	}

	;
    }

    /**
     * Liest benutzte Interfaces der angegebenen Klasse aus
     * 
     * @param i
     * @param countIN
     */
    private void impClassConnection(int i, int countIN)
    {

	String interface1 = "keinem";
	String followingInterfaces = "keinem";
	ClassConnection cc;
	// Erstellung des Patterns um Namen nach dem Keyword "implements" auszulesen
	Matcher mImp1 = Pattern.compile("implements +([a-zA-Z0-9]*) *( *, *[a-zA-Z0-9]*)* *.*\\{")
		.matcher(classConnection.get(i));
	if (mImp1.find())
	{
	    interface1 = mImp1.group(1); // das erste Interface
	    // Hinzufuegen einer neuen Verbindung
	    cc = new ClassConnection((i + countIN), bothNumber(interface1), extensionType);
	    classConnectionArray.add(cc);

	    // wenn nach dem Namen ein Komma steht kommen weitere Interfaces die eingebunden
	    // werden
	    if (mImp1.group(2) != null)
	    {
		followingInterfaces = mImp1.group(2);
	    }

	    System.out.println(interface1 + " ist Interface von " + className.get(i));

	    Matcher mImp2 = Pattern.compile(" *, *([a-zA-Z0-9]*)").matcher(followingInterfaces);
	    while (mImp2.find())
	    {
		interface1 = mImp2.group(1);
		System.out.println(interface1 + " ist Interface von " + className.get(i));
		// hinzufuegen der Verbindung zum Array
		cc = new ClassConnection((i + countIN), bothNumber(interface1), extensionType);
		classConnectionArray.add(cc);
		System.out.println("G " + classConnectionArray.size());
	    }
	    System.out.println("test");
	}
    }

    /**
     * Liest eingebundene Interfaces in anderen Interfaces aus
     * 
     * @param i
     */
    private void exInterfaceConnection(int i)
    {
	String interface1 = "keinem";
	String followingInterfaces = "keins";
	ClassConnection cc;
	// Erstellen des Patterns um bei Interfaces auszulesen, ob es von einem oder
	// mehreren Interfaces "erbt"
	Matcher mEx1 = Pattern.compile("extends +([a-zA-Z0-9]*) *( *, *[a-zA-Z0-9]*)* *.*\\{")
		.matcher(interfaceConnection.get(i));
	if (mEx1.find())
	{
	    interface1 = mEx1.group(1); // das erste Interface
	    System.out.println("jap");
	    // Hinzufuegen einer Verbindung
	    cc = new ClassConnection((i), bothNumber(interface1), extensionType);
	    classConnectionArray.add(cc);

	    // bei folgendem "," ist von folgenden Interfacenamen auszugehen, die man noch
	    // auslesen und hinzufuegen muss
	    if (mEx1.group(2) != null)
	    {
		followingInterfaces = mEx1.group(2);
	    }

	    System.out.println(interface1 + " ist Interface von " + bothName.get(i));

	    Matcher mEx2 = Pattern.compile(" *, *([a-zA-Z0-9]*)").matcher(followingInterfaces);
	    while (mEx2.find())
	    {
		interface1 = mEx2.group(1);
		System.out.println(interface1 + " ist Interface von " + bothName.get(i));
		// Neue Verbindung wird hinzugefuegt
		cc = new ClassConnection((i), bothNumber(interface1), extensionType);
		classConnectionArray.add(cc);
	    }
	    System.out.println("test");
	}
    }
	
	
	// Teil aus der parser() Funktion:
	//	// Auslesen aller Interfacenamen
//	// 1. Suche nach Interface-Deklaration
//	// TODO: BUG! entfernt zu viel! -> lï¿½scht alle Stellen mit // oder /* ... */
//	// deswegen darauf achten, dass keine "//" oder /* ... */ in RegEx oder Strings
//	// benutzt werden
//	sourceCode = getCommentlessSourceCode(sourceCode);
//	Matcher interfaceMatcher = Pattern.compile("interface +([a-zA-Z0-9_]+).*\\R* *\\{").matcher(sourceCode);
//	System.out.println("Interfaces: ");
//	// Solange im Quelltext Interfacedeklarationen gefunden werden, such weiter und
//	// speichere sie ab
//	while (interfaceMatcher.find())
//	{
//	    interfaceName.add(interfaceMatcher.group(1));
//	    bothName.add(interfaceMatcher.group(1)); // speichere den Namen
//	    interfaceConnection.add(interfaceMatcher.group()); // speichere die Zeile um spï¿½ter die Interfaces
//	    // auszulesen, die in Verbindung zu dem Interface stehen
//	    classPos.add(interfaceMatcher.start());
//	    System.out.println(interfaceMatcher.group(1));
//	}
//	System.out.println("Anzahl " + interfaceName.size());
//	int countIN = interfaceName.size(); // Speichern der Grï¿½ï¿½e des Interfaces fï¿½r die ï¿½bersichtlichkeit
//
//	for (int i = 0; i < interfaceConnection.size(); i++) // suche nach extends, da Interfaces von einem oder
//	// mehreren anderen Interfaces erben kï¿½nnen
//	{
//	    if (interfaceConnection.get(i).indexOf("extends") > 1)
//	    {
//		exInterfaceConnection(i); // Funktion, die alle Verbindungen des Interfaces i zu den anderen Interfaces
//		// herausliest und speichert
//	    }
//	}
//
//	// Auslesen aller Klassennamen
//	Matcher classMatcher = Pattern.compile("class +([a-zA-Z0-9_]+).*\\R* *\\{").matcher(sourceCode);
//	System.out.println("Klassen: ");
//
//	// Speichern der Klassennamen und der ganzen Zeile bis "{" um Interfaces und
//	// Vererbungen herauszubekommen
//	while (classMatcher.find())
//	{
//	    className.add(classMatcher.group(1));
//	    bothName.add(classMatcher.group(1));
//	    classConnection.add(classMatcher.group());
//	    classPos.add(classMatcher.start());
//	    System.out.println(classMatcher.group(1));
//
//	}
//	System.out.println("Anzahl " + className.size());
//
//	for (String classname : bothName)
//	{
//	    System.out.println("analyzeClassBody() von Klasse " + classname + ": ");
//	    analyzeClassBody(sourceCode.substring(classPos.get(bothName.indexOf(classname))), classname);
//	}
//
//	// Auslesen der Verbindungen zwischen Klassen und Interfaces
//	for (int i = 0; i < classConnection.size(); i++)
//	{
//	    // Auslesen und Zuweisen von Verbindungen die Interfaces implementieren und/oder
//	    // zusï¿½tzlich auch von einer Klasse erben
//	    if (classConnection.get(i).indexOf("implements") > 1)
//	    {
//
//		if (classConnection.get(i).indexOf("extends") > 1)
//		{
//		    impClassConnection(i, countIN);
//		    exClassConnection(i, countIN);
//		    System.out.println("Klasse " + (i + countIN) + " implementiert und erbt");
//		}
//		else
//		{
//		    impClassConnection(i, countIN);
//		    System.out.println("Bin hier");
//		}
//
//	    }
//	    // Klassen, die nur erben
//	    else if (classConnection.get(i).indexOf("extends") > 1)
//	    {
//		exClassConnection(i, countIN);
//	    }
//	    // KLassen, die weder erben noch Interfaces implementieren
//	    else
//	    {
//		System.out.println("Klasse " + (i + countIN) + " implementiert nicht und erbt nicht ");
//	    }
//	}
//	// Ausgabe aller Verbindungen
//	for (int i = 0; i < classConnectionArray.size(); i++)
//	{
//	    System.out.println("Verbindung " + i + " : von " + classConnectionArray.get(i).getFrom() + " nach "
//		    + classConnectionArray.get(i).getTo() + " .");
//	}
//
//	// Speichern der Klassennamen und Verbindungen im result
//	result = new ParsingResult(bothName, classConnectionArray);


    /**
     * Liefert die Ergebnisse des Parsens zurï¿½ck
     * 
     * @return Struktur mit den Ergebnissen des Parsens
     
    public ParsingResult getParsingResult()
    {
	return result;
    }
     */
	 
	 
//////////////////////////////////////////////////Alter Code aus neuem Parser/////////////////////////////////////////////////////////////

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





