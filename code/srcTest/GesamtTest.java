import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.cli.ParseException;
import org.junit.internal.runners.TestClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

class GesamtTest
{
    private PUMLgenerator testClass;

    @AfterAll
    static void tearDownAfterClass() throws Exception
    {
	File here = new File("testfolder/tempData/.");
	System.out.println("Waehrend des Tests erzeugte Testfiles unter "+ here.getAbsolutePath()+ " sollen gelöscht werden.");
	File cDFile = new File("testfolder/tempData/CDoutPUML_Graph");
	File sQFile = new File("testfolder/tempData/SQoutPUML_Graph");
	if (sQFile.delete() && cDFile.delete())
	{
		System.out.println("Testfile erfolgreich nach Test gelöscht.");
	}
	else
	{
		System.out.println("Testfile konnte nicht gelöscht werden.");
	}
    }

    @SuppressWarnings({ "static-access" })
    @Test
    void testSQ() throws IOException, ParseException
    {
	testClass = new PUMLgenerator();
	//ARGS : -c -ijar -ct -cs Class1,method1  - -i testfolder/xmlSpecifications/sources/java/Class1.java -o testfolder/xmlSpecifications
	String[] specification = {
		"-c",
		"-ijar",
		"-ct" ,
		"-cs" ,
		"Class1,method1",
		"--i",
		"testfolder/xmlSpecifications/sources/java/Class1.java" ,
		"testfolder/xmlSpecifications/sources/java/Class2.java;" ,
		"testfolder/xmlSpecifications/sources/java/Class3.java" ,
		"-o", //erstellt Ordener, wenn nicht vorhanden??
		"testfolder/tempData/SQ"
		
	};
	testClass.main(specification);
	/*
	 * Expected File wird gelesen und mit einem StringBuilder in einen String ("expected") geschrieben
	 */
	FileReader fr = new FileReader(new File("testfolder/xmlSpecifications/SeqDiagramNew.txt"));
	BufferedReader br = new BufferedReader(fr);
	StringBuilder sb = new StringBuilder();
	
	for (String line = br.readLine(); line != null; line = br.readLine())
	{
	    sb.append(line).append(line).append("\n");
	}
	String expected = sb.toString();
	
	String actual = "";//TODO Actual File Beschreiben
	assertEquals(expected, actual);
	//fail("Not yet implemented");
    }
    
    @SuppressWarnings({ "static-access" })
    @Test
    void testCD() throws IOException, ParseException
    {
	testClass = new PUMLgenerator();
	//ARGS : -c -ijar -ct -cs Class1,method1  - -i testfolder/xmlSpecifications/sources/java/Class1.java -o testfolder/xmlSpecifications
	String[] specification = {
		"-c",
		"-ijar",
		"-ct" ,
		"-cc" ,
		//"Class1,method1",
		"--i",
		"testfolder/xmlSpecifications/sources/java/Class1.java" ,
		//"testfolder/xmlSpecifications/sources/java/Class2.java;" ,
		//"testfolder/xmlSpecifications/sources/java/Class3.java" ,
		"-o", //erstellt Ordener, wenn nicht vorhanden??
		"testfolder/tempData/CD"
		
	};
	testClass.main(specification);
	/*
	 * Expected File wird gelesen und mit einem StringBuilder in einen String ("expected") geschrieben
	 */
	FileReader fr = new FileReader(new File("testfolder/xmlSpecifications/SeqDiagramNew.txt"));
	BufferedReader br = new BufferedReader(fr);
	StringBuilder sb = new StringBuilder();
	
	for (String line = br.readLine(); line != null; line = br.readLine())
	{
	    sb.append(line).append(line).append("\n");
	}
	String expected = sb.toString();
	
	String actualPath = "testfolder/tempData/SQoutPUML_Code";//TODO Actual File Beschreiben
	assertTrue(compareTextFile(expected, actualPath));
	//fail("Not yet implemented");
    }
    
    @SuppressWarnings("resource")
	boolean compareTextFile (String expected,String pumlFile) throws IOException
	{
			FileReader fr = new FileReader(pumlFile);
			BufferedReader br = new BufferedReader(fr);
			
			StringBuilder sb = new StringBuilder();
			
			for (String line = br.readLine(); line != null; line = br.readLine())
			{
			    sb.append(line).append(line).append("\n");
			}
			String actual = sb.toString();
			System.out.println("/////////////////////\n\n" + actual + "\n\n/////////////////////");
			
			if (expected.equals(actual))
			{
			    return true;
			}
			else
			{
			    return false;
			}
	}

}
