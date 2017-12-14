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
        if (trace)
            izpis(array, -1);

        if (up)
            insertionUp(array, trace);
        else
            insertionDown(array, trace);
    }

    private static void selectionSort(int[] array, boolean trace, boolean up) {
        if (up)
            selectionUp(array, trace);
        else
            selectionDown(array, trace);
    }

    private static void bubbleSort(int[] array, boolean trace, boolean up) {
        if (up)
            bubbleUp(array, trace);
        else
            bubbleDown(array, trace);
    }

    private static void heapSort(int[] array, boolean trace, boolean up) {
        if (up)
            heapUp(array, trace);
        else
            heapDown(array, trace);
    }

    private static void insertionUp(int[] array, boolean trace) {
        int locilo = 1;
        for (int i = 1; i < array.length; i++) {
            int j = i;
            while (j > 0 && array[j - 1] > array[j]) {
                swap(array, j - 1, j);
                j--;
            }
            locilo++;
            if (trace) {
                izpis(array, locilo);
            }
        }

    }

    private static void insertionDown(int[] array, boolean trace) {
        int locilo = 1;
        for (int i = 1; i < array.length; i++) {
            int j = i;
            while (j > 0 && array[j - 1] < array[j]) {
                swap(array, j - 1, j);
                j--;
            }
            locilo++;
            if (trace) {
                izpis(array, locilo);
            }
        }

    }

    private static void selectionUp(int[] array, boolean trace) {
        int locilo = 0;
        if (trace) {
            izpis(array, -1);
        }
        for (int i = 0; i < array.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[min])
                    min = j;
            }
            swap(array, i, min);
            locilo++;
            if (trace) {
                izpis(array, locilo);
            }
        }
    }

    private static void selectionDown(int[] array, boolean trace) {
        int locilo = 0;
        if (trace) {
            izpis(array, -1);
        }
        for (int i = 0; i < array.length - 1; i++) {
            int max = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] > array[max])
                    max = j;
            }
            swap(array, i, max);
            locilo++;
            if (trace) {
                izpis(array, locilo);
            }
        }
    }

    private static void bubbleUp(int[] array, boolean trace) {
        int locilo;
        if (trace) {
            izpis(array, -1);
        }
        int zadnjiSwap = 1;
        for (int i = 1; i < array.length; i++) {
            boolean u = true;
            int trenutni = 0;
            for (int j = array.length - 1; j >= zadnjiSwap; j--) {
                if (array[j - 1] > array[j]) {
                    swap(array, j - 1, j);
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
    }

    private static void bubbleDown(int[] array, boolean trace) {
        int locilo;
        if (trace) {
            izpis(array, -1);
        }
        int zadnjiSwap = 1;
        for (int i = 1; i < array.length; i++) {
            boolean u = true;
            int trenutni = 0;
            for (int j = array.length - 1; j >= zadnjiSwap; j--) {
                if (array[j - 1] < array[j]) {
                    swap(array, j - 1, j);
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
    }

    private static void heapUp(int[] array, boolean trace) {
        if (trace) {
            izpis(array, -1);
        }
        createHeap(array, true);
        for (int last = array.length - 1; last >= 0; ) {
            if (trace) {
                izpis(array, last + 1);
            }
            swap(array, 0, last);
            last--;
            maxHeap(array, 0, last);

        }
    }

    private static void heapDown(int[] array, boolean trace) {
        if (trace) {
            izpis(array, -1);
        }
        createHeap(array, false);
        for (int last = array.length - 1; last >= 0; ) {
            if (trace) {
                izpis(array, last + 1);
            }
            swap(array, 0, last);
            last--;
            minHeap(array, 0, last);
        }
    }

    private static void createHeap(int[] array, boolean max) {
        if (max) {
            for (int i = (array.length / 2) - 1; i >= 0; i--) {
                maxHeap(array, i, array.length);
            }
        } else {
            for (int i = (array.length / 2) - 1; i >= 0; i--) {
                minHeap(array, i, array.length);
            }
        }

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

    private static void mergSort(int[] array, boolean trace, boolean max) {
        if (trace)
            izpis(array, -1);

        if (max)
            mergSortUp(array, trace);
        else
            mergSortDown(array, trace);

    }

    private static void mergSortUp(int[] array, boolean trace) {
        if (array.length <= 1)
            return;
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

        mergSortUp(left, trace);
        mergSortUp(right, trace);

        mergUp(array, left, right, trace);
    }

    private static void mergUp(int[] array, int[] left, int[] right, boolean trace) {
        int[] tmp = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
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
    }

    private static void mergSortDown(int[] array, boolean trace) {
        if (array.length <= 1)
            return;
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

        mergSortDown(left, trace);
        mergSortDown(right, trace);

        mergDown(array, left, right, trace);
    }

    private static void mergDown(int[] array, int[] left, int[] right, boolean trace) {
        int[] tmp = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
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

    private static void quickSortUp(int[] array, int left, int right, boolean trace) {
        if (left >= right)
            return;
        int r = partitionUp(array, left, right);
        if (trace)
            izpis(array, r, r + 1, left, right);

        quickSortUp(array, left, r - 1, trace);
        quickSortUp(array, r + 1, right, trace);
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

    private static void quickSortDown(int[] array, int left, int right, boolean trace) {
        if (left >= right)
            return;
        int r = partitionDown(array, left, right);
        if (trace)
            izpis(array, r, r + 1, left, right);

        quickSortDown(array, left, r - 1, trace);
        quickSortDown(array, r + 1, right, trace);
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

    private static void radixUp(int[] array, boolean trace) {
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max)
                max = array[i];
        }

        if (trace)
            izpis(array, -1);

        for (int e = 1; max / e > 0; e *= 10) {
            int[] tmpArray = new int[array.length];
            int[] count = new int[10];

            for (int el : array)
                count[(el / e) % 10]++;

            for (int i = 1; i < 10; i++)
                count[i] += count[i - 1];

            for (int i = array.length - 1; i >= 0; i--) {
                tmpArray[count[((array[i] / e) % 10)] - 1] = array[i];
                count[(array[i] / e) % 10]--;
            }

            System.arraycopy(tmpArray, 0, array, 0, array.length);

            if (trace)
                izpis(array, -1);
        }
    }

    private static void radixDown(int[] array, boolean trace) {
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max)
                max = array[i];
        }

        if (trace)
            izpis(array, -1);

        for (int e = 1; max / e > 0; e *= 10) {
            int[] tmpArray = new int[array.length];
            int[] count = new int[10];

            for (int el : array)
                count[9 - (el / e) % 10]++;

            for (int i = 1; i < 10; i++)
                count[i] += count[i - 1];

            for (int i = array.length - 1; i >= 0; i--) {
                tmpArray[count[9 - ((array[i] / e) % 10)] - 1] = array[i];
                count[9 - (array[i] / e) % 10]--;
            }

            System.arraycopy(tmpArray, 0, array, 0, array.length);
            if (trace)
                izpis(array, -1);
        }
    }

    private static void bucketSort(int[] array, boolean trace, boolean up) {
        if (up)
            bucketUp(array, trace);
        else
            bucketDown(array, trace);

    }

    private static void bucketDown(int[] array, boolean trace) {
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

        int [] tmpArray = new int[array.length];
        int [] bucket = new int[k];

        for (int anArray : array) {
            int p = (int) ((anArray - min) / v);
            bucket[k - 1 - p]++;
        }

        for (int i = 1; i < bucket.length; i++)
            bucket[i] += bucket[i- 1];

        for (int i = array.length - 1; i >= 0; i--) {
            int p = (int) ((array[i] - min) / v);
            tmpArray[bucket[k - 1 - p] - 1] = array[i];
            bucket[k - 1 - p]--;
        }

        System.arraycopy(tmpArray, 0, array, 0, tmpArray.length);

        if (trace)
            izpis(array, bucket);

        insertionDown(array, trace);

    }

    private static void bucketUp(int[] array, boolean trace) {
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

        int [] tmpArray = new int[array.length];
        int [] bucket = new int[k];

        for (int anArray : array) {
            int p = (int) ((anArray - min) / v);
            bucket[p]++;
        }

        for (int i = 1; i < bucket.length; i++)
            bucket[i] += bucket[i- 1];

        for (int i = array.length - 1; i >= 0; i--) {
            int p = (int) ((array[i] - min) / v);
            tmpArray[bucket[p]-1] = array[i];
            bucket[p]--;
        }

        System.arraycopy(tmpArray, 0, array, 0, tmpArray.length);

        if (trace)
            izpis(array, bucket);

        insertionUp(array, trace);
    }

    private static void izpis(int[] array, int [] locila) {
        StringBuilder sb = new StringBuilder();
        int p = (locila.length > 1) ? 1 : 0;
        int locilo = locila[p];
        for (int k = 0; k < array.length; k++) {
            sb.append(array[k]).append(" ");
            if (locilo == k + 1) {
                sb.append("| ");
                p++;
                locilo = (p < locila.length) ? locila[p] : 0;
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
