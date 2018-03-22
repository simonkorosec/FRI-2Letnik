package aps2.hashmap;

import java.util.Arrays;
import java.util.LinkedList;
import junit.framework.TestCase;

public class PublicTests extends TestCase {
	protected void setUp() throws Exception {
	}

	public void testHashFunctionDivisionMethod() {
		assertEquals(4, aps2.hashmap.HashFunction.DivisionMethod(10, 6));
		assertEquals(0, aps2.hashmap.HashFunction.DivisionMethod(15, 5));
	}
	
	public void testHashFunctionKnuthMethod() {
		assertEquals(1, aps2.hashmap.HashFunction.KnuthMethod(10, 6));
		assertEquals(1, aps2.hashmap.HashFunction.KnuthMethod(15, 5));
	}
	
	public void testHashMapChainingAdd() {
		aps2.hashmap.HashMapChaining hm = new aps2.hashmap.HashMapChaining(6, aps2.hashmap.HashFunction.HashingMethod.DivisionMethod);
		hm.add(4000, "Kranj");
		hm.add(6000, "Koper");
		hm.add(8000, "Novo mesto");
		hm.add(10000, "Zagreb");
		
		LinkedList<Element> table[] = hm.getTable();
		assertEquals(Arrays.asList(new Element(6000, "Koper")), table[0]);
		assertEquals(Arrays.asList(), table[1]);
		assertEquals(Arrays.asList(new Element(8000, "Novo mesto")), table[2]);
		assertEquals(Arrays.asList(), table[3]);
		assertEquals(Arrays.asList(new Element(4000, "Kranj"), new Element(10000, "Zagreb")), table[4]);
	}
	
	public void testHashMapChainingContainsGet() {
		aps2.hashmap.HashMapChaining hm = new aps2.hashmap.HashMapChaining(6, aps2.hashmap.HashFunction.HashingMethod.DivisionMethod);
		hm.add(6000, "Koper");
		hm.add(4000, "Kranj");
		
		assertTrue(hm.contains(6000));
		assertFalse(hm.contains(6006));
		assertEquals("Kranj", hm.get(4000));
	}
	
	public void testHashMapOpenAddressingAdd() {
		aps2.hashmap.HashMapOpenAddressing hm =
			new aps2.hashmap.HashMapOpenAddressing(
				7,
				aps2.hashmap.HashFunction.HashingMethod.DivisionMethod,
				aps2.hashmap.HashMapOpenAddressing.CollisionProbeSequence.LinearProbing
			);
		
		hm.add(1, "B");
		hm.add(8, "H");
		hm.add(15, "O");
		
		Element table[] = hm.getTable();
		assertEquals(Integer.MIN_VALUE, table[0].key);
		assertEquals(1, table[1].key);
		assertEquals(8, table[2].key);
		assertEquals(15, table[3].key);
		assertEquals(Integer.MIN_VALUE, table[4].key);
		assertEquals(Integer.MIN_VALUE, table[5].key);
		assertEquals(Integer.MIN_VALUE, table[6].key);
	}
	
	public void testHashMapOpenAddressingContainsGet() {
		aps2.hashmap.HashMapOpenAddressing hm =
			new aps2.hashmap.HashMapOpenAddressing(
				5,
				aps2.hashmap.HashFunction.HashingMethod.DivisionMethod,
				aps2.hashmap.HashMapOpenAddressing.CollisionProbeSequence.LinearProbing
			);

		hm.add(6, "F");
		
		assertTrue(hm.contains(6));
		assertFalse(hm.contains(11));
		assertEquals("F", hm.get(6));
	}


    public void testHashMapOpenAddressingRemove() {
        aps2.hashmap.HashMapOpenAddressing hm =
                new aps2.hashmap.HashMapOpenAddressing(
                        5,
                        aps2.hashmap.HashFunction.HashingMethod.DivisionMethod,
                        aps2.hashmap.HashMapOpenAddressing.CollisionProbeSequence.LinearProbing
                );

        hm.add(0, "A");
        hm.add(5, "B");
        hm.add(10, "C");
        hm.add(15, "D");
        hm.add(20, "E");

        assertTrue(hm.remove(10));
        assertTrue(hm.remove(20));
        assertFalse(hm.remove(20));

        assertEquals("D", hm.get(15));
    }

}
