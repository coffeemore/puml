
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

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
     * @param parsedData - xml Eingabe Dokument
     * @return plantUML-Code zur Erzeugung in OutputPUML als xmlDoc
     * @throws ParserConfigurationException
     */

    public Document createDiagram(Document parsedData, String epClass, String epMethod)
	    throws ParserConfigurationException
    {
	// neues Dokument, das seqDiagramm Informationen enthalten wird
	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	Document seqDiagramm = docBuilder.newDocument();

	Element root = seqDiagramm.createElement("parsed");
	seqDiagramm.appendChild(root);
	Element seq = seqDiagramm.createElement("sequencediagramm");
	root.appendChild(seq);

	listClasses(parsedData, seqDiagramm, seq);

	Element entrypoint = seqDiagramm.createElement("entrypoint");
	seq.appendChild(entrypoint);
	Element epClass1 = seqDiagramm.createElement("class");
	epClass1.setTextContent(epClass);
	entrypoint.appendChild(epClass1);
	Element epMethod1 = seqDiagramm.createElement("method");
	epMethod1.setTextContent(epMethod);
	entrypoint.appendChild(epMethod1);

	parsedData = deleteInstancesNotInMethodcalls(parsedData);
	listMethoddef(parsedData, seqDiagramm, seq);

	addClassesToInstances(parsedData, seqDiagramm);
	addType(parsedData, seqDiagramm, seq, epClass);
	xmlHM.writeDocumentToConsole(seqDiagramm);

	return seqDiagramm;
    }

    /**
     * Die Klassen werden aus parsedData übernommen und in seqDiagramm aufgelistet
     * 
     * @param parsedData  - xml Eingabe Dokument
     * @param seqDiagramm - Document für OutputPuml
     * @param seq         - Kindelement von root
     */
    private void listClasses(Document parsedData, Document seqDiagramm, Element seq)
    {
	Element classes = seqDiagramm.createElement("classes");
	seq.appendChild(classes);
	// alle Klassen aus parsedData werden in eine Liste geschrieben
	NodeList cList = parsedData.getElementsByTagName("classdefinition");
	for (int i = 0; i < cList.getLength(); i++)
	{
	    Node cNode = cList.item(i);
	    if (cNode.getNodeType() == Node.ELEMENT_NODE)
	    {
		Element classEl = (Element) cNode;
		// in Tag <name> gespeicherter Text wird als Klassenname übernommen
		String cName = classEl.getElementsByTagName("name").item(0).getTextContent();
		Element entry = seqDiagramm.createElement("entry");
		entry.setTextContent(cName);
		classes.appendChild(entry);
	    }
	}
    }

    /**
     * Die Methode kopiert aus parsedData alle Methoddefinitions von Klassen in
     * seqDiagramm
     * 
     * @param parsedData  - xml Eingabe Dokument
     * @param seqDiagramm - Document für OutputPuml
     * @param seq         - Kindelement von root
     */
    private void listMethoddef(Document parsedData, Document seqDiagramm, Element seq)
    {
	// alle Methoden aus parsedData werden in die Liste geschrieben
	NodeList mList = parsedData.getElementsByTagName("methoddefinition");
	for (int i = 0; i < mList.getLength(); i++)
	{
	    Node mNode = mList.item(i);
	    if (mNode.getNodeType() == Node.ELEMENT_NODE)
	    {
		Element mParent = (Element) mNode.getParentNode();
		// wenn die Methode zu einer Klasse gehört, wird sie kopiert
		if (mParent.getTagName().equals("classdefinition"))
		{
		    seq.appendChild(seqDiagramm.importNode(mNode, true));
		}
	    }
	}
    }

    /**
     * Instanz: von welcher Klasse? im methodcall ein Tag instanz -> Klassentag +
     * -name muss reingenommen werden -> Instanzenliste anlegen
     * 
     * Funktion fügt im seqDiagramm innerhalb der methodcalls das passende class-Tag
     * hinzu, sofern ein instance-Tag existiert
     * 
     * param: parsedData - Quell-Dokument seqDiagramm - das generierte Dokument, in
     * dem die Instanzen eingefügt werden sollen
     * 
     * @throws XPathExpressionException
     */

    private void addClassesToInstances(Document parsedData, Document seqDiagramm)
    {
//	NodeList list = xmlHM.listChildnodeswithName(parsedData, "instance"); 

	ArrayList<ArrayList<String>> instanceList = createInstanceList(parsedData);

	NodeList methodcalls = seqDiagramm.getElementsByTagName("methodcall");
	for (int i = 0; i < methodcalls.getLength(); i++)
	{
	    NodeList mchildnodes = methodcalls.item(i).getChildNodes();
	    for (int j = 0; j < mchildnodes.getLength(); j++)
	    {
		if (mchildnodes.item(j).getNodeName().equals("instance"))
		{
		    String iname = mchildnodes.item(j).getTextContent();
		    // wenn Instanz in InstanzenListe vorhanden
		    String cname = findClassofInstance(instanceList, iname);
		    if (!cname.equals(""))
		    {

			Node classTag = seqDiagramm.createElement("class");
			classTag.setTextContent(cname);
			methodcalls.item(i).appendChild(classTag);

		    }

		}

	    }
	}

    }

    /**
     * Funktion erstellt eine Liste aller im Document parsedData vorkommenden
     * Klassen mit ihren jeweiligen Instanzen param: parsedData return: instanceList
     * 
     * @param parsedData
     * @return instanceList
     */
    private ArrayList<ArrayList<String>> createInstanceList(Document parsedData)
    {

	ArrayList<ArrayList<String>> instanceList = new ArrayList<ArrayList<String>>(); // Liste mit den Klassen und
											// ihren Instanzen
	// für jede Classdefinition in parsed Data eine Liste, darin auch die Instanzen
	// dieser Klasse vermerken

	NodeList cList = parsedData.getElementsByTagName("classdefinition");
	// index i : Klasenliste
	// index j: Unterknoten der Klasseneinträge

	// alle Klassen werden durchgegangen
	for (int i = 0; i < cList.getLength(); i++)
	{

	    instanceList.add(i, new ArrayList<String>());
	    NodeList cuList = cList.item(i).getChildNodes();// Liste aller Unterknoten v. Classdefinition
	    // alle Unterknoten der Klassen werden durchgegangen
	    // durch if-Bed. wird gewährleistet, dass der Name auch dann eingefügt wird,
	    // wenn er nicht der erste Unterknoten ist
	    for (int j = 0; j < cuList.getLength(); j++)
	    {
		if (cuList.item(j).getNodeName().equals("name"))
		{

		    String cname = cuList.item(j).getTextContent();
		    instanceList.get(i).add(0, cname); // Klassennamen werden der cList hinzugefügt
		}
	    }

	}
	// alle Klassen werden durchgegangen
	for (int i = 0; i < cList.getLength(); i++)
	{
	    NodeList cuList = cList.item(i).getChildNodes();// Liste aller Unterknoten v. Classdefinition
	    // alle Unterknoten der Klassen werden durchgegangen
	    for (int j = 0; j < cuList.getLength(); j++)
	    {
		if (cuList.item(j).getNodeName().equals("instance"))
		{
		    // iname = Instanzenname
		    // cname = Klassenname
		    NodeList ciList = cuList.item(j).getChildNodes();
//		 String iname = ciList.item(0).getTextContent();
//		 String cname = ciList.item(1).getTextContent();
		    String iname = new String();
		    String cname = new String();
		    for (int k = 0; k < ciList.getLength(); k++)
		    {
			if (ciList.item(k).getNodeName().equals("name"))
			{
			    iname = ciList.item(k).getTextContent();
			}
			if (ciList.item(k).getNodeName().equals("class"))
			{
			    cname = ciList.item(k).getTextContent();
			}
		    }
//		  instanceList =  addToInstanceList(instanceList, "Instanzenname", "Class1");
		    instanceList = addToInstanceList(instanceList, iname, cname);

		}
	    }
	}
//	listArrayList(instanceList);

	return instanceList;
    }

    // Fügt den Instanzennamen in die Instanzenliste ein
    private ArrayList<ArrayList<String>> addToInstanceList(ArrayList<ArrayList<String>> instanceList, String iname,
	    String cname)
    {
	for (int i = 0; i < instanceList.size(); i++)
	{
	    String classname = instanceList.get(i).get(0);
	    if (cname.equals(classname))
	    {
		instanceList.get(i).add(iname);
		return instanceList;
	    }
	}
	return instanceList;

    }

    private String findClassofInstance(ArrayList<ArrayList<String>> instanceList, String iname)
    {
	for (int i = 0; i < instanceList.size(); i++)
	{
	    // es werden nur die Klassen betrachtet, die auch mindestens eine Instanz haben
	    if (!(instanceList.get(i).size() == 1))
	    {
		for (int j = 0; j < instanceList.get(i).size(); j++)
		{

		    if (instanceList.get(i).get(j).equals(iname))
		    {
			String cname = instanceList.get(i).get(0);
			return cname;
		    }
		}
	    }
	}
	return "";

    }

    /**
     * Die Methodcalls werden mit Type-Tags versehen
     * 
     * @param parsedData  - xml Eingabe Dokument
     * @param seqDiagramm - Document für OutputPuml
     * @param seq         - Kindelement von root
     * @param epClass     - übergebener Entrypoint
     */
    private void addType(Document parsedData, Document seqDiagramm, Element seq, String epClass)
    {
	// Liste mit allen Klassen und ihren zugeordneten Methoden
	ArrayList<ArrayList<String>> classesWithMethodsList = new ArrayList<ArrayList<String>>();
	createList(parsedData, classesWithMethodsList);

	// Liste für bereits aufgerufene Methoden
	ArrayList<String> calledMethodsList = new ArrayList<String>();
	// alle Methoddefinitions in SeqDiagram
	NodeList seqMethodDefList = seqDiagramm.getElementsByTagName("methoddefinition");

	// in jeder Methoddefinition wird nach Methodcalls gesucht
	for (int m = 0; m < seqMethodDefList.getLength(); m++)
	{
	    Node seqMethodDefNode = seqMethodDefList.item(m);
	    if (seqMethodDefNode.getNodeType() == Node.ELEMENT_NODE)
	    {
		Element seqMethodDefEl = (Element) seqMethodDefNode;
		NodeList seqMethodCallList = seqMethodDefEl.getElementsByTagName("methodcall");
		for (int n = 0; n < seqMethodCallList.getLength(); n++)
		{
		    Node seqMethodCallNode = seqMethodCallList.item(n);
		    if (seqMethodCallNode.getNodeType() == Node.ELEMENT_NODE)
		    {
			Element seqMethodCallEl = (Element) seqMethodCallNode;
			// aktuell aufgerufene Methode
			String calledMethod = seqMethodCallEl.getElementsByTagName("method").item(0).getTextContent();

			/**
			 * type - handled
			 */
			Element type = seqDiagramm.createElement("type");
			int a = 0;
			// alle bisher aufgerufenen Methoden werden mit der aktuell aufgerufenen
			// verglichen
			for (int i = 0; i < calledMethodsList.size(); i++)
			{
			    String calledEl = calledMethodsList.get(i);
			    if (calledMethod.equals(calledEl) && a == 0
				    && (seqMethodCallEl.getElementsByTagName("type").item(0) == null))
			    {
				type.setTextContent("handled");
				seqMethodCallEl.appendChild(type);
				a++;
			    }
			}
			calledMethodsList.add(calledMethod);

			/**
			 * type - unknown
			 */
			int b = 0;
			int c = 0;
			// alle vorhandenen Methoden werden mit der aktuell aufgerufenen verglichen
			for (int i = 0; i < classesWithMethodsList.size(); i++)
			{
			    // b speichert die Anzahl aller Klassen und Methoden
			    b += classesWithMethodsList.get(i).size();
			    for (int j = 0; j < classesWithMethodsList.get(i).size(); j++)
			    {
				// c wird hochgesetzt, wenn die calledMethod mit keinem Eintrag in der Liste
				// übereinstimmt
				if (!calledMethod.equals(classesWithMethodsList.get(i).get(j)))
				{
				    c++;
				}
			    }
			}
			if (b == c)
			{
			    type.setTextContent("unknown");
			    seqMethodCallEl.appendChild(type);
			}

			/**
			 * type - recursive
			 */
			int d = 0;
			String currentMethod = seqMethodDefEl.getElementsByTagName("name").item(0).getTextContent();

			if (calledMethod.equals(currentMethod))
			{
			    // Test, ob instance-Tag oder class-Tag vorhanden
			    if ((seqMethodCallEl.getElementsByTagName("instance").item(0) == null)
				    && (seqMethodCallEl.getElementsByTagName("class").item(0) == null))
			    {
				// kein instance-Tag oder class-Tag
				for (int i = 0; i < classesWithMethodsList.size(); i++)
				{
				    // Klasse des Entrypoints wird gesucht
				    if (classesWithMethodsList.get(i).get(0).equals(epClass))
				    {
					for (int j = 0; j < classesWithMethodsList.get(i).size(); j++)
					{
					    // die Methoden des Entrypoints werden mit der aktuell aufgerufenen Methode
					    // verglichen
					    if (calledMethod.equals(classesWithMethodsList.get(i).get(j)) && (d == 0))
					    {
						type.setTextContent("recursive");
						seqMethodCallEl.appendChild(type);
						d++;
					    }
					}
				    }
				}
			    }
			} else
			{
			    // Funktion zur Prüfung verschachtelter Rekursion
			    recursiveLoop(type, currentMethod, seqMethodDefList, m);
			}
		    }
		}
	    }
	}
    }

    /**
     * verschachtelte Rekursion wird geprüft
     * 
     * @param type             - Element von Methodcall
     * @param currentMethod    - aktuell aufgerufene Methode
     * @param seqMethodDefList - Methoddefinition-Liste
     * @param m                - Index der Stelle in der Methoddefinition-Liste
     */
    private void recursiveLoop(Element type, String currentMethod, NodeList seqMethodDefList, int m)
    {
	// die Methodcalls der aufgerufenen Methoden werden auf Rekursivität geprüft
	if (m < seqMethodDefList.getLength())
	{
	    Node defNode = seqMethodDefList.item(m);
	    if (defNode.getNodeType() == Node.ELEMENT_NODE)
	    {
		Element defEl = (Element) defNode;
		NodeList callList = defEl.getElementsByTagName("methodcall");
		for (int j = 0; j < callList.getLength(); j++)
		{
		    Node callNode = callList.item(j);
		    if (callNode.getNodeType() == Node.ELEMENT_NODE)
		    {
			Element callEl = (Element) callNode;
			String called = callEl.getElementsByTagName("method").item(0).getTextContent();
			// die aktuell behandelte Methode wird mit den Methoden der Methodcalls
			// verglichen
			if (called.equals(currentMethod))
			{
			    type.setTextContent("recursive");
			    callEl.appendChild(type);
			} else
			{
			    m++;
			    recursiveLoop(type, currentMethod, seqMethodDefList, m);
			}
		    }
		}
	    }
	}
    }

    /**
     * eine Liste mit allen Klassen und ihren Methoden wird erstellt
     * 
     * @param parsedData             - xml Eingabe Dokument
     * @param classesWithMethodsList - Liste mit allen Klassen und ihren Methoden
     */
    private void createList(Document parsedData, ArrayList<ArrayList<String>> classesWithMethodsList)
    {
	NodeList mList = parsedData.getElementsByTagName("methoddefinition");
	NodeList cList = parsedData.getElementsByTagName("classdefinition");
	for (int i = 0; i < cList.getLength(); i++)
	{
	    // jede Klasse bekommt ihre eigene ArrayList
	    classesWithMethodsList.add(i, new ArrayList<String>());
	    Node cNode = cList.item(i);
	    if (cNode.getNodeType() == Node.ELEMENT_NODE)
	    {
		Element classEl = (Element) cNode;
		String tmpClass = classEl.getElementsByTagName("name").item(0).getTextContent();
		classesWithMethodsList.get(i).add(tmpClass);

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
			    classesWithMethodsList.get(i).add(tmpMethod);
			}
		    }
		}
	    }
	}
    }

    public Document deleteInstancesNotInMethodcalls(Document parsedData)
    {
	// mList = Liste der Methoddefinitions
	try
	{
	    NodeList iList = xmlHM.getList(parsedData, "//instance");
	    for (int i = 0; i < iList.getLength(); i++)
	    {
//		System.out.println("Klasseninstanz gefunden: ");
//		if (xmlHM.hasChildwithName(iList.item(i), "name"))
//		{
//		    System.out.println(xmlHM.getChildwithName(iList.item(i), "name").getTextContent());
//		}
//		System.out.println("Parent Node:");
//		System.out.println(iList.item(i).getParentNode().getNodeName());
		if (iList.item(i).getParentNode().equals(null))
		{
		    System.out.println("kein ParentNode");
		}
		if (!(iList.item(i).getParentNode().getNodeName().equals("methodcall")
			|| iList.item(i).getParentNode().getNodeName().equals("classdefinition")))
		{
		    iList.item(i).getParentNode().removeChild(iList.item(i));
//		    System.out.println("Instanz-Knoten entfernt");
		}
	    }
	} catch (Exception e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return parsedData;
    }

    public void listArrayList(ArrayList<ArrayList<String>> list2)
    {
	for (int i = 0; i < list2.size(); i++)
	{
	    System.out.println(list2.get(i));
	}
    }
}
