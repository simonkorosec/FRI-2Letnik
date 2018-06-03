package seznami;

import org.junit.*;

import java.io.IOException;

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
    public void testUse() {
        assertEquals("Error: enter command", uv.processInput(""));
        assertEquals("Error: please specify a data structure (use {pv|sk|bst|bk})", uv.processInput("add"));
        assertEquals("Error: please specify a correct data structure type {pv|sk|bst|bk}", uv.processInput("use add"));
        assertEquals("Error: please specify a data structure type {pv|sk|bst|bk}", uv.processInput("use"));

        assertEquals("OK", uv.processInput("use sk"));
        assertEquals("OK", uv.processInput("use pv"));
        assertEquals("OK", uv.processInput("use bst"));
        assertEquals("OK", uv.processInput("use bk"));
    }

    @Test
    public void testUseSklad() {
        runAllTests("sk");
    }

    @Test
    public void testUsePrioritetnaVrsta() {
        runAllTests("pv");
    }

    @Test
    public void testUseBst() {
        runAllTests("bst");
    }

    @Test
    public void testUseBk() {
        runAllTests("bk");
    }

    @Test
    public void testErrors() {
        assertEquals("OK", uv.processInput("use bst"));
        assertEquals("Error: invalid command", uv.processInput("nekii"));
        assertEquals("Error: data structure is empty", uv.processInput("get_first"));
        assertEquals("OK", uv.processInput("add 5"));
        assertEquals("Error: Duplicated entry", uv.processInput("add 5"));
    }

    @Test
    public void testErrorNoSpace() {
        assertEquals("OK", uv.addTmp(new BstMock<>(), "bstMock"));

        assertEquals("OK", uv.processInput("use bstMock"));
        assertEquals("Error: IO error There is not enough space on the disk", uv.processInput("save test.bin"));
    }

    // *****************
    // POMOZNE METODE
    // *****************
    private void runAllTests(String struktura) {
        assertEquals("OK", uv.processInput("use " + struktura));
        switch (struktura) {
            case "sk":
                testSklad();
                break;
            case "pv":
                testPrioritetnaVrsta();
                break;
            case "bst":
                testBst();
                break;
            case "bk":
                testBk();
                break;
        }
        reset();
        testAdd();
        reset();
        testAddNothing();
        reset();
        testRemoveFirst();
        reset();
        testRemoveFirstNothing();
        reset();
        testRemove();
        reset();
        testAddNothing();
        reset();
        testGetFirst();
        reset();
        testGetFirstNothing();
        reset();
        testSizeOnEmpty();
        reset();
        testSizeOne();
        reset();
        testSizeTwo();
        reset();

        if (struktura.equals("bk")) {
            testDepthBk();
        } else {
            testDepthOnEmpty();
            reset();
            testDepthOne();
            reset();
            testDepthTwo();
        }
        reset();

        testExists();
        reset();
        testIsEmptyEmpty();
        reset();
        testIsEmptyNotEmpty();
        reset();
        testResetOnEmpty();
        reset();
        testResetOnFull();
        reset();
        testAsList(struktura);
    }

    private void testAsList(String struktura) {
        for (int i = 15; i > 0; i--) {
            if (i < 10) {
                uv.processInput("add 0" + i);
            } else {
                uv.processInput("add " + i);
            }
        }

        switch (struktura) {
            case "sk":
                assertEquals("01 02 03 04 05 06 07 08 09 10 11 12 13 14 15", uv.processInput("asList"));
                break;
            case "pv":
                assertEquals("15 14 13 12 11 10 09 08 07 06 05 04 03 02 01", uv.processInput("asList"));
                break;
            case "bst":
                assertEquals("01 02 03 04 05 06 07 08 09 10 11 12 13 14 15", uv.processInput("asList"));
                break;
            case "bk":
                assertEquals("01 02 03 04 05 06 07 08 09 10 11 12 13 14 15", uv.processInput("asList"));
                break;
        }


    }

    private void reset() {
        uv.processInput("reset");
    }

    private void testAdd() {
        assertEquals("OK", uv.processInput("add Test1"));
        assertEquals("OK", uv.processInput("add Test2"));
    }

    private void testAddNothing() {
        assertEquals("Error: please specify a string", uv.processInput("add"));
    }

    private void testRemoveFirst() {
        assertEquals("OK", uv.processInput("add Test"));
        assertEquals("Test", uv.processInput("remove_first"));
    }

    private void testRemoveFirstNothing() {
        assertEquals("Error: data structure is empty", uv.processInput("remove_first"));
        assertEquals("Error: please specify a string", uv.processInput("add"));
        assertEquals("Error: data structure is empty", uv.processInput("remove_first"));
    }

    private void testGetFirst() {
        assertEquals("OK", uv.processInput("add Test"));
        assertEquals("Test", uv.processInput("get_first"));
    }

    private void testGetFirstNothing() {
        assertEquals("Error: data structure is empty", uv.processInput("get_first"));
        assertEquals("Error: please specify a string", uv.processInput("add"));
        assertEquals("Error: data structure is empty", uv.processInput("get_first"));
    }

    private void testSizeOnEmpty() {
        assertEquals("0", uv.processInput("size"));
    }

    private void testSizeOne() {
        assertEquals("OK", uv.processInput("add Test"));
        assertEquals("1", uv.processInput("size"));
    }

    private void testSizeTwo() {
        assertEquals("OK", uv.processInput("add Test1"));
        assertEquals("OK", uv.processInput("add Test2"));
        assertEquals("2", uv.processInput("size"));
    }

    private void testDepthOnEmpty() {
        assertEquals("0", uv.processInput("depth"));
    }

    private void testDepthOne() {
        assertEquals("OK", uv.processInput("add Test"));
        assertEquals("1", uv.processInput("depth"));
    }

    private void testDepthTwo() {
        assertEquals("OK", uv.processInput("add Test1"));
        assertEquals("OK", uv.processInput("add Test2"));
        assertEquals("2", uv.processInput("depth"));
    }

    private void testIsEmptyEmpty() {
        assertEquals("Data structure is empty.", uv.processInput("is_empty"));
        assertEquals("Error: please specify a string", uv.processInput("add"));
        assertEquals("Data structure is empty.", uv.processInput("is_empty"));
    }

    private void testIsEmptyNotEmpty() {
        assertEquals("OK", uv.processInput("add Test1"));
        assertEquals("OK", uv.processInput("add Test2"));
        assertEquals("OK", uv.processInput("add Test3"));
        assertEquals("Data structure is not empty.", uv.processInput("is_empty"));
    }

    private void testResetOnEmpty() {
        assertEquals("OK", uv.processInput("reset"));
    }

    private void testResetOnFull() {
        assertEquals("OK", uv.processInput("add Test"));
        assertEquals("OK", uv.processInput("reset"));
        assertEquals("Error: data structure is empty", uv.processInput("remove_first"));
        assertEquals("0", uv.processInput("size"));
    }

    private void testExists() {
        assertEquals("Error: please specify a string", uv.processInput("exists"));
        assertEquals("OK", uv.processInput("add Test3"));
        assertEquals("1", uv.processInput("size"));
        assertEquals("Element exists in data structure.", uv.processInput("exists Test3"));
        assertEquals("Element doesn't exist in data structure.", uv.processInput("exists neki"));
    }

    private void testRemove() {
        testAddSequence();
        assertEquals("Error: please specify a string", uv.processInput("remove"));
        assertEquals("Test3", uv.processInput("remove Test3"));
        assertEquals("Test1", uv.processInput("remove Test1"));

    }

    private void testAddSequence() {
        assertEquals("OK", uv.processInput("add Test4"));
        assertEquals("OK", uv.processInput("add Test2"));
        assertEquals("OK", uv.processInput("add Test3"));
        assertEquals("OK", uv.processInput("add Test1"));
        assertEquals("OK", uv.processInput("add Test5"));
    }

    private void testSklad() {
        testAddSequence();
        assertEquals("Test5", uv.processInput("remove_first"));
        assertEquals("Test1", uv.processInput("remove_first"));
        assertEquals("Test3", uv.processInput("remove_first"));
        assertEquals("Test2", uv.processInput("remove_first"));
        assertEquals("Test4", uv.processInput("remove_first"));
    }

    private void testPrioritetnaVrsta() {
        testAddSequence();
        assertEquals("Test5", uv.processInput("remove_first"));
        assertEquals("Test4", uv.processInput("remove_first"));
        assertEquals("Test3", uv.processInput("remove_first"));
        assertEquals("Test2", uv.processInput("remove_first"));
        assertEquals("Test1", uv.processInput("remove_first"));
    }

    private void testBst() {
        testAddSequence();
        assertEquals("Test4", uv.processInput("remove_first"));
        assertEquals("Test5", uv.processInput("remove_first"));
        assertEquals("Test2", uv.processInput("remove_first"));
        assertEquals("Test3", uv.processInput("remove_first"));
        assertEquals("Test1", uv.processInput("remove_first"));
    }

    private void testDepthBk() {
        assertEquals("0", uv.processInput("depth"));
        assertEquals("OK", uv.processInput("add Tes1"));
        assertEquals("0", uv.processInput("depth"));
        assertEquals("OK", uv.processInput("add Tes2"));
        assertEquals("1", uv.processInput("depth"));
        assertEquals("OK", uv.processInput("add Tes2"));
        assertEquals("1", uv.processInput("depth"));
    }

    private void testBk() {
        testAddSequence();
        assertEquals("OK", uv.processInput("add Test0"));
        assertEquals("Test5", uv.processInput("remove_first"));
        assertEquals("Test4", uv.processInput("remove_first"));
        assertEquals("Test3", uv.processInput("remove_first"));
        assertEquals("Test2", uv.processInput("remove_first"));
        assertEquals("Test1", uv.processInput("remove_first"));
        assertEquals("Test0", uv.processInput("remove_first"));
    }
}
