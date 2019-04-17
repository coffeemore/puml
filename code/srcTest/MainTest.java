import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
		classUnderTest.main(testArgs);
		String[] reference = {"@startuml\n","class Main\n","@enduml"};
		String filePath = "./outPUML_Code_defaultlocation";
		equals(compareTextFile(reference,filePath));
	 }
	
	void createOutputFile(String cont) throws IOException
	{
		String data = cont;
		 
		FileOutputStream out = new FileOutputStream("./testfolder/outMainJunitTest.txt");
		out.write(data.getBytes());
		out.close();
    }
	
	@SuppressWarnings("resource")
	boolean compareTextFile (String[] reference,String pumlFile) throws IOException
	{
		FileReader fr = new FileReader(pumlFile);
		BufferedReader br = new BufferedReader(fr);
		
		String line = "";
		int i = 0;
		boolean endFile = false;
		while(!endFile);
		{
			line = br.readLine();
			if (line ==null)
			{
				endFile = true;
			}
			System.out.println(line);
			if (!(line.equals(reference[i])) && !endFile)
			{
				return false;
			}
			i++;
		}
		br.close();
		return true;
	}
}
