import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;

class ConsoleTest
{
	//private Console testConsole;
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
		assertTrue(PUMLgenerator.codeCollector.isUseCppAndHppFiles());
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
