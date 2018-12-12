import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
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
	ZipFile zFile = null;
	File file = null;
	
	if (!paths.isEmpty())
	{
	    /**
	     * durchsucht ggf. übergebene Ordner und fügt den Inhalt in die paths-Liste ein
	     */
	    while (contDir(paths))
	    {
		for (int j = 0; j < paths.size(); j++)
		{
		    file = new File(paths.get(j));
		    if (file.isDirectory())
		    {
			File[] fArray = file.listFiles();
			for (int i = 0; i < fArray.length; i++)
			{
			    paths.add(fArray[i].getAbsolutePath());
			}
		    }
		}
	    }
	    if (useJavaFiles && !useJarFiles)
	    {
		return (collectJava(sc, buffr, filer));
	    } else
	    {
		if (!useJavaFiles && useJarFiles)
		{
		    return (collectJar(sc, buffr, zFile));
		} 
		/**
		 * wenn useJavaFiles und useJarFiles beide auf true oder false gesetzt sind 
		 * bzw. ihre Belegung anderweitig ungültig ist, wird eine Fehlermeldung ausgegeben
		 */
		else
		{
		    JOptionPane.showMessageDialog(null, "Bitte wählen Sie JAR- oder Java-Dateien aus.", "Error",
			    JOptionPane.ERROR_MESSAGE);
		    return null;
		}
	    }
	} 
	/**
	 * Bei ungültiger Pfadauswahl wird eine Fehlermeldung ausgegeben
	 */
	else
	{
	    JOptionPane.showMessageDialog(null, "Bitte wählen Sie einen Pfad aus.", "Error", JOptionPane.ERROR_MESSAGE);
	    return null;
	}
    }

    /**
     * Java-Dateien werden in einen String eingelesen
     * @param sc
     * @param buffr
     * @param filer
     * @return sc (eingelesener String)
     */
    private String collectJava(String sc, BufferedReader buffr, FileReader filer)
    {
	for (int i = 0; i < paths.size(); i++)
	{
	    try
	    {
		filer = new FileReader(paths.get(i));
		buffr = new BufferedReader(filer);
		String currLine;
		
		if (paths.get(i).endsWith(".java"))
		{
		    while ((currLine = buffr.readLine()) != null)
		    {
			sc += currLine;
		    }
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
	return sc;
    }

    /**
     * Jar-Dateien werden in einen String eingelesen
     * @param sc
     * @param buffr
     * @param zFile
     * @return sc (eingelesener String)
     */
    private String collectJar(String sc, BufferedReader buffr, ZipFile zFile)
    {
	try
	{
	    for (int i = 0; i < paths.size(); i++)
	    {
		if (paths.get(i).endsWith(".jar"))
		{
		    zFile = new ZipFile(paths.get(i));
		    if (zFile != null)
		    {
			Enumeration<? extends ZipEntry> entries = zFile.entries();
			while (entries.hasMoreElements())
			{
			    ZipEntry entry = entries.nextElement();
			    if (!entry.isDirectory() && entry.getName().endsWith(".java"))
			    {
				buffr = new BufferedReader(new InputStreamReader(zFile.getInputStream(entry)));
				String currLine;

				while ((currLine = buffr.readLine()) != null)
				{
				    sc += currLine;
				}
			    } else
			    {
				continue;
			    }
			}
		    }
		}
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
		if (zFile != null)
		{
		    zFile.close();
		}
	    } catch (IOException ex)
	    {
		ex.printStackTrace();
	    }
	}
	return sc;
    }

    /**
     * Prüft, ob in der übergebenen ArrayList Directories enthalten sind
     * @param paths
     * @return boolean
     */
    private boolean contDir(ArrayList<String> paths)
    {
	for (int i = 0; i < paths.size(); i++)
	{
	    File file = new File(paths.get(i));
	    if (file.isDirectory())
	    {
		return true;
	    }
	}
	return false;
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
