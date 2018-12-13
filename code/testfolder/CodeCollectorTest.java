import static org.junit.jupiter.api.Assertions.*;

//import java.io.File;
//import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CodeCollectorTest
{
    private CodeCollector classUnderTest;

    @BeforeEach
    public void SetUp() throws Exception
    {
	classUnderTest = new CodeCollector();
    }
//    @SuppressWarnings("deprecation")
//    void resetTest() {
//	classUnderTest.paths.clear();
//	classUnderTest.setUseJavaFiles(new Boolean(null));
//	classUnderTest.setUseJarFiles(new Boolean(null));    }

    @Test
    void testGetSourceCode()
    {
	
	assertAll(
		// Test: use Java, !useJar, 1 Pfad in paths
		
		() -> 
		{
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(false);
//	    classUnderTest.paths.add(System.getProperty("user.dir") + File.separator + "code" + File.separator +"testfolder"   + File.separator +"CodeJavaTest.java");
		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//CodeJavaTest.java");
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = "erste Zeile zweite Zeiledritte Zeile ";
		    assertEquals(erwErg, aktErg);
		    classUnderTest.paths.clear();
		},
		// 1 Pfad, !useJava, useJar (1 Datei in Jar)
		() ->
		{
		    classUnderTest.setUseJavaFiles(false);
		    classUnderTest.setUseJarFiles(true);
		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//jartest.jar");
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = "erste Zeile zweite Zeiledritte Zeile ";
		    assertEquals(erwErg, aktErg);
		    classUnderTest.paths.clear();
		},
		// 1 Pfad, !useJava, useJar (2 Dateien in Jar)		
		() ->
		{	
		    classUnderTest.setUseJavaFiles(false);
		    classUnderTest.setUseJarFiles(true);
		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//jartest2.jar");
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = "erste Zeile zweite Zeiledritte Zeile //Beginn der zweiten Dateierste Zeile zweite Zeiledritte Zeile ";
		    assertEquals(erwErg, aktErg);
		    classUnderTest.paths.clear();
		},
		// 1 Pfad, useJava, useJar
		() ->
		{
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(true);
		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//CodeJavaTest.java");
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = null;
		    assertEquals(erwErg, aktErg);
		    classUnderTest.paths.clear();
		},
		// 1 Pfad, (useJava, useJar) nicht initialisiert
		() ->
		{
		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//CodeJavaTest.java");
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = null;
		    assertEquals(erwErg, aktErg);
		    classUnderTest.paths.clear();
		},
		// 1 Pfad, useJava, useJar nicht initialisiert
		() ->
		{
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//CodeJavaTest.java");
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = null;
		    assertEquals(erwErg, aktErg);
		    classUnderTest.paths.clear();
		},
		// 1 Pfad, useJar, useJava nicht initialisiert
		() ->
		{
		    classUnderTest.setUseJarFiles(true);
		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//CodeJavaTest.java");
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = null;
		    assertEquals(erwErg, aktErg);
		    classUnderTest.paths.clear();
		}, // 1 Pfad, useJava, useJar nicht initialisiert
		() ->
		{
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//CodeJavaTest.java");
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = null;
		    assertEquals(erwErg, aktErg);
		    classUnderTest.paths.clear();
		},
		// 1 Pfad, useJar, useJava nicht initialisiert
		() ->
		{
		    classUnderTest.setUseJarFiles(true);
		    classUnderTest.paths.add("//home//developer//workspace//puml//code//testfolder//CodeJavaTest.java");
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = null;
		    assertEquals(erwErg, aktErg);
		    classUnderTest.paths.clear();
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

		// Test: use Java, !useJar, 2 Pfade in paths
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
		    String erwErg = "erste Zeile zweite Zeiledritte Zeile //Beginn der zweiten Dateierste Zeile zweite Zeiledritte Zeile ";
		    assertEquals(erwErg, aktErg);
		    classUnderTest.paths.clear();
		},

		// keine Pfadangabe
		// keine Pfadangabe, useJava, !useJar
		() ->
		{
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(false);
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = null;
		    assertEquals(erwErg, aktErg);
		    classUnderTest.paths.clear();

		},
		// keine Pfadangabe, useJava, useJar
		() ->
		{
		    classUnderTest.setUseJavaFiles(true);
		    classUnderTest.setUseJarFiles(true);
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = null;
		    assertEquals(erwErg, aktErg);
		    classUnderTest.paths.clear();

		},

		// keine Pfadangabe, !useJava, useJar
		() ->
		{
		    classUnderTest.setUseJavaFiles(false);
		    classUnderTest.setUseJarFiles(true);
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = null;
		    assertEquals(erwErg, aktErg);
		    classUnderTest.paths.clear();

		},
		// keine Pfadangabe, !useJava, !useJar
		() ->
		{
		    classUnderTest.setUseJavaFiles(false);
		    classUnderTest.setUseJarFiles(false);
		    String aktErg = classUnderTest.getSourceCode();
		    String erwErg = null;
		    assertEquals(erwErg, aktErg);
		    classUnderTest.paths.clear();

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

}
