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
    private SequenceDiagramGenerator classUnderTest; 
    File xmlFile1;
    File xmlFile2;
    XmlHelperMethods xmlHM = new XmlHelperMethods();
    
    private Document parsedData;

	public void SetUp() throws ParserConfigurationException, SAXException, IOException
	    {
	    	
		classUnderTest = new SequenceDiagramGenerator();
		xmlFile1 = new File("//home//developer//puml-master//code//testfolder//xmlSpecifications//parsedData.xml");
		DocumentBuilderFactory dbFactory1 = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder1 = dbFactory1.newDocumentBuilder();
		parsedData = dBuilder1.parse(xmlFile1);
		
		classUnderTest.createDiagram(parsedData,"Class1","method1");
		//classUnderTest.listAllNodes(classUnderTest.root1);
	    }

    @Test
    void testCreateDiagram() throws ParserConfigurationException, SAXException, IOException
    {
	SetUp();
	assertAll(
		    () -> 
		    {
			
	    Document test = classUnderTest.createDiagram(parsedData,"Class1","method1");
	    
	    xmlFile2 = new File("//home//developer//puml-master//code//testfolder//xmlSpecifications//SeqDiagram.xml");
	    DocumentBuilderFactory dbFactory2 = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dBuilder2 = dbFactory2.newDocumentBuilder();
	    Document seqDiagram = dBuilder2.parse(xmlFile2);
	    System.out.println(seqDiagram.getDocumentElement());
	    xmlHM.removeComments(seqDiagram.getDocumentElement());
	    
	    assertEquals(seqDiagram, test);
	  });
	
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
