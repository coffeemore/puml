import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import net.sourceforge.plantuml.FileUtils;

class OutputPUMLTest_sequencedia
{
    XmlHelperMethods xmlHM = new XmlHelperMethods();

    Document doc = xmlHM.getDocumentFrom("testfolder/xmlSpecifications/SeqDiagram.xml");
	
	public OutputPUML output = new OutputPUML();
	
    @Test
    void testGetPUML() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException
    {
		// GetPuml testen
		String actual = new OutputPUML().getPUML(doc);
		String expected =
				"@startuml\n" + 
				"participant Class1\n" + 
				"participant Class2\n" + 
				"participant Class3\n" + 
				"note over Class1: method1\n" + 
				"activate Class1\n" + 
				"alt if(a!=b)\n" + 
				"Class1 -> Class2: call1 (classInstance1)\n" + 
				"activate Class2\n" + 
				"Class2 --> Class1\n" + 
				"deactivate Class2\n" + 
				"else else\n" + 
				"loop for(int i=0; i!=100; i++)\n" + 
				"Class1 -> Class2: call2\n" + 
				"activate Class2\n" + 
				"Class2 --> Class1\n" + 
				"deactivate Class2\n" + 
				"end\n" + 
				"end\n" + 
				"Class1 -> Class1: method2\n" + 
				"activate Class1\n" + 
				"alt switch(i)/case 0\n" + 
				"loop while(j!=0)\n" + 
				"Class1 -> Class1: method3\n" + 
				"activate Class1\n" + 
				"alt if(a!=b)\n" + 
				"Class1 ->o Class1: method3\n" + 
				"end\n" + 
				"alt if(b!=c)\n" + 
				"Class1 -> Class1: method4\n" + 
				"activate Class1\n" + 
				"alt if(d!=e)\n" +
				"Class1 ->o Class1: method3\n" + 
				"end\n" + 
				"deactivate Class1\n" + 
				"end\n" + 
				"deactivate Class1\n" + 
				"end\n" + 
				"else switch(i)/case 1\n" + 
				"loop do/while(j!=0)\n" + 
				"Class1 [#0000FF]->> Class1: method3\n" + 
				"activate Class1\n" + 
				"deactivate Class1\n" + 
				"end\n" + 
				"else switch(i)/default\n" + 
				"Class1 -> Class3: call1 (classInstance1)\n" + 
				"activate Class3\n" + 
				"Class3 --> Class1\n" + 
				"deactivate Class3\n" + 
				"Class1 -> Class2: call1 (classInstance1)\n" + 
				"activate Class2\n" + 
				"Class2 --> Class1\n" + 
				"deactivate Class2\n" + 
				"Class1 -> Class2: call1 (classInstance2)\n" + 
				"activate Class2\n" + 
				"Class2 --> Class1\n" + 
				"deactivate Class2\n" + 
				"Class1 -> Class2: call1 (classInstance3)\n" + 
				"activate Class2\n" + 
				"Class2 --> Class1\n" + 
				"deactivate Class2\n" + 
				"Class1 ->x]: callX (classInstance4)\n" + 
				"end\n" + 
				"deactivate Class1\n" + 
				"deactivate Class1\n" + 
				"@enduml";
		actual = new OutputPUML().getPUML(doc);
		assertEquals(expected, actual);
		//System.out.println("Hier is der Code: \n" + actual + "oder so" + expected);
    }
    
    @Test
    // expectedFile.txt befindet sich fuer den Test im srcTest Ordner um in Git
    // aufgenommen zu werden.
    void testSavePUMLtoFile() throws IOException, ParserConfigurationException, SAXException, XPathExpressionException
    {
		File expected = new File("testfolder/xmlSpecifications/SeqDiagram.txt");
		// savePumltoFile testen
		// TODO Aendern der Filepaths bevor Test (je nach System), expectedFile
		// platzieren
		output.savePUMLtoFile(output.getPUML(doc), "testfolder/xmlSpecifications/actualSeqFile.txt");
		File actual = new File("testfolder/xmlSpecifications/actualSeqFile.txt");
		assertEquals(FileUtils.readFile(actual), FileUtils.readFile(expected));
    }

    /*  @Test
    void testCreatePUMLfromFile() throws IOException
    {
		// TODO Aendern der Filepaths bevor Test (je nach System)
		File expected = new File("testfolder/xmlSpecifications/SeqDiagram.png");
		output.createPUMLfromFile("testfolder/xmlSpecifications/actualSeqFile.txt", "SeqD_fromFile");
		File actual = new File("testfolder/xmlSpecifications/SeqD_fromFile/SeqD_fromFile.png");
		assertEquals(FileUtils.readFile(actual), FileUtils.readFile(expected));
	}*/
	
    @Test
    void testCreatePUMLfromString() throws IOException, ParserConfigurationException, SAXException
    {
	// TODO Aendern der Filepaths bevor Test (je nach System)
	File expected = new File("testfolder/xmlSpecifications/SeqDiagram.png");
	try {
		output.createPUMLfromString("testfolder/xmlSpecifications/SeqD_fromString.png", output.getPUML(doc));
	} catch (XPathExpressionException | IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	File actual = new File("testfolder/xmlSpecifications/SeqD_fromString.png");
	assertEquals(FileUtils.readFile(actual), FileUtils.readFile(expected));
    }
}
