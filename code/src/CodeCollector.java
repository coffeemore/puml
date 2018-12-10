import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * 
 * @author Die Klasse dient zum Einsammeln des Quellcodes. Sie kann .java-,
 *         .jar-Dateien und komplette Ordner einsammeln. Die Pfade zu den
 *         Dateien und den Ordnern werden von der Anwendung in das paths-Array
 *         geschrieben.
 */
public class CodeCollector
{

    /**
     * Enthält die Pfade zu den Dateien und Ordnern Die Liste mit den Pfaden ist vor
     * dem Aufrufen der getSourceCode-Methode zu füllen.
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
	paths = new ArrayList<String>();
    }

    /**
     * Sammelt den Quellcode aus allen ausgewählten Dateien und gibt diesen als
     * String zurück
     * 
     * @return String, der den vollständigen Quellcode enthält
     */
    public String getSourceCode()
    {
	String sc = new String();
	BufferedReader buffr = null;
	FileReader filer = null;
	if (!paths.isEmpty())
	{
	    if (useJavaFiles && !useJarFiles)
	    {

		for (int i = 0; i < paths.size(); i++)
		{
		    // Text in File in String einlesen
		    // eingelesenes an sc ranhängen

		    try
		    {

//        		buffr = new BufferedReader(new FileReader(FILENAME));
			filer = new FileReader(paths.get(i));
			buffr = new BufferedReader(filer);

			String currLine;

			while ((currLine = buffr.readLine()) != null)
			{

			    // ranhängen
			    sc += currLine;
			}

		    } catch (IOException e)
		    {
			e.printStackTrace();
		    }

		    finally
		    {
			try
			{
			    if (buffr != null)
				buffr.close();
			    if (filer != null)
				filer.close();
			} catch (IOException ex)
			{
			    ex.printStackTrace();
			}
		    }
		}
	    } else
	    {
		if (useJarFiles && !useJavaFiles)
		{
		    try
		    {

//    		buffr = new BufferedReader(new FileReader(FILENAME));
			filer = new FileReader(paths.get(0));
			buffr = new BufferedReader(filer);

			String currLine;

			while ((currLine = buffr.readLine()) != null)
			{
			    // ranhängen
			    sc += currLine;
			}

		    } catch (IOException e)
		    {
			e.printStackTrace();
		    } finally
		    {
			try
			{
			    if (buffr != null)
			    {
				buffr.close();
			    }
			    if (filer != null)
			    {
				filer.close();
			    }
			} catch (IOException ex)
			{
			    ex.printStackTrace();
			}
		    }
		}

		else
		{
		    JOptionPane.showMessageDialog(null, "Bitte wählen Sie JAR- oder Java-Dateien aus.", "Error",
			    JOptionPane.ERROR_MESSAGE);
		    return null;
		}
	    }
	    return sc;

	}

	else
	{

	    JOptionPane.showMessageDialog(null, "Bitte wählen Sie einen Pfad aus.", "Error", JOptionPane.ERROR_MESSAGE);
	    return null;
	}
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
