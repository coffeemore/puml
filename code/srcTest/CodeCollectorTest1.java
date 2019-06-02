import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

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
    void testCppAndHpp()
    {
	assertAll(
		
		() ->{
	    resetTest();
	    classUnderTest.setUseJarFiles(false);
	    classUnderTest.setUseJavaFiles(false);
	    classUnderTest.setUseCppAndHppFilesFiles(true);
	    classUnderTest.paths.add("../code/testfolder/xmlSpecifications/sources/cpp/Class1.cpp");
	    ArrayList<String> aktErg = classUnderTest.getSourceCode();

	    String erwErgsc2 = "#include \"Class1.hpp\"\n\n//In C++ findet die Implementierung der Methoden normalerweise in der cpp-Datei statt\n\n//Konstruktor\nClass1::Class1(Class4* iClass4, Class5* iClass5)\n{\n	this->iClass4 = iClass4; //Aggregationen zu Class4\n	this->iClass5 = iClass5;//Aggregationen zu Class5\n}\n\nint Class1::method1(int param1, int param2)\n{\n	int a = 0;\n	int b = 1;\n	if (a != b)\n	{\n		classInstance1.call1();	//Aufruf der Instanz-Methode\n	}\n	else\n	{\n		for (int i = 0; i != 100; i++)\n		{\n			Class2::call2();		//Aufruf der statischen Methode\n		}\n	}\n	this->method2();\n\n	return 1;\n}\n\nvoid Class1::method2()\n{\n	int i = 0;\n	switch (i)\n	{\n	case 0:\n	{\n		int j = 10;\n		while (j != 0)\n		{\n			method3();\n			j--;\n		}\n		break;\n	}\n	case 1:\n	{\n		int j = 10;\n		do\n		{\n			method3();\n			j--;\n		}\n		while (j != 0);\n		break;\n	}\n	default:\n	{\n		Class2 classInstance3 = new Class2();\n		Class3 classInstance1 = new Class3();	//Wegen dieser Instanz besteht eine Komposition zu Class3\n		classInstance1.call1();	//Sollte Class3 verwenden\n		this->classInstance1.call1();	//Sollte Class2 verwenden\n		classInstance2.call1();\n		classInstance3.call1();\n		classInstance4.callX();\n\n		break;\n	}\n	}\n}\n\nvoid Class1::method3()\n{\n	int a = 0;\n	int b = 1;\n	int c = 1;\n	if (a != b)\n	{\n		method3();	//Rekursiver selbstaufruf\n	}\n\n	if (b != c)\n	{\n		method4();	//Rekursiver wechselaufruf (Teil1)\n	}\n}\n\nvoid Class1::method4()\n{\n	int d = 0;\n	int e = 1;\n	if (d != e)\n	{\n		method3();	//Rekursiver wechselaufruf (Teil2)\n	}\n}\n";

	    ArrayList<String> erwErg = new ArrayList<String>();
	    erwErg.add(new String());
	    erwErg.add(erwErgsc2);

	    assertEquals(erwErg, aktErg);
	    resetTest();
	}, 
	
	// zwei Ddateien, !useJar, !useJava, usecppandhppfiles
	() ->
	{
	    resetTest();
	    classUnderTest.setUseJarFiles(false);
	    classUnderTest.setUseJavaFiles(false);
	    classUnderTest.setUseCppAndHppFilesFiles(true);
	    classUnderTest.paths.add("../code/testfolder/xmlSpecifications/sources/cpp/Class1.cpp");
	    classUnderTest.paths.add("../code/testfolder/xmlSpecifications/sources/cpp/Class1.hpp");
	    ArrayList<String> aktErg = classUnderTest.getSourceCode();

	    String erwErgsc1 = "#include <iostream>\n#include \"Class2.hpp\"\n#include \"Class3.hpp\"\n#include \"Class4.hpp\"\n#include \"Class5.hpp\"\n#include \"If1.hpp\"\n#include \"If2.hpp\"\n\nusing namespace std;\n\n//Es gibt in c++ keine Interfaces, daher werden diese durch die Mehrfachvererbung realisiert.\n//Interfaces werden daran erkannt, dass die Klassen nur Virtuelle Methoden beinhalten.\nclass Class1 : public Class3, public If1, public If2\n{\nprivate:\n    Class2* classInstance1 = new Class2();	//Komposition zu Class2\n    Class2* classInstance2 = new Class2();\n\n    Class4* iClass4;\n    Class5* iClass5;\n\npublic:\n    //Konstruktor\n    Class1(Class4* iClass4, Class5* iClass5);\n\n    int method1(int param1, int param2);\n\n    void method2();\n\n    void method3();\n\n    void method4();\n};\n";
	    String erwErgsc2 = "#include \"Class1.hpp\"\n\n//In C++ findet die Implementierung der Methoden normalerweise in der cpp-Datei statt\n\n//Konstruktor\nClass1::Class1(Class4* iClass4, Class5* iClass5)\n{\n	this->iClass4 = iClass4; //Aggregationen zu Class4\n	this->iClass5 = iClass5;//Aggregationen zu Class5\n}\n\nint Class1::method1(int param1, int param2)\n{\n	int a = 0;\n	int b = 1;\n	if (a != b)\n	{\n		classInstance1.call1();	//Aufruf der Instanz-Methode\n	}\n	else\n	{\n		for (int i = 0; i != 100; i++)\n		{\n			Class2::call2();		//Aufruf der statischen Methode\n		}\n	}\n	this->method2();\n\n	return 1;\n}\n\nvoid Class1::method2()\n{\n	int i = 0;\n	switch (i)\n	{\n	case 0:\n	{\n		int j = 10;\n		while (j != 0)\n		{\n			method3();\n			j--;\n		}\n		break;\n	}\n	case 1:\n	{\n		int j = 10;\n		do\n		{\n			method3();\n			j--;\n		}\n		while (j != 0);\n		break;\n	}\n	default:\n	{\n		Class2 classInstance3 = new Class2();\n		Class3 classInstance1 = new Class3();	//Wegen dieser Instanz besteht eine Komposition zu Class3\n		classInstance1.call1();	//Sollte Class3 verwenden\n		this->classInstance1.call1();	//Sollte Class2 verwenden\n		classInstance2.call1();\n		classInstance3.call1();\n		classInstance4.callX();\n\n		break;\n	}\n	}\n}\n\nvoid Class1::method3()\n{\n	int a = 0;\n	int b = 1;\n	int c = 1;\n	if (a != b)\n	{\n		method3();	//Rekursiver selbstaufruf\n	}\n\n	if (b != c)\n	{\n		method4();	//Rekursiver wechselaufruf (Teil1)\n	}\n}\n\nvoid Class1::method4()\n{\n	int d = 0;\n	int e = 1;\n	if (d != e)\n	{\n		method3();	//Rekursiver wechselaufruf (Teil2)\n	}\n}\n";

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
	    classUnderTest.paths.add("../code/testfolder/xmlSpecifications/sources/cpp/Class1.cpp");
	    classUnderTest.paths.add("../code/testfolder/xmlSpecifications/sources/cpp/Class1.hpp");
	    ArrayList<String> aktErg = classUnderTest.getSourceCode();

	    String erwErgsc1 = "#include <iostream>\n#include \"Class2.hpp\"\n#include \"Class3.hpp\"\n#include \"Class4.hpp\"\n#include \"Class5.hpp\"\n#include \"If1.hpp\"\n#include \"If2.hpp\"\n\nusing namespace std;\n\n//Es gibt in c++ keine Interfaces, daher werden diese durch die Mehrfachvererbung realisiert.\n//Interfaces werden daran erkannt, dass die Klassen nur Virtuelle Methoden beinhalten.\nclass Class1 : public Class3, public If1, public If2\n{\nprivate:\n    Class2* classInstance1 = new Class2();	//Komposition zu Class2\n    Class2* classInstance2 = new Class2();\n\n    Class4* iClass4;\n    Class5* iClass5;\n\npublic:\n    //Konstruktor\n    Class1(Class4* iClass4, Class5* iClass5);\n\n    int method1(int param1, int param2);\n\n    void method2();\n\n    void method3();\n\n    void method4();\n};\n";
	    String erwErgsc2 = "#include \"Class1.hpp\"\n\n//In C++ findet die Implementierung der Methoden normalerweise in der cpp-Datei statt\n\n//Konstruktor\nClass1::Class1(Class4* iClass4, Class5* iClass5)\n{\n	this->iClass4 = iClass4; //Aggregationen zu Class4\n	this->iClass5 = iClass5;//Aggregationen zu Class5\n}\n\nint Class1::method1(int param1, int param2)\n{\n	int a = 0;\n	int b = 1;\n	if (a != b)\n	{\n		classInstance1.call1();	//Aufruf der Instanz-Methode\n	}\n	else\n	{\n		for (int i = 0; i != 100; i++)\n		{\n			Class2::call2();		//Aufruf der statischen Methode\n		}\n	}\n	this->method2();\n\n	return 1;\n}\n\nvoid Class1::method2()\n{\n	int i = 0;\n	switch (i)\n	{\n	case 0:\n	{\n		int j = 10;\n		while (j != 0)\n		{\n			method3();\n			j--;\n		}\n		break;\n	}\n	case 1:\n	{\n		int j = 10;\n		do\n		{\n			method3();\n			j--;\n		}\n		while (j != 0);\n		break;\n	}\n	default:\n	{\n		Class2 classInstance3 = new Class2();\n		Class3 classInstance1 = new Class3();	//Wegen dieser Instanz besteht eine Komposition zu Class3\n		classInstance1.call1();	//Sollte Class3 verwenden\n		this->classInstance1.call1();	//Sollte Class2 verwenden\n		classInstance2.call1();\n		classInstance3.call1();\n		classInstance4.callX();\n\n		break;\n	}\n	}\n}\n\nvoid Class1::method3()\n{\n	int a = 0;\n	int b = 1;\n	int c = 1;\n	if (a != b)\n	{\n		method3();	//Rekursiver selbstaufruf\n	}\n\n	if (b != c)\n	{\n		method4();	//Rekursiver wechselaufruf (Teil1)\n	}\n}\n\nvoid Class1::method4()\n{\n	int d = 0;\n	int e = 1;\n	if (d != e)\n	{\n		method3();	//Rekursiver wechselaufruf (Teil2)\n	}\n}\n";

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
	    classUnderTest.paths.add("../code/testfolder/xmlSpecifications/sources/cpp/Class1.cpp");
	    classUnderTest.paths.add("../code/testfolder/xmlSpecifications/sources/cpp/Class1.hpp");
	    ArrayList<String> aktErg = classUnderTest.getSourceCode();

	    String erwErgsc1 = "#include <iostream>\n#include \"Class2.hpp\"\n#include \"Class3.hpp\"\n#include \"Class4.hpp\"\n#include \"Class5.hpp\"\n#include \"If1.hpp\"\n#include \"If2.hpp\"\n\nusing namespace std;\n\n//Es gibt in c++ keine Interfaces, daher werden diese durch die Mehrfachvererbung realisiert.\n//Interfaces werden daran erkannt, dass die Klassen nur Virtuelle Methoden beinhalten.\nclass Class1 : public Class3, public If1, public If2\n{\nprivate:\n    Class2* classInstance1 = new Class2();	//Komposition zu Class2\n    Class2* classInstance2 = new Class2();\n\n    Class4* iClass4;\n    Class5* iClass5;\n\npublic:\n    //Konstruktor\n    Class1(Class4* iClass4, Class5* iClass5);\n\n    int method1(int param1, int param2);\n\n    void method2();\n\n    void method3();\n\n    void method4();\n};\n";
	    String erwErgsc2 = "#include \"Class1.hpp\"\n\n//In C++ findet die Implementierung der Methoden normalerweise in der cpp-Datei statt\n\n//Konstruktor\nClass1::Class1(Class4* iClass4, Class5* iClass5)\n{\n	this->iClass4 = iClass4; //Aggregationen zu Class4\n	this->iClass5 = iClass5;//Aggregationen zu Class5\n}\n\nint Class1::method1(int param1, int param2)\n{\n	int a = 0;\n	int b = 1;\n	if (a != b)\n	{\n		classInstance1.call1();	//Aufruf der Instanz-Methode\n	}\n	else\n	{\n		for (int i = 0; i != 100; i++)\n		{\n			Class2::call2();		//Aufruf der statischen Methode\n		}\n	}\n	this->method2();\n\n	return 1;\n}\n\nvoid Class1::method2()\n{\n	int i = 0;\n	switch (i)\n	{\n	case 0:\n	{\n		int j = 10;\n		while (j != 0)\n		{\n			method3();\n			j--;\n		}\n		break;\n	}\n	case 1:\n	{\n		int j = 10;\n		do\n		{\n			method3();\n			j--;\n		}\n		while (j != 0);\n		break;\n	}\n	default:\n	{\n		Class2 classInstance3 = new Class2();\n		Class3 classInstance1 = new Class3();	//Wegen dieser Instanz besteht eine Komposition zu Class3\n		classInstance1.call1();	//Sollte Class3 verwenden\n		this->classInstance1.call1();	//Sollte Class2 verwenden\n		classInstance2.call1();\n		classInstance3.call1();\n		classInstance4.callX();\n\n		break;\n	}\n	}\n}\n\nvoid Class1::method3()\n{\n	int a = 0;\n	int b = 1;\n	int c = 1;\n	if (a != b)\n	{\n		method3();	//Rekursiver selbstaufruf\n	}\n\n	if (b != c)\n	{\n		method4();	//Rekursiver wechselaufruf (Teil1)\n	}\n}\n\nvoid Class1::method4()\n{\n	int d = 0;\n	int e = 1;\n	if (d != e)\n	{\n		method3();	//Rekursiver wechselaufruf (Teil2)\n	}\n}\n";

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
		    classUnderTest.paths.add("../code/testfolder/xmlSpecifications/sources/cpp/Class1.cpp");
		    classUnderTest.paths.add("../code/testfolder/xmlSpecifications/sources/cpp/Class1.hpp");
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
}
