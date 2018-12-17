
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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
	options.addOption("c", false, "Konsole mit Anleitung wird aufgerufen");

	Option input = Option.builder() // Angabe der zu verarbeitenden Dateien
		.longOpt("i").argName("filepath").hasArg().valueSeparator(';').numberOfArgs(Option.UNLIMITED_VALUES)
		.desc("Angabe der zu verarbeitenden Pfade, durch ; getrennt").build();
	options.addOption(input);

	// ignorieren verschiedener Dateitypen
	options.addOption("ijar", false, "Dateien mit der Endung .jar werden ignoriert.");

	options.addOption("ijava", false, "Dateien mit der Endung .java werden ignoriert.");

	Option output = Option.builder() // Angabe fuer den Ausgabepfad
		.longOpt("o").argName("filepath").hasArg().desc("Angabe des Pfades fuer den Zielordner.").build();
	options.addOption(output);

	// Parser
	CommandLineParser commandParser = new DefaultParser();
	CommandLine cmd = commandParser.parse(options, args);

	// Argumentauswertungen und Ausfuehrungen
	if (cmd.hasOption("c")) // Anleitung ausgeben
	{
	    HelpFormatter formatter = new HelpFormatter();
	    formatter.printHelp("PUML", options);
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
	    PUMLgenerator.parser.parse(codeCollector.getSourceCode()); // Parser berbeitet Daten welche ihm übergeben
								       // werden

	    if (cmd.hasOption("o")) // Zielordner abfragen, sonst in Arbeitsverzeichnis sichern
	    {
		outputPUML.savePUMLtoFile(parser.getParsingResult(), cmd.getOptionValue("o")); // UML Code generieren
											       // und im uebergebenen
											       // Pfad ablegen
		System.out.println("test");
		try
		{
		    outputPUML.createPlantUML(cmd.getOptionValue("o"), outputPUML.getPUML(parser.getParsingResult())); // Diagramm
														       // erstellen
														       // und
														       // im
														       // uebergebenen
														       // Pfad
														       // ablegen
		}
		catch (IOException e)
		{
		    e.printStackTrace();
		}
		System.out.println(
			"Zielordner:" + cmd.getOptionValue("o") + "\nQuelle: " + codeCollector.getSourceCode());
	    }
	    else
	    {
		outputPUML.savePUMLtoFile(parser.getParsingResult(), "./outputPUML");
		try
		{
		    outputPUML.createPlantUML("./outputPUML", outputPUML.getPUML(parser.getParsingResult())); // Diagramm
												    // erstellen und im
												    // Arbeitsverzeichnis
												    // ablegen
		}
		catch (IOException e)
		{
		    e.printStackTrace();
		}
	    }
	}
	else if (!cmd.hasOption("c"))
	{
	    System.out.println("Es fehlt ein zu bearbeitender Pfad.");
	}
    }
}
