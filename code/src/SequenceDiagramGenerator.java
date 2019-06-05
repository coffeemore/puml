
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
    String dataClassDef = "/source/classdefinition/";
    String seqMethodDef = "/parsed/sequencediagram/methoddefinition";
    // ArrayList<String> usedClasses = new ArrayList<String>();
    // ArrayList<String> usedMethods = new ArrayList<String>();

    // Liste für bereits aufgerufene Methoden
    ArrayList<ArrayList<String>> calledMethodsList = new ArrayList<ArrayList<String>>();

    // Liste mit allen Klassen und ihren zugeordneten Methoden
    ArrayList<ArrayList<String>> classesWithMethodsList = new ArrayList<ArrayList<String>>();

    /**
     * Konstruktor
     */
    public SequenceDiagramGenerator()
    {

    }

    /**
     * Erstellt aus dem geparsten xml-Dokument das xml-Dokument für das
     * Sequenzdiagramm
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

	createList(parsedData, classesWithMethodsList);
	listClasses(parsedData, seqDiagramm, seq);

	Element entrypoint = seqDiagramm.createElement("entrypoint");
	seq.appendChild(entrypoint);
	Element epClass1 = seqDiagramm.createElement("class");
	epClass1.setTextContent(epClass);
	entrypoint.appendChild(epClass1);
	Element epMethod1 = seqDiagramm.createElement("method");
	epMethod1.setTextContent(epMethod);
	entrypoint.appendChild(epMethod1);

//	parsedData = deleteInstancesNotInMethodcalls(parsedData);
	listMethoddef(parsedData, seqDiagramm, seq);

	addClassesToInstances(parsedData, seqDiagramm);

	seqDiagramm = deleteInstancesNotInMethodcalls(seqDiagramm);

	addType(parsedData, seqDiagramm, seq, epClass);

	deleteUnusedClassesAndMethods(seqDiagramm, epClass);

	xmlHM.removeComments(root);
	xmlHM.writeDocumentToConsole(seqDiagramm);

	return seqDiagramm;
    }

    /**
     * Die Klassen werden aus parsedData übernommen und in seqDiagramm aufgelistet
     * 
     * @param parsedData  - xml Eingabe Dokument
     * @param seqDiagramm - Document für OutputPuml
     * @param seq         - Kindelement von root
     * @throws XPathExpressionException
     */
    private void listClasses(Document parsedData, Document seqDiagramm, Element seq) throws XPathExpressionException
    {
	Element classes = seqDiagramm.createElement("classes");
	seq.appendChild(classes);
	// alle Klassen aus parsedData werden in eine Liste geschrieben
	NodeList cList = xmlHM.getList(parsedData, dataClassDef + "name");
	for (int i = 0; i < cList.getLength(); i++)
	{
	    // in Tag <name> gespeicherter Text wird als Klassenname übernommen
	    String cName = cList.item(i).getTextContent();
	    Element entry = seqDiagramm.createElement("entry");
	    entry.setTextContent(cName);
	    classes.appendChild(entry);
	}
    }

    /**
     * Die Methode kopiert aus parsedData alle Methoddefinitions von Klassen in
     * seqDiagramm
     * 
     * @param parsedData  - xml Eingabe Dokument
     * @param seqDiagramm - Document für OutputPuml
     * @param seq         - Kindelement von root
     * @throws XPathExpressionException
     */
    private void listMethoddef(Document parsedData, Document seqDiagramm, Element seq) throws XPathExpressionException
    {
	// alle Methoden aus parsedData werden in die Liste geschrieben
	NodeList mList = xmlHM.getList(parsedData, dataClassDef + "methoddefinition");

	for (int i = 0; i < mList.getLength(); i++)
	{
	    // jede Methode wird in das Dokument seqDiagramm importiert
	    seq.appendChild(seqDiagramm.importNode(mList.item(i), true));

	    Element classTag = seqDiagramm.createElement("class");
	    NodeList list = xmlHM.getList(seqDiagramm, seqMethodDef);

	    // zu jeder Methode wird ihre Klasse mittels Class-Tag eingefügt
	    Node seqMethodNode = list.item(i);
	    String cName = xmlHM.getList(mList.item(i), "../name").item(0).getTextContent();
	    classTag.setTextContent(cName);
	    seqMethodNode.insertBefore(classTag, seqMethodNode.getFirstChild());

	    // vorhandene Parameters- oder Result-Tags werden gesucht und entfernt
	    Node node = list.item(i);
	    NodeList childs = node.getChildNodes();
	    for (int j = 0; j < childs.getLength(); j++)
	    {
		Node child = childs.item(j);
		if ((child.getNodeName().equals("parameters") || child.getNodeName().equals("result")))
		{
		    node.removeChild(child);
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

    private void addClassesToInstances(Document parsedData, Document seqDiagramm) throws XPathExpressionException
    {
	seqDiagramm = handleLocalInstances(seqDiagramm);

	ArrayList<ArrayList<String>> instanceList = createInstanceList(parsedData);

//	NodeList methodcalls = seqDiagramm.getElementsByTagName("methodcall");
	NodeList methodcalls = xmlHM.getList(seqDiagramm, "//methodcall");
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
     * Klassen mit ihren jeweiligen Instanzen
     * 
     * @param parsedData
     * @return instanceList
     * @throws XPathExpressionException
     */
    private ArrayList<ArrayList<String>> createInstanceList(Document parsedData) throws XPathExpressionException
    {

	ArrayList<ArrayList<String>> instanceList = new ArrayList<ArrayList<String>>(); // Liste mit den Klassen und
											// ihren Instanzen
	// für jede Classdefinition in parsed Data eine Liste, darin auch die Instanzen
	// dieser Klasse vermerken

//	NodeList cList = parsedData.getElementsByTagName("classdefinition");
	NodeList cList = xmlHM.getList(parsedData, "source/classdefinition");

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
	return instanceList;
    }

    /**
     * Die Funktion fügt den Instanzennamen in die Instanzenliste ein
     * 
     * @param instanceList - Instanzenliste
     * @param iname        - Instanzenname
     * @param cname        - Klassenname
     * @return
     */
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
     * " " zurückgegeben
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
     * Funktion, die die class-Tags bei methodcalls mit lokal verwendeten Instanzen
     * in das Dokument einfügt
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
	    // Instanzen nicht direkt unterhalb v methodcalls -> lokale Instanzen
	    if (!(iList.item(i).getParentNode().getNodeName().equals("methodcall")
		    || iList.item(i).getParentNode().getNodeName().equals("classdefinition")))
	    {
		Node currentI = iList.item(i); // currentI ist ein instance-Knoten

		String instanceName = xmlHM.getChildwithName(currentI, "name").getTextContent();
		String instanceClass = xmlHM.getChildwithName(currentI, "class").getTextContent();

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
	// lokale Instanz ist in späteren Siblings gültig
	Node current = currentNode;
	while (!(current.getNextSibling() == null))
	{
	    while (current.getNodeType() != Node.ELEMENT_NODE || current.getNodeName().equals("instance"))
	    {
		current = current.getNextSibling();
	    }
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

    /**
     * Die Methodcalls werden mit Type-Tags versehen
     * 
     * @param parsedData  - xml Eingabe Dokument
     * @param seqDiagramm - Document für OutputPuml
     * @param seq         - Kindelement von root
     * @param epClass     - übergebener Entrypoint
     * @throws XPathExpressionException
     */
    private void addType(Document parsedData, Document seqDiagramm, Element seq, String epClass)
	    throws XPathExpressionException
    {
	// alle Methoddefinitions in SeqDiagram
	NodeList seqMethodDefList = xmlHM.getList(seqDiagramm, seqMethodDef);

	// in jeder Methoddefinition wird nach Methodcalls gesucht
	for (int m = 0; m < seqMethodDefList.getLength(); m++)
	{
	    Node seqMethodDefNode = seqMethodDefList.item(m);

	    String currentMethod = xmlHM.getList(seqMethodDefNode, "./name").item(0).getTextContent();
	    String currentClass = xmlHM.getList(seqMethodDefNode, "./class").item(0).getTextContent();

	    NodeList seqMethodCallList = xmlHM.getList(seqMethodDefNode, ".//methodcall");

	    for (int n = 0; n < seqMethodCallList.getLength(); n++)
	    {
		Node seqMethodCallNode = seqMethodCallList.item(n);
		Node classNode = xmlHM.getList(seqMethodCallNode, "class").item(0);
		Node instanceNode = xmlHM.getList(seqMethodCallNode, "instance").item(0);
		Node typeNode = xmlHM.getList(seqMethodCallNode, "type").item(0);

		// Element seqMethodCallEl = (Element) seqMethodCallNode;
		// aktuell aufgerufene Methode
		String calledMethod = xmlHM.getList(seqMethodCallNode, "method").item(0).getTextContent();
		String calledClass;
		if (classNode == null)
		{
		    calledClass = epClass;
		} else
		{
		    if (classNode.getTextContent().equals(" "))
		    {
			calledClass = " ";
		    } else
		    {
			calledClass = classNode.getTextContent();
		    }
		}
		String calledInstance = " ";
		if (instanceNode != null)
		{
		    calledInstance = instanceNode.getTextContent();
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
		    String calledEl = calledMethodsList.get(i).get(1);
		    String calledCl = calledMethodsList.get(i).get(0);
		    String calledIn = calledMethodsList.get(i).get(2);
		    if (calledMethod.equals(calledEl) && calledClass.equals(calledCl) && calledInstance.equals(calledIn)
			    && a == 0 && (typeNode == null))
		    {
			type.setTextContent("handled");
			seqMethodCallNode.appendChild(type);
			a++;
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
		if (classNode == null || !(classNode.getTextContent().equals(" ")))
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
			seqMethodCallNode.appendChild(type);
		    }
		} else if (typeNode == null)
		{
		    type.setTextContent("unknown");
		    seqMethodCallNode.appendChild(type);
		}

		/**
		 * type - recursive
		 */
		int d = 0;
		if (calledMethod.equals(currentMethod) && calledClass.equals(currentClass))
		{
		    // Test, ob instance-Tag oder class-Tag vorhanden
		    if ((instanceNode == null) && (classNode == null))
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
					seqMethodCallNode.appendChild(type);
					d++;
				    }
				}
			    }
			}
		    }
		} else
		{
		    // Funktion zur Prüfung verschachtelter Rekursion
		    recursiveLoop(type, currentMethod, seqMethodDefList, m, typeNode);
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
     * @throws XPathExpressionException
     */
    private void recursiveLoop(Element type, String currentMethod, NodeList seqMethodDefList, int m, Node typeNode)
	    throws XPathExpressionException
    {
	// die Methodcalls der aufgerufenen Methoden werden auf Rekursivität geprüft
	if (m < seqMethodDefList.getLength())
	{
	    Node defNode = seqMethodDefList.item(m);
	    NodeList callList = xmlHM.getList(defNode, ".//methodcall");
	    for (int j = 0; j < callList.getLength(); j++)
	    {
		Node callNode = callList.item(j);
		String called = xmlHM.getList(callNode, "method").item(0).getTextContent();
		// die aktuell behandelte Methode wird mit den Methoden der Methodcalls
		// verglichen
		if (called.equals(currentMethod))
		{
		    if (typeNode == null || typeNode.getNodeName().equals("type"))
		    {
			type.setTextContent("recursive");
			callNode.appendChild(type);
		    }
		} else
		{
		    m++;
		    typeNode = xmlHM.getList(callNode, "type").item(m);

		    recursiveLoop(type, currentMethod, seqMethodDefList, m, typeNode);
		}
	    }
	}
    }

    /**
     * eine Liste mit allen Klassen und ihren Methoden wird erstellt
     * 
     * @param parsedData             - xml Eingabe Dokument
     * @param classesWithMethodsList - Liste mit allen Klassen und ihren Methoden
     * @throws XPathExpressionException
     */
    private void createList(Document parsedData, ArrayList<ArrayList<String>> classesWithMethodsList)
	    throws XPathExpressionException
    {
	NodeList mList = xmlHM.getList(parsedData, dataClassDef + "methoddefinition/name");
	NodeList cList = xmlHM.getList(parsedData, dataClassDef + "name");
	for (int i = 0; i < cList.getLength(); i++)
	{
	    // jede Klasse bekommt ihre eigene ArrayList
	    classesWithMethodsList.add(i, new ArrayList<String>());

	    String tmpClass = cList.item(i).getTextContent();
	    classesWithMethodsList.get(i).add(tmpClass);

	    for (int j = 0; j < mList.getLength(); j++)
	    {
		Node mNode = mList.item(j);
		String tmpParent = xmlHM.getList(mNode, "../../name").item(0).getTextContent();
		String tmpMethod = mNode.getTextContent();
		if (tmpParent == tmpClass)
		{
		    classesWithMethodsList.get(i).add(tmpMethod);
		}
	    }
	}
    }

    private void deleteUnusedClassesAndMethods(Document Doc, String epClass) throws XPathExpressionException
    {
	deleteUnusedClasses(Doc, epClass);
	deleteUnusedMethods(Doc);
    }

    private void deleteUnusedClasses(Document Doc, String epClass) throws XPathExpressionException
    {
	/**
	 * calledMethodsList: pro Eintrag in äußerer ArrayList: List [0] -> Klassenname;
	 * List [1] -> Methodenname; List [2] -> Instanz (ggf)
	 */
	// ungenutzte Klassen löschen
	// Liste aller genutzten Klassen erstellen
	ArrayList<String> usedClasses = new ArrayList<String>();
	usedClasses.add(epClass);
	for (int i = 0; i < calledMethodsList.size(); i++)
	{
	    String currentClassName = calledMethodsList.get(i).get(0);
	    if ((!usedClasses.contains(currentClassName)) && !currentClassName.equals(" "))
	    {
		usedClasses.add(currentClassName);
	    }
	}

	// System.out.println("calledMethods :" + calledMethods);
	// System.out.println("usedClasses :" + usedClasses);

	// Liste der Klassen im xml-Doc
	NodeList classesinDoc = xmlHM.getList(Doc, "/parsed/sequencediagram/classes/entry");

	// alle classDefinitions, deren Methoden nicht im Sequenzdiagramm auftauchen,
	// werden gelöscht
	for (int j = 0; j < classesinDoc.getLength(); j++)
	{
	    Node currentClass = classesinDoc.item(j);
	    String classname = currentClass.getTextContent();
	    if (!usedClasses.contains(classname))
	    {
//		Doc.removeChild(classesinDoc.item(j));
		currentClass.getParentNode().removeChild(currentClass);
	    }
	}
    }

    private void deleteUnusedMethods(Document Doc) throws XPathExpressionException
    {
	// ungenutzte Methoden löschen
	// alle im SeqDia vorkommenden Methoden
	NodeList methodsinDoc = xmlHM.getList(Doc, "/parsed/sequencediagram/methoddefinition");
	for (int k = 0; k < methodsinDoc.getLength(); k++)
	{
	    Node currentMethod = methodsinDoc.item(k);
	    String methodName = xmlHM.getChildwithName(currentMethod, "name").getTextContent();
	    String methodClass = xmlHM.getChildwithName(currentMethod, "class").getTextContent();
	    if (!methodWasUsed(methodName, methodClass))
	    {
		currentMethod.getParentNode().removeChild(currentMethod);
	    }
	}
    }

    private boolean methodWasUsed(String methodName, String methodClass)
    {
	/**
	 * calledMethodsList: pro Eintrag in äußerer ArrayList: List [0] -> Klassenname;
	 * List [1] -> Methodenname; List [2] -> Instanz (ggf)
	 */
	for (int i = 0; i < calledMethodsList.size(); i++)
	{
	    String currentMethodName = calledMethodsList.get(i).get(1);
	    String currentMethodClass = calledMethodsList.get(i).get(0);
	    if (currentMethodName.equals(methodName) && currentMethodClass.equals(methodClass))
	    {
		return true;
	    }
	}

	return false;
    }

//    public void listArrayList(ArrayList<ArrayList<String>> list2)
//    {
//	for (int i = 0; i < list2.size(); i++)
//	{
//	    System.out.println(list2.get(i));
//	}
//    }
}
