package psa.naloga1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

public class PublicTests extends TestCase {
	private BSTSet bst;

	protected void setUp() throws Exception {
		bst = new BSTSet();
		
	}

	public void testBSTSetContains() {
		bst.add(5);
		bst.add(2);
		bst.add(1);
		bst.add(4);
		bst.add(9);
		bst.add(4);
		assertTrue(bst.contains(9));
	}
	
	public void testBSTSetNotContains() {
		bst.add(5);
		bst.add(2);
		bst.add(1);
		bst.add(4);
		bst.add(9);
		bst.add(4);
		assertFalse(bst.contains(3));
	}
	
	public void testBSTRemove() {
		bst.add(5);
		bst.add(2);
		bst.add(1);
		bst.add(4);
		bst.add(9);
		bst.add(4);
		assertTrue(bst.remove(4));
		assertFalse(bst.contains(4));
	}
	
	public void testBSTNumberOfCompares() {
		bst.add(1);
		bst.add(2);
		bst.add(3);
		bst.add(4);
		bst.resetCounter();
		bst.contains(4);
		assertEquals(4, bst.getCounter());
	}
	
	public void testBSTTraversePreOrder() {
		bst.add(4); // root
		bst.add(2);
		bst.add(6);
		bst.add(1);
		bst.add(3);
		bst.add(5);
		bst.add(7);
		assertEquals(Arrays.asList(4, 2, 1, 3, 6, 5, 7), bst.traversePreOrder());
	}

    // additional tests
    private BSTSet createTestData()
    {
        List<Integer> numbers = new ArrayList<>(Arrays.asList(52, 27, 65, 20, 38, 62, 72, 12, 21, 35, 39, 61, 63, 81, 32, 36));
        for(int i = 0; i < numbers.size(); i++)
        {
            bst.add(numbers.get(i));
        }
        return bst;
    }

    // test if level order works
    public void test1()
    {
        bst = createTestData();
        assertEquals(Arrays.asList(52, 27, 65, 20, 38, 62, 72, 12, 21, 35, 39, 61, 63, 81, 32, 36), bst.traverseLevelOrder());
    }

    // test if level order works
    public void test2()
    {
        bst = createTestData();
        bst.remove(32);
        assertEquals(Arrays.asList(52, 27, 65, 20, 38, 62, 72, 12, 21, 35, 39, 61, 63, 81, 36), bst.traverseLevelOrder());
    }
    // test if level order works
    public void test3()
    {
        bst = createTestData();
        bst.add(40);
        bst.remove(35);
        bst.remove(38);
        assertEquals(Arrays.asList(52, 27, 65, 20, 39, 62, 72, 12, 21, 36, 40, 61, 63, 81, 32), bst.traverseLevelOrder());

        bst.add(31);
        bst.resetCounter();
        bst.contains(31);
        assertEquals(6, bst.getCounter());

    }




}
