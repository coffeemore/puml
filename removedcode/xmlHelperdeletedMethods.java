    /*
     * Hilfsmethode zum Herauslöschen von Kommentaren aus einem xml-Dokument
     * 
     * 
     */
    public Document deleteComments(Document xmlDoc)
    {
	if (xmlDoc.hasChildNodes())
	{
	    NodeList nodes = xmlDoc.getChildNodes();
	    for (int i = 0; i < nodes.getLength(); i++)
	    {
		recdelCom(nodes.item(i));

	    }
//        	xmlDoc = recdelCom(xmlDoc, nodes);      	
	}

	return xmlDoc;
    }

//    public Document recdelCom(Document xmlDoc, NodeList nodes) {
    public void recdelCom(Node node) throws NullPointerException
    {
	System.out.println("rekursion");
// 	Ursprünglicher Ansatz - StackOverflow Exceptions 
	// Kommentarknoten haben den Typ 8
//		Document Doc = nodes.item(0).getOwnerDocument();
//		for (int i = 0; i < nodes.getLength(); i++) {
//	        	if (nodes.item(i).getNodeType()==8) {	        	   
//	        	    Doc.removeChild(nodes.item(i));	        	   
//	        	}
//		}
//		nodes = Doc.getChildNodes();
//		for (int i = 0; i < nodes.getLength(); i++) {
//		    if (nodes.item(i).hasChildNodes()) {
//			NodeList chnodes = nodes.item(i).getChildNodes();
//			xmlDoc = recdelCom(xmlDoc, chnodes);	
//		    }
//		    
//		}

	if (node.getNodeType() == 8)
	{
	    node.getOwnerDocument().removeChild(node);
	} else
	{
	    if (node.hasChildNodes())
	    {
		recdelCom(node.getFirstChild());
	    }
	
	//wenn der aktuelle Knoten noch Siblings hat, wird mit dem nächsten Sibling weitergemacht

//gibt Fehlermeldungen wg ParentNode
	if (!node.getParentNode().getLastChild().equals(node)) 	    
	{
	    recdelCom(node.getNextSibling());
	}
	}

    }