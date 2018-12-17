import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;

//import ClassConnection.connectionType;

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
     * Liefert den plantUML-Code zurück
     * 
     * @param myParsingResult Ergebnisse des Parsens
     * @return String der den plantUML-Code enthält
     */
    public String getPUML(ParsingResult myParsingResult) // TODO eventuell ueberfluessig? http://plantuml.com/api
							 // ->Hilfe
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
		pumlCode += " --|> "; // TODO eventuell Pfeile
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
     * Speichert den plantUML-Code in eine Datei
     * 
     * @param myParsingResult Ergebnisse des Parsens
     * @param filePath        Pfad an den die Datei gespeichert werden soll
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
     * Erzeugt ein PlantUML-Diagramm aus dem getPUML String am übergebenen Pfad
     * 
     * @param filePath Pfad an der die plantUML-Code-Datei liegt
     * @throws IOException
     */
    public void createPlantUML(String filePath, String pumlCode) throws IOException
    {
	File source = new File(filePath);
	SourceFileReader reader = new SourceFileReader(source);
	List<GeneratedImage> list = reader.getGeneratedImages();
	// Generated files
	File png = list.get(0).getPngFile();
	//TODO eventuell, damit kommt der Fehler dann auch Weg und das File kann nicht verändert werden?
	png.setReadable(true);
	png.setExecutable(false);
    }
}
