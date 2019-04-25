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
	
	@SuppressWarnings({ "static-access", "unlikely-arg-type" })
	@Test
	void testMainCmd() throws IOException, ParseException //Test 1 einlesen Main aus Testdatensatz
	{   
		String[] testArgs = {"-c","-ijar","--i","./testfolder/datensatz/Main.java"};
		//classUnderTest.main(testArgs);
		String[] reference = {"@startuml","class Main","@enduml"};
		String filePath = "./outPUML_Code_defaultlocation";
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
