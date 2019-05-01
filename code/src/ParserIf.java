import org.w3c.dom.Document;

/**
 * 
 * @author
 * Interface für das Parsen von Quellcode
 */
public interface ParserIf
{

    public void parse(String sourceCode);
    
    public Document getParsingResult();
    /**
     * Alte Deklaration
    public ParsingResult getParsingResult();
    */
}
