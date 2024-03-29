
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
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
     * @throws IOException
     * @throws SAXException
     * @throws XPathExpressionException
     */

    public Document createDiagram(Document parsedData, String epClass, String epMethod)
	    throws ParserConfigurationException, SAXException, IOException, XPathExpressionException
    {
	// neues Dokument, das seqDiagramm Informationen enthalten wird
	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	Document seqDiagramm = docBuilder.newDocument();

	Element root = seqDiagramm.createElement("parsed");
	seqDiagramm.appendChild(root);
	Element seq = seqDiagramm.createElement("sequencediagram");
	root.appendChild(seq);

	listClasses(parsedData, seqDiagramm, seq, epClass);

	Element entrypoint = seqDiagramm.createElement("entrypoint");
	seq.appendChild(entrypoint);
	Element epClass1 = seqDiagramm.createElement("class");
	epClass1.setTextContent(epClass);
	entrypoint.appendChild(epClass1);
	Element epMethod1 = seqDiagramm.createElement("method");
	epMethod1.setTextContent(epMethod);
	entrypoint.appendChild(epMethod1);

//	parsedData = deleteInstancesNotInMethodcalls(parsedData);
	listMethoddef(parsedData, seqDiagramm, seq, epClass);

	addClassesToInstances(parsedData, seqDiagramm);

	seqDiagramm = deleteInstancesNotInMethodcalls(seqDiagramm);

	addType(parsedData, seqDiagramm, seq, epClass);
	xmlHM.removeComments(root);
	xmlHM.writeDocumentToConsole(seqDiagramm);

	return seqDiagramm;
    }
    
    //nur Klassen, die von der epClass aufgerufen werden

    /**
     * Die Klassen werden aus parsedData übernommen und in seqDiagramm aufgelistet
     * 
     * @param parsedData  - xml Eingabe Dokument
     * @param seqDiagramm - Document für OutputPuml
     * @param seq         - Kindelement von root
     */
    private void listClasses(Document parsedData, Document seqDiagramm, Element seq, String epClass)
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
    
    //nur methoddefs, die in epClass sind

    /**
     * Die Methode kopiert aus parsedData alle Methoddefinitions von Klassen in
     * seqDiagramm
     * 
     * @param parsedData  - xml Eingabe Dokument
     * @param seqDiagramm - Document für OutputPuml
     * @param seq         - Kindelement von root
     */
    private void listMethoddef(Document parsedData, Document seqDiagramm, Element seq, String epClass)
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
	NodeList list = seqDiagramm.getElementsByTagName("methoddefinition");
	for (int i = 0; i < list.getLength(); i++)
	{
	    Node node = list.item(i);
	    if (node.getNodeType() == Node.ELEMENT_NODE)
	    {
		NodeList childs = node.getChildNodes();
		for (int j = 0; j < childs.getLength(); j++)
		{
		    Node child = childs.item(j);
		    if (child.getNodeType() == Node.ELEMENT_NODE
			    && (child.getNodeName().equals("parameters") || child.getNodeName().equals("result")))
		    {
			node.removeChild(child);
		    }
		}
	    }
	}
	// parameters, result
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

    private void addClassesToInstances(Document parsedData, Document seqDiagramm) throws XPathExpressionException
    {
//	NodeList list = xmlHM.listChildnodeswithName(parsedData, "instance"); 

	seqDiagramm = handleLocalInstances(seqDiagramm);

	ArrayList<ArrayList<String>> instanceList = createInstanceList(parsedData);

	NodeList methodcalls = seqDiagramm.getElementsByTagName("methodcall");
	for (int i = 0; i < methodcalls.getLength(); i++)
	{
	    NodeList mchildnodes = methodcalls.item(i).getChildNodes();
	    for (int j = 0; j < mchildnodes.getLength(); j++)
	    {
		if (mchildnodes.item(j).getNodeName().equals("instance"))
		{
		    if (!xmlHM.hasChildwithName(mchildnodes.item(j).getParentNode(), "class"))
		    {
			String iname = mchildnodes.item(j).getTextContent();
			// wenn Instanz in InstanzenListe vorhanden
			String cname = findClassofInstance(instanceList, iname);

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

	// alle Klassen werden durchgegangen
	for (int i = 0; i < cList.getLength(); i++)
	{
	    instanceList.add(i, new ArrayList<String>());

	    String cname = xmlHM.getChildwithName(cList.item(i), "name").getTextContent();
	    instanceList.get(i).add(0, cname); // der Klassenname wird der InstanceList hinzugefügt

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

		    String iname = new String();
		    String cname = new String();
		    iname = xmlHM.getChildwithName(cuList.item(j), "name").getTextContent();
		    cname = xmlHM.getChildwithName(cuList.item(j), "class").getTextContent();

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

    /**
     * Die Funktion geht die Instanzenliste durch und gibt den Klassennamen zu dem
     * übergebenen Instanzennamen zurück. Existiert keine Instnz mit dem Namen, wird
     * "" zurückgegeben
     * 
     * @param instanceList - InstanzenListe
     * @param iname        - Name der Instanz, deren Klasse gesucht wird
     * @return - Name der Klasse (oder "")
     */
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
	return " ";

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
	listArrayList(classesWithMethodsList);
	// Liste für bereits aufgerufene Methoden
	ArrayList<ArrayList<String>> calledMethodsList = new ArrayList<ArrayList<String>>();
	// alle Methoddefinitions in SeqDiagram
	NodeList seqMethodDefList = seqDiagramm.getElementsByTagName("methoddefinition");

	// in jeder Methoddefinition wird nach Methodcalls gesucht
	for (int m = 0; m < seqMethodDefList.getLength(); m++)
	{
	    //int f = 0;
	    //ArrayList<ArrayList<String>> recursiveList = new ArrayList<ArrayList<String>>();
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
			String calledClass;
			if (seqMethodCallEl.getElementsByTagName("class").item(0) == null)
			{
			    calledClass = epClass;
			} else
			{
			    if (seqMethodCallEl.getElementsByTagName("class").item(0).getTextContent().equals(" "))
			    {
				calledClass = " ";
			    } else
			    {
				calledClass = seqMethodCallEl.getElementsByTagName("class").item(0).getTextContent();
			    }
			}
			String calledInstance = " ";
			if (seqMethodCallEl.getElementsByTagName("instance").item(0) != null)
			{
			    calledInstance = seqMethodCallEl.getElementsByTagName("instance").item(0).getTextContent();
			}
			
			/**
			 * type - handled
			 */
			Element type = seqDiagramm.createElement("type");
			int a = 0;
			int e = 0;
			// alle bisher aufgerufenen Methoden werden mit der aktuell aufgerufenen
			// verglichen
			for (int i = 0; i < calledMethodsList.size(); i++)
			{
			    for (int j = 0; j < calledMethodsList.get(i).size(); j++)
			    {
				String calledEl = calledMethodsList.get(i).get(1);
				String calledCl = calledMethodsList.get(i).get(0);
				String calledIn = calledMethodsList.get(i).get(2);
				if (calledMethod.equals(calledEl) && calledClass.equals(calledCl)
					&& calledInstance.equals(calledIn) && a == 0
					&& (seqMethodCallEl.getElementsByTagName("type").item(0) == null))
				{
				    type.setTextContent("handled");
				    seqMethodCallEl.appendChild(type);
				    a++;
				}
			    }
			}
			calledMethodsList.add(e, new ArrayList<String>());
			calledMethodsList.get(e).add(calledClass);
			calledMethodsList.get(e).add(calledMethod);
			calledMethodsList.get(e).add(calledInstance);
			e += 1;

			/**
			 * type - unknown
			 */
			int b = 0;
			int c = 0;
			// alle vorhandenen Methoden werden mit der aktuell aufgerufenen verglichen
			// Prüfung des Class-Tags
			if (seqMethodCallEl.getElementsByTagName("class").item(0) == null || !(seqMethodCallEl
				.getElementsByTagName("class").item(0).getTextContent().equals(" ")))
			{
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
			} else
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
						//recursiveList.add(f, new ArrayList<String>());
					    	//recursiveList.get(f).add(calledClass);
					    	//recursiveList.get(f).add(calledMethod);
					   	//f++;
					    }
					}
				    }
				}
			    }
			} else
			{
			//		    recursiveList.add(f, new ArrayList<String>());
//		    recursiveList.get(f).add(calledClass);
//		    recursiveList.get(f).add(calledMethod);
//		   
//		    // recursiveList.get(f).add(calledInstance);
//		    f++;
//
//		    for (int i = 0; i < recursiveList.size(); i++)
//		    {
//			String recEl = recursiveList.get(i).get(1);
//			String recCl = recursiveList.get(i).get(0);
//			// String recIn = recursiveList.get(i).get(2);
//			//System.out.println(currentMethod + " - " + currentClass);
//			//System.out.println(recEl + " + " + recCl);
//			if (currentMethod.equals(recEl) && currentClass.equals(recCl))
//			{
//			    type.setTextContent("recursive2");
//			    xmlHM.getList(seqMethodCallNode, ".").item(i).appendChild(type);
//			    //seqMethodCallList.item(i).appendChild(type);
//			}
//		    }
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

    /**
     * Die Instanzen, die nicht in methodcalls drin sind, werden gelöscht (=
     * Deklarationen v. lokalen Instanzen werden aus der Sequenzdiagramm-XML
     * entfernt)
     * 
     * @param doc - das Dokument, aus dem die Einträge gelöscht werden
     * @return - das bearbeitete Dokument
     */
    public Document deleteInstancesNotInMethodcalls(Document doc)
    {
	// mList = Liste der Methoddefinitions
	try
	{
	    NodeList iList = xmlHM.getList(doc, "//instance");
	    for (int i = 0; i < iList.getLength(); i++)
	    {
//		System.out.println("Klasseninstanz gefunden: ");
//		if (xmlHM.hasChildwithName(iList.item(i), "name"))
//		{
//		    System.out.println(xmlHM.getChildwithName(iList.item(i), "name").getTextContent());
//		}
//		System.out.println("Parent Node:");
//		System.out.println(iList.item(i).getParentNode().getNodeName());
//		if (iList.item(i).getParentNode().equals(null))
//		{
//		    System.out.println("kein ParentNode");
//		}
		if (!(iList.item(i).getParentNode().getNodeName().equals("methodcall")
			|| iList.item(i).getParentNode().getNodeName().equals("classdefinition")))
		{
		    iList.item(i).getParentNode().removeChild(iList.item(i));
//		    System.out.println("Instanz-Knoten entfernt");
		}
	    }
	} catch (Exception e)
	{
	    e.printStackTrace();
	}

	return doc;
    }

    /**
     * Funktion, die die class-Tags bei lokale Instanzen in das Dokument einfügt
     * 
     * @param doc - das Dokument, das bearbeitet wird
     * @return - das bearbeitete Dokument
     * @throws XPathExpressionException
     */
    public Document handleLocalInstances(Document doc) throws XPathExpressionException
    {

	NodeList iList = xmlHM.getList(doc, "//instance");
	for (int i = 0; i < iList.getLength(); i++)
	{
	    // System.out.println("instanz gefunden");

	    // Instanzen nicht direkt unterhalb v methodcalls -> lokale Instanzen
	    if (!(iList.item(i).getParentNode().getNodeName().equals("methodcall")
		    || iList.item(i).getParentNode().getNodeName().equals("classdefinition")))
	    {
		Node currentI = iList.item(i); // currentI ist ein instance-Knoten
		Node linode = currentI.getParentNode(); // Knoten mit darin gültigen lokalen instanzen

		String instanceName = xmlHM.getChildwithName(currentI, "name").getTextContent();
		String instanceClass = xmlHM.getChildwithName(currentI, "class").getTextContent();
		// System.out.println("Parent Node : " + linode.getNodeName());

		recursiveHandlelocalInstances(doc, currentI, instanceName, instanceClass);

	    }
	}

	return doc;
    }

    /**
     * rekursiver Teil zum Einfügen der class-Tags bei lokalen Instanzen
     * 
     * @param doc           - Dokument, in dem die class-Tags eingefügt werden
     *                      sollen
     * @param currentNode   - aktuell betrachteter Knoten
     * @param instanceName  - Name der lokalen Instanz
     * @param instanceClass - Name der Klasse der lokalen Instanz
     */

    private void recursiveHandlelocalInstances(Document doc, Node currentNode, String instanceName,
	    String instanceClass)
    {
	// TODO
	// lokale Instanz ist in späteren Siblings gültig
	Node current = currentNode;
	while (!(current.getNextSibling() == null))
	{
	    while (current.getNodeType() != Node.ELEMENT_NODE || current.getNodeName().equals("instance"))
	    {
//		   while (current.getNodeName().equals("instance")) {
		current = current.getNextSibling();

//		   }   
	    }
//	      Node current = currentNode;
	    // System.out.println("current: " + current.getNodeName());
//	       current = currentNode.getNextSibling();
	    if (current.getNodeName().equals("methodcall"))
	    {
		Node instanceNode = xmlHM.getChildwithName(current, "instance");

		if (instanceNode.getTextContent().equals(instanceName)
			&& !xmlHM.hasChildwithName(instanceNode.getParentNode(), "validity"))
		{
		    Node classTag = doc.createElement("class");
		    classTag.setTextContent(instanceClass);
		    instanceNode.getParentNode().appendChild(classTag);
		}

	    }
	    if (current.hasChildNodes() && !current.getNodeName().equals("methodcall"))
	    {
		NodeList cnodes = current.getChildNodes();
		for (int k = 0; k < cnodes.getLength(); k++)
		{
		    recursiveHandlelocalInstances(doc, current, instanceName, instanceClass);
		}
	    }
	    current = current.getNextSibling();
	}

    }

    public void listArrayList(ArrayList<ArrayList<String>> list2)
    {
	for (int i = 0; i < list2.size(); i++)
	{
	    System.out.println(list2.get(i));
	}
    }
}
