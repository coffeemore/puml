import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import net.sourceforge.plantuml.SourceStringReader;


//import ClassConnection.connectionType;


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
    public String getPUML(ParsingResult myParsingResult)	//TODO eventuell ueberfluessig? http://plantuml.com/api ->Hilfe
    {
    	String pumlCode = null;
    	int from;
    	int to;
    	pumlCode+="@startuml%";
    	for (int i = 0; i < myParsingResult.classes.size(); i++) 
    	{
    		pumlCode+="class ";
			pumlCode+=myParsingResult.classes.get(i);
    		pumlCode+="%";
		}
    	for (int i = 0; i < myParsingResult.classConnections.size(); i++) 
    	{
    		from=myParsingResult.classConnections.get(i).getFrom();
    		to=myParsingResult.classConnections.get(i).getTo();
    		pumlCode+=myParsingResult.classes.get(from);
    		if (myParsingResult.classConnections.get(i).getConnection()==ClassConnection.connectionType.extension) 
    		{
				pumlCode+="--";		//TODO eventuell Pfeile
			}
    		else if (myParsingResult.classConnections.get(i).getConnection()==ClassConnection.connectionType.aggregation) 
    		{
    			pumlCode+="o--";	//TODO eventuell Richtung aendern
			}
    		else 
    		{
    			pumlCode+="*--";	//TODO eventuell Richtung aendern
			}
    		pumlCode+=myParsingResult.classes.get(to);
    		pumlCode+="%";
		}
    	pumlCode+="@enduml";
    
	return pumlCode;
    }

    /**
     * Speichert den plantUML-Code in eine Datei
     * @param myParsingResult Ergebnisse des Parsens
     * @param filePath Pfad an den die Datei gespeichert werden soll
     */
    public void savePUMLtoFile(ParsingResult myParsingResult, String filePath)
    {
    	int from;
    	int to;
    	try 
    	{
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filePath)));
			bw.write("@startuml");
			bw.newLine();
	    	for (int i = 0; i < myParsingResult.classes.size(); i++) 
	    	{
	    		bw.write("class ");
	    		bw.write(myParsingResult.classes.get(i));
	    		bw.newLine();
			}
	    	for (int i = 0; i < myParsingResult.classConnections.size(); i++) 
	    	{
	    		from=myParsingResult.classConnections.get(i).getFrom();
	    		to=myParsingResult.classConnections.get(i).getTo();
	    		bw.write(myParsingResult.classes.get(from));
	    		if (myParsingResult.classConnections.get(i).getConnection()==ClassConnection.connectionType.extension) 
	    		{
	    			bw.write("--");		//TODO eventuell Pfeile
				}
	    		else if (myParsingResult.classConnections.get(i).getConnection()==ClassConnection.connectionType.aggregation) 
	    		{
	    			bw.write("o--");	//TODO eventuell Richtung aendern
				}
	    		else 
	    		{
	    			bw.write("*--");	//TODO eventuell Richtung aendern
				}
	    		bw.write(myParsingResult.classes.get(to));
	    		bw.newLine();
			}
	    	bw.write("@enduml");
	    	bw.flush();
	    	bw.close();
		} 
    	catch (Exception e) 
    	{
			e.printStackTrace(); 	//is geil aber nur fuer DEBUGEN
		}
    }
    
    /**
     * Erzeugt ein PlantUML-Diagramm aus der plantUML-Code-Datei am übergebenen Pfad
     * @param filePath Pfad an der die plantUML-Code-Datei liegt
     */
    public void createPlantUML(String filePath, String pumlCode)
    {	
    	OutputStream png=filePath;
    	SourceStringReader reader = new SourceStringReader(pumlCode);
    	try {
			String desc = reader.outputImage(png).getDescription();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
