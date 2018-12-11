
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.*;

import jdk.nashorn.internal.parser.Token;

/**
 * 
 * @author Klasse die den Parser für Java implementiert
 */
public class ParserJava implements ParserIf
{

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
    
    private ArrayList<String> classSubstrings = new ArrayList<String>();
    private ArrayList<String> classBodies = new ArrayList<String>();
    /**
     * Konstruktor
     */
    public ParserJava()
    {

    }

    /**
     * Liest den übergebenen Quellcode ein und parsed die Informationen daraus
     * 
     * @param sourceCode Vollständiger Java-Quellcode
     */
    public String getCommentlessSourceCode(String sourcec) //Entfernt Kommentare eines Strings
    {	
    	sourcec=sourcec.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)","");
    	System.out.println("!!! Commentless Sourcecode:\n"+sourcec+"\n!!!");
    	return sourcec;
    }
    
	public String getClassBody(String substring) //nimmt String und gibt Inhalt innerhalb { } wieder
	{ 
		//System.out.println("$$$ substring:\n"+substring+"\n$$$");

		ArrayList<Integer> lCurlyPos= new ArrayList<Integer>();
		ArrayList<Integer> rCurlyPos= new ArrayList<Integer>();
		
		Matcher lCurly = Pattern.compile("(?=(\\{))").matcher(substring);
		while (lCurly.find())
		{
			lCurlyPos.add(lCurly.start());
		}
		
		Matcher rCurly = Pattern.compile("(?=(\\}))").matcher(substring);
		while (rCurly.find())
		{
			rCurlyPos.add(rCurly.start());
		}	
		
		for (Integer integer : lCurlyPos) {
			System.out.println("{ Position: "+integer);
		}
		for (Integer integer : rCurlyPos) {
			System.out.println("} Position: "+integer);
		}
		String classBody="";
		if (lCurlyPos.size() < rCurlyPos.size()) { 
			System.out.println("Betrachtete Klasse liegt innerhalb einer anderen Klasse");
			classBody=substring.substring(lCurlyPos.get(0)+1,rCurlyPos.get(lCurlyPos.size()-1));
		}
		if (lCurlyPos.size() == rCurlyPos.size()) { 
			classBody=substring.substring(lCurlyPos.get(0)+1,rCurlyPos.get(lCurlyPos.size()-1));
		}
		if (lCurlyPos.size() > rCurlyPos.size()) { 
			System.out.println("Betrachtete Klasse hat offenes Ende (fehlende })");
			classBody=substring.substring(lCurlyPos.get(0)+1);
		}

		System.out.println("@@@ Classbody:\n"+classBody+"\n@@@");
		
		return classBody;
	}
    public void parse(String sourceCode)
    {
	//Auslesen aller Interfacenamen 
	//1. Suche nach Interface-Deklaration
    String s = getCommentlessSourceCode(sourceCode);
	Matcher interfaceMatcher = Pattern.compile("interface +([a-zA-Z0-9]*).*\\{").matcher(sourceCode);	
	System.out.println("Interfaces: ");
	//Solange im Quelltext Interfacedeklarationen gefunden werden, such weiter und speichere sie ab
	while (interfaceMatcher.find())
	{
	    interfaceName.add(interfaceMatcher.group(1));
	    bothName.add(interfaceMatcher.group(1));			//speichere den Namen
	    interfaceConnection.add(interfaceMatcher.group());	//speichere die Zeile um später die Interfaces auszulesen, die in Verbindung zu dem Interface stehen
	    System.out.println(interfaceMatcher.group(1));
	}
	System.out.println("Anzahl " + interfaceName.size());
	int countIN = interfaceName.size();						//Speichern der Größe des Interfaces für die Übersichtlichkeit

	for (int i = 0; i < interfaceConnection.size(); i++)	//suche nach extends, da Interfaces von einem oder mehreren anderen Interfaces erben können
	{
	    if (interfaceConnection.get(i).indexOf("extends") > 1)
	    {
		exInterfaceConnection(i);			// Funktion, die alle Verbindungen des Interfaces i zu den anderen Interfaces herausliest und speichert
	    }
	}

	//Auslesen aller Klassennamen
	Matcher classMatcher = Pattern.compile("class +([a-zA-Z0-9]*).*\\{").matcher(sourceCode);
	System.out.println("Klassen: ");
	
	//Speichern der Klassennamen und der ganzen Zeile bis "{" um Interfaces und Vererbungen herauszubekommen
 	while (classMatcher.find())
	{
	    className.add(classMatcher.group(1));
	    bothName.add(classMatcher.group(1));
	    classConnection.add(classMatcher.group());
	    System.out.println(classMatcher.group(1));
	    
	    classSubstrings.add(sourceCode.substring(classMatcher.start()));
	    classBodies.add(getClassBody(sourceCode.substring(classMatcher.start())));
	    System.out.println("###"+sourceCode.substring(classMatcher.start()));
	}
	System.out.println("Anzahl " + className.size());

	//Auslesen der Verbindungen zwischen Klassen und Interfaces
	for (int i = 0; i < classConnection.size(); i++)
	{
	    // Auslesen und Zuweisen von Verbindungen die Interfaces implementieren und/oder
	    // zusätzlich auch von einer Klasse erben
	    if (classConnection.get(i).indexOf("implements") > 1)
	    {

			if (classConnection.get(i).indexOf("extends") > 1)
			{
			    impClassConnection(i, countIN);
			    exClassConnection(i, countIN);
			    System.out.println("Klasse " + (i + countIN) + " implementiert und erbt");
			}
			else
			{
			    impClassConnection(i, countIN);
			    System.out.println("Bin hier");
			}

	    }
	    //Klassen, die nur erben
	    else if (classConnection.get(i).indexOf("extends") > 1)
	    {
	    	exClassConnection(i, countIN);
	    }
	    //KLassen, die weder erben noch Interfaces implementieren
	    else
	    {
	    	System.out.println("Klasse " + (i + countIN) + " implementiert nicht und erbt nicht ");
	    }
	}
	//Ausgabe aller Verbindungen
	for (int i = 0; i < classConnectionArray.size(); i++)
	{
	    System.out.println("Verbindung " + i + " : von " + classConnectionArray.get(i).getFrom() + " nach "
	    		+ classConnectionArray.get(i).getTo() + " .");
	}
	
	//Speichern der Klassennamen und Verbindungen im result
	result = new ParsingResult(bothName, classConnectionArray);

    }

    //Knotennummer der Klasse rausbekommen
    private int bothNumber(String parent)
    {
		int classNu = -1; 				//Bei nicht vorhandenem Knoten kommt -1 zurück
		for (int i = 0; i < bothName.size(); i++)
		{
		    if (bothName.get(i).equals(parent))
		    {
		    	classNu = i;
		    }
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

    //Vererbung der angegebenen Klasse auslesen
    private void exClassConnection(int i, int countIN)
    {
		String parent = "keinem";
		ClassConnection cc;
	
	//Pattern um den Namen auszulesen erstellen
	Matcher mEx = Pattern.compile("extends +([a-zA-Z0-9]*).*\\{").matcher(classConnection.get(i));
	while (mEx.find())
	{
	    parent = mEx.group(1);
	    System.out.println(bothNumber(parent));
	}
	System.out.println("Klasse " + bothName.get(i + countIN) + " erbt von " + bothName.get(bothNumber(parent)));
	
	//Hinzufuegen der Verbindung ins Array
	cc = new ClassConnection((i + countIN), bothNumber(parent), extensionType);
	classConnectionArray.add(cc);
	//System.out.println("G " + classConnectionArray.size());
    }

    /*
     * Herauslesen von mittels implements eingebundenen Interfaces aus einer Klasse 
     */
    private void impClassConnection(int i, int countIN)
    {

	String interface1 = "keinem";
	String followingInterfaces = "keinem";
	ClassConnection cc;
	//Erstellung des Patterns um Namen nach dem Keyword "implements" auszulesen
	Matcher mImp1 = Pattern.compile("implements +([a-zA-Z0-9]*) *( *, *[a-zA-Z0-9]*)* *.*\\{")
		.matcher(classConnection.get(i));
	if (mImp1.find())
	{
	    interface1 = mImp1.group(1); // das erste Interface
	    //Hinzufuegen einer neuen Verbindung
	    cc = new ClassConnection((i + countIN), bothNumber(interface1), extensionType);
	    classConnectionArray.add(cc);
	    
	    //wenn nach dem Namen ein Komma steht kommen weitere Interfaces die eingebunden werden
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
		//hinzufuegen der Verbindung zum Array
		cc = new ClassConnection((i + countIN), bothNumber(interface1), extensionType);
		classConnectionArray.add(cc);
		System.out.println("G " + classConnectionArray.size());
	    }
	    System.out.println("test");
	}
    }

    /*
     * Herauslesen von eingebundenen Interfaces in andere Interfaces mittels extends
     */
    private void exInterfaceConnection(int i)
    {
		String interface1 = "keinem";
		String followingInterfaces = "keins";
		ClassConnection cc;
		//Erstellen des Patterns um bei Interfaces auszulesen, ob es von einem oder mehreren Interfaces "erbt"
		Matcher mEx1 = Pattern.compile("extends +([a-zA-Z0-9]*) *( *, *[a-zA-Z0-9]*)* *.*\\{")
			.matcher(interfaceConnection.get(i));
		if (mEx1.find())
		{
		    interface1 = mEx1.group(1); // das erste Interface
		    System.out.println("jap");
		    //Hinzufuegen einer Verbindung
		    cc = new ClassConnection((i), bothNumber(interface1), extensionType);
		    classConnectionArray.add(cc);
		    
		    //bei folgendem "," ist von folgenden Interfacenamen auszugehen, die man noch auslesen und hinzufuegen muss
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
			//Neue Verbindung wird hinzugefuegt
			cc = new ClassConnection((i), bothNumber(interface1), extensionType);
			classConnectionArray.add(cc);
		    }
		    System.out.println("test");
		}
    }
    /**
     * Liefert die Ergebnisse des Parsens zurueck
     * 
     * @return Struktur mit den ergebnisse des Parsens
     */
    public ParsingResult getParsingResult()
    {
	return result;
    }

}
