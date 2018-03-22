import org.junit.*;

import static org.junit.Assert.*;

/*
Izziv:
    search - skladTest za 0,1,n,-1
           - seznamiUvTest: specify string

 */


public class SeznamiUVTest {

    static SeznamiUV uv;

    @BeforeClass
    public static void setUpClass() {
        uv = new SeznamiUV();
    }

    @After
    public void tearDown() {
        uv.processInput("reset");
    }


    @Before
    public void setUp() {
        // uv = new SeznamiUV();
    }

    @Test
    public void testPushBasic() {
        System.out.println("testPushBasic");
        assertEquals("OK", uv.processInput("push Test1"));
        assertEquals("OK", uv.processInput("push Test2"));
    }

    @Test
    public void testPushMultipleWords() {
        System.out.println("testPushMultipleWords");
        assertEquals("OK", uv.processInput("push \"Test with multiple word\""));
        assertEquals("1", uv.processInput("count"));
        assertEquals("OK", uv.processInput("push \"Another test with multiple words\""));
        assertEquals("2", uv.processInput("count"));
    }

    @Test
    public void testPushNothing() {
        System.out.println("testPushNothing");
        assertEquals("Error: please specify a string", uv.processInput("push"));
    }

    @Test
    public void testPopBasic() {
        System.out.println("testPopBasic");
        assertEquals("OK", uv.processInput("push Test1"));
        assertEquals("OK", uv.processInput("push Test2"));
        assertEquals("Test2", uv.processInput("pop"));
        assertEquals("Test1", uv.processInput("pop"));
    }

    @Test
    public void testPopMultipleWords() {
        System.out.println("testPopMultipleWords");
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
        System.out.println("testPopNothing");
        assertEquals("Error: stack is empty", uv.processInput("pop"));
        assertEquals("Error: please specify a string", uv.processInput("push"));
        assertEquals("Error: stack is empty", uv.processInput("pop"));
    }

    @Test
    public void testResetOnEmpty() {
        System.out.println("testResetOnEmpty");
        assertEquals("OK", uv.processInput("reset"));
    }

    @Test
    public void testResetOnFull() {
        System.out.println("testResetOnFull");
        assertEquals("OK", uv.processInput("push Test"));
        assertEquals("OK", uv.processInput("reset"));
        assertEquals("Error: stack is empty", uv.processInput("pop"));
        assertEquals("0", uv.processInput("count"));
    }

    @Test
    public void testCountOnEmpty() {
        System.out.println("testCountOnEmpty");
        assertEquals("0", uv.processInput("count"));
    }

    @Test
    public void testCountOne() {
        System.out.println("testCountOne");
        assertEquals("OK", uv.processInput("push Test"));
        assertEquals("1", uv.processInput("count"));
    }

    @Test
    public void testCountTwo() {
        System.out.println("testCountTwo");
        assertEquals("OK", uv.processInput("push Test1"));
        assertEquals("OK", uv.processInput("push Test2"));
        assertEquals("2", uv.processInput("count"));
    }

    @Test
    public void testTopSingleWord() {
        System.out.println("testTopSingleWord");
        assertEquals("OK", uv.processInput("push Test1"));
        assertEquals("OK", uv.processInput("push Test2"));
        assertEquals("OK", uv.processInput("top Test2"));
    }

    @Test
    public void testTopMultipleWords() {
        assertEquals("OK", uv.processInput("push \"Test with multiple words\""));
        assertEquals("OK", uv.processInput("top \"Test with multiple words\""));
    }

    @Test
    public void testTopEmpty() {
        assertEquals("Error: stack is empty", uv.processInput("top test"));
    }

    @Test
    public void testTopNoString() {
        assertEquals("Error: please specify a string", uv.processInput("top"));
    }

    @Test
    public void testTopWrongElement() {
        assertEquals("OK", uv.processInput("push Test1"));
        assertEquals("Error: wrong element", uv.processInput("top Test2"));
    }

    @Test
    public void testSearchBasic0() {
        uv.processInput("push Test1");
        assertEquals("0", uv.processInput("search Test1"));
    }

    @Test
    public void testSearchBasic1() {
        uv.processInput("push Test2");
        uv.processInput("push Test1");
        assertEquals("1", uv.processInput("search Test2"));
    }

    @Test
    public void testSearchBasicN() {
        uv.processInput("push Test3");
        uv.processInput("push Test2");
        uv.processInput("push Test1");
        assertEquals("2", uv.processInput("search Test3"));
    }

    @Test
    public void testSearchNotContains() {
        uv.processInput("push Test3");
        uv.processInput("push Test2");
        uv.processInput("push Test1");
        assertEquals("-1", uv.processInput("search Test4"));
    }

    @Test
    public void testSearchErrorString() {
        uv.processInput("push Test1");
        assertEquals("Error: please specify a string", uv.processInput("search"));
    }
}