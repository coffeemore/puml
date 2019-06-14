import static org.junit.jupiter.api.Assertions.*;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class XmlHelperMethodsTest
{
    XmlHelperMethods xmlHM = new XmlHelperMethods();
    Document testDoc;

    void SetUptestDoc() throws ParserConfigurationException
    {
	testDoc = xmlHM.createDocument();

	Element root = testDoc.createElement("root");
	testDoc.appendChild(root);

	Element testNode = testDoc.createElement("testNode");
	root.appendChild(testNode);

	Element cN1 = testDoc.createElement("ChildNode1");
	testNode.appendChild(cN1);
	Element cN2 = testDoc.createElement("ChildNode2");
	testNode.appendChild(cN2);
	Element cN3 = testDoc.createElement("ChildNode3");
	testNode.appendChild(cN3);
	Element cN4 = testDoc.createElement("ChildNode4");
	testNode.appendChild(cN4);

    }

    @Test
    void delNode() throws ParserConfigurationException
    {
	assertAll(
		
		//Test: delNode mit keepChildNodes; testNode wird gelöscht
		() ->
	{
	    boolean status = true;
	    SetUptestDoc();
	    Node testPNode = testDoc.getElementsByTagName("testNode").item(0);
	    xmlHM.writeDocumentToConsole(testDoc);
	    System.out.println("delNode, keepChildNodes");
	    xmlHM.delNode(testPNode, true);

	    xmlHM.writeDocumentToConsole(testDoc);

	    Element rootN = testDoc.getDocumentElement();
	    if (!rootN.getTagName().equals("root"))
	    {
		status = false;
	    }
	    if (rootN.hasChildNodes())
	    {
		NodeList childNodes = rootN.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++)
		{
		    if (!childNodes.item(i).getNodeName().equals("ChildNode" + (i + 1)))
		    {
			System.out.println("keepChildNodes;");
			System.out.println(childNodes.item(i).getNodeName() + " ist falsch");
			status = false;
		    }
		}
	    }
	    assertTrue(status);
	}, 
	//Test: delNode ohne keepChildNodes; testNode wird gelöscht
	() ->
	{
	    boolean status = true;
	    SetUptestDoc();
	    Node testPNode = testDoc.getElementsByTagName("testNode").item(0);
	    System.out.println("delNode, !keepChildNodes");
	    xmlHM.delNode(testPNode, false);
	    xmlHM.writeDocumentToConsole(testDoc);
	    Element rootN = testDoc.getDocumentElement();
	    if (!rootN.getTagName().equals("root"))
	    {
		status = false;
	    }
	    if (rootN.hasChildNodes())
	    {
		status = false;
	    }
	    assertTrue(status);
	}
	);
    }

    // Test für hasChildwithName und getChildwithName
    @Test
    void childwithName() throws ParserConfigurationException
    {
	SetUptestDoc();
	Node testPNode = testDoc.getElementsByTagName("testNode").item(0);
	assertAll(
		// Test für has ChildwithName -true
		() ->
		{
		    boolean s = xmlHM.hasChildwithName(testPNode, "ChildNode3");
		    System.out.println(s);
		    assertTrue(s);
		},
		// Test für has ChildwithName - false
		() ->
		{
		    boolean s = xmlHM.hasChildwithName(testPNode, "ChildNode8");
		    System.out.println(s);
		    assertFalse(s);
		}, 
		//Test für getChildwithName
		() -> 
		{
		  Node erwErg = testDoc.getElementsByTagName("ChildNode2").item(0);
		  Node aktErg = xmlHM.getChildwithName(testPNode, "ChildNode2");
		  boolean s =erwErg.isSameNode(aktErg);
		  assertTrue (s);
		}
	);
    }

    @Test
    void writeToFile()
    {

    }

    @Test
    void listAllNodes()
    {

    }

    @Test
    void removeComments()
    {

    }

    @Test
    void removeWhitespace()
    {

    }

    @Test
    void getList()
    {

    }

    @Test
    void getChildwithName()
    {

    }

    @Test
    void hasChildwithName()
    {

    }

}
