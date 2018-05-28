package projekt;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class BinomskaKopicaTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private BinomskaKopica<Oseba> bk;

    @Before
    public void setUp() throws Exception {
        bk = new BinomskaKopica<>();
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
    public void add() {
        Oseba o = new Oseba("3105000500232", "Jan Vid", "Novak", 18);
        bk.add(o);
        assertEquals(1, bk.size());
        o = new Oseba("3105998500232", "Jan", "Novak", 20);
        bk.add(o);
        assertEquals(2, bk.size());
    }

    @Test
    public void removeFirst() {
        Oseba o = new Oseba("2405000500232", "Jan Vid", "Novak", 18);
        bk.add(o);
        assertEquals(1, bk.size());
        assertEquals(o, bk.removeFirst());
        assertEquals(0, bk.size());


        Oseba p = new Oseba("2605000500222", "Jan", "Novak", 18);
        bk.add(o);
        bk.add(p);
        assertEquals(2, bk.size());
        assertEquals(p, bk.removeFirst());
        assertEquals(1, bk.size());
    }

    @Test (expected = NoSuchElementException.class)
    public void removeFirstEmpty() {
        bk.removeFirst();
    }

    @Test
    public void getFirst() {
        Oseba o = new Oseba("2405000500232", "Jan Vid", "Novak", 18);
        bk.add(o);
        assertEquals(1, bk.size());
        assertEquals(o, bk.getFirst());
        assertEquals(1, bk.size());


        Oseba p = new Oseba("2605000500222", "Jan", "Novak", 18);
        bk.add(p);
        assertEquals(2, bk.size());
        assertEquals(p, bk.getFirst());
        assertEquals(2, bk.size());
    }

    @Test (expected = NoSuchElementException.class)
    public void getFirstEmpty() {
        bk.getFirst();
    }

    @Test
    public void size() {
        assertEquals(0, bk.size());
        Oseba o = new Oseba("3105000500232", "Jan Vid", "Novak", 18);
        bk.add(o);
        assertEquals(1, bk.size());
        o = new Oseba("3105998500232", "Jan", "Novak", 20);
        bk.add(o);
        assertEquals(2, bk.size());
    }

    @Test
    public void depth() {
        assertEquals(0, bk.depth());

        Oseba o = new Oseba("3105000500232", "Jan Vid", "Novak", 18);
        bk.add(o);
        assertEquals(0, bk.depth());

        o = new Oseba("2605000500222", "Jan", "Novak", 18);
        bk.add(o);
        assertEquals(1, bk.depth());

        o = new Oseba("1605000500222", "Jan", "Novak", 18);
        bk.add(o);
        assertEquals(1, bk.depth());

        o = new Oseba("0605000500222", "Jan", "Novak", 18);
        bk.add(o);
        assertEquals(2, bk.depth());
    }

    @Test
    public void isEmpty() {
        assertTrue(bk.isEmpty());
        Oseba o = new Oseba("3105000500232", "Jan Vid", "Novak", 18);
        bk.add(o);
        assertFalse(bk.isEmpty());
    }

    @Test
    public void remove() {
        Oseba o = new Oseba("3105000500232", "Jan Vid", "Novak", 18);
        bk.add(o);
        assertEquals(o, bk.remove("3105000500232"));

        bk.add(o);
        assertEquals(o, bk.remove("Jan Vid", "Novak"));
    }

    @Test
    public void removeTwo() {
        Oseba o = new Oseba("3105000500232", "Jan Vid", "Novak", 18);
        bk.add(o);
        Oseba p = new Oseba("2105000500212", "Lojze", "Novak", 20);
        bk.add(p);

        assertEquals(o, bk.remove("3105000500232"));
        bk.add(o);
        assertEquals(o, bk.remove("Jan Vid", "Novak"));

        assertEquals(p, bk.remove("2105000500212"));
        bk.add(p);
        assertEquals(p, bk.remove("Lojze", "Novak"));
    }

    @Test (expected = NoSuchElementException.class)
    public void removeEmpty() {
        bk.remove("Jan Vid", "Novak");
    }

    @Test
    public void exists() {
        Oseba o = new Oseba("3105000500232", "Jan Vid", "Novak", 18);
        bk.add(o);
        Oseba p = new Oseba("2105000500212", "Lojze", "Novak", 20);
        bk.add(p);
        Oseba h = new Oseba("2310997500225", "Simon", "Korošec", 21);
        bk.add(h);

        assertFalse(bk.exists("0105000500232"));
        assertFalse(bk.exists("Vid", "Novak"));

        assertTrue(bk.exists("3105000500232"));
        assertTrue(bk.exists("Jan Vid", "Novak"));

        assertTrue(bk.exists("2105000500212"));
        assertTrue(bk.exists("Lojze", "Novak"));

        assertTrue(bk.exists("2310997500225"));
        assertTrue(bk.exists("Simon", "Korošec"));

    }

    @Test
    public void print() {
        bk.add(new Oseba("2111965500138", "Boris","Anderlič",53));
        bk.add(new Oseba("1310004505091", "Amadeja","Svet",14));
        bk.add(new Oseba("3105000500232", "Jan Vid","Novak",18));

        String out = "\t2111965500138 | Anderlič, Boris | 53 - lahko voli\r\n\t3105000500232 | Novak, Jan Vid | 18 - lahko voli\r\n\t1310004505091 | Svet, Amadeja | 14 - ne more voliti\r\n";

        bk.print();
        assertEquals(out, outContent.toString());
    }

    @Test
    public void saveRestore() throws IOException, ClassNotFoundException {
        Oseba o = new Oseba("2111965500138", "Boris","Anderlič",53);
        bk.add(o);
        assertEquals(o, bk.getFirst());

        bk.save(new FileOutputStream("save.bin"));

        bk = new BinomskaKopica<>();
        assertEquals(0, bk.size());
        bk.restore(new FileInputStream("save.bin"));
        assertEquals(1, bk.size());
    }

    @Test (expected = IOException.class)
    public void restoreError() throws IOException, ClassNotFoundException {
        bk.restore(new FileInputStream("ne_obstajam.bin"));
    }
}