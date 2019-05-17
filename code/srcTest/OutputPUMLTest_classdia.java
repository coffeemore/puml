import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import net.sourceforge.plantuml.FileUtils;

class OutputPUMLTest_classdia
{
	public Document getDoc() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuild = docBuildFact.newDocumentBuilder();
		Document doc = docBuild.parse(new File("/home/patrick/Studium/Code/puml/code/testfolder/xmlSpecifications/ClassDiagram.xml"));
		return doc;
	}
	public OutputPUMLTest_classdia(){
	}
    @Test
    void testGetPUML() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException
    {
    
		// GetPuml testen
		String actual = "";
		actual = new OutputPUML().getPUML(getDoc());
		String expected = "@startuml\n" + 
				"class Class1\n" + 
				"class Class2\n" + 
				"class Class3\n" + 
				"class Class4\n" + 
				"class Class5\n" + 
				"interface If1\n" + 
				"interface If2\n" + 
				"Class3 <|-- Class1\n" +
				"If1 <|-- Class1\n" + 
				"If2 <|-- Class1\n" +  
				"Class1 *-- Class2\n" + 
				"Class1 *-- Class3\n" + 
				"Class1 o-- Class4\n" + 
				"Class1 o-- Class5\n" + 
				"@enduml";
		assertEquals(expected, actual);
		//System.out.println("Hier is der Code: \n" + actual + "oder so" + expected);
    }
    
    @Test
    // expectedFile.txt befindet sich fuer den Test im srcTest Ordner um in Git
    // aufgenommen zu werden.
    void testSavePUMLtoFile() throws IOException, ParserConfigurationException, SAXException, XPathExpressionException
    {
		File expected = new File("/home/patrick/Studium/Code/puml/code/testfolder/xmlSpecifications/ClassDiagram.txt");
		// savePumltoFile testen
		// TODO Aendern der Filepaths bevor Test (je nach System), expectedFile
		// platzieren
		OutputPUML output = new OutputPUML();
		output.savePUMLtoFile(output.getPUML(getDoc()), "/home/patrick/Studium/Code/puml/code/testfolder/xmlSpecifications/actualFile.txt");
		File actual = new File("/home/patrick/Studium/Code/puml/code/testfolder/xmlSpecifications/actualFile.txt");
		assertEquals(FileUtils.readFile(actual), FileUtils.readFile(expected));
    }

    @Test
    void testCreatePUMLfromFile() throws IOException
    {
		// TODO Aendern der Filepaths bevor Test (je nach System)
		File expected = new File("/home/patrick/Studium/Code/puml/code/testfolder/xmlSpecifications/ClassDiagram.png");
		OutputPUML output = new OutputPUML();
		output.createPUMLfromFile("/home/patrick/Studium/Code/puml/code/testfolder/xmlSpecifications/ClassDiagram.txt", "/home/patrick/Studium/Code/puml/code/testfolder/xmlSpecifications/ClassD_fromFile.png");
		File actual = new File("/home/patrick/Studium/Code/puml/code/testfolder/xmlSpecifications/ClassD_fromFile.png");
		assertEquals(FileUtils.readFile(actual), FileUtils.readFile(expected));
	}
	
    @Test
    void testCreatePUMLfromString() throws IOException, ParserConfigurationException, SAXException
    {
	// TODO Aendern der Filepaths bevor Test (je nach System)
	File expected = new File("/home/patrick/Studium/Code/puml/code/testfolder/xmlSpecifications/ClassDiagram.png");
	OutputPUML output = new OutputPUML();
	try {
		output.createPUMLfromString("/home/patrick/Studium/Code/puml/code/testfolder/xmlSpecifications/ClassD_fromString.png", output.getPUML(getDoc()));
	} catch (XPathExpressionException | IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	File actual = new File("/home/patrick/Studium/Code/puml/code/testfolder/xmlSpecifications/ClassD_fromString.png");
	assertEquals(FileUtils.readFile(actual), FileUtils.readFile(expected));
    }
}
