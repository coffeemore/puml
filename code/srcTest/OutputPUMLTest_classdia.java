import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import net.sourceforge.plantuml.FileUtils;

class OutputPUMLTest_classdia
{
    XmlHelperMethods xmlHM = new XmlHelperMethods();

    Document doc = xmlHM.getDocumentFrom("testfolder/xmlSpecifications/ClassDiagram.xml");

    @Test
    void testGetPUML() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException
    {
    	
    	PUMLgenerator.logger.startLogging("testfolder/tempData/PUMLlog/", false);
    	
	// GetPuml testen
	String actual = "";
	actual = new OutputPUML().getPUML(doc);
	String expected = "@startuml\n" + "class Class1{\n" + "-Class2 classInstance1\n" + "#Class2 classInstance2\n"
		+ "+Class4 iClass4\n" + "~Class5 iClass5\n" + "-int var1\n" + "#double var2\n" + "+short var3\n"
		+ "~byte var4\n" + "+Class1(Class4 iClass4, Class5 iClass5)\n"
		+ "-int method1(int param1, int param2)\n" + "#void method2()\n" + "+void method3()\n"
		+ "~void method4()\n" + "}\n" + "class Class2{\n" + "+void call1()\n" + "+{static} void call2()\n"
		+ "}\n" + "class Class3{\n" + "+void call1()\n" + "}\n" + "class Class4\n" + "class Class5\n"
		+ "interface If1{\n" + "+int method1(int param1, int param2)\n" + "}\n" + "interface If2{\n"
		+ "+void method2()\n" + "}\n" + "Class3 <|-- Class1\n" + "If1 <|-- Class1\n" + "If2 <|-- Class1\n"
		+ "Class1 *-- Class2\n" + "Class1 *-- Class3\n" + "Class1 o-- Class4\n" + "Class1 o-- Class5\n"
		+ "@enduml";
	assertEquals(expected, actual);
	//System.out.println("Hier is der Code: \n" + actual + "oder so" + expected);
    }

    @Test
    // expectedFile.txt befindet sich fuer den Test im srcTest Ordner um in Git
    // aufgenommen zu werden.
    void testSavePUMLtoFile() throws IOException, XPathExpressionException
    {
	File expected = new File("testfolder/xmlSpecifications/ClassDiagram.txt");
	OutputPUML output = new OutputPUML();
	output.savePUMLtoFile(output.getPUML(doc), "testfolder/xmlSpecifications/actualFile.txt");
	File actual = new File("testfolder/xmlSpecifications/actualFile.txt");
	assertEquals(FileUtils.readFile(actual), FileUtils.readFile(expected));
    }

    /*@Test
    void testCreatePUMLfromFile() throws IOException
    {
	// TODO Aendern der Filepaths bevor Test (je nach System)
	File expected = new File("testfolder/xmlSpecifications/ClassDiagram.png");
	OutputPUML output = new OutputPUML();
	output.createPUMLfromFile("testfolder/xmlSpecifications/ClassDiagram.txt", "ClassD_fromFile");
	File actual = new File("testfolder/xmlSpecifications/ClassD_fromFile.png");
	assertEquals(FileUtils.readFile(actual), FileUtils.readFile(expected));
    }*/

    @Test
    void testCreatePUMLfromString() throws IOException
    {
		// TODO Aendern der Filepaths bevor Test (je nach System)
		File expected = new File("testfolder/xmlSpecifications/ClassDiagram.png");
		OutputPUML output = new OutputPUML();
		try
		{
		    output.createPUMLfromString("testfolder/xmlSpecifications/ClassD_fromString.png", output.getPUML(doc));
		} 
		catch (XPathExpressionException | IOException e)
		{
			PUMLgenerator.logger.getLog().severe(e.getMessage());
		    e.printStackTrace();
		}
		File actual = new File("testfolder/xmlSpecifications/ClassD_fromString.png");
		try
		{	
		assertEquals(FileUtils.readFile(actual), FileUtils.readFile(expected));
		}
		catch (IOException e)
		{
			PUMLgenerator.logger.getLog().severe(e.getMessage());
		}
    }
}
