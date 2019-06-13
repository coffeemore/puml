
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author Klasse für die Konsolenanwendung
 */
public class Console extends PUMLgenerator
{

    /**
     * Konstruktor
     */
    public Console()
    {
	// System.out.println("Aufruf Console() wird erstmal nicht genutzt.");

    }
    /**
     * Startet die Abarbeitung der Konsolenanwendung und ggf. Dialoge
     * 
     * @param Eingabeparameter der Kommandozeile: String[] args
     * @throws ParseException
     */
    void showConsole(String[] args) throws ParseException // Konsolenanwendung implementiert
    {

	// hier wird ein neuer Container für Optionen angelegt
	Options options = new Options();
	options.addOption("c", false, "Konsole wird aufgerufen");

	// ignorieren verschiedener Dateitypen
	options.addOption("ijar", false, "Dateien mit der Endung .jar werden ignoriert.");

	options.addOption("ijava", false, "Dateien mit der Endung .java werden ignoriert.");
	
	//Erstelle Klassendiagramm
	options.addOption("cc",false, "Erzeugt ein Klassendiagramm.");
		
	//Alles auflisten
	options.addOption("s",false, "Listet alle Klassen und Methoden auf.");
		
	//Alles auflisten
	options.addOption("int",false, "Startet interaktiven Modus.");

	// Angabe fuer den Ausgabepfad
	Option output = Option.builder() 
		.longOpt("o").argName("filepath").hasArg().desc("Angabe des Pfades fuer den Zielordner.").build();
	options.addOption(output);
	
	//Erstelle SeqenceDiagramm
	Option seqDiag = Option.builder()
			.longOpt("cs").argName("Klasse, Methode").hasArgs().type(Integer.class).valueSeparator(',').numberOfArgs(2).desc("Erzeugt ein Sequenzdiagramm.").build();
	options.addOption(seqDiag);
	
	// Angabe der zu verarbeitenden Dateien
	Option input = Option.builder() 
		.longOpt("i").argName("filepath").hasArg().valueSeparator(';').numberOfArgs(Option.UNLIMITED_VALUES)
		.desc("Angabe der zu verarbeitenden Pfade, durch ; getrennt").build();
	options.addOption(input);

	// Parser
	CommandLineParser commandParser = new DefaultParser();

	try
	{
	    CommandLine cmd = commandParser.parse(options, args);

	    // Argumentauswertungen und Ausfuehrungen
	    if (cmd.hasOption("c")) // Anleitung ausgeben
	    {
	    	System.out.println("Consolemode");
	    }
	    if (cmd.hasOption("ijar")) // ignore jar files
	    {
	    	codeCollector.setUseJarFiles(false);
	    }
	    if (cmd.hasOption("ijava")) // ignore java files
	    {
	    	codeCollector.setUseJavaFiles(false);
	    }
	    if (cmd.hasOption("i")) // Verarbeitung vieler zu verarbeitenden Pfade
	    {
		    for (int i = 0; i < cmd.getOptionValues("i").length; i++) // Pfade in Collector Liste schreiben
		    {
		    	codeCollector.paths.add(cmd.getOptionValues("i")[i]);
		    	System.out.println(codeCollector.paths.get(i));
		    }
			PUMLgenerator.parser.parse(codeCollector.getSourceCode().get(0)); // Parser berbeitet Daten welche ihm übergeben werden 
			if (cmd.hasOption("s"))
			{
				showAllClassesMethods();
			}
			if (cmd.hasOption("o")) // Pruefe ob Zielordner gegeben
			{
				if (cmd.hasOption("int"))
				{
					System.out.println("Interaktiver Modus");
					interactiveMode(true);
				}
				else
				{
					if (cmd.hasOption("cc")) //Gewuenschtes Diagramm
					{
						//createClassDiagram(cmd, true);
						createClassDiag(cmd,false,true,"");
					}
					if (cmd.hasOption("cs"))
					{
						int entryClass = Integer.parseInt(cmd.getOptionValues("cs")[0]);
			    		int entryMethode = Integer.parseInt(cmd.getOptionValues("cs")[1]);
						createInteractiveSQDiagram(cmd,entryClass,entryMethode,true,cmd.getOptionValue("o"),true);
					}
				}
			}
			else 
			{
				if (cmd.hasOption("int"))
				{
					System.out.println("Interaktiver Modus");
					interactiveMode(false);
				}
				if (cmd.hasOption("cc")) //Gewuenschtes Diagramm
				{
					//createClassDiagram(cmd, false);
					createClassDiag(cmd,false,false,"./outPUML_Code_defaultlocation");
				}
				if (cmd.hasOption("cs"))
				{
					int entryClass = Integer.parseInt(cmd.getOptionValues("cs")[0]);
		    		int entryMethode = Integer.parseInt(cmd.getOptionValues("cs")[1]);
					createInteractiveSQDiagram(cmd,entryClass,entryMethode,false,"",false);
				}
			}
		}
	    else if (!cmd.hasOption("i"))
	    {
		System.out.println("Es fehlt ein zu bearbeitender Pfad.");
	    }
	}
	catch (UnrecognizedOptionException uoe) // Falls Parameter unbekannt, Hilfe ausgeben
	{
	    System.out.println("Paramter: " + uoe.getOption() + " unknown.");
	    HelpFormatter formatter = new HelpFormatter();
	    formatter.printHelp("PUML", options);
	}

    }
    /*
     * Erstellt den Quellcode fuer ein Klassendiagramm und erzeugt das Textfile,
     * entweder im aktuellen Arbeitsverzeichnis oder unter spezifiziertem Pfad
     * @param cmd Uebergabe des Kommandos
     * @param outPath Ausgabeort festgelegt ja/nein
     * 
    private void createClassDiagram(CommandLine cmd, Boolean outPath)
    {
    	if (!outPath) //Falls kein Ausgabeordner definiert, in Arbeitsverzeichnis schreiben
    	{
    		try
		    {
    			//PUML code erstellen
    			outputPUML.savePUMLtoFile(outputPUML.getPUML(classDiagramGenerator.createDiagram(parser.getParsingResult())),"./outPUML_Code_defaultlocation");
    			//Diagramm erzeugen
    			outputPUML.createPUMLfromFile("./outPUML_Code_defaultlocation", "./outPUML_Graph_defaultlocation");
    			/*
    			outputPUML.savePUMLtoFile(outputPUML.getPUML(parser.getParsingResult()),
				"./outPUML_Code_defaultlocation"); // Code erzeugen Funktion

    			outputPUML.createPUMLfromString("./outPUML_Graph_defaultlocation",
				outputPUML.getPUML(parser.getParsingResult())); // Einbinden der Diagrammfunktion
				*/ 
    		/*
		    }
		    catch (IOException | XPathExpressionException e)
		    {
		    	System.out.println("Kommandozeile: Verarbeitung ohne output-Pfad fehlgeschlagen");
		    	e.printStackTrace();
		    }
    	}
    	else // Sonst in Verzeichniss schreiben
    	{
    		try
		    {

    			//PUML code erstellen
    			outputPUML.savePUMLtoFile(outputPUML.getPUML(classDiagramGenerator.createDiagram(parser.getParsingResult())),cmd.getOptionValue("o") + "/outPUML_Code");
    			//Diagramm erzeugen
    			outputPUML.createPUMLfromFile(cmd.getOptionValue("o") + "/outPUML_Code", cmd.getOptionValue("o") + "outPUML_Graph");
    			//outputPUML.savePUMLtoFile(outputPUML.getPUML(parser.getParsingResult()),cmd.getOptionValue("o") + "outPUML_Code"); // Einbinden der neuen Code Funktion

    			//outputPUML.createPUMLfromString(cmd.getOptionValue("o") + "outPUML_Graph",outputPUML.getPUML(parser.getParsingResult())); // Einbinden der Diagrammfunktion
		    }
		    catch (IOException | XPathExpressionException e)
		    {
		    	System.out.println("Kommandozeile: Verarbeitung mit output-Pfad fehlgeschlagen");
		    	e.printStackTrace();
		    }
		    	System.out.println(
			    "Zielordner:" + cmd.getOptionValue("o") + "\nQuelle: " + codeCollector.getSourceCode());
    	}
    }*/
    
