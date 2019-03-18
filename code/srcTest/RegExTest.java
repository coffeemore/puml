//import static org.junit.jupiter.api.Assertions.*;

//import java.util.regex.Matcher;
//import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class RegExTest
{

    @Test
    void test()
    {	
	ParserJava parsertest = new ParserJava();
	parsertest.parse("\r\n" + 
		"\r\n" + 
		"public class ClassConnection\r\n" + 
		"{\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    enum connectionType\r\n" + 
		"    {\r\n" + 
		"	extension, composition, aggregation\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"    private int from;\r\n" + 
		"    private int to;\r\n" + 
		"    private connectionType connection;\r\n" + 
		"    \r\n" + 
		"\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    public ClassConnection(int from, int to, connectionType connection)\r\n" + 
		"    {\r\n" + 
		"    	this.from=from;\r\n" + 
		"    	this.to=to;\r\n" + 
		"    	this.connection=connection;\r\n" + 
		"    };\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    public void setFrom(int newVar)\r\n" + 
		"    {\r\n" + 
		"	from = newVar;\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    public int getFrom()\r\n" + 
		"    {\r\n" + 
		"	return from;\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    public void setTo(int newVar)\r\n" + 
		"    {\r\n" + 
		"	to = newVar;\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    public int getTo()\r\n" + 
		"    {\r\n" + 
		"	return to;\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    public void setConnection(connectionType newVar)\r\n" + 
		"    {\r\n" + 
		"	connection = newVar;\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    public connectionType getConnection()\r\n" + 
		"    {\r\n" + 
		"	return connection;\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"}\r\n" + 
		"import java.io.BufferedReader;\r\n" + 
		"import java.io.File;\r\n" + 
		"import java.io.FileReader;\r\n" + 
		"import java.io.IOException;\r\n" + 
		"import java.io.InputStreamReader;\r\n" + 
		"import java.util.ArrayList;\r\n" + 
		"import java.util.Enumeration;\r\n" + 
		"import java.util.zip.ZipEntry;\r\n" + 
		"import java.util.zip.ZipFile;\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"public class CodeCollector\r\n" + 
		"{\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    public ArrayList<String> paths;\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    private boolean useJavaFiles;\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    private boolean useJarFiles;\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    public CodeCollector()\r\n" + 
		"    {\r\n" + 
		"	paths = new ArrayList<String>();\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    public String getSourceCode()\r\n" + 
		"    {\r\n" + 
		"	String sc = new String();\r\n" + 
		"	BufferedReader buffr = null;\r\n" + 
		"	FileReader filer = null;\r\n" + 
		"	ZipFile zFile = null;\r\n" + 
		"	File file = null;\r\n" + 
		"\r\n" + 
		"	if (!paths.isEmpty())\r\n" + 
		"	{\r\n" + 
		"\r\n" + 
		"	    while (contDir(paths))\r\n" + 
		"	    {\r\n" + 
		"		for (int j = 0; j < paths.size(); j++)\r\n" + 
		"		{\r\n" + 
		"		    file = new File(paths.get(j));\r\n" + 
		"		    if (file.isDirectory())\r\n" + 
		"		    {\r\n" + 
		"			File[] fArray = file.listFiles();\r\n" + 
		"			for (int i = 0; i < fArray.length; i++)\r\n" + 
		"			{\r\n" + 
		"			    paths.add(fArray[i].getAbsolutePath());\r\n" + 
		"			}\r\n" + 
		"			paths.remove(paths.get(j));\r\n" + 
		"		    }\r\n" + 
		"		}\r\n" + 
		"	    }\r\n" + 
		"	    if (useJavaFiles && !useJarFiles)\r\n" + 
		"	    {\r\n" + 
		"\r\n" + 
		"		return (collectJava(sc, buffr, filer));\r\n" + 
		"	    } else\r\n" + 
		"	    {\r\n" + 
		"		if (!useJavaFiles && useJarFiles)\r\n" + 
		"		{\r\n" + 
		"		    \r\n" + 
		"		    return (collectJar(sc, buffr, zFile));\r\n" + 
		"		}\r\n" + 
		"\r\n" + 
		"		else\r\n" + 
		"		{\r\n" + 
		"\r\n" + 
		"		    if (useJavaFiles && useJarFiles)\r\n" + 
		"		    {\r\n" + 
		"			sc = collectJava(sc, buffr, filer);\r\n" + 
		"			sc += collectJar(sc, buffr, zFile);\r\n" + 
		"			return sc;\r\n" + 
		"		    } else\r\n" + 
		"		    {\r\n" + 
		"			\r\n" + 
		"			throw new IllegalArgumentException();\r\n" + 
		"		    }\r\n" + 
		"		}\r\n" + 
		"	    }\r\n" + 
		"	}\r\n" + 
		"\r\n" + 
		"	else\r\n" + 
		"	{\r\n" + 
		"	    throw new NullPointerException();\r\n" + 
		"	}\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    private String collectJava(String sc, BufferedReader buffr, FileReader filer)\r\n" + 
		"    {\r\n" + 
		"\r\n" + 
		"	for (int i = 0; i < paths.size(); i++)\r\n" + 
		"	{\r\n" + 
		"\r\n" + 
		"	    try\r\n" + 
		"	    {\r\n" + 
		"\r\n" + 
		"		if (paths.get(i).endsWith(\".java\"))\r\n" + 
		"		{\r\n" + 
		"		    filer = new FileReader(paths.get(i));\r\n" + 
		"		    buffr = new BufferedReader(filer);\r\n" + 
		"		    String currLine;\r\n" + 
		"		    \r\n" + 
		"		    while ((currLine = buffr.readLine()) != null)\r\n" + 
		"		    {\r\n" + 
		"			sc += currLine;\r\n" + 
		"		    }\r\n" + 
		"		    sc += \"\\n\";\r\n" + 
		"		}\r\n" + 
		"	    } catch (IOException e)\r\n" + 
		"	    {\r\n" + 
		"		e.printStackTrace();\r\n" + 
		"	    } finally\r\n" + 
		"	    {\r\n" + 
		"\r\n" + 
		"		try\r\n" + 
		"		{\r\n" + 
		"\r\n" + 
		"		    if (filer != null)\r\n" + 
		"		    {\r\n" + 
		"			filer.close();\r\n" + 
		"		    }\r\n" + 
		"		    if (buffr != null)\r\n" + 
		"		    {\r\n" + 
		"			buffr.close();\r\n" + 
		"		    }\r\n" + 
		"		} catch (IOException ex)\r\n" + 
		"		{\r\n" + 
		"		    ex.printStackTrace();\r\n" + 
		"		}\r\n" + 
		"	    }\r\n" + 
		"	}\r\n" + 
		"	return sc;\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    private String collectJar(String sc, BufferedReader buffr, ZipFile zFile)\r\n" + 
		"    {\r\n" + 
		"	try\r\n" + 
		"	{\r\n" + 
		"\r\n" + 
		"	    for (int i = 0; i < paths.size(); i++)\r\n" + 
		"	    {\r\n" + 
		"		if (paths.get(i).endsWith(\".jar\"))\r\n" + 
		"		{\r\n" + 
		"\r\n" + 
		"		    zFile = new ZipFile(paths.get(i));\r\n" + 
		"		    if (zFile != null)\r\n" + 
		"		    {\r\n" + 
		"			\r\n" + 
		"			Enumeration<? extends ZipEntry> entries = zFile.entries();\r\n" + 
		"			while (entries.hasMoreElements())\r\n" + 
		"			{\r\n" + 
		"			    ZipEntry entry = entries.nextElement();\r\n" + 
		"			    \r\n" + 
		"			    if (!entry.isDirectory() && entry.getName().endsWith(\".java\"))\r\n" + 
		"			    {\r\n" + 
		"				buffr = new BufferedReader(new InputStreamReader(zFile.getInputStream(entry)));\r\n" + 
		"				String currLine;\r\n" + 
		"\r\n" + 
		"				while ((currLine = buffr.readLine()) != null)\r\n" + 
		"				{\r\n" + 
		"				    sc += currLine;\r\n" + 
		"				}\r\n" + 
		"				sc += \"\\n\";\r\n" + 
		"			    } else\r\n" + 
		"			    {\r\n" + 
		"				continue;\r\n" + 
		"			    }\r\n" + 
		"			}\r\n" + 
		"		    }\r\n" + 
		"		}\r\n" + 
		"	    }\r\n" + 
		"\r\n" + 
		"	} catch (IOException e)\r\n" + 
		"	{\r\n" + 
		"	    e.printStackTrace();\r\n" + 
		"	} finally\r\n" + 
		"	{\r\n" + 
		"	    try\r\n" + 
		"	    {\r\n" + 
		"		if (buffr != null)\r\n" + 
		"		{\r\n" + 
		"		    buffr.close();\r\n" + 
		"		}\r\n" + 
		"		if (zFile != null)\r\n" + 
		"		{\r\n" + 
		"		    zFile.close();\r\n" + 
		"		}\r\n" + 
		"	    } catch (IOException ex)\r\n" + 
		"	    {\r\n" + 
		"		ex.printStackTrace();\r\n" + 
		"	    }\r\n" + 
		"	}\r\n" + 
		"	return sc;\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    private boolean contDir(ArrayList<String> paths)\r\n" + 
		"    {\r\n" + 
		"	\r\n" + 
		"	for (int i = 0; i < paths.size(); i++)\r\n" + 
		"	{\r\n" + 
		"	    File file = new File(paths.get(i));\r\n" + 
		"	    if (file.isDirectory())\r\n" + 
		"	    {\r\n" + 
		"		return true;\r\n" + 
		"	    }\r\n" + 
		"	}\r\n" + 
		"	return false;\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"    public boolean isUseJavaFiles()\r\n" + 
		"    {\r\n" + 
		"	return useJavaFiles;\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"    public void setUseJavaFiles(boolean useJavaFiles)\r\n" + 
		"    {\r\n" + 
		"	this.useJavaFiles = useJavaFiles;\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"    public boolean isUseJarFiles()\r\n" + 
		"    {\r\n" + 
		"	return useJarFiles;\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"    public void setUseJarFiles(boolean useJarFiles)\r\n" + 
		"    {\r\n" + 
		"	this.useJarFiles = useJarFiles;\r\n" + 
		"    }\r\n" + 
		"}\r\n" + 
		"\r\n" + 
		"import java.io.IOException;\r\n" + 
		"\r\n" + 
		"import org.apache.commons.cli.CommandLine;\r\n" + 
		"import org.apache.commons.cli.CommandLineParser;\r\n" + 
		"import org.apache.commons.cli.DefaultParser;\r\n" + 
		"import org.apache.commons.cli.HelpFormatter;\r\n" + 
		"import org.apache.commons.cli.Option;\r\n" + 
		"import org.apache.commons.cli.Options;\r\n" + 
		"import org.apache.commons.cli.ParseException;\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"public class Console extends PUMLgenerator\r\n" + 
		"{\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    public Console()\r\n" + 
		"    {\r\n" + 
		"	\r\n" + 
		"\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    void showConsole(String[] args) throws ParseException \r\n" + 
		"    {\r\n" + 
		"\r\n" + 
		"	\r\n" + 
		"	Options options = new Options();\r\n" + 
		"	options.addOption(\"c\", false, \"Konsole mit Anleitung wird aufgerufen\");\r\n" + 
		"\r\n" + 
		"	Option input = Option.builder() \r\n" + 
		"		.longOpt(\"i\").argName(\"filepath\").hasArg().valueSeparator(';').numberOfArgs(Option.UNLIMITED_VALUES)\r\n" + 
		"		.desc(\"Angabe der zu verarbeitenden Pfade, durch ; getrennt\").build();\r\n" + 
		"	options.addOption(input);\r\n" + 
		"\r\n" + 
		"	\r\n" + 
		"	options.addOption(\"ijar\", false, \"Dateien mit der Endung .jar werden ignoriert.\");\r\n" + 
		"\r\n" + 
		"	options.addOption(\"ijava\", false, \"Dateien mit der Endung .java werden ignoriert.\");\r\n" + 
		"\r\n" + 
		"	Option output = Option.builder() \r\n" + 
		"		.longOpt(\"o\").argName(\"filepath\").hasArg().desc(\"Angabe des Pfades fuer den Zielordner.\").build();\r\n" + 
		"	options.addOption(output);\r\n" + 
		"\r\n" + 
		"	\r\n" + 
		"	CommandLineParser commandParser = new DefaultParser();\r\n" + 
		"	CommandLine cmd = commandParser.parse(options, args);\r\n" + 
		"\r\n" + 
		"	\r\n" + 
		"	if (cmd.hasOption(\"c\")) \r\n" + 
		"	{\r\n" + 
		"	    HelpFormatter formatter = new HelpFormatter();\r\n" + 
		"	    formatter.printHelp(\"PUML\", options);\r\n" + 
		"	}\r\n" + 
		"	if (cmd.hasOption(\"ijar\")) \r\n" + 
		"	{\r\n" + 
		"	    codeCollector.setUseJarFiles(false);\r\n" + 
		"	}\r\n" + 
		"	if (cmd.hasOption(\"ijava\")) \r\n" + 
		"	{\r\n" + 
		"	    codeCollector.setUseJavaFiles(false);\r\n" + 
		"	}\r\n" + 
		"	if (cmd.hasOption(\"i\")) \r\n" + 
		"	{\r\n" + 
		"	    for (int i = 0; i < cmd.getOptionValues(\"i\").length; i++) \r\n" + 
		"	    {\r\n" + 
		"		codeCollector.paths.add(cmd.getOptionValues(\"i\")[i]);\r\n" + 
		"		System.out.println(codeCollector.paths.get(i));\r\n" + 
		"	    }\r\n" + 
		"	    PUMLgenerator.parser.parse(codeCollector.getSourceCode()); \r\n" + 
		"								       \r\n" + 
		"\r\n" + 
		"	    if (cmd.hasOption(\"o\")) \r\n" + 
		"	    {\r\n" + 
		"		outputPUML.savePUMLtoFile(parser.getParsingResult(), cmd.getOptionValue(\"o\")); \r\n" + 
		"											       \r\n" + 
		"		System.out.println(\"test\");\r\n" + 
		"		try\r\n" + 
		"		{\r\n" + 
		"		    outputPUML.createPlantUML(cmd.getOptionValue(\"o\"), outputPUML.getPUML(parser.getParsingResult())); \r\n" + 
		"\r\n" + 
		"		}\r\n" + 
		"		catch (IOException e)\r\n" + 
		"		{\r\n" + 
		"		    e.printStackTrace();\r\n" + 
		"		}\r\n" + 
		"		System.out.println(\r\n" + 
		"			\"Zielordner:\" + cmd.getOptionValue(\"o\") + \"\\nQuelle: \" + codeCollector.getSourceCode());\r\n" + 
		"	    }\r\n" + 
		"	    else\r\n" + 
		"	    {\r\n" + 
		"		outputPUML.savePUMLtoFile(parser.getParsingResult(), \"./outputPUML\");\r\n" + 
		"		try\r\n" + 
		"		{\r\n" + 
		"		    outputPUML.createPlantUML(\"./outputPUML\", outputPUML.getPUML(parser.getParsingResult())); \r\n" + 
		"												  \r\n" + 
		"		}\r\n" + 
		"		catch (IOException e)\r\n" + 
		"		{\r\n" + 
		"		    e.printStackTrace();\r\n" + 
		"		}\r\n" + 
		"	    }\r\n" + 
		"	}\r\n" + 
		"	else if (!cmd.hasOption(\"c\"))\r\n" + 
		"	{\r\n" + 
		"	    System.out.println(\"Es fehlt ein zu bearbeitender Pfad.\");\r\n" + 
		"	}\r\n" + 
		"    }\r\n" + 
		"}\r\n" + 
		"import java.awt.EventQueue;\r\n" + 
		"\r\n" + 
		"import javax.swing.JFrame;\r\n" + 
		"import javax.swing.JButton;\r\n" + 
		"import java.awt.BorderLayout;\r\n" + 
		"import java.awt.event.ActionListener;\r\n" + 
		"import java.awt.event.ActionEvent;\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"public class GUI\r\n" + 
		"{\r\n" + 
		"    private JFrame frame;\r\n" + 
		"\r\n" + 
		"    GUI()\r\n" + 
		"    {\r\n" + 
		"	initialize();\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    void showGUI()\r\n" + 
		"    {\r\n" + 
		"\r\n" + 
		"	EventQueue.invokeLater(new Runnable()\r\n" + 
		"	{\r\n" + 
		"	    public void run()\r\n" + 
		"	    {\r\n" + 
		"		try\r\n" + 
		"		{\r\n" + 
		"		    GUI window = new GUI();\r\n" + 
		"		    window.frame.setVisible(true);\r\n" + 
		"		}\r\n" + 
		"		catch (Exception e)\r\n" + 
		"		{\r\n" + 
		"		    e.printStackTrace();\r\n" + 
		"		}\r\n" + 
		"	    }// endmethod run\r\n" + 
		"	});\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    private void initialize()\r\n" + 
		"    {\r\n" + 
		"	frame = new JFrame();\r\n" + 
		"	frame.setBounds(100, 100, 450, 300);\r\n" + 
		"	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);\r\n" + 
		"\r\n" + 
		"	JButton btnNewButton = new JButton(\"New button\");\r\n" + 
		"	btnNewButton.addActionListener(new ActionListener()\r\n" + 
		"	{\r\n" + 
		"	    public void actionPerformed(ActionEvent arg0)\r\n" + 
		"	    {\r\n" + 
		"		System.out.println(\"Button pressed\");\r\n" + 
		"		PUMLgenerator.parser.parse(\"MySourceCode\");\r\n" + 
		"		// System.out.println(PUMLgenerator.outputPUML.getPUML());\r\n" + 
		"	    }\r\n" + 
		"	});\r\n" + 
		"	frame.getContentPane().add(btnNewButton, BorderLayout.CENTER);\r\n" + 
		"    }\r\n" + 
		"}\r\n" + 
		"import java.io.BufferedWriter;\r\n" + 
		"import java.io.File;\r\n" + 
		"import java.io.FileWriter;\r\n" + 
		"import java.io.IOException;\r\n" + 
		"import java.util.List;\r\n" + 
		"\r\n" + 
		"import net.sourceforge.plantuml.GeneratedImage;\r\n" + 
		"import net.sourceforge.plantuml.SourceFileReader;\r\n" + 
		"\r\n" + 
		"//import ClassConnection.connectionType;\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"public class OutputPUML\r\n" + 
		"{\r\n" + 
		"\r\n" + 
		"    public OutputPUML()\r\n" + 
		"    {\r\n" + 
		"    };\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    public String getPUML(ParsingResult myParsingResult) // TODO eventuell ueberfluessig? http://plantuml.com/api\r\n" + 
		"							 // ->Hilfe\r\n" + 
		"    {\r\n" + 
		"	String pumlCode = \"\";\r\n" + 
		"	int from;\r\n" + 
		"	int to;\r\n" + 
		"	pumlCode += \"@startuml%\";\r\n" + 
		"	for (int i = 0; i < myParsingResult.classes.size(); i++)\r\n" + 
		"	{\r\n" + 
		"	    pumlCode += \"class \";\r\n" + 
		"	    pumlCode += myParsingResult.classes.get(i);\r\n" + 
		"	    pumlCode += \"%\";\r\n" + 
		"	}\r\n" + 
		"	for (int i = 0; i < myParsingResult.classConnections.size(); i++)\r\n" + 
		"	{\r\n" + 
		"	    from = myParsingResult.classConnections.get(i).getFrom();\r\n" + 
		"	    to = myParsingResult.classConnections.get(i).getTo();\r\n" + 
		"	    pumlCode += myParsingResult.classes.get(from);\r\n" + 
		"	    if (myParsingResult.classConnections.get(i).getConnection() == ClassConnection.connectionType.extension)\r\n" + 
		"	    {\r\n" + 
		"		pumlCode += \" -- \"; // TODO eventuell Pfeile\r\n" + 
		"	    }\r\n" + 
		"	    else if (myParsingResult.classConnections.get(i)\r\n" + 
		"		    .getConnection() == ClassConnection.connectionType.aggregation)\r\n" + 
		"	    {\r\n" + 
		"		pumlCode += \" o-- \"; // TODO eventuell Richtung aendern\r\n" + 
		"	    }\r\n" + 
		"	    else\r\n" + 
		"	    {\r\n" + 
		"		pumlCode += \" *-- \"; // TODO eventuell Richtung aendern\r\n" + 
		"	    }\r\n" + 
		"	    pumlCode += myParsingResult.classes.get(to);\r\n" + 
		"	    pumlCode += \"%\";\r\n" + 
		"	}\r\n" + 
		"	pumlCode += \"@enduml\";\r\n" + 
		"\r\n" + 
		"	return pumlCode;\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    public void savePUMLtoFile(ParsingResult myParsingResult, String filePath)\r\n" + 
		"    {\r\n" + 
		"	int from;\r\n" + 
		"	int to;\r\n" + 
		"	try\r\n" + 
		"	{\r\n" + 
		"	    BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filePath)));\r\n" + 
		"	    bw.write(\"@startuml\");\r\n" + 
		"	    bw.newLine();\r\n" + 
		"	    for (int i = 0; i < myParsingResult.classes.size(); i++)\r\n" + 
		"	    {\r\n" + 
		"		bw.write(\"class \");\r\n" + 
		"		bw.write(myParsingResult.classes.get(i));\r\n" + 
		"		bw.newLine();\r\n" + 
		"	    }\r\n" + 
		"	    for (int i = 0; i < myParsingResult.classConnections.size(); i++)\r\n" + 
		"	    {\r\n" + 
		"		from = myParsingResult.classConnections.get(i).getFrom();\r\n" + 
		"		to = myParsingResult.classConnections.get(i).getTo();\r\n" + 
		"		bw.write(myParsingResult.classes.get(from));\r\n" + 
		"		if (myParsingResult.classConnections.get(i).getConnection() == ClassConnection.connectionType.extension)\r\n" + 
		"		{\r\n" + 
		"		    bw.write(\" -- \"); // TODO eventuell Pfeile\r\n" + 
		"		}\r\n" + 
		"		else if (myParsingResult.classConnections.get(i)\r\n" + 
		"			.getConnection() == ClassConnection.connectionType.aggregation)\r\n" + 
		"		{\r\n" + 
		"		    bw.write(\" o-- \"); // TODO eventuell Richtung aendern\r\n" + 
		"		}\r\n" + 
		"		else // composition\r\n" + 
		"		{\r\n" + 
		"		    bw.write(\" *-- \"); // TODO eventuell Richtung aendern\r\n" + 
		"		}\r\n" + 
		"		bw.write(myParsingResult.classes.get(to));\r\n" + 
		"		bw.newLine();\r\n" + 
		"	    }\r\n" + 
		"	    bw.write(\"@enduml\");\r\n" + 
		"	    bw.flush();\r\n" + 
		"	    bw.close();\r\n" + 
		"	}\r\n" + 
		"	catch (Exception e)\r\n" + 
		"	{\r\n" + 
		"	    e.printStackTrace(); // is geil aber nur fuer DEBUGEN\r\n" + 
		"	}\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    public void createPlantUML(String filePath, String pumlCode) throws IOException\r\n" + 
		"    {\r\n" + 
		"	File source = new File(filePath);\r\n" + 
		"	SourceFileReader reader = new SourceFileReader(source);\r\n" + 
		"	List<GeneratedImage> list = reader.getGeneratedImages();\r\n" + 
		"\r\n" + 
		"	File png = list.get(0).getPngFile();\r\n" + 
		"    }\r\n" + 
		"}\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"public interface ParserIf\r\n" + 
		"{\r\n" + 
		"\r\n" + 
		"    public void parse(String sourceCode);\r\n" + 
		"    \r\n" + 
		"    public ParsingResult getParsingResult();\r\n" + 
		"\r\n" + 
		"}\r\n" + 
		"\r\n" + 
		"import java.util.ArrayList;\r\n" + 
		"import java.util.Vector;\r\n" + 
		"import java.util.regex.*;\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"public class ParserJava implements ParserIf\r\n" + 
		"{\r\n" + 
		"\r\n" + 
		"    private ParsingResult result = null;\r\n" + 
		"    private Vector<String> className = new Vector<String>();\r\n" + 
		"    private Vector<String> classConnection = new Vector<String>();\r\n" + 
		"    private Vector<String> interfaceName = new Vector<String>();\r\n" + 
		"    private Vector<String> interfaceConnection = new Vector<String>();\r\n" + 
		"    private ArrayList<String> bothName = new ArrayList<String>();\r\n" + 
		"    private ArrayList<ClassConnection> classConnectionArray = new ArrayList<ClassConnection>();\r\n" + 
		"    private ClassConnection.connectionType extensionType = ClassConnection.connectionType.extension;\r\n" + 
		"    private ClassConnection.connectionType aggregationType = ClassConnection.connectionType.aggregation;\r\n" + 
		"    private ClassConnection.connectionType compositionType = ClassConnection.connectionType.composition;\r\n" + 
		"    private ArrayList<Integer> classPos = new ArrayList<Integer>();\r\n" + 
		"\r\n" + 
		"    public ParserJava()\r\n" + 
		"    {\r\n" + 
		"\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    private String getCommentlessSourceCode(String sourcec) // Entfernt Kommentare eines Strings\r\n" + 
		"    {\r\n" + 
		"	sourcec = sourcec.replaceAll(\"(?:/\\\\*(?:[^*]|(?:\\\\*+[^*/]))*\\\\*+/)|(?://.*)\", \"\");\r\n" + 
		"	System.out.println(\"!!! Commentless Sourcecode:\\n\" + sourcec + \"\\n!!!\");\r\n" + 
		"	return sourcec;\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"    private void analyzeClassBody(String substring, String parentClassName)\r\n" + 
		"    {\r\n" + 
		"	System.out.println(\"$$$ substring:\\n\" + substring + \"\\n$$$\");\r\n" + 
		"\r\n" + 
		"	ArrayList<Integer> lCurlyPos = new ArrayList<Integer>();\r\n" + 
		"	ArrayList<Integer> rCurlyPos = new ArrayList<Integer>();\r\n" + 
		"\r\n" + 
		"	Matcher lCurly = Pattern.compile(\"(?=(\\\\{))\").matcher(substring);\r\n" + 
		"	while (lCurly.find())\r\n" + 
		"	{\r\n" + 
		"	    lCurlyPos.add(lCurly.start());\r\n" + 
		"	}\r\n" + 
		"\r\n" + 
		"	Matcher rCurly = Pattern.compile(\"(?=(\\\\}))\").matcher(substring);\r\n" + 
		"	while (rCurly.find())\r\n" + 
		"	{\r\n" + 
		"	    rCurlyPos.add(rCurly.start());\r\n" + 
		"	}\r\n" + 
		"\r\n" + 
		"	for (Integer integer : lCurlyPos)\r\n" + 
		"	{\r\n" + 
		"	    System.out.println(\"{ Position: \" + integer);\r\n" + 
		"	}\r\n" + 
		"	for (Integer integer : rCurlyPos)\r\n" + 
		"	{\r\n" + 
		"	    System.out.println(\"} Position: \" + integer);\r\n" + 
		"	}\r\n" + 
		"	String classBody = \"\";\r\n" + 
		"	if (lCurlyPos.size() < rCurlyPos.size())\r\n" + 
		"	{\r\n" + 
		"	    System.out.println(\"Betrachtete Klasse liegt innerhalb einer anderen Klasse\");\r\n" + 
		"	    classBody = substring.substring(lCurlyPos.get(0) + 1, rCurlyPos.get(lCurlyPos.size() - 1));\r\n" + 
		"	}\r\n" + 
		"	if (lCurlyPos.size() == rCurlyPos.size())\r\n" + 
		"	{\r\n" + 
		"	    classBody = substring.substring(lCurlyPos.get(0) + 1, rCurlyPos.get(lCurlyPos.size() - 1));\r\n" + 
		"	}\r\n" + 
		"	if (lCurlyPos.size() > rCurlyPos.size())\r\n" + 
		"	{\r\n" + 
		"	    System.out.println(\"Betrachtete Klasse hat offenes Ende (fehlende })\");\r\n" + 
		"	    classBody = substring.substring(lCurlyPos.get(0) + 1);\r\n" + 
		"	}\r\n" + 
		"\r\n" + 
		"	System.out.println(\"@@@ Classbody:\\n\" + classBody + \"\\n@@@\");\r\n" + 
		"\r\n" + 
		"	int parentClassNo = bothName.indexOf(parentClassName); // Klassennummer des Körpers\r\n" + 
		"	String parentClass; // Klassenname des Körpers\r\n" + 
		"\r\n" + 
		"	System.out.println(\"Klassenname:\" + parentClassName);\r\n" + 
		"	for (String classname : className)\r\n" + 
		"	{\r\n" + 
		"	    ArrayList<Integer> newClassPos = new ArrayList<>();\r\n" + 
		"	    ArrayList<Integer> constructorPos = new ArrayList<>();\r\n" + 
		"	    ArrayList<Integer> constructorWithNewClassPos = new ArrayList<>();\r\n" + 
		"	    ArrayList<Integer> methodsWithNewClassPos = new ArrayList<>();\r\n" + 
		"\r\n" + 
		"	    if (classBody.contains(classname) && classname != parentClassName)\r\n" + 
		"	    {\r\n" + 
		"		System.out.println(\"enthält Klasse \" + classname);\r\n" + 
		"		Matcher match = Pattern.compile(\"new +\" + classname + \" *\\\\(.*\\\\)\").matcher(classBody);\r\n" + 
		"		while (match.find())\r\n" + 
		"		{\r\n" + 
		"		    newClassPos.add(match.start());\r\n" + 
		"		    System.out.println(\"enthält Klasse \" + classname + \" als neuen Aufruf an Stelle \" + match.start());\r\n" + 
		"		}\r\n" + 
		"		Matcher match2 = Pattern\r\n" + 
		"			.compile(parentClassName + \" *\\\\(.*\\\\) *\\\\{.*new +\" + classname + \" *\\\\(.*\\\\).*\\\\} *;\")\r\n" + 
		"			.matcher(classBody);\r\n" + 
		"		while (match2.find())\r\n" + 
		"		{\r\n" + 
		"		    constructorWithNewClassPos.add(match2.start());\r\n" + 
		"		}\r\n" + 
		"		Matcher match3 = Pattern.compile(parentClassName + \" *\\\\(.*\\\\) *\\\\{.*\\\\} *;\").matcher(classBody);\r\n" + 
		"		while (match3.find())\r\n" + 
		"		{\r\n" + 
		"		    constructorPos.add(match3.start());\r\n" + 
		"		}\r\n" + 
		"		Matcher match4 = Pattern.compile(\"\\\\{.*new +\" + classname + \" *\\\\(.*\\\\).*\\\\}\").matcher(classBody);\r\n" + 
		"		while (match4.find())\r\n" + 
		"		{\r\n" + 
		"		    methodsWithNewClassPos.add(match4.start());\r\n" + 
		"		}\r\n" + 
		"		if (!newClassPos.isEmpty())\r\n" + 
		"		{\r\n" + 
		"		    if (constructorPos.size() == constructorWithNewClassPos.size()\r\n" + 
		"			    || newClassPos.size() > methodsWithNewClassPos.size())\r\n" + 
		"		    {\r\n" + 
		"			System.out.println(\"enthält Klasse \" + classname + \" als Komposition\");\r\n" + 
		"			ClassConnection cc = new ClassConnection(className.indexOf(classname), parentClassNo,\r\n" + 
		"				compositionType);\r\n" + 
		"			classConnectionArray.add(cc);\r\n" + 
		"		    }\r\n" + 
		"		    else\r\n" + 
		"		    {\r\n" + 
		"			System.out.println(\"enthält Klasse \" + classname + \" als Agregation\");\r\n" + 
		"			ClassConnection cc = new ClassConnection(className.indexOf(classname), parentClassNo,\r\n" + 
		"				aggregationType);\r\n" + 
		"			classConnectionArray.add(cc);\r\n" + 
		"		    }\r\n" + 
		"\r\n" + 
		"		}\r\n" + 
		"	    }\r\n" + 
		"	}\r\n" + 
		"\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"    public void parse(String sourceCode)\r\n" + 
		"    {\r\n" + 
		"\r\n" + 
		"	Matcher interfaceMatcher = Pattern.compile(\"interface +([a-zA-Z0-9]+).*\\\\R* *\\\\{\").matcher(sourceCode);\r\n" + 
		"	System.out.println(\"Interfaces: \");\r\n" + 
		"\r\n" + 
		"	while (interfaceMatcher.find())\r\n" + 
		"	{\r\n" + 
		"	    interfaceName.add(interfaceMatcher.group(1));\r\n" + 
		"	    bothName.add(interfaceMatcher.group(1)); \r\n" + 
		"	    interfaceConnection.add(interfaceMatcher.group()); \r\n" + 
		"							       \r\n" + 
		"	    classPos.add(interfaceMatcher.start());\r\n" + 
		"	    System.out.println(interfaceMatcher.group(1));\r\n" + 
		"	}\r\n" + 
		"	System.out.println(\"Anzahl \" + interfaceName.size());\r\n" + 
		"	int countIN = interfaceName.size(); \r\n" + 
		"\r\n" + 
		"	for (int i = 0; i < interfaceConnection.size(); i++) \r\n" + 
		"							    \r\n" + 
		"	{\r\n" + 
		"	    if (interfaceConnection.get(i).indexOf(\"extends\") > 1)\r\n" + 
		"	    {\r\n" + 
		"		exInterfaceConnection(i);\r\n" + 
		"					  \r\n" + 
		"	    }\r\n" + 
		"	}\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"	Matcher classMatcher = Pattern.compile(\"class +([a-zA-Z0-9]+).*\\\\R* *\\\\{\").matcher(sourceCode);\r\n" + 
		"	System.out.println(\"Klassen: \");\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"	while (classMatcher.find())\r\n" + 
		"	{\r\n" + 
		"	    className.add(classMatcher.group(1));\r\n" + 
		"	    bothName.add(classMatcher.group(1));\r\n" + 
		"	    classConnection.add(classMatcher.group());\r\n" + 
		"	    classPos.add(classMatcher.start());\r\n" + 
		"	    System.out.println(classMatcher.group(1));\r\n" + 
		"\r\n" + 
		"	}\r\n" + 
		"	System.out.println(\"Anzahl \" + className.size());\r\n" + 
		"\r\n" + 
		"	for (String classname : bothName)\r\n" + 
		"	{\r\n" + 
		"	    System.out.println(\"analyzeClassBody() von Klasse \" + classname + \": \");\r\n" + 
		"	    analyzeClassBody(sourceCode.substring(classPos.get(bothName.indexOf(classname))), classname);\r\n" + 
		"	}\r\n" + 
		"\r\n" + 
		"	\r\n" + 
		"	for (int i = 0; i < classConnection.size(); i++)\r\n" + 
		"	{\r\n" + 
		"	    \r\n" + 
		"	    if (classConnection.get(i).indexOf(\"implements\") > 1)\r\n" + 
		"	    {\r\n" + 
		"\r\n" + 
		"		if (classConnection.get(i).indexOf(\"extends\") > 1)\r\n" + 
		"		{\r\n" + 
		"		    impClassConnection(i, countIN);\r\n" + 
		"		    exClassConnection(i, countIN);\r\n" + 
		"		    System.out.println(\"Klasse \" + (i + countIN) + \" implementiert und erbt\");\r\n" + 
		"		}\r\n" + 
		"		else\r\n" + 
		"		{\r\n" + 
		"		    impClassConnection(i, countIN);\r\n" + 
		"		    System.out.println(\"Bin hier\");\r\n" + 
		"		}\r\n" + 
		"\r\n" + 
		"	    }\r\n" + 
		"	    \r\n" + 
		"	    else if (classConnection.get(i).indexOf(\"extends\") > 1)\r\n" + 
		"	    {\r\n" + 
		"		exClassConnection(i, countIN);\r\n" + 
		"	    }\r\n" + 
		"	    \r\n" + 
		"	    else\r\n" + 
		"	    {\r\n" + 
		"		System.out.println(\"Klasse \" + (i + countIN) + \" implementiert nicht und erbt nicht \");\r\n" + 
		"	    }\r\n" + 
		"	}\r\n" + 
		"	\r\n" + 
		"	for (int i = 0; i < classConnectionArray.size(); i++)\r\n" + 
		"	{\r\n" + 
		"	    System.out.println(\"Verbindung \" + i + \" : von \" + classConnectionArray.get(i).getFrom() + \" nach \"\r\n" + 
		"		    + classConnectionArray.get(i).getTo() + \" .\");\r\n" + 
		"	}\r\n" + 
		"\r\n" + 
		"	\r\n" + 
		"	result = new ParsingResult(bothName, classConnectionArray);\r\n" + 
		"\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    private int bothNumber(String parent)\r\n" + 
		"    {\r\n" + 
		"	int classNu = -1; // Bei nicht vorhandenem Knoten kommt -1 zurück\r\n" + 
		"	for (int i = 0; i < bothName.size(); i++)\r\n" + 
		"	{\r\n" + 
		"	    if (bothName.get(i).equals(parent))\r\n" + 
		"	    {\r\n" + 
		"		classNu = i;\r\n" + 
		"	    }\r\n" + 
		"	}\r\n" + 
		"	return classNu;\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"    private void exClassConnection(int i, int countIN)\r\n" + 
		"    {\r\n" + 
		"	String parent = \"keinem\";\r\n" + 
		"	ClassConnection cc;\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"	Matcher mEx = Pattern.compile(\"extends +([a-zA-Z0-9]*).*\\\\{\").matcher(classConnection.get(i));\r\n" + 
		"	while (mEx.find())\r\n" + 
		"	{\r\n" + 
		"	    parent = mEx.group(1);\r\n" + 
		"	    System.out.println(bothNumber(parent));\r\n" + 
		"	}\r\n" + 
		"	System.out.println(\"Klasse \" + bothName.get(i + countIN) + \" erbt von \" + bothName.get(bothNumber(parent)));\r\n" + 
		"\r\n" + 
		"	cc = new ClassConnection((i + countIN), bothNumber(parent), extensionType);\r\n" + 
		"	classConnectionArray.add(cc);\r\n" + 
		"\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    private void impClassConnection(int i, int countIN)\r\n" + 
		"    {\r\n" + 
		"\r\n" + 
		"	String interface1 = \"keinem\";\r\n" + 
		"	String followingInterfaces = \"keinem\";\r\n" + 
		"	ClassConnection cc;\r\n" + 
		"	\r\n" + 
		"	Matcher mImp1 = Pattern.compile(\"implements +([a-zA-Z0-9]*) *( *, *[a-zA-Z0-9]*)* *.*\\\\{\")\r\n" + 
		"		.matcher(classConnection.get(i));\r\n" + 
		"	if (mImp1.find())\r\n" + 
		"	{\r\n" + 
		"	    interface1 = mImp1.group(1); \r\n" + 
		"	    \r\n" + 
		"	    cc = new ClassConnection((i + countIN), bothNumber(interface1), extensionType);\r\n" + 
		"	    classConnectionArray.add(cc);\r\n" + 
		"\r\n" + 
		"	   \r\n" + 
		"	    if (mImp1.group(2) != null)\r\n" + 
		"	    {\r\n" + 
		"		followingInterfaces = mImp1.group(2);\r\n" + 
		"	    }\r\n" + 
		"\r\n" + 
		"	    System.out.println(interface1 + \" ist Interface von \" + className.get(i));\r\n" + 
		"\r\n" + 
		"	    Matcher mImp2 = Pattern.compile(\" *, *([a-zA-Z0-9]*)\").matcher(followingInterfaces);\r\n" + 
		"	    while (mImp2.find())\r\n" + 
		"	    {\r\n" + 
		"		interface1 = mImp2.group(1);\r\n" + 
		"		System.out.println(interface1 + \" ist Interface von \" + className.get(i));\r\n" + 
		"		\r\n" + 
		"		cc = new ClassConnection((i + countIN), bothNumber(interface1), extensionType);\r\n" + 
		"		classConnectionArray.add(cc);\r\n" + 
		"		System.out.println(\"G \" + classConnectionArray.size());\r\n" + 
		"	    }\r\n" + 
		"	    System.out.println(\"test\");\r\n" + 
		"	}\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    private void exInterfaceConnection(int i)\r\n" + 
		"    {\r\n" + 
		"	String interface1 = \"keinem\";\r\n" + 
		"	String followingInterfaces = \"keins\";\r\n" + 
		"	ClassConnection cc;\r\n" + 
		"	\r\n" + 
		"	Matcher mEx1 = Pattern.compile(\"extends +([a-zA-Z0-9]*) *( *, *[a-zA-Z0-9]*)* *.*\\\\{\")\r\n" + 
		"		.matcher(interfaceConnection.get(i));\r\n" + 
		"	if (mEx1.find())\r\n" + 
		"	{\r\n" + 
		"	    interface1 = mEx1.group(1); \r\n" + 
		"	    System.out.println(\"jap\");\r\n" + 
		"	    \r\n" + 
		"	    cc = new ClassConnection((i), bothNumber(interface1), extensionType);\r\n" + 
		"	    classConnectionArray.add(cc);\r\n" + 
		"\r\n" + 
		"	    \r\n" + 
		"	    if (mEx1.group(2) != null)\r\n" + 
		"	    {\r\n" + 
		"		followingInterfaces = mEx1.group(2);\r\n" + 
		"	    }\r\n" + 
		"\r\n" + 
		"	    System.out.println(interface1 + \" ist Interface von \" + bothName.get(i));\r\n" + 
		"\r\n" + 
		"	    Matcher mEx2 = Pattern.compile(\" *, *([a-zA-Z0-9]*)\").matcher(followingInterfaces);\r\n" + 
		"	    while (mEx2.find())\r\n" + 
		"	    {\r\n" + 
		"		interface1 = mEx2.group(1);\r\n" + 
		"		System.out.println(interface1 + \" ist Interface von \" + bothName.get(i));\r\n" + 
		"		\r\n" + 
		"		cc = new ClassConnection((i), bothNumber(interface1), extensionType);\r\n" + 
		"		classConnectionArray.add(cc);\r\n" + 
		"	    }\r\n" + 
		"	    System.out.println(\"test\");\r\n" + 
		"	}\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    public ParsingResult getParsingResult()\r\n" + 
		"    {\r\n" + 
		"	return result;\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"}\r\n" + 
		"\r\n" + 
		"import java.util.*;\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"public class ParsingResult\r\n" + 
		"{\r\n" + 
		"\r\n" + 
		"    public ArrayList<String> classes;\r\n" + 
		"    public ArrayList<ClassConnection> classConnections;\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"    public ParsingResult(ArrayList<String> classes, ArrayList<ClassConnection> classConnections)\r\n" + 
		"    {\r\n" + 
		"	this.classes = classes;\r\n" + 
		"	this.classConnections = classConnections;\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"    public ArrayList<String> getClasses()\r\n" + 
		"    {\r\n" + 
		"        return classes;\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"    public void setClasses(ArrayList<String> classes)\r\n" + 
		"    {\r\n" + 
		"        this.classes = classes;\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"    public ArrayList<ClassConnection> getClassConnections()\r\n" + 
		"    {\r\n" + 
		"        return classConnections;\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"    public void setClassConnections(ArrayList<ClassConnection> classConnections)\r\n" + 
		"    {\r\n" + 
		"        this.classConnections = classConnections;\r\n" + 
		"    };\r\n" + 
		"    \r\n" + 
		"    \r\n" + 
		"\r\n" + 
		"}\r\n" + 
		"import org.apache.commons.cli.ParseException;\r\n" + 
		"\r\n" + 
		"import gui.GUI_SWT;\r\n" + 
		"\r\n" + 
		"public class PUMLgenerator\r\n" + 
		"{\r\n" + 
		"\r\n" + 
		"    static GUI myGUI = null;\r\n" + 
		"    static GUI_SWT myGUI_SWT = null;\r\n" + 
		"    static Console myConsole = null;\r\n" + 
		"    static CodeCollector codeCollector = new CodeCollector();\r\n" + 
		"    static ParserIf parser = new ParserJava();\r\n" + 
		"    static OutputPUML outputPUML = new OutputPUML();\r\n" + 
		"  \r\n" + 
		"\r\n" + 
		"    public static void main(String[] args) throws ParseException\r\n" + 
		"    {\r\n" + 
		"	\r\n" + 
		"	boolean useGUI = true;\r\n" + 
		"	if (args.length > 0)\r\n" + 
		"	   {\r\n" + 
		"		useGUI = false;\r\n" + 
		"	   }\r\n" + 
		"\r\n" + 
		"	if (!useGUI)\r\n" + 
		"	{\r\n" + 
		"	    myConsole = new Console();\r\n" + 
		"	    myConsole.showConsole(args);\r\n" + 
		"	}\r\n" + 
		"	else\r\n" + 
		"	{\r\n" + 
		"	   \r\n" + 
		"		myGUI_SWT = new GUI_SWT();\r\n" + 
		"		myGUI_SWT.open();\r\n" + 
		"	}\r\n" + 
		"    }\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"}");
    }

}
