import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

/**
 * Klasse fuer den Test des ClassDaigrammGenerators
 */
class ClassGeneratorTest
{
    //private PUMLgenerator classUnderTest;
    private ClassDiagramGenerator classUnderTest;
    private XmlHelperMethods xmlHM = new XmlHelperMethods();
    private Document parsedData;
    
    @BeforeEach
    public void SetUp() {
	classUnderTest = new ClassDiagramGenerator();
	parsedData = xmlHM.getDocumentFrom("../code/testfolder/xmlSpecifications/parsedData.xml");
    }
    
   
    @Test
    void test()
    {
	// Vorgabe und Element generieren
//	Document testDoc = classUnderTest.xmlHelper.getDocumentFrom("testfolder/xmlSpecifications/parsedData.xml");
//	Document output = classUnderTest.classDiagramGenerator.createDiagram(testDoc);
//
//	classUnderTest.xmlHelper.writeDocumentToConsole(testDoc);
//	classUnderTest.xmlHelper.writeDocumentToConsole(output);
//	assertTrue(false);
    }

    @Test
    void compareXmls()
    {
	assertAll(() ->
	{
	    Document erwErg = xmlHM.getDocumentFrom("../code/testfolder/xmlSpecifications/ClassDiagram.xml");
	    Document aktErg = classUnderTest.createDiagram(parsedData);
	    boolean test = false;
	    test = xmlHM.compareXML(erwErg, aktErg);
	    xmlHM.writeToFile(aktErg);
	    assertTrue(test);
	});
    }

}
