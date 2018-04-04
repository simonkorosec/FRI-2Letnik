import static org.junit.Assert.*;
import org.junit.*;

public class BstTest {

    private Bst<String> bst;

    public BstTest() {
    }

    @Before
    public void setUp() {
        bst = new Bst<>();
    }

    // Praviloma bi morali testirati vsako funkcijo v razredu
    // torej tudi: member, insert, delete, deleteMin, getDepth, countNodes
    
    // Glede na to, da teste zasnujemo pred poznavanjem podrobnosti implementacije
    // zasnujemo teste za metode vmesnika: 
    // add, removeFirst, getFirst, size,depth, isEmpty, remove, exists
    
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
}
