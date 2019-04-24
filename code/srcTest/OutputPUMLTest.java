import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import net.sourceforge.plantuml.FileUtils;

class OutputPUMLTest
{

    @Test
    void testGetPUML()
    {
	FileReader fr = new FileReader("/home/tore/Documents/Softwareprojekt/puml/code/testfolder/ClassDiagramExample.xml");
	
	// ClassConnection Elemente erstellen
	ClassConnection elA = new ClassConnection(1, 4, ClassConnection.connectionType.aggregation);

	ClassConnection elB = new ClassConnection(1, 2, ClassConnection.connectionType.extension);

	ClassConnection elC = new ClassConnection(2, 3, ClassConnection.connectionType.composition);

	// classes fuellen
	ArrayList<String> classes = new ArrayList<String>();
	classes.add("BeispielKlasse1");
	classes.add("BeispielKlasse2");
	classes.add("BeispielKlasse3");
	classes.add("BeispielKlasse4");
	classes.add("BeispielKlasse5");

	// classConnections fuellen
	ArrayList<ClassConnection> classConnections = new ArrayList<ClassConnection>();
	classConnections.add(elA);
	classConnections.add(elB);
	classConnections.add(elC);

	// ParsingResult erstellen
	ParsingResult actualParsTest = new ParsingResult(classes, classConnections);

	// GetPuml testen
	String actual = new OutputPUML().getPUML(createXMLStreamReader(fr));

	String expected = "@startuml\nclass BeispielKlasse1\nclass BeispielKlasse2\nclass BeispielKlasse3\nclass BeispielKlasse4\nclass BeispielKlasse5\nBeispielKlasse2 o-- BeispielKlasse5\nBeispielKlasse2 --|> BeispielKlasse3\nBeispielKlasse3 *-- BeispielKlasse4\n@enduml";
	assertEquals(expected, actual);
    }

    @Test
    // expectedFile.txt befindet sich fuer den Test im srcTest Ordner um in Git
    // aufgenommen zu werden.
    void testSavePUMLtoFile() throws IOException
    {
	File expected = new File("/home/tore/Test/expectedFile.txt");

	// ClassConnection Elemente erstellen
	ClassConnection elA = new ClassConnection(1, 4, ClassConnection.connectionType.aggregation);

	ClassConnection elB = new ClassConnection(1, 2, ClassConnection.connectionType.extension);

	ClassConnection elC = new ClassConnection(2, 3, ClassConnection.connectionType.composition);

	// classes fuellen
	ArrayList<String> classes = new ArrayList<String>();
	classes.add("BeispielKlasse1");
	classes.add("BeispielKlasse2");
	classes.add("BeispielKlasse3");
	classes.add("BeispielKlasse4");
	classes.add("BeispielKlasse5");

	// classConnections fuellen
	ArrayList<ClassConnection> classConnections = new ArrayList<ClassConnection>();
	classConnections.add(elA);
	classConnections.add(elB);
	classConnections.add(elC);

	// ParsingResult erstellen
	ParsingResult actualParsTest = new ParsingResult(classes, classConnections);

	// savePumltoFile testen
	// TODO Aendern der Filepaths bevor Test (je nach System), expectedFile
	// platzieren
	OutputPUML output = new OutputPUML();
	output.savePUMLtoFile(output.getPUML(actualParsTest), "/home/tore/Test/actualFile.txt");

	File actual = new File("/home/tore/Test/actualFile.txt");
	assertEquals(FileUtils.readFile(actual), FileUtils.readFile(expected));
    }

    @Test
    void testCreatePUMLfromFile() throws IOException
    {
	// TODO Aendern der Filepaths bevor Test (je nach System)
	File expected = new File("/home/tore/Test/expectedFile.png");
	OutputPUML output = new OutputPUML();
	output.createPUMLfromFile("/home/tore/Test/actualFile.txt", "/home/tore/Test/Test/actualFile.png");
	File actual = new File("/home/tore/Test/Test/actualFile.png");
	assertEquals(FileUtils.readFile(actual), FileUtils.readFile(expected));
    }

    @Test
    void testCreatePUMLfromString() throws IOException
    {

	// ClassConnection Elemente erstellen
	ClassConnection elA = new ClassConnection(1, 4, ClassConnection.connectionType.aggregation);
	ClassConnection elB = new ClassConnection(1, 2, ClassConnection.connectionType.extension);
	ClassConnection elC = new ClassConnection(2, 3, ClassConnection.connectionType.composition);

	// classes fuellen
	ArrayList<String> classes = new ArrayList<String>();
	classes.add("BeispielKlasse1");
	classes.add("BeispielKlasse2");
	classes.add("BeispielKlasse3");
	classes.add("BeispielKlasse4");
	classes.add("BeispielKlasse5");

	// classConnections fuellen
	ArrayList<ClassConnection> classConnections = new ArrayList<ClassConnection>();
	classConnections.add(elA);
	classConnections.add(elB);
	classConnections.add(elC);

	// ParsingResult erstellen
	ParsingResult actualParsTest = new ParsingResult(classes, classConnections);

	// TODO Aendern der Filepaths bevor Test (je nach System)
	File expected = new File("/home/tore/Test/expectedFile.png");
	OutputPUML output = new OutputPUML();
	File actual = new File("/home/tore/Test/actualFile.png");
	output.createPUMLfromString("/home/tore/Test/actualFile.png", output.getPUML(actualParsTest));
	assertEquals(FileUtils.readFile(actual), FileUtils.readFile(expected));
    }
}
