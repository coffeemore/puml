import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

class SequenceDiagramGeneratorTest
{
//    private SequenceDiagramGenerator classUnderTest; 
    File xmlFile;
    XmlHelperMethods xmlHM = new XmlHelperMethods();
    
  

	public void SetUp() throws Exception
	    {
	    	
//		classUnderTest = new SequenceDiagramGenerator();
		
		xmlFile = new File("home//developer//workspace//puml//code//testfolder//xmlSpecifications//parsedData.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document parsedData = dBuilder.parse(xmlFile);
		
//		classUnderTest.listAllNodes(classUnderTest.root1);
	    }

    @Test
    void testCreateDiagram() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException
    {
	
	File xmlFile;
	XmlHelperMethods xmlH = new XmlHelperMethods();
	
	xmlFile = new File("//home//developer//workspace//puml//code//testfolder//xmlSpecifications//parsedData.xml");
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document parsedData = dBuilder.parse(xmlFile);
	
//	Document seqDia = classUnderTest.createDiagram(parsedData, "class1","method1");
	//seqgen.listAllNodes(seqDia.getDocumentElement());
	//seqgen.listAllNodes(parsedData.getDocumentElement());
//	xmlH.writeDocumentToConsole(xmlH.deleteComments(parsedData));
	xmlH.listChildnodeswithName(parsedData, "instance");
	
    }

//  @Test
//  void testSequenceDiagramGenerator()
//  {
//	fail("Not yet implemented");
//  }

}
