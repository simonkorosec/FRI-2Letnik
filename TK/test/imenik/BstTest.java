package imenik;

import java.util.Comparator;
import org.junit.*;
import static org.junit.Assert.*;

class NonComparableClass {

    public NonComparableClass() {
    }
}

class StringComparator implements Comparator<String> {

    @Override
    public int compare(String s1, String s2) {
        return s1.compareTo(s2);
    }
}

public class BstTest {

    private Bst<String> bst;
    //PRIMER: uporaba razreda, ki ne implementira vmesnika Comparable
    //private Bst<NonComparableClass> nccBst;

    public BstTest() {
    }

    @Before
    public void setUp() {
        bst = new Bst<>(new StringComparator());
    }

    @Test
    public void testAddOne() {
        bst.add("Test");
        assertEquals(1, bst.size());
        assertEquals(1, bst.depth());
    }

    @Test
    public void testRemoveMultipleScenario2() {
        bst.add("Test4");
        bst.add("Test2");
        bst.add("Test1");
        bst.add("Test3");
        bst.add("Test5");
        bst.remove("Test2");
        assertEquals(4, bst.size());
        assertEquals(3, bst.depth());
        bst.remove("Test5");
        assertEquals(3, bst.size());
        assertEquals(3, bst.depth());
    }

    @Test
    public void testGetOne() {
        bst.add("Test");
        assertEquals("Test", bst.getFirst());
        assertEquals(1, bst.size());
        assertEquals(1, bst.depth());
    }

    @Test
    public void testGetMultiple() {
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
    public void testGetOnEmpty() {
        bst.getFirst();
    }

    @Test
    public void testDepthOnEmpty() {
        assertEquals(0, bst.depth());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(bst.isEmpty());
        bst.add("Test");
        assertFalse(bst.isEmpty());
    }

    @Test
    public void testExists() {
        bst.add("Test");
        assertTrue(bst.exists("Test"));
        assertEquals(1, bst.size());
        assertEquals(1, bst.depth());
        bst.removeFirst();
        assertFalse(bst.exists("Test"));
        assertEquals(0, bst.size());
        assertEquals(0, bst.depth());
    }
}
