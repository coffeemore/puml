import org.apache.commons.cli.ParseException;

public class PUMLgenerator
{

    static GUI_SWT myGUI_SWT = null;
    static Console myConsole = null;
    public static CodeCollector codeCollector = new CodeCollector();
    static ParserIf parser = new ParserJava();
    static OutputPUML outputPUML = new OutputPUML();
    static LogMain logger = new LogMain();
    static ClassDiagramGenerator classDiagramGenerator = new ClassDiagramGenerator();
    static SequenceDiagramGenerator seqDiagramGenerator = new SequenceDiagramGenerator();
    static XmlHelperMethods xmlHelper = new XmlHelperMethods();
  
    /**
     * Launch the application.
     * @throws ParseException 
     */
    public static void main(String[] args) throws ParseException
    {
	//Nur temporäre Lösung. Sollte durch schöneres Konstrukt ersetzt werden
	boolean useGUI = true;
	if (args.length > 0)
	   {
		useGUI = false;
	   }

	if (!useGUI)
	{
	    myConsole = new Console();
	    myConsole.showConsole(args);
	}
	else
	{
		
		myGUI_SWT = new GUI_SWT();
		myGUI_SWT.open();
	}//endelse
    }






}
