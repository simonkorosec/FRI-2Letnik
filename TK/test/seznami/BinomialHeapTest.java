package seznami;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class BinomialHeapTest {


    private BinomialHeap<String> bh;


    @Before
    public void setUp() {
        bh = new BinomialHeap<>();
    }


    @Test
    public void add() {
        bh.add("1");
        bh.add("2");
        bh.add("3");
        bh.add("4");
        assertEquals(4, bh.size());
    }

    @Test
    public void removeFirst() {
        bh.add("5");
        assertEquals("5", bh.removeFirst());
        assertEquals(0, bh.size());


        bh.add("1");
        bh.add("2");
        bh.add("3");
        bh.add("4");
        assertEquals("4", bh.removeFirst());
        assertEquals(3, bh.size());
        assertEquals(Arrays.asList("3", "1", "2"), bh.asList());

        bh.add("4");
        bh.add("5");
        bh.add("6");
        bh.add("7");
        bh.add("8");
        assertEquals(8, bh.size());
        assertEquals("8", bh.removeFirst());
        assertEquals(7, bh.size());
        assertEquals(Arrays.asList("7", "5", "6", "1", "2", "3", "4"), bh.asList());
    }

    @Test
    public void getFirst() {
        bh.add("1");
        assertEquals("1", bh.getFirst());
        bh.add("2");
        assertEquals("2", bh.getFirst());

        bh.add("8");
        bh.add("3");
        bh.add("4");
        bh.add("5");
        assertEquals("8", bh.getFirst());

    }

    @Test
    public void size() {
        assertEquals(0, bh.size());
        bh.add("1");
        assertEquals(1, bh.size());
        bh.add("1");
        bh.add("1");
        bh.add("1");
        bh.add("1");
        bh.add("1");
        bh.add("1");
        assertEquals(7, bh.size());
    }

    @Test
    public void depth() {
        assertEquals(0, bh.depth());
        bh.add("1");
        assertEquals(0, bh.depth());
        bh.add("2");
        assertEquals(1, bh.depth());
        bh.add("3");
        assertEquals(1, bh.depth());
        bh.add("4");
        bh.add("5");
        bh.add("6");
        bh.add("7");
        bh.add("8");
        assertEquals(3, bh.depth());
    }

    @Test
    public void isEmpty() {
        assertTrue(bh.isEmpty());
        bh.add("1");
        assertFalse(bh.isEmpty());
    }

    @Test
    public void remove() {
        bh.add("1");
        bh.add("2");
        bh.add("3");
        bh.add("4");
        bh.add("5");
        bh.add("6");
        bh.add("7");

        assertEquals(Arrays.asList("7", "5", "6", "1", "2", "3", "4"), bh.asList());

        assertEquals("4", bh.remove("4"));

        assertEquals(Arrays.asList("3", "7", "1", "2", "5", "6"), bh.asList());

        assertEquals("1", bh.remove("1"));

        assertEquals(Arrays.asList("5", "2", "6", "3", "7"), bh.asList());

    }

    @Test
    public void exists() {
        assertFalse(bh.exists("25"));

        bh.add("1");
        bh.add("2");
        bh.add("3");
        bh.add("4");
        bh.add("5");
        bh.add("6");
        bh.add("7");
        assertEquals(Arrays.asList("7", "5", "6", "1", "2", "3", "4"), bh.asList());

        assertTrue(bh.exists("1"));
        assertFalse(bh.exists("25"));
    }

    @Test
    public void asList() {
        bh.add("1");
        assertEquals(Collections.singletonList("1"), bh.asList());

        bh.add("2");
        assertEquals(Arrays.asList("1", "2"), bh.asList());

        bh.add("3");
        assertEquals(Arrays.asList("3", "1", "2"), bh.asList());

        bh.add("4");
        assertEquals(Arrays.asList("1", "2", "3", "4"), bh.asList());

        bh.add("5");
        assertEquals(Arrays.asList("5", "1", "2", "3", "4"), bh.asList());

    }

    private String insertK(int k) {
        StringBuilder s = new StringBuilder();
        for (int i = 1; i <= k; i++) {
            if (i < 10) {
                bh.add("0" + Integer.toString(i));
                s.append("0").append(i).append(" ");
            } else {
                bh.add(Integer.toString(i));
                s.append(i).append(" ");
            }
        }
        return s.toString();
    }

    @Test
    public void testAddBig() {
        String s = insertK(32);
        assertEquals(32, bh.size());
        assertEquals(Arrays.asList(s.split(" ")), bh.asList());
        assertEquals("32", bh.remove("32"));
        s = "31 29 30 25 26 27 28 17 18 19 20 21 22 23 24 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16";
        assertEquals(Arrays.asList(s.split(" ")), bh.asList());
    }

    @Test
    public void testRemoveFirstBig() {
        StringBuilder s = new StringBuilder();
        for (int i = 15; i > 0; i--) {
            if (i < 10) {
                bh.add("0" + Integer.toString(i));
                s.insert(0, "0" + i + " ");
            } else {
                bh.add(Integer.toString(i));
                s.insert(0, i + " ");
            }
        }
        assertEquals(Arrays.asList(s.toString().split(" ")), bh.asList());
        assertEquals("15", bh.getFirst());
        assertEquals("15", bh.removeFirst());

        s = new StringBuilder("01 14 02 03 12 13 04 05 06 07 08 09 10 11");
        assertEquals(Arrays.asList(s.toString().split(" ")), bh.asList());
    }
    /* Testi napak */

    @Test(expected = NoSuchElementException.class)
    public void testRemoveFirstEmpty() {
        bh.removeFirst();
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetFirstEmpty() {
        bh.getFirst();
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveNotFound() {
        bh.add("2");
        bh.remove("3");
    }
}