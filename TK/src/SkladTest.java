import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SkladTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void push() {
        Sklad sklad = new Sklad();
        String a = "a";
        sklad.push(a);
    }

    @Test
    public void pop() {
        Sklad<String> sklad = new Sklad<>();
        String a = "test";
        sklad.push(a);
        String b = sklad.pop();
        assertEquals("test", b);
    }

    @Test
    public void testWithTwoElements() {
        Sklad<String> instance = new Sklad<>();
        String a = "Prvi test";
        String b = "Drugi test";
        instance.push(a);
        instance.push(b);
        assertEquals(b, instance.pop());
        assertEquals(a, instance.pop());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testPopOnEmptyStack() {
        Sklad<String> instance = new Sklad<>();
        String a = instance.pop();
    }

    @Test
    public void testIsEmptyOnEmpty() {
        Sklad<String> instance = new Sklad<>();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testIsEmptyOnFull() {
        Sklad<String> instance = new Sklad<>();
        instance.push("Test");
        assertFalse(instance.isEmpty());
    }

    @Test
    public void peek() {
        Sklad<String> sklad = new Sklad<>();
        String a = "a";
        String b = "b";
        String c = "c";
        sklad.push(a);
        sklad.push(b);
        sklad.push(c);

        assertEquals(c, sklad.peek());
        assertEquals(c, sklad.peek());
        assertEquals(c, sklad.pop());
        assertEquals(b, sklad.peek());
        assertEquals(b, sklad.peek());
        assertEquals(b, sklad.pop());
    }

    @Test
    public void count() {
        Sklad<String> sklad = new Sklad<>();
        assertEquals(0, sklad.count());
        String a = "a";
        String b = "b";
        String c = "c";
        sklad.push(a);
        sklad.push(b);
        sklad.push(c);

        assertEquals(3, sklad.count());
        assertEquals(c, sklad.pop());
        assertEquals(b, sklad.pop());
        assertEquals(a, sklad.pop());
        assertEquals(0, sklad.count());
    }
}