package seznami;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

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
        bh.add("3");
        assertEquals("3", bh.getFirst());
        bh.add("4");
        assertEquals("4", bh.getFirst());
        bh.add("5");
        assertEquals("5", bh.getFirst());
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
    }

    @Test
    public void exists() {
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
}