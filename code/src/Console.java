
import java.io.IOException;

import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;

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
	Options classDiag = new Options();
	options.addOption("cc",false, "Erzeugt ein Klassendiagramm.");
		
	//Alles auflisten
	Options show = new Options();
	options.addOption("s",false, "Listet alle Klassen und Methoden auf.");
		
	//Alles auflisten
	Options interactive = new Options();
	options.addOption("int",false, "Startet interaktiven Modus.");

	// Angabe fuer den Ausgabepfad
	Option output = Option.builder() 
		.longOpt("o").argName("filepath").hasArg().desc("Angabe des Pfades fuer den Zielordner.").build();
	options.addOption(output);
	
	//Erstelle SeqenceDiagramm
	Option seqDiag = Option.builder()
			.longOpt("cs").argName("Klasse").argName("Methode").hasArgs().valueSeparator(',').numberOfArgs(2).desc("Erzeugt ein Sequenzdiagramm.").build();
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
			PUMLgenerator.parser.parse(codeCollector.getSourceCode()); // Parser berbeitet Daten welche ihm übergeben werden
			
			if (cmd.hasOption("o")) // Pruefe ob Zielordner vorhanden
			{
				if (cmd.hasOption("cc")) //Gewuenschtes Diagramm
				{
					createClassDiagram(cmd, true);
				}
				if (cmd.hasOption("cs"))
				{
					createSQDiagram();
				}
			}
			else // Falls kein Output-Path definiert
			{
				if (cmd.hasOption("cc")) //Gewuenschtes Diagramm
				{
					createClassDiagram(cmd, false);
				}
				if (cmd.hasOption("cs"))
				{
					createSQDiagram();
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
    
    private void createClassDiagram(CommandLine cmd, Boolean outPath)
    {
    	if (!outPath) //Falls kein Ausgabeordner definiert, in Arbeitsverzeichnis schreiben
    	{
    		try
		    {
    			
    			outputPUML.savePUMLtoFile(outputPUML.getPUML(parser.getParsingResult()),
				"./outPUML_Code_defaultlocation"); // Code erzeugen Funktion

    			outputPUML.createPUMLfromString("./outPUML_Graph_defaultlocation",
				outputPUML.getPUML(parser.getParsingResult())); // Einbinden der Diagrammfunktion
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
    			outputPUML.savePUMLtoFile(outputPUML.getPUML(parser.getParsingResult()),
				cmd.getOptionValue("o") + "outPUML_Code"); // Einbinden der neuen Code Funktion

    			outputPUML.createPUMLfromString(cmd.getOptionValue("o") + "outPUML_Graph",
				outputPUML.getPUML(parser.getParsingResult())); // Einbinden der Diagrammfunktion
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
    private void createSQDiagram()
    {
 
    }
}
