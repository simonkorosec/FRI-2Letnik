package imenik;

import org.junit.*;

import static org.junit.Assert.assertEquals;


public class SeznamiUVTest {

    private SeznamiUV uv;

    public SeznamiUVTest() {
    }

    @Before
    public void setUp() {
        uv = new SeznamiUV();
        uv.addImpl("pv", new PrioritetnaVrsta<>(new PrijateljPrimerjajPoImenu()), new PrioritetnaVrsta<>(new PrijateljPrimerjajPoTelSt()));
        uv.addImpl("sk", new Sklad<>(new PrijateljPrimerjajPoImenu()), new Sklad<>(new PrijateljPrimerjajPoTelSt()));
        uv.addImpl("bst", new Bst<>(new PrijateljPrimerjajPoImenu()), new Bst<>(new PrijateljPrimerjajPoTelSt()));
    }

    @Test
    public void testUseSklad() {
        assertEquals("OK", uv.processInput("use sk"));
        reset();
        testAdd();
        reset();
        testAddNothing();
        reset();
        testRemoveFirst();
        reset();
        testRemoveFirstNothing();
        reset();
        testGet();
        reset();
        testGetNothing();
        reset();
        testResetOnEmpty();
        reset();
        testResetOnFull();
        reset();
        testCountOnEmpty();
        reset();
        testCountOne();
        reset();
        testCountTwo();
        reset();
        testDepthOnEmpty();
        reset();
        testDepthOne();
        reset();
        testDepthTwo();
        reset();
        testSklad(true); // preveri, ali je PS dejansko sklad
    }

    @Test
    public void testUsePrioritetnaVrsta() {
        assertEquals("OK", uv.processInput("use pv"));
        reset();
        testAdd();
        reset();
        testAddNothing();
        reset();
        testRemoveFirst();
        reset();
        testRemoveFirstNothing();
        reset();
        testGet();
        reset();
        testGetNothing();
        reset();
        testResetOnEmpty();
        reset();
        testResetOnFull();
        reset();
        testCountOnEmpty();
        reset();
        testCountOne();
        reset();
        testCountTwo();
        reset();
        testDepthOnEmpty();
        reset();
        testDepthOne();
        reset();
        testDepthTwo();
        reset();
        testPrioritetnaVrsta(true); // preveri, ali je PS dejansko prioritetna vrsta
    }

    @Test
    public void testUseBST() {
        assertEquals("OK", uv.processInput("use bst"));
        reset();
        testAdd();
        reset();
        testAddNothing();
        reset();
        testRemoveFirst();
        reset();
        testRemoveFirstNothing();
        reset();
        testGet();
        reset();
        testGetNothing();
        reset();
        testResetOnEmpty();
        reset();
        testResetOnFull();
        reset();
        testCountOnEmpty();
        reset();
        testCountOne();
        reset();
        testCountTwo();
        reset();
        testDepthOnEmpty();
        reset();
        testDepthOne();
        reset();
        testDepthTwo();
        reset();
        testBst(true); // preveri, ali je PS dejansko BST
    }

    @Test
    public void testUseAll() {
        testUseSklad();
        testUseBST();
        testUsePrioritetnaVrsta();
    }

    @Test
    public void testUseAllMixed() {
        assertEquals("OK", uv.processInput("use sk"));
        testAddTestSequence();
        assertEquals("OK", uv.processInput("use pv"));
        testAddTestSequence();
        assertEquals("OK", uv.processInput("use bst"));
        testAddTestSequence();
        assertEquals("OK", uv.processInput("use pv"));
        testPrioritetnaVrsta(false);
        assertEquals("OK", uv.processInput("use bst"));
        testBst(false);
        assertEquals("OK", uv.processInput("use sk"));
        testSklad(false);
    }
    
    /*
    @Test
    public void testAddMemoryFull() {
        assertEquals("OK", uv.processInput("use sk"));
        while (true) {
            uv.processInput("add Ime Priimek TelefonskaStevilka");
        }
    }
    */
    
    @Test
    public void testAddMemoryFullMock() {
        uv.addImpl("skmock", new SkladMock<Prijatelj>(), new SkladMock<Prijatelj>());
        assertEquals("OK", uv.processInput("use skmock"));
        assertEquals("Error: not enough memory, operation failed",
                uv.processInput("add Ime Priimek TelefonskaStevilka"));
    }



    // *****************
    // POMOŽNE METODE
    // *****************   
    private void reset() {
        uv.processInput("reset");
    }

    private void testAdd() {
        assertEquals("OK", uv.processInput("add Ime1 Priimek1 TelefonskaStevilka1"));
        assertEquals("OK", uv.processInput("add Ime2 Priimek2 TelefonskaStevilka2"));
    }

    private void testAddNothing() {
        assertEquals("Error: please specify three strings", uv.processInput("add"));
    }

    private void testRemoveFirst() {
        assertEquals("OK", uv.processInput("add Ime Priimek TelefonskaStevilka"));
        assertEquals("Priimek, Ime - TelefonskaStevilka", uv.processInput("removefirst"));
    }

