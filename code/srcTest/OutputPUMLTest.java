import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import net.sourceforge.plantuml.FileUtils;
import java.io.IOException;

class OutputPUMLTest { 
	
	

	@Test
	void testGetPUML() {
		//ClassConnection Elemente erstellen
		ClassConnection elA = new ClassConnection();
		elA.setConnection(ClassConnection.connectionType.aggregation);
		elA.setFrom(1);
		elA.setTo(4);
		
		ClassConnection elB = new ClassConnection();
		elB.setConnection(ClassConnection.connectionType.extension);
		elB.setFrom(1);
		elB.setTo(2);
		
		ClassConnection elC = new ClassConnection();
		elC.setConnection(ClassConnection.connectionType.composition);
		elC.setFrom(2);
		elC.setTo(3);
		
		//classes fuellen
		ArrayList<String> classes = new ArrayList<String>();
		classes.add("BeispielKlasse1");
		classes.add("BeispielKlasse2");
		classes.add("BeispielKlasse3");
		classes.add("BeispielKlasse4");
		classes.add("BeispielKlasse5");
		
		//classConnections fuellen
		ArrayList<ClassConnection> classConnections = new ArrayList<ClassConnection>();
		classConnections.add(elA);
		classConnections.add(elB);
		classConnections.add(elC);
		
		//ParsingResult erstellen
		ParsingResult actualParsTest = new ParsingResult();
		actualParsTest.classConnections = classConnections;
		actualParsTest.classes = classes;
		
		//GetPuml testen
		String actual = new OutputPUML().getPUML(actualParsTest);
		
		String expected = "@startuml%class BeispielKlasse1%class BeispielKlasse2%class BeispielKlasse3%class BeispielKlasse4%class BeispielKlasse5%BeispielKlasse2 o-- BeispielKlasse5%BeispielKlasse2 -- BeispielKlasse3%BeispielKlasse3 *-- BeispielKlasse4%@enduml";
		
		assertEquals(expected, actual, "Heyyy, hat Jefunzt");
	}

	@Test
	void testSavePUMLtoFile() throws IOException
	{ 
		
		//ClassConnection Elemente erstellen
		ClassConnection elA = new ClassConnection();
		elA.setConnection(ClassConnection.connectionType.aggregation);
		elA.setFrom(1);
		elA.setTo(4);
		
		ClassConnection elB = new ClassConnection();
		elB.setConnection(ClassConnection.connectionType.extension);
		elB.setFrom(1);
		elB.setTo(2);
		
		ClassConnection elC = new ClassConnection();
		elC.setConnection(ClassConnection.connectionType.composition);
		elC.setFrom(2);
		elC.setTo(3);
		
		//classes fuellen
		ArrayList<String> classes = new ArrayList<String>();
		classes.add("BeispielKlasse1");
		classes.add("BeispielKlasse2");
		classes.add("BeispielKlasse3");
		classes.add("BeispielKlasse4");
		classes.add("BeispielKlasse5");
		
		//classConnections fuellen
		ArrayList<ClassConnection> classConnections = new ArrayList<ClassConnection>();
		classConnections.add(elA);
		classConnections.add(elB);
		classConnections.add(elC);
		
		//ParsingResult erstellen
		ParsingResult actualParsTest = new ParsingResult();
		actualParsTest.classConnections = classConnections;
		actualParsTest.classes = classes;
		

		//savePumltoFile testen //TODO Ändern der Filepaths bevor Test (je nach System)
								//TODO expectedFile platzieren
		OutputPUML output = new OutputPUML();
		output.savePUMLtoFile(actualParsTest, "C:\\Users\\Cedric\\Test\\actualFile.txt");
		String testactfilePath="C:\\Users\\Cedric\\Test\\actualFile.txt";
		String testexpfilePath="C:\\Users\\Cedric\\Test\\expectedFile.txt";
		File actual = new File(testactfilePath);
		File expected = new File(testexpfilePath);		
	    //assertEquals(actual , expected)
		assertEquals(FileUtils.readFile(actual) , FileUtils.readFile(expected));
}

	@Test
	void testCreatePlantUML() {
		fail("Not yet implemented");
	}

}
