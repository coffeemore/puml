import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;
import net.sourceforge.plantuml.SourceStringReader;


/**
 * 
 * @author developer Klasse welche die Ausgabe des plantUML-Codes und die
 *         Klassendiagramme erzeugt
 */
public class OutputPUML
{
    /**
     * Konstruktor
     */
    public OutputPUML()
    {
    };

    /**
     * Liefert den plantUML-Code zurueck
     * 
     * @param myParsingResult Ergebnisse des Parsens
     * @return String der den plantUML-Code enthaelt
     */
    public String getPUML(ParsingResult myParsingResult)
    {
	String pumlCode = "";
	int from;
	int to;
	pumlCode += "@startuml\n";
	for (int i = 0; i < myParsingResult.classes.size(); i++)
	{
	    pumlCode += "class ";
	    pumlCode += myParsingResult.classes.get(i);
	    pumlCode += "\n";
	}
	for (int i = 0; i < myParsingResult.classConnections.size(); i++)
	{
	    from = myParsingResult.classConnections.get(i).getFrom();
	    to = myParsingResult.classConnections.get(i).getTo();
	    pumlCode += myParsingResult.classes.get(from);
	    if (myParsingResult.classConnections.get(i).getConnection() == ClassConnection.connectionType.extension)
	    {
		pumlCode += " <|-- "; // TODO eventuell Pfeile aendern
	    }
	    else if (myParsingResult.classConnections.get(i)
		    .getConnection() == ClassConnection.connectionType.aggregation)
	    {
		pumlCode += " o-- "; // TODO eventuell Richtung aendern
	    }
	    else
	    {
		pumlCode += " *-- "; // TODO eventuell Richtung aendern
	    }
	    pumlCode += myParsingResult.classes.get(to);
	    pumlCode += "\n";
	}
	pumlCode += "@enduml";
	return pumlCode;
    }

    
    /**
     * Speichert den plantUML-Code aus dem String der getPUML Methode in eine Datei
     * 
     * @param pumlCode		String der durch die getPUML Methode erzeugt wird
     * @param filePath		Pfad an den die Datei gespeichert werden soll
     * @throws IOException 
     */
    public void savePUMLtoFile(String pumlCode, String filePath) throws IOException
    {
	    BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filePath)));
	    bw.write(pumlCode);
	    bw.flush();
	    bw.close();
    }

    /**
     * Erzeugt ein PlantUML-Diagramm aus der plantUML-Code-Datei am uebergebenen Pfad
     * 
     * @param sourcePath	Pfad an der die plantUML-Code-Datei liegt
     * @param destPath		Ordnerpfad, !!nicht Dateiname!!, an dem die png-Datei gespeichert wird, Name der PNG=Name der Textdatei
     * @throws IOException
     */
    public void createPUMLfromFile(String sourcePath, String destPath) throws IOException //
    {
	File source = new File(sourcePath);
	File dest = new File (destPath);
	SourceFileReader reader = new SourceFileReader(source, dest);
	List<GeneratedImage> list = reader.getGeneratedImages();
	list.get(0).getPngFile();
    }
    
    
    /**
     * Erzeugt ein PlantUML-Diagramm aus dem durch die Methode getPUML erzeugten String
     * 
     * @param filePath	Pfad an der die plantUML-PNG gespeichert werden soll
     * @param pumlCode	String der durch die getPUML Methode erzeugt wurde
     * @throws IOException
     */
    public void createPUMLfromString(String filePath, String pumlCode) throws IOException
    {
    	OutputStream png = new FileOutputStream(filePath);
    	SourceStringReader reader = new SourceStringReader(pumlCode);
    	reader.outputImage(png).getDescription();
    }
    
}
