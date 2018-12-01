import java.util.regex.*;

/**
 * 
 * @author Klasse die den Parser für Java implementiert
 */
public class ParserJava implements ParserIf
{

    private ParsingResult result = null;

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
	// System.out.println(sourceCode);
	this.result = new ParsingResult();

	Matcher classMatcher = Pattern.compile("class .*\\{").matcher(sourceCode);
	while (classMatcher.find())
	{
	    System.out.println(classMatcher.group());
	}

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
