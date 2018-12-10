import static org.junit.jupiter.api.Assertions.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class RegExTest
{

    @Test
    void test()
    {
	ParserJava parsertest = new ParserJava();
	parsertest.parse("import java.util.regex.*;\n" + 
		"\n" + 
		"/**\n" + 
		"public interface ParserIf{" +
		" * \n" + 
		" * @author Klasse die den Parser für Java implementiert\n" + 
		" */\n" + 
		"public class ParserJava implements ParserIf{" +
		"\n" + 
		"    private ParsingResult result = null;\n" + 
		"\n" + 
		"    /*public interface test3000 extends ParserIf{*\n" + 
		"     * Konstruktor\n" + 
		"     */\n" + 
		"    public ParserJava()\n" + 
		"    {\n" + 
		"\n" + 
		"    };\n" + 
		"\n" + 
		"    /**\n" + 
		"     * Liest den übergebenen Quellcode ein und parst die Informationen daraus\n" + 
		"     * \n" + 
		"     * @param sourceCode Vollständigen Java-Quellcode\n" + 
		"     */\n" + 
		"    public void parse(String sourceCode)\n" + 
		"    {\n" + 
		"	System.out.println(sourceCode);\n" + 
		"	this.result = new ParsingResult();\n" + 
		"\n" + 
		"	Matcher classMatcher = Pattern.compile(\"class*{\").matcher(sourceCode);\n" + 
		"	while (classMatcher.find())\n" + 
		"	{\n" + 
		"public class test2 implements test3000, ParserIf { \n" +
		"public class bla2 extends test2{" +
		"	    System.out.println(classMatcher.group());\n" + 
		"	}\n" + 
		"\n" + 
		"//	String text= \"Das ist ein 'Test' !\" ;\n" + 
		"//	Matcher matcher = Pattern.compile(\"'.*'\").matcher(text);\n" + 
		"//	while (matcher.find())\n" + 
		"//	{\n" + 
		"//		System.out.println(matcher.group());\n" + 
		"//	    \n" + 
		"//	}\n" + 
		"\n" + 
		"    }\n" + 
		"\n" + 
		"    /**\n" + 
		"     * Liefert die Ergebnisse des Parsens zurück\n" + 
		"     * \n" + 
		"     * @return Struktur mit den ergebnisse des Parsens\n" + 
		"     */\n" + 
		"    public ParsingResult getParsingResult()\n" + 
		"    {\n" + 
		"	return result;\n" + 
		"    }\n" + 
		"\n" + 
		"}\n" + 
		"");
    }

}
