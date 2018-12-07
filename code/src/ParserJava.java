
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

	// Matcher classMatcher =
	// Pattern.compile("(((class)([\t]+))(.&&[^\t]&&[^\\{])*)([\t]*)(\\{)").matcher(sourceCode);
	Matcher classMatcher = Pattern.compile("class +([a-zA-Z0-9]*).*\\{").matcher(sourceCode);
	while (classMatcher.find())
	{
	    className.add(classMatcher.group(1));
	}
	System.out.println(className.size());
	int countN = 0;

	for (int i = 0; i < className.size(); i++)
	{
	    System.out.println(className.get(i));
	}

	Matcher classImpExMatcher = Pattern.compile("class +.*\\{").matcher(sourceCode);
	while (classImpExMatcher.find())
	{
	    System.out.println(countN + " " + classImpExMatcher.group());
	    classConnection.add(classImpExMatcher.group());
	    countN++;
	}

	for (int i = 0; i < classConnection.size(); i++)
	{
	    if (classConnection.get(i).indexOf("implements") > 1)
	    {

		if (classConnection.get(i).indexOf("extends") > 1)
		{
		    System.out.println("String " + i + " enthält implements und extends");

		}
		else
		{
		    impConnection(classConnection.get(i));
		    System.out.println("String " + i + " enthält implements ");

		    // System.out.println(classConnection.get(i).substring(classConnection.get(i).indexOf("implements")));
		}

	    }
	    else if (classConnection.get(i).indexOf("extends") > 1)
	    {
		System.out.println("String " + i + " enthält extends");
		String parent;
		Matcher mEx = Pattern.compile("extends +([a-zA-Z0-9]*).*\\{").matcher(classConnection.get(i));
		while(mEx.find()) {
		    parent = mEx.group(1);
		    System.out.println(parent);
		}
		
	    }
	    else
	    {
		System.out.println("String " + i + " enthält kein implements oder extends");
	    }
	}
    }

    private void impConnection(String impString)
    {
	// TODO Auto-generated method stub

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


