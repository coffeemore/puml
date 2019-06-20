import java.util.logging.Logger;
import java.util.logging.XMLFormatter;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.io.File;
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
		//File pumlDir = new File("testfolder/tempData/PUMLlog");
		String path =""+timelog.format(dlog)+"_PUMLlog.xml";
		/*if (!pumlDir.exists()) 
		{

		    try{
		        pumlDir.mkdir();
		    } 
		    catch(SecurityException se){
		        //handle it
		    }        
		}*/
		try {
			xml = new FileHandler(path, true); //true = FileHandler darf immer weiter in die Datei schreiben
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.setLevel(Level.ALL); //Alles wird geloggt
		log.setUseParentHandlers(false); //deaktiviert default Handler
		xml.setFormatter(new XMLFormatter());
		con.setLevel(Level.ALL);
		log.addHandler(xml);
		log.addHandler(con);
		//File xmlf = new File(path);
		/*Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		      public void run() 
		      {
		    	try 
		    	{
					deleteEmptyLog(path);
				}
				catch (XPathExpressionException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		      }
		    }));
		    */
	}

	/*public void deleteEmptyLog(String path) throws XPathExpressionException
	{
	    XmlHelperMethods helper = new XmlHelperMethods();
		Document doc = helper.getDocumentFrom(path);
	    XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = xpath.compile("/log/*");
		NodeList list =  (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		if(list.item(0).getNodeName().equals(null)) 
		{
			((File) doc).deleteOnExit();
			System.out.println("Log gelöscht");
		}
	}*/
	private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	public Logger getLog()
	{
		return log;
	}
}