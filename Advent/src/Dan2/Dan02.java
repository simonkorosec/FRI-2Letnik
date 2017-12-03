package Dan2;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Dan02 {
    public static void main(String[] args) {
        try {


            Scanner sc = new Scanner(new File("src/Dan2/input.txt"));
            int sum = 0;
            int sum1 = 0;

            while (sc.hasNextLine()) {
                String [] s = sc.nextLine().split("\\s+");
                int[] array = Arrays.stream(s).mapToInt(Integer::parseInt).toArray();
                List<Integer> b = IntStream.of( array ).boxed().collect( Collectors.toList() );


                int min = Collections.min(b);
                int max = Collections.max(b);
                sum += (max - min);


                for (int i = 0; i < array.length; i++) {
                    for (int j = 0; j < array.length; j++) {
                        if (i != j && array[j] != 0&& array[i] % array[j] == 0)
                            sum1 += array[i]/array[j];
                    }
                }
            }

            System.out.printf("Prvi del: %d\n", sum);
            System.out.printf("Drugi del: %d", sum1);

            sc.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
