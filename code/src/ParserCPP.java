import org.w3c.dom.Document;

public class ParserCPP implements ParserIf 
{
	private Document document;
	
	//Getter- und Settermethoden 
	public Document getDocument() 
	{
		return document;
	}
	public void setDocument(Document document) 
	{
		this.document = document;
	}
	
	//Hilfsklasse TokenResult: 
	class TokenResult
    {
		private int foundToken;
		private String data, sourceCode;
		
		public TokenResult(int foundToken, String data, String sourceCode)
		{
		    super();
		    this.foundToken = foundToken;
		    this.data = data;
		    this.sourceCode = sourceCode;
		}
	
		//Getter- und Settermethoden
		public int getFoundToken() 
		{
			return foundToken;
		}
		public void setFoundToken(int foundToken) 
		{
			this.foundToken = foundToken;
		}
		public String getData() 
		{
			return data;
		}
		public void setData(String data) 
		{
			this.data = data;
		}
		public String getSourceCode() 
		{
			return sourceCode;
		}
		public void setSourceCode(String sourceCode) 
		{
			this.sourceCode = sourceCode;
		}
    }
	
	@Override //Parsed den übergeben Source-Code
	public void parse(String sourceCode) 
	{
		// TODO Auto-generated method stub

	}

	@Override //Übergibt das Parsing-Ergebnis (document)
	public Document getParsingResult() 
	{
		return document;
	}

}