    /*
     * Erstellt den Quellcode fuer ein Klassendiagramm und erzeugt das Textfile,
     * entweder im aktuellen Arbeitsverzeichnis oder unter spezifiziertem Pfad
     * @param cmd Uebergabe des Kommandos
     * @param outPath Ausgabeort festgelegt ja/nein
     * 
    private void createSQDiagram(CommandLine cmd, Boolean outpath)
    {
    	if (cmd.getOptionValues("cs")[0].isEmpty() || cmd.getOptionValues("cs")[1].isEmpty())
    	{
    		System.out.println("Keine Klasse / Methode als Einstiegspunkt gewaehlt. <int, int>");
    	}
    	System.out.println("Klasse: " + cmd.getOptionValues("cs")[0] + " und Methode: " + cmd.getOptionValues("cs")[1]);
    
		try
		{
			//Doc Initialisierung und filtern der Elemente aus xml -> Klasse und Methode
	    	Document parserDoc = PUMLgenerator.parser.getParsingResult();
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("/parsed/*"); // Startpunkt parsed Knoten
			NodeList classNodeList = (NodeList) expr.evaluate(parserDoc, XPathConstants.NODESET);
			
			classNodeList = xmlHelper.getList(parserDoc, "/source/classdefinition/name");
			System.out.println("Anzahl Klassen: "+ classNodeList.getLength() + "\n");
			
			NodeList methodeNodeList = (NodeList) expr.evaluate(parserDoc, XPathConstants.NODESET);
			
			methodeNodeList = xmlHelper.getList(parserDoc, "/source/classdefinition/methoddefinition/name");
			System.out.println("Anzahl Methoden: "+ methodeNodeList.getLength() + "\n");
		
			//Funktionenaufruf
			int entryClass = Integer.parseInt(cmd.getOptionValues("cs")[0]);
    		int entryMethode = Integer.parseInt(cmd.getOptionValues("cs")[1]);
	    	if (!outpath) //Falls kein Ausgabeordner definiert, in Arbeitsverzeichnis schreiben
	    	{
	    		try
	    		{
	    			//Code Erzeugen
					outputPUML.savePUMLtoFile(outputPUML.getPUML(seqDiagramGenerator.createDiagram(parser.getParsingResult(), classNodeList.item(entryClass).getTextContent(), classNodeList.item(entryMethode).getTextContent())),"./outPUML_Code_defaultlocation");
					//Diagramm erzeugen
	    			outputPUML.createPUMLfromFile("./outPUML_Code_defaultlocation", "./outPUML_Graph_defaultlocation");
	    		}
	    		catch (XPathExpressionException | DOMException | IOException | ParserConfigurationException | SAXException e)
	    		{
	    			System.out.println("Kommandozeile: Verarbeitung SQ ohne output-Pfad fehlgeschlagen");
					e.printStackTrace();
				}
	    	}
	    	else
	    	{
	    		try
	    		{
	    			//Code Erzeugen
					outputPUML.savePUMLtoFile(outputPUML.getPUML(seqDiagramGenerator.createDiagram(parser.getParsingResult(), classNodeList.item(entryClass).getTextContent(), classNodeList.item(entryMethode).getTextContent())), cmd.getOptionValue("o") + "/outPUML_Code");
					//Diagramm erzeugen
					outputPUML.createPUMLfromFile(cmd.getOptionValue("o") + "/outPUML_Code", cmd.getOptionValue("o") + "outPUML_Graph");
				}
	    		catch (XPathExpressionException | DOMException | IOException | ParserConfigurationException | SAXException e)
	    		{
	    			System.out.println("Kommandozeile: Verarbeitung SQ ohne output-Pfad fehlgeschlagen");
					e.printStackTrace();
				}
	    	}
		}
		catch (XPathExpressionException e1)
		{
			e1.printStackTrace();
		}
    }
     */
    
