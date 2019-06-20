import java.util.logging.Logger;
import java.util.logging.XMLFormatter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;

/**
 * 
 * @author Patrick Otte
 */

public class LogMain  {
	
	//Konstruktor
	public LogMain() 
	{
		log.setUseParentHandlers(false); //deaktiviert default Handler
	}
	
	private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private String path=null;
	
	public Logger getLog()
	{
		return log;
	}
	public String getPath() 
	{
		return path;
	}
	public void setPath(String setpath)
	{
		path=setpath;
	}
	
	
	public void startLogging(String dirpath, Boolean console)
	{
		//Setup  des XML-Loggers
		SimpleDateFormat timelog = new SimpleDateFormat("dd-MM-yyyy_HH-mm");
		Date dlog = new Date();
		FileHandler xml=null;
		setPath(dirpath+timelog.format(dlog)+"_PUMLlog.xml");
		try 
		{
			xml = new FileHandler(getPath(), true); //true = FileHandler darf immer weiter in die Datei schreiben
		} 
		catch (SecurityException | IOException e) 
		{
			e.printStackTrace();
			PUMLgenerator.logger.getLog().severe(e.getMessage());
		}
		xml.setFormatter(new XMLFormatter());
		xml.setLevel(Level.ALL);
		log.addHandler(xml);
		
		//Setup Konsolenausgabe
		if(console)
		{
			ConsoleHandler con = new ConsoleHandler();
			con.setLevel(Level.ALL);
			log.addHandler(con);
		}
		/*
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() 
		{
		      public void run() 
		      {
					try 
					{
						deleteEmptyLog(getPath());
					} 
					catch (XPathExpressionException e) 
					{
						// TODO Auto-generated catch block
						PUMLgenerator.logger.getLog().severe(e.getMessage());
						PUMLgenerator.logger.getLog().severe("sollte eig loggen");
						System.out.println("TEESSTTTT");
						e.printStackTrace();
					}
		      }
	    }));
		*/
			    
	}

	/*public void deleteEmptyLog(String path) throws XPathExpressionException
	{
		try 
		{
		    XmlHelperMethods helper = new XmlHelperMethods();
			Document doc = helper.getDocumentFrom(path);
		    XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("/*");
			System.out.println("VORHER");
			NodeList list =  (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			System.out.println("NACHHER?");
			System.out.println(list.getLength());
			if(list.item(0).getNodeName().equals(null)) 
			{
				((File) doc).deleteOnExit();
				//System.out.println("Log gelöscht");
			}
		}
		catch (Exception ex)
		{
			PUMLgenerator.logger.getLog().warning(ex.getMessage());
		}
	}*/
}