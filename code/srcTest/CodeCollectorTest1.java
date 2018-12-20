import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;

//Testfälle anpassen - 
//	für Ordner mit Unterordner - noch buggy?


class CodeCollectorTest1
{
    private CodeCollector classUnderTest;

    @BeforeEach
    public void SetUp() throws Exception
    {
	classUnderTest = new CodeCollector();
    }

    boolean status = false;

    @Test
    void testGetSourceCode()
    {

	assertAll(
		// Test: use Java, !useJar, 1 Pfad (Java) in paths

		() ->
		{
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(false);
		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//CodeJavaTest.java");
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = "erste Zeile zweite Zeiledritte Zeile \n";
		    assertEquals(erwErg, aktErg);
		    resetTest();
		},
		// 1 Pfad, !useJava, useJar (1 Java-Datei in Jar)
		() ->
		{
		    classUnderTest.setUseJavaFiles(false);
		    classUnderTest.setUseJarFiles(true);
		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//jartest.jar");
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = "erste Zeile zweite Zeiledritte Zeile \n";
		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

		// 1 Pfad (Jar-Datei), !useJava, !useJar
		() ->
		{
		    classUnderTest.setUseJavaFiles(false);
		    classUnderTest.setUseJarFiles(false);
		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//jartest.jar");
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
		    classUnderTest.setUseJavaFiles(false);
		    classUnderTest.setUseJarFiles(false);
		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//CodeJavaTest.java");
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
		    classUnderTest.setUseJavaFiles(false);
		    classUnderTest.setUseJarFiles(true);
		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//jartest2.jar");
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = "erste Zeile zweite Zeiledritte Zeile \n//Beginn der zweiten Dateierste Zeile zweite Zeiledritte Zeile \n";
		    assertEquals(erwErg, aktErg);
		    resetTest();
		},
		// 1 Pfad (Java), useJava, useJar
		() ->
		{
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(true);
		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//CodeJavaTest.java");
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = "erste Zeile zweite Zeiledritte Zeile \n";
		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

		// 1 Pfad (Jar), useJava, useJar
		() ->
		{
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(true);
		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//jartest2.jar");
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = "erste Zeile zweite Zeiledritte Zeile \n//Beginn der zweiten Dateierste Zeile zweite Zeiledritte Zeile \n";
		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

//		// 1 Pfad, useJava, useJar nicht initialisiert
//		() ->
//		{
//		    classUnderTest.setUseJavaFiles(true);
//		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//CodeJavaTest.java");
//		    String aktErg = classUnderTest.getSourceCode();
//		    String erwErg = null;
//		    assertEquals(erwErg, aktErg);
//		    resetTest();
//		},
//		// 1 Pfad, useJar, useJava nicht initialisiert
//		() ->
//		{
//		    classUnderTest.setUseJarFiles(true);
//		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//CodeJavaTest.java");
//		    String aktErg = classUnderTest.getSourceCode();
//		    String erwErg = null;
//		    assertEquals(erwErg, aktErg);
//		    resetTest();
//		}, 
		// 1 Pfad, useJava, useJar nicht initialisiert
		() ->
		{
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//CodeJavaTest.java");
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = "erste Zeile zweite Zeiledritte Zeile \n";
		    assertEquals(erwErg, aktErg);
		    resetTest();
		},
		// 1 Pfad(Java), useJar, useJava nicht initialisiert
		() ->
		{
		    classUnderTest.setUseJarFiles(true);
		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//CodeJavaTest.java");
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = "";
		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

//		// 1 Pfad, !useJava, useJar nicht initialisiert
//		() ->
//		{
//		    classUnderTest.setUseJavaFiles(false);
//		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//CodeJavaTest.java");
//		    String aktErg = classUnderTest.getSourceCode();
//		    String erwErg = null;
//		    assertEquals(erwErg, aktErg);
//		    resetTest();
//		},
//		// 1 Pfad, !useJar, useJava nicht initialisiert
//		() ->
//		{
//		    classUnderTest.setUseJarFiles(false);
//		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//CodeJavaTest.java");
//		    String aktErg = classUnderTest.getSourceCode();
//		    String erwErg = null;
//		    assertEquals(erwErg, aktErg);
//		    resetTest();
//		},

		// Test: use Java, !useJar, 2 Pfade (Java) in paths
		() ->
		{
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(false);
		    // classUnderTest.paths.add(System.getProperty("user.dir") + File.separator +
		    // "code" + File.separator +"testfolder" + File.separator +"CodeJavaTest.java");
		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//CodeJavaTest.java");
		    classUnderTest.paths
			    .add("//home//developer//workspace//puml//code//testfolder//CodeJavaTest1.java");
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = "erste Zeile zweite Zeiledritte Zeile \n//Beginn der zweiten Dateierste Zeile zweite Zeiledritte Zeile \n";
		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

		// Test: use !Java, useJar, 2 Pfade (Java) in paths
		() ->
		{
		    classUnderTest.setUseJavaFiles(false);
		    classUnderTest.setUseJarFiles(true);
		    // classUnderTest.paths.add(System.getProperty("user.dir") + File.separator +
		    // "code" + File.separator +"testfolder" + File.separator +"CodeJavaTest.java");
		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//CodeJavaTest.java");
		    classUnderTest.paths
			    .add("//home//developer//workspace//puml//code//testfolder//CodeJavaTest1.java");
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = "";
		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

		// keine Pfadangabe
		// keine Pfadangabe, useJava, !useJar
		() ->
		{

		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(false);
		    classUnderTest.paths.clear();
		    try
		    {
			String aktErg = classUnderTest.getSourceCode();
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
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(true);
		    try
		    {
			String aktErg = classUnderTest.getSourceCode();
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
		    classUnderTest.setUseJavaFiles(false);
		    classUnderTest.setUseJarFiles(true);
		    try
		    {
			String aktErg = classUnderTest.getSourceCode();
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
		    classUnderTest.setUseJavaFiles(false);
		    classUnderTest.setUseJarFiles(false);
		    try
		    {
			String aktErg = classUnderTest.getSourceCode();
		    } catch (NullPointerException | IllegalArgumentException e)
		    {
			status = true;
		    }
		    assertTrue(status);
		    resetTest();

		},
		// Test: use Java, !useJar, 1 Pfad (Ordner mit je 2 Jar- und Java-Dateien) in
		// paths

		() ->
		{
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(false);
		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder");
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = "erste Zeile zweite Zeiledritte Zeile \n//Beginn der zweiten Dateierste Zeile zweite Zeiledritte Zeile \n";
		    assertEquals(erwErg, aktErg);
		    resetTest();
		},
		// Test: !useJava, useJar; 1 Pfad mit Jar-Datei (ohne Java-Datei darin)
		() ->
		{
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(false);
		    classUnderTest.paths.add("//home//developer//workspace//test3.jar");
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = "";
		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

		// Test: !useJava, useJar; 1 Pfad mit Jar-Datei (leeres Archiv)
		() ->
		{
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(false);
		    classUnderTest.paths.add("//home//developer//workspace//test4.jar");
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = "";
		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

		// Test: !useJava, useJar, 2 Pfade (Jar-Dateien) in paths

		() ->
		{
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(false);
		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder");
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = "erste Zeile zweite Zeiledritte Zeile \n//Beginn der zweiten Dateierste Zeile zweite Zeiledritte Zeile \n";
		    assertEquals(erwErg, aktErg);
		    resetTest();
		},

		// Test: !useJava, useJar, 1 Pfad (Ordner mit je 2 Jar- und Java-Dateien) in
		// paths

		() ->
		{
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(false);
		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder");
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = "erste Zeile zweite Zeiledritte Zeile \n//Beginn der zweiten Dateierste Zeile zweite Zeiledritte Zeile \n";
		    assertEquals(erwErg, aktErg);
		    resetTest();
		}

//	() -> {}, <-- in die geschweiften Klammern schreibst du deinen Testfall:
	// Wert festlegen
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
	classUnderTest.setUseJavaFiles(false);
	classUnderTest.setUseJarFiles(false);
    }
}
