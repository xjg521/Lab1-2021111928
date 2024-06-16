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

    public void testQueryBridgeWordsWithMissingWord1() throws IOException {
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

    @Test
    public void testQueryBridgeWords2() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);
        GraphProcessor g = new GraphProcessor();
        g.generateGraphFromFile("1.txt");
        g.queryBridgeWords("in", "cases");
        System.out.flush();
        System.setOut(old);
        assertEquals("The bridge words from in to cases are: various.", baos.toString().trim());
    }

    @Test
    public void testQueryBridgeWords3() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);
        GraphProcessor g = new GraphProcessor();
        g.generateGraphFromFile("1.txt");
        g.queryBridgeWords("thank", "you");
        System.out.flush();
        System.setOut(old);
        assertEquals("No bridge words from thank to you!", baos.toString().trim());
    }

    @Test
    public void testQueryBridgeWords4() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);
        GraphProcessor g = new GraphProcessor();
        g.generateGraphFromFile("1.txt");
        g.queryBridgeWords("you", "dogs");
        System.out.flush();
        System.setOut(old);
        assertEquals("No you or dogs in the graph!", baos.toString().trim());
    }

}
