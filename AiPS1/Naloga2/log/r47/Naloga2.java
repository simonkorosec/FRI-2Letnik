import java.util.Scanner;

public class Naloga2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] b = sc.nextLine().split(" ");
        int[] a = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            a[i] = Integer.parseInt(b[i]);
        }

        boolean trace = args[0].equals("trace");
        boolean up = args[2].equals("up");

        switch (args[1]) {
            case "insert":
                insertionSort(a, trace, up);
                break;
            case "select":
                selectionSort(a, trace, up);
                break;
            case "bubble":
                bubbleSort(a, trace, up);
                break;
            case "heap":
                heapSort(a, trace, up);
                break;
            case "merge":
                mergSort(a, trace, up);
                break;
            case "quick":
                quickSort(a, trace, up);
                break;
            case "radix":
                radixSort(a, trace, up);
                break;
            case "bucket":
                bucketSort(a, trace, up);
                break;
            default:
                break;
        }
    }

    private static void insertionSort(int[] array, boolean trace, boolean up) {
        if (trace) {
            izpis(array, -1);
            if (up)
                insertionUp(array, true);
            else
                insertionDown(array, true);
        } else {
            if (up) {
                int[] r = insertionUp(array, false);
                System.out.printf("%d %d | ", r[0], r[1]);
                r = insertionUp(array, false);
                System.out.printf("%d %d | ", r[0], r[1]);
                r = insertionDown(array, false);
                System.out.printf("%d %d", r[0], r[1]);


            } else {
                int[] r = insertionDown(array, false);
                System.out.printf("%d %d | ", r[0], r[1]);
                r = insertionDown(array, false);
                System.out.printf("%d %d | ", r[0], r[1]);
                r = insertionUp(array, false);
                System.out.printf("%d %d", r[0], r[1]);
            }
        }
    }

    private static void selectionSort(int[] array, boolean trace, boolean up) {
        if (trace) {
            if (up)
                selectionUp(array, trace);
            else
                selectionDown(array, trace);
        } else {
            if (up) {
                int[] r = selectionUp(array, false);
                System.out.printf("%d %d | ", r[0], r[1]);
                r = selectionUp(array, false);
                System.out.printf("%d %d | ", r[0], r[1]);
                r = selectionDown(array, false);
                System.out.printf("%d %d", r[0], r[1]);

            } else {
                int[] r = selectionDown(array, false);
                System.out.printf("%d %d | ", r[0], r[1]);
                r = selectionDown(array, false);
                System.out.printf("%d %d | ", r[0], r[1]);
                r = selectionUp(array, false);
                System.out.printf("%d %d", r[0], r[1]);

            }
        }
    }

    private static void bubbleSort(int[] array, boolean trace, boolean up) {
        if (trace) {
            if (up)
                bubbleUp(array, trace);
            else
                bubbleDown(array, trace);
        } else {
            if (up) {
                int[] r = bubbleUp(array, false);
                System.out.printf("%d %d | ", r[0], r[1]);
                r = bubbleUp(array, false);
                System.out.printf("%d %d | ", r[0], r[1]);
                r = bubbleDown(array, false);
                System.out.printf("%d %d", r[0], r[1]);
            } else {
                int[] r = bubbleDown(array, false);
                System.out.printf("%d %d | ", r[0], r[1]);
                r = bubbleDown(array, false);
                System.out.printf("%d %d | ", r[0], r[1]);
                r = bubbleUp(array, false);
                System.out.printf("%d %d", r[0], r[1]);
            }
        }
    }

    private static void heapSort(int[] array, boolean trace, boolean up) {
        if (up)
            heapSortUp(array, trace);
        else
            heapSortDown(array, trace);
    }

    private static void mergSort(int[] array, boolean trace, boolean max) {
        if (trace)
            izpis(array, -1);

        if (max)
            mergSortUp(array, trace);
        else
            mergSortDown(array, trace);

    }

    private static void quickSort(int[] array, boolean trace, boolean max) {
        if (trace)
            izpis(array, -1);

        if (max)
            quickSortUp(array, 0, array.length - 1, trace);
        else
            quickSortDown(array, 0, array.length - 1, trace);

        if (trace)
            izpis(array, -1);

    }

    private static void bucketSort(int[] array, boolean trace, boolean up) {
        if (up)
            bucketUp(array, trace);
        else
            bucketDown(array, trace);

    }

    private static int[] insertionUp(int[] array, boolean trace) {
        int locilo = 1;
        int[] rez = new int[]{0, 0};    // rez[0] -> Premiki  | rez[1] -> Primerjave
        for (int i = 1; i < array.length; i++) {
            int j = i;
            while (j > 0 && array[j - 1] > array[j]) {
                rez[1]++;
                swap(array, j - 1, j);
                rez[0] += 3;
                j--;
            }
            rez[1]++;
            locilo++;
            if (trace) {
                izpis(array, locilo);
            }
        }

        return rez;
    }

    private static int[] insertionDown(int[] array, boolean trace) {
        int locilo = 1;
        int[] rez = new int[]{0, 0};    // rez[0] -> Premiki  | rez[1] -> Primerjave
        for (int i = 1; i < array.length; i++) {
            int j = i;
            while (j > 0 && array[j - 1] < array[j]) {
                rez[1]++;
                swap(array, j - 1, j);
                rez[0] += 3;
                j--;
            }
            locilo++;
            if (trace) {
                izpis(array, locilo);
            }
        }

        return rez;
    }

    private static int[] selectionUp(int[] array, boolean trace) {
        int[] rez = new int[]{0, 0};    // rez[0] -> Premiki  | rez[1] -> Primerjave
        int locilo = 0;
        if (trace) {
            izpis(array, -1);
        }
        for (int i = 0; i < array.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < array.length; j++) {
                rez[1]++;
                if (array[j] < array[min])
                    min = j;
            }
            swap(array, i, min);
            rez[0] += 3;
            locilo++;
            if (trace) {
                izpis(array, locilo);
            }
        }
        return rez;
    }

    private static int[] selectionDown(int[] array, boolean trace) {
        int[] rez = new int[]{0, 0};    // rez[0] -> Premiki  | rez[1] -> Primerjave
        int locilo = 0;
        if (trace) {
            izpis(array, -1);
        }
        for (int i = 0; i < array.length - 1; i++) {
            int max = i;
            for (int j = i + 1; j < array.length; j++) {
                rez[1]++;
                if (array[j] > array[max])
                    max = j;
            }
            swap(array, i, max);
            rez[0] += 3;
            locilo++;
            if (trace) {
                izpis(array, locilo);
            }
        }
        return rez;
    }

    private static int[] bubbleUp(int[] array, boolean trace) {
        int[] rez = new int[]{0, 0};    // rez[0] -> Premiki  | rez[1] -> Primerjave
        int locilo;
        if (trace) {
            izpis(array, -1);
        }
        int zadnjiSwap = 1;
        for (int i = 1; i < array.length; i++) {
            boolean u = true;
            int trenutni = 0;
            for (int j = array.length - 1; j >= zadnjiSwap; j--) {
                rez[1]++;
                if (array[j - 1] > array[j]) {
                    swap(array, j - 1, j);
                    rez[0] += 3;
                    trenutni = j;
                    u = false;
                }
            }
            if (u) continue;
            zadnjiSwap = trenutni;
            locilo = zadnjiSwap;
            if (trace) {
                izpis(array, locilo);
            }
        }
        return rez;
    }

    private static int[] bubbleDown(int[] array, boolean trace) {
        int[] rez = new int[]{0, 0};    // rez[0] -> Premiki  | rez[1] -> Primerjave
        int locilo;
        if (trace) {
            izpis(array, -1);
        }
        int zadnjiSwap = 1;
        for (int i = 1; i < array.length; i++) {
            boolean u = true;
            int trenutni = 0;
            for (int j = array.length - 1; j >= zadnjiSwap; j--) {
                rez[1]++;
                if (array[j - 1] < array[j]) {
                    swap(array, j - 1, j);
                    rez[0] += 3;
                    trenutni = j;
                    u = false;
                }
            }
            if (u) continue;
            zadnjiSwap = trenutni;
            locilo = zadnjiSwap;
            if (trace) {
                izpis(array, locilo);
            }
        }
        return rez;
    }

    private static int[] heapSortUp(int[] array, boolean trace) {
        if (trace) {
            izpis(array, -1);
        }

        int[] rez = new int[]{0, 0};    // rez[0] -> Premiki  | rez[1] -> Primerjave

        for (int i = (array.length / 2) - 1; i >= 0; i--) {
            maxHeap(array, i, array.length);
        }

        for (int last = array.length - 1; last >= 0; last--) {
            if (trace) {
                izpis(array, last + 1);
            }
            swap(array, 0, last);
            rez[0] += 3;
            maxHeap(array, 0, last);

        }

        return rez;
    }

    private static int[] heapSortDown(int[] array, boolean trace) {
        if (trace) {
            izpis(array, -1);
        }

        int[] rez = new int[]{0, 0};    // rez[0] -> Premiki  | rez[1] -> Primerjave

        for (int i = (array.length / 2) - 1; i >= 0; i--) {
            minHeap(array, i, array.length);
        }
        for (int last = array.length - 1; last >= 0; last--) {
            if (trace) {
                izpis(array, last + 1);
            }
            swap(array, 0, last);
            rez[0] += 3;
            minHeap(array, 0, last);
        }

        return rez;
    }

    private static void maxHeap(int[] array, int i, int velikost) {
        int root = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < velikost && array[root] < array[left])
            root = left;
        if (right < velikost && array[root] < array[right])
            root = right;

        if (root != i) {
            swap(array, i, root);
            maxHeap(array, root, velikost);
        }
    }

    private static void minHeap(int[] array, int i, int velikost) {
        int root = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < velikost && array[root] > array[left])
            root = left;
        if (right < velikost && array[root] > array[right])
            root = right;

        if (root != i) {
            swap(array, i, root);
            minHeap(array, root, velikost);
        }
    }

    private static int[] mergSortUp(int[] array, boolean trace) {
        int[] rez = new int[]{0, 0};    // rez[0] -> Premiki  | rez[1] -> Primerjave

        if (array.length > 1) {
            int mid;
            if (array.length % 2 == 0)
                mid = array.length / 2;
            else
                mid = array.length / 2 + 1;
            if (trace)
                izpis(array, mid);

            int[] left = new int[mid];
            int[] right = new int[array.length - mid];
            System.arraycopy(array, 0, left, 0, mid);
            System.arraycopy(array, mid, right, 0, right.length);


            int[] t = mergSortUp(left, trace);
            rez[0] += t[0];
            rez[1] += t[1];
            t = mergSortUp(right, trace);
            rez[0] += t[0];
            rez[1] += t[1];

            t = mergUp(array, left, right, trace);
            rez[0] += t[0];
            rez[1] += t[1];
        }
        return rez;
    }

    private static int[] mergUp(int[] array, int[] left, int[] right, boolean trace) {
        int[] tmp = new int[left.length + right.length];
        int[] rez = new int[]{0, 0};    // rez[0] -> Premiki  | rez[1] -> Primerjave
        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
            rez[1]++;
            if (left[i] <= right[j])
                tmp[k++] = left[i++];
            else
                tmp[k++] = right[j++];
        }
        while (i < left.length)
            tmp[k++] = left[i++];
        while (j < right.length)
            tmp[k++] = right[j++];

        System.arraycopy(tmp, 0, array, 0, array.length);
        if (trace)
            izpis(array, -1);
        return rez;
    }

    private static int[] mergSortDown(int[] array, boolean trace) {
        int[] rez = new int[]{0, 0};    // rez[0] -> Premiki  | rez[1] -> Primerjave

        if (array.length > 1) {
            int mid;
            if (array.length % 2 == 0)
                mid = array.length / 2;
            else
                mid = array.length / 2 + 1;
            if (trace)
                izpis(array, mid);

            int[] left = new int[mid];
            int[] right = new int[array.length - mid];
            System.arraycopy(array, 0, left, 0, mid);
            System.arraycopy(array, mid, right, 0, right.length);

            int[] t = mergSortDown(left, trace);
            rez[0] += t[0];
            rez[1] += t[1];
            t = mergSortDown(right, trace);
            rez[0] += t[0];
            rez[1] += t[1];

            t = mergDown(array, left, right, trace);
            rez[0] += t[0];
            rez[1] += t[1];

        }
        return rez;
    }

    private static int[] mergDown(int[] array, int[] left, int[] right, boolean trace) {
        int[] tmp = new int[left.length + right.length];
        int[] rez = new int[]{0, 0};    // rez[0] -> Premiki  | rez[1] -> Primerjave
        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
            rez[1]++;
            if (left[i] >= right[j])
                tmp[k++] = left[i++];
            else
                tmp[k++] = right[j++];
        }
        while (i < left.length)
            tmp[k++] = left[i++];
        while (j < right.length)
            tmp[k++] = right[j++];

        System.arraycopy(tmp, 0, array, 0, array.length);
        if (trace)
            izpis(array, -1);
        return rez;
    }

    private static int[] quickSortUp(int[] array, int left, int right, boolean trace) {
        int[] rez = new int[]{0, 0};    // rez[0] -> Premiki  | rez[1] -> Primerjave

        if (left >= right)
            return rez;
        int r = partitionUp(array, left, right);
        if (trace)
            izpis(array, r, r + 1, left, right);

        int[] t = quickSortUp(array, left, r - 1, trace);
        rez[0] += t[0];
        rez[1] += t[1];

        t = quickSortUp(array, r + 1, right, trace);
        rez[0] += t[0];
        rez[1] += t[1];

        return rez;
    }

    private static int partitionUp(int[] array, int left, int right) {
        int p = array[left];
        int l = left, r = right + 1;
        while (true) {
            do {
                l++;
            } while (l < right && array[l] < p);
            do {
                r--;
            } while (array[r] > p);
            if (l >= r)
                break;
            swap(array, l, r);
        }
        swap(array, left, r);
        return r;
    }

    private static int[] quickSortDown(int[] array, int left, int right, boolean trace) {
        int[] rez = new int[]{0, 0};    // rez[0] -> Premiki  | rez[1] -> Primerjave

        if (left >= right)
            return rez;
        int r = partitionDown(array, left, right);
        if (trace)
            izpis(array, r, r + 1, left, right);

        int[] t = quickSortDown(array, left, r - 1, trace);
        rez[0] += t[0];
        rez[1] += t[1];

        t = quickSortDown(array, r + 1, right, trace);
        rez[0] += t[0];
        rez[1] += t[1];
        return rez;
    }

    private static int partitionDown(int[] array, int left, int right) {
        int p = array[left];
        int l = left, r = right + 1;
        while (true) {
            do {
                l++;
            } while (l < right && array[l] > p);
            do {
                r--;
            } while (array[r] < p);
            if (l >= r)
                break;
            swap(array, l, r);
        }
        swap(array, left, r);
        return r;
    }

    private static void radixSort(int[] array, boolean trace, boolean up) {
        if (up)
            radixUp(array, trace);
        else
            radixDown(array, trace);

    }

    private static int[] radixUp(int[] array, boolean trace) {
        int[] rez = new int[]{0, 0};    // rez[0] -> Premiki  | rez[1] -> Primerjave

        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            rez[1]++;
            if (array[i] > max)
                max = array[i];
        }

        if (trace)
            izpis(array, -1);

        for (int e = 1; max / e > 0; e *= 10) {
            int[] tmpArray = new int[array.length];
            int[] count = new int[10];

            for (int el : array) {
                count[(el / e) % 10]++;
                rez[1]++;
            }

            for (int i = 1; i < 10; i++)
                count[i] += count[i - 1];

            for (int i = array.length - 1; i >= 0; i--) {
                tmpArray[count[((array[i] / e) % 10)] - 1] = array[i];
                rez[0]++;
                count[(array[i] / e) % 10]--;
            }

            System.arraycopy(tmpArray, 0, array, 0, array.length);
            rez[0] += array.length;

            if (trace)
                izpis(array, -1);
        }

        return rez;
    }

    private static int[] radixDown(int[] array, boolean trace) {
        int[] rez = new int[]{0, 0};    // rez[0] -> Premiki  | rez[1] -> Primerjave

        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            rez[1]++;

            if (array[i] > max)
                max = array[i];
        }

        if (trace)
            izpis(array, -1);

        for (int e = 1; max / e > 0; e *= 10) {
            int[] tmpArray = new int[array.length];
            int[] count = new int[10];

            for (int el : array) {
                count[9 - (el / e) % 10]++;
                rez[1]++;
            }

            for (int i = 1; i < 10; i++)
                count[i] += count[i - 1];

            for (int i = array.length - 1; i >= 0; i--) {
                tmpArray[count[9 - ((array[i] / e) % 10)] - 1] = array[i];
                rez[0]++;
                count[9 - (array[i] / e) % 10]--;
            }

            System.arraycopy(tmpArray, 0, array, 0, array.length);
            rez[0] += array.length;

            if (trace)
                izpis(array, -1);
        }

        return rez;
    }

    private static int[] bucketDown(int[] array, boolean trace) {
        int[] rez = new int[]{0, 0};    // rez[0] -> Premiki  | rez[1] -> Primerjave

        if (trace)
            izpis(array, -1);

        int max = array[0];
        int min = array[0];
        for (int i = 1; i < array.length; i++) {
            rez[1] += 2;
            if (array[i] > max)
                max = array[i];
            if (array[i] < min)
                min = array[i];
        }

        int k = array.length / 2;
        double v = (max - min + 1.0) / k;

        int[] tmpArray = new int[array.length];
        int[] bucket = new int[k];

        for (int anArray : array) {
            int p = (int) ((anArray - min) / v);
            bucket[k - 1 - p]++;
            rez[1]++;
        }

        for (int i = 1; i < bucket.length; i++)
            bucket[i] += bucket[i - 1];

        for (int i = array.length - 1; i >= 0; i--) {
            int p = (int) ((array[i] - min) / v);
            tmpArray[bucket[k - 1 - p] - 1] = array[i];
            rez[0]++;
            bucket[k - 1 - p]--;
        }

        System.arraycopy(tmpArray, 0, array, 0, tmpArray.length);
        rez[0] += tmpArray.length;

        if (trace)
            izpis(array, bucket);

        int[] t = insertionDown(array, trace);
        rez[0] += t[0];
        rez[1] += t[1];

        return rez;
    }

    private static int[] bucketUp(int[] array, boolean trace) {
        int[] rez = new int[]{0, 0};    // rez[0] -> Premiki  | rez[1] -> Primerjave

        if (trace)
            izpis(array, -1);

        int max = array[0];
        int min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max)
                max = array[i];
            if (array[i] < min)
                min = array[i];
        }

        int k = array.length / 2;
        double v = (max - min + 1.0) / k;

        int[] tmpArray = new int[array.length];
        int[] bucket = new int[k];

        for (int anArray : array) {
            int p = (int) ((anArray - min) / v);
            bucket[p]++;
            rez[1]++;
        }

        for (int i = 1; i < bucket.length; i++)
            bucket[i] += bucket[i - 1];

        for (int i = array.length - 1; i >= 0; i--) {
            int p = (int) ((array[i] - min) / v);
            tmpArray[bucket[p] - 1] = array[i];
            rez[0]++;
            bucket[p]--;
        }

        System.arraycopy(tmpArray, 0, array, 0, tmpArray.length);
        rez[0] += tmpArray.length;

        if (trace)
            izpis(array, bucket);

        int[] t = insertionUp(array, trace);
        rez[0] += t[0];
        rez[1] += t[1];

        return rez;
    }

    private static void izpis(int[] array, int[] locila) {
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < array.length; k++) {
            sb.append(array[k]).append(" ");

            for (int locilo : locila) {
                if (locilo == k + 1) {
                    sb.append("| ");
                }
            }
        }
        System.out.println(sb.toString().trim());
    }

    private static void izpis(int[] array, int locilo) {
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < array.length; k++) {
            sb.append(array[k]).append(" ");
            if (locilo == k + 1)
                sb.append("| ");
        }
        System.out.println(sb.toString().trim());
    }

    private static void izpis(int[] array, int locilo1, int locilo2, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int k = start; k <= end; k++) {
            if (locilo1 == k)
                sb.append("| ");
            sb.append(array[k]).append(" ");
            if (locilo2 == k + 1)
                sb.append("| ");
        }
        System.out.println(sb.toString().trim());
    }

    private static void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

}
