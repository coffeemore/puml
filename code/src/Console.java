
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
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
	//Initialisierung des Ausgabeortes, default: "./"
	private String outputLocation = "./";
	private String entryClass = new String();
    private String  entryMethode = new String();
	
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
	
	/*//Erstelle SeqenceDiagramm
	Option seqDiag = Option.builder()
			.longOpt("cs").argName("Klasse, Methode").hasArgs().type(Integer.class).valueSeparator(',').numberOfArgs(2).desc("Erzeugt ein Sequenzdiagramm.").build();
	options.addOption(seqDiag);*/
	
	//Erstelle SeqenceDiagramm
	Option seqDiag = Option.builder() 
		.longOpt("cs").argName("Klasse, Methode").hasArg().valueSeparator(',').numberOfArgs(2).desc("Erzeugt ein Sequenzdiagramm.")
		.build();
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
		    	//Print Ausgabeort, wenn vorhanden
		    	if (cmd.getOptionValue("o") != null)
		    	{
					System.out.print("PUML-Ausgabe unter : "+ cmd.getOptionValue("o")+ "\n");
		    	}
				//Lesen der Pfade
				for (String k : cmd.getOptionValues("i"))
				{
			       	if (!k.equals(""))
			    	{
			    		System.out.println("Lese: "+ k + "\nadded: "+codeCollector.paths.add(k));
			    	}
				}
				// Parser verarbeitet Daten
			    PUMLgenerator.parser.parse(codeCollector.getSourceCode()); 
			    
				if (cmd.hasOption("s")) //Alle Klassen Methoden auflisten
				{
					showAllClassesMethods();
				}
				if (cmd.hasOption("o")) // Pruefe ob Zielordner gegeben
				{
					outputLocation = cmd.getOptionValue("o");
				}
				if (cmd.hasOption("int")) //Starte Dialog zur Abfrage
				{
					interactiveMode();
				}
				if (cmd.hasOption("cc")) //Gewuenschtes Diagramm
				{
					createClassDiag(outputLocation);
				}
				if (cmd.hasOption("cs"))
				{
					System.out.print("Entry " + cmd.getOptionValues("cs")[0] + " und " + cmd.getOptionValues("cs")[1] +"\n");
					entryClass = cmd.getOptionValues("cs")[0];
					entryMethode = cmd.getOptionValues("cs")[1];
		    		createSQDiagram(entryClass, entryMethode, outputLocation);
				}
			}
		    else if (!cmd.hasOption("i"))
		    {
		    	System.out.println("Es fehlt ein zu bearbeitender Pfad.");
		    }
		}
		catch (UnrecognizedOptionException uoe ) // Falls Parameter unbekannt, Hilfe ausgeben
		{
		    System.out.println("Paramter: " + uoe.getOption() + " unknown.");
		    HelpFormatter formatter = new HelpFormatter();
		    formatter.printHelp("PUML", options);
		}
    }
    
    /**
     * Interaktiv Dialog
     */
    private void interactiveMode()
    {
    	Scanner scanner = new Scanner(System.in);
		String choice = new String();
		
    	System.out.println("Interaktiver Modus");
    	
    	//Ausgabeort festlegen
    	while (!(choice.contains("a") || choice.contains("p"))) 
		{
			System.out.println("Zeil-Datei in [a]rbeitsverzeichnis oder [p]fad speichern?");
			choice = scanner.next();
		}
    	if (choice.contains("p")) //Pfad einlesen
		{
			System.out.println("Ausgabepfad fuer UML-Diagramm und -Code angeben");
			outputLocation = scanner.next();
		}
    	//Diagrammauswahl treffen
    	while (!(choice.contains("s") || choice.contains("k")))
		{
			System.out.println("Erzeuge [s]equenzdiagramm oder [k]lassendiagramm");
			choice = scanner.next();
		}
		if (choice.contains("s")) //SQ
		{
			System.out.println("Auswahl: "+ choice);
			
			while ((entryClass.isEmpty() || entryMethode.isEmpty()))
			{
				showAllClassesMethods();
				setClassesI();
				System.out.println("Waehle Klasse als Einstiegspunkt fuer SQDiagramm:");
				entryClass = scanner.next();
				if (Character.isLowerCase(entryClass.toCharArray()[0]))
				{
					System.out.println("Der Klassenname : '"+ entryClass + "' wurde klein geschrieben. Uebereinstimmung mit Quelltext ueberpruefen!\n");
				}
				System.out.println("Waehle Methode als Einstiegspunkt fuer SQDiagramm:");
				entryMethode = scanner.next();
				if (Character.isUpperCase(entryMethode.toCharArray()[0]))
				{
					System.out.println("Der Methodenname : '"+ entryMethode + "' wurde gross geschrieben. Uebereinstimmung mit Quelltext ueberpruefen!\n");
				}
			}
			System.out.println("Gewaehlte Klasse: " + entryClass + " und Methode: " + entryMethode);
			createSQDiagram(entryClass, entryMethode ,outputLocation);
		}
		else //ClassDiag
		{
			createClassDiag(outputLocation);
		}
		scanner.close();
    }
    
    /**
	 * Methode zum Erzeugen eines Klassendiagramms aus Interactivem Modus und Cmd-Standard-Nutzung
	 * @param outPath Ausgabepunkt, beachte Klassenvariable
	 */
	private void createClassDiag(String outPath)
	{
		try 
	    {  
			//PUML code erstellen
			outputPUML.savePUMLtoFile(outputPUML.getPUML(
					classDiagramGenerator.createDiagram(parser.getParsingResult())), outPath + "outPUML_Code");
			//Diagramm erzeugen aus String
			outputPUML.createPUMLfromString(
					outPath + "outPUML_Graph", outputPUML.getPUML(
							classDiagramGenerator.createDiagram(parser.getParsingResult())));
	    }
	    catch (IOException | XPathExpressionException e)
	    {
	    	System.out.println("Kommandozeile: Verarbeitung Klassendiagramm fehlgeschlagen");
	    	e.printStackTrace();
	    }
	}
	
    /**
     * Erstellen eines Sequenzdiagramms mit Angabe der Einsiegspunkte
     * @param entryClass Ausgewaehlte Klasse
     * @param entryMethode Ausgewaehlte Methode
     * @param outPath Ausgabepunkt, beachte Klassenvariable
     */
    private void createSQDiagram(String entryClass, String entryMethode, String outPath)
    {
    	try
		{
			//Code Erzeugen
			outputPUML.savePUMLtoFile(outputPUML.getPUML(
					seqDiagramGenerator.createDiagram(parser.getParsingResult(), entryClass, entryMethode)), outPath + "outPUML_Code");
			//Diagramm erzeugen
			outputPUML.createPUMLfromString(
					outPath + "outPUML_Graph", outputPUML.getPUML(seqDiagramGenerator.createDiagram(
							parser.getParsingResult(),
								entryClass,
								entryMethode)));
		}
		catch (XPathExpressionException | DOMException | IOException | ParserConfigurationException | SAXException e)
		{
			System.out.println("Kommandozeile: Verarbeitung SQ ohne output-Pfad fehlgeschlagen");
			e.printStackTrace();
		}
    }
    
    /**
     * Ausgabe aller Klassen und Methoden im Konsolendialog
     *
     */
	private void showAllClassesMethods()
    {
		try
		{
			//Document parserDoc = (); //Test
			Document parserDoc = PUMLgenerator.parser.getParsingResult();
			//Initialisiere Nodelist fuer Klassennamen
			NodeList classNodeList = xmlHelper.getList(parserDoc, "/source/classdefinition/name");
			System.out.println("\nAnzahl Klassen total: " + classNodeList.getLength() + "\n");
			//Ausgabe fuer jede Klasse
			for (int i = 0; i < classNodeList.getLength(); i++)
			{
				System.out.println("Klasse "+ i + ": "+ classNodeList.item(i).getTextContent());
				
				//Initialisiere NodeList fuer methoden der Klasse
				NodeList methodeNodeList = xmlHelper.getList(classNodeList.item(i), "../methoddefinition/name");
				System.out.println("Anzahl Methoden: "+ methodeNodeList.getLength() + " in " + classNodeList.item(i).getTextContent());
				for (int j = 0; j <methodeNodeList.getLength(); j++)
				{
					System.out.println("Methode "+ j + ", Name: " + methodeNodeList.item(j).getTextContent());
				}
				System.out.print("\n");
			}
		}
		catch (XPathExpressionException e)
		{
			e.printStackTrace();
			System.out.println("Kommandozeile: Verarbeitung showAllClassesMethods fehlgeschlagen.");
		}
    }
	
	private void setClassesI()
    {
		Scanner scanner = new Scanner(System.in);
		String choice = new String();
		try
		{
			Document parserDoc = PUMLgenerator.parser.getParsingResult();
			//Initialisiere Nodelist fuer Klassennamen
			NodeList classNodeList = xmlHelper.getList(parserDoc, "/source/classdefinition/name");
			
			System.out.println("\nAnzahl Klassen total: " + classNodeList.getLength() + "\n");
			//Ausgabe fuer jede Klasse
			for (int i = 0; i < classNodeList.getLength(); i++)
			{
				while(!(choice.contains("y") || choice.contains("n")))
				{
				System.out.println("Klasse "+ i + ": '"+ classNodeList.item(i).getTextContent() + "' zu Diagram hinzufuegen? (y/n) \n");
				choice = scanner.next();
				}
				if (choice.contains("y"))
				{
					System.out.println("Klasse: '"+ classNodeList.item(i).getTextContent() + "' hinzugefuegt." );
				}
				else if (choice.contains("n"))
				{
					System.out.println("Klasse: '"+ classNodeList.item(i).getTextContent() + "' wird nicht beruecksichtigt." );
				}
				choice = "";
			}
			scanner.close();
		}
		catch (XPathExpressionException e)
		{
			e.printStackTrace();
			System.out.println("Kommandozeile: Verarbeitung setAllClasses fehlgeschlagen.");
		}
    }
	public Document getTestDoc()
	{
		try {
		DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuild = docBuildFact.newDocumentBuilder();
		Document doc = docBuild.parse(new File("/home/mariangeissler/puml/code/testfolder/xmlSpecifications/parsedData.xml"));
		return doc;
		}
		catch (ParserConfigurationException | SAXException | IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
