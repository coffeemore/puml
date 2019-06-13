import static org.junit.jupiter.api.Assertions.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

class XmlHelperMethodsTest
{
    XmlHelperMethods xmlHM = new XmlHelperMethods();
    Document testDoc;

    void SetUpdelNode() throws ParserConfigurationException
    {
	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	testDoc = docBuilder.newDocument();

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
	assertAll(() ->
	{
	    boolean status = true;
	    SetUpdelNode();
	    xmlHM.writeDocumentToConsole(testDoc);
	    System.out.println("delNode, keepChildNodes");
	    xmlHM.delNode(testDoc.getElementsByTagName("testNode").item(0), true);

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

	}, () ->
	{
	    boolean status = true;
	    SetUpdelNode();
	    System.out.println("delNode, !keepChildNodes");
	    xmlHM.delNode(testDoc.getElementsByTagName("testNode").item(0), false);
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
    @Test
    void hasChildwithName() {
	
    }
    
    @Test
    void getChildwithName() {
	
    }
    

}
