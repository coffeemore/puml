import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class KommentarfilterTest {

	@Test
	void test() {

		ParserJava parsertest = new ParserJava();
		parsertest.getCommentlessSourceCode("" + "Das ist ein Test \"Test\"" + "" + "Das hier soll stehen bleiben \"das nicht\""
				+ "/*Das ist ein \r\n" + "*Gruppen-\r\n" + "Kommentar*/\r\n" + "\"Das ist ein //Kommentar im String\""+ "Das ist kein Kommentar" + "// Das ist ein Kommentar");
		
	}
}
