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
 * @author Die Klasse dient zum Einsammeln des Quellcodes. Sie kann .java-,
 *         .jar-Dateien und komplette Ordner einsammeln. Die Pfade zu den
 *         Dateien und den Ordnern werden von der Anwendung in das paths-Array
 *         geschrieben.
 */
public class CodeCollector
{

    /**
     * EnthÃ¤lt die Pfade zu den Dateien und Ordnern Die Liste mit den Pfaden ist vor
     * dem Aufrufen der getSourceCode-Methode zu fÃ¼llen.
     */
    public ArrayList<String> paths;
    /**
     * zweite Pathsliste, in die die Ã¼bergebenen Pfade kopiert werden GewÃ¤hrleistung
     * des unabhÃ¤ngigen Einlesens von Java- und Jar-Dateien
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
     * Konstruktor
     */
    public CodeCollector()
    {
	paths = new ArrayList<String>();
	paths2 = new ArrayList<String>();
    }

    /**
     * Sammelt den Quellcode aus allen ausgewÃ¤hlten Dateien und gibt diesen als
     * String zurÃ¼ck
     * 
     * @return String, der den vollstÃ¤ndigen Quellcode enthÃ¤lt
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
	    printList(paths);
	    /**
	     * durchsucht ggf. Ã¼bergebene Ordner und fÃ¼gt den Inhalt in die paths-Liste ein
	     */
	    while (contDir(paths))
	    {
		System.out.println("true");
		printList(paths);
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
			System.out.println("Ordner aufgelöst: \n");
			printList(paths);
		    }
		}
	    }
	    // Kopieren der Pfade in die zweite Liste
	    paths2 = (ArrayList<String>) paths.clone();

	    if (useJavaFiles && !useJarFiles)
	    {
		// sammelt den Quellcode aus den Java-Dateien ein
		return (collectJava( buffr, filer));
	    } else
	    {
		if (!useJavaFiles && useJarFiles)
		{
		    
		    // sammelt den Quellcode aus den Jar-Dateien ein
		    return (collectJar( buffr, zFile));
		}
		/**
		 * wenn useJavaFiles und useJarFiles beide auf true oder false gesetzt sind bzw.
		 * ihre Belegung anderweitig ungÃ¼ltig ist, wird eine Fehlermeldung ausgegeben
		 */
		else
		{
		    // sammelt den Quellcode aus den Jar- und Java-Dateien ein
		    if (useJavaFiles && useJarFiles)
		    {
			
			sc = collectJava( buffr, filer);
			sc += collectJar( buffr, zFile);

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
	 * Bei ungÃ¼ltiger Pfadauswahl wird eine Fehlermeldung ausgegeben
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
    private String collectJava( BufferedReader buffr, FileReader filer)
    {
	
	String sc="";
	System.out.println("Beginn collectJava");
	printList(paths);
	System.out.println("Größe paths: " + paths.size());
	// Schleife, die paths-EintrÃ¤ge durchgeht
	for (int i = 0; i < paths.size(); i++)
	{
	    // Dateien werden Ã¼ber Pfade eingelesen und gebuffert
	    try
	    {
		System.out.println("geht in try-Block rein");
		System.out.println("AktuellerEintrag: \n" + paths.get(i));
		// alle Java-Dateien werden eingelesen
		if (paths.get(i).endsWith(".java"))
		{
		    System.out.println("Java-Datei gefunden");
		    filer = new FileReader(paths.get(i));
		    buffr = new BufferedReader(filer);
		    String currLine;

		    while ((currLine = buffr.readLine()) != null)
		    {
			sc += currLine;
			sc += "\n";
		    }
		} else
		{
		    paths.remove(paths.get(i));
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
	System.out.println("sc = " + sc);
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
    private String collectJar( BufferedReader buffr, ZipFile zFile)
    {
	System.out.println("Beginn collectJar");
	String sc2="";
	//String sc2 = new String();
	// Exception werfen, wenn in Jardatei nur .class Dateien sind
	try
	{
	    // Schleife, die alle EintrÃ¤ge in paths durchgeht
	    for (int i = 0; i < paths2.size(); i++)
	    {
		if (paths2.get(i).endsWith(".jar"))
		{
		    System.out.println("geht in .jar-Bedingung rein");
		    // Jar-Datei wird eingelesen
		    zFile = new ZipFile(paths2.get(i));
		    if (zFile != null)
		    {
			// die Dateien in der Jar-Datei werden hier aufgelistet und gespeichert
			Enumeration<? extends ZipEntry> entries = zFile.entries();
			// Hilfsvariable zum PrÃ¼fen, ob Java-Dateien in Jar vorhanden sind
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
				    sc2 += currLine;
				    sc2 += "\n";
				}
			    } else
			    {
				continue;
			    }
			}
			//Exception wird geworfen, wenn in der Jar-Datei keine Java-Dateien vorhanden sind
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
	System.out.println("sc2 = " + sc2);
	return sc2;
    }

    /**
     * PrÃ¼ft, ob in der Ã¼bergebenen ArrayList Directories enthalten sind
     * 
     * @param paths - die Liste mit den Ã¼bergebenen Pfaden
     * @return boolean
     */
    private boolean contDir(ArrayList<String> paths)
    {
	// die Schleife geht alle EintrÃ¤ge von paths durch und schaut, ob sich darunter
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
    
    void printList(ArrayList <String> list) {
	
	for (int i = 0; i< list.size(); i++) {
	    System.out.println(list.get(i));
	}
    }

}
