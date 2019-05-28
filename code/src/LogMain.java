import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;


public class LogMain  {

	//Konstruktor
	public LogMain() {
		ConsoleHandler con = new ConsoleHandler();
		SimpleDateFormat timelog = new SimpleDateFormat("dd-MM-yyyy_HH-mm");
		Date dlog = new Date();
		FileHandler xml=null;
		home=System.getProperty("user.home");
		File pumlDir = new File(home+"/PUMLlog");
		String path = home+"/PUMLlog/"+timelog.format(dlog)+"_PUMLlog.xml";
		if (!pumlDir.exists()) {

		    try{
		        pumlDir.mkdir();
		    } 
		    catch(SecurityException se){
		        //handle it
		    }        
		}
		try {
			xml = new FileHandler(path, true);
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* logfile wird im User/Home Ordner des jeweiligen Systems gespeichert
		 * true sagt aus, dass der FileHandler weiter in die Datei schreiben darf
		 * ACHTUNG: Datei hat in der Variante kein Größenlimit! 
		 * Kann aber nachträglich eingefügt werden.
		 */
		
		log.setLevel(Level.ALL); //Alles wird geloggt
		log.setUseParentHandlers(false); //deaktiviert default Handler
	
		
		xml.setFormatter(new Formatter(){
			@Override
			public String format(LogRecord record) {
				String ret = "";
				SimpleDateFormat df = new SimpleDateFormat("dd MM yyyy HH:mm");
				Date d = new Date(record.getMillis());
				ret +="DATE: " + df.format(d);
				ret +=" CLASS: " + record.getSourceClassName();
				ret +=" METHOD: " + record.getSourceMethodName();
				ret += "\r\n";
				ret+=record.getLevel();
				if(record.getLevel().intValue() >= Level.WARNING.intValue()) {
					ret+="/ACHTUNG: ";
				}
				ret += " " + this.formatMessage(record);
				ret += "\r\n";
				ret += "\r\n";
				return ret;
			}
		}); 
		con.setLevel(Level.ALL);
		log.addHandler(xml);
		log.addHandler(con);
		//File xmlf = new File (path);
		//deleteEmptyLog(xmlf);
	}

	/*public void deleteEmptyLog(File xmlf){
		try {
			if(isEmpty(xmlf)) {
				xmlf.deleteOnExit();
				System.out.println("Leere Logdatei gelöscht");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isEmpty(File xml) throws IOException 
	{
		FileReader fr = new FileReader(xml);
		BufferedReader br = new BufferedReader(fr);
		String empty = br.readLine();
		br.close();
		fr.close();
		if (xml.exists()) 
		{
			if(empty==null) 
			{
				return true;
			}
		}
		return false;
	}*/
	public String home; //Zur Speicherung des jeweiligen home directories des Systems
	private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	public Logger getLog() {
		return log;
	}
}