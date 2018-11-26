import java.util.ArrayList;

/**
 * 
 * @author 
 * Die Klasse dient zum Einsammeln des Quellcodes. 
 * Sie kann .java-, .jar-Dateien und komplette Ordner einsammeln.
 * Die Pfade zu den Dateien und den Ordnern werden von der Anwendung in das paths-Array geschrieben.
 */
public class CodeCollector
{

    /**
     * Enthält die Pfade zu den Dateien und Ordnern
     * Die Liste mit den Pfaden ist vor dem Aufrufen der getSourceCode-Methode zu füllen.
     */
    public ArrayList<String> paths;
    
    /**
     * True = .java-Dateien werden verwendet; False = .java-Dateien werden ignoriert
     */
    private boolean useJavaFiles;
    
    /**
     * True = .jar-Dateien werden verwendet; False = .jar-Dateien werden ignoriert
     */
    private boolean useJarFiles;



    /**
     * Konstruktor
     */
    public CodeCollector()
    {
    };



    
    /**
     * Sammelt den Quellcode aus allen ausgewählten Dateien und gibt diesen als String zurück
     * @return String, der den vollständigen Quellcode enthält
     */
    public String getSourceCode()
    {
	return new String("testcode");
    }
    
    
    public boolean isUseJavaFiles()
    {
        return useJavaFiles;
    }

    public void setUseJavaFiles(boolean useJavaFiles)
    {
        this.useJavaFiles = useJavaFiles;
    }
    
    public boolean isUseJarFiles()
    {
        return useJarFiles;
    }


    public void setUseJarFiles(boolean useJarFiles)
    {
        this.useJarFiles = useJarFiles;
    }

}
