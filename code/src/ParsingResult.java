
import java.util.*;

/**
 * Datenstruktur welche die Ergebnisse des Parsens enthält
 */
public class ParsingResult
{

    public ArrayList<String> classes;
    public ArrayList<ClassConnection> classConnections;

    /**
     * Konstruktor
     */
    public ParsingResult(ArrayList<String> classes, ArrayList<ClassConnection> classConnections)
    {
	this.classes = classes;
	this.classConnections = classConnections;
    }

    public ArrayList<String> getClasses()
    {
        return classes;
    }

    public void setClasses(ArrayList<String> classes)
    {
        this.classes = classes;
    }

    public ArrayList<ClassConnection> getClassConnections()
    {
        return classConnections;
    }

    public void setClassConnections(ArrayList<ClassConnection> classConnections)
    {
        this.classConnections = classConnections;
    };
    
    

}
