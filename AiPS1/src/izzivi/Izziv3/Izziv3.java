package izzivi.Izziv3;

public class Izziv3 {
    public static void main(String[] args) {
        ArrayDeque<Integer> stack = new ArrayDeque<>();

        try {
            System.out.println("Demonstracija ArrayDeque kot Stack:");
            System.out.println("Metoda push():");
            stack.push(1);
            stack.push(2);
            stack.push(3);
            System.out.println(stack);

            System.out.println("Metoda pop():");
            Integer i = stack.pop();
            System.out.println(stack);
            System.out.printf("Vrnjen element %d\n", i);

            System.out.println("Metoda top():");
            Integer k = stack.pop();
            System.out.printf("Vrnjen element %d\n", k);

        } catch (CollectionException e) {
            System.out.println(e.toString());
        }
        System.out.print("\n\n\n");

        ArrayDeque<Integer> deque = new ArrayDeque<>();

        try {
            System.out.println("Demonstracija ArrayDeque kot Deque:");
            System.out.println("Metoda enqueue():");
            deque.enqueue(3);
            deque.enqueue(2);
            deque.enqueue(1);
            System.out.println(deque);

            System.out.println("Metoda enqueueFront():");
            deque.enqueueFront(4);
            deque.enqueueFront(5);
            deque.enqueueFront(6);
            System.out.println(deque);

            System.out.println("Metoda dequeue():");
            Integer h = deque.dequeue();
            System.out.println(deque);
            System.out.printf("Vrnjen element %d\n", h);

            System.out.println("Metoda dequeueBack():");
            Integer j = deque.dequeueBack();
            System.out.println(deque);
            System.out.printf("Vrnjen element %d\n", j);

            System.out.println("Metoda front():");
            Integer v = deque.front();
            System.out.println(deque);
            System.out.printf("Vrnjen element %d\n", v);

            System.out.println("Metoda back():");
            Integer u = deque.back();
            System.out.println(deque);
            System.out.printf("Vrnjen element %d\n", u);
        } catch (CollectionException e) {
            System.out.println(e.toString());
        }
        System.out.print("\n\n\n");

        ArrayDeque<Integer> sequence = new ArrayDeque<>();

        try {
            System.out.println("Demonstracija ArrayDeque kot Sequence:");
            System.out.println("Metoda set():");
            sequence.set(0, 1);
            sequence.set(1, 2);
            sequence.set(2, 3);
            sequence.set(3, 4);
            System.out.println(sequence);

            System.out.println("Metoda get(2):");
            Integer x = sequence.get(2);
            System.out.printf("Vrnjen element %d\n", x);
        } catch (CollectionException e) {
            System.out.println(e.toString());
        }


    }
}
