
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.*;
import java.util.Vector;
import java.util.regex.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author Klasse, die den Parser fï¿½r Java implementiert
 */
public class ParserJava implements ParserIf {
	Document document;


	/**
	 * Konstruktor
	 */
	public ParserJava() {

	}

	class TokenResult {

		public TokenResult(int foundToken, String data) {
			super();
			this.foundToken = foundToken;
			this.data = data;
		}

		private int foundToken;
		private String data = "";

		public int getFoundToken() {
			return foundToken;
		}

		public void setFoundToken(int foundToken) {
			this.foundToken = foundToken;
		}

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}

	}

	public TokenResult goToTokenWithName(String[] source, String[] name) {
		String part = ""; 														// Variable wird genutztum zB Namen zu speichern
		boolean found = false;
		int foundNameIndex = 0;
		// Erstes/Erste Zeichen werden auf die übertragenen Tokens überprüft
		for (int i = 0; i < name.length; i++) {
			if (source[0].substring(0, name[i].length()).equals(name[i])) {
				found = true;
				foundNameIndex = i;
			}
		}
		while (!found) {

			part = part + source[0].substring(0, 1); //erstes Zeichen wird in Part geschrieben
			source[0] = source[0].substring(1);	// erstes Zeichen wird aus dem Sourcecode entfernt
			for (int i = 0; i < name.length; i++) {
				if (source[0].substring(0, name[i].length()).equals(name[i])) {
					found = true;
					foundNameIndex = i;
				}
			}
		}

		return new TokenResult(foundNameIndex, part); // Rückgabe welcher Token gefunden wurde und den Inhalt zwischen den Tokens (zB einen Klassennamen)

	}

	/**
	 * Entfernt Kommentare aus übergebenem String
	 * 
	 * @param sourcece übergebener String aus dem Kommentare entfernt werden
	 * @return XMl-Dokument
	 * @throws ParserConfigurationException
	 */
	public void buildTree(String sourcec) throws ParserConfigurationException 
	{
		// Erstellen des Dokuments
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

		document = documentBuilder.newDocument();

		// root element
		Element root = document.createElement("source");
		document.appendChild(root);
		// System.out.println(sourcec);
		String compString;
		String[] sourceArray = new String[1];
		sourceArray[0] = sourcec;

		try {
			while (!sourceArray[0].isEmpty()) {
				sourceArray[0] = sourceArray[0].trim();	//Entfernen von Leerzeichen und Zeilenumbrüchen
				boolean done = false;
				compString = "\"";	//Entfernen von Strings zur Vermeidung von Problemen beim Entfernen von Kommentaren
				if (sourceArray[0].substring(0, compString.length()).equals(compString)) {
					sourceArray[0] = sourceArray[0].substring(compString.length());
					sourceArray[0] = sourceArray[0].trim();
					String[] nameArray = new String[2];
					nameArray[0] = "\\\"";
					nameArray[1] = "\"";

					TokenResult res;
					//Gehe durch den Sourcecode, bis kein String mehr gefunden wurde
					do {
						res = goToTokenWithName(sourceArray, nameArray);
						// System.out.println("@res: " + res.getData());
						if (res.getFoundToken() == 0) {
							sourceArray[0] = sourceArray[0].substring(1);
						}
					} while (res.getFoundToken() != 1);
					sourceArray[0] = sourceArray[0].substring(1); //Entfernen des ersten Zeichens
					done = true;
				}
				;
				//Entfernen von Zeilen-Kommentaren
				compString = "//";
				if (sourceArray[0].substring(0, compString.length()).equals(compString)) {
					sourceArray[0] = sourceArray[0].substring(compString.length());
					sourceArray[0] = sourceArray[0].trim();
					String[] nameArray = new String[1];
					nameArray[0] = "\n";
					goToTokenWithName(sourceArray, nameArray);
					done = true;
				}
				;
				
				//Entfernen von Block-Kommentaren
				compString = "/*";
				if (sourceArray[0].substring(0, compString.length()).equals(compString)) {
					sourceArray[0] = sourceArray[0].substring(compString.length());
					sourceArray[0] = sourceArray[0].trim();
					String[] nameArray = new String[1];
					nameArray[0] = "*/";
					goToTokenWithName(sourceArray, nameArray);
					done = true;
				}
				;
				
				compString = "import ";
				if (sourceArray[0].substring(0, compString.length()).equals(compString)) {
					sourceArray[0] = sourceArray[0].substring(compString.length());
					sourceArray[0] = sourceArray[0].trim();
					String[] nameArray = new String[1];
					nameArray[0] = ";";
					goToTokenWithName(sourceArray, nameArray);
					done = true;
				}
				;

				compString = "class ";
				if (sourceArray[0].substring(0, compString.length()).equals(compString)) {
					sourceArray[0] = sourceArray[0].substring(compString.length());
					sourceArray[0] = sourceArray[0].trim();
					String[] nameArray = new String[3];
					nameArray[0] = "{";
					nameArray[1] = "extends";
					nameArray[2] = "implements";
					TokenResult res = goToTokenWithName(sourceArray, nameArray);
					String className = res.getData();
					className = className.strip();
					System.out.println("@className: " + className);
					Element classElement = document.createElement("class");
					classElement.appendChild(document.createTextNode(className));
					root.appendChild(classElement);
					compString = "extends ";
					if (sourceArray[0].substring(0, compString.length()).equals(compString)) {
						sourceArray[0] = sourceArray[0].substring(compString.length());
						sourceArray[0] = sourceArray[0].trim();
						nameArray = new String[2];
						nameArray[0] = "{";
						nameArray[1] = "implements";
						res = goToTokenWithName(sourceArray, nameArray);
						String extendsName = res.getData();
						extendsName = extendsName.strip();
						System.out.println("@extendsName: " + extendsName);
					}
					compString = "implements ";
					if (sourceArray[0].substring(0, compString.length()).equals(compString)) {
						sourceArray[0] = sourceArray[0].substring(compString.length());
						compString = "{";
						do {
							sourceArray[0] = sourceArray[0].trim();
							nameArray = new String[2];
							nameArray[0] = "{";
							nameArray[1] = ",";
							res = goToTokenWithName(sourceArray, nameArray);
							String interfaceName = res.getData();
							interfaceName = interfaceName.strip();
							System.out.println("@implements: " + interfaceName);
						} while (!(sourceArray[0].substring(0, compString.length()).equals(compString)));

					}
					done = true;
				}
				compString = "interface ";
				if (sourceArray[0].substring(0, compString.length()).equals(compString)) {
					sourceArray[0] = sourceArray[0].substring(compString.length());
					sourceArray[0] = sourceArray[0].trim();
					String[] nameArray = new String[2];
					nameArray[0] = "{";
					nameArray[1] = "extends";
					TokenResult res = goToTokenWithName(sourceArray, nameArray);
					String interfaceName = res.getData();
					interfaceName = interfaceName.strip();
					System.out.println("@interfaceName: " + interfaceName);
					Element classElement = document.createElement("class");
					classElement.appendChild(document.createTextNode(interfaceName));
					root.appendChild(classElement);
					compString = "extends ";
					if (sourceArray[0].substring(0, compString.length()).equals(compString)) {
						sourceArray[0] = sourceArray[0].substring(compString.length());
						sourceArray[0] = sourceArray[0].trim();
						nameArray = new String[1];
						nameArray[0] = "{";
						res = goToTokenWithName(sourceArray, nameArray);
						String extendsName = res.getData();
						extendsName = extendsName.strip();
						System.out.println("@extendsName: " + extendsName);
					}
					done = true;
				}
				;
				if (!done) {
					sourceArray[0] = sourceArray[0].substring(1);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();
			StringWriter writer = new StringWriter();

			transformer.transform(new DOMSource(document), new StreamResult(writer));

			String xmlString = writer.getBuffer().toString();
			System.out.println(xmlString); // Print to console or logs
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(document.getTextContent());

	}

	/**
	 * Liest den übergebenen Quellcode ein und parsed die Informationen daraus
	 * 
	 * @param sourceCode Vollständiger Java-Quellcode
	 */
	public void parse(String sourceCode) {
		sourceCode.trim();
		try {
			buildTree(sourceCode);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Liefert die Ergebnisse des Parsens zurueck
	 * 
	 * @return XML Document mit den Ergebnissen des Parsens
	 */
	public Document getParsingResult() {
		
		return document;
	}
}
