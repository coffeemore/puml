
import java.util.Vector;
import java.util.regex.*;

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
    private Vector<String> bothName = new Vector<String>();
    private Vector<ClassConnection> classConnectionArray = new Vector<ClassConnection>();
    private ClassConnection.connectionType extensionType = ClassConnection.connectionType.extension;
    private ClassConnection.connectionType aggregationType = ClassConnection.connectionType.aggregation;
    private ClassConnection.connectionType compositionType = ClassConnection.connectionType.composition;

    /**
     * Konstruktor
     */
    public ParserJava()
    {

    };

    /**
     * Liest den übergebenen Quellcode ein und parst die Informationen daraus
     * 
     * @param sourceCode Vollständigen Java-Quellcode
     */
    public void parse(String sourceCode)
    {

	// System.out.println(sourceCode); &&[^(\\t*)]
	this.result = new ParsingResult();
	ClassConnection cc;
	// Matcher classMatcher =
	// Pattern.compile("(((class)([\t]+))(.&&[^\t]&&[^\\{])*)([\t]*)(\\{)").matcher(sourceCode);
	int countN = 0;
	// Auslesen aller Interfacenamen
	Matcher interfaceMatcher = Pattern.compile("interface +([a-zA-Z0-9]*).*\\{").matcher(sourceCode);
	System.out.println("Interfaces: ");
	while (interfaceMatcher.find())
	{
	    interfaceName.add(interfaceMatcher.group(1));
	    bothName.add(interfaceMatcher.group(1));
	    interfaceConnection.add(interfaceMatcher.group());
	    System.out.println(interfaceMatcher.group(1));
	    countN++;
	}
	System.out.println("Anzahl " + interfaceName.size());
	int countIN = countN;
	
	for (int i = 0; i < interfaceConnection.size(); i++)
	{
	    if (interfaceConnection.get(i).indexOf("extends") > 1)
	    {
		exInterfaceConnection(i);
	    }
	}

	// Auslesen aller Klassennamen
	Matcher classMatcher = Pattern.compile("class +([a-zA-Z0-9]*).*\\{").matcher(sourceCode);
	System.out.println("Klassen: ");
	while (classMatcher.find())
	{
	    className.add(classMatcher.group(1));
	    bothName.add(classMatcher.group(1));
	    classConnection.add(classMatcher.group());
	    System.out.println(classMatcher.group(1));
	    countN++;
	}
	System.out.println("Anzahl " + className.size());
	

	// Auslesen der Verbindungen zwischen Klassen und Interfaces
	for (int i = 0; i < classConnection.size(); i++)
	{
	    // Auslesen und zuweisen von Verbindungen die Interfaces implementieren und/oder
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
		    // System.out.println("Klasse " + (i + countIN) + " implementiert");

		    // System.out.println(classConnection.get(i).substring(classConnection.get(i).indexOf("implements")));
		}

	    }
	    else if (classConnection.get(i).indexOf("extends") > 1)
	    {

		exClassConnection(i, countIN);

	    }
	    else
	    {
		System.out.println("Klasse " + (i + countIN) + " implementiert nicht und erbt nicht ");
	    }
	}

	for (int i = 0; i < classConnectionArray.size(); i++)
	{
	    System.out.println("Verbindung " + i + " : von " + classConnectionArray.get(i).getFrom() + " nach "
		    + classConnectionArray.get(i).getTo() + " .");
	}

    }

    private int bothNumber(String parent)
    {
	int classNu = -1;
	for (int i = 0; i < bothName.size(); i++)
	{
	    if (bothName.get(i).equals(parent))
	    {
		classNu = i ;
	    }
	}
	return classNu;
    }

//    private int interfaceNumber(String interName)
//    {
//	int interNu = -1;
//	for (int i = 0; i < interfaceName.size(); i++)
//	{
//	    if (className.get(i).equals(interName))
//	    {
//		interNu = i;
//	    }
//	}
//	return interNu;
//    }

    private void exClassConnection(int i, int countIN)
    {
	String parent = "keinem";
	ClassConnection cc;
	Matcher mEx = Pattern.compile("extends +([a-zA-Z0-9]*).*\\{").matcher(classConnection.get(i));
	while (mEx.find())
	{
	    parent = mEx.group(1);
	    // System.out.println(parent);
	    System.out.println(bothNumber(parent));
	}
	System.out.println("Klasse " + bothName.get(i + countIN) + " erbt von " + bothName.get(bothNumber(parent)));
	cc = new ClassConnection((i + countIN), bothNumber(parent), extensionType);
	classConnectionArray.addElement(cc);
	System.out.println("G " + classConnectionArray.size());
    }

    private void impClassConnection(int i, int countIN)
    {

	String interface1 = "keinem";
	String followingInterfaces = "keinem";
	ClassConnection cc;
	Matcher mImp1 = Pattern.compile("implements +([a-zA-Z0-9]*) *( *, *[a-zA-Z0-9]*)* *.*\\{")
		.matcher(classConnection.get(i));
	if (mImp1.find())
	{
	    interface1 = mImp1.group(1); // das erste Interface
	    cc = new ClassConnection((i + countIN), bothNumber(interface1), extensionType);
	    classConnectionArray.addElement(cc);
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
		cc = new ClassConnection((i + countIN), bothNumber(interface1), extensionType);
		classConnectionArray.addElement(cc);
		System.out.println("G " + classConnectionArray.size());
	    }
	    System.out.println("test");
	}
    }

    private void exInterfaceConnection(int i)
    {
	String interface1 = "keinem";
	String followingInterfaces = "keins";
	ClassConnection cc;
	Matcher mEx1 = Pattern.compile("extends +([a-zA-Z0-9]*) *( *, *[a-zA-Z0-9]*)* *.*\\{")
		.matcher(interfaceConnection.get(i));
	if (mEx1.find())
	{
	    interface1 = mEx1.group(1); // das erste Interface
	    System.out.println("jap");
	    cc = new ClassConnection((i), bothNumber(interface1), extensionType);
	    classConnectionArray.addElement(cc);
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
		cc = new ClassConnection((i), bothNumber(interface1), extensionType);
		classConnectionArray.addElement(cc);
	    }
	    System.out.println("test");
	}
    }

    /**
     * Liefert die Ergebnisse des Parsens zurück
     * 
     * @return Struktur mit den ergebnisse des Parsens
     */
    public ParsingResult getParsingResult()
    {
	return result;
    }

}
