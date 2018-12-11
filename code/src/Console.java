import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * 
 * @author
 * Klasse für die Konsolenanwendung
 */
public class Console extends PUMLgenerator
{
   
    /**
     * Konstruktor
     */
    public Console()
    {
    	//System.out.println("Aufruf Console() wird erstmal nicht genutzt.");
	
    }
    
    /**
     * Startet die Abarbeitung der Konsolenanwendung und ggf. Dialoge
     */
    void showConsole(String[] args) throws ParseException //Konsolenanwendung implementiert
    {
    	
    	//hier wird ein neuer Container für Optionen angelegt
    	Options options = new Options();
    	options.addOption("c",false,"Versuch ein -c abzufangen");
    	
    	Option input = Option.builder() //Input files
    			.longOpt("input")
    			.argName("filepath")
    			.hasArg()
    			.valueSeparator('-')
    			.numberOfArgs(Option.UNLIMITED_VALUES)
    			.desc("Angabe der zu verarbeitenden Pfade, durch - getrennt")
    			.build();
    	options.addOption(input);
  
    	options.addOption("ijar",false,"Dateien mit der Endung .jar werden ignoriert.");
    	
    	options.addOption("ijava", false, "Datein mit der Endung .java werden ignoriert.");
    	
    	Option output = Option.builder()	//Angabe fuer den Ausgabepfad
    			.longOpt("output")
    			.argName("filepath")
    			.hasArg()
    			.desc("Angabe des Pfades fuer den Zielordner.")
    			.build();
    	options.addOption(output);
  
    	//Parser
    	CommandLineParser commandParser = new DefaultParser();
    	CommandLine cmd = commandParser.parse( options, args);
    	
    	//hat Option Phase
    	if(cmd.hasOption("c"))	//hat "c"
    	{ 
    		String pfad;
            System.out.println("Pfad der zu pumelnden Datei eingeben:");
            		 
    	}
    	if(cmd.hasOption("input")) //Verarbeitung vieler Pfade
    	{
    		String parseResult="";
    		//evtl. nutzen von CodeCollector Class
    		String[] pathArray = cmd.getOptionValues("input");
    		for(int i=1; i < pathArray.length;i++)
    		{
    			System.out.println(i + pathArray[i]);// Code to parse data into in
    		}
    	}
    	if(cmd.hasOption("ijar")) //ignore jar files
    	{
    		codeCollector.setUseJarFiles(false);
    	}
    	if(cmd.hasOption("ijava")) //ignore java files
    	{
    		codeCollector.setUseJavaFiles(false);
    	}
    	if(cmd.hasOption("output")) //Zielordner festlegen
    	{
    		outputPUML.savePUMLtoFile(parser.getParsingResult(),cmd.getOptionValue("output"));
    		System.out.println(cmd.getOptionValue("output"));
    	}
    	else {	//sonst Hilfe anzeigen
    	HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("CLITester", options);
    	}
    	//System.out.println(PUMLgenerator.outputPUML.getPUML());
    }
}

