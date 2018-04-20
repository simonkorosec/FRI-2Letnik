package seznami;

import org.junit.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


public class PrioritetnaVrstaTest {
    
    private PrioritetnaVrsta<String> pv;
    
    public PrioritetnaVrstaTest() {}

    @Before
    public void setUp() {
        pv = new PrioritetnaVrsta<>(10);
    }


    /** Test metod razreda <seznami.PrioritetnaVrsta> */
	
    // testi dodajanja

    @Test
    public void testAddOne() {
        pv.add("Test");
    }
    
    @Test
    public void testAddMultiple() {
        pv.add("Test1");
        pv.add("Test2");
    }
    
    @Test
    public void testAddOverflow() {
        pv = new PrioritetnaVrsta<>(2);
        pv.add("Test1");
        pv.add("Test2");
        pv.add("Test3");
    }
    
    // testi brisanja
	
    @Test(expected=java.util.NoSuchElementException.class)
    public void testRemoveFirstEmpty() {
        pv.removeFirst();
    }
    
    @Test
    public void testRemoveFirstOne() {
        pv.add("Test");
        assertEquals("Test", pv.removeFirst());
    }
    
    @Test
    public void testRemoveFirstMultiple() {
        pv.add("Test1");
        pv.add("Test5");
        pv.add("Test2");
        pv.add("Test4");
        pv.add("Test3");
        assertEquals("Test5", pv.removeFirst());
        assertEquals("Test4", pv.removeFirst());
        assertEquals("Test3", pv.removeFirst());
        assertEquals("Test2", pv.removeFirst());
        assertEquals("Test1", pv.removeFirst());
    }

    @Test(expected=java.util.NoSuchElementException.class)
    public void testGetFirstEmpty() {
        pv.getFirst();
    }
    
    @Test
    public void testGetFirstOne() {
        pv.add("Test");
        assertEquals("Test", pv.getFirst());
    }
    
    @Test
    public void testGetFirstMultiple() {
        pv.add("Test1");
        assertEquals("Test1", pv.getFirst());
        pv.add("Test3");
        pv.add("Test2");
        assertEquals("Test3", pv.getFirst());
        assertEquals("Test3", pv.getFirst());
    }
    
    // testiranje metode za globino
	
    @Test
    public void testDepthEmpty() {
        assertEquals(0, pv.depth());
    }
    
    @Test
    public void testDepthOne() {
        pv.add("Test1");
        assertEquals(1, pv.depth());
    }

    @Test
    public void testDepthMultiple() {
        pv.add("Test1");
        assertEquals(1, pv.depth());
        pv.add("Test5");
        assertEquals(2, pv.depth());
        pv.add("Test2");
        assertEquals(2, pv.depth());
        pv.add("Test4");
        assertEquals(3, pv.depth());
        pv.add("Test3");
        assertEquals(3, pv.depth());
        pv.add("Test6");
        assertEquals(3, pv.depth());
        pv.add("Test8");
        assertEquals(3, pv.depth());
        pv.add("Test7");
        assertEquals(4, pv.depth());
    }
    
    // test metode size
	
    @Test
    public void testSizeEmpty() {
        assertEquals(0, pv.size());
    }
    
    @Test
    public void testSizeOne() {
        pv.add("Test");
        assertEquals(1, pv.size());
    }
    
    @Test
    public void testSizeMultiple() {
        assertEquals(0, pv.size());
        pv.add("Test");
        assertEquals(1, pv.size());
        pv.add("Test1");
        assertEquals(2, pv.size());
        pv.add("Test2");
        assertEquals(3, pv.size());
    }
    
    // test metode isEmpty
	
    @Test
    public void testIsEmptyEmpty() {
        assertTrue(pv.isEmpty());
    }

    @Test
    public void testIsEmptyOne() {
        pv.add("Test");
        assertFalse(pv.isEmpty());
    }
    
    @Test
    public void testIsEmptyMultiple() {
        pv.add("Test");
        pv.add("Test1");
        pv.add("Test2");
        assertFalse(pv.isEmpty());
    }

    // Test remove in exists

    @Test
    public void testRemove() {
        pv.add("test1");
        pv.add("test2");
        assertEquals("test1", pv.remove("test1"));
        assertNull(pv.remove("test1"));
    }

    @Test
    public void testExists() {
        pv.add("test1");
        pv.add("test2");
        assertTrue(pv.exists("test1"));
        assertFalse(pv.exists("test3"));
    }


    /*
     * Od tu naprej testi za domačo nalogo
     */

    @Test
    public void testAsList(){
        List<String> originalList = Arrays.asList("5","4","2","1","3");
        pv.add("1");
        pv.add("2");
        pv.add("3");
        pv.add("4");
        pv.add("5");
        assertEquals(originalList, pv.asList());
    }

}