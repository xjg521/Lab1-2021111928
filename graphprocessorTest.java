package lab;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

public class graphprocessorTest {
 
	@Before
	public void setUp() throws Exception {

	}
	

	@Test
	
	   public void testQueryBridgeWordsWithMissingWord() throws IOException {
        // Redirect System.out to capture the output
        graphprocessor g = new graphprocessor();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        g.generateGraphFromFile("1.txt");           
        g.queryBridgeWords("is", "test");
        String expectedOutput = "The bridge words from is to test are: a, to.\n";
        String actualOutput = outContent.toString();
        System.out.println("Actual Output: \"" + actualOutput + "\"");
        System.out.println("Expected Output: \"" + expectedOutput + "\"");
        assertEquals(expectedOutput.trim(), actualOutput.trim());
        outContent.reset();

       
    }
	
	
}
