

/**
 * 
 * @author
 * Interface für das Parsen von Quellcode
 */
public interface ParserIf
{

    public void parse(String sourceCode);
    
    public ParsingResult getParsingResult();
    
    

}
