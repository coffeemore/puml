
import java.util.ArrayList;
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
public class SequenceDiagramGenerator
{

    XmlHelperMethods xmlHM = new XmlHelperMethods();

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

    // Fall, wenn keiner Übergeben wurde
    // Rekursivität - Klasse und Methode merken, abprüfen ob noch mal aufgerufen ist
    public Document createDiagram(Document parsedData, String epClass, String epMethod)
	    throws ParserConfigurationException
    {
	// neues Dokument, das SeqDiagramm Informationen enthalten wird
	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	Document seqDiagramm = docBuilder.newDocument();

	// ArrayList<Element> list2 = new ArrayList<Element>();

	Element root = seqDiagramm.createElement("parsed");
	seqDiagramm.appendChild(root);
	Element seq = seqDiagramm.createElement("sequencediagramm");
	root.appendChild(seq);

	listClasses(parsedData, seqDiagramm, seq);

	// Entrypoint über Strings festgelegt
	Element entrypoint = seqDiagramm.createElement("entrypoint");
	seq.appendChild(entrypoint);
	Element epClass1 = seqDiagramm.createElement("class");
	epClass1.setTextContent(epClass);
	entrypoint.appendChild(epClass1);
	Element epMethod1 = seqDiagramm.createElement("method");
	epMethod1.setTextContent(epMethod);
	entrypoint.appendChild(epMethod1);

	// Element root1 = parsedData.getDocumentElement();

	listMethoddef(parsedData, seqDiagramm, seq);
	addType(parsedData, seqDiagramm, seq);
	// xmlHM.listAllNodes(root);
	xmlHM.writeDocumentToConsole(seqDiagramm);

	return null;
    }

    private void listClasses(Document parsedData, Document seqDiagramm, Element seq)
    {

	Element classes = seqDiagramm.createElement("classes");
	seq.appendChild(classes);

	NodeList cList = parsedData.getElementsByTagName("classdefinition");
	// alle Einträge der Liste werden durchgegangen
	for (int i = 0; i < cList.getLength(); i++)
	{
	    Node cNode = cList.item(i);
	    // System.out.println("Current Node: " + cNode.getNodeName());
	    if (cNode.getNodeType() == Node.ELEMENT_NODE)
	    {
		// in Tag <name> gespeicherter Text wird als Klassenname übernommen
		Element classEl = (Element) cNode;
		String cName = classEl.getElementsByTagName("name").item(0).getTextContent();
		Element entry = seqDiagramm.createElement("entry");
		// Name der Klasse aus paseddata wird übernommen
		entry.setTextContent(cName);
		classes.appendChild(entry);
	    }
	}
    }

    private void listMethoddef(Document parsedData, Document seqDiagramm, Element seq)
    {
	NodeList mList = parsedData.getElementsByTagName("methoddefinition");

	for (int i = 0; i < mList.getLength(); i++)
	{
	    // jeder einzelner Knoten wird herausgenommen
	    Node mNode = mList.item(i);

	    if (mNode.getNodeType() == Node.ELEMENT_NODE)
	    {
		Element mParent = (Element) mNode.getParentNode();
		if (mParent.getTagName().equals("classdefinition"))
		{
		    seq.appendChild(seqDiagramm.importNode(mNode, true));
		}
	    }
	}
    }

