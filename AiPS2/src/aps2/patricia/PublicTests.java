package aps2.patricia;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * insert Public Tests to this class. Public tests are written by the Instructor
 * and may be used for grading projects. This source code is made available to
 * students.
 */

public class PublicTests extends TestCase {
    protected void setUp() {
    }

    public void testPatriciaSetNode() {
        PatriciaSetNode root = new PatriciaSetNode("", false);
        PatriciaSetNode abc = new PatriciaSetNode("ABC", true);
        PatriciaSetNode def = new PatriciaSetNode("DEF", true);
        PatriciaSetNode ghi = new PatriciaSetNode("GHI", true);
        PatriciaSetNode abc2 = new PatriciaSetNode("ABC2", true);

        assertTrue(root.addChild(ghi));
        assertTrue(root.addChild(abc));
        assertTrue(root.addChild(def));
        assertFalse(root.addChild(abc2)); // child in direction of A already exists!

        assertEquals(3, root.countChildren());

        assertEquals(abc, root.getChild('A'));
        assertEquals(def, root.getChild('D'));
        assertEquals(ghi, root.getChild('G'));

        assertEquals(abc, root.firstChild); // children should be ordered
        assertEquals(def, root.firstChild.nextSibling);
        assertEquals(ghi, root.firstChild.nextSibling.nextSibling);

        assertEquals(root, abc.getParent());
        assertEquals(root, def.getParent());
        assertEquals(root, ghi.getParent());

        assertTrue(root.removeChild('G'));
        assertFalse(root.removeChild('H')); // child in direction of H doesn't exist!
    }

    public void testPatriciaSetInsertion() {
        PatriciaSet p = new PatriciaSet();
        assertTrue(p.insert("TEST"));
        assertTrue(p.insert("TEST2"));
        assertTrue(p.insert("TEMPO"));
        assertTrue(p.insert("TAMPO"));
        assertFalse(p.insert("TEST")); // duplicate

        assertEquals("T", p.getRoot().firstChild.getLabel());
        assertEquals("AMPO", p.getRoot().firstChild.firstChild.getLabel());
        assertEquals("E", p.getRoot().firstChild.firstChild.nextSibling.getLabel());
        assertEquals("MPO", p.getRoot().firstChild.firstChild.nextSibling.firstChild.getLabel());
    }

    public void testPatriciaSetFind() {
        PatriciaSet p = new PatriciaSet();
        assertTrue(p.insert("TEST"));
        assertTrue(p.insert("TEST2"));
        assertTrue(p.insert("TEMPO"));
        assertTrue(p.insert("TAMPO"));
        assertTrue(p.insert("TARPO"));
        assertTrue(p.insert("TE"));

        assertTrue(p.contains("TEST"));
        assertFalse(p.contains("TES"));
        assertTrue(p.contains("TE"));
        assertFalse(p.contains("TEST3"));
        assertFalse(p.contains("TESR"));
        assertFalse(p.contains("TAR"));
    }

    public void testPatriciaInsertAllSuffixes() {
        String text = "ABRACADABRA";
        PatriciaSet p = new PatriciaSet();
        for (int i = 0; i < text.length(); i++) {
            assertTrue(p.insert(text.substring(i)));
        }

        for (int i = 0; i < text.length(); i++) {
            assertTrue(p.contains(text.substring(i)));
        }
    }

    public void testPatriciaPrefix() {
        String text = "ABRACADABRA";
        PatriciaSet p = new PatriciaSet();
        for (int i = 0; i < text.length(); i++) {
            assertTrue(p.insert(text.substring(i)));
        }

        assertEquals("ACADA", p.longestPrefixOf("ACADAZAA"));
    }

    public void testPatriciaKeysPrefix() {
        PatriciaSet p = new PatriciaSet();

        String[] keys = new String[]{"TEST", "TEST2", "TAMPO", "TATKO", "TATI"};
        String[] extra = new String[]{"ABC", "ADD", "ADC", "FANT", "JANKO"};

        for (String key : keys) {
            assertTrue(p.insert(key));
        }
        for (String key : extra) {
            assertTrue(p.insert(key));
        }

        Set<String> strings = p.keysWithPrefix("T");
        System.out.println(Arrays.toString(strings.toArray()));
        assertEquals(keys.length, strings.size());
        for (String key : keys) {
            assertTrue(strings.contains(key));
        }

    }

    public void testPatriciaSetRemove() {
        PatriciaSet p = new PatriciaSet();
        assertTrue(p.insert("T"));
        assertTrue(p.insert("TEST"));
        assertTrue(p.insert("TEST2"));
        assertTrue(p.insert("TEMPO"));
        assertTrue(p.insert("TAMPO"));

        assertTrue(p.contains("T"));
        assertTrue(p.remove("T"));
        assertFalse(p.contains("T"));
        //assertFalse(p.remove("TAMPO"));

        assertEquals("T", p.getRoot().firstChild.getLabel());
        assertEquals("AMPO", p.getRoot().firstChild.firstChild.getLabel());
        assertEquals("E", p.getRoot().firstChild.firstChild.nextSibling.getLabel());
        assertEquals("MPO", p.getRoot().firstChild.firstChild.nextSibling.firstChild.getLabel());
        assertEquals("ST", p.getRoot().firstChild.firstChild.nextSibling.firstChild.nextSibling.getLabel());
    }

    public void testPatriciaSetInsertion2() {
        PatriciaSet p = new PatriciaSet();
        assertTrue(p.insert("TEST"));
        assertTrue(p.insert("APS"));

        assertEquals("APS", p.getRoot().firstChild.getLabel());
        assertEquals("TEST", p.getRoot().firstChild.nextSibling.getLabel());
    }


}
