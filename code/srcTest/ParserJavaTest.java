
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ParserJavaTest
{

    private ParserJava parserTest= new ParserJava();
    File xmlFile1;
    File xmlFile2;
    XmlHelperMethods xmlHM = new XmlHelperMethods();

    private Document parsedData;

    @BeforeEach
    public void SetUp() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException
    {
	String sc = new String();
	BufferedReader buffr = null;
	FileReader filer = null;
	File file = null;

	try
	    {
		// alle Dateien werden eingelesen

		    filer = new FileReader(System.getProperty("user.home") + "/tempLogger/" + "PUMLsource.dat");
		    buffr = new BufferedReader(filer);
		    String currLine;

		    while ((currLine = buffr.readLine()) != null)
		    {
			sc += currLine;
			sc += "\n";
		    }
		
	    } catch (IOException e)
	    {
		e.printStackTrace();
	    } finally
	    {
		// BufferedReader und FileReader werden geschlossen
		try
		{
		    if (filer != null)
		    {
			filer.close();
		    }
		    if (buffr != null)
		    {
			buffr.close();
		    }
		} catch (IOException ex)
		{
		    ex.printStackTrace();
		}
	    }
	
	
	
	ArrayList<String> pumlSourceCode = new ArrayList<String>();
	pumlSourceCode.add(sc);
		
	parserTest.parse(pumlSourceCode);
	

    }

    @Test
    void test1() throws TransformerException, XPathExpressionException, ParserConfigurationException, SAXException,
	    IOException, NullPointerException
    {
	assertAll(() ->
	{
//	    Document test = parserTest.createDiagram(parsedData, "Class1", "method1");
	    Document parsedData = xmlHM.getDocumentFrom("..//code//testfolder//xmlSpecifications//parsedData.xml");
	    boolean s = false;
	    s = xmlHM.compareXML(parsedData, parserTest.getParsingResult());
	    assertTrue(s);
	}, () ->
	{
//	    Document test = parserTest.createDiagram(parsedData, "Class2", "method1");
	    Document parsedData = xmlHM.getDocumentFrom("..//code//testfolder//xmlSpecifications//parsedData.xml");
	    boolean s = false;
	    s = xmlHM.compareXML(parsedData, parserTest.getParsingResult());
	    assertFalse(s);
	});
    }

}
