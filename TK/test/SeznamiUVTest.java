
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
        assertEquals("OK", uv.processInput("s_add Test1"));
        assertEquals("OK", uv.processInput("s_add Test2"));
    }

    @Test
    public void testPushMultipleWords() {
        assertEquals("OK", uv.processInput("s_add \"Test with multiple words\""));
        assertEquals("1", uv.processInput("s_size"));
        assertEquals("OK", uv.processInput("s_add \"Another test with multiple words\""));
        assertEquals("2", uv.processInput("s_size"));
    }

    @Test
    public void testPushNothing() {
        assertEquals("Error: please specify a string", uv.processInput("s_add"));
    }

    @Test
    public void testPopBasic() {
        assertEquals("OK", uv.processInput("s_add Test1"));
        assertEquals("OK", uv.processInput("s_add Test2"));
        assertEquals("Test2", uv.processInput("s_remove_first"));
        assertEquals("Test1", uv.processInput("s_remove_first"));
    }

    @Test
    public void testPopMultipleWords() {
        assertEquals("OK", uv.processInput("s_add \"Test with multiple words\""));
        assertEquals("OK", uv.processInput("s_add \"Another test with multiple words\""));
        assertEquals("2", uv.processInput("s_size"));
        assertEquals("Another test with multiple words", uv.processInput("s_remove_first"));
        assertEquals("1", uv.processInput("s_size"));
        assertEquals("Test with multiple words", uv.processInput("s_remove_first"));
        assertEquals("0", uv.processInput("s_size"));
    }

    @Test
    public void testPopNothing() {
        assertEquals("Error: stack is empty", uv.processInput("s_remove_first"));
        assertEquals("Error: please specify a string", uv.processInput("s_add"));
        assertEquals("Error: stack is empty", uv.processInput("s_remove_first"));
    }

    @Test
    public void testResetOnEmpty() {
        assertEquals("OK", uv.processInput("s_reset"));
    }

    @Test
    public void testResetOnFull() {
        assertEquals("OK", uv.processInput("s_add Test"));
        assertEquals("OK", uv.processInput("s_reset"));
        assertEquals("Error: stack is empty", uv.processInput("s_remove_first"));
        assertEquals("0", uv.processInput("s_size"));
    }

    @Test
    public void testCountOnEmpty() {
        assertEquals("0", uv.processInput("s_size"));
    }

    @Test(timeout = 100)
    public void testCountOne() {
        assertEquals("OK", uv.processInput("s_add Test"));
        assertEquals("1", uv.processInput("s_size"));
    }

    @Test(timeout = 100)
    public void testCountTwo() {
        assertEquals("OK", uv.processInput("s_add Test1"));
        assertEquals("OK", uv.processInput("s_add Test2"));
        assertEquals("2", uv.processInput("s_size"));
    }

    @Test
    public void testTopBasic() {
        assertEquals("OK", uv.processInput("s_add Test1"));
        assertEquals("OK", uv.processInput("s_add Test2"));
        assertEquals("OK", uv.processInput("s_top Test2"));
    }

    @Test
    public void testTopNotEqual() {
        assertEquals("OK", uv.processInput("s_add Test1"));
        assertEquals("OK", uv.processInput("s_add Test2"));
        assertEquals("2", uv.processInput("s_size"));
        assertEquals("Error: wrong element", uv.processInput("s_top Test"));
        assertEquals("2", uv.processInput("s_size"));
    }

    @Test
    public void testTopEmpty() {
        assertEquals("Error: stack is empty", uv.processInput("s_top Test"));
        assertEquals("Error: please specify a string", uv.processInput("s_add"));
        assertEquals("Error: stack is empty", uv.processInput("s_top Test"));
    }

    @Test
    public void testTopNothing() {
        assertEquals("Error: please specify a string", uv.processInput("s_top"));
    }

    @Test
    public void testSearchFound() {
        assertEquals("OK", uv.processInput("s_add Test1"));
        assertEquals("OK", uv.processInput("s_add Test2"));
        assertEquals("0", uv.processInput("s_search Test2"));
        assertEquals("OK", uv.processInput("s_add Test3"));
        assertEquals("2", uv.processInput("s_search Test1"));
    }

    @Test
    public void testTopNotFound() {
        assertEquals("-1", uv.processInput("s_search Test1"));
        assertEquals("OK", uv.processInput("s_add Test1"));
        assertEquals("OK", uv.processInput("s_add Test2"));
        assertEquals("-1", uv.processInput("s_search Test"));
    }

    @Test
    public void testSearchNothing() {
        assertEquals("Error: please specify a string", uv.processInput("s_search"));
    }

    @Test
    public void testPqAdd() {
        assertEquals("OK", uv.processInput("pq_add Neki"));
    }

    @Test
    public void testPqRemoveFirst() {
        uv.processInput("pq_add Neki");
        assertEquals("Neki", uv.processInput("pq_remove_first"));
        assertEquals("Error: priority queue is empty", uv.processInput("pq_remove_first"));
    }

    @Test
    public void testPqGetFirst() {
        assertEquals("Error: priority queue is empty", uv.processInput("pq_get_first"));

        uv.processInput("pq_add Neki");
        assertEquals("Neki", uv.processInput("pq_get_first"));
        assertEquals("Neki", uv.processInput("pq_get_first"));
    }

    @Test
    public void testPqSize() {
        uv.processInput("pq_add Neki1");
        assertEquals("1", uv.processInput("pq_size"));
        uv.processInput("pq_add Neki2");
        assertEquals("2", uv.processInput("pq_size"));
        uv.processInput("pq_add Neki3");
        assertEquals("3", uv.processInput("pq_size"));
    }

    @Test
    public void testPqDepth() {
        uv.processInput("pq_add 1");
        assertEquals("1", uv.processInput("pq_depth"));


        uv.processInput("pq_add 2");
        uv.processInput("pq_add 3");
        assertEquals("2", uv.processInput("pq_depth"));

        uv.processInput("pq_add 4");
        uv.processInput("pq_add 5");
        assertEquals("3", uv.processInput("pq_depth"));
    }

    @Test
    public void testPqEmpty() {
        assertEquals("Priority queue is empty", uv.processInput("pq_isEmpty"));

        uv.processInput("pq_add Neki");
        assertEquals("Priority queue is not empty", uv.processInput("pq_isEmpty"));
    }


}
