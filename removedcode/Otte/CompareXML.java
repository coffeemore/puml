import java.io.FileNotFoundException;
import java.util.Iterator;
import org.w3c.dom.Document;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;
import org.xmlunit.diff.ElementSelectors;
import org.xmlunit.diff.DefaultComparisonFormatter;
import org.xmlunit.diff.DefaultNodeMatcher;

public class CompareXML 
{
	  static XmlHelperMethods helper = new XmlHelperMethods();
	  
	public static boolean main(String args[]) throws FileNotFoundException
	{
		Document doc1=helper.getDocumentFrom("testfolder/xmlSpecifications/ClassDiagram.xml");
		Document doc2=helper.getDocumentFrom("testfolder/xmlSpecifications/ClassDiagram2.xml");
		DefaultComparisonFormatter formatter = new DefaultComparisonFormatter();
		DefaultNodeMatcher nodeMatcher = new DefaultNodeMatcher(ElementSelectors.byNameAndText);
		Diff d = DiffBuilder.compare(doc1).withTest(doc2)
				 .checkForSimilar()//.checkForIdentical()
				 .withNodeMatcher(nodeMatcher)
				 .ignoreWhitespace()
				 .normalizeWhitespace()
				 .withComparisonFormatter(formatter)
				 .ignoreComments()
				 .ignoreElementContentWhitespace()
				 .build();
		Iterable<Difference> diffList = d.getDifferences();
	    Iterator<Difference> iterator = diffList.iterator();
	    while(iterator.hasNext()) 
	    {
	        Difference next = iterator.next();
	        System.out.println("Difference: " + next);
		}
	    if (iterator.hasNext()) {
	    	return false;
	    }
	    else {
	    	return true;
	    }
	}
}