    private void addType(Document parsedData, Document seqDiagramm, Element seq)
    {
	/**
	 * unter methodcall: recursive, unknown, handled
	 */

	// Liste für alle Methoden und Klassen
	NodeList mList = parsedData.getElementsByTagName("methoddefinition");
	NodeList cList = parsedData.getElementsByTagName("classdefinition");
	ArrayList<ArrayList<String>> list1 = new ArrayList<ArrayList<String>>();
	createList(parsedData, mList, cList, list1);

	// Liste für bereits aufgerufene Methoden
	ArrayList<String> calledMethodsList = new ArrayList<String>();
	NodeList seqMethodList = seqDiagramm.getElementsByTagName("methoddefinition");
	for (int m = 0; m < seqMethodList.getLength(); m++)
	{
	    Node seqMethodNode = seqMethodList.item(m);
	    if (seqMethodNode.getNodeType() == Node.ELEMENT_NODE)
	    {
		Element seqMethodEl = (Element) seqMethodNode;
		NodeList seqCallList = seqMethodEl.getElementsByTagName("methodcall");
		for (int n = 0; n < seqCallList.getLength(); n++)
		{
		    Node seqCallNode = seqCallList.item(n);
		    if (seqCallNode.getNodeType() == Node.ELEMENT_NODE)
		    {
			Element seqCallEl = (Element) seqCallNode;
			String called = seqCallEl.getElementsByTagName("method").item(0).getTextContent();
			// handled
			Element type = seqDiagramm.createElement("type");
			int a = 0;
			for (int i = 0; i < calledMethodsList.size(); i++)
			{
			    String calledEl = calledMethodsList.get(i);
			    if (called.equals(calledEl))
			    {
				if (a == 0)
				{
				    type.setTextContent("handled");
				    seqCallEl.appendChild(type);
				    a++;
				}
			    }
			}
			calledMethodsList.add(called);

			// unknown
			int b = 0;
			int c = 0;
			for (int i = 0; i < list1.size(); i++)
			{
			    b += list1.get(i).size();
			    for (int j = 0; j < list1.get(i).size(); j++)
			    {
				if (!called.equals(list1.get(i).get(j)))
				{
				    c++;
				}
			    }
			}
			if (b == c)
			{
			    type.setTextContent("unknown");
			    seqCallEl.appendChild(type);
			}

			// recursive
			/**
			 * list1 benutzen aktuell behandelte Methode vergleicht mit Klassenname der
			 * aktuellen Methode finden und deren Arraylist)
			 * 
			 * ToDo: verschachtelte Rekursion - Lösung finden
			 * ToDo: Klassenzuweisung der Methoden!
			 */
			Element parent = (Element) seqMethodNode.getParentNode().getFirstChild().getFirstChild();
			int d = 0;
			for (int i = 0; i < list1.size(); i++)
			{
			    for (int j = 0; j < list1.get(i).size(); j++)
			    {
				String tried = seqMethodEl.getElementsByTagName("name").item(0).getTextContent();
				if (called.equals(tried) && d==0 /*&& list1.get(i).get(0).equals(parent.getTagName())*/)
				{
				    seqCallEl.removeChild(type);
				    type.setTextContent("recursive");
				    seqCallEl.appendChild(type);
				    d++;
				}
			    }
			}
		    }
		}
	    }
	}
    }

    
    private void addInstances(Document parsedData, Document seqDiagramm, Element seq)
    {

    }

    private void createMElementList(Document parsedData, ArrayList<Element> list2)
    {
	NodeList mList = parsedData.getElementsByTagName("methoddefinition");
	for (int i = 0; i < mList.getLength(); i++)
	{
	    Node mNode = mList.item(i);
	    if (mNode.getNodeType() == Node.ELEMENT_NODE)
	    {
		Element methodEl = (Element) mNode;
		list2.add(methodEl);
	    }
	}

    }

    private void createList(Document parsedData, NodeList mList, NodeList cList, ArrayList<ArrayList<String>> list1)
    {
	/**
	 * gehe jede Klasse durch merke dir den Knotennamen und gehe dann alle Methoden
	 * dieses Knotens durch jede Klasse bekommt ihre eigene Arraylist
	 */
	for (int i = 0; i < cList.getLength(); i++)
	{
	    list1.add(i, new ArrayList<String>());
	    Node cNode = cList.item(i);
	    if (cNode.getNodeType() == Node.ELEMENT_NODE)
	    {
		Element classEl = (Element) cNode;
		String tmpClass = classEl.getElementsByTagName("name").item(0).getTextContent();
		list1.get(i).add(tmpClass);

		for (int j = 0; j < mList.getLength(); j++)
		{
		    Node mNode = mList.item(j);

		    if (mNode.getNodeType() == Node.ELEMENT_NODE
			    && mNode.getParentNode().getNodeName() == "classdefinition")
		    {
			Element methodEl = (Element) mNode;
			Element parentEl = (Element) mNode.getParentNode();
			String tmpParent = parentEl.getElementsByTagName("name").item(0).getTextContent();
			String tmpMethod = methodEl.getElementsByTagName("name").item(0).getTextContent();
			if (tmpParent == tmpClass)
			{
			    list1.get(i).add(tmpMethod);
			}
		    }
		}
	    }
	}
    }

    public void listArrayList(ArrayList<ArrayList<String>> list2)
    {
	for (int i = 0; i < list2.size(); i++)
	{
	    System.out.println(list2.get(i));
	}
    }

    public void listArray(String[][] feld1, NodeList cList, NodeList mList)
    {
	for (int i = 0; i < cList.getLength(); i++)
	{
	    for (int j = 0; j < mList.getLength(); j++)
	    {
		System.out.print(feld1[i][j] + " ");
	    }
	    System.out.println();
	}
    }
}
