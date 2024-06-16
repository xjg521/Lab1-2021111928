
package lab;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

public class graphprocessorTest2 {
@Before
public void setUp() throws Exception {

}
@Test
public void testcalcShortestPath1() throws IOException {
// Redirect System.out to capture the output
graphprocessor g = new graphprocessor();

ByteArrayOutputStream outContent = new ByteArrayOutputStream();
System.setOut(new PrintStream(outContent));
g.generateGraphFromFile("1.txt"); 
g.calcShortestPath("is", "test");
String expectedOutput = "The length of the shortest path is: 2\r\n"
+ "The shortest path from is to test is: test <- a <- is\r\n"
+ "\n";
String actualOutput = outContent.toString();
System.out.println("Actual Output: \"" + actualOutput + "\"");
System.out.println("Expected Output: \"" + expectedOutput + "\"");
assertEquals(expectedOutput.trim(), actualOutput.trim());
outContent.reset();

}

@Test
public void testcalcShortestPath2() throws IOException {
// Redirect System.out to capture the output
graphprocessor g = new graphprocessor();

ByteArrayOutputStream outContent = new ByteArrayOutputStream();
System.setOut(new PrintStream(outContent));
g.generateGraphFromFile("1.txt"); 
g.calcShortestPath("words", "dogs");
String expectedOutput = "No dogs in the graph!\n";
String actualOutput = outContent.toString();
System.out.println("Actual Output: \"" + actualOutput + "\"");
System.out.println("Expected Output: \"" + expectedOutput + "\"");
assertEquals(expectedOutput.trim(), actualOutput.trim());
outContent.reset();
}

@Test
public void testcalcShortestPath3() throws IOException {
// Redirect System.out to capture the output
graphprocessor g = new graphprocessor();

ByteArrayOutputStream outContent = new ByteArrayOutputStream();
System.setOut(new PrintStream(outContent));
g.generateGraphFromFile("1.txt"); 
g.calcShortestPath("words", "dog");
String expectedOutput = "No path found from words to dog!\n";
String actualOutput = outContent.toString();
System.out.println("Actual Output: \"" + actualOutput + "\"");
System.out.println("Expected Output: \"" + expectedOutput + "\"");
assertEquals(expectedOutput.trim(), actualOutput.trim());
outContent.reset();
}
@Test
public void testcalcShortestPath4() throws IOException {
// Redirect System.out to capture the output
graphprocessor g = new graphprocessor();

ByteArrayOutputStream outContent = new ByteArrayOutputStream();
System.setOut(new PrintStream(outContent));
g.generateGraphFromFile("1.txt"); 
g.calcShortestPath("dogs", "words");
String expectedOutput = "No dogs in the graph!\n";
String actualOutput = outContent.toString();
System.out.println("Actual Output: \"" + actualOutput + "\"");
System.out.println("Expected Output: \"" + expectedOutput + "\"");
assertEquals(expectedOutput.trim(), actualOutput.trim());
outContent.reset();
}

@Test
public void testcalcShortestPath5() throws IOException {
// Redirect System.out to capture the output
graphprocessor g = new graphprocessor();

ByteArrayOutputStream outContent = new ByteArrayOutputStream();
System.setOut(new PrintStream(outContent));
g.generateGraphFromFile("2.txt"); 
g.calcShortestPath("new","!");
String expectedOutput = "No path found from new to new!\r\n"
		+ "The shortest path from new to east : length 1 east <- new\r\n"
		+ "The shortest path from new to world : length 2 world <- east <- new\r\n"
		+ "";
String actualOutput = outContent.toString();
System.out.println("Actual Output: \"" + actualOutput + "\"");
System.out.println("Expected Output: \"" + expectedOutput + "\"");
assertEquals(expectedOutput.trim(), actualOutput.trim());
outContent.reset();
}
}