package seznami;

import static org.junit.Assert.*;
import org.junit.*;
import seznami.Bst;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class BstTest {

    private Bst<String> bst;

    public BstTest() {
    }

    @Before
    public void setUp() {
        bst = new Bst<>();
    }

    @Test
    public void testAdd() {
       bst.add("test1");
       bst.add("test2");
       bst.add("test3");
    }

    @Test
    public void testRemoveFirst(){
        bst.add("b");
        bst.add("c");
        bst.add("a");

        assertEquals("b", bst.removeFirst());
        assertEquals("c", bst.removeFirst());
        assertEquals("a", bst.removeFirst());
    }
    
    @Test
    public void testGetFirstOne() {
        bst.add("Test");
        assertEquals("Test", bst.getFirst());
        assertEquals(1, bst.size());
        assertEquals(1, bst.depth());
    }

    @Test
    public void testGetFirstMultiple() {
        bst.add("Test2");
        assertEquals("Test2", bst.getFirst());
        assertEquals(1, bst.size());
        assertEquals(1, bst.depth());
        bst.add("Test3");
        bst.add("Test1");
        assertEquals("Test2", bst.getFirst());
        assertEquals("Test2", bst.getFirst());
        assertEquals(3, bst.size());
        assertEquals(2, bst.depth());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testGetFirstOnEmpty() {
        bst.getFirst();
    }

   @Test
    public void testSizeOnEmpty() {
        assertEquals(0, bst.size());
    }
    
    @Test
    public void testSize(){
        bst.add("test1");
        bst.add("test2");
        bst.add("test3");
        assertEquals(3, bst.size());
        bst.add("test4");
        bst.add("test5");
        bst.add("test6");
        assertEquals(6, bst.size());
        bst.removeFirst();
        bst.removeFirst();
        assertEquals(4, bst.size());
    }
    
    @Test
    public void testDepthOnEmpty() {
        assertEquals(0, bst.depth());
    }
    
    @Test
    public void testDepth(){
        bst.add("4");
        assertEquals(1, bst.depth());
        bst.add("2");
        bst.add("6");
        assertEquals(2, bst.depth());
        bst.add("1");
        bst.add("3");
        bst.add("5");
        bst.add("7");
        assertEquals(3, bst.depth());
        bst.remove("1");
        bst.remove("3");
        bst.remove("5");
        bst.remove("7");
        assertEquals(2, bst.depth());
        bst.remove("2");
        bst.remove("6");
        assertEquals(1, bst.depth());
        bst.remove("4");
        assertEquals(0, bst.depth());
    }
    
    @Test
    public void isEmpty(){
        assertTrue(bst.isEmpty());
        bst.add("1");
        assertFalse(bst.isEmpty());
    }
    
    @Test
    public void testExists(){
        bst.add("5");
        bst.add("2");
        bst.add("7");
        bst.add("3");
        assertTrue(bst.exists("3"));
        assertTrue(bst.exists("7"));
        assertFalse(bst.exists("8"));
        assertFalse(bst.exists("4"));
    }
    
    @Test
    public void testRemove(){
        bst.add("5");
        bst.add("2");
        bst.add("7");
        bst.add("3");
        assertEquals("3",bst.remove("3"));
        assertEquals("5",bst.remove("5"));
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testRemoveNotContains(){
        bst.add("5");
        bst.add("2");
        bst.add("6");
        bst.remove("3");
    }

    @Test
    public void testDeleteMin(){
        bst.add("1");
        bst.add("4");
        bst.add("2");
        bst.add("3");
        bst.add("6");
        bst.add("5");
        bst.add("7");

        assertEquals("4",bst.remove("4"));
    }

    /*
     * Od tu naprej testi za domačo nalogo
     */

    @Test
    public void testAsList(){
        List<String> originalList = Arrays.asList("1","2","3","4","5","6","7","8","9");

        bst.add("5");
        bst.add("3");
        bst.add("8");
        bst.add("2");
        bst.add("1");
        bst.add("4");
        bst.add("6");
        bst.add("9");
        bst.add("7");

        assertEquals(originalList, bst.asList());
    }


    /*
        test za save poln
     */
/*
    @Ignore
    @Test
    public void testSaveFull(){
        for (int i = 0; i < 5000000; i++) {
            bst.add(Integer.toString(i));
        }

        try {
            bst.save(new FileOutputStream("G:/test2.bin"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
*/
}
