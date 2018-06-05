package projekt;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class BinomskaKopicaTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private BinomskaKopica<Oseba> bkEmso;
    private BinomskaKopica<Oseba> bk;

    @Before
    public void setUp() {
        bkEmso = new BinomskaKopica<>(new ComperatorEMSO());
        bk = new BinomskaKopica<>(new ComperatorImePriimek());
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
        bkEmso.add(o);
        bk.add(o);
        assertEquals(1, bkEmso.size());
        assertEquals(1, bk.size());
        o = new Oseba("3105998500232", "Jan", "Novak", 20);
        bkEmso.add(o);
        bk.add(o);
        assertEquals(2, bkEmso.size());
        assertEquals(2, bk.size());
    }

    @Test
    public void removeFirst() {
        Oseba o = new Oseba("2405000500232", "Jan Vid", "Novak", 18);
        bkEmso.add(o);
        bk.add(o);
        assertEquals(1, bkEmso.size());
        assertEquals(o, bkEmso.removeFirst());
        assertEquals(0, bkEmso.size());

        assertEquals(1, bk.size());
        assertEquals(o, bk.removeFirst());
        assertEquals(0, bk.size());


        Oseba p = new Oseba("2605000500222", "Jan", "Novak", 18);
        bkEmso.add(o);
        bkEmso.add(p);
        bk.add(o);
        bk.add(p);

        assertEquals(2, bkEmso.size());
        assertEquals(p, bkEmso.removeFirst());
        assertEquals(1, bkEmso.size());

        assertEquals(2, bk.size());
        assertEquals(o, bk.removeFirst());
        assertEquals(1, bk.size());
    }

    @Test (expected = NoSuchElementException.class)
    public void removeFirstEmptyEMSO() {
        bkEmso.removeFirst();
    }

    @Test (expected = NoSuchElementException.class)
    public void removeFirstEmpty() {
        bk.removeFirst();
    }


    @Test
    public void getFirst() {
        Oseba o = new Oseba("2405000500232", "Jan Vid", "Novak", 18);
        bkEmso.add(o);
        bk.add(o);

        assertEquals(1, bkEmso.size());
        assertEquals(o, bkEmso.getFirst());
        assertEquals(1, bkEmso.size());

        assertEquals(1, bk.size());
        assertEquals(o, bk.getFirst());
        assertEquals(1, bk.size());


        Oseba p = new Oseba("2605000500222", "Rok", "Novak", 18);
        bkEmso.add(p);
        bk.add(p);

        assertEquals(2, bkEmso.size());
        assertEquals(p, bkEmso.getFirst());
        assertEquals(2, bkEmso.size());

        assertEquals(2, bk.size());
        assertEquals(p, bk.getFirst());
        assertEquals(2, bk.size());
    }

    @Test (expected = NoSuchElementException.class)
    public void getFirstEmpty() {
        bk.getFirst();
    }

    @Test (expected = NoSuchElementException.class)
    public void getFirstEmptyEMSO() {
        bkEmso.getFirst();
    }

    @Test
    public void size() {
        assertEquals(0, bkEmso.size());
        Oseba o = new Oseba("3105000500232", "Jan Vid", "Novak", 18);
        bkEmso.add(o);
        bk.add(o);
        assertEquals(1, bkEmso.size());
        assertEquals(1, bk.size());
        o = new Oseba("3105998500232", "Jan", "Novak", 20);
        bkEmso.add(o);
        bk.add(o);
        assertEquals(2, bk.size());
        assertEquals(2, bkEmso.size());
    }

    @Test
    public void depth() {
        assertEquals(0, bkEmso.depth());

        Oseba o = new Oseba("3105000500232", "Jan Vid", "Novak", 18);
        bkEmso.add(o);
        bk.add(o);
        assertEquals(0, bkEmso.depth());
        assertEquals(0, bk.depth());

        o = new Oseba("2605000500222", "Jan", "Novak", 18);
        bkEmso.add(o);
        bk.add(o);
        assertEquals(1, bkEmso.depth());
        assertEquals(1, bk.depth());

        o = new Oseba("1605000500222", "Jan", "Novak1", 18);
        bkEmso.add(o);
        bk.add(o);
        assertEquals(1, bkEmso.depth());
        assertEquals(1, bk.depth());

        o = new Oseba("0605000500222", "Jan", "Novak2", 18);
        bkEmso.add(o);
        bk.add(o);
        assertEquals(2, bkEmso.depth());
        assertEquals(2, bk.depth());
    }

    @Test
    public void isEmpty() {
        assertTrue(bkEmso.isEmpty());
        assertTrue(bk.isEmpty());
        Oseba o = new Oseba("3105000500232", "Jan Vid", "Novak", 18);
        bkEmso.add(o);
        bk.add(o);
        assertFalse(bkEmso.isEmpty());
        assertFalse(bk.isEmpty());
    }

    @Test
    public void remove() {
        Oseba o = new Oseba("3105000500232", "Jan Vid", "Novak", 18);
        bkEmso.add(o);
        assertEquals(o, bkEmso.remove(new Oseba("3105000500232", "", "", 0)));
        bk.add(o);
        assertEquals(o, bk.remove(new Oseba("","Jan Vid", "Novak",0)));
    }

    @Test
    public void removeMore() {
        Oseba o = new Oseba("3105000500232", "Jan Vid", "Novak", 18);
        bkEmso.add(o);
        bkEmso.add(new Oseba("1105000500232", "Jan", "Novak1", 18));
        bkEmso.add(new Oseba("2105000500232", "Jan", "Novak2", 18));
        bkEmso.add(new Oseba("3005000500232", "Jan", "Novak3", 18));
        bkEmso.add(new Oseba("0105000500232", "Jan", "Novak4", 18));

        assertEquals(o, bkEmso.remove(o));
    }


    @Test
    public void removeTwo() {
        Oseba o = new Oseba("3105000500232", "Jan Vid", "Novak", 18);
        bkEmso.add(o);
        bk.add(o);
        Oseba p = new Oseba("2105000500212", "Lojze", "Novak", 20);
        bkEmso.add(p);
        bk.add(p);

        assertEquals(o, bkEmso.remove(o));
       //assertEquals(o, bk.remove(new Oseba("","Jan Vid", "Novak",0)));
        assertEquals(o, bk.remove(o));

        assertEquals(p, bkEmso.remove(p));
        assertEquals(p, bk.remove(new Oseba("","Lojze", "Novak",0)));
    }

    @Test (expected = NoSuchElementException.class)
    public void removeEmpty() {
        bkEmso.remove(new Oseba("3105000500232", "", "", 0));
    }

    @Test
    public void exists() {
        Oseba o = new Oseba("3105000500232", "Jan Vid", "Novak", 18);
        bkEmso.add(o);
        Oseba p = new Oseba("2105000500212", "Lojze", "Novak", 20);
        bkEmso.add(p);
        Oseba h = new Oseba("2310997500225", "Simon", "Korošec", 21);
        bkEmso.add(h);

        assertFalse(bkEmso.exists(new Oseba("0105000500232","","",0)));
        //assertFalse(bkEmso.exists("Vid", "Novak"));

        assertTrue(bkEmso.exists(new Oseba("3105000500232","","",0)));
        //assertTrue(bkEmso.exists("Jan Vid", "Novak"));

        assertTrue(bkEmso.exists(new Oseba("2105000500212", "","",0)));
        //assertTrue(bkEmso.exists("Lojze", "Novak"));

        assertTrue(bkEmso.exists(new Oseba("2310997500225","","",0)));
        //assertTrue(bkEmso.exists("Simon", "Korošec"));

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
        bkEmso.add(o);
        assertEquals(o, bkEmso.getFirst());

        bkEmso.save(new FileOutputStream("save.bin"));

        bkEmso = new BinomskaKopica<>();
        assertEquals(0, bkEmso.size());
        bkEmso.restore(new FileInputStream("save.bin"));
        assertEquals(1, bkEmso.size());
    }

    @Test (expected = IOException.class)
    public void restoreError() throws IOException, ClassNotFoundException {
        bkEmso.restore(new FileInputStream("ne_obstajam.bin"));
    }
}