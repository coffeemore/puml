import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 
 * @author Leo Rauschke, Elisabeth Schuster
 */
public class CodeCollector
{

    /**
     * Enthält die Pfade zu den Dateien und Ordnern Die Liste mit den Pfaden ist vor
     * dem Aufrufen der getSourceCode-Methode zu füllen.
     */
    public ArrayList<String> paths;
    /**
     * zweite Pathsliste, in die die übergebenen Pfade kopiert werden Gewährleistung
     * des unabhängigen Einlesens von Java- und Jar-Dateien
     */
    public ArrayList<String> paths2;

    /**
     * True = .java-Dateien werden verwendet; False = .java-Dateien werden ignoriert
     */
    private boolean useJavaFiles = true;

    /**
     * True = .jar-Dateien werden verwendet; False = .jar-Dateien werden ignoriert
     */
    private boolean useJarFiles = true;

    /**
     * True = .cpp- und .hpp-Dateien werden verwendet; False = .cpp- und
     * .hpp-Dateien werden ignoriert
     */
    private boolean useCppAndHppFiles = false;

    /**
     * Konstruktor
     */
    public CodeCollector()
    {
	paths = new ArrayList<String>();
	paths2 = new ArrayList<String>();
    }

    /**
     * Sammelt den Quellcode aus allen ausgewählten Dateien und gibt diesen als
     * String zurück
     * 
     * @return String, der den vollständigen Quellcode enthält
     */
    @SuppressWarnings("unchecked")
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
			paths.remove(paths.get(j));
		    }
		}
	    }
	    // Kopieren der Pfade in die zweite Liste
	    paths2 = (ArrayList<String>) paths.clone();

	    if (useCppAndHppFiles)
	    {
		useJavaFiles = false;
		useJarFiles = false;
		return (collectCppAndHpp(buffr, filer));
	    }
	    if (useJavaFiles && !useJarFiles)
	    {
		// sammelt den Quellcode aus den Java-Dateien ein
		return (collectJava(buffr, filer));
	    } else
	    {
		if (!useJavaFiles && useJarFiles)
		{
		    // sammelt den Quellcode aus den Jar-Dateien ein
		    return (collectJar(buffr, zFile));
		}
		/**
		 * wenn useJavaFiles und useJarFiles beide auf true oder false gesetzt sind bzw.
		 * ihre Belegung anderweitig ungültig ist, wird eine Fehlermeldung ausgegeben
		 */
		else
		{
		    // sammelt den Quellcode aus den Jar- und Java-Dateien ein
		    if (useJavaFiles && useJarFiles)
		    {
			sc = collectJava(buffr, filer);
			sc += collectJar(buffr, zFile);
			return sc;
		    } else
		    {
			// wirft Exception, falls useJavaFiles und useJarFiles auf false stehen
			throw new IllegalArgumentException();
		    }
		}
	    }
	}
	/**
	 * Bei ungültiger Pfadauswahl wird eine Fehlermeldung ausgegeben
	 */
	else
	{
	    throw new NullPointerException();
	}
    }

    /**
     * Java-Dateien werden in einen String eingelesen
     * 
     * @param sc    - String zum Einsammeln
     * @param filer - FileReader zum Einlesen der Dateien
     * @param buffr - BufferedReader zum Buffern der eingelesenen Dateien
     * @return sc (eingelesener String)
     */
    private String collectJava(BufferedReader buffr, FileReader filer)
    {
	String sc = new String();
	// Schleife, die paths-Einträge durchgeht
	for (int i = 0; i < paths.size(); i++)
	{
	    // Dateien werden über Pfade eingelesen und gebuffert
	    try
	    {
		// alle Java-Dateien werden eingelesen
		if (paths.get(i).endsWith(".java"))
		{
		    filer = new FileReader(paths.get(i));
		    buffr = new BufferedReader(filer);
		    String currLine;

		    while ((currLine = buffr.readLine()) != null)
		    {
			sc += currLine;
			sc += "\n";
		    }
		}
	    } catch (IOException e)
	    {
		e.printStackTrace();
	    } finally
	    {
		// BufferedReader und FileReader werden geschlossen
		try
		{
		    if (filer != null)
		    {
			filer.close();
		    }
		    if (buffr != null)
		    {
			buffr.close();
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
     * 
     * @param sc    - String zum Einsammeln
     * @param buffr - BufferedReader zum Buffern der eingelesenen Dateien
     * @param zFile - ZipFile zum Einlesen der Jar-Dateien
     * @return sc (eingelesener String)
     */
    @SuppressWarnings("resource")
    private String collectJar(BufferedReader buffr, ZipFile zFile)
    {
	String sc = new String();
	try
	{
	    // Schleife, die alle Einträge in paths durchgeht
	    for (int i = 0; i < paths2.size(); i++)
	    {
		if (paths2.get(i).endsWith(".jar"))
		{
		    // Jar-Datei wird eingelesen
		    zFile = new ZipFile(paths2.get(i));
		    if (zFile != null)
		    {
			// die Dateien in der Jar-Datei werden hier aufgelistet und gespeichert
			Enumeration<? extends ZipEntry> entries = zFile.entries();
			// Hilfsvariable zum Prüfen, ob Java-Dateien in Jar vorhanden sind
			int m = 0;

			while (entries.hasMoreElements())
			{
			    ZipEntry entry = entries.nextElement();
			    // wenn es sich um eine Java-Datei handelt, wird diese eingelesen
			    if (!entry.isDirectory() && entry.getName().endsWith(".java"))
			    {
				m++;
				buffr = new BufferedReader(new InputStreamReader(zFile.getInputStream(entry)));
				String currLine;

				while ((currLine = buffr.readLine()) != null)
				{
				    sc += currLine;
				    sc += "\n";
				}
			    } else
			    {
				continue;
			    }
			}
			// Exception wird geworfen, wenn in der Jar-Datei keine Java-Dateien vorhanden
			// sind
			if (m == 0)
			{
			    throw new FileNotFoundException();
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

    private String collectCppAndHpp(BufferedReader buffr, FileReader filer)
    {
	String sc = new String();
	// Schleife, die paths-Einträge durchgeht
	for (int i = 0; i < paths.size(); i++)
	{
	    // Dateien werden über Pfade eingelesen und gebuffert
	    try
	    {
		// alle Hpp-Dateien werden eingelesen
		if (paths.get(i).endsWith(".hpp"))
		{
		    filer = new FileReader(paths.get(i));
		    buffr = new BufferedReader(filer);
		    String currLine;

		    while ((currLine = buffr.readLine()) != null)
		    {
			sc += currLine;
			sc += "\n";
		    }
		}
	    } catch (IOException e)
	    {
		e.printStackTrace();
	    } finally
	    {
		// BufferedReader und FileReader werden geschlossen
		try
		{
		    if (filer != null)
		    {
			filer.close();
		    }
		    if (buffr != null)
		    {
			buffr.close();
		    }
		} catch (IOException ex)
		{
		    ex.printStackTrace();
		}
	    }
	}
	for (int i = 0; i < paths.size(); i++)
	{
	    // Dateien werden über Pfade eingelesen und gebuffert
	    try
	    {
		// alle Cpp-Dateien werden eingelesen
		if (paths.get(i).endsWith(".cpp"))
		{
		    filer = new FileReader(paths.get(i));
		    buffr = new BufferedReader(filer);
		    String currLine;

		    while ((currLine = buffr.readLine()) != null)
		    {
			sc += currLine;
			sc += "\n";
		    }
		}
	    } catch (IOException e)
	    {
		e.printStackTrace();
	    } finally
	    {
		// BufferedReader und FileReader werden geschlossen
		try
		{
		    if (filer != null)
		    {
			filer.close();
		    }
		    if (buffr != null)
		    {
			buffr.close();
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
     * Prüft, ob in der übergebenen ArrayList Directories enthalten sind
     * 
     * @param paths - die Liste mit den übergebenen Pfaden
     * @return boolean
     */
    private boolean contDir(ArrayList<String> paths)
    {
	// die Schleife geht alle Einträge von paths durch und schaut, ob sich darunter
	// ein Ordner befindet
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

    public boolean isUseCppAndHppFiles()
    {
	return useCppAndHppFiles;
    }

    public void setUseCppAndHppFilesFiles(boolean useCppAndHppFiles)
    {
	this.useCppAndHppFiles = useCppAndHppFiles;
    }

}