    /*
     * Ausgabe aller Klassen und Methoden im Konsolendialog
     * 
     */
	private void showAllClassesMethods()
    {
		try
		{
			
			/*TODO*/
			//Document parserDoc = (); //Test
			Document parserDoc = PUMLgenerator.parser.getParsingResult();
			/*XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("/parsed/*"); // Startpunkt parsed Knoten
			NodeList classNodeList = (NodeList) expr.evaluate(parserDoc, XPathConstants.NODESET);
			*/
			//Initialisiere Nodelist fuer Klassennamen
			NodeList classNodeList = xmlHelper.getList(parserDoc, "/source/classdefinition/name");
			System.out.println("Anzahl Klassen: " + classNodeList.getLength() + "\n");
			//Ausgabe fuer jede Klasse
			for (int i = 0; i < classNodeList.getLength(); i++)
			{
				System.out.println("Klasse  "+ i + ": "+ classNodeList.item(i).getTextContent());
				//Initialisiere NodeList fuer methoden der Klasse
				NodeList methodeNodeList = xmlHelper.getList(classNodeList.item(i), "../methoddefinition/name");
				System.out.println("Anzahl Methoden: "+ methodeNodeList.getLength() + "\n");
				for (int j = 0; j <methodeNodeList.getLength(); j++)
				{
					System.out.println("Methode "+ j + ": " + methodeNodeList.item(j).getTextContent());
				}
			}
			/*
			System.out.println();
			NodeList methodeNodeList = (NodeList) expr.evaluate(parserDoc, XPathConstants.NODESET);
			methodeNodeList = xmlHelper.getList(parserDoc, "/source/classdefinition/methoddefinition/name");
			System.out.println("Anzahl Methoden: "+ methodeNodeList.getLength() + "\n");
			for (int i = 0; i < methodeNodeList.getLength(); i++)
			{
				System.out.println("Methode "+ i + ": " + methodeNodeList.item(i).getTextContent());
			}
			System.out.println();
			*/
		}
		catch (XPathExpressionException e)
		{
			e.printStackTrace();
		}
    }
	/**
	 * Interaktiv Dialog
	 * @param outPathGiven
	 */
	private void interactiveMode(Boolean outPathGiven)
	{
		Scanner scanner = new Scanner(System.in);
		String choice = new String();
		String outPath = new String();
		if (!outPathGiven) //Ohne ausgabepfad
		{
			while (!(choice.contains("s") || choice.contains("k")))
			{
				System.out.println("Erzeuge [s]equenzdiagramm oder [k]lassendiagramm");
				choice = scanner.next();
			}
			if (choice.contains("k")) //Klasse ohne Ausgabepfad
			{
				System.out.println(choice);
				while (choice.contains("k") || !(choice.contains("a") || choice.contains("p")))
				{
					System.out.println("Ausgabe in [a]rbeitsverzeichnis oder [p]fad angeben?");
					choice = scanner.next();
				}
				System.out.println("Ausgabepfad fuer UML-Diagramm und -Code angeben");
				outPath = scanner.next();
				//createClassDiagram(null,false);
				if (choice.contains("a"))
				{
					createClassDiag(null,true,false,"./outPUML_Code_defaultlocation");
				}
				else
				{
					createClassDiag(null,true,true,outPath);
				}
			}
			else //SQ
			{
				int entryClass = -1;
				int entryMethode = -1;
				while (!(entryClass > -1) || !(entryMethode > -1))
				{
					showAllClassesMethods();
					System.out.println("Waehle Klasse als Einstiegspunkt (Zahl >0) fuer SQDiagramm");
					entryClass = scanner.nextInt();
					System.out.println("Waehle Methode als Einstiegspunkt (Zahl >0) fuer SQDiagramm");
					entryMethode = scanner.nextInt();
				}
				System.out.println("Klasse: " + entryClass + " und Methode: " + entryMethode);
				createInteractiveSQDiagram(null, entryClass, entryMethode, true, "", false);
			}
		}
		else //mit ausgabepfad
		{
			while (!(choice.contains("s") || choice.contains("k")))
			{
				System.out.println("Erzeuge [s]equenzdiagramm oder [k]lassendiagramm");
				choice = scanner.next();
			}
			
			if (choice.contains("k"))
			{
				createClassDiag(null,true,true,outPath);
			}
			else //SQ
			{
				int entryClass = -1;
				int entryMethode = -1;
				while (!(entryClass > -1) || !(entryMethode > -1))
				{
					showAllClassesMethods();
					System.out.println("Waehle Klasse als Einstiegspunkt (Zahl >= 0) fuer SQDiagramm");
					entryClass = scanner.nextInt();
					System.out.println("Waehle Methode als Einstiegspunkt (Zahl >= 0) fuer SQDiagramm");
					entryMethode = scanner.nextInt();
				}
				System.out.println("Klasse: " + entryClass + " und Methode: " + entryMethode);
				createInteractiveSQDiagram(null, entryClass, entryMethode, true, outPath, true);
			}
		}
		scanner.close();
	}
	/**
	 * Methode zum Erzeugen eines Klassendiagramms aus Interactivem Modus und Cmd-Standard-Nutzung
	 * @param cmd Kommandozeilenparameter des Ausgabeortes 
	 * @param useString Schaltet den Uebergebenen String als Ausgabepfad bei "true" frei
	 * @param givenOutPath false, wenn kein Ausgabeort verfuegbar, true, wenn vorhanden
	 * @param outPath Ausgabeort als Zeichenkette
	 */
	private void createClassDiag(CommandLine cmd, Boolean useString ,Boolean givenOutPath, String outPath)
	{
		String actualOutputPath = new String();
		if (useString) //Wenn Aufruf ohne cmd 
		{
			actualOutputPath = outPath;
		}
		else // Sonst verwende Commando Parameter als Ausgabeort
		{
			actualOutputPath = cmd.getOptionValue("o");
		}
		if (!givenOutPath) //Falls kein Ausgabeordner definiert, in Arbeitsverzeichnis schreiben
    	{
    		try
		    {
    			//PUML code erstellen
    			outputPUML.savePUMLtoFile(outputPUML.getPUML(classDiagramGenerator.createDiagram(parser.getParsingResult())),"./outPUML_Code_defaultlocation");
    			//Diagramm erzeugen aus String
    			outputPUML.createPUMLfromString("./outPUML_Graph_defaultlocation", outputPUML.getPUML(classDiagramGenerator.createDiagram(parser.getParsingResult())));
		    }
		    catch (IOException | XPathExpressionException e)
		    {
		    	System.out.println("Kommandozeile: Verarbeitung ohne output-Pfad fehlgeschlagen");
		    	e.printStackTrace();
		    }
    	}
    	else // Sonst in Verzeichniss schreiben
    	{
    		try
		    {
    			//PUML code erstellen
    			outputPUML.savePUMLtoFile(outputPUML.getPUML(classDiagramGenerator.createDiagram(parser.getParsingResult())), actualOutputPath + "/outPUML_Code");
    			//Diagramm erzeugen aus String
    			outputPUML.createPUMLfromString(actualOutputPath + "/outPUML_Code", outputPUML.getPUML(classDiagramGenerator.createDiagram(parser.getParsingResult())));
		    }
		    catch (IOException | XPathExpressionException e)
		    {
		    	System.out.println("Kommandozeile: Verarbeitung mit output-Pfad fehlgeschlagen");
		    	e.printStackTrace();
		    }
		    	System.out.println(
			    "Zielordner:" + cmd.getOptionValue("o") + "\nQuelle: " + codeCollector.getSourceCode());
    	}
	}
	/**
	 * Interaktive Methode zum Erstellen des SQDiagramms
	 * @param cmd
	 * @param existingOutpath
	 */
	private void createInteractiveSQDiagram(CommandLine cmd,int entryClass, int entryMethode, Boolean useString, String outPath, Boolean existingOutpath)
    {
		String actualOutputPath = new String();
		if (useString) //Wenn Aufruf ohne cmd 
		{
			actualOutputPath = outPath;
		}
		else // Sonst verwende Commando Parameter als Ausgabeort
		{
			actualOutputPath = cmd.getOptionValue("o");
		}
		
		try
		{
			//Doc Initialisierung und filtern der Elemente aus xml -> Klasse und Methode
	    	Document parserDoc = PUMLgenerator.parser.getParsingResult();
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("/parsed/*"); // Startpunkt parsed Knoten
			NodeList classNodeList = (NodeList) expr.evaluate(parserDoc, XPathConstants.NODESET);
			
			classNodeList = xmlHelper.getList(parserDoc, "/source/classdefinition/name");
			System.out.println("Anzahl Klassen: "+ classNodeList.getLength() + "\n");
			
			NodeList methodeNodeList = (NodeList) expr.evaluate(parserDoc, XPathConstants.NODESET);
			
			methodeNodeList = xmlHelper.getList(parserDoc, "/source/classdefinition/methoddefinition/name");
			System.out.println("Anzahl Methoden: "+ methodeNodeList.getLength() + "\n");
			
	    	if (!existingOutpath) //Falls kein Ausgabeordner definiert, in Arbeitsverzeichnis schreiben
	    	{
	    		try
	    		{
	    			//Code Erzeugen
					outputPUML.savePUMLtoFile(outputPUML.getPUML(seqDiagramGenerator.createDiagram(parser.getParsingResult(), classNodeList.item(entryClass).getTextContent(), classNodeList.item(entryMethode).getTextContent())),"./outPUML_Code_defaultlocation");
					//Diagramm erzeugen
					outputPUML.createPUMLfromString("./outPUML_Graph_defaultlocation", outputPUML.getPUML(seqDiagramGenerator.createDiagram(parser.getParsingResult(), classNodeList.item(entryClass).getTextContent(), classNodeList.item(entryMethode).getTextContent())));
	    		}
	    		catch (XPathExpressionException | DOMException | IOException | ParserConfigurationException | SAXException e)
	    		{
	    			System.out.println("Kommandozeile: Verarbeitung SQ ohne output-Pfad fehlgeschlagen");
					e.printStackTrace();
				}
	    	}
	    	else
	    	{
	    		try
	    		{
	    			//Code Erzeugen
					outputPUML.savePUMLtoFile(outputPUML.getPUML(seqDiagramGenerator.createDiagram(parser.getParsingResult(), classNodeList.item(entryClass).getTextContent(), classNodeList.item(entryMethode).getTextContent())), actualOutputPath);
					//Diagramm erzeugen
					outputPUML.createPUMLfromString(actualOutputPath, outputPUML.getPUML(seqDiagramGenerator.createDiagram(parser.getParsingResult(), classNodeList.item(entryClass).getTextContent(), classNodeList.item(entryMethode).getTextContent())));
				}
	    		catch (XPathExpressionException | DOMException | IOException | ParserConfigurationException | SAXException e)
	    		{
	    			System.out.println("Kommandozeile: Verarbeitung SQ ohne output-Pfad fehlgeschlagen");
					e.printStackTrace();
				}
	    	}
		}
		catch (XPathExpressionException e1)
		{
			e1.printStackTrace();
		}
    }
	
	public Document getTestDoc()
	{
		try {
		DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuild = docBuildFact.newDocumentBuilder();
		Document doc = docBuild.parse(new File("/Users/mariangeissler/Entwickler/eclipse-workspace/puml/code/testfolder/xmlSpecifications/parsedData.xml"));
		return doc;
		}
		catch (ParserConfigurationException | SAXException | IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
