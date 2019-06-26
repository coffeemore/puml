
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
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
     * @throws TransformerException
     */

    public Document createDiagram(Document parsedData, String epClass, String epMethod)
	    throws ParserConfigurationException, SAXException, IOException, XPathExpressionException,
	    TransformerException
    {
	// neues Dokument, das seqDiagramm Informationen enthalten wird
	Document seqDiagram = xmlHM.createDocument();

	Element root = seqDiagram.createElement("parsed");
	seqDiagram.appendChild(root);
	Element seq = seqDiagram.createElement("sequencediagram");
	root.appendChild(seq);

	createList(parsedData, classesWithMethodsList);
	listClasses(parsedData, seqDiagram, seq);

	Element entrypoint = seqDiagram.createElement("entrypoint");
	seq.appendChild(entrypoint);
	Element epClass1 = seqDiagram.createElement("class");
	epClass1.setTextContent(epClass);
	entrypoint.appendChild(epClass1);
	Element epMethod1 = seqDiagram.createElement("method");
	epMethod1.setTextContent(epMethod);
	entrypoint.appendChild(epMethod1);

	addEpClass(epClass, epMethod);

	listMethoddef(parsedData, seqDiagram, seq);
	deleteFrame(seqDiagram);

	addClassesToInstances(parsedData, seqDiagram);

	seqDiagram = deleteInstancesNotInMethodcalls(seqDiagram);

	addType(parsedData, seqDiagram, seq, epClass);

	deleteUnusedClassesAndMethods(seqDiagram, epClass);

	xmlHM.removeComments(root);
	seqDiagram = xmlHM.removeWhitespace(seqDiagram);

	return seqDiagram;
    }

    /**
     * Die Klassen werden aus parsedData übernommen und in seqDiagramm aufgelistet
     * 
     * @param parsedData - xml Eingabe Dokument
     * @param seqDiagram - Document für OutputPuml
     * @param seq        - Kindelement von root
     * @throws XPathExpressionException
     */
    private void listClasses(Document parsedData, Document seqDiagram, Element seq) throws XPathExpressionException
    {
	Element classes = seqDiagram.createElement("classes");
	seq.appendChild(classes);
	// alle Klassen aus parsedData werden in eine Liste geschrieben
	NodeList cList = xmlHM.getList(parsedData, dataClassDef + "name");
	for (int i = 0; i < cList.getLength(); i++)
	{
	    // in Tag <name> gespeicherter Text wird als Klassenname übernommen
	    String cName = cList.item(i).getTextContent();
	    Element entry = seqDiagram.createElement("entry");
	    entry.setTextContent(cName);
	    classes.appendChild(entry);
	}
    }

    /**
     * Die Methode kopiert aus parsedData alle Methoddefinitions von Klassen in
     * seqDiagramm
     * 
     * @param parsedData - xml Eingabe Dokument
     * @param seqDiagram - Document für OutputPuml
     * @param seq        - Kindelement von root
     * @throws XPathExpressionException
     */
    private void listMethoddef(Document parsedData, Document seqDiagram, Element seq) throws XPathExpressionException
    {
	// alle Methoden aus parsedData werden in die Liste geschrieben
	NodeList mList = xmlHM.getList(parsedData, dataClassDef + "methoddefinition");

	for (int i = 0; i < mList.getLength(); i++)
	{
	    // jede Methode wird in das Dokument seqDiagram importiert
	    seq.appendChild(seqDiagram.importNode(mList.item(i), true));

	    Element classTag = seqDiagram.createElement("class");
	    NodeList list = xmlHM.getList(seqDiagram, seqMethodDef);

	    // zu jeder Methode wird ihre Klasse mittels Class-Tag eingefügt
	    Node seqMethodNode = list.item(i);
	    String cName = xmlHM.getList(mList.item(i), "../name").item(0).getTextContent();
	    classTag.setTextContent(cName);
	    seqMethodNode.insertBefore(classTag, xmlHM.getList(seqMethodNode, "child::*").item(0));

	    // vorhandene Parameters-, Access- oder Result-Tags werden gesucht und entfernt
	    Node node = list.item(i);
	    NodeList childs = xmlHM.getList(node, "child::*");
	    for (int j = 0; j < childs.getLength(); j++)
	    {
		Node child = childs.item(j);
		if (child.getNodeName().equals("parameters") || child.getNodeName().equals("result")
			|| child.getNodeName().equals("access") || child.getNodeName().equals("type"))
		{
		    node.removeChild(child);
		}
	    }
	}
    }

    public void deleteFrame(Document seqDiagram) throws XPathExpressionException
    {
	NodeList list = xmlHM.getList(seqDiagram, seqMethodDef + "//frame");
	for (int i = 0; i < list.getLength(); i++)
	{
	    xmlHM.delNode(list.item(i), true);
	}
    }

    /**
     * Instanz: von welcher Klasse? im methodcall ein Tag instanz -> Klassentag +
     * -name muss reingenommen werden -> Instanzenliste anlegen
     * 
     * Funktion fügt im seqDiagramm innerhalb der methodcalls das passende class-Tag
     * hinzu, sofern ein instance-Tag existiert
     * 
     * param: parsedData - Quell-Dokument seqDiagram - das generierte Dokument, in
     * dem die Instanzen eingefügt werden sollen
     * 
     * @throws XPathExpressionException
     */

    private void addClassesToInstances(Document parsedData, Document seqDiagram) throws XPathExpressionException
    {
	seqDiagram = handleLocalInstances(seqDiagram);

	ArrayList<ArrayList<String>> instanceList = createInstanceList(parsedData);

	NodeList methodcalls = xmlHM.getList(seqDiagram, "//methodcall");
	for (int i = 0; i < methodcalls.getLength(); i++)
	{
	    NodeList mchildnodes = xmlHM.getList(methodcalls.item(i), "child::*");
	    for (int j = 0; j < mchildnodes.getLength(); j++)
	    {
		if (mchildnodes.item(j).getNodeName().equals("instance"))
		{
		    if (!xmlHM.hasChildwithName(xmlHM.getList(mchildnodes.item(j),"..").item(0), "class"))
		    {
			String iname = mchildnodes.item(j).getTextContent();
			// wenn Instanz in InstanzenListe vorhanden
			String cname = findClassofInstance(instanceList, iname);

			Node classTag = seqDiagram.createElement("class");
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
	// Liste mit den Klassen und ihren Instanzen
	ArrayList<ArrayList<String>> instanceList = new ArrayList<ArrayList<String>>();
	NodeList cList = xmlHM.getList(parsedData, "source/classdefinition");

	// alle Klassen werden durchgegangen
	for (int i = 0; i < cList.getLength(); i++)
	{
	    instanceList.add(i, new ArrayList<String>());

	    String cname = xmlHM.getChildwithName(cList.item(i), "name").getTextContent();
	    // der Klassenname wird der InstanceList hinzugefügt
	    instanceList.get(i).add(0, cname);
	}

	// alle Klassen werden durchgegangen
	for (int i = 0; i < cList.getLength(); i++)
	{
	    // Liste aller Unterknoten v. Classdefinition
	    NodeList cuList = xmlHM.getList(cList.item(i), "child::*");
	    // alle Unterknoten der Klassen werden durchgegangen
	    for (int j = 0; j < cuList.getLength(); j++)
	    {
		if (cuList.item(j).getNodeName().equals("instance"))
		{
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
     * übergebenen Instanzennamen zurück. Existiert keine Instanz mit dem Namen,
     * wird " " zurückgegeben
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
	try
	{
	    NodeList iList = xmlHM.getList(doc, "//instance");
	    for (int i = 0; i < iList.getLength(); i++)
	    {
		if (!(xmlHM.getList(iList.item(i), "..").item(0).getNodeName().equals("methodcall")
			|| xmlHM.getList(iList.item(i), "..").item(0).getNodeName().equals("classdefinition")))
		{
		    xmlHM.getList(iList.item(i), "..").item(0).removeChild(iList.item(i));
		}
	    }
	} catch (Exception e)
	{
		PUMLgenerator.logger.getLog().warning("@SequenceDiagramGenerator/deleteInstancesNotInMethodcalls: "+e.toString());
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
	    // Instanzen nicht direkt unterhalb von methodcalls -> lokale Instanzen
	    if (!(xmlHM.getList(iList.item(i), "..").item(0).getNodeName().equals("methodcall")
		    || xmlHM.getList(iList.item(i), "..").item(0).getNodeName().equals("classdefinition")))
	    {
		// currentI ist ein instance-Knoten
		Node currentI = iList.item(i);

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
     * @throws XPathExpressionException 
     */

    private void recursiveHandlelocalInstances(Document doc, Node currentNode, String instanceName,
	    String instanceClass) throws XPathExpressionException
    {
	// lokale Instanz ist in späteren Siblings gültig
	Node current = currentNode;
	while (!(xmlHM.getList(current, "following-sibling::*").item(0) == null))
	{
	    while (current.getNodeType() != Node.ELEMENT_NODE || current.getNodeName().equals("instance"))
	    {
		current = xmlHM.getList(current, "following-sibling::*").item(0);
	    }
	    if (current.getNodeName().equals("methodcall"))
	    {
		Node instanceNode = xmlHM.getChildwithName(current, "instance");

		if (instanceNode.getTextContent().equals(instanceName)
			&& !xmlHM.hasChildwithName(xmlHM.getList(instanceNode, "..").item(0), "validity"))
		{
		    Node classTag = doc.createElement("class");
		    classTag.setTextContent(instanceClass);
		    xmlHM.getList(instanceNode, "..").item(0).appendChild(classTag);
		}
	    }
	    if (current.hasChildNodes() && !current.getNodeName().equals("methodcall"))
	    {
		NodeList cnodes = xmlHM.getList(current, "child::*");
		for (int k = 0; k < cnodes.getLength(); k++)
		{
		    recursiveHandlelocalInstances(doc, current, instanceName, instanceClass);
		}
	    }
	    current = xmlHM.getList(current, "following-sibling::*").item(0);
	}
    }

    private void addEpClass(String epClass, String epMethod)
    {
	calledMethodsList.add(0, new ArrayList<String>());
	calledMethodsList.get(0).add(epClass);
	calledMethodsList.get(0).add(epMethod);
	calledMethodsList.get(0).add(" ");
    }

    /**
     * Die Methodcalls werden mit Type-Tags versehen
     * 
     * @param parsedData - xml Eingabe Dokument
     * @param seqDiagram - Document für OutputPuml
     * @param seq        - Kindelement von root
     * @param epClass    - übergebener Entrypoint
     * @throws XPathExpressionException
     */
    private void addType(Document parsedData, Document seqDiagram, Element seq, String epClass)
	    throws XPathExpressionException
    {
	// alle Methoddefinitions in SeqDiagram
	NodeList seqMethodDefList = xmlHM.getList(seqDiagram, seqMethodDef);

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
		Element type = seqDiagram.createElement("type");
		int a = 0;
		int e = 1;
		// alle bisher aufgerufenen Methoden werden mit der aktuell aufgerufenen
		// verglichen
		for (int i = 0; i < calledMethodsList.size(); i++)
		{
		    String calledEl = calledMethodsList.get(i).get(1);
		    String calledCl = calledMethodsList.get(i).get(0);
		    String calledIn = calledMethodsList.get(i).get(2);
		    /**
		     * wir befinden uns in den methodcalls einer methoddef wenn die im methodcall
		     * aufgerufene Methode keine anderen Methoden aufruft, muss der handled-tag
		     * nicht gesetzt werden
		     * 
		     */
		    if (calledMethod.equals(calledEl) && calledClass.equals(calledCl) && calledInstance.equals(calledIn)
			    && a == 0 && (typeNode == null))
		    {
			boolean test = existingMethodcalls(seqDiagram, calledMethod,
				xmlHM.getList(seqMethodCallNode, "method").item(0));
			if (test)
			{
			    type.setTextContent("handled");
			    seqMethodCallNode.appendChild(type);
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

    private boolean existingMethodcalls(Document seqDiagram, String calledMethod, Node cMethod)
	    throws XPathExpressionException
    {
	NodeList list = xmlHM.getList(seqDiagram, seqMethodDef);
	for (int i = 0; i < list.getLength(); i++)
	{
	    Node node = xmlHM.getChildwithName(list.item(i), "name");
	    String name = node.getTextContent();

	    if (name.equals(calledMethod))
	    {
		NodeList list2 = xmlHM.getList(xmlHM.getList(node, "..").item(0), ".//methodcall");
		if (list2.getLength() == 0)
		{
		    return false;
		}
	    }
	}
	return true;
    }

    /**
     * verschachtelte Rekursion wird geprüft
     * 
     * @param type             - Element von Methodcall
     * @param currentMethod    - aktuell aufgerufene Methode
     * @param seqMethodDefList - Methoddefinition-Liste
     * @param m                - Index der Stelle in der Methoddefinition-Liste
     * @param typeNode         - type-Tag von aktueller Methode
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
		xmlHM.getList(currentClass, "..").item(0).removeChild(currentClass);
	    }
	}
    }

    private void deleteUnusedMethods(Document Doc) throws XPathExpressionException
    {
	// ungenutzte Methoden löschen
	// alle im SeqDiagram vorkommenden Methoden
	NodeList methodsinDoc = xmlHM.getList(Doc, "/parsed/sequencediagram/methoddefinition");
	for (int k = 0; k < methodsinDoc.getLength(); k++)
	{
	    Node currentMethod = methodsinDoc.item(k);
	    String methodName = xmlHM.getChildwithName(currentMethod, "name").getTextContent();
	    String methodClass = xmlHM.getChildwithName(currentMethod, "class").getTextContent();
	    if (!methodWasUsed(methodName, methodClass))
	    {
		xmlHM.getList(currentMethod, "..").item(0).removeChild(currentMethod);
	    }
	}
    }

    private boolean methodWasUsed(String methodName, String methodClass)
    {
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
}