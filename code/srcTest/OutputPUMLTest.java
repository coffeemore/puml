import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class OutputPUMLTest {

	@Test
	void testGetPUML() {
		ParsingResult parsTest = new ParsingResult();
		parsTest.classConnections = "";
		assertEquals(parsTest, parsTest, "Heyyy");
	}

	@Test
	void testSavePUMLtoFile() {
		fail("Not yet implemented");
	}

	@Test
	void testCreatePlantUML() {
		fail("Not yet implemented");
	}

}
