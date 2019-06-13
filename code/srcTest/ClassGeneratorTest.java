import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

/**
 * Klasse fuer den Test des ClassDaigrammGenerators
 */
class ClassGeneratorTest
{
	private PUMLgenerator classUnderTest;

	@SuppressWarnings("static-access")
	@Test
	void test()
	{
		//Vorgabe und Element generieren
		Document testDoc = classUnderTest.xmlHelper.getDocumentFrom("testfolder/xmlSpecifications/parsedData.xml");
    	Document output = classUnderTest.classDiagramGenerator.createDiagram(testDoc);
    	
    	classUnderTest.xmlHelper.writeDocumentToConsole(testDoc);
    	classUnderTest.xmlHelper.writeDocumentToConsole(output);
		assertTrue(false);
	}

}
