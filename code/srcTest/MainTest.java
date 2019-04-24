import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

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
		String[] reference = {"@startuml\n","class Main\n","@enduml"};
		String filePath = "./code/outPUML_Code_defaultlocation";
		System.out.println("Test q");
		equals(compareTextFile(reference,filePath));
	 }
	
	@SuppressWarnings({ "static-access", "unlikely-arg-type" })
	@Test
	void testMainDatensatz() throws ParseException, IOException
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
	
	/*
	void createOutputFile(String cont) throws IOException
	{
		String data = cont;
		 
		FileOutputStream out = new FileOutputStream("./testfolder/outMainJunitTest.txt");
		out.write(data.getBytes());
		out.close();
    }*/
	/*
	@SuppressWarnings("resource")
	boolean compareTextFile (String[] reference,String pumlFile) throws IOException
	{
		FileReader fr = new FileReader(pumlFile);
		BufferedReader br = new BufferedReader(fr);
		
		String line = "";
		int i = 0;
		boolean onGoingFile = true;
		while(onGoingFile)
		{
			line = br.readLine();
			if (line == null) //Textdokument ende
			{
				onGoingFile = false;
			}
			System.out.println(line);
			if (!(line.equals(reference[i])) && onGoingFile)
			{
				System.out.println(reference[i] + line);
				return false;
			}
			i++;
		}
		br.close();
		return true;
	}*/
	@SuppressWarnings("resource")
	boolean compareTextFile (String[] reference,String pumlFile) throws IOException
	{
		FileReader fr = new FileReader(pumlFile);
		BufferedReader br = new BufferedReader(fr);
		
		String line = "";
		int entryLength = pumlFile.length();
		if (entryLength == reference.length)
			{
				while (entryLength > 0)
				{
					//
					//Kommentar
					//für Testzwecke
				}
			}
		
		return true;
	}
		
}
