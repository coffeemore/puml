import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class GesamtTest
{
    Boolean classDFlag = false;
    Boolean seqDFlag = false;
    private PUMLgenerator testClass;
/*
    @AfterEach
    public void tearDownAfterClass() throws Exception
    {
	File here = new File("testfolder/tempData/.");
	System.out.println("Waehrend des Tests erzeugte Testfiles unter "+ here.getAbsolutePath()+ " sollen gelöscht werden.");
	File cDFile = new File("testfolder/tempData/CDoutPUML_Graph");
	File sQFile = new File("testfolder/tempData/SQoutPUML_Graph");
	if (sQFile.delete() || cDFile.delete())
	{
		System.out.println("Testfile erfolgreich nach Test gelöscht.");
	}
	else
	{
		System.out.println("Testfile konnte nicht gelöscht werden.");
	}
    }

*/
    @SuppressWarnings({ "static-access" })
    @Test
    void testSQ() throws IOException, ParseException
    {
	seqDFlag = true;
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
//	FileReader fr = new FileReader(new File("testfolder/xmlSpecifications/SeqDiagramNew.txt"));
//	BufferedReader br = new BufferedReader(fr);
//	StringBuilder sb = new StringBuilder();
//	
//	for (String line = br.readLine(); line != null; line = br.readLine())
//	{
//	    sb.append(line).append(line).append("\n");
//	}
//	String expected = sb.toString();
//	
//	String actual = "";//TODO Actual File Beschreiben
//	assertEquals(expected, actual);
//	//fail("Not yet implemented");
    }
    
    @SuppressWarnings({ "static-access" })
    @Test
    void testCD() throws IOException, ParseException
    {
	classDFlag = true;
	testClass = new PUMLgenerator();
	//ARGS : -c -ijar -ct -cs Class1,method1  - -i testfolder/xmlSpecifications/sources/java/Class1.java -o testfolder/xmlSpecifications
	String[] specification = {
		"-c",
		"-ijar",
		"-ct" ,
		"-cc" ,
		//"Class1,method1",
		"--i",
		"testfolder/xmlSpecifications/sources/java/" ,
		"-o", //erstellt Ordener, wenn nicht vorhanden??
		"testfolder/tempData/CD"
		
	};
	testClass.main(specification);
	/*
	 * Expected File wird gelesen und mit einem StringBuilder in einen String ("expected") geschrieben
	 */
	
//	FileReader fr = new FileReader(new File("testfolder/xmlSpecifications/SeqDiagram.txt"));
//	BufferedReader br = new BufferedReader(fr);
//	StringBuilder sb = new StringBuilder();
//	
//	for (String line = br.readLine(); line != null; line = br.readLine())
//	{
//	    sb.append(line).append(line).append("\n");
//	}
//	String expected = sb.toString();
//	
//	String actualPath = "testfolder/tempData/SQoutPUML_Code";//TODO Actual File Beschreiben
//	assertTrue(compareTextFile(expected, actualPath));
//	//fail("Not yet implemented");
    }
    
    @SuppressWarnings("resource")
    @AfterEach
	void compareTextFile () throws IOException
	{
	String expectedPath = "";
	String actualPath = "";  //init
	if(classDFlag) { 
	    actualPath = "testfolder/tempData/CDoutPUML_Code";
	    expectedPath = "testfolder/xmlSpecifications/ClassDiagram.txt";
	    classDFlag = false;
	} else if(seqDFlag) {
	    actualPath = "testfolder/tempData/SQoutPUML_Code";
	    expectedPath = "testfolder/xmlSpecifications/SeqDiagram.txt";
	    seqDFlag = false;
	} else {
	    System.out.println("Output Files not found");
	    assertTrue(false);
	}
			FileReader fr = new FileReader(actualPath);
			BufferedReader br = new BufferedReader(fr);
			
			StringBuilder sb = new StringBuilder();
			
			for (String line = br.readLine(); line != null; line = br.readLine())
			{
			    sb.append(line).append(line).append("\n");
			}
			String actualString = sb.toString();
			br.close();
			///////////////////////////////////
			fr = new FileReader(expectedPath);
			br = new BufferedReader(fr);
			
			sb = new StringBuilder();
			
			for (String line = br.readLine(); line != null; line = br.readLine())
			{
			    sb.append(line).append(line).append("\n");
			}
			String expectedString = sb.toString();
			///////////////////////////////////
			assertEquals(expectedString, actualString);
	}

}
