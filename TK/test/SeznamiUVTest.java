
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
    public void testSAddBasic() {
        assertEquals("OK", uv.processInput("s_add Test1"));
        assertEquals("OK", uv.processInput("s_add Test2"));
    }

    // @Ignore("To be implemented at a later stage...")
    @Test
    public void testSAddMultipleWords() {
        assertEquals("OK", uv.processInput("s_add \"Test with multiple words\""));
        assertEquals("1", uv.processInput("s_size"));
        assertEquals("OK", uv.processInput("s_add \"Another test with multiple words\""));
        assertEquals("2", uv.processInput("s_size"));
    }

    @Test
    public void testSAddNothing() {
        assertEquals("Error: please specify a string", uv.processInput("s_add"));
    }

    @Test
    public void testSRemoveFirstBasic() {
        assertEquals("OK", uv.processInput("s_add Test1"));
        assertEquals("OK", uv.processInput("s_add Test2"));
        assertEquals("Test2", uv.processInput("s_remove_first"));
        assertEquals("Test1", uv.processInput("s_remove_first"));
    }

    @Test
    public void testRemoveFirstMultipleWords() {
        assertEquals("OK", uv.processInput("s_add \"Test with multiple words\""));
        assertEquals("OK", uv.processInput("s_add \"Another test with multiple words\""));
        assertEquals("2", uv.processInput("s_size"));
        assertEquals("Another test with multiple words", uv.processInput("s_remove_first"));
        assertEquals("1", uv.processInput("s_size"));
        assertEquals("Test with multiple words", uv.processInput("s_remove_first"));
        assertEquals("0", uv.processInput("s_size"));
    }

    @Test
    public void testSRemoveFirstNothing() {
        assertEquals("Error: stack is empty", uv.processInput("s_remove_first"));
        assertEquals("Error: please specify a string", uv.processInput("s_add"));
        assertEquals("Error: stack is empty", uv.processInput("s_remove_first"));
    }

    @Test
    public void testSGetFirst() {
        assertEquals("OK", uv.processInput("s_add Test1"));
        assertEquals("OK", uv.processInput("s_add Test3"));
        assertEquals("OK", uv.processInput("s_add Test2"));
        assertEquals("Test2", uv.processInput("s_get_first"));
        assertEquals("Test2", uv.processInput("s_get_first"));
        assertEquals("Test2", uv.processInput("s_get_first"));
    }

    @Test
    public void testSGetFirstEmpty() {
        assertEquals("Error: stack is empty", uv.processInput("s_get_first"));
        assertEquals("Error: please specify a string", uv.processInput("s_add"));
        assertEquals("Error: stack is empty", uv.processInput("s_get_first"));
    }

    @Test
    public void testSSizetOnEmpty() {
        assertEquals("0", uv.processInput("s_size"));
    }

    @Test(timeout = 100)
    public void testSSizeOne() {
        assertEquals("OK", uv.processInput("s_add Test"));
        assertEquals("1", uv.processInput("s_size"));
    }

    @Test(timeout = 100)
    public void testSSizeTwo() {
        assertEquals("OK", uv.processInput("s_add Test1"));
        assertEquals("OK", uv.processInput("s_add Test2"));
        assertEquals("2", uv.processInput("s_size"));
    }

    @Test
    public void testSDepth() {
        assertEquals("0", uv.processInput("s_depth"));
        assertEquals("OK", uv.processInput("s_add Test1"));
        assertEquals("OK", uv.processInput("s_add Test2"));
        assertEquals("OK", uv.processInput("s_add Test3"));
        assertEquals("3", uv.processInput("s_depth"));
    }

    @Test
    public void testSIsEmptyEmpty() {
        assertEquals("Stack is empty.", uv.processInput("s_is_empty"));
        assertEquals("Error: please specify a string", uv.processInput("s_add"));
        assertEquals("Stack is empty.", uv.processInput("s_is_empty"));
    }

    @Test
    public void testSIsEmptyNotEmpty() {
        assertEquals("OK", uv.processInput("s_add Test1"));
        assertEquals("OK", uv.processInput("s_add Test2"));
        assertEquals("OK", uv.processInput("s_add Test3"));
        assertEquals("Stack is not empty.", uv.processInput("s_is_empty"));
    }

    @Test
    public void testSResetOnEmpty() {
        assertEquals("OK", uv.processInput("s_reset"));
    }

    @Test
    public void testSResetOnFull() {
        assertEquals("OK", uv.processInput("s_add Test"));
        assertEquals("OK", uv.processInput("s_reset"));
        assertEquals("Error: stack is empty", uv.processInput("s_remove_first"));
        assertEquals("0", uv.processInput("s_size"));
    }

    @Test
    public void testSTopBasic() {
        assertEquals("OK", uv.processInput("s_add Test1"));
        assertEquals("OK", uv.processInput("s_add Test2"));
        assertEquals("OK", uv.processInput("s_top Test2"));
    }

    @Test
    public void testSTopNotEqual() {
        assertEquals("OK", uv.processInput("s_add Test1"));
        assertEquals("OK", uv.processInput("s_add Test2"));
        assertEquals("2", uv.processInput("s_size"));
        assertEquals("Error: wrong element", uv.processInput("s_top Test"));
        assertEquals("2", uv.processInput("s_size"));
    }

    @Test
    public void testSTopEmpty() {
        assertEquals("Error: stack is empty", uv.processInput("s_top Test"));
        assertEquals("Error: please specify a string", uv.processInput("s_add"));
        assertEquals("Error: stack is empty", uv.processInput("s_top Test"));
    }

    @Test
    public void testSTopNothing() {
        assertEquals("Error: please specify a string", uv.processInput("s_top"));
    }

    @Test
    public void testSSearchFound() {
        assertEquals("OK", uv.processInput("s_add Test1"));
        assertEquals("OK", uv.processInput("s_add Test2"));
        assertEquals("0", uv.processInput("s_search Test2"));
        assertEquals("OK", uv.processInput("s_add Test3"));
        assertEquals("2", uv.processInput("s_search Test1"));
    }

    @Test
    public void testSSearchNotFound() {
        assertEquals("-1", uv.processInput("s_search Test1"));
        assertEquals("OK", uv.processInput("s_add Test1"));
        assertEquals("OK", uv.processInput("s_add Test2"));
        assertEquals("-1", uv.processInput("s_search Test"));
    }

    @Test
    public void testSSearchNothing() {
        assertEquals("Error: please specify a string", uv.processInput("s_search"));
    }

    @Test
    public void testPQAdd() {
        assertEquals("OK", uv.processInput("pq_add Test1"));
        assertEquals("OK", uv.processInput("pq_add Test2"));
    }

    @Test
    public void testPQAddNothing() {
        assertEquals("Error: please specify a string", uv.processInput("pq_add"));
    }

    @Test
    public void testPQRemoveFirst() {
        assertEquals("OK", uv.processInput("pq_add Test1"));
        assertEquals("OK", uv.processInput("pq_add Test3"));
        assertEquals("OK", uv.processInput("pq_add Test2"));
        assertEquals("Test3", uv.processInput("pq_remove_first"));
        assertEquals("Test2", uv.processInput("pq_remove_first"));
        assertEquals("Test1", uv.processInput("pq_remove_first"));
    }

    @Test
    public void testPQRemoveFirstEmpty() {
        assertEquals("Error: priority queue is empty", uv.processInput("pq_remove_first"));
        assertEquals("Error: please specify a string", uv.processInput("pq_add"));
        assertEquals("Error: priority queue is empty", uv.processInput("pq_remove_first"));
    }

    @Test
    public void testPQGetFirst() {
        assertEquals("OK", uv.processInput("pq_add Test1"));
        assertEquals("OK", uv.processInput("pq_add Test3"));
        assertEquals("OK", uv.processInput("pq_add Test2"));
        assertEquals("Test3", uv.processInput("pq_get_first"));
        assertEquals("Test3", uv.processInput("pq_get_first"));
        assertEquals("Test3", uv.processInput("pq_get_first"));
    }

    @Test
    public void testPQGetFirstEmpty() {
        assertEquals("Error: priority queue is empty", uv.processInput("pq_get_first"));
        assertEquals("Error: please specify a string", uv.processInput("pq_add"));
        assertEquals("Error: priority queue is empty", uv.processInput("pq_get_first"));
    }

    @Test
    public void testPQSize() {
        assertEquals("0", uv.processInput("pq_size"));
        assertEquals("OK", uv.processInput("pq_add Test1"));
        assertEquals("OK", uv.processInput("pq_add Test2"));
        assertEquals("OK", uv.processInput("pq_add Test3"));
        assertEquals("3", uv.processInput("pq_size"));
    }

    @Test
    public void testPQDepth() {
        assertEquals("0", uv.processInput("pq_depth"));
        assertEquals("OK", uv.processInput("pq_add Test1"));
        assertEquals("OK", uv.processInput("pq_add Test2"));
        assertEquals("OK", uv.processInput("pq_add Test3"));
        assertEquals("2", uv.processInput("pq_depth"));
    }

    @Test
    public void testPQIsEmptyEmpty() {
        assertEquals("Priority queue is empty.", uv.processInput("pq_is_empty"));
        assertEquals("Error: please specify a string", uv.processInput("pq_add"));
        assertEquals("Priority queue is empty.", uv.processInput("pq_is_empty"));
    }

    @Test
    public void testPQIsEmptyNotEmpty() {
        assertEquals("OK", uv.processInput("pq_add Test1"));
        assertEquals("OK", uv.processInput("pq_add Test2"));
        assertEquals("OK", uv.processInput("pq_add Test3"));
        assertEquals("Priority queue is not empty.", uv.processInput("pq_is_empty"));
    }
}
