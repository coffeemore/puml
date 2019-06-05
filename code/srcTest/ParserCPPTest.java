import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

class ParserCPPTest
{
	@Test
	void testCommentsSimple()
	{
		ParserCPP parsertest = new ParserCPP();
		String in = "richtig1\n" +
				"richtig2//f1\n" +
				"rich/*f2*/tig3\n" +
				"rich\"Str1\"itg4\n" +
				"richtig*\n";
		String out = "richtig1\n" + 
				"richtig2\n" + 
				"richtig3\n" + 
				"richitg4\n" + 
				"richtig*\n";
		System.out.println("Kommentare und Strings löschen von: \n" + in + "\n");
		System.out.println("Ohne Kommentare mit Programm: \n" + parsertest.deleteComStr(in));
		//System.out.println("erwartet wurde: \n" + out);
		assertTrue(parsertest.deleteComStr(in).equals(out));
	}
	
	@Test
	void testCommentsAdvanced()
	{
		ParserCPP parsertest = new ParserCPP();
		String in = "richtig1\n" +
				"richtig2//f1\\n\n" +		//verschachtelte //-Kommentare
				"richtig3//fal/*sch*/\n" +
				"richt/*fal\"sch*/ig4\n" +	//verschachtelte /* */-Kommentare
				"richt/*fal//sch*/ig5\n" +
				"richt\"fal///*s*/ch\"ig6\n" +	//verschachtelte " " Strings
				//"richt\"fal\"sch\"ig7\n" +	//TODO klappt noch nicht
				"richtig*\n";
		String out = "richtig1\n" +
				"richtig2\n" +
				"richtig3\n" +
				"richtig4\n" +
				"richtig5\n" +
				"richtig6\n" +
				//"richtig7\n" +
				"richtig*\n";
		System.out.println("Kommentare und Strings löschen von: \n" + in + "\n");
		System.out.println("Ohne Kommentare mit Programm: \n" + parsertest.deleteComStr(in));
		//System.out.println("erwartet wurde: \n" + out);
		assertTrue(parsertest.deleteComStr(in).equals(out));
	}
}