
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
/**
 * @author Leo Rauschke, Elisabeth Schuster
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
    public Document createDiagram(Document parsedData, String epClass, String epMethod) throws ParserConfigurationException
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
	
	Element epClass1 = seqDiagramm.createElement("class");
	epClass1.setTextContent(epClass);
	
	Element epMethod1 = seqDiagramm.createElement("method");
	epMethod1.setTextContent(epMethod);
	
	entrypoint.appendChild(epMethod1);
	entrypoint.appendChild(epClass1);
	
	
	seq.appendChild(entrypoint);
	
	Element root1 = parsedData.getDocumentElement();
	System.out.println(root1.getTagName());
	
	listMethoddef(parsedData, seqDiagramm, seq);
	listAllNodes(root);

	//System.out.println(root1.getTagName());
	
	listMethoddef(parsedData, seqDiagramm, seq);
	listAllNodes(root);
	

	
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
	    //System.out.println("Current Node: " + cNode.getNodeName());
	    if(cNode.getNodeType() == Node.ELEMENT_NODE) {
		//in Tag <name> gespeicherter Text wird als Klassenname übernommen
		Element classEl = (Element) cNode;
		//String cName = cNode.getFirstChild().getTextContent();
		String cName = classEl.getElementsByTagName("name").item(0).getTextContent();
		Element entry = seqDiagramm.createElement("entry");
		//Name der Klasse aus paseddata wird übernommen
		entry.setTextContent(cName);
		//System.out.println("TagName: "+entry.getTagName());
		//-->entry
		//System.out.println("TextContent: "+entry.getTextContent());
		//-->Class1
		classes.appendChild(entry);
	    }
	}
    }

    private void listMethoddef(Document parsedData, Document seqDiagramm, Element seq) {
	//Schleife, die Methoden auflistet
	//kommt man so in die Unterpunkte <methoddefinition>?

	/**
	 * Element method = seqDiagramm.createElement("methoddefinition");
	 * seq.appendChild(method);
	 */
	
	/*methoddefinition*/
//	Element method = seqDiagramm.createElement("methoddefinition");
	
	
//	seq.appendChild(method);
	
	
	//types (zB rekursiv angeben bei methodcall)
	// type 
	//	recursive  <-- ruft sich selber auf
	//	unknown	<-- ruft unbekannte Methode auf
	//	handled  <-- ruft bereits aufgerufene Methoden
	

	
	//Liste mit den einzelnen Knoten der Methoden
	NodeList mList = parsedData.getElementsByTagName("methoddefinition");
	
	for(int i = 0; i < mList.getLength(); i++) {
	    //jeder einzelner Knoten wird herausgenommen
	    Node mNode = mList.item(i);
	    
	    if(mNode.getNodeType() == Node.ELEMENT_NODE) {

		//Knoten wird zu Element
		
	//	method.appendChild(seqDiagramm.importNode(mNode, true));
		
		seq.appendChild(seqDiagramm.importNode(mNode, true));
		
		seq.appendChild(seqDiagramm.importNode(mNode, true));
		

		/**String cName = e.getElementsByTagName("name").item(0).getTextContent();
		Element entry = seqDiagramm.createElement("entry");
		entry.setTextContent(cName);
		classes.appendChild(entry);
		*/
	    }
	}

	
	System.out.println();

	/**
	 * unter <methoddefinition> - name und alternatives kopieren
	 * Rekursivität bei methodcalls 
	 */
	
    }
    
    /**
     * Bug: Fehler bei Textausgabe -> getTextContent listet allen Content der Unterknoten auf
     * nicht nur der Childs
     * @param root
     */
   
    private void addType(Document parsedData, Document seqDiagramm, Element seq) {
	NodeList mNode = parsedData.getElementsByTagName("methoddefinition");
	NodeList cNode = parsedData.getElementsByTagName("classdefinition");
    }
   
    
    /**
     * Bug: Fehler bei Textausgabe -> getTextContent listet allen Content der Unterknoten auf
     * nicht nur der Childs
     * @param root
     */
    public void listAllNodes(Element root) 
    {
	if(root.hasChildNodes()) 
	{
	    NodeList list = root.getChildNodes();
	    for(int i=0; i<list.getLength();i++) 
	    {
		Node node = list.item(i);
		
		if(node.getNodeType()==Node.ELEMENT_NODE) 
		{
		    Element e = (Element) node;
		    String m = e.getTagName();
		    System.out.println(m);
		    //System.out.println(e.getElementsByTagName(m).item(0).getTextContent());
		    listAllNodes(e);
		    System.out.println("/"+e.getTagName());
		}
	    }
	}
    }
}

