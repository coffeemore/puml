
/**
 * 
 * @author Datenstruktur die eine Verbindung zwischen Klassen im UML-Diagram
 *         repräsentiert.
 */
public class ClassConnection
{

    enum connectionType
    {
	extension, composition, aggregation
    }

    private int from;
    private int to;
    private connectionType connection;

    /**
     * Konstruktor
     */
    public ClassConnection()
    {
    };

    /**
     * Set the value of from
     * 
     * @param newVar the new value of from
     */
    public void setFrom(int newVar)
    {
	from = newVar;
    }

    /**
     * Get the value of from
     * 
     * @return the value of from
     */
    public int getFrom()
    {
	return from;
    }

    /**
     * Set the value of to
     * 
     * @param newVar the new value of to
     */
    public void setTo(int newVar)
    {
	to = newVar;
    }

    /**
     * Get the value of to
     * 
     * @return the value of to
     */
    public int getTo()
    {
	return to;
    }

    /**
     * Set the value of connection
     * 
     * @param newVar the new value of connection
     */
    public void setConnection(connectionType newVar)
    {
	connection = newVar;
    }

    /**
     * Get the value of connection
     * 
     * @return the value of connection
     */
    public connectionType getConnection()
    {
	return connection;
    }

    //
    // Other methods
    //

}
