import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
	File here = new File(".");
	System.out.println("Waehrend des Tests erzeugte Testfiles unter "+ here.getAbsolutePath()+ " sollen gelöscht werden.");
	File defaultFile = new File("outPUML_Code_defaultlocation");
	File givenOutFile = new File("testfolder/datensatz/TestMainoutPUML_Code");
	if (defaultFile.delete() || givenOutFile.delete())
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
    void test() throws IOException, ParseException
    {
	testClass = new PUMLgenerator();
	String[] specification = {
		"-c",
		"-ijar",
		"--i",
		"testfolder/xmlSpecifications/sources/java/Class1.java",
		"-o", //erstellt Ordener, wenn nicht vorhanden??
		"testfolder/tests"
		
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

}
