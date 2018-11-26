
/**
 * 
 * @author 
 * Klasse die den Parser für Java implementiert
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
     * @param sourceCode Vollständigen Java-Quellcode
     */
    public void parse(String sourceCode)
    {
	System.out.println(sourceCode);
	this.result = new ParsingResult();
	//...
	
    }
    
    /**
     * Liefert die Ergebnisse des Parsens zurück
     * @return Struktur mit den ergebnisse des Parsens
     */
    public ParsingResult getParsingResult()
    {
	return result;
    }

}
