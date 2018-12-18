import static org.junit.jupiter.api.Assertions.*;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import net.sourceforge.plantuml.FileUtils;

class OutputPUMLTest { 

	
	

	@Test
	void testGetPUML() {
		//ClassConnection Elemente erstellen
		ClassConnection elA = new ClassConnection(1,4,ClassConnection.connectionType.aggregation);
		
		ClassConnection elB = new ClassConnection(1,2,ClassConnection.connectionType.extension);
		
		ClassConnection elC = new ClassConnection(2,3,ClassConnection.connectionType.composition);
		
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
		ParsingResult actualParsTest = new ParsingResult(classes,classConnections);
		
		//GetPuml testen
		String actual = new OutputPUML().getPUML(actualParsTest);
		
		String expected = "@startuml%class BeispielKlasse1%class BeispielKlasse2%class BeispielKlasse3%class BeispielKlasse4%class BeispielKlasse5%BeispielKlasse2 o-- BeispielKlasse5%BeispielKlasse2 -- BeispielKlasse3%BeispielKlasse3 *-- BeispielKlasse4%@enduml";
		assertEquals(expected, actual);
	}

	@Test
	// expectedFile.txt befindet sich fuer den Test im srcTest Ordner um in Git aufgenommen zu werden.
	void testSavePUMLtoFile() throws IOException {
		File expected = new File("/home/tore/Test/expectedFile.txt");
		
		//ClassConnection Elemente erstellen
		ClassConnection elA = new ClassConnection(1,4,ClassConnection.connectionType.aggregation);
		
		ClassConnection elB = new ClassConnection(1,2,ClassConnection.connectionType.extension);
		
		ClassConnection elC = new ClassConnection(2,3,ClassConnection.connectionType.composition);
		
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
		ParsingResult actualParsTest = new ParsingResult(classes,classConnections);
		
		//savePumltoFile testen //TODO Aendern der Filepaths bevor Test (je nach System)
		//TODO expectedFile platzieren
		OutputPUML output = new OutputPUML();
		output.savePUMLtoFile(output.getPUML(actualParsTest), "/home/tore/Test/actualFile.txt");
		
		File actual = new File("/home/tore/Test/actualFile.txt");		
		assertEquals(FileUtils.readFile(actual) , FileUtils.readFile(expected));
	}

	@Test
	void testCreatePlantUML() throws IOException 
	{
		
		File expected = new File("/home/tore/Test/expectedFile.png"); //TODO Aendern der Filepaths bevor Test (je nach System)
		
		//ClassConnection Elemente erstellen
				ClassConnection elA = new ClassConnection(1,4,ClassConnection.connectionType.aggregation);
				ClassConnection elB = new ClassConnection(1,2,ClassConnection.connectionType.extension);
				ClassConnection elC = new ClassConnection(2,3,ClassConnection.connectionType.composition);
				
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
				ParsingResult actualParsTest = new ParsingResult(classes,classConnections);
				
				//GetPuml testen
				String actualString = new OutputPUML().getPUML(actualParsTest);
				
				//TODO Aendern der Filepaths bevor Test (je nach System)
		OutputPUML output = new OutputPUML();
		output.createPlantUML("/home/tore/Test/actualFile.txt", actualString);
		
		File actual = new File("/home/tore/Test/expectedFile.png");
		
		assertEquals(FileUtils.readFile(actual) , FileUtils.readFile(expected));
	}

}
