

public class PUMLgenerator
{

    static GUI myGUI = null;
    static Console myConsole = null;
    static CodeCollector codeCollector = new CodeCollector();
    static ParserIf parser = new ParserJava();
    static OutputPUML outputPUML = new OutputPUML();
  
    /**
     * Launch the application.
     */
    public static void main(String[] args)
    {
	//Nur temporäre Lösung. Sollte durch schöneres Konstrukt ersetzt werden
	boolean useGUI = true;
	for (int i = 0; i < args.length; i++)
	{
	    if (args[i].equals("-c"))
	    {
		useGUI = false;
	    }
	}

	if (!useGUI)
	{
	    myConsole = new Console();
	    myConsole.showConsole();
	}
	else
	{
	    myGUI = new GUI();
	    myGUI.showGUI();
	}//endelse
    }





}
