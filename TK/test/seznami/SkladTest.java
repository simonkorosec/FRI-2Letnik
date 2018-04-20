package seznami;

import org.junit.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class SkladTest {

    private static Sklad<String> sk;

    public SkladTest() {
    }

    @BeforeClass
    public static void setUpOnce() {
        sk = new Sklad<>();
    }

    @Before
    public void setUp() {
        while (!sk.isEmpty()) {
            sk.pop();
        }
    }

    @Test
    public void testPush() {
        String a = "Test";
        sk.push(a);
    }

    @Test
    public void testPop() {
        String a = "Test";
        sk.push(a);
        String b = sk.pop();
        assertEquals("Test", b);
    }

    @Test
    public void testWithTwoElements() {
        String a = "Prvi element";
        String b = "Drugi element";
        sk.push(a);
        sk.push(b);
        assertEquals(b, sk.pop());
        assertEquals(a, sk.pop());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testPopOnEmptyStack() {
        sk.pop();
    }

    @Test
    public void testIsEmptyOnEmpty() {
        assertTrue(sk.isEmpty());
    }

    @Test
    public void testIsEmptyOnFull() {
        sk.push("Test");
        assertFalse(sk.isEmpty());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testPeekOnEmptyStack() {
        sk.peek();
    }

    @Test
    public void testPeekOnFullStack() {
        String a = "Vrednost 1";
        String b = "Vrednost 2";
        sk.push(a);
        sk.push(b);
        String c = sk.peek();
        assertEquals(c, b);
    }

    @Test
    public void testPeekSame() {
        sk.push("Test1");
        sk.push("Test2");
        sk.push("Test3");
        assertEquals("Test3", sk.peek());
        assertEquals("Test3", sk.pop());
        assertEquals("Test2", sk.pop());
        assertEquals("Test1", sk.pop());
        assertTrue(sk.isEmpty());
    }

    @Test
    public void testCountEmpty() {
        assertEquals(0, sk.count());
    }

    @Test(timeout = 100)
    public void testCountNonEmpty() {
        sk.push("Vrednost 1");
        sk.push("Vrednost 2");
        sk.push("Vrednost 3");
        assertEquals(3, sk.count());
    }

    @Test
    public void testCountSame() {
        sk.push("Test1");
        sk.push("Test2");
        sk.push("Test3");
        assertEquals(3, sk.count());
        assertEquals("Test3", sk.pop());
        assertEquals("Test2", sk.pop());
        assertEquals("Test1", sk.pop());
        assertTrue(sk.isEmpty());
    }

    @Test
    public void testTopTrue() {
        sk.push("Test");
        assertTrue(sk.top("Test"));
    }

    @Test
    public void testTopFalse() {
        sk.push("Test1");
        assertFalse(sk.top("Test2"));
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testTopEmpty() {
        assertFalse(sk.top("Test"));
    }

    @Test
    public void testTopSame() {
        sk.push("Test1");
        sk.push("Test2");
        sk.push("Test3");
        assertTrue(sk.top("Test3"));
        assertEquals("Test3", sk.pop());
        assertEquals("Test2", sk.pop());
        assertEquals("Test1", sk.pop());
        assertTrue(sk.isEmpty());
    }

    @Test
    public void testSearchEmpty() {
        assertEquals(-1, sk.search("Test"));
    }

    @Test(timeout = 100)
    public void testSearchFoundTop() {
        sk.push("Vrednost 1");
        sk.push("Vrednost 2");
        sk.push("Vrednost 3");
        assertEquals(0, sk.search("Vrednost 3"));
    }

    @Test(timeout = 100)
    public void testSearchFoundNonTop() {
        sk.push("Vrednost 1");
        sk.push("Vrednost 2");
        sk.push("Vrednost 3");
        assertEquals(2, sk.search("Vrednost 1"));
    }

    @Test(timeout = 100)
    public void testSearchNotFound() {
        sk.push("Vrednost 1");
        sk.push("Vrednost 2");
        sk.push("Vrednost 3");
        assertEquals(-1, sk.search("Vrednost"));
    }

    @Test
    public void testSearchSame() {
        sk.push("Test1");
        sk.push("Test2");
        sk.push("Test3");
        assertEquals(1, sk.search("Test2"));
        assertEquals("Test3", sk.pop());
        assertEquals("Test2", sk.pop());
        assertEquals("Test1", sk.pop());
        assertTrue(sk.isEmpty());
    }

    @Test
    public void testRemove() {
        sk.add("test1");
        sk.add("test2");
        assertEquals("test1", sk.remove("test1"));
        assertNull(sk.remove("test1"));
    }

    @Test
    public void testExists() {
        sk.add("test1");
        sk.add("test2");
        assertTrue(sk.exists("test1"));
        assertFalse(sk.exists("test3"));
    }

    /*
     * Od tu naprej testi za domačo nalogo
     */

    @Test
    public void testAsList(){
        List<String> originalList = Arrays.asList("1","2","3","4","5");
        sk.add("5");
        sk.add("4");
        sk.add("3");
        sk.add("2");
        sk.add("1");
        assertEquals(originalList, sk.asList());
    }
}
