package imenik;

import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SkladTest {

    public SkladTest() {
    }

    @Test
    public void testPush() {
        Sklad<String> instance = new Sklad<>(new StringComparator());
        String a = "Test";
        instance.push(a);
    }

    @Test
    public void testPop() {
        Sklad<String> instance = new Sklad<>(new StringComparator());
        String a = "Test";
        instance.push(a);
        String b = instance.pop();
        assertEquals("Test", b);
    }

    @Test
    public void testWithTwoElements() {
        Sklad<String> instance = new Sklad<>(new StringComparator());
        String a = "Prvi test";
        String b = "Drugi test";
        instance.push(a);
        instance.push(b);
        assertEquals(b, instance.pop());
        assertEquals(a, instance.pop());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testPopOnEmptyStack() {
        Sklad<String> instance = new Sklad<>(new StringComparator());
        String a = instance.pop();
    }

    @Test
    public void testIsEmptyOnEmpty() {
        Sklad<String> instance = new Sklad<>(new StringComparator());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testIsEmptyOnFull() {
        Sklad<String> instance = new Sklad<>(new StringComparator());
        instance.push("Test");
        assertFalse(instance.isEmpty());
    }
}
