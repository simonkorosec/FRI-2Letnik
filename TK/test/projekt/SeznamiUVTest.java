package projekt;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

public class SeznamiUVTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private SeznamiUV uv;

    @Before
    public void setUp() {
        uv = new SeznamiUV();
    }

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(System.out);
    }

    @Test
    public void testInvalidCommand() {
        assertEquals("Invalid command", uv.processInput(""));
        assertEquals("Invalid command", uv.processInput("test"));
    }

    @Test
    public void testAdd() {
        assertEquals("OK", uv.processInput("add 3105000500232,Jan Vid,Novak,18"));
        assertEquals("Person already exists", uv.processInput("add 3105000500232,Jan Vid,Novak,18"));
    }

    @Test
    public void testAddExistsEMSO() {
        assertEquals("OK", uv.processInput("add 3105000500232,Jan Vid,Novak,18"));
        assertEquals("OK", uv.processInput("add 2105000500232,Jan,Novak,18"));
        assertEquals("OK", uv.processInput("add 0105000500232,Jan Vid,Klop,18"));

        assertEquals("Person already exists", uv.processInput("add 2105000500232,Lojze,Novak,18"));
    }

    @Test
    public void testAddExistsIme() {
        assertEquals("OK", uv.processInput("add 3105000500232,Jan Vid,Novak,18"));
        assertEquals("OK", uv.processInput("add 2105000500232,Jan,Novak,18"));
        assertEquals("OK", uv.processInput("add 0105000500232,Jan Vid,Klop,18"));

        assertEquals("Person already exists", uv.processInput("add 2104000500232,Jan Vid,Novak,18"));
    }

    @Test
    public void testRemoveEMSO() {
        assertEquals("OK", uv.processInput("add 3105000500232,Jan Vid,Novak,18"));
        assertEquals("OK", uv.processInput("remove 3105000500232"));
    }

    @Test
    public void testRemoveIme() {
        assertEquals("OK", uv.processInput("add 3105000500232,Jan Vid,Novak,18"));
        assertEquals("OK", uv.processInput("remove Jan Vid,Novak"));
    }

    @Test
    public void testRemoveExists() {
        assertEquals("OK", uv.processInput("add 3105000500232,Jan Vid,Novak,18"));
        assertEquals("Person does not exist", uv.processInput("remove 3005000500232"));
        assertEquals("Person does not exist", uv.processInput("remove Vid,Novak"));
    }

    @Test
    public void testSearch() {
        assertEquals("OK", uv.processInput("add 3105000500232,Jan Vid,Novak,18"));
        assertEquals("3105000500232 | Novak, Jan Vid | 18 - lahko voli", uv.processInput("search 3105000500232"));
        assertEquals("3105000500232 | Novak, Jan Vid | 18 - lahko voli", uv.processInput("search Jan Vid,Novak"));
    }

    @Test
    public void testSearchExists() {
        assertEquals("OK", uv.processInput("add 3105000500232,Jan Vid,Novak,18"));
        assertEquals("Person does not exist", uv.processInput("search 3105001500232"));
        assertEquals("Person does not exist", uv.processInput("search Vid,Novak"));
    }

    @Test
    public void testCount() {
        assertEquals("OK", uv.processInput("add 3105000500232,Jan Vid,Novak,18"));
        assertEquals("No. of persons: 1", uv.processInput("count"));

        assertEquals("OK", uv.processInput("add 2105000500232,Vid,Novak,18"));
        assertEquals("No. of persons: 2", uv.processInput("count"));

        assertEquals("OK", uv.processInput("add 1105000500232,Vid,Noak,18"));
        assertEquals("No. of persons: 3", uv.processInput("count"));
    }

    @Test
    public void testReset() {
        assertEquals("OK", uv.processInput("add 3105000500232,Jan Vid,Novak,18"));
        assertEquals("OK", uv.processInput("add 2105000500232,Vid,Novak,18"));
        assertEquals("OK", uv.processInput("add 1105000500232,Vid,Noak,18"));
        assertEquals("No. of persons: 3", uv.processInput("count"));
        assertEquals("OK", uv.processInput("reset"));
        assertEquals("No. of persons: 0", uv.processInput("count"));
    }

    @Test
    public void testPrint() {
        assertEquals("OK", uv.processInput("add 2111965500138,Boris,Anderlič,53"));
        assertEquals("OK", uv.processInput("add 1310004505091,Amadeja,Svet,14"));
        assertEquals("OK", uv.processInput("add 3105000500232,Jan Vid,Novak,18"));

        String out = "No. of persons: 3\r\n\t2111965500138 | Anderlič, Boris | 53 - lahko voli\r\n\t3105000500232 | Novak, Jan Vid | 18 - lahko voli\r\n\t1310004505091 | Svet, Amadeja | 14 - ne more voliti\r\n";

        uv.processInput("print");
        assertEquals(out, outContent.toString());
    }

    @Test
    public void testPrintEmpty() {
        String out = "No. of persons: 0\r\n";
        uv.processInput("print");
        assertEquals(out, outContent.toString());
    }

    @Test
    public void saveRestore() {
        assertEquals("OK", uv.processInput("add 2111965500138,Boris,Anderlič,53"));
        assertEquals("OK", uv.processInput("add 1310004505091,Amadeja,Svet,14"));
        assertEquals("OK", uv.processInput("add 3105000500232,Jan Vid,Novak,18"));

        assertEquals("OK", uv.processInput("save test.bin"));
        assertEquals("OK", uv.processInput("reset"));
        assertEquals("No. of persons: 0", uv.processInput("count"));
        assertEquals("OK", uv.processInput("restore test.bin"));

    }

    @Test
    public void restoreNotFound() {
        assertEquals("I/O Error: E_nime.bin (The system cannot find the file specified)", uv.processInput("restore nime.bin"));
    }

    @Test
    public void testExit() {
        assertEquals("Goodbye", uv.processInput("exit"));
    }

    @Test
    public void testOutOfMemory() {
        uv.seznamEMSO = new BinKopicaMock<>();
        assertEquals("Error: not enough memory, operation failed", uv.processInput("add 2111965500138,Boris,Anderlič,53"));
    }

    @Test
    public void testNoDiskSpace() throws IOException {
            Seznam mock = EasyMock.createNiceMock(Seznam.class);
            mock.save(EasyMock.anyObject());
            EasyMock.expectLastCall().andThrow(new IOException("not enough space on disk"));
            replay(mock);
            uv.seznamEMSO = mock;
            assertEquals("I/O Error: not enough space on disk", uv.processInput("save test.bin"));
            verify(mock);
    }
}