
import org.junit.*;
import static org.junit.Assert.*;

public class SeznamiUVTest {

    private SeznamiUV uv;

    public SeznamiUVTest() {
    }

    @Before
    public void setUp() {
        uv = new SeznamiUV();
    }

    @Test
    public void testPushBasic() {
        assertEquals("OK", uv.processInput("push Test1"));
        assertEquals("OK", uv.processInput("push Test2"));
    }

    @Test
    public void testPushMultipleWords() {
        assertEquals("OK", uv.processInput("push \"Test with multiple words\""));
        assertEquals("1", uv.processInput("count"));
        assertEquals("OK", uv.processInput("push \"Another test with multiple words\""));
        assertEquals("2", uv.processInput("count"));
    }

    @Test
    public void testPushNothing() {
        assertEquals("Error: please specify a string", uv.processInput("push"));
    }

    @Test
    public void testPopBasic() {
        assertEquals("OK", uv.processInput("push Test1"));
        assertEquals("OK", uv.processInput("push Test2"));
        assertEquals("Test2", uv.processInput("pop"));
        assertEquals("Test1", uv.processInput("pop"));
    }

    @Test
    public void testPopMultipleWords() {
        assertEquals("OK", uv.processInput("push \"Test with multiple words\""));
        assertEquals("OK", uv.processInput("push \"Another test with multiple words\""));
        assertEquals("2", uv.processInput("count"));
        assertEquals("Another test with multiple words", uv.processInput("pop"));
        assertEquals("1", uv.processInput("count"));
        assertEquals("Test with multiple words", uv.processInput("pop"));
        assertEquals("0", uv.processInput("count"));
    }

    @Test
    public void testPopNothing() {
        assertEquals("Error: stack is empty", uv.processInput("pop"));
        assertEquals("Error: please specify a string", uv.processInput("push"));
        assertEquals("Error: stack is empty", uv.processInput("pop"));
    }

    @Test
    public void testResetOnEmpty() {
        assertEquals("OK", uv.processInput("reset"));
    }

    @Test
    public void testResetOnFull() {
        assertEquals("OK", uv.processInput("push Test"));
        assertEquals("OK", uv.processInput("reset"));
        assertEquals("Error: stack is empty", uv.processInput("pop"));
        assertEquals("0", uv.processInput("count"));
    }

    @Test
    public void testCountOnEmpty() {
        assertEquals("0", uv.processInput("count"));
    }

    @Test(timeout = 100)
    public void testCountOne() {
        assertEquals("OK", uv.processInput("push Test"));
        assertEquals("1", uv.processInput("count"));
    }

    @Test(timeout = 100)
    public void testCountTwo() {
        assertEquals("OK", uv.processInput("push Test1"));
        assertEquals("OK", uv.processInput("push Test2"));
        assertEquals("2", uv.processInput("count"));
    }

    @Test
    public void testTopBasic() {
        assertEquals("OK", uv.processInput("push Test1"));
        assertEquals("OK", uv.processInput("push Test2"));
        assertEquals("OK", uv.processInput("top Test2"));
    }

    @Test
    public void testTopNotEqual() {
        assertEquals("OK", uv.processInput("push Test1"));
        assertEquals("OK", uv.processInput("push Test2"));
        assertEquals("2", uv.processInput("count"));
        assertEquals("Error: wrong element", uv.processInput("top Test"));
        assertEquals("2", uv.processInput("count"));
    }

    @Test
    public void testTopEmpty() {
        assertEquals("Error: stack is empty", uv.processInput("top Test"));
        assertEquals("Error: please specify a string", uv.processInput("push"));
        assertEquals("Error: stack is empty", uv.processInput("top Test"));
    }

    @Test
    public void testTopNothing() {
        assertEquals("Error: please specify a string", uv.processInput("top"));
    }

    @Test
    public void testSearchFound() {
        assertEquals("OK", uv.processInput("push Test1"));
        assertEquals("OK", uv.processInput("push Test2"));
        assertEquals("0", uv.processInput("search Test2"));
        assertEquals("OK", uv.processInput("push Test3"));
        assertEquals("2", uv.processInput("search Test1"));
    }

    @Test
    public void testTopNotFound() {
        assertEquals("-1", uv.processInput("search Test1"));
        assertEquals("OK", uv.processInput("push Test1"));
        assertEquals("OK", uv.processInput("push Test2"));
        assertEquals("-1", uv.processInput("search Test"));
    }

    @Test
    public void testSearchNothing() {
        assertEquals("Error: please specify a string", uv.processInput("search"));
    }



}
