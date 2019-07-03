import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
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
    private Scanner scanner = new Scanner(System.in);
 
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
	
	// Logging Commandline
	options.addOption("l", false, "Debug: Ausgabe in Konsole");

	// ignorieren verschiedener Dateitypen
	options.addOption("ijar", false, "Dateien mit der Endung '.jar' werden ignoriert.");

	options.addOption("ijava", false, "Dateien mit der Endung '.java' werden ignoriert.");
	
	options.addOption("ucpp", false, "Verarbeite Dateien mit der Endung '.cpp'.");
	
	// ignoriere Instanzen, Variablen, Methoden in Klassendiagrammen anzeigen
	options.addOption("iinst",false, "Instanzen werden bei Erzeugung des Klassendiagramms nicht beruecksichtigt.");
	
	options.addOption("ivar",false, "Variablen werden bei Erzeugung des Klassendiagramms nicht beruecksichtigt.");
	
	options.addOption("imeth",false, "Methoden werden bei Erzeugung des Klassendiagramms nicht beruecksichtigt.");
	
	// Erstelle Klassendiagramm
	options.addOption("cc",false, "Erzeugt ein Klassendiagramm.");
	
	// Erstelle Klassendiagramm
	options.addOption("ct",false, "Erzeugt den PlantUML-Code als Text.");
		
	// Alles auflisten
	options.addOption("s",false, "Listet alle Klassen und Methoden in der Konsole auf.");
		
	// Interactive Mode
	options.addOption("int",false, "Startet interaktiven Modus.");
	
	//Hilfe
	options.addOption("h", false, "Beschreibung der Kommandos.");
	
	// Angabe fuer den Ausgabepfad
	Option output = Option.builder() 
		.longOpt("o").argName("filepath").hasArg().desc("Angabe des Pfades fuer den Zielordner.").build();
	options.addOption(output);
	
	// Logging als Logfile
	Option logfile = Option.builder()
			.longOpt("lf").argName("log-filepath").hasArg().desc("Angabe des Pfades fuer das Logfile").build();
	options.addOption(logfile);
	
	/*//Erstelle SeqenceDiagramm
	Option seqDiag = Option.builder()
			.longOpt("cs").argName("Klasse, Methode").hasArgs().type(Integer.class).valueSeparator(',').numberOfArgs(2).desc("Erzeugt ein Sequenzdiagramm.").build();
	options.addOption(seqDiag);*/
	
	// Erstelle SeqenceDiagramm
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
	
		    //Ausgabe der Hilfe
		    if (cmd.hasOption("h"))
		    {
		    	 HelpFormatter formatter = new HelpFormatter();
				 formatter.printHelp("PUML", options);
	    	}
		    else
		    {
		    	// Argumentauswertungen und Ausfuehrungen
				if (cmd.hasOption("l")) //Debugging in Console
		    	{
		    		System.out.println("Debugging");
		    		PUMLgenerator.logger.startLoggingConsole(true);
		    	}
				if (cmd.hasOption("lf"))
				{
					PUMLgenerator.logger.startLoggingFile(cmd.getOptionValue("lf"));
				}
			    //Start: Setter-Abfragen
			    if (cmd.hasOption("c")) // Anleitung ausgeben
			    {
			    	System.out.println("Consolemode");
			    
			    	if (cmd.hasOption("ucpp")) // Cpp Dateien Parsen
			    	{
			    		parser = new ParserCPP();
			    		codeCollector.setUseCppAndHppFilesFiles(true);
			    		codeCollector.setUseJarFiles(false);
			    		codeCollector.setUseJavaFiles(false);
			    	}
			    	else
			    	{
			    		// parser = new ParserJava();
			    	}
				    if (cmd.hasOption("ijar")) // ignore jar files
				    {
				    	codeCollector.setUseJarFiles(false);
				    }
				    if (cmd.hasOption("ijava")) // ignore java files
				    {
				    	codeCollector.setUseJavaFiles(false);
				    }
				    if (cmd.hasOption("iinst")) //ignore instances in classdiagramm
				    {
				    	classDiagramGenerator.setShowInstances(false);
				    }
				    if (cmd.hasOption("ivar")) //ignore variables in classdiagramm
				    {
				    	classDiagramGenerator.setShowVars(false);
				    }
				    if (cmd.hasOption("imeth")) //ignore methods in classdiagramm
				    {
				    	classDiagramGenerator.setShowMethods(false);
				    } //Ende: Setter-Abfragen
				    //Verarbeitung
				    if (cmd.hasOption("i")) // Verarbeitung vieler zu verarbeitenden Pfade
				    {
				    	//Print Ausgabeort, wenn vorhanden
				    	if (cmd.getOptionValue("o") != null) // Pruefe ob Zielordner gegeben
				    	{
							System.out.print("PUML-Ausgabe unter : "+ cmd.getOptionValue("o")+ "\n");
							//Setze Ausgabeort
							outputLocation = cmd.getOptionValue("o");
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
						if (cmd.hasOption("int")) //Starte Dialog zur Abfrage
						{
							interactiveMode();
						}
						if (cmd.hasOption("cc")) //Gewuenschtes Diagramm: Klassendiagramm
						{
							if (cmd.hasOption("ct"))
							{
								createClassPlantUmlText(outputLocation);
							}
							createClassDiag(outputLocation);
						}
						else if (cmd.hasOption("cs")) //Gewuenschtes Diagramm: SQDiagramm
						{
							System.out.print("Entry " + cmd.getOptionValues("cs")[0] + " und " + cmd.getOptionValues("cs")[1] +"\n");
							entryClass = cmd.getOptionValues("cs")[0];
							entryMethode = cmd.getOptionValues("cs")[1];
							if (cmd.hasOption("ct"))
							{
								createSQPlantUmlText(entryClass, entryMethode, outputLocation);
							}
				    		createSQDiagram(entryClass, entryMethode, outputLocation);
						}
					}
				    else if (!cmd.hasOption("i"))
				    {
				    	System.out.println("Es fehlt ein zu bearbeitender Pfad.");
				    }
				    }
			    else
			    {
					myGUI_Swing = new GUI_Swing();
			    	GUI_Swing.showGUI();
			    }
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
		char choice = '\0';
    	System.out.println("Interaktiver Modus");
    	
    	//Ausgabeort festlegen
    	//Nicht wenn outputlocation ueber "-o" gegeben
    	while ((outputLocation.contentEquals("./")) && !(choice == 'a' || choice == 'p')) 
		{
			System.out.println("Ziel-Datei in [a]rbeitsverzeichnis oder [p]fad speichern?");
			choice = scanner.next().charAt(0);
		}
    	if (choice == 'p') //Pfad einlesen
		{
			System.out.println("Ausgabepfad fuer UML-Diagramm und -Code angeben");
			outputLocation = scanner.next().toString();
		}
    	//Diagrammauswahl treffen
    	while (!(choice == 's' || choice == 'k'))
		{
			System.out.println("Erzeuge [s]equenzdiagramm oder [k]lassendiagramm");
			choice = scanner.next().charAt(0);
		}
    	System.out.println("Auswahl: "+ choice);
		if (choice == 's') //SQ
		{
			while ((entryClass.isEmpty() || entryMethode.isEmpty()))
			{
				showAllClassesMethods();
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
				if (!validateUserInput(entryClass, entryMethode))
				{
					System.out.println("Klasse/Methode nicht gefunden. Evtl. Schreibweise pruefen.");
					entryClass = "";
					entryMethode = "";
				}
			}
			System.out.println("Gewaehlte Klasse: " + entryClass + " und Methode: " + entryMethode);
			createSQPlantUmlText(entryClass, entryMethode, outputLocation);
			createSQDiagram(entryClass, entryMethode ,outputLocation);
		}
		else //ClassDiag
		{
			//Waehle Klassen aus und erzeuge das Klassen-Diagramm
			setClassesI();
			createClassPlantUmlText(outputLocation);
			createClassDiag(outputLocation);
		}
		System.out.println("Ende Interaktiver Modus");
    }
    
    /**
	 * Methode zum Erzeugen eines Klassendiagramms aus Interactivem Modus und Cmd-Standard-Nutzung
	 * @param outPath Ausgabepunkt, beachte Klassenvariable
	 */
	private void createClassDiag(String outPath)
	{
		try 
	    {  
			//Diagramm erzeugen aus String
			outputPUML.createPUMLfromString(
					outPath + "outPUML_Graph", outputPUML.getPUML(
							classDiagramGenerator.createDiagram(parser.getParsingResult())));
	    }
	    catch (IOException | XPathExpressionException e)
	    {
	    	PUMLgenerator.logger.getLog().warning("@Console/createClassDiag: Verarbeitung Klassendiagramm fehlgeschlagen" + e.toString());
	    }
	}
    /**
	 * Methode zum Erzeugen des PlantUML-Codes eines Klassendiagramms aus Interactivem Modus und Cmd-Standard-Nutzung
	 * @param outPath Ausgabepunkt, beachte Klassenvariable
	 */
	private void createClassPlantUmlText(String outPath)
	{
		try 
	    {  
			//PUML code erstellen
			outputPUML.savePUMLtoFile(outputPUML.getPUML(
					classDiagramGenerator.createDiagram(parser.getParsingResult())), outPath + "outPUML_Code");
	    }
	    catch (IOException | XPathExpressionException e)
	    {
	    	PUMLgenerator.logger.getLog().warning("@Console/createClassPlantUmlText: Verarbeitung Klassendiagramm fehlgeschlagen" + e.toString());
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
			//Diagramm erzeugen
			outputPUML.createPUMLfromString(
					outPath + "outPUML_Graph", outputPUML.getPUML(seqDiagramGenerator.createDiagram(
							parser.getParsingResult(),
								entryClass,
								entryMethode)));
		}
		catch (XPathExpressionException | DOMException | IOException | ParserConfigurationException | SAXException | TransformerException e)
		{
			PUMLgenerator.logger.getLog().warning("@Console/createSQDiagram: Verarbeitung SQ fehlgeschlagen" + e.toString());
		}
    }
    /**
     * Erstellt PlantUML-Code eines Sequenzdiagramms mit Angabe der Einsiegspunkte
     * @param entryClass Ausgewaehlte Klasse
     * @param entryMethode Ausgewaehlte Methode
     * @param outPath Ausgabepunkt, beachte Klassenvariable
     */
    private void createSQPlantUmlText(String entryClass, String entryMethode, String outPath)
    {
    	try
		{
			//Code als txt Erzeugen
			outputPUML.savePUMLtoFile(outputPUML.getPUML(
					seqDiagramGenerator.createDiagram(parser.getParsingResult(), entryClass, entryMethode)), outPath + "outPUML_Code");
		}
		catch (XPathExpressionException | DOMException | IOException | ParserConfigurationException | SAXException | TransformerException e)
		{
			PUMLgenerator.logger.getLog().warning("@Console/createSQPlantUmlText: Verarbeitung SQ fehlgeschlagen" + e.toString());
		}
    }
    /**
     * Funktion zum Testen auf vorhandene Klassen, bei Nutzereingabe
     * @param userEntryClass Eingabe des Einstiegspunktes "Klasse"
     * @param userEntryMethode Eingabe des Einstiegspunktes "Methode"
     * @return
     */
    private boolean validateUserInput(String userEntryClass, String userEntryMethode)
    {
		try
		{
	    	Document parserDoc = PUMLgenerator.parser.getParsingResult();
			NodeList classNodeList;
			NodeList methodeNodeList;
			classNodeList = xmlHelper.getList(parserDoc, "/source/classdefinition/name");
	    	List<String> possibleClasses = new ArrayList<String>();
	    	List<String> possibleMethodes = new ArrayList<String>();
	    	
			for (int i = 0; i < classNodeList.getLength(); i++)
			{
				possibleClasses.add(classNodeList.item(i).getTextContent());
				methodeNodeList = xmlHelper.getList(classNodeList.item(i), "../methoddefinition/name");
				for (int j = 0; j <methodeNodeList.getLength(); j++)
				{
					possibleMethodes.add(methodeNodeList.item(j).getTextContent());
				}
			}
			boolean classUser = false;
			for (String c : possibleClasses)
			{
				if(c.contains(userEntryClass))
				{
					classUser = true;
				}
			}
			for (String m : possibleMethodes)
			{
				if( classUser && m.contains(userEntryMethode))
				{
					return true;
				}
			}
	    	return false;
		}
		catch (XPathExpressionException e)
		{
			PUMLgenerator.logger.getLog().warning("@Console/validateUserInput: Fehler beim Initialisieren der Klassen-Nodeliste in validateClass." + e.toString());
		}
		return false;
    }
    /**
     * Ausgabe aller Klassen und Methoden im Konsolendialog
     *
     */
	private void showAllClassesMethods()
    {
		try
		{
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
			PUMLgenerator.logger.getLog().warning("@Console/showAllClassesMethods: Verarbeitung fehlgeschlagen" + e.toString());
		}
    }
	
	/**
	 * Auswahl der Klassen im interaktiven Dialog
	 * Sideeffect: Aendert Parser Result
	 * 
	 */
	private void setClassesI()
    {
		char choice = '\0';
		try
		{
			Document parserDoc = PUMLgenerator.parser.getParsingResult();
			//Initialisiere Nodelist fuer Klassennamen
			NodeList classNodeList = xmlHelper.getList(parserDoc, "/source/classdefinition/name");
			//Alle Klassen Anzeigen
			System.out.println("\nAnzahl Klassen total: " + classNodeList.getLength() + "\n");
			for (int i = 0; i < classNodeList.getLength(); i++)
			{
				System.out.println("Klasse " + i + ": " + classNodeList.item(i).getTextContent());
			}
			//Abfragen ob Klasse mit verarbeitet werden soll
			for (int i = 0; i < classNodeList.getLength(); i++)
			{
				while(!(choice == 'y' || choice == 'n'))
				{
				System.out.println("\nKlasse "+ i + ": '"+ classNodeList.item(i).getTextContent() + "' zu Diagram hinzufuegen? (y/n)");
				choice = scanner.next().charAt(0);
				}
				if (choice == 'y')
				{
					System.out.println("Klasse: '"+ classNodeList.item(i).getTextContent() + "' hinzugefuegt." );
				}
				else if (choice == 'n')
				{
					System.out.println(" **"+ classNodeList.item(i).getTextContent());
					System.out.println("get Parent "+ classNodeList.item(i).getParentNode().getNodeName());
					xmlHelper.writeDocumentToConsole(parserDoc);
					xmlHelper.delNode(classNodeList.item(i).getParentNode(), false);
					xmlHelper.writeDocumentToConsole(parserDoc);
					System.out.println("Klasse: '"+ classNodeList.item(i).getTextContent() + "' wird nicht beruecksichtigt." );
				}
				choice = '\0';
			}
		}
		catch (XPathExpressionException e)
		{
			PUMLgenerator.logger.getLog().warning("@Console/setClassesI: Verarbeitung fehlgeschlagen" + e.toString());
		}
    }	
}
