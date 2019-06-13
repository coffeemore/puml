import java.util.logging.Logger;
import java.util.logging.XMLFormatter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;


public class LogMain  {

	//Konstruktor
	public LogMain() {
		ConsoleHandler con = new ConsoleHandler();
		SimpleDateFormat timelog = new SimpleDateFormat("dd-MM-yyyy_HH-mm");
		Date dlog = new Date();
		FileHandler xml=null;
		home=System.getProperty("user.home");
		File pumlDir = new File(home+"/PUMLlog");
		String path = home+"/PUMLlog/"+timelog.format(dlog)+"_PUMLlog.xml"
				+ "";
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
	
		xml.setFormatter(new XMLFormatter());
		con.setLevel(Level.ALL);
		log.addHandler(xml);
		log.addHandler(con);
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		      public void run() {
		    	File xmlf = new File (path);
		    	try {
					deleteEmptyLog(xmlf);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		      }
		    }));
	}

	public void deleteEmptyLog(File xmlf) throws IOException{
		FileReader fr = new FileReader(xmlf);
		BufferedReader br = new BufferedReader(fr);
		String empty = br.readLine();
		br.close();
		fr.close();
		if (xmlf.exists()) 
		{
			if(empty==null) 
			{
				xmlf.deleteOnExit();
			}
		}
	}
	public String home; //Zur Speicherung des jeweiligen home directories des Systems
	private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	public Logger getLog() {
		return log;
	}
}