
import java.util.Vector;
import java.util.regex.*;

/**
 * 
 * @author Klasse die den Parser für Java implementiert
 */
public class ParserJava implements ParserIf
{

    private ParsingResult result = null;
    private Vector<String> className = new Vector();

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


	//Matcher classMatcher = Pattern.compile("(((class)([\t]+))(.&&[^\t]&&[^\\{])*)([\t]*)(\\{)").matcher(sourceCode);
	Matcher classMatcher = Pattern.compile("class +([a-zA-Z0-9])* *\\{").matcher(sourceCode);
	while (classMatcher.find())
	{
	    System.out.println(classMatcher.group());
	    className.add(classMatcher.group());   
	}
	System.out.println(className.size());
	int countN = className.size();	
	
	Matcher classImplementsMatcher = Pattern.compile("class +([a-zA-Z0-9])* *implements +([a-zA-Z0-9])* *\\{").matcher(sourceCode);
	while (classImplementsMatcher.find())
	{
	    System.out.println(classImplementsMatcher.group());
	    className.add(classImplementsMatcher.group());  
	}
	System.out.println(className.size());
	int countI = className.size();	
	
	Matcher classExtendsMatcher = Pattern.compile("class +([a-zA-Z0-9])* *extends +([a-zA-Z0-9])* *\\{").matcher(sourceCode);
	while (classExtendsMatcher.find())
	{
	    System.out.println(classExtendsMatcher.group());
	    className.add(classExtendsMatcher.group());
	  
	}
	System.out.println(className.size());
	
	Matcher classBothMatcher = Pattern.compile("class +([a-zA-Z0-9])* *extends +([a-zA-Z0-9])* *implements +([a-zA-Z0-9])* *\\{").matcher(sourceCode);
	while (classBothMatcher.find())
	{
	    System.out.println(classBothMatcher.group());
	    className.add(classBothMatcher.group());
	  
	}
	System.out.println(className.size());
	
	
	for (int i = 0; i < countN; i++)
	{
	    className.set(i, className.get(i).replace("class ",""));
	    className.set(i, className.get(i).replace(" ",""));
	    className.set(i, className.get(i).replace("{",""));
	    System.out.println(className.get(i));
	}
	

//	Pattern containsImplement = Pattern.compile(".*implements.*\\{");
//	Pattern containsExtends = Pattern.compile(".*extends.*\\{");

//	for (int i = 0; i < className.size(); i++)
//	{
//	    if (containsImplement.matches(regex, className.elementAt(i)))
//	    {
//		System.out.println("I " + className.elementAt(i));
//	    }
//	    else if (className.elementAt(i) == containsExtends)
//	    {
//		System.out.println("E" + className.elementAt(i));
//	    }
//	    else
//	    {
//		System.out.println("N " + className.elementAt(i));
//	    }
//
//	}

//	String text= "Das ist ein 'Test' !" ;
//	Matcher matcher = Pattern.compile("'.*'").matcher(text);
//	while (matcher.find())
//	{
//		System.out.println(matcher.group());
//	    
//	}

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
