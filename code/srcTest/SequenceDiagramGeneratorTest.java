import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

class SequenceDiagramGeneratorTest
{
    private SequenceDiagramGenerator classUnderTest; 
    File xmlFile;
    XmlHelperMethods xmlHM = new XmlHelperMethods();
    
    private Document parsedData;

	public void SetUp() throws Exception
	    {
	    	
		classUnderTest = new SequenceDiagramGenerator();
		xmlFile = new File("home//developer//workspace//puml//code//testfolder//xmlSpecifications//parsedData.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document parsedData = dBuilder.parse(xmlFile);
		classUnderTest.createDiagram(parsedData);
		classUnderTest.listAllNodes(classUnderTest.root1);
	    }

    @Test
    void testCreateDiagram()
    {
	try
	{
	    classUnderTest.createDiagram(parsedData);
	} catch (ParserConfigurationException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
    }

//  @Test
//  void testSequenceDiagramGenerator()
//  {
//	fail("Not yet implemented");
//  }

}
