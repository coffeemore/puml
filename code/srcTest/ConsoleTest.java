import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;

class ConsoleTest
{
	private Console testConsole;
	
	@Test
	void testMissingPath() throws ParseException
	{
	    PrintStream save_out = System.out;
	    final ByteArrayOutputStream out = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(out));
	    //Testaufruf
	    String [] options = {"-c"};
		PUMLgenerator.main(options);
		//Auswertung
	    assertEquals("Consolemode\n" + 
	    		"Es fehlt ein zu bearbeitender Pfad.", out.toString());
	    System.setOut(save_out);
	}
	
	@Test
	void testIgnoreJar() throws ParseException
	{
		String [] options = {"-c","-ijar"};
		PUMLgenerator.main(options);
		assertFalse(PUMLgenerator.codeCollector.isUseJarFiles());
	}
	@Test
	void testUseCpp() throws ParseException
	{
		String [] options = {"-c","-ucpp"};
		PUMLgenerator.main(options);
		assertFalse(PUMLgenerator.codeCollector.isUseCppAndHppFiles());
	}
	@Test
	void testIgnoreJava() throws ParseException
	{
		String [] options = {"-c","-ijava"};
		PUMLgenerator.main(options);
		assertFalse(PUMLgenerator.codeCollector.isUseJavaFiles());
	}
	
	@Test
	void testIgnoreInstances() throws ParseException
	{
		String [] options = {"-c","-iinst"};
		PUMLgenerator.main(options);
		assertFalse(PUMLgenerator.classDiagramGenerator.getShowInstances());
	}
	
	@Test
	void testIgnoreMeth() throws ParseException
	{
		String [] options = {"-c","-imeth"};
		PUMLgenerator.main(options);
		assertFalse(PUMLgenerator.classDiagramGenerator.getShowMethods());
	}
	
	@Test
	void testIgnoreVar() throws ParseException
	{
		String [] options = {"-c","-ivar"};
		PUMLgenerator.main(options);
		assertFalse(PUMLgenerator.classDiagramGenerator.getShowVars());
	}
}
