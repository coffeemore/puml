import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
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
	// Test, ob das erstellte Document mit der Spezifikation übereinstimmt
	assertAll(() ->
	{
	    Document test = classUnderTest.createDiagram(parsedData, "Class1", "method1");
	    test = classUnderTest.createDiagram(parsedData, "Class1", "method1");

	    Document seqDiagram = xmlHM.getDocumentFrom("..//code//testfolder//xmlSpecifications//SeqDiagram.xml");
	    boolean s = false;
	    s = xmlHM.compareXML(seqDiagram, test);
	    assertTrue(s);
	}, () ->
	// Test, ob das erstellte Document NICHT mit der Spezifikation übereinstimmt
	{
	    Document test = classUnderTest.createDiagram(parsedData, "Class2", "method1");
	    Document seqDiagram = xmlHM.getDocumentFrom("..//code//testfolder//xmlSpecifications//SeqDiagram.xml");
	    boolean s = false;
	    s = xmlHM.compareXML(seqDiagram, test);
	    assertFalse(s);
	});
    }

    // Test mit dem vom Parser erstellen Dokument
    // Voraussetzung, dass die Datei mit den richtigen Daten vorhanden ist
    @Test
    void test2() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException,
	    TransformerException
    {
	Document source = xmlHM.getDocumentFrom("//home//developer//tempLogger//PUMLlog.xml");

	Document test = classUnderTest.createDiagram(source, "Class1", "method1");
	Document seqDiagram = xmlHM.getDocumentFrom("..//code//testfolder//xmlSpecifications//SeqDiagram.xml");
	boolean m = xmlHM.compareXML(seqDiagram, test);
	assertTrue(m);
    }

    @Test
    void testEPClassFirst() throws XPathExpressionException, TransformerException
    {
	Document seqDiagram = xmlHM.getDocumentFrom("..//code//testfolder//xmlSpecifications//SeqDiaTest.xml");
	seqDiagram = classUnderTest.sortClasses(seqDiagram, "Class3");

	boolean s = true;
	NodeList listed = xmlHM.getList(seqDiagram, "/parsed/sequencediagram/classes/entry");
	if (!listed.item(0).getTextContent().equals("Class3"))
	{
	    s = false;
	}
	for (int i = 1; i < listed.getLength() - 1; i++)
	{
	    if (listed.item(i).getTextContent().compareTo(listed.item(i + 1).getTextContent()) > 0)
	    {
		s = false;
	    }
	}

	assertTrue(s);
    }
}
