import java.io.File;
import java.io.FileNotFoundException;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;
import org.xmlunit.diff.NodeMatcher;
import org.xmlunit.diff.DefaultComparisonFormatter;
import org.xmlunit.diff.DefaultNodeMatcher;
import javax.xml.transform.Source;

public class CompareXML 
  {
	public static void main(String args[]) throws FileNotFoundException {
	// reading two xml file to compare in Java program 
	File fis1 = new File("/home/patrick/Studium/Code/puml/code/testfolder/xmlSpecifications/ClassDiagram.xml");
	File fis2 = new File("/home/patrick/Studium/Code/puml/code/testfolder/xmlSpecifications/ClassDiagram2.xml"); 
	
	String source = fis1.toString();
	String target = fis2.toString();
	
	//configuring DiffBuilder
	DefaultComparisonFormatter formatter = new DefaultComparisonFormatter();
	//ComparisonListener listener = new ComparisonListener();
	NodeMatcher nmatcher = new DefaultNodeMatcher();
	DocumentBuilderFactory docbuild = null;
	Diff diff = DiffBuilder.compare(source).withTest(target)
			.checkForSimilar().checkForIdentical() // [1]
	        .ignoreComments()
	        .ignoreWhitespace()
	        .normalizeWhitespace()
	       //.withComparisonController(ComparisonController)
	        .withComparisonFormatter(formatter)
        //.withComparisonListeners(listener) 
	        //.withDifferenceEvaluator(differenceEvaluator)
        //.withDifferenceListeners(listener)
	        .withNodeMatcher(nmatcher)
	        //.withAttributeFilter(attributeFilter)
	        //.withNodeFilter(nodeFilter)
	        //.withNamespaceContext(map)
	        .withDocumentBuilderFactory(docbuild)
	        .ignoreElementContentWhitespace()
			.build();
	//Assert.assertFalse(diff.toString(), diff.hasDifferences());


	Iterable<Difference> differences = diff.getDifferences();
	for (Difference difference : differences) {
		System.out.println(difference);
	}
	
	}
  }
