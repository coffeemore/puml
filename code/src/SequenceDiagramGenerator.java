
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
/**
 * 
 * @author Klasse zur Erzeugung von Sequenzdiagrammendaten
 */
public class SequenceDiagramGenerator {
	
	/**
     * Konstruktor
     */
    public SequenceDiagramGenerator()
    {
	
    }
    
    /**
     * Erstellt den plantUML-Code aus geparstem xmlDocument
     * 
     * @param parsedData xml Eingabe Dokument
     * @return plantUML-Code zur Erzeugung in OutputPUML als xmlDoc
     * @throws ParserConfigurationException 
     */
    public Document createDiagram(Document parsedData) throws ParserConfigurationException
    {
	//neues Dokument, das SeqDiagramm Informationen enthalten wird
	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	Document seqDiagramm = docBuilder.newDocument();
	
	Element root = seqDiagramm.createElement("parsed");
	seqDiagramm.appendChild(root);
	Element seq = seqDiagramm.createElement("sequencediagramm");
	root.appendChild(seq);
	
	listClasses(parsedData, seqDiagramm, seq);
	
	//unklar: woher bekommen wir den Entrypoint?
	Element entrypoint = seqDiagramm.createElement("entrypoint");
	seq.appendChild(entrypoint);
	
	listMethoddef(parsedData, seqDiagramm, seq);
	
	return null;
    }
    
    
    private void listClasses(Document parsedData, Document seqDiagramm, Element seq) {
	
	Element classes = seqDiagramm.createElement("classes");
	seq.appendChild(classes);
	//Schleife, die entries auflistet
	//in parsedData: classdefinition-name
	NodeList cList = parsedData.getElementsByTagName("classdefinition");
	//alle Einträge der Liste werden durchgegangen
	for(int i = 0; i < cList.getLength(); i++) {
	    Node cNode = cList.item(i);
	    if(cNode.getNodeType() == Node.ELEMENT_NODE) {
		//in Tag <name> gespeicherter Text wird als Klassenname übernommen
		String cName = cNode.getFirstChild().getTextContent();
		Element entry = seqDiagramm.createElement("entry");
		entry.setTextContent(cName);
		classes.appendChild(entry);
	    }
	}
    }

    private void listMethoddef(Document parsedData, Document seqDiagramm, Element seq) {
	//Schleife, die Methoden auflistet
	//kommt man so in die Unterpunkte <methoddefinition>?
	NodeList mList = parsedData.getElementsByTagName("methoddefinition");
	
	/**
	 * unter <methoddefinition> - name und alternatives kopieren
	 * Rekursivität bei methodcalls 
	 */
	
	//Element methoddef = seqDiagramm.createElement("methoddefinition");
	//seq.appendChild(methoddef);
    }
}
