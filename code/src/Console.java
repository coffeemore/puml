import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * 
 * @author
 * Klasse für die Konsolenanwendung
 */
public class Console
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
    void showConsole(String[] args) throws ParseException
    {
	// hier die Konsolenanwendung implementieren
    	
    	//hier wird ein neuer Container für Optionen angelegt
    	Options options = new Options();
    	options.addOption("c",false,"Versuch ein -c abzufangen");
    	
    	options.addOption("i",true,"Durch Strichpunkt getrennte Liste der Pfade zu den Dateien und Ordnern eingeben");
    	
    	//Output Path an dieser Stelle
    	
    	//Parser
    	CommandLineParser parser = new DefaultParser();
    	CommandLine cmd = parser.parse( options, args);
    	
    	//hat Option Phase
    	if(cmd.hasOption("c"))	//hat "c"
    	{ 
             System.out.println("Versuch Aufruf c hat geklappt");
    	}
    	else if(cmd.hasOption("i")) //hat "i"
    	{
    		System.out.println("Versuch Aufruf i hat geklappt i");
    		for(int i = 1; i < args.length; i++)
        	{
        		System.out.println(args[i]);
        	}
    	}
    	else {	//sonst Hilfe anzeigen
    	HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("CLITester", options);
    	}
    	//System.out.println(PUMLgenerator.outputPUML.getPUML());
    }
}

