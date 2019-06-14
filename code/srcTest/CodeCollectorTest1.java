import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;

//Testfälle anpassen - 
//	für Ordner mit Unterordner - noch buggy?

class CodeCollectorTest1
{
    private CodeCollector classUnderTest;
    private String fileCpp;
    private String fileHpp;

    @BeforeEach
    public void SetUp() throws Exception
    {
	classUnderTest = new CodeCollector();
	fileCpp = "../code/testfolder/xmlSpecifications/sources/cpp/Class1.cpp";
	fileHpp = "../code/testfolder/xmlSpecifications/sources/cpp/Class1.hpp";
    }

    boolean status = false;

    @Test
    void testCppAndHpp()
    {
	assertAll(

		() ->
		{
		    resetTest();
		    classUnderTest.setUseJarFiles(false);
		    classUnderTest.setUseJavaFiles(false);
		    classUnderTest.setUseCppAndHppFilesFiles(true);
		    classUnderTest.paths.add(fileCpp);
		    ArrayList<String> aktErg = classUnderTest.getSourceCode();

		    String erwErgsc2 = read(fileCpp);
		    ArrayList<String> erwErg = new ArrayList<String>();
		    erwErg.add(new String());
		    erwErg.add(erwErgsc2);

		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

		// zwei Dateien, !useJar, !useJava, usecppandhppfiles
		() ->
		{
		    resetTest();
		    classUnderTest.setUseJarFiles(false);
		    classUnderTest.setUseJavaFiles(false);
		    classUnderTest.setUseCppAndHppFilesFiles(true);
		    classUnderTest.paths.add(fileCpp);
		    classUnderTest.paths.add(fileHpp);
		    ArrayList<String> aktErg = classUnderTest.getSourceCode();

		    String erwErgsc1 = read(fileHpp);
		    String erwErgsc2 = read(fileCpp);

		    ArrayList<String> erwErg = new ArrayList<String>();
		    erwErg.add(erwErgsc1);
		    erwErg.add(erwErgsc2);

		    assertEquals(erwErg, aktErg);
		    resetTest();
		},
		// zwei Ddateien, useJar, !useJava, usecppandhppfiles
		() ->
		{
		    resetTest();
		    classUnderTest.setUseJarFiles(true);
		    classUnderTest.setUseJavaFiles(false);
		    classUnderTest.setUseCppAndHppFilesFiles(true);
		    classUnderTest.paths.add(fileCpp);
		    classUnderTest.paths.add(fileHpp);
		    ArrayList<String> aktErg = classUnderTest.getSourceCode();

		    String erwErgsc1 = read(fileHpp);
		    String erwErgsc2 = read(fileCpp);

		    ArrayList<String> erwErg = new ArrayList<String>();
		    erwErg.add(erwErgsc1);
		    erwErg.add(erwErgsc2);

		    assertEquals(erwErg, aktErg);
		    resetTest();
		}
		// zwei Ddateien, useJar, useJava, usecppandhppfiles
		, () ->
		{
		    resetTest();
		    classUnderTest.setUseJarFiles(true);
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseCppAndHppFilesFiles(true);
		    classUnderTest.paths.add(fileCpp);
		    classUnderTest.paths.add(fileHpp);
		    ArrayList<String> aktErg = classUnderTest.getSourceCode();

		    String erwErgsc1 = read(fileHpp);
		    String erwErgsc2 = read(fileCpp);

		    ArrayList<String> erwErg = new ArrayList<String>();
		    erwErg.add(erwErgsc1);
		    erwErg.add(erwErgsc2);

		    assertEquals(erwErg, aktErg);
		    resetTest();
		},
		// zwei Ddateien, useJar, useJava, !usecppandhppfiles
		() ->
		{
		    resetTest();
		    classUnderTest.setUseJarFiles(true);
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseCppAndHppFilesFiles(false);
		    classUnderTest.paths.add(fileCpp);
		    classUnderTest.paths.add(fileHpp);
		    ArrayList<String> aktErg = classUnderTest.getSourceCode();

		    ArrayList<String> erwErg = new ArrayList<String>();
		    erwErg.add(new String());

		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

		// zwei Ddateien, useJar, useJava, !usecppandhppfiles

		() ->
		{
		    resetTest();
		    classUnderTest.setUseJarFiles(false);
		    classUnderTest.setUseJavaFiles(false);
		    classUnderTest.setUseCppAndHppFilesFiles(false);
		    classUnderTest.paths.add("../code/testfolder/CodeCollector/Testdateien/CodeJavaTest.java");
		    try
		    {
			classUnderTest.getSourceCode();
		    } catch (IllegalArgumentException e)
		    {
			status = true;
		    }
		    assertTrue(status);
		    resetTest();
		}

	);
    }

    @Test
    void testGetSourceCode()
    {

	assertAll(
		// Test: use Java, !useJar, 1 Pfad (Java) in paths

		() ->
		{
		    resetTest();
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(false);
		    classUnderTest.paths.add("../code/testfolder/CodeCollector/Testdateien/CodeJavaTest.java");
		    ArrayList<String> aktErg = classUnderTest.getSourceCode();

		    String erwErgsc = "erste Zeile \nzweite Zeile\ndritte Zeile \n";

		    ArrayList<String> erwErg = new ArrayList<String>();
		    erwErg.add(erwErgsc);

		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

		// 1 Pfad, !useJava, useJar (1 Java-Datei in Jar)
		() ->
		{
		    resetTest();
		    classUnderTest.setUseJavaFiles(false);
		    classUnderTest.setUseJarFiles(true);
		    classUnderTest.paths.add("../code/testfolder/CodeCollector/Testdateien/jartest.jar");
		    ArrayList<String> aktErg = classUnderTest.getSourceCode();
		    String erwErgsc = "erste Zeile \nzweite Zeile\ndritte Zeile \n";

		    ArrayList<String> erwErg = new ArrayList<String>();
		    erwErg.add(erwErgsc);

		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

		// 1 Pfad (Jar-Datei), !useJava, !useJar
		() ->
		{
		    resetTest();
		    classUnderTest.setUseJavaFiles(false);
		    classUnderTest.setUseJarFiles(false);
		    classUnderTest.paths.add("../code/testfolder/CodeCollector/Testdateien/jartest.jar");
		    try
		    {
			classUnderTest.getSourceCode();
		    } catch (IllegalArgumentException e)
		    {
			status = true;
		    }
		    assertTrue(status);
		    resetTest();
		},

		// 1 Pfad (Java-Datei), !useJava, !useJar
		() ->
		{
		    resetTest();
		    classUnderTest.setUseJavaFiles(false);
		    classUnderTest.setUseJarFiles(false);
		    classUnderTest.paths.add("../code/testfolder/CodeCollector/Testdateien/CodeJavaTest.java");
		    try
		    {
			classUnderTest.getSourceCode();
		    } catch (IllegalArgumentException e)
		    {
			status = true;
		    }
		    assertTrue(status);
		    resetTest();
		},

		// 1 Pfad (Jar), !useJava, useJar (2 Java-Dateien in Jar)
		() ->
		{
		    resetTest();
		    classUnderTest.setUseJavaFiles(false);
		    classUnderTest.setUseJarFiles(true);
		    classUnderTest.paths.add("../code/testfolder/CodeCollector/Testdateien/jartest2.jar");
		    ArrayList<String> aktErg = classUnderTest.getSourceCode();
		    String erwErgsc = "erste Zeile \nzweite Zeile\ndritte Zeile \n//Beginn der zweiten Datei\nerste Zeile \nzweite Zeile\ndritte Zeile \n";

		    ArrayList<String> erwErg = new ArrayList<String>();
		    erwErg.add(erwErgsc);

		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

		// 1 Pfad (Java), useJava, useJar
		() ->
		{
		    resetTest();
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(true);
		    classUnderTest.paths.add("../code/testfolder/CodeCollector/Testdateien/CodeJavaTest.java");
		    ArrayList<String> aktErg = classUnderTest.getSourceCode();
		    String erwErgsc = "erste Zeile \nzweite Zeile\ndritte Zeile \n";

		    ArrayList<String> erwErg = new ArrayList<String>();
		    erwErg.add(erwErgsc);

		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

		// 1 Pfad (Jar), useJava, useJar
		() ->
		{
		    resetTest();
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(true);
		    classUnderTest.paths.add("../code/testfolder/CodeCollector/Testdateien/jartest2.jar");
		    ArrayList<String> aktErg = classUnderTest.getSourceCode();
		    String erwErgsc = "erste Zeile \nzweite Zeile\ndritte Zeile \n//Beginn der zweiten Datei\nerste Zeile \nzweite Zeile\ndritte Zeile \n";

		    ArrayList<String> erwErg = new ArrayList<String>();
		    erwErg.add(erwErgsc);

		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

		// 1 Pfad, useJava, useJar nicht initialisiert
		() ->
		{
		    resetTest();
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.paths.add("../code/testfolder/CodeCollector/Testdateien/CodeJavaTest.java");
		    ArrayList<String> aktErg = classUnderTest.getSourceCode();
		    String erwErgsc = "erste Zeile \nzweite Zeile\ndritte Zeile \n";

		    ArrayList<String> erwErg = new ArrayList<String>();
		    erwErg.add(erwErgsc);

		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

		// 1 Pfad(Java), useJar, useJava nicht initialisiert
		() ->
		{
		    resetTest();
		    classUnderTest.setUseJarFiles(true);
		    classUnderTest.paths.add("../code/testfolder/CodeCollector/Testdateien/CodeJavaTest.java");
		    ArrayList<String> aktErg = classUnderTest.getSourceCode();
		    String erwErgsc = "";

		    ArrayList<String> erwErg = new ArrayList<String>();
		    erwErg.add(erwErgsc);

		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

		// Test: use Java, !useJar, 2 Pfade (Java) in paths
		() ->
		{
		    resetTest();
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(false);

		    classUnderTest.paths.add("../code/testfolder/CodeCollector/Testdateien/CodeJavaTest.java");
		    classUnderTest.paths.add("../code/testfolder/CodeCollector/Testdateien/CodeJavaTest1.java");
		    ArrayList<String> aktErg = classUnderTest.getSourceCode();
		    String erwErgsc = "erste Zeile \nzweite Zeile\ndritte Zeile \n//Beginn der zweiten Datei\nerste Zeile \nzweite Zeile\ndritte Zeile \n";

		    ArrayList<String> erwErg = new ArrayList<String>();
		    erwErg.add(erwErgsc);

		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

		// Test: use !Java, useJar, 2 Pfade (Java) in paths
		() ->
		{
		    resetTest();
		    classUnderTest.setUseJavaFiles(false);
		    classUnderTest.setUseJarFiles(true);
		    // classUnderTest.paths.add(System.getProperty("user.dir") + File.separator +
		    // "code" + File.separator +"testfolder" + File.separator +"CodeJavaTest.java");
		    classUnderTest.paths.add("../code/testfolder/CodeCollector/Testdateien/CodeJavaTest.java");
		    classUnderTest.paths.add("../code/testfolder/CodeCollector/Testdateien/CodeJavaTest1.java");
		    ArrayList<String> aktErg = classUnderTest.getSourceCode();
		    String erwErgsc = "";

		    ArrayList<String> erwErg = new ArrayList<String>();
		    erwErg.add(erwErgsc);

		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

		// keine Pfadangabe
		// keine Pfadangabe, useJava, !useJar
		() ->
		{
		    resetTest();
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(false);
		    classUnderTest.paths.clear();
		    try
		    {
			ArrayList<String> aktErg = classUnderTest.getSourceCode();
		    } catch (NullPointerException e)
		    {
			status = true;
		    }
		    assertTrue(status);
		    resetTest();

		},

		// keine Pfadangabe, useJava, useJar
		() ->
		{
		    resetTest();
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(true);
		    try
		    {
			ArrayList<String> aktErg = classUnderTest.getSourceCode();
		    } catch (NullPointerException e)
		    {
			status = true;
		    }
		    assertTrue(status);
		    resetTest();

		},

		// keine Pfadangabe, !useJava, useJar
		() ->
		{
		    resetTest();
		    classUnderTest.setUseJavaFiles(false);
		    classUnderTest.setUseJarFiles(true);
		    try
		    {
			ArrayList<String> aktErg = classUnderTest.getSourceCode();
		    } catch (NullPointerException e)
		    {
			status = true;
		    }
		    assertTrue(status);
		    resetTest();

		},

		// keine Pfadangabe, !useJava, !useJar
		() ->
		{
		    resetTest();
		    classUnderTest.setUseJavaFiles(false);
		    classUnderTest.setUseJarFiles(false);
		    try
		    {
			ArrayList<String> aktErg = classUnderTest.getSourceCode();
		    } catch (NullPointerException | IllegalArgumentException e)
		    {
			status = true;
		    }
		    assertTrue(status);
		    resetTest();

		},

		// Test, ob Unterordner auch in mehreren Ebenen berücksichtigt werden
		() ->
		{
		    resetTest();
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(false);
		    classUnderTest.paths.add("../code/testfolder/CodeCollector/UOTest");
		    ArrayList<String> aktErg = classUnderTest.getSourceCode();
		    String erwErgsc = "UOTest\nUO2\nUO2.1\nUO1\nUO1.1\nUO1.1.1\nUO1.2\n";

		    ArrayList<String> erwErg = new ArrayList<String>();
		    erwErg.add(erwErgsc);

		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

		// Test: use Java, !useJar, 1 Pfad (Ordner mit je 2 Jar- und Java-Dateien) in
		// paths
		() ->
		{
		    resetTest();
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(false);
		    classUnderTest.paths.add("../code/testfolder/CodeCollector/Testdateien");
		    ArrayList<String> aktErg = classUnderTest.getSourceCode();
		    String erwErgsc = "erste Zeile \nzweite Zeile\ndritte Zeile \n//Beginn der zweiten Datei\nerste Zeile \nzweite Zeile\ndritte Zeile \n";

		    ArrayList<String> erwErg = new ArrayList<String>();
		    erwErg.add(erwErgsc);

		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

		// Test: !useJava, useJar; 1 Pfad mit Jar-Datei (ohne Java-Datei darin)
		() ->
		{
		    resetTest();
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(false);
		    classUnderTest.paths.add("//home//developer//workspace//test3.jar");
		    ArrayList<String> aktErg = classUnderTest.getSourceCode();
		    String erwErgsc = "";

		    ArrayList<String> erwErg = new ArrayList<String>();
		    erwErg.add(erwErgsc);

		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

		// Test: !useJava, useJar; 1 Pfad mit Jar-Datei (leeres Archiv)
		() ->
		{
		    resetTest();
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(false);
		    classUnderTest.paths.add("//home//developer//workspace//test4.jar");
		    ArrayList<String> aktErg = classUnderTest.getSourceCode();
		    String erwErgsc = "";

		    ArrayList<String> erwErg = new ArrayList<String>();
		    erwErg.add(erwErgsc);

		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

		// Test: !useJava, useJar, 2 Pfade (Jar-Dateien) in paths
		() ->
		{
		    resetTest();
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(false);
		    classUnderTest.paths.add("../code/testfolder/CodeCollector/Testdateien/");
		    ArrayList<String> aktErg = classUnderTest.getSourceCode();
		    String erwErgsc = "erste Zeile \nzweite Zeile\ndritte Zeile \n//Beginn der zweiten Datei\nerste Zeile \nzweite Zeile\ndritte Zeile \n";

		    ArrayList<String> erwErg = new ArrayList<String>();
		    erwErg.add(erwErgsc);

		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

		// Test: !useJava, useJar, 1 Pfad (Ordner mit je 2 Jar- und Java-Dateien) in
		// paths
		() ->
		{
		    resetTest();
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(false);
		    classUnderTest.paths.add("../code/testfolder/CodeCollector/Testdateien/");
		    ArrayList<String> aktErg = classUnderTest.getSourceCode();
		    String erwErgsc = "erste Zeile \nzweite Zeile\ndritte Zeile \n//Beginn der zweiten Datei\nerste Zeile \nzweite Zeile\ndritte Zeile \n";

		    ArrayList<String> erwErg = new ArrayList<String>();
		    erwErg.add(erwErgsc);

		    assertEquals(erwErg, aktErg);
		    resetTest();
		}

//	() -> {}, <-- in die geschweiften Klammern schreibst du deinen Testfall:
//	// Wert festlegen
//		classUnderTest.setUseJavaFiles(); 
//	    	classUnderTest.setUseJarFiles(); 
//	    	//mit Pfaden füllen 
//	    	classUnderTest.paths.add("...");
//
//	    	//wie wird das Ergebnis berechnet(immer gleich)?
//	    	String aktErg = classUnderTest.getSourceCode();
//	    	//welches Ergebnis erwartest du?
//	    	String erwErg ="";
//	    	//Ergebnisse werden miteinander verglichen
//	    	assertEquals (erwErg, aktErg);
//	    	//muss gecleart werden, sonst wird das Ergebnis des nächsten Tests verfälscht, da paths aus irgendeinem Grund übernommen wird
//	    	classUnderTest.paths.clear();

	);
    }

    void resetTest()
    {
	classUnderTest.paths.clear();
	classUnderTest.paths2.clear();
	classUnderTest.setUseJavaFiles(false);
	classUnderTest.setUseJarFiles(false);
	classUnderTest.setUseCppAndHppFilesFiles(false);
	status = false;
    }

    String read(String file) throws IOException
    {
	String line = new String();
	FileReader filer = new FileReader(file);
	BufferedReader buffr = new BufferedReader(filer);
	String currLine;

	while ((currLine = buffr.readLine()) != null)
	{
	    line += currLine;
	    line += "\n";
	}
	filer.close();
	return line;
    }
}
