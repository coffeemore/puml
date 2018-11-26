
/**
 * 
 * @author developer
 * Klasse welche die Ausgabe des plantUML-Codes und die Klassendiagramme erzeugt
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
     * @param myParsingResult Ergebnisse des Parsens
     * @return String der den plantUML-Code enthält
     */
    public String getPUML(ParsingResult myParsingResult)
    {
	return new String();
    }

    /**
     * Speichert den plantUML-Code in eine Datei
     * @param myParsingResult Ergebnisse des Parsens
     * @param filePath Pfad an den die Datei gespeichert werden soll
     */
    public void savePUMLtoFile(ParsingResult myParsingResult, String filePath)
    {
    }
    
    /**
     * Erzeugt ein PlantUML-Diagramm aus der plantUML-Code-Datei am übergebenen Pfad
     * @param filePath Pfad an der die plantUML-Code-Datei liegt
     */
    public void createPlantUML(String filePath)
    {	
    }

}