    private void testRemoveFirstNothing() {
        assertEquals("Error: structure is empty", uv.processInput("removefirst"));
        assertEquals("Error: please specify three strings", uv.processInput("add"));
        assertEquals("Error: structure is empty", uv.processInput("removefirst"));
    }

    private void testGet() {
        assertEquals("OK", uv.processInput("add Ime Priimek TelefonskaStevilka"));
        assertEquals("Priimek, Ime - TelefonskaStevilka", uv.processInput("getfirst"));
    }

    private void testGetNothing() {
        assertEquals("Error: structure is empty", uv.processInput("getfirst"));
        assertEquals("Error: please specify three strings", uv.processInput("add"));
        assertEquals("Error: structure is empty", uv.processInput("getfirst"));
    }

    private void testResetOnEmpty() {
        assertEquals("OK", uv.processInput("reset"));
    }

    private void testResetOnFull() {
        assertEquals("OK", uv.processInput("add Ime Priimek TelefonskaStevilka"));
        assertEquals("OK", uv.processInput("reset"));
        assertEquals("Error: structure is empty", uv.processInput("removefirst"));
        assertEquals("0", uv.processInput("count"));
    }

    private void testCountOnEmpty() {
        assertEquals("0", uv.processInput("count"));
    }

    private void testCountOne() {
        assertEquals("OK", uv.processInput("add Ime Priimek TelefonskaStevilka"));
        assertEquals("1", uv.processInput("count"));
    }

    private void testCountTwo() {
        assertEquals("OK", uv.processInput("add Ime1 Priimek1 TelefonskaStevilka1"));
        assertEquals("OK", uv.processInput("add Ime2 Priimek2 TelefonskaStevilka2"));
        assertEquals("2", uv.processInput("count"));
    }

    private void testDepthOnEmpty() {
        assertEquals("0", uv.processInput("depth"));
    }

    private void testDepthOne() {
        assertEquals("OK", uv.processInput("add Ime Priimek TelefonskaStevilka"));
        assertEquals("1", uv.processInput("depth"));
    }

    private void testDepthTwo() {
        assertEquals("OK", uv.processInput("add Ime1 Priimek1 TelefonskaStevilka1"));
        assertEquals("OK", uv.processInput("add Ime2 Priimek2 TelefonskaStevilka2"));
        assertEquals("2", uv.processInput("depth"));
    }

    private void testAddTestSequence() {
        assertEquals("OK", uv.processInput("add Ime4 Priimek4 TelefonskaStevilka4"));
        assertEquals("OK", uv.processInput("add Ime2 Priimek2 TelefonskaStevilka2"));
        assertEquals("OK", uv.processInput("add Ime3 Priimek3 TelefonskaStevilka3"));
        assertEquals("OK", uv.processInput("add Ime1 Priimek1 TelefonskaStevilka1"));
        assertEquals("OK", uv.processInput("add Ime5 Priimek5 TelefonskaStevilka5"));
    }

    private void testSklad(boolean add) {
        if (add) {
            testAddTestSequence();
        }
        assertEquals("Priimek5, Ime5 - TelefonskaStevilka5", uv.processInput("removefirst"));
        assertEquals("Priimek1, Ime1 - TelefonskaStevilka1", uv.processInput("removefirst"));
        assertEquals("Priimek3, Ime3 - TelefonskaStevilka3", uv.processInput("removefirst"));
        assertEquals("Priimek2, Ime2 - TelefonskaStevilka2", uv.processInput("removefirst"));
        assertEquals("Priimek4, Ime4 - TelefonskaStevilka4", uv.processInput("removefirst"));
    }

    private void testPrioritetnaVrsta(boolean add) {
        if (add) {
            testAddTestSequence();
        }
        assertEquals("Priimek5, Ime5 - TelefonskaStevilka5", uv.processInput("removefirst"));
        assertEquals("Priimek4, Ime4 - TelefonskaStevilka4", uv.processInput("removefirst"));
        assertEquals("Priimek3, Ime3 - TelefonskaStevilka3", uv.processInput("removefirst"));
        assertEquals("Priimek2, Ime2 - TelefonskaStevilka2", uv.processInput("removefirst"));
        assertEquals("Priimek1, Ime1 - TelefonskaStevilka1", uv.processInput("removefirst"));
    }

    private void testBst(boolean add) {
        if (add) {
            testAddTestSequence();
        }
        assertEquals("Priimek4, Ime4 - TelefonskaStevilka4", uv.processInput("removefirst"));
        assertEquals("Priimek5, Ime5 - TelefonskaStevilka5", uv.processInput("removefirst"));
        assertEquals("Priimek2, Ime2 - TelefonskaStevilka2", uv.processInput("removefirst"));
        assertEquals("Priimek3, Ime3 - TelefonskaStevilka3", uv.processInput("removefirst"));
        assertEquals("Priimek1, Ime1 - TelefonskaStevilka1", uv.processInput("removefirst"));
    }
}
