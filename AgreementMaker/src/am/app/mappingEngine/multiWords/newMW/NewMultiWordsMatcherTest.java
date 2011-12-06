package am.app.mappingEngine.multiWords.newMW;


import org.junit.BeforeClass;
import org.junit.Test;

import am.app.mappingEngine.AbstractMatcherTest;

public class NewMultiWordsMatcherTest extends AbstractMatcherTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		AbstractMatcherTest.setUpBeforeClass();
		testMatcher = new NewMultiWordsMatcher();
	}
	
	@Test
	public void testMatch() {
		super.testMatch();
	}

}