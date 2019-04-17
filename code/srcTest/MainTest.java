import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileOutputStream;
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
	@SuppressWarnings("static-access")
	@Test
	void testMainCmd() throws IOException, ParseException //Test 1 einlesen Main aus Testdatensatz
	{   
		String[] testArgs = {"-c","-ijar","--i","./testfolder/datensatz/Main.java"};
		classUnderTest.main(testArgs);
		createOutputFile("@startuml\n" + 
						"class Main\n" + 
						"@enduml");
		equals(true);
	 }
	
	void createOutputFile(String cont) throws IOException
	{
		String data = cont;
		 
		FileOutputStream out = new FileOutputStream("./testfolder/outMainJunitTest.txt");
		out.write(data.getBytes());
		out.close();
    }
}
