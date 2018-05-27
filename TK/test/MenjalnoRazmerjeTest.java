import java.io.IOException;

import org.junit.*;

import static org.junit.Assert.*;

import org.easymock.EasyMock;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;


public class MenjalnoRazmerjeTest {
// public class MenjalnoRazmerjeTest extends TestCase 

    private Valuta testObject;
    private Valuta expected;

    public MenjalnoRazmerjeTest() {
    }

    @Before
    public void setUp() {
        testObject = new Valuta(2.50, "USD");
        expected = new Valuta(3.75, "EUR");
    }


    /**
     * Test method razreda <MenjalnoRazmerje.>
     */

    @Test
    public void testVEvre() throws IOException {
        MenjalnoRazmerje mock = EasyMock.createMock(MenjalnoRazmerje.class);
        expect(mock.getRazmerje("USD", "EUR")).andReturn(1.5);
        replay(mock);

        Valuta actual = testObject.vEvre(mock);
        assertEquals(expected, actual);

        // EasyMock lahko preverja, ce so bile klicane prave metode v pravem vrstnem redu in samo v pravem vrstnem redu.
        // preverjanje vkljucimo z verify metodo na koncu testne metode
        //  - normal: createMock(), vrstni klica metod red ni pomemben, unexpected methods --> fail
        //  - strict: createStrictMock(), vse metode moramo klicati in to v tocno dolocenem vrstnem redu
        //  - nice: createNiceMock(), vse metode morajo biti klicane, vrstni red ni pomemben, unexpected methods --> ok
        verify(mock);
    }

    @Test
    public void testServerMenjalnoRazmerjeUnavailable() throws IOException {
        MenjalnoRazmerje mock = EasyMock.createMock(MenjalnoRazmerje.class);
        expect(mock.getRazmerje("USD", "EUR")).andThrow(new IOException());
        // EasyMock.anyInt()
        // expect(mock.getRazmerje((String) EasyMock.anyObject(), (String) EasyMock.anyObject())).andReturn(1.5);
        // expect(mock.getRazmerje((String) EasyMock.notNull(), (String) EasyMock.notNull())).andReturn(1.5);
        replay(mock);
        Valuta actual = testObject.vEvre(mock);
        assertNull(actual);
    }
}
