import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainTest {
	private PUMLgenerator classUnderTest;
	private File output;
	
	@BeforeEach
	public void SetUp() throws Exception
	{
		classUnderTest = new PUMLgenerator();
	}
	
	/*
	 * Einfacher Test
	 * Einzelne Klasse aus Datensatz "Main"
	 * Ohne File Ausgabepfad -> defaultlocation
	 */
	@SuppressWarnings({ "static-access", "unlikely-arg-type" })
	@Test
	void testMainCmd() throws IOException, ParseException //Test 1 einlesen Main aus Testdatensatz
	{   
		String[] testArgs = {"-c","-ijar","--i","./testfolder/datensatz/Main.java"};
		classUnderTest.main(testArgs);
		String[] reference = {"@startuml","class Main","@enduml"};
		String filePath = "./code/outPUML_Code_defaultlocation";
		equals(compareTextFile(reference,filePath));
	 }
	
	/*
	 * Einfacher Test
	 * Einzelne Klasse aus Datensatz "Main"
	 * Mit Ausgabepfad "./testfolder/datensatz/"
	 */
	@SuppressWarnings({ "static-access", "unlikely-arg-type" })
	@Test
	void testMainCmd() throws IOException, ParseException //Test 1 einlesen Main aus Testdatensatz
	{   
		String[] testArgs = {"-c","-ijar","--i",
				"./testfolder/datensatz/Main.java",
				"-o", "./testfolder/datensatz/TestMain"};
		classUnderTest.main(testArgs);
		String[] reference = {"@startuml","class Main","@enduml"};
		String filePath = "./testfolder/datensatz/TestMain";
		equals(compareTextFile(reference,filePath));
	 }
	/*
	 * Test mit drei Klassen
	 * Klasse aus Datensatz "Lebewesen","Mensch","Tier"
	 * Ohne File Ausgabepfad -> defaultlocation
	 */
	@SuppressWarnings({ "unlikely-arg-type", "static-access" })
	@Test
	void testMainLMT() throws IOException, ParseException
	{   
		String[] testArgs = {"-c","-ijar","--i",
				"./testfolder/datensatz/Lebewesen.java",
				"./testfolder/datensatz/Mensch.java",
				"./testfolder/datensatz/Tier.java"};
		classUnderTest.main(testArgs);
		String[] reference = {"@startuml",
				"abstract class Lebewesen",
				"abstract class Mensch",
				"abstract class Tier",
				"Mensch --|> Lebewesen",
				"Tier --|> Lebewesen",
				"@enduml"};
		String filePath = "./code/outPUML_Code_defaultlocation";
		equals(compareTextFile(reference,filePath));
	 }
	
	/*
	 * Test mit allen Klassen
	 * Klasse aus Datensatz "Lebewesen","Mensch","Tier","Vogel","kannFliegen"
	 * Ohne File Ausgabepfad -> defaultlocation
	 */
	@SuppressWarnings({ "static-access", "unlikely-arg-type" })
	@Test
	void testMainKompletterDatensatz() throws ParseException, IOException
	{
		String[] testArgs = {"-c","-ijar","--i",
				"./testfolder/datensatz/Lebewesen.java",
				"./testfolder/datensatz/Main.java",
				"./testfolder/datensatz/Mensch.java",
				"./testfolder/datensatz/Tier.java",
				"./testfolder/datensatz/Vogel.java",
				"./testfolder/datensatz/kannFliegen.java"};
		classUnderTest.main(testArgs);
		String[] reference = {"@startuml\n",
				"abstract class Lebewesen",
				"abstract class Mensch",
				"abstract class Tier",
				/*TODO*/
				"class Main\n",
				"@enduml"};
		String filePath = "./code/outPUML_Code_defaultlocation";
		System.out.println("Test q");
		equals(compareTextFile(reference,filePath));
	}

	@SuppressWarnings("resource")
	boolean compareTextFile (String[] reference,String pumlFile) throws IOException
	{
		try {
			FileReader fr = new FileReader(pumlFile);
			BufferedReader br = new BufferedReader(fr);
			
			String line = "";
			Path path = Paths.get(pumlFile);		
			int entryLength = (int)Files.lines(path).count();
			System.out.println("Länge der Files: " + entryLength + " " + reference.length);
			if (entryLength == reference.length)
				{
					for(int i = 0; i < entryLength; i++)
					{
						line = br.readLine();
						System.out.println("Programmausgabe: " + line + "	Korrekt: " + reference[i]);
						if(!line.equals(reference[i]))
						{						
							br.close();
							return false;
						}
					}
				}
			else 
			{
				br.close();
				return false;
			}
			br.close();
			System.out.println("Richtig! :)");
			return true;
		}
		catch (IOException e) {
			System.out.println(e);
			return false;
		}
	}
}	
