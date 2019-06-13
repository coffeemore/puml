import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    public void SetUp() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException
    {
	classUnderTest = new SequenceDiagramGenerator();
	parsedData = xmlHM.getDocumentFrom("..//code//testfolder//xmlSpecifications//parsedData.xml");

    }

    @Test
    void test1() throws TransformerException, XPathExpressionException, ParserConfigurationException, SAXException,
	    IOException
    {
	assertAll(() ->
	{

	    Document test = classUnderTest.createDiagram(parsedData, "Class1", "method1");
	    Document seqDiagram = xmlHM.getDocumentFrom("..//code//testfolder//xmlSpecifications//SeqDiagram.xml");
	    boolean s = false;
	    s = xmlHM.compareXML(seqDiagram, test);
	    assertTrue(s);
	}, () ->
	{
	    Document test = classUnderTest.createDiagram(parsedData, "Class2", "method1");
	    Document seqDiagram = xmlHM.getDocumentFrom("..//code//testfolder//xmlSpecifications//SeqDiagram.xml");
	    boolean s = false;
	    s = xmlHM.compareXML(seqDiagram, test);
	    assertFalse(s);
	});
    }
}